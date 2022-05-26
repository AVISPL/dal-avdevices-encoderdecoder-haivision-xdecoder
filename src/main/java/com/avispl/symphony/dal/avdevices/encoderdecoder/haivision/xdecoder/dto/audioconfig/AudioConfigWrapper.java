package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.audioconfig;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Audio Config info Wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/16/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioConfigWrapper {

	@JsonAlias("AudioAnalogConfiguration")
	private AudioConfig audioConfig;

	/**
	 * Retrieves {@code {@link #audioConfig}}
	 *
	 * @return value of {@link #audioConfig}
	 */
	public AudioConfig getAudioConfig() {
		return audioConfig;
	}

	/**
	 * Sets {@code audioConfig}
	 *
	 * @param audioConfig the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.x4decoder.dto.audio.AudioConfig} field
	 */
	public void setAudioConfig(AudioConfig audioConfig) {
		this.audioConfig = audioConfig;
	}
}
