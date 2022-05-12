/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of reject unencrypted modes
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/12/2022
 * @since 1.0.0
 */
public enum RejectUnencrypted {

	ENABLE_REJECT_UNENCRYPTED("Yes", true, 1),
	DISABLE_REJECT_UNENCRYPTED("No", false, 0);

	private final String name;
	private final boolean isEnable;
	private final int code;

	/**
	 * Parameterized constructor
	 * @param name Name of reject unencrypted modes
	 * @param isEnable status of reject unencrypted modes
	 * @param code code of reject unencrypted modes
	 */
	RejectUnencrypted(String name, boolean isEnable, int code) {
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
	public static RejectUnencrypted getByName(String name) {
		Optional<RejectUnencrypted> state = Arrays.stream(RejectUnencrypted.values()).filter(com -> com.getName().equals(name)).findFirst();
		return state.orElse(RejectUnencrypted.DISABLE_REJECT_UNENCRYPTED);
	}
}

