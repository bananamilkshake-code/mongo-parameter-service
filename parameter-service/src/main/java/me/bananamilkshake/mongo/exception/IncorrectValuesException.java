package me.bananamilkshake.mongo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectValuesException extends RuntimeException {

	public IncorrectValuesException(Exception cause) {
		super("Unable to parse values description", cause);
	}
}
