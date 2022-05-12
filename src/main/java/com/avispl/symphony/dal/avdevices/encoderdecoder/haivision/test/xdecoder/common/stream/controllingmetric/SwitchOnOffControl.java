/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of ae encrypted modes
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/12/2022
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
	 * @param name Name of ae encrypted modes
	 * @param isEnable status of ae encrypted modes
	 * @param code code of ae encrypted modes
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
	public int getCode() {
		return code;
	}

	/**
	 * This method is used to get sync mode by name
	 *
	 * @param name is the name of sync mode that want to get
	 * @return SyncMode is the sync mode that want to get
	 */
	public static SwitchOnOffControl getByName(String name) {
		Optional<SwitchOnOffControl> state = Arrays.stream(SwitchOnOffControl.values()).filter(com -> com.getName().equals(name)).findFirst();
		return state.orElse(SwitchOnOffControl.OFF);
	}
}

