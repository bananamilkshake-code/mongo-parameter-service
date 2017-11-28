package me.bananamilkshake.mongo.service.convert;

import com.google.common.collect.ImmutableMap;
import ma.glasnost.orika.MapperFacade;
import me.bananamilkshake.mongo.configuration.OrikaConfiguration;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.dto.ParameterDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = OrikaConfiguration.class)
public class ParameterConverterTest {

	@Autowired
	private MapperFacade mapperFacade;

	@Test
	public void should_map_all_fields_of_parameter_to_corresponding_fields_of_parameter_dto() {

		// given
		String type = "Type";
		LocalDateTime validFrom = LocalDateTime.of(2017, 5, 6, 0, 0);
		List<Map<String, Object>> values = newArrayList(
				value("Name1", 9),
				value("Name2", 7),
				value("Name3", 4));

		Parameter parameter = new Parameter(type, validFrom, values);

		// when
		ParameterDto dto = mapperFacade.map(parameter, ParameterDto.class);

		// then
		assertThat(dto).isEqualToComparingFieldByField(parameter);
	}

	private Map<String, Object> value(String name, Integer width) {
		return ImmutableMap.<String, Object>builder()
				.put("name", name)
				.put("width", width)
				.build();
	}
}