/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.talkback.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of talkback controlling metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/3/2022
 * @since 1.0.0
 */
public enum TalkbackControllingMetric {

	ACTIVE("ActivateTalkback"),
	CURRENT_TALKBACK_ACTIVATE_STATUS("CurrentActivateTalkbackStatus"),
	ACTIVE_DECODER_SDI("DecoderSDI"),
	PRIMARY_STREAM("PrimaryStream"),
	SECONDARY_STREAM("SecondaryStream"),
	EDITED("Edited"),
	PORT("Port"),
	APPLY_CHANGE("ApplyChanges"),
	CANCEL("CancelChanges");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of service monitoring metric
	 */
	TalkbackControllingMetric(String name) {
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
	 * This method is used to get talkback controlling metric by name
	 *
	 * @param name is the name of talkback controlling metric that want to get
	 * @return TalkbackControllingMetric is the talkback controlling metric that want to get
	 */
	public static TalkbackControllingMetric getByName(String name) {
		Optional<TalkbackControllingMetric> decoderControllingMetric = Arrays.stream(TalkbackControllingMetric.values()).filter(controllingMetric -> controllingMetric.getName().equals(name)).findFirst();
		if(decoderControllingMetric.isPresent()) {
			return decoderControllingMetric.get();
		}
		throw new IllegalArgumentException("Could not find the controlling metric group with name: " + name);
	}
}

