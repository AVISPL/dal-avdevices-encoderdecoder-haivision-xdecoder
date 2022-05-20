/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of encapsulation (protocol) option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum Encapsulation {

	TS_OVER_UDP("TS over UDP", "TS-UDP", "ts-udp", "Udp://"),
	TS_OVER_RTP("TS over RTP", "TS-RTP", "ts-rtp", "Rtp://"),
	TS_OVER_SRT("TS over SRT", "TS-SRT", "ts-srt", "Srt://"),
	RTSP("RTSP", "RTSP", "rtsp", "Rtsp://");

	private final String uiName;
	private final String apiStatsName;
	private final String apiConfigName;
	private final String shortName;

	/**
	 * Parameterized constructor
	 * @param uiName IU name of Encapsulation
	 * @param apiStatsName API stats name of Encapsulation
	 * @param apiName API config name of Encapsulation
	 * @param shortName
	 */
	Encapsulation(String uiName, String apiStatsName, String apiName, String shortName) {
		this.uiName = uiName;
		this.apiStatsName = apiStatsName;
		this.apiConfigName = apiName;
		this.shortName = shortName;
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
	public String getApiStatsName() {
		return apiStatsName;
	}

	/**
	 * Retrieves {@code {@link #apiConfigName }}
	 *
	 * @return value of {@link #apiConfigName}
	 */
	public String getApiConfigName() {
		return apiConfigName;
	}

	/**
	 * Retrieves {@code {@link #shortName}}
	 *
	 * @return value of {@link #shortName}
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * This method is used to get encapsulation mode by ui name
	 *
	 * @param uiName is the ui name of encapsulation mode that want to get
	 * @return Encapsulation is the protocol that want to get
	 */
	public static Encapsulation getByUiName(String uiName) {
		Optional<Encapsulation> encapsulation = Arrays.stream(Encapsulation.values()).filter(com -> com.getUiName().equals(uiName)).findFirst();
		return encapsulation.orElse(Encapsulation.TS_OVER_UDP);
	}

	/**
	 * This method is used to get encapsulation mode by api stats name
	 *
	 * @param apiStatsName is the api stats name of encapsulation mode that want to get
	 * @return Encapsulation is the protocol that want to get
	 */
	public static Encapsulation getByApiStatsName(String apiStatsName) {
		Optional<Encapsulation> encapsulation = Arrays.stream(Encapsulation.values()).filter(com -> com.getApiStatsName().equals(apiStatsName)).findFirst();
		return encapsulation.orElse(Encapsulation.TS_OVER_UDP);
	}

	/**
	 * This method is used to get encapsulation mode by api config name
	 *
	 * @param apiConfigName is the api config name of encapsulation mode that want to get
	 * @return Encapsulation is the protocol that want to get
	 */
	public static Encapsulation getByApiConfigName(String apiConfigName) {
		Optional<Encapsulation> encapsulation = Arrays.stream(Encapsulation.values()).filter(com -> com.getApiConfigName().equals(apiConfigName)).findFirst();
		return encapsulation.orElse(Encapsulation.TS_OVER_UDP);
	}
}

