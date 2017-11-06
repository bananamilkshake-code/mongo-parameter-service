package me.bananamilkshake.mongo.domain.aggregation.match;

import lombok.Getter;
import me.bananamilkshake.mongo.domain.ParameterBase;
import me.bananamilkshake.mongo.domain.aggregation.match.field.ComparableMatchField;
import me.bananamilkshake.mongo.domain.aggregation.match.field.EqualMatchField;
import me.bananamilkshake.mongo.domain.aggregation.match.field.DateField;

import java.time.LocalDateTime;

@Getter
public class MatchDescription implements ParameterBase {

	private EqualMatchField<String> user;
	private ComparableMatchField<DateField> validFrom;

	public MatchDescription(String user, LocalDateTime date) {
		this.user = new EqualMatchField<>(user);
		this.validFrom = new ComparableMatchField<DateField>().lte(new DateField(date));
	}
}
