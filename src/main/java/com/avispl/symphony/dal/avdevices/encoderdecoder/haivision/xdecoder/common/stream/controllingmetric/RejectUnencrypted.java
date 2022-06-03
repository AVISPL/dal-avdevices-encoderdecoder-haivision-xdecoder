/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric;

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

	ENABLE_REJECT_UNENCRYPTED("Enabled", "Yes", true, 1),
	DISABLE_REJECT_UNENCRYPTED("Disabled", "No", false, 0);

	private final String uiName;
	private final String apiName;
	private final boolean isEnable;
	private final Integer code;

	/**
	 * Parameterized constructor
	 * @param uiName ui name of reject unencrypted modes
	 * @param apiName api name of reject unencrypted modes
	 * @param isEnable status of reject unencrypted modes
	 * @param code code of reject unencrypted modes
	 */
	RejectUnencrypted(String uiName, String apiName, boolean isEnable, int code) {
		this.uiName = uiName;
		this.apiName = apiName;
		this.isEnable = isEnable;
		this.code = code;
	}

	/**
	 * Retrieves {@code {@link #uiName}}
	 *
	 * @return value of {@link #uiName}
	 */
	public String getUiName() {
		return uiName;
	}

	/**
	 * Retrieves {@code {@link #apiName }}
	 *
	 * @return value of {@link #apiName}
	 */
	public String getApiName() {
		return this.apiName;
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
	 * @param apiName is the api name of reject unencrypted mode that want to get
	 * @return SyncMode is the reject unencrypted mode that want to get
	 */
	public static RejectUnencrypted getByApiName(String apiName) {
		Optional<RejectUnencrypted> rejectUnencrypted = Arrays.stream(RejectUnencrypted.values()).filter(ru -> ru.getApiName().equals(apiName)).findFirst();
		return rejectUnencrypted.orElse(RejectUnencrypted.DISABLE_REJECT_UNENCRYPTED);
	}

	/**
	 * This method is used to get reject unencrypted mode by code
	 *
	 * @param code is the code of reject unencrypted mode that want to get
	 * @return State is the reject unencrypted mode that want to get
	 */
	public static RejectUnencrypted getByCode(Integer code) {
		Optional<RejectUnencrypted> rejectUnencrypted = Arrays.stream(RejectUnencrypted.values()).filter(ru -> ru.getCode().equals(code)).findFirst();
		return rejectUnencrypted.orElse(RejectUnencrypted.DISABLE_REJECT_UNENCRYPTED);
	}
}

