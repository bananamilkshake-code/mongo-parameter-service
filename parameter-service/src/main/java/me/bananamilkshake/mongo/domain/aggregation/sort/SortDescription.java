package me.bananamilkshake.mongo.domain.aggregation.sort;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import me.bananamilkshake.mongo.domain.ParameterBase;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class SortDescription implements ParameterBase {

	private Integer user;
	private Integer validFrom;
}
