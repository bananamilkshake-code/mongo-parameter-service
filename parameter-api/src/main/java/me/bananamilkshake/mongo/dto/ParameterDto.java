package me.bananamilkshake.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterDto {

	private String user;
	private LocalDateTime validFrom;
	private List<Map<String, Object>> values;
}
