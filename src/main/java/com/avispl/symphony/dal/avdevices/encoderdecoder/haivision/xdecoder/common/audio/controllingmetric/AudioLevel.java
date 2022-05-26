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
public enum AudioLevel {

	LEVEL_5("5"),
	LEVEL_6("6"),
	LEVEL_7("7"),
	LEVEL_8("8"),
	LEVEL_9("9"),
	LEVEL_10("10"),
	LEVEL_11("11"),
	LEVEL_12("12"),
	LEVEL_13("13"),
	LEVEL_14("14"),
	LEVEL_15("15"),
	LEVEL_16("16"),
	LEVEL_17("17"),
	LEVEL_18("18"),
	LEVEL_19("19"),
	LEVEL_20("20");

	private final String uiName;

	/**
	 * Parameterized constructor
	 * @param uiName IU name of AudioChannel
	 */
	AudioLevel(String uiName) {
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
	 * This method is used to get audio level by ui name
	 *
	 * @param uiName is the ui name of audio channel that want to get
	 * @return AudioLevel is the audio level that want to get
	 */
	public static AudioLevel getByUiName(String uiName) {
		Optional<AudioLevel> audioLevel = Arrays.stream(AudioLevel.values()).filter(level -> level.getUiName().equals(uiName)).findFirst();
		return audioLevel.orElse(AudioLevel.LEVEL_6);
	}
}
