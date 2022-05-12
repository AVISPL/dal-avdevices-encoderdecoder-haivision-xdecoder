/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of SRT option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
 * @since 1.0.0
 */
public enum SRTMode {

	CALLER("Caller"),
	LISTENER("Listener"),
	RENDEZVOUS("Rendezvous");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	SRTMode(String name) {
		this.name = name;
	}

	/**
	 * retrieve {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method is used to get srt mode by name
	 *
	 * @param name is the name of srt mode that want to get
	 * @return SRTMode is the srt that want to get
	 */
	public static SRTMode getByName(String name) {
		Optional<SRTMode> srtMode = Arrays.stream(SRTMode.values()).filter(com -> com.getName().equals(name)).findFirst();
		return srtMode.orElse(SRTMode.LISTENER);
	}
}

