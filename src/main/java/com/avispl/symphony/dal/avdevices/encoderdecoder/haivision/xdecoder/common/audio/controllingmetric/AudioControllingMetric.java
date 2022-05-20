package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.audio.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of stream controlling metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/16/2022
 * @since 1.0.0
 */
public enum AudioControllingMetric {
	AUDIO_SOURCE("Source"),
	AUDIO_CHANNELS("Channels"),
	AUDIO_ZERO_DBFS_AUDIO_LEVEL("0 dBFS Audio Level"),
	APPLY_CHANGE("ApplyChanges"),
	CANCEL("CancelChanges"),
	EDITED("Edited");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder contorlling metric
	 */
	AudioControllingMetric(String name) {
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
	 * @return AudioControllingMetric is the decoder controlling metric that want to get
	 */
	public static AudioControllingMetric getByName(String name) {
		Optional<AudioControllingMetric> audioControllingMetric = Arrays.stream(AudioControllingMetric.values()).filter(com -> com.getName().equals(name)).findFirst();
		if (audioControllingMetric.isPresent()) {
			return audioControllingMetric.get();
		}
		throw new IllegalArgumentException("Can not find the enum with name: " + name);
	}
}
