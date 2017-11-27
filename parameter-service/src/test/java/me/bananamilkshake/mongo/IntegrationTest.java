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
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class IntegrationTest {

	private static final String LOCALHOST = "127.0.0.1";
	private static final int MONGO_TEST_PORT = 27028;

	private static final String DB_NAME = "test";

	private static MongodProcess mongoProcess;
	private static MongoClient mongoClient;

	private MongoTemplate mongoTemplate;

	@Autowired
	private MockMvc mockMvc;

	@BeforeClass
	public static void initialize() throws IOException {
		mongoProcess = createMongoProcess();
		mongoClient = new MongoClient(LOCALHOST, MONGO_TEST_PORT);
	}

	@Before
	public void setUp() throws Exception {
		mongoTemplate = new MongoTemplate(mongoClient, DB_NAME);
	}

	@After
	public void tearDown() throws Exception {
		mongoTemplate.getCollectionNames().forEach(collectionName -> mongoTemplate.dropCollection(collectionName));
	}

	@AfterClass
	public static void shutdown() throws InterruptedException {
		if (nonNull(mongoClient)) {
			mongoClient.close();
		}

		if (nonNull(mongoProcess)) {
			mongoProcess.stop();
		}
	}

	@Test
	public void shouldCreateParameterIfNoneExists() {
	}

	@Test
	public void shouldNotCreateParameterIfNameIsAlreadyUsed() {
	}

	@Test
	public void shouldCreateParameterIfValidatorIsCorrect() {
	}

	@Test
	public void shouldNotCreateParameterIfValidatorIsIncorrect() {
	}

	@Test
	public void shouldReturnListOfParametersOnRequest() {
	}

	@Test
	public void shouldReturnNoContentOnGetIfParameterWithSuchNameDoesNotExists() {
	}

	@Test
	public void shouldReturnGeneralWhenParameterIfNotSetForSpecifiedUser() {
	}

	@Test
	public void shouldReturnEmptyListIfNoParametersForSpecifiedDate() {
	}

	@Test
	public void shouldUploadCorrectParameters() {
	}

	@Test
	public void shouldReturnNoContentOnUploadIfParameterWithSuchNameDoesNotExists() {
	}

	@Test
	public void shouldReturnBadRequestOnInvalidParametersUpload() {
	}

	private void givenParameterExists(String name) {
		DBCollection collection = mongoTemplate.createCollection(name);
		assertThat(collection).isNotNull().as("Collection not created");
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

	private static MongodProcess createMongoProcess() throws IOException {
		return createStarter().prepare(createMongoConfig()).start();
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
