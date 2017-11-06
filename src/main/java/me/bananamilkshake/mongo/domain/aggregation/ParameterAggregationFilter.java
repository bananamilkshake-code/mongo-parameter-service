package me.bananamilkshake.mongo.domain.aggregation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Value;
import me.bananamilkshake.mongo.domain.aggregation.match.MatchDescription;
import me.bananamilkshake.mongo.domain.aggregation.sort.SortDescription;

import java.util.ArrayList;

@NoArgsConstructor
public class ParameterAggregationFilter extends ArrayList<Object> {

	@Value
	private class Match {

		@JsonProperty("$match")
		private final MatchDescription match;
	}

	@Value
	private class Sort {

		@JsonProperty("$sort")
		private final SortDescription sort;
	}

	@Value
	private class Limit {

		@JsonProperty("$limit")
		private Integer limit;
	}

	public ParameterAggregationFilter setMatch(MatchDescription matchDescription) {
		this.add(new Match(matchDescription));
		return this;
	}

	public ParameterAggregationFilter setSort(SortDescription sort) {
		this.add(new Sort(sort));
		return this;
	}

	public ParameterAggregationFilter setLimit(Integer limit) {
		this.add(new Limit(limit));
		return this;
	}
}
