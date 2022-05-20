/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of SRT option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum SRTMode {

	CALLER("Caller"),
	LISTENER("Listener"),
	RENDEZVOUS("Rendezvous");

	private final String uiName;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	SRTMode(String name) {
		this.uiName = name;
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
	 * This method is used to get srt mode by name
	 *
	 * @param name is the name of srt mode that want to get
	 * @return SRTMode is the srt that want to get
	 */
	public static SRTMode getByName(String name) {
		Optional<SRTMode> srtMode = Arrays.stream(SRTMode.values()).filter(srt -> srt.getUiName().equals(name)).findFirst();
		return srtMode.orElse(SRTMode.LISTENER);
	}
}

