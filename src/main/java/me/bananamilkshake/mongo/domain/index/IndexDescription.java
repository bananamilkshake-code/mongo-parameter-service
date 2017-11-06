package me.bananamilkshake.mongo.domain.index;

import me.bananamilkshake.mongo.domain.ParameterBase;

public class IndexDescription implements ParameterBase {

	@Override
	public Object getUser() {
		return 1;
	}

	@Override
	public Object getValidFrom() {
		return 1;
	}
}
