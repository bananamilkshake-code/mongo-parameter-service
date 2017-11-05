package me.bananamilkshake.mongo.domain.aggregation.match;

import lombok.Getter;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.domain.aggregation.match.field.ComparableMatchField;
import me.bananamilkshake.mongo.domain.aggregation.match.field.EqualMatchField;
import me.bananamilkshake.mongo.domain.aggregation.match.field.LocalDateField;

import java.time.LocalDate;

@Getter
public class MatchDescription implements Parameter {

	private EqualMatchField<String> user;
	private ComparableMatchField<LocalDateField> validFrom;

	public MatchDescription(String user, LocalDate date) {
		this.user = new EqualMatchField<>(user);
		this.validFrom = new ComparableMatchField<LocalDateField>().lte(new LocalDateField(date));
	}
}
