package me.bananamilkshake.mongo.configuration;

import com.google.common.collect.Lists;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.dto.ParameterDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class OrikaConfiguration {

	@Bean
	public MapperFactory mapperFactory() {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

		mapperFactory.getConverterFactory()
				.registerConverter(new PassThroughConverter(LocalDateTime.class));

		mapperFactory.classMap(Parameter.class, ParameterDto.class)
				.byDefault()
				.exclude("values")
				.customize(new CustomMapper<Parameter, ParameterDto>() {
					@Override
					public void mapAtoB(Parameter parameter, ParameterDto parameterDto, MappingContext context) {
						parameterDto.setValues(Lists.newArrayList(parameter.getValues()));
					}
				})
				.register();

		return mapperFactory;
	}

	@Bean
	public MapperFacade mapperFacade(MapperFactory mapperFactory) {
		return mapperFactory.getMapperFacade();
	}
}
