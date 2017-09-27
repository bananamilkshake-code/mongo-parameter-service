package me.bananamilkshake.mongo.domain;

/**
 * This interface is created to trace any changes in necessary parameter
 * fields. Query and Validation classes should be implemented from that.
 */
public interface Parameter {

	Object getUser();

	Object getValidFrom();
	Object getValidTo();
}
