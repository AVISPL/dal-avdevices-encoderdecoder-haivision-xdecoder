/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of output frame rate option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/4/2022
 * @since 1.0.0
 */
public enum OutputFrameRate {

	AUTO("Automatic", "Auto"),
	OUTPUT_FRAME_RATE_75 ("75", "75"),
	OUTPUT_FRAME_RATE_60 ("60", "60"),
	OUTPUT_FRAME_RATE_59 ("59", "59"),
	OUTPUT_FRAME_RATE_50 ("50", "50"),
	OUTPUT_FRAME_RATE_30 ("30", "30"),
	OUTPUT_FRAME_RATE_29 ("29", "29"),
	OUTPUT_FRAME_RATE_25 ("25", "25"),
	OUTPUT_FRAME_RATE_24 ("24", "24"),
	OUTPUT_FRAME_RATE_23 ("23", "23");

	private final String uiName;
	private final String apiName;

	/**
	 * Parameterized constructor
	 * @param uiName ui name of decoder output frame rate
	 * @param apiName api name of decoder output frame rate
	 */
	OutputFrameRate(String uiName, String apiName) {
		this.uiName = uiName;
		this.apiName = apiName;
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
	 * Retrieves {@code {@link #apiName}}
	 *
	 * @return value of {@link #apiName}
	 */
	public String getApiName() {
		return apiName;
	}

	/**
	 * This method is used to get output frame rate by UI name
	 *
	 * @param uiName is the ui name of output frame rate that want to get
	 * @return OutputFrameRate is the output frame rate that want to get
	 */
	public static OutputFrameRate getByUIName(String uiName) {
		Optional<OutputFrameRate> outputFrameRate = Arrays.stream(OutputFrameRate.values()).filter(frameRate -> frameRate.getUiName().equals(uiName)).findFirst();
		return outputFrameRate.orElse(OutputFrameRate.AUTO);
	}

	/**
	 * This method is used to get output frame rate by API name
	 *
	 * @param apiName is the api name of output frame rate that want to get
	 * @return OutputFrameRate is the output frame rate that want to get
	 */
	public static OutputFrameRate getByAPIName(String apiName) {
		Optional<OutputFrameRate> outputFrameRate = Arrays.stream(OutputFrameRate.values()).filter(frameRate -> frameRate.getApiName().equals(apiName)).findFirst();
		return outputFrameRate.orElse(OutputFrameRate.AUTO);
	}
}

