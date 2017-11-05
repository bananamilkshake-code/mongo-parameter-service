package me.bananamilkshake.mongo.domain.index;

import me.bananamilkshake.mongo.domain.Parameter;

public class IndexDescription implements Parameter {

	@Override
	public Object getUser() {
		return 1;
	}

	@Override
	public Object getValidFrom() {
		return 1;
	}
}
