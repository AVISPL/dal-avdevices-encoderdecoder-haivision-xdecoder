package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command;

/**
 * DeviceInfoCommand
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum Haiversion {
	HAIVERSION("haiversion"),
	TEMPERATURE("temperature get");

	private final String name;

	/**
	 * Haiversion command
	 *
	 * @param name {@code {@link #name}}
	 */
	Haiversion(String name) {
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
