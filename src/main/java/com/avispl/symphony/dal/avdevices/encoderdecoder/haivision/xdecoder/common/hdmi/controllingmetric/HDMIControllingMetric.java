/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of HDMI controlling metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/3/2022
 * @since 1.0.0
 */
public enum HDMIControllingMetric {

	VIDEO_SOURCE("VideoSource"),
	SOUND_MODE("SoundMode"),
	AUDIO_OUT("AudioOut"),
	FRAME_RATE("CurrentFrameRate"),
	VIDEO_SOURCE_STATE("VideoSourceState"),
	RESOLUTION("CurrentResolution"),
	APPLY_CHANGE("ApplyChanges"),
	CANCEL("CancelChanges"),
	EDITED("Edited");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of hdmi monitoring metric
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
	 * This method is used to get hdmi controlling metric by name
	 *
	 * @param name is the name of hdmi controlling metric that want to get
	 * @return HDMIControllingMetric is the hdmi controlling metric that want to get
	 */
	public static HDMIControllingMetric getByName(String name) {
		Optional<HDMIControllingMetric> hdmiControllingMetric = Arrays.stream(HDMIControllingMetric.values()).filter(com -> com.getName().equals(name)).findFirst();
		if(hdmiControllingMetric.isPresent()) {
			return hdmiControllingMetric.get();
		}
		throw new IllegalArgumentException("Can not find the enum with name: " + name);
	}
}

