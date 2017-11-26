package me.bananamilkshake.mongo.service.convert;

import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.dto.ParameterDto;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ParameterConverter {

	private final MapperFacade mapperFacade;

	public ParameterDto convert(Parameter parameter) {
		return mapperFacade.map(parameter, ParameterDto.class);
	}
}

