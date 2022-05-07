/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of decoder state option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/5/2022
 * @since 1.0.0
 */
public enum State {

	STOPPED("STOPPED", 0),
	START("START", 1);

	private final String name;
	private final Integer code;

	/**
	 * Parameterized constructor
	 * @param name name of decoder status
	 * @param code code of decoder status
	 */
	State(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * retrieve {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retrieves {@code {@link #code}}
	 *
	 * @return value of {@link #code}
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * This method is used to get state by name
	 *
	 * @param name is the name of state that want to get
	 * @return State is the state that want to get
	 */
	public static State getByName(String name) {
		Optional<State> state = Arrays.stream(State.values()).filter(com -> com.getName().equals(name)).findFirst();
		return state.orElse(State.STOPPED);
	}

	/**
	 * This method is used to get state by code
	 *
	 * @param code is the code of state that want to get
	 * @return State is the state that want to get
	 */
	public static State getByCode(Integer code) {
		Optional<State> state = Arrays.stream(State.values()).filter(com -> com.getCode().equals(code)).findFirst();
		return state.orElse(State.STOPPED);
	}
}

