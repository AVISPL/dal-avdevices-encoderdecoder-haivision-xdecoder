package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.audioconfig;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.CommandOperation;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;


/**
 * Audio
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/16/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioConfig {

	private String source;
	private String channels;

	@JsonAlias("OutputSource")
	private String outputSource;

	@JsonAlias("OutputLevel")
	private String outputLevel;

	/**
	 * This constructor is used for deep clone object
	 *
	 * @param audioConfig AudioConfig info
	 */
	public AudioConfig(AudioConfig audioConfig) {
		this.source = audioConfig.getSource();
		this.channels = audioConfig.getChannels();
		this.outputSource = audioConfig.getOutputSource();
		this.outputLevel = audioConfig.getOutputLevel();
	}

	/**
	 * Non-parameterized constructor
	 */
	public AudioConfig() {
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
		AudioConfig that = (AudioConfig) o;
		return Objects.equals(source, that.source)
				&& Objects.equals(channels, that.channels)
				&& Objects.equals(outputSource, that.outputSource)
				&& Objects.equals(outputLevel, that.outputLevel);
	}

	/**
	 * This method is used to create command for decoder audio controlling
	 *
	 * @return String CLI command
	 */
	public String contributeCommand(String command) {
		return command
				+ DecoderConstant.SPACE
				+ CommandOperation.SET.getName()
				+ DecoderConstant.SPACE
				+ String.format("%s=%s", "source", this.getOutputSource())
				+ DecoderConstant.SPACE
				+ String.format("%s=%s", "level", this.getOutputLevel());
	}

	/**
	 * Retrieves {@code {@link #outputSource}}
	 *
	 * @return value of {@link #outputSource}
	 */
	public String getOutputSource() {
		return outputSource;
	}

	/**
	 * Sets {@code outputSource}
	 *
	 * @param outputSource the {@code java.lang.String} field
	 */
	public void setOutputSource(String outputSource) {
		this.outputSource = outputSource;
	}

	/**
	 * Retrieves {@code {@link #outputLevel}}
	 *
	 * @return value of {@link #outputLevel}
	 */
	public String getOutputLevel() {
		return outputLevel;
	}

	/**
	 * Sets {@code outputLevel}
	 *
	 * @param outputLevel the {@code java.lang.String} field
	 */
	public void setOutputLevel(String outputLevel) {
		this.outputLevel = outputLevel;
	}

	/**
	 * Retrieves {@code {@link #source}}
	 *
	 * @return value of {@link #source}
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets {@code source}
	 *
	 * @param source the {@code java.lang.String} field
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Retrieves {@code {@link #channels}}
	 *
	 * @return value of {@link #channels}
	 */
	public String getChannels() {
		return channels;
	}

	/**
	 * Sets {@code channels}
	 *
	 * @param channels the {@code java.lang.String} field
	 */
	public void setChannels(String channels) {
		this.channels = channels;
	}
}
