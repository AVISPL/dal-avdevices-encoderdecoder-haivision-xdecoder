/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of video source mode option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/4/2022
 * @since 1.0.0
 */
public enum VideoSource {

	DEFAULT_VIDEO_SOURCE("None", "None"),
	DECODER_1("SDI 1", "Decoder1"),
	DECODER_2("SDI 2", "Decoder2");

	private final String uiName;
	private final String apiName;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of video source mode
	 * @param apiName  api name of video source mode
	 */
	VideoSource(String uiName, String apiName) {
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
	 * This method is used to get video source mode by ui name
	 *
	 * @param uiName is the ui name of video source mode that want to get
	 * @return VideoSource is the video source mode that want to get
	 */
	public static VideoSource getByUiName(String uiName) {
		Optional<VideoSource> videoSourceMode = Arrays.stream(VideoSource.values()).filter(com -> com.getUiName().equals(uiName)).findFirst();
		return videoSourceMode.orElse(VideoSource.DEFAULT_VIDEO_SOURCE);
	}

	/**
	 * This method is used to get video source mode by api name
	 *
	 * @param apiName is the api name of video source mode that want to get
	 * @return VideoSource is the video source mode that want to get
	 */
	public static VideoSource getByAPIName(String apiName) {
		Optional<VideoSource> videoSourceMode = Arrays.stream(VideoSource.values()).filter(com -> com.getApiName().equals(apiName)).findFirst();
		return videoSourceMode.orElse(VideoSource.DEFAULT_VIDEO_SOURCE);
	}
}

