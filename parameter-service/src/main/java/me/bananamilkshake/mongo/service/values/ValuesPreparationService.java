package me.bananamilkshake.mongo.service.values;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import me.bananamilkshake.mongo.domain.Fields;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValuesPreparationService {

	public Map<String, Object> prepare(String description) {
		return readValues(description).entrySet()
				.stream()
				.map(this::convert)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private BasicDBObject readValues(String description) {
		return (BasicDBObject) JSON.parse(description);
	}

	private Map.Entry<String, Object> convert(Map.Entry<String, Object> entry) {
		String newKey = Joiner.on('.').join(Fields.VALUES, entry.getKey());
		return new HashMap.SimpleEntry<>(newKey, entry.getValue());
	}
}
