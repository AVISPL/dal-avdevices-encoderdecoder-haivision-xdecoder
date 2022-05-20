/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of decoder controlling metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/3/2022
 * @since 1.0.0
 */
public enum HDMIControllingMetric {

	VIDEO_SOURCE("VideoSource"),
	SOUND_MODE("SoundMode"),
	AUDIO_OUT("AudioOut"),
	APPLY_CHANGE("ApplyChanges"),
	CANCEL("CancelChanges"),
	EDITED("Edited");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	HDMIControllingMetric(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method is used to get decoder controlling metric by name
	 *
	 * @param name is the name of decoder controlling metric that want to get
	 * @return DecoderControllingMetric is the decoder controlling metric that want to get
	 */
	public static HDMIControllingMetric getByName(String name) {
		Optional<HDMIControllingMetric> decoderControllingMetric = Arrays.stream(HDMIControllingMetric.values()).filter(com -> com.getName().equals(name)).findFirst();
		if(decoderControllingMetric.isPresent()) {
			return decoderControllingMetric.get();
		}
		throw new IllegalArgumentException("Can not find the enum with name: " + name);
	}
}

