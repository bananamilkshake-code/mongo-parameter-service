package me.bananamilkshake.mongo.service.insert;

import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class InsertCommandCreator {

	public InsertDescription create(String user, LocalDate validFrom, String values) {
		return new InsertDescription(user, validFrom, prepareValues(values));
	}

	private List<Object> prepareValues(String values) {
		return (BasicDBList) JSON.parse(values);
	}
}
