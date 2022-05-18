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

	TS_OVER_UDP("TS-UDP", "ts-udp", "Udp://"),
	TS_OVER_RTP("TS-RTP", "ts-rtp", "Rtp://"),
	TS_OVER_SRT("TS-SRT", "ts-srt", "Srt://"),
	RTSP("RTSP", "rtsp", "Rtsp://");

	private final String uiName;
	private final String apiName;
	private final String shortName;

	/**
	 * Parameterized constructor
	 * @param uiName IU name of Encapsulation
	 * @param apiName API name of Encapsulation
	 * @param shortName
	 */
	Encapsulation(String uiName, String apiName, String shortName) {
		this.uiName = uiName;
		this.apiName = apiName;
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
	 * Retrieves {@code {@link #apiName}}
	 *
	 * @return value of {@link #apiName}
	 */
	public String getApiName() {
		return apiName;
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
		Optional<Encapsulation> encapsulation = Arrays.stream(Encapsulation.values()).filter(en -> en.getUiName().equals(uiName)).findFirst();
		return encapsulation.orElse(Encapsulation.TS_OVER_UDP);
	}

	/**
	 * This method is used to get encapsulation mode by api name
	 *
	 * @param apiName is the ui name of encapsulation mode that want to get
	 * @return Encapsulation is the protocol that want to get
	 */
	public static Encapsulation getByApiName(String apiName) {
		Optional<Encapsulation> encapsulation = Arrays.stream(Encapsulation.values()).filter(en -> en.getApiName().equals(apiName)).findFirst();
		return encapsulation.orElse(Encapsulation.TS_OVER_UDP);
	}
}

