package me.bananamilkshake.mongo.service.values;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSONParseException;
import me.bananamilkshake.mongo.exception.IncorrectValuesException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ValuesParser {

	public List<Map<String, Object>> parse(String values) {
		try {
			return (List<Map<String, Object>>) BasicDBObject.parse(values);
		} catch (JSONParseException jsonParseException) {
			throw new IncorrectValuesException(jsonParseException);
		}
	}
}
