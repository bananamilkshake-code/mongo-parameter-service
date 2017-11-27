package me.bananamilkshake.mongo.service.values;

import com.mongodb.util.JSON;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ValuesParser {

	public List<Map<String, Object>> parse(String values) {
		return (List<Map<String, Object>>) JSON.parse(values);
	}
}
