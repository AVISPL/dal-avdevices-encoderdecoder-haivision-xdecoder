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
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum RejectUnencrypted {

	ENABLE_REJECT_UNENCRYPTED("Yes", true, 1),
	DISABLE_REJECT_UNENCRYPTED("No", false, 0);

	private final String name;
	private final boolean isEnable;
	private final Integer code;

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
	public Integer getCode() {
		return code;
	}

	/**
	 * This method is used to get reject unencrypted mode by name
	 *
	 * @param name is the name of reject unencrypted mode that want to get
	 * @return SyncMode is the reject unencrypted mode that want to get
	 */
	public static RejectUnencrypted getByName(String name) {
		Optional<RejectUnencrypted> state = Arrays.stream(RejectUnencrypted.values()).filter(com -> com.getName().equals(name)).findFirst();
		return state.orElse(RejectUnencrypted.DISABLE_REJECT_UNENCRYPTED);
	}

	/**
	 * This method is used to get reject unencrypted mode by code
	 *
	 * @param code is the code of reject unencrypted mode that want to get
	 * @return State is the reject unencrypted mode that want to get
	 */
	public static RejectUnencrypted getByCode(Integer code) {
		Optional<RejectUnencrypted> rejectUnencrypted = Arrays.stream(RejectUnencrypted.values()).filter(com -> com.getCode().equals(code)).findFirst();
		return rejectUnencrypted.orElse(RejectUnencrypted.DISABLE_REJECT_UNENCRYPTED);
	}
}

