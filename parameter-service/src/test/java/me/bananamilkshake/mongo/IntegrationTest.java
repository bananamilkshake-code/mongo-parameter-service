package me.bananamilkshake.mongo;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;

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
}
