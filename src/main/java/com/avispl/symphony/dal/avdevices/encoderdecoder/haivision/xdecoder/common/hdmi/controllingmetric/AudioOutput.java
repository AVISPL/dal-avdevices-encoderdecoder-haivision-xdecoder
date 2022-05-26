/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of audio output mode option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/4/2022
 * @since 1.0.0
 */
public enum AudioOutput {

	CHANNEL_12("1 & 2", "Chan1+2"),
	CHANNEL_34("3 & 4", "Chan3+4"),
	CHANNEL_56("5 & 6", "Chan5+6"),
	CHANNEL_78("7 & 8", "Chan7+8");

	private final String uiName;
	private final String apiName;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of audio output mode
	 * @param apiName  api name of audio output mode
	 */
	AudioOutput(String uiName, String apiName) {
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
	 * This method is used to get audio output mode by ui name
	 *
	 * @param uiName is the ui name of audio output mode that want to get
	 * @return AudioOutputMode is the audio output mode that want to get
	 */
	public static AudioOutput getByUiName(String uiName) {
		Optional<AudioOutput> audioOutputMode = Arrays.stream(AudioOutput.values()).filter(audioOutput -> audioOutput.getUiName().equals(uiName)).findFirst();
		return audioOutputMode.orElse(AudioOutput.CHANNEL_12);
	}

	/**
	 * This method is used to get audio output mode by api name
	 *
	 * @param apiName is the api name of audio output mode that want to get
	 * @return AudioOutputMode is the audio output mode that want to get
	 */
	public static AudioOutput getByAPIName(String apiName) {
		Optional<AudioOutput> audioOutputMode = Arrays.stream(AudioOutput.values()).filter(audioOutput -> audioOutput.getApiName().equals(apiName)).findFirst();
		return audioOutputMode.orElse(AudioOutput.CHANNEL_12);
	}
}

