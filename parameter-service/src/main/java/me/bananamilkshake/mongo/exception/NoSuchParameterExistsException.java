package me.bananamilkshake.mongo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.text.MessageFormat.format;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchParameterExistsException extends RuntimeException {

	public NoSuchParameterExistsException(String type) {
		super(format("There is no parameter with type `{0}`", type));
	}
}
