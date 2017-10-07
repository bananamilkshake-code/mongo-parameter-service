package me.bananamilkshake.mongo.service;

import com.mongodb.CommandResult;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import me.bananamilkshake.mongo.service.index.IndexSetupService;
import me.bananamilkshake.mongo.service.query.QueryCreator;
import me.bananamilkshake.mongo.service.validation.ValidationSetupService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(MockitoJUnitRunner.class)
public class ParameterServiceTest {

	private static final String LOCALHOST = "127.0.0.1";
	private static final int MONGO_TEST_PORT = 27028;

	private static final String DB_NAME = "test";

	private static final String PARAMETER_NAME = "someParameter";

	private static MongodProcess mongoProcess;
	private static MongoClient mongoClient;

	private MongoTemplate mongoTemplate;

	@Mock
	private QueryCreator queryCreator;

	@Mock
	private ValidationSetupService validationSetupService;

	@Mock
	private IndexSetupService indexSetupService;

	private ParameterService parameterService;

	@BeforeClass
	public static void initialize() throws IOException {
		mongoProcess = createMongoProcess();
		mongoClient = new MongoClient(LOCALHOST, MONGO_TEST_PORT);
	}

	@Before
	public void setUp() throws Exception {
		mongoTemplate = new MongoTemplate(mongoClient, DB_NAME);

		mongoTemplate.createCollection(PARAMETER_NAME);
		final CommandResult commandResult = mongoTemplate.executeCommand(
				"{" +
				"    collMod: \"" + PARAMETER_NAME + "\"," +
				"    validator: {" +
				"        $and: [" +
				"            {" +
				"                width: {" +
				"                    $exists: \"true\"," +
				"                    $type: \"number\"," +
				"                    $gte: 0" +
				"                }," +
				"                height: {" +
				"                    $exists: \"true\"," +
				"                    $type: \"number\"," +
				"                    $gte: 0" +
				"                }" +
				"            }" +
				"        ]" +
				"    }" +
				"}");
		assertThat(commandResult.ok()).isTrue();

		parameterService = new ParameterServiceImpl(mongoTemplate, queryCreator, validationSetupService, indexSetupService);
	}

	@After
	public void tearDown() throws Exception {
		mongoTemplate.dropCollection(PARAMETER_NAME);
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
	public void shouldAcceptValidParameter() {

		// given
		final String parameters =
				"[" +
				"    {" +
				"        user: \"SizeCompanyInc\"," +
				"        validFrom: new Date(\"2017-01-01\"), " +
				"        validTo: new Date(\"2017-12-31\"), " +
				"        width: -1," +
				"        height: \"hello\"" +
				"    }" +
				"]";

		// when
		Throwable thrown = catchThrowable(() -> parameterService.uploadParameters(PARAMETER_NAME, parameters));

		// then
		assertThat(thrown).isNull();
	}

	@Test
	public void shouldNotAcceptInvalidParameters() {

		// given
		final String invalidParameters =
				"[" +
				"    {" +
				"        width: -1," +
				"        height: \"hello\"" +
				"    }" +
				"]";

		// when
		Throwable thrown = catchThrowable(() -> parameterService.uploadParameters(PARAMETER_NAME, invalidParameters));

		// then
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
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
