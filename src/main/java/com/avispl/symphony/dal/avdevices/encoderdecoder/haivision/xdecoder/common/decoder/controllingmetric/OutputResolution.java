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

	AUTOMATIC("Automatic", "Auto", "Auto", "Automatic"),
	NATIVE("Native", "Native", "Native", "Native"),
	TV_RESOLUTIONS_1080P("1920x1080p", "1080p", "1080p", "TV"),
	TV_RESOLUTIONS_1080I("1920x1080i", "1080i", "1080i", "TV"),
	TV_RESOLUTIONS_720P("1280x720p", "720p", "720p", "TV"),
	TV_RESOLUTIONS_576P("720x576p", "576p", "576p", "TV"),
	TV_RESOLUTIONS_576I("720x576i", "576i", "576i", "TV"),
	TV_RESOLUTIONS_480P("720x480p", "480p", "480p", "TV"),
	TV_RESOLUTIONS_480I("720x480i", "480i", "480i", "TV"),
	COMPUTER_RESOLUTIONS_1920_1200("1920x1200 (WUXGA)", "1920x1200p", "1920x1200p", "Computer"),
	COMPUTER_RESOLUTIONS_1680_1050("1680x1050 (WSXGA+)", "1680x1050p", "1680x1050p", "Computer"),
	COMPUTER_RESOLUTIONS_1600_1200("1600x1200 (UXGA)", "1600x1200p", "1600x1200p", "Computer"),
	COMPUTER_RESOLUTIONS_1600_900("1600x900 (HD+)", "1600x900p", "1600x900p", "Computer"),
	COMPUTER_RESOLUTIONS_1440_900("1440x900 (WXGA+)", "1440x900p", "1440x900p", "Computer"),
	COMPUTER_RESOLUTIONS_1440_1050("1400x1050 (SXGA+)", "1400x1050p", "1400x1050p", "Computer"),
	COMPUTER_RESOLUTIONS_1366_768("1366x768 (HD)", "1366x768p", "1366x768p", "Computer"),
	COMPUTER_RESOLUTIONS_1360_768("1360x768 (HD)", "1360x768p", "1360x768p", "Computer"),
	COMPUTER_RESOLUTIONS_1280_1024("1280x1024 (SXGA)", "Sxga", "SXGAP", "Computer"),
	COMPUTER_RESOLUTIONS_1280_800("1280x800 (WXGA)", "1280x800p", "1280x800p", "Computer"),
	COMPUTER_RESOLUTIONS_1280_768("1280x768 (WXGA)", "1280x768p", "1280x768p", "Computer"),
	COMPUTER_RESOLUTIONS_71152_864("1152x864 (XGA+)", "1152x864p", "1152x864p", "Computer"),
	COMPUTER_RESOLUTIONS_1024_768("1024x768 (XGA)", "Xga", "XGAP", "Computer"),
	COMPUTER_RESOLUTIONS_800_600("800x600 (SVGA)", "Svga", "SVGAP", "Computer"),
	COMPUTER_RESOLUTIONS_640_480("640x480 (VGA)", "Vga", "VGAP", "Computer");

	private final String uiName;
	private final String apiConfigName;
	private final String apiStatsName;
	private final String resolutionCategory;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of decoder resolutions
	 * @param apiConfigName api config name of decoder resolutions
	 * @param apiStatsName api stats name of decoder resolutions
	 * @param resolutionCategory Type of resolution category
	 */
	OutputResolution(String uiName, String apiConfigName, String apiStatsName, String resolutionCategory) {
		this.uiName = uiName;
		this.apiConfigName = apiConfigName;
		this.apiStatsName = apiStatsName;
		this.resolutionCategory = resolutionCategory;
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
	 * Retrieves {@code {@link #apiStatsName}}
	 *
	 * @return value of {@link #apiStatsName}
	 */
	public String getApiConfigName() {
		return apiConfigName;
	}

	/**
	 * Retrieves {@code {@link #apiConfigName }}
	 *
	 * @return value of {@link #apiConfigName}
	 */
	public String getApiStatsName() {
		return apiStatsName;
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
		Optional<OutputResolution> resolutions = Arrays.stream(OutputResolution.values()).filter(resolution -> resolution.getUiName().equals(uiName)).findFirst();
		return resolutions.orElse(OutputResolution.AUTOMATIC);
	}

	/**
	 * This method is used to get resolution by name
	 *
	 * @param apiStatsName is the api stats name of resolution that want to get
	 * @return Resolutions is the Resolution that want to get
	 */
	public static OutputResolution getByAPIStatsName(String apiStatsName) {
		Optional<OutputResolution> stillImage = Arrays.stream(OutputResolution.values()).filter(resolution -> resolution.getApiStatsName().equals(apiStatsName)).findFirst();
		return stillImage.orElse(OutputResolution.AUTOMATIC);
	}
}

