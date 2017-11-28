package me.bananamilkshake.mongo;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PreDestroy;
import java.io.IOException;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {IntegrationTest.EmbeddedMongoConfiguration.class})
public class IntegrationTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MockMvc mockMvc;


	@After
	public void tearDown() throws Exception {
		mongoTemplate.getCollectionNames().forEach(collectionName -> mongoTemplate.dropCollection(collectionName));
	}

	@Test
	public void should_create_parameter_if_none_exists() throws Exception {

		// given
		String type = "some_type";
		String emptyParameterValidation = "{}";
		givenParameterNotExists(type);

		// when
		mockMvc.perform(post(pathToParameter(type))
								.content(emptyParameterValidation)
								.contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isCreated());

		// then
		assertThat(mongoTemplate.collectionExists(type)).isTrue();
	}

	@Test
	public void should_not_create_parameter_if_name_is_already_used() throws Exception {

		// given
		String type = "some_type";
		String emptyParameterValidation = "{}";
		givenParameterExists(type);

		// when
		mockMvc.perform(post(pathToParameter(type))
								.content(emptyParameterValidation)
								.contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isBadRequest());

		// then
		assertThat(mongoTemplate.collectionExists(type)).isTrue();
	}

	@Test
	public void should_create_parameter_if_validator_is_correct() {
	}

	@Test
	public void should_not_create_parameter_if_validator_is_incorrect() {
	}

	@Test
	public void should_return_list_of_parameters_on_request() {
	}

	@Test
	public void should_return_no_content_on_get_if_parameter_with_such_name_does_not_exists() {
	}

	@Test
	public void should_return_general_when_parameter_if_not_set_for_specified_user() {
	}

	@Test
	public void should_return_empty_list_if_no_parameters_for_specified_date() {
	}

	@Test
	public void should_upload_correct_parameters() {
	}

	@Test
	public void should_return_no_content_on_upload_if_parameter_with_such_name_does_not_exists() {
	}

	@Test
	public void should_return_bad_request_on_invalid_parameters_upload() {
	}

	private String pathToParameter(String type) {
		return "/parameter/" + type;
	}

	private void givenParameterExists(String name, String validation) {
		givenParameterExists(name);

		final CommandResult commandResult = mongoTemplate.executeCommand(
				"{" +
				"    collMod: \"" + name + "\"," +
				"    validator: {" + validation + "}" +
				"}");

		assertThat(commandResult.ok()).isTrue().as("Failed to setup parameter validation");
	}

	private void givenParameterExists(String name) {
		DBCollection collection = mongoTemplate.createCollection(name);
		assertThat(collection).isNotNull().as("Collection not created");
	}

	private void givenParameterNotExists(String type) {
		assertThat(mongoTemplate.collectionExists(type)).isFalse();
	}

	@Configuration
	@Import(MongoApplication.class)
	public static class EmbeddedMongoConfiguration {

		private static final String LOCALHOST = "127.0.0.1";
		private static final int MONGO_TEST_PORT = 27028;

		private static final String DB_NAME = "test";

		@Autowired
		private MongoClient mongoClient;

		@Autowired
		private MongodProcess mongodProcess;

		@Bean
		@Primary
		public MongoTemplate mongoTemplate(MongoClient mongoClient) {
			return new MongoTemplate(mongoClient, DB_NAME);
		}

		@Bean
		public MongodProcess mongodProcess() throws IOException {
			return createStarter().prepare(createMongoConfig()).start();
		}

		@Bean
		public MongoClient mongoClient() {
			return new MongoClient(LOCALHOST, MONGO_TEST_PORT);
		}

		@PreDestroy
		public void shutdown() {
			if (nonNull(mongoClient)) {
				mongoClient.close();
			}

			if (nonNull(mongodProcess)) {
				mongodProcess.stop();
			}
		}

		private static MongodStarter createStarter() {
			return MongodStarter.getDefaultInstance();
		}

		private static IMongodConfig createMongoConfig() throws IOException {
			return new MongodConfigBuilder()
					.version(Version.V3_3_1)
					.net(new Net(LOCALHOST, MONGO_TEST_PORT, false))
					.build();
		}
	}
}
