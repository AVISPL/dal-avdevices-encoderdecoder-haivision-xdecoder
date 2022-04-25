/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command;

/**
 * Stream command
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum StreamCommand {
	STREAM("stream"),
	CREATE("create"),
	PAUSE("pause"),
	RESUME("resume"),
	DELETE("delete");

	private final String name;

	/**
	 * Stream command
	 *
	 * @param name {@code {@link #name}}
	 */
	StreamCommand(String name) {
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
