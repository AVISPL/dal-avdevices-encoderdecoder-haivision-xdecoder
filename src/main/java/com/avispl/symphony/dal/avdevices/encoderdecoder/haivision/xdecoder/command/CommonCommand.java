/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command;

/**
 * CommonCommand
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum CommonCommand {
	GET("get"),
	ALL("all"),
	START("start"),
	STOP("stop"),
	SET("set");

	private final String name;

	/**
	 * Common command
	 *
	 * @param name {@code {@link #name}}
	 */
	CommonCommand(String name) {
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
