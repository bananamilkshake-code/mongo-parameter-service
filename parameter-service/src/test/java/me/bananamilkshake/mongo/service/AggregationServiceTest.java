package me.bananamilkshake.mongo.service;

import com.mongodb.*;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.exception.NoParameterWithSuchArgumentsExistsException;
import me.bananamilkshake.mongo.exception.NoSuchParameterExistsException;
import me.bananamilkshake.mongo.service.query.AggregationFilterCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AggregationServiceTest {

	@Mock
	private AggregationFilterCreator aggregationFilterCreator;

	@Mock
	private UtcConverter utcConverter;

	@Mock
	private MongoTemplate mongoTemplate;

	@Test
	public void should_return_found_parameter() {

		// given
		String type = "Existing type";
		String user = "Some user";
		ZonedDateTime date = ZonedDateTime.of(2017, 11, 28, 0, 0, 0, 0, ZoneId.of("UTC"));

		Parameter existingParameter = givenParameterValuesExist(type, user, date);

		// when
		Parameter parameter = aggregationService().aggregate(type, user, date);

		// then
		assertThat(parameter).isEqualToComparingFieldByField(existingParameter);
	}

	@Test
	public void should_throw_NoSuchParameterExistsException_if_there_is_no_parameter_with_specified_name() {

		// given
		String type = "Non existing type";
		String user = "Some user";
		ZonedDateTime date = ZonedDateTime.of(2017, 11, 28, 0, 0, 0, 0, ZoneId.of("UTC"));

		givenNoParameterExists(type);

		// when
		Throwable throwable = catchThrowable(() -> aggregationService().aggregate(type, user, date));

		// then
		assertThat(throwable).isNotNull()
							 .isInstanceOf(NoSuchParameterExistsException.class);
	}

	@Test
	public void should_throw_NoParameterWithSuchArgumentsExistsException_if_there_is_no_values_with_specified_arguments() {

		// given
		String type = "Existing type";
		String user = "Some user";
		ZonedDateTime date = ZonedDateTime.of(2017, 11, 28, 0, 0, 0, 0, ZoneId.of("UTC"));

		givenNoValuesForArgumentsExist(type, user, date);

		// when
		Throwable throwable = catchThrowable(() -> aggregationService().aggregate(type, user, date));

		// then
		assertThat(throwable).isNotNull()
							 .isInstanceOf(NoParameterWithSuchArgumentsExistsException.class);
	}

	private AggregationService aggregationService() {
		return new AggregationService(aggregationFilterCreator, utcConverter, mongoTemplate);
	}

	private void givenParameterExists(String type) {
		when(mongoTemplate.collectionExists(eq(type))).thenReturn(true);
	}

	private void givenNoParameterExists(String type) {
		when(mongoTemplate.collectionExists(eq(type))).thenReturn(false);
	}

	private Parameter givenParameterValuesExist(String type, String user, ZonedDateTime date) {
		givenParameterExists(type);

		Parameter parameter = new Parameter(user, converted(date), newArrayList());

		when(mongoTemplate.execute(eq(type), any()))
				.thenAnswer(invocation -> executeAndReturn(invocation, user, date, output(parameter)));

		return parameter;
	}

	private void givenNoValuesForArgumentsExist(String type, String user, ZonedDateTime date) {
		givenParameterExists(type);

		when(mongoTemplate.execute(eq(type), any()))
				.thenAnswer(invocation -> executeAndReturn(invocation, user, date, emptyOutput()));
	}

	private Parameter executeAndReturn(InvocationOnMock invocation,
									   String user,
									   ZonedDateTime date,
									   AggregationOutput aggregationOutput) {
		return callback(invocation).doInCollection(collection(user, converted(date), aggregationOutput));
	}

	private AggregationOutput emptyOutput() {
		return mockAggregationOutputWith(newArrayList());
	}

	private AggregationOutput output(Parameter... parameters) {
		MongoConverter mongoConverter = mock(MongoConverter.class);

		List<DBObject> dbObjects = mockMongoConverterFor(mongoConverter, parameters);

		return mockAggregationOutputWith(dbObjects);
	}

	private LocalDateTime converted(ZonedDateTime zonedDateTime) {
		LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
		when(utcConverter.convertToUtc(eq(zonedDateTime))).thenReturn(localDateTime);
		return localDateTime;
	}

	private CollectionCallback<Parameter> callback(InvocationOnMock invocation) {
		return ((CollectionCallback<Parameter>) invocation.getArguments()[1]);
	}

	private DBCollection collection(String user, LocalDateTime date, AggregationOutput aggregationOutput) {
		when(aggregationFilterCreator.create(eq(user), eq(date))).thenReturn(newArrayList());

		DBCollection collection = mock(DBCollection.class);
		when(collection.aggregate(any())).thenReturn(aggregationOutput);
		return collection;
	}

	private AggregationOutput mockAggregationOutputWith(List<DBObject> parameters) {
		AggregationOutput aggregationOutput = mock(AggregationOutput.class);
		when(aggregationOutput.results()).thenReturn(parameters);
		return aggregationOutput;
	}

	private List<DBObject> mockMongoConverterFor(MongoConverter mongoConverter, Parameter... parameters) {
		when(mongoTemplate.getConverter()).thenReturn(mongoConverter);

		List<DBObject> dbObjects = newArrayList();
		for (Parameter parameter : parameters) {
			DBObject parameterDbObject = new BasicDBObject();
			when(mongoConverter.read(any(), eq(parameterDbObject))).thenReturn(parameter);
			dbObjects.add(parameterDbObject);
		}
		return dbObjects;
	}
}