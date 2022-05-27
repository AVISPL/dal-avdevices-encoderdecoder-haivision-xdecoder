package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.audio.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of audio controlling metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/16/2022
 * @since 1.0.0
 */
public enum AudioChannel {

	CHANNEL_1_2("1&2"),
	CHANNEL_3_4("3&4"),
	CHANNEL_5_6("5&6"),
	CHANNEL_7_8("7&8");

	private final String uiName;

	/**
	 * Parameterized constructor
	 * @param uiName IU name of AudioChannel
	 */
	AudioChannel(String uiName) {
		this.uiName = uiName;
	}

	/**
	 * Retrieves {@code {@link #uiName}}
	 *
	 * @return value of {@link #uiName}
	 */
	public String getUiName() {
		return uiName;
	}

	/**
	 * This method is used to get audio channel by ui name
	 *
	 * @param uiName is the ui name of audio channel that want to get
	 * @return AudioChannel is the audio channel that want to get
	 */
	public static AudioChannel getByUiName(String uiName) {
		Optional<AudioChannel> audioChannel = Arrays.stream(AudioChannel.values()).filter(channel -> channel.getUiName().equals(uiName)).findFirst();
		return audioChannel.orElse(AudioChannel.CHANNEL_1_2);
	}
}
