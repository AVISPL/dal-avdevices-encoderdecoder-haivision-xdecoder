/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

/**
 * Common command
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum CommandOperation {

	// Common command
	GET("get"),
	ALL("all"),
	START("start"),
	STOP("stop"),
	SET("set"),

	// Account command
	OPERATION_ACCOUNT("account"),

	// Audio command
	OPERATION_AUDDEC("auddec"),

	// Device info command
	OPERATION_HAIVERSION("haiversion"),
	OPERATION_TEMPERATURE("temperature get"),

	// Service command
	OPERATION_SERVICE("service"),
	OPERATION_STATUS("status"),

	// Stream command
	OPERATION_STREAM("stream"),
	OPERATION_CREATE("create"),
	OPERATION_PAUSE("pause"),
	OPERATION_RESUME("resume"),
	OPERATION_DELETE("delete"),

	// Talkback command
	OPERATION_TALKBACK("talkback"),

	// Video command
	OPERATION_VIDDEC("viddec");

	private final String name;

	/**
	 * Common command
	 *
	 * @param name {@code {@link #name}}
	 */
	CommandOperation(String name) {
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
