package me.bananamilkshake.mongo.service.values;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import me.bananamilkshake.mongo.domain.Fields;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValuesPreparationService {

	public Map<String, BsonValue> prepare(String description) {
		return readValues(description).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private BsonDocument readValues(String description) {
		return BsonDocument.parse(description);
	}

//	private BsonValue convert(Map.Entry<String, Object> entry) {
//		String newKey = Joiner.on('.').join(Fields.VALUES, entry.getKey());
//		return new HashMap.SimpleEntry<>(newKey, new BsonValue(entry.getValue()));
//	}
}
