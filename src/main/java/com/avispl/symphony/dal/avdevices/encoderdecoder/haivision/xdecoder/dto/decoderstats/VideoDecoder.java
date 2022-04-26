/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Video decoder
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoDecoder {

	@JsonAlias("Algorithm")
	private String algorithm;

	@JsonAlias("Profile")
	private String profile;

	@JsonAlias("Level")
	private String level;

	@JsonAlias("State")
	private String state;

	@JsonAlias("BufferingMode")
	private String bufferingMode;

	@JsonAlias("MultisyncStatus")
	private String videoMultisyncStatus;

	@JsonAlias("MultisyncDelay")
	private String videoMultisyncDelay;

	@JsonAlias("MultisyncDelayRange")
	private String multisyncDelayRange;

	@JsonAlias("MultisyncDelaySet")
	private String multisyncDelaySet;

	@JsonAlias("InputFormat")
	private String inputFormat;

	@JsonAlias("Bitrate")
	private String bitrate;

	@JsonAlias("DecodedFrames")
	private String decodedFrames;

	@JsonAlias("OutputFormat")
	private String outputFormat;

	@JsonAlias("OutputFrames")
	private String outputFrames;

	@JsonAlias("SkippedOutputFrames")
	private String skippedOutputFrames;

	@JsonAlias("ReplayedOutputFrames")
	private String replayedOutputFrames;

	/**
	 * Retrieves {@code {@link #algorithm}}
	 *
	 * @return value of {@link #algorithm}
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * Sets {@code algorithm}
	 *
	 * @param algorithm the {@code java.lang.String} field
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Retrieves {@code {@link #profile}}
	 *
	 * @return value of {@link #profile}
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * Sets {@code profile}
	 *
	 * @param profile the {@code java.lang.String} field
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}

	/**
	 * Retrieves {@code {@link #level}}
	 *
	 * @return value of {@link #level}
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Sets {@code level}
	 *
	 * @param level the {@code java.lang.String} field
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * Retrieves {@code {@link #state}}
	 *
	 * @return value of {@link #state}
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets {@code state}
	 *
	 * @param state the {@code java.lang.String} field
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Retrieves {@code {@link #bufferingMode}}
	 *
	 * @return value of {@link #bufferingMode}
	 */
	public String getBufferingMode() {
		return bufferingMode;
	}

	/**
	 * Sets {@code bufferingMode}
	 *
	 * @param bufferingMode the {@code java.lang.String} field
	 */
	public void setBufferingMode(String bufferingMode) {
		this.bufferingMode = bufferingMode;
	}

	/**
	 * Retrieves {@code {@link #videoMultisyncStatus}}
	 *
	 * @return value of {@link #videoMultisyncStatus}
	 */
	public String getMultisyncStatus() {
		return videoMultisyncStatus;
	}

	/**
	 * Sets {@code videoMultisyncStatus}
	 *
	 * @param videoMultisyncStatus the {@code java.lang.String} field
	 */
	public void setVideoMultisyncStatus(String videoMultisyncStatus) {
		this.videoMultisyncStatus = videoMultisyncStatus;
	}

	/**
	 * Retrieves {@code {@link #videoMultisyncDelay}}
	 *
	 * @return value of {@link #videoMultisyncDelay}
	 */
	public String getMultisyncDelay() {
		return videoMultisyncDelay;
	}

	/**
	 * Sets {@code videoMultisyncDelay}
	 *
	 * @param videoMultisyncDelay the {@code java.lang.String} field
	 */
	public void setVideoMultisyncDelay(String videoMultisyncDelay) {
		this.videoMultisyncDelay = videoMultisyncDelay;
	}

	/**
	 * Retrieves {@code {@link #multisyncDelayRange}}
	 *
	 * @return value of {@link #multisyncDelayRange}
	 */
	public String getMultisyncDelayRange() {
		return multisyncDelayRange;
	}

	/**
	 * Sets {@code multisyncDelayRange}
	 *
	 * @param multisyncDelayRange the {@code java.lang.String} field
	 */
	public void setMultisyncDelayRange(String multisyncDelayRange) {
		this.multisyncDelayRange = multisyncDelayRange;
	}

	/**
	 * Retrieves {@code {@link #multisyncDelaySet}}
	 *
	 * @return value of {@link #multisyncDelaySet}
	 */
	public String getMultisyncDelaySet() {
		return multisyncDelaySet;
	}

	/**
	 * Sets {@code multisyncDelaySet}
	 *
	 * @param multisyncDelaySet the {@code java.lang.String} field
	 */
	public void setMultisyncDelaySet(String multisyncDelaySet) {
		this.multisyncDelaySet = multisyncDelaySet;
	}

	/**
	 * Retrieves {@code {@link #inputFormat}}
	 *
	 * @return value of {@link #inputFormat}
	 */
	public String getInputFormat() {
		return inputFormat;
	}

	/**
	 * Sets {@code inputFormat}
	 *
	 * @param inputFormat the {@code java.lang.String} field
	 */
	public void setInputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}

	/**
	 * Retrieves {@code {@link #bitrate}}
	 *
	 * @return value of {@link #bitrate}
	 */
	public String getBitrate() {
		return bitrate;
	}

	/**
	 * Sets {@code bitrate}
	 *
	 * @param bitrate the {@code java.lang.String} field
	 */
	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	/**
	 * Retrieves {@code {@link #decodedFrames}}
	 *
	 * @return value of {@link #decodedFrames}
	 */
	public String getDecodedFrames() {
		return decodedFrames;
	}

	/**
	 * Sets {@code decodedFrames}
	 *
	 * @param decodedFrames the {@code java.lang.String} field
	 */
	public void setDecodedFrames(String decodedFrames) {
		this.decodedFrames = decodedFrames;
	}

	/**
	 * Retrieves {@code {@link #outputFormat}}
	 *
	 * @return value of {@link #outputFormat}
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * Sets {@code outputFormat}
	 *
	 * @param outputFormat the {@code java.lang.String} field
	 */
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	/**
	 * Retrieves {@code {@link #outputFrames}}
	 *
	 * @return value of {@link #outputFrames}
	 */
	public String getOutputFrames() {
		return outputFrames;
	}

	/**
	 * Sets {@code outputFrames}
	 *
	 * @param outputFrames the {@code java.lang.String} field
	 */
	public void setOutputFrames(String outputFrames) {
		this.outputFrames = outputFrames;
	}

	/**
	 * Retrieves {@code {@link #skippedOutputFrames}}
	 *
	 * @return value of {@link #skippedOutputFrames}
	 */
	public String getSkippedOutputFrames() {
		return skippedOutputFrames;
	}

	/**
	 * Sets {@code skippedOutputFrames}
	 *
	 * @param skippedOutputFrames the {@code java.lang.String} field
	 */
	public void setSkippedOutputFrames(String skippedOutputFrames) {
		this.skippedOutputFrames = skippedOutputFrames;
	}

	/**
	 * Retrieves {@code {@link #replayedOutputFrames}}
	 *
	 * @return value of {@link #replayedOutputFrames}
	 */
	public String getReplayedOutputFrames() {
		return replayedOutputFrames;
	}

	/**
	 * Sets {@code replayedOutputFrames}
	 *
	 * @param replayedOutputFrames the {@code java.lang.String} field
	 */
	public void setReplayedOutputFrames(String replayedOutputFrames) {
		this.replayedOutputFrames = replayedOutputFrames;
	}
}
