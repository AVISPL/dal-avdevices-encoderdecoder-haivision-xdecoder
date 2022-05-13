/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of decoder sync mode option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/4/2022
 * @since 1.0.0
 */
public enum SyncMode {

	ENABLE_SYNC_MODE("Stc", true, 1),
	DISABLE_SYNC_MODE("pass", false, 0);

	private final String name;
	private final boolean isEnable;
	private final int code;

	/**
	 * Parameterized constructor
	 *  @param name Name of enable buffering mode
	 * @param isEnable status of enable buffering mode
	 * @param code code of enable buffering mode
	 */
	SyncMode(String name, boolean isEnable, int code) {
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
	public static SyncMode getByName(String name) {
		Optional<SyncMode> state = Arrays.stream(SyncMode.values()).filter(com -> com.getName().equals(name)).findFirst();
		return state.orElse(SyncMode.DISABLE_SYNC_MODE);
	}
}

