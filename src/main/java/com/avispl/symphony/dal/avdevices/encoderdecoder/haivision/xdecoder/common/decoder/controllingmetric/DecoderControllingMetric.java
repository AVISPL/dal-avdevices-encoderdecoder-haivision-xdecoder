/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of decoder controlling metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/3/2022
 * @since 1.0.0
 */
public enum DecoderControllingMetric {

	PRIMARY_STREAM("PrimaryStream"),
	SECONDARY_STREAM("SecondaryStream"),
	STILL_IMAGE("StillImage"),
	SELECT_STILL_IMAGE("SelectStillImage"),
	STILL_IMAGE_DELAY("StillImageDelay"),
	SYNC_MODE("EnableBuffering"),
	BUFFERING_DELAY("BufferingDelay"),
	BUFFERING_MODE("BufferingMode"),
	MULTI_SYNC_BUFFERING_DELAY("MultiSyncDelay"),
	OUTPUT_RESOLUTION("OutputResolution"),
	OUTPUT_FRAME_RATE("OutputFrameRate"),
	STATE("Active"),
	APPLY_CHANGE("ApplyChanges"),
	CANCEL("CancelChanges"),
	EDITED("Edited");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	DecoderControllingMetric(String name) {
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
	 * This method is used to get decoder controlling metric by name
	 *
	 * @param name is the name of decoder controlling metric that want to get
	 * @return DecoderControllingMetric is the decoder controlling metric that want to get
	 */
	public static DecoderControllingMetric getByName(String name) {
		Optional<DecoderControllingMetric> decoderControllingMetric = Arrays.stream(DecoderControllingMetric.values()).filter(com -> com.getName().equals(name)).findFirst();
		if(decoderControllingMetric.isPresent()) {
			return decoderControllingMetric.get();
		}
		throw new IllegalArgumentException("Can not find the enum with name: " + name);
	}
}

