package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command;

/**
 * AccountCommand
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum Audenc {
	START("start"),
	STOP("stop"),
	SET("set"),
	GET("get");

	private final String name;

	/**
	 * Audenc command
	 *
	 * @param name {@code {@link #name}}
	 */
	Audenc(String name) {
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
