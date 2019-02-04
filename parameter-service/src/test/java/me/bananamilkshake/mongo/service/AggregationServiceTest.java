package me.bananamilkshake.mongo.service;

import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.exception.NoParameterWithSuchArgumentsExistsException;
import me.bananamilkshake.mongo.exception.NoSuchParameterExistsException;
import me.bananamilkshake.mongo.service.query.AggregationFilterCreator;
import org.bson.Document;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AggregationServiceTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

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
									   AggregateIterable<Parameter> aggregateIterable) {
		return callback(invocation).doInCollection(collection(user, converted(date), aggregateIterable));
	}

	private AggregateIterable<Parameter> emptyOutput() {
		return mockAggregationOutputWith(newArrayList());
	}

	private AggregateIterable<Parameter> output(Parameter... parameters) {
		return mockAggregationOutputWith(newArrayList(parameters));
	}

	private LocalDateTime converted(ZonedDateTime zonedDateTime) {
		LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
		when(utcConverter.convertToUtc(eq(zonedDateTime))).thenReturn(localDateTime);
		return localDateTime;
	}

	private CollectionCallback<Parameter> callback(InvocationOnMock invocation) {
		return invocation.getArgument(1);
	}

	@Mock
	public MongoCollection<Document> collection;

	private MongoCollection<Document> collection(String user, LocalDateTime date, AggregateIterable<Parameter> aggregateIterable) {
		when(aggregationFilterCreator.create(eq(user), eq(date))).thenReturn(newArrayList());

		when(collection.aggregate(any(), eq(Parameter.class))).thenReturn(aggregateIterable);
		return collection;
	}

	@Mock
	public AggregateIterable<Parameter> aggregationOutput;

	private AggregateIterable<Parameter> mockAggregationOutputWith(List<Parameter> parameters) {
		MongoCursor<Parameter> mongoCursor = new MongoCursor<Parameter>() {

			private Iterator<Parameter> parametersIterator = parameters.iterator();

			@Override
			public void close() {}

			@Override
			public boolean hasNext() {
				return parametersIterator.hasNext();
			}

			@Override
			public Parameter next() {
				return parametersIterator.next();
			}

			@Override
			public Parameter tryNext() {
				return null;
			}

			@Override
			public ServerCursor getServerCursor() {
				return null;
			}

			@Override
			public ServerAddress getServerAddress() {
				return null;
			}
		};
		when(aggregationOutput.iterator()).thenReturn(mongoCursor);
		return aggregationOutput;
	}
}