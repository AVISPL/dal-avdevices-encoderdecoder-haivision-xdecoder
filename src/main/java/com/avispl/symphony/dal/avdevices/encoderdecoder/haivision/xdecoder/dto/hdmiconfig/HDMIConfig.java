package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.hdmiconfig;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * HDMI config info
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

	@JsonAlias("CurrentResolution")
	private String currentResolution;

	@JsonAlias("CurrentFrameRate")
	private String currentFrameRate;

	@JsonAlias("Decoder1State")
	private String decoderSDI1State;

	@JsonAlias("Decoder2State")
	private String decoderSDI2State;

	/**
	 * Non-parameterized constructor
	 */
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
		currentResolution = hdmiConfig.getCurrentResolution();
		currentFrameRate = hdmiConfig.getCurrentFrameRate();
		decoderSDI1State = hdmiConfig.getDecoderSDI1State();
		decoderSDI2State = hdmiConfig.getDecoderSDI2State();
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
	 * Retrieves {@code {@link #currentResolution}}
	 *
	 * @return value of {@link #currentResolution}
	 */
	public String getCurrentResolution() {
		return currentResolution;
	}

	/**
	 * Sets {@code currentResolution}
	 *
	 * @param currentResolution the {@code java.lang.String} field
	 */
	public void setCurrentResolution(String currentResolution) {
		this.currentResolution = currentResolution;
	}

	/**
	 * Retrieves {@code {@link #currentFrameRate}}
	 *
	 * @return value of {@link #currentFrameRate}
	 */
	public String getCurrentFrameRate() {
		return currentFrameRate;
	}

	/**
	 * Sets {@code currentFrameRate}
	 *
	 * @param currentFrameRate the {@code java.lang.String} field
	 */
	public void setCurrentFrameRate(String currentFrameRate) {
		this.currentFrameRate = currentFrameRate;
	}

	/**
	 * Retrieves {@code {@link #decoderSDI1State}}
	 *
	 * @return value of {@link #decoderSDI1State}
	 */
	public String getDecoderSDI1State() {
		return decoderSDI1State;
	}

	/**
	 * Sets {@code decoderSDI1State}
	 *
	 * @param decoderSDI1State the {@code java.lang.String} field
	 */
	public void setDecoderSDI1State(String decoderSDI1State) {
		this.decoderSDI1State = decoderSDI1State;
	}

	/**
	 * Retrieves {@code {@link #decoderSDI2State}}
	 *
	 * @return value of {@link #decoderSDI2State}
	 */
	public String getDecoderSDI2State() {
		return decoderSDI2State;
	}

	/**
	 * Sets {@code decoderSDI2State}
	 *
	 * @param decoderSDI2State the {@code java.lang.String} field
	 */
	public void setDecoderSDI2State(String decoderSDI2State) {
		this.decoderSDI2State = decoderSDI2State;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		HDMIConfig that = (HDMIConfig) o;
		return Objects.equals(videoSource, that.videoSource) && Objects.equals(audioSource, that.audioSource) && Objects.equals(surroundSound, that.surroundSound)
				&& Objects.equals(currentResolution, that.currentResolution) && Objects.equals(currentFrameRate, that.currentFrameRate) && Objects.equals(decoderSDI1State,
				that.decoderSDI1State) && Objects.equals(decoderSDI2State, that.decoderSDI2State);
	}

	@Override
	public int hashCode() {
		return Objects.hash(videoSource, audioSource, surroundSound, currentResolution, currentFrameRate, decoderSDI1State, decoderSDI2State);
	}
}
