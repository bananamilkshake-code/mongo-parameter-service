package me.bananamilkshake.mongo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.exception.IncorrectParameterDescription;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Component
@AllArgsConstructor
class ParameterCreationDescriptionParser {

	private static final String NODE_VALIDATION = "validation";
	private static final String NODE_INDEX = "index";

	private final ObjectMapper objectMapper;

	ParameterCreationDescription parse(String value) {
		if (isNull(value)) {
			return new ParameterCreationDescription();
		}

		JsonNode jsonNode;
		try {
			jsonNode = objectMapper.reader().readTree(value);
		} catch (IOException exception) {
			throw new IncorrectParameterDescription("Could not match description to JSON object", exception);
		}

		return new ParameterCreationDescription(read(jsonNode, NODE_VALIDATION), read(jsonNode, NODE_INDEX));
	}

	private String read(final JsonNode jsonNode, final String nodeName) {
		final JsonNode node = jsonNode.get(nodeName);
		return nonNull(node) ? node.toString() : null;
	}
}
