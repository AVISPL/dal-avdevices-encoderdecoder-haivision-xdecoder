package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command;

/**
 * StreamCommand
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum Stream {
	CREATE("create"),
	START("start"),
	STOP("stop"),
	PAUSE("pause"),
	RESUME("resume"),
	DELETE("delete"),
	GET("get");

	private final String name;

	/**
	 * Stream command
	 *
	 * @param name {@code {@link #name}}
	 */
	Stream(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}
}
