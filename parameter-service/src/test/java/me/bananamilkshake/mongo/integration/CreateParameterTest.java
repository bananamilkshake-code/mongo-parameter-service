package me.bananamilkshake.mongo.integration;

import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateParameterTest extends AbstractIntegrationTest {

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
    public void should_create_parameter_if_index_is_correct() {
    }

    @Test
    public void should_not_create_parameter_if_index_is_incorrect() {
    }

    @Test
    public void should_create_parameter_if_validator_is_correct() {
    }

    @Test
    public void should_not_create_parameter_if_validator_is_incorrect() {
    }

    private String pathToParameter(String type) {
        return "/parameter/" + type;
    }

    private void givenParameterExists(String name) {
        MongoCollection collection = mongoTemplate.createCollection(name);
        assertThat(collection).as("Collection not created").isNotNull();
    }

    private void givenParameterNotExists(String type) {
        assertThat(mongoTemplate.collectionExists(type)).isFalse();
    }
}