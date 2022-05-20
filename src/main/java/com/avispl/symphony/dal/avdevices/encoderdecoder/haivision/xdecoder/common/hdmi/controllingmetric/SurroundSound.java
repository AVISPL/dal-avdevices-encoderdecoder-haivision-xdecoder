/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of surround sound mode option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/4/2022
 * @since 1.0.0
 */
public enum SurroundSound {

	STEREO("Stereo", "No"),
	SURROUND("Surround", "Yes");

	private final String uiName;
	private final String apiName;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of surround sound mode
	 * @param apiName  api name of surround sound mode
	 */
	SurroundSound(String uiName, String apiName) {
		this.uiName = uiName;
		this.apiName = apiName;
	}

	/**
	 * Retrieves {@code {@link #uiName }}
	 *
	 * @return value of {@link #uiName}
	 */
	public String getUiName() {
		return this.uiName;
	}

	/**
	 * Retrieves {@code {@link #apiName}}
	 *
	 * @return value of {@link #apiName}
	 */
	public String getApiName() {
		return apiName;
	}

	/**
	 * This method is used to get surround sound mode by ui name
	 *
	 * @param uiName is the ui name of surround sound mode that want to get
	 * @return AudioOutputMode is the surround sound mode that want to get
	 */
	public static SurroundSound getByUiName(String uiName) {
		Optional<SurroundSound> bufferingMode = Arrays.stream(SurroundSound.values()).filter(com -> com.getUiName().equals(uiName)).findFirst();
		return bufferingMode.orElse(SurroundSound.STEREO);
	}

	/**
	 * This method is used to get surround sound mode by api name
	 *
	 * @param apiName is the api name of surround sound mode that want to get
	 * @return AudioOutputMode is the surround sound mode that want to get
	 */
	public static SurroundSound getByAPIName(String apiName) {
		Optional<SurroundSound> bufferingMode = Arrays.stream(SurroundSound.values()).filter(com -> com.getApiName().equals(apiName)).findFirst();
		return bufferingMode.orElse(SurroundSound.STEREO);
	}
}

