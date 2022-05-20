package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.hdmiconfig;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.SurroundSound;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * Set of HDMI config info
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/16/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HDMIConfig {

	@JsonAlias("VideoSource")
	private String videoSource;

	@JsonAlias("AudioSource")
	private String audioSource;

	@JsonAlias("SurroundSound")
	private String surroundSound;

	public HDMIConfig() {
	}

	/**
	 * This constructor is used for deep clone object
	 *
	 * @param hdmiConfig hdmi config info
	 */
	public HDMIConfig(HDMIConfig hdmiConfig) {
		videoSource = hdmiConfig.getVideoSource();
		audioSource = hdmiConfig.getAudioSource();
		surroundSound = hdmiConfig.getSurroundSound();
	}

	/**
	 * Retrieves {@code {@link #videoSource}}
	 *
	 * @return value of {@link #videoSource}
	 */
	public String getVideoSource() {
		return videoSource;
	}

	/**
	 * Sets {@code videoSource}
	 *
	 * @param videoSource the {@code java.lang.String} field
	 */
	public void setVideoSource(String videoSource) {
		this.videoSource = videoSource;
	}

	/**
	 * Retrieves {@code {@link #audioSource}}
	 *
	 * @return value of {@link #audioSource}
	 */
	public String getAudioSource() {
		return audioSource;
	}

	/**
	 * Sets {@code audioSource}
	 *
	 * @param audioSource the {@code java.lang.String} field
	 */
	public void setAudioSource(String audioSource) {
		this.audioSource = audioSource;
	}

	/**
	 * Retrieves {@code {@link #surroundSound}}
	 *
	 * @return value of {@link #surroundSound}
	 */
	public String getSurroundSound() {
		return surroundSound;
	}

	/**
	 * Sets {@code surroundSound}
	 *
	 * @param surroundSound the {@code java.lang.String} field
	 */
	public void setSurroundSound(String surroundSound) {
		this.surroundSound = surroundSound;
	}

	/**
	 * This method is used to compare object in specify case
	 */
	public boolean deepEquals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SurroundSound surroundSoundEnum = SurroundSound.getByAPIName(getDefaultValueForNullData(surroundSound, DecoderConstant.EMPTY));
		HDMIConfig that = (HDMIConfig) o;
		if (surroundSoundEnum.equals(SurroundSound.STEREO)) {
			return Objects.equals(videoSource, that.videoSource)
					&& Objects.equals(surroundSound, that.surroundSound)
					&& Objects.equals(audioSource, that.audioSource);
		}else
			return Objects.equals(videoSource, that.videoSource)
					&& Objects.equals(surroundSound, that.surroundSound);

	}

	/**
	 * This method is used to create command for HDMI controlling: update
	 *
	 * @return String CLI command
	 */
	public String contributeCommand(String command, String action) {
		StringBuilder request = new StringBuilder();
		request.append(command)
				.append(DecoderConstant.SPACE)
				.append(action);

		if (!StringUtils.isNullOrEmpty(videoSource)) {
			request.append(" videoSource=").append(videoSource);
		}
		if (!StringUtils.isNullOrEmpty(audioSource)) {
			request.append(" audioSource=").append(audioSource);
		}
		if (!StringUtils.isNullOrEmpty(surroundSound)) {
			request.append(" surroundSound=").append(surroundSound);
		}

		return request.toString();
	}

	/**
	 * get default value for null data
	 *
	 * @param value value of monitoring properties
	 * @return String (none/value)
	 */
	private String getDefaultValueForNullData(String value, String defaultValue) {
		return StringUtils.isNullOrEmpty(value) ? defaultValue : value;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		HDMIConfig that = (HDMIConfig) o;
		return Objects.equals(videoSource, that.videoSource) && Objects.equals(audioSource, that.audioSource) && Objects.equals(surroundSound, that.surroundSound);
	}

	@Override
	public int hashCode() {
		return Objects.hash(videoSource, audioSource, surroundSound);
	}
}
