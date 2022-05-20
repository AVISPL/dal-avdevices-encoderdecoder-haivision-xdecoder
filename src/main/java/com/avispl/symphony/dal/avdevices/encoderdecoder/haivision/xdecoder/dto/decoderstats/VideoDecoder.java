/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderVideoMonitoringMetric;
import com.avispl.symphony.dal.util.StringUtils;

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

	@JsonAlias("Buffering")
	private String bufferingMode;

	@JsonAlias("MultiSyncStatus")
	private String multiSyncStatus;

	@JsonAlias("BufferingDelay")
	private String bufferingDelay;

	@JsonAlias("MultiSyncDelayRange")
	private String multiSyncDelayRange;

	@JsonAlias("MultiSyncDelaySet")
	private String multiSyncDelaySet;

	@JsonAlias("InputFormat")
	private String inputFormat;

	@JsonAlias("Bitrate")
	private String bitrate;

	@JsonAlias("DecodedFrames")
	private String decodedFrames;

	@JsonAlias("OutputFormat")
	private String outputFormat;

	@JsonAlias("DisplayedFrames")
	private String outputFrames;

	@JsonAlias("SkippedFrames")
	private String skippedOutputFrames;

	@JsonAlias("ReplayedFrames")
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
	 * Retrieves {@code {@link #multiSyncStatus }}
	 *
	 * @return value of {@link #multiSyncStatus}
	 */
	public String getMultisyncStatus() {
		return multiSyncStatus;
	}

	/**
	 * Sets {@code videoMultisyncStatus}
	 *
	 * @param multiSyncStatus the {@code java.lang.String} field
	 */
	public void setMultiSyncStatus(String multiSyncStatus) {
		this.multiSyncStatus = multiSyncStatus;
	}

	/**
	 * Retrieves {@code {@link #bufferingDelay }}
	 *
	 * @return value of {@link #bufferingDelay}
	 */
	public String getMultisyncDelay() {
		return bufferingDelay;
	}

	/**
	 * Sets {@code videoMultisyncDelay}
	 *
	 * @param bufferingDelay the {@code java.lang.String} field
	 */
	public void setBufferingDelay(String bufferingDelay) {
		this.bufferingDelay = bufferingDelay;
	}

	/**
	 * Retrieves {@code {@link #multiSyncDelayRange }}
	 *
	 * @return value of {@link #multiSyncDelayRange}
	 */
	public String getMultiSyncDelayRange() {
		return multiSyncDelayRange;
	}

	/**
	 * Sets {@code multisyncDelayRange}
	 *
	 * @param multiSyncDelayRange the {@code java.lang.String} field
	 */
	public void setMultiSyncDelayRange(String multiSyncDelayRange) {
		this.multiSyncDelayRange = multiSyncDelayRange;
	}

	/**
	 * Retrieves {@code {@link #multiSyncDelaySet }}
	 *
	 * @return value of {@link #multiSyncDelaySet}
	 */
	public String getMultiSyncDelaySet() {
		return multiSyncDelaySet;
	}

	/**
	 * Sets {@code multisyncDelaySet}
	 *
	 * @param multiSyncDelaySet the {@code java.lang.String} field
	 */
	public void setMultiSyncDelaySet(String multiSyncDelaySet) {
		this.multiSyncDelaySet = multiSyncDelaySet;
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
		if (StringUtils.isNullOrEmpty(skippedOutputFrames)) {
			return DecoderConstant.DEFAULT_SKIPPED_FRAMES_VALUE;
		}
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
		if (StringUtils.isNullOrEmpty(replayedOutputFrames)) {
			return DecoderConstant.DEFAULT_SKIPPED_FRAMES_VALUE;
		}
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

	/**
	 * @return String value of decoder monitoring properties by metric
	 */
	public String getValueByDecoderMonitoringMetric(DecoderVideoMonitoringMetric decoderVideoMonitoringMetric) {
		switch (decoderVideoMonitoringMetric) {
			case VIDEO_ALGORITHM:
				return getAlgorithm();
			case VIDEO_PROFILE:
				return getProfile();
			case VIDEO_LEVEL:
				return getLevel();
			case VIDEO_STATE:
				return getState();
			case VIDEO_BUFFERING_MODE:
				return getBufferingMode();
			case MULTISYNC_STATUS:
				return getMultisyncStatus();
			case BUFFERING_DELAY:
				return NormalizeData.getDataNumberValue(getMultisyncDelay());
			case MULTISYNC_DELAY_RANGE:
				return getMultiSyncDelayRange();
			case MULTISYNC_DELAY_SET:
				return getMultiSyncDelaySet();
			case VIDEO_INPUT_FORMAT:
				return getInputFormat();
			case VIDEO_BITRATE:
				return NormalizeData.getDataNumberValue(getBitrate());
			case VIDEO_DECODED_FRAMES:
				return NormalizeData.getDataNumberValue(getDecodedFrames());
			case VIDEO_DECODED_FRAMES_PERCENT:
				return NormalizeData.getDataNumberValueBySpaceIndex(getDecodedFrames(), DecoderConstant.PERCENT_VALUE_DATA_INDEX);
			case VIDEO_OUTPUT_FORMAT:
				return getOutputFormat();
			case VIDEO_OUTPUT_FRAMES:
				return  NormalizeData.getDataNumberValue(getOutputFrames());
			case VIDEO_OUTPUT_FRAMES_PERCENT:
				return  NormalizeData.getDataNumberValueBySpaceIndex(getOutputFrames(), DecoderConstant.PERCENT_VALUE_DATA_INDEX);
			case VIDEO_SKIPPED_OUTPUT_FRAMES:
				return NormalizeData.getDataNumberValue(getSkippedOutputFrames());
			case VIDEO_SKIPPED_OUTPUT_FRAMES_PERCENT:
				return NormalizeData.getDataNumberValueBySpaceIndex(getSkippedOutputFrames(), DecoderConstant.PERCENT_VALUE_DATA_INDEX);
			case LAST_VIDEO_SKIPPED_OUTPUT_FRAMES_PERCENT:
				return NormalizeData.getDataExtraInfo(getSkippedOutputFrames());
			case VIDEO_REPLAYED_OUTPUT_FRAMES:
				return NormalizeData.getDataNumberValue(getReplayedOutputFrames());
			case VIDEO_REPLAYED_OUTPUT_FRAMES_PERCENT:
				return NormalizeData.getDataNumberValueBySpaceIndex(getReplayedOutputFrames(), DecoderConstant.PERCENT_VALUE_DATA_INDEX);
			case LAST_VIDEO_REPLAYED_OUTPUT_FRAMES:
				return NormalizeData.getDataExtraInfo(getReplayedOutputFrames());
			default:
				return DecoderConstant.EMPTY;
		}
	}
}
