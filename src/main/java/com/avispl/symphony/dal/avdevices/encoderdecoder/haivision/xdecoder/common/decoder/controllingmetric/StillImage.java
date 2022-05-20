/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of still image mode option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/4/2022
 * @since 1.0.0
 */
public enum StillImage {

	FREEZE("Freeze", "Freeze"),
	BLACK("Black Screen", "Black"),
	BLUE("Blue  Screen", "Blue"),
	COLOR_BARS("Color Bars", "Bars"),
	MUTE("Mute", "Mute"),
	SELECT_IMAGE("Select Image", "Select"),
	CUSTOM("Custom", "Custom");

	private final String uiName;
	private final String apiName;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of decoder stillImage
	 * @param apiName  api name of decoder stillImage
	 */
	StillImage(String uiName, String apiName) {
		this.uiName = uiName;
		this.apiName = apiName;
	}

	/**
	 * retrieve {@code {@link #uiName }}
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
	 * This method is used to get still image by name
	 *
	 * @param uiName is the ui name of still image that want to get
	 * @return StillImage is the still image that want to get
	 */
	public static StillImage getByUIName(String uiName) {
		Optional<StillImage> stillImage = Arrays.stream(StillImage.values()).filter(still -> still.getUiName().equals(uiName)).findFirst();
		return stillImage.orElse(StillImage.CUSTOM);
	}

	/**
	 * This method is used to get still image by name
	 *
	 * @param apiName is the api name of still image that want to get
	 * @return StillImage is the still image that want to get
	 */
	public static StillImage getByAPIName(String apiName) {
		Optional<StillImage> stillImage = Arrays.stream(StillImage.values()).filter(still -> still.getApiName().equals(apiName)).findFirst();
		return stillImage.orElse(StillImage.CUSTOM);
	}
}

