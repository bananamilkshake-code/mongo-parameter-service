package me.bananamilkshake.mongo.integration;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import me.bananamilkshake.mongo.MongoApplication;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PreDestroy;
import java.io.IOException;

import static java.util.Objects.nonNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {AbstractIntegrationTest.EmbeddedMongoConfiguration.class})
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected MockMvc mockMvc;


    @After
    public void tearDown() throws Exception {
        mongoTemplate.getCollectionNames().forEach(collectionName -> mongoTemplate.dropCollection(collectionName));
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
