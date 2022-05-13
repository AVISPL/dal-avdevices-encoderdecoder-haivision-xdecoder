/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of switch on/off modes
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum SwitchOnOffControl {

	ON("On", true, 1),
	OFF("Off", false, 0);

	private final String name;
	private final boolean isEnable;
	private final int code;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of switch on/off mode
	 * @param isEnable status of switch on/off mode
	 * @param code code of switch on/off mode
	 */
	SwitchOnOffControl(String name, boolean isEnable, int code) {
		this.name = name;
		this.isEnable = isEnable;
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
	 * Retrieves {@code {@link #isEnable }}
	 *
	 * @return value of {@link #isEnable}
	 */
	public boolean isEnable() {
		return isEnable;
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
	 * This method is used to get switch on/off mode by name
	 *
	 * @param name is the name of switch on/off mode that want to get
	 * @return SyncMode is the switch on/off mode that want to get
	 */
	public static SwitchOnOffControl getByName(String name) {
		Optional<SwitchOnOffControl> state = Arrays.stream(SwitchOnOffControl.values()).filter(com -> com.getName().equals(name)).findFirst();
		return state.orElse(SwitchOnOffControl.OFF);
	}

	/**
	 * This method is used to get switch on/off code
	 *
	 * @param code is the code of switch on/off mode that want to get
	 * @return State is the switch on/off mode that want to get
	 */
	public static SwitchOnOffControl getByCode(Integer code) {
		Optional<SwitchOnOffControl> rejectUnencrypted = Arrays.stream(SwitchOnOffControl.values()).filter(com -> com.getCode().equals(code)).findFirst();
		return rejectUnencrypted.orElse(SwitchOnOffControl.OFF);
	}
}

