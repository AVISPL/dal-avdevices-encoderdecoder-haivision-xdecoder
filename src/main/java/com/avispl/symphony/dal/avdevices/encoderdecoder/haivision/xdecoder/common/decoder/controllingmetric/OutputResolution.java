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
public enum OutputResolution {

	AUTOMATIC("Automatic", "Auto", "Automatic"),
	NATIVE("Native", "Native", "Native"),
	TV_RESOLUTIONS_1080P("1920x1080p", "1080p", "TV"),
	TV_RESOLUTIONS_1080I("1920x1080i", "1080i", "TV"),
	TV_RESOLUTIONS_720P("1280x720p", "720p", "TV"),
	TV_RESOLUTIONS_576P("720x576p", "576p", "TV"),
	TV_RESOLUTIONS_576I("720x576i", "576i", "TV"),
	TV_RESOLUTIONS_480P("720x480p", "480p", "TV"),
	TV_RESOLUTIONS_480I("720x480i", "480i", "TV"),
	COMPUTER_RESOLUTIONS_1920_1200("1920x1200 (WUXGA)", "1920x1200p", "Computer"),
	COMPUTER_RESOLUTIONS_1680_1050("1680x1050 (WSXGA+)", "1680x1050p", "Computer"),
	COMPUTER_RESOLUTIONS_1600_1200("1600x1200 (UXGA)", "1600x1200p", "Computer"),
	COMPUTER_RESOLUTIONS_1600_900("1600x900 (HD+)", "1600x900p", "Computer"),
	COMPUTER_RESOLUTIONS_1440_900("1440x900 (SXGA+)", "1440x900p", "Computer"),
	COMPUTER_RESOLUTIONS_1440_1050("1440x1050 (SXGA+)", "1440x1050p", "Computer"),
	COMPUTER_RESOLUTIONS_1366_768("1366x768 (HD)", "1366x768p", "Computer"),
	COMPUTER_RESOLUTIONS_1360_768("1360x768 (HD)", "1360x768p", "Computer"),
	COMPUTER_RESOLUTIONS_1280_1024("1280x1024 (SXGA)", "1280x1024p", "Computer"),
	COMPUTER_RESOLUTIONS_1280_800("1280x800 (WXGA)", "1280x800", "Computer"),
	COMPUTER_RESOLUTIONS_1280_768("1280x768 (WXGA)", "1280x768p", "Computer"),
	COMPUTER_RESOLUTIONS_71152_864("71152x864 (XGA+)", "71152x864p", "Computer"),
	COMPUTER_RESOLUTIONS_1024_768("1024x768 (XGA)", "1024x768p", "Computer"),
	COMPUTER_RESOLUTIONS_800_600("800x600 (SVGA)", "800x600p", "Computer"),
	COMPUTER_RESOLUTIONS_640_480("640x480 (VGA)", "640x480p", "Computer");

	private final String uiName;
	private final String apiName;
	private final String resolutionCategory;

	/**
	 * Parameterized constructor
	 *  @param uiName ui name of decoder resolutions
	 * @param apiName  api name of decoder resolutions
	 * @param resolutionCategory Type of resolution category
	 */
	OutputResolution(String uiName, String apiName, String resolutionCategory) {
		this.uiName = uiName;
		this.apiName = apiName;
		this.resolutionCategory = resolutionCategory;
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
	 * Retrieves {@code {@link #resolutionCategory}}
	 *
	 * @return value of {@link #resolutionCategory}
	 */
	public String getResolutionCategory() {
		return resolutionCategory;
	}

	/**
	 * This method is used to get resolution by name
	 *
	 * @param uiName is the ui name of resolution that want to get
	 * @return Resolutions is the Resolution that want to get
	 */
	public static OutputResolution getByUIName(String uiName) {
		Optional<OutputResolution> resolutions = Arrays.stream(OutputResolution.values()).filter(com -> com.getUiName().equals(uiName)).findFirst();
		return resolutions.orElse(OutputResolution.AUTOMATIC);
	}

	/**
	 * This method is used to get resolution by name
	 *
	 * @param apiName is the api name of resolution that want to get
	 * @return Resolutions is the Resolution that want to get
	 */
	public static OutputResolution getByAPIName(String apiName) {
		Optional<OutputResolution> stillImage = Arrays.stream(OutputResolution.values()).filter(com -> com.getApiName().equals(apiName)).findFirst();
		return stillImage.orElse(OutputResolution.AUTOMATIC);
	}
}

