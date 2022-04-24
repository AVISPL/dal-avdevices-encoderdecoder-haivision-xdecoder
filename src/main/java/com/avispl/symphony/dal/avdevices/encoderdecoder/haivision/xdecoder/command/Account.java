package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command;

/**
 * AccountCommand
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum Account {
	ACCOUNT("account"),
	GET("get");

	private final String name;

	/**
	 * Account command
	 *
	 * @param name {@code {@link #name}}
	 */
	Account(String name) {
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
