/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of buffering mode option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/4/2022
 * @since 1.0.0
 */
public enum BufferingMode {

	AUTO("Automatic", "Automatic"),
	FIXED("Fixed", "Fixed"),
	MULTI_SYNC("Multi Sync", "MultiSync"),
	ADAPTIVE_LOW_LATENCY("Adaptive Low Latency", "Adaptive");

	private final String uiName;
	private final String apiName;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of decoder buffering mode
	 * @param apiName  api name of decoder buffering mode
	 */
	BufferingMode(String uiName, String apiName) {
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
	 * This method is used to get buffering mode by ui name
	 *
	 * @param uiName is the ui name of buffering mode that want to get
	 * @return BufferingMode is the buffering mode that want to get
	 */
	public static BufferingMode getByUiName(String uiName) {
		Optional<BufferingMode> bufferingMode = Arrays.stream(BufferingMode.values()).filter(com -> com.getUiName().equals(uiName)).findFirst();
		return bufferingMode.orElse(BufferingMode.AUTO);
	}

	/**
	 * This method is used to get buffering mode by api name
	 *
	 * @param apiName is the api name of buffering mode that want to get
	 * @return BufferingMode is the buffering mode that want to get
	 */
	public static BufferingMode getByAPIName(String apiName) {
		Optional<BufferingMode> bufferingMode = Arrays.stream(BufferingMode.values()).filter(com -> com.getApiName().equals(apiName)).findFirst();
		return bufferingMode.orElse(BufferingMode.AUTO);
	}
}

