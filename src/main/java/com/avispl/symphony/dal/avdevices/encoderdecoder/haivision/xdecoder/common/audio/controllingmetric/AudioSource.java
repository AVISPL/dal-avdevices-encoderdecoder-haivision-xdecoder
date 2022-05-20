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
public enum AudioSource {
	SDI_1("SDI1"),
	SDI_2("SDI2");

	private final String uiName;

	/**
	 * Parameterized constructor
	 * @param uiName IU name of AudioSource
	 */
	AudioSource(String uiName) {
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
	 * This method is used to get audio source by ui name
	 *
	 * @param uiName is the ui name of audio source that want to get
	 * @return AudioSource is the audio source that want to get
	 */
	public static AudioSource getByUiName(String uiName) {
		Optional<AudioSource> audioSource = Arrays.stream(AudioSource.values()).filter(com -> com.getUiName().equals(uiName)).findFirst();
		return audioSource.orElse(AudioSource.SDI_1);
	}
}
