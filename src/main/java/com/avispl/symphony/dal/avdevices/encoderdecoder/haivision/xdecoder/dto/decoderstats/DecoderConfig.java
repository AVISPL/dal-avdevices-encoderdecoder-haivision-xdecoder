/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import java.util.Objects;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.BufferingMode;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.OutputResolution;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.StillImage;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.SyncMode;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.util.StringUtils;

/**
 * Set of decoder configuration properties
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecoderConfig {

	private String decoderID;

	@JsonAlias("StreamID")
	private String primaryStream;

	@JsonAlias("AlternativeStreamID")
	private String secondaryStream;

	@JsonAlias("StillImage")
	private String stillImage;

	@JsonAlias("StillFile")
	private String  stillFile;

	@JsonAlias("StillDelay")
	private String stillImageDelay;

	@JsonAlias("SyncMode")
	private String enableBuffering;

	@JsonAlias("Buffering")
	private String bufferingMode;

	@JsonAlias("Delay")
	private String bufferingDelay;

	@JsonAlias("Resolution")
	private String outputResolution;

	@JsonAlias("FrameRate")
	private String outputFrameRate;

	@JsonAlias("State")
	private String state;

	public DecoderConfig() {
	}

	/**
	 * This constructor is used for deep clone object
	 *
	 * @param decoderConfig Decoder config info
	 */
	public DecoderConfig(DecoderConfig decoderConfig) {
		this.decoderID = decoderConfig.getDecoderID();
		this.primaryStream = decoderConfig.getPrimaryStream();
		this.secondaryStream = decoderConfig.getSecondaryStream();
		this.stillImage = decoderConfig.getStillImage();
		this.stillFile = decoderConfig.getStillFile();
		this.stillImageDelay = decoderConfig.getStillImageDelay();
		this.enableBuffering = decoderConfig.getEnableBuffering();
		this.bufferingMode = decoderConfig.getBufferingMode();
		this.bufferingDelay = decoderConfig.getBufferingDelay();
		this.outputResolution = decoderConfig.getOutputResolution();
		this.outputFrameRate = decoderConfig.getOutputFrameRate();
		this.state = decoderConfig.getState();
	}

	/**
	 * Retrieves {@code {@link #decoderID}}
	 *
	 * @return value of {@link #decoderID}
	 */
	public String getDecoderID() {
		return decoderID;
	}

	/**
	 * Sets {@code decoderID}
	 *
	 * @param decoderID the {@code java.lang.String} field
	 */
	public void setDecoderID(String decoderID) {
		this.decoderID = decoderID;
	}

	/**
	 * Retrieves {@code {@link #primaryStream}}
	 *
	 * @return value of {@link #primaryStream}
	 */
	public String getPrimaryStream() {
		return primaryStream;
	}

	/**
	 * Sets {@code primaryStream}
	 *
	 * @param primaryStream the {@code java.lang.String} field
	 */
	public void setPrimaryStream(String primaryStream) {
		this.primaryStream = primaryStream;
	}

	/**
	 * Retrieves {@code {@link #secondaryStream}}
	 *
	 * @return value of {@link #secondaryStream}
	 */
	public String getSecondaryStream() {
		return secondaryStream;
	}

	/**
	 * Sets {@code secondaryStream}
	 *
	 * @param secondaryStream the {@code java.lang.String} field
	 */
	public void setSecondaryStream(String secondaryStream) {
		this.secondaryStream = secondaryStream;
	}

	/**
	 * Retrieves {@code {@link #stillImage}}
	 *
	 * @return value of {@link #stillImage}
	 */
	public String getStillImage() {
		return stillImage;
	}

	/**
	 * Sets {@code stillImage}
	 *
	 * @param stillImage the {@code java.lang.String} field
	 */
	public void setStillImage(String stillImage) {
		this.stillImage = stillImage;
	}

	/**
	 * Retrieves {@code {@link #stillFile}}
	 *
	 * @return value of {@link #stillFile}
	 */
	public String getStillFile() {
		return stillFile;
	}

	/**
	 * Sets {@code stillFile}
	 *
	 * @param stillFile the {@code java.lang.String} field
	 */
	public void setStillFile(String stillFile) {
		this.stillFile = stillFile;
	}

	/**
	 * Retrieves {@code {@link #stillImageDelay}}
	 *
	 * @return value of {@link #stillImageDelay}
	 */
	public String getStillImageDelay() {
		return stillImageDelay;
	}

	/**
	 * Sets {@code stillImageDelay}
	 *
	 * @param stillImageDelay the {@code java.lang.String} field
	 */
	public void setStillImageDelay(String stillImageDelay) {
		this.stillImageDelay = stillImageDelay;
	}

	/**
	 * Retrieves {@code {@link #enableBuffering}}
	 *
	 * @return value of {@link #enableBuffering}
	 */
	public String getEnableBuffering() {
		return enableBuffering;
	}

	/**
	 * Sets {@code enableBuffering}
	 *
	 * @param enableBuffering the {@code java.lang.String} field
	 */
	public void setEnableBuffering(String enableBuffering) {
		this.enableBuffering = enableBuffering;
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
	 * Retrieves {@code {@link #bufferingDelay}}
	 *
	 * @return value of {@link #bufferingDelay}
	 */
	public String getBufferingDelay() {
		return bufferingDelay;
	}

	/**
	 * Sets {@code bufferingDelay}
	 *
	 * @param bufferingDelay the {@code java.lang.String} field
	 */
	public void setBufferingDelay(String bufferingDelay) {
		this.bufferingDelay = bufferingDelay;
	}

	/**
	 * Retrieves {@code {@link #outputResolution}}
	 *
	 * @return value of {@link #outputResolution}
	 */
	public String getOutputResolution() {
		return outputResolution;
	}

	/**
	 * Sets {@code outputResolution}
	 *
	 * @param outputResolution the {@code java.lang.String} field
	 */
	public void setOutputResolution(String outputResolution) {
		this.outputResolution = outputResolution;
	}

	/**
	 * Retrieves {@code {@link #outputFrameRate}}
	 *
	 * @return value of {@link #outputFrameRate}
	 */
	public String getOutputFrameRate() {
		return outputFrameRate;
	}

	/**
	 * Sets {@code outputFrameRate}
	 *
	 * @param outputFrameRate the {@code java.lang.String} field
	 */
	public void setOutputFrameRate(String outputFrameRate) {
		this.outputFrameRate = outputFrameRate;
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
	 * This method is used to compare object in specify case
	 */
	public boolean deepEquals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DecoderConfig that = (DecoderConfig) o;
		return Objects.equals(primaryStream, that.primaryStream)
				&& Objects.equals(secondaryStream, that.secondaryStream)
				&& Objects.equals(stillImage, that.stillImage)
				&& Objects.equals(NormalizeData.getDataNumberValue(stillImageDelay), NormalizeData.getDataNumberValue(that.stillImageDelay))
				&& Objects.equals(enableBuffering, that.enableBuffering)
				&& Objects.equals(outputResolution, that.outputResolution)
				&& equalsByOutputResolution(o)
				&& equalsByBufferingMode(o)
				&& equalsByStillImage(o);
	}

	/**
	 * This method is used to compare object in specify BufferingMode
	 */
	public boolean equalsByBufferingMode(Object o) {
		BufferingMode bufferingModeEnum = BufferingMode.getByAPIName(getDefaultValueForNullData(getBufferingMode(), DecoderConstant.EMPTY));
		SyncMode syncModeEnum = SyncMode.getByName(getDefaultValueForNullData(getEnableBuffering(), DecoderConstant.EMPTY));
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DecoderConfig that = (DecoderConfig) o;
		if (!syncModeEnum.isEnable()) {
			return true;
		}
		switch (bufferingModeEnum) {
			case AUTO:
			case ADAPTIVE_LOW_LATENCY:
				return Objects.equals(bufferingMode, that.bufferingMode);
			case FIXED:
			case MULTI_SYNC:
				return Objects.equals(bufferingMode, that.bufferingMode)
						&& Objects.equals(NormalizeData.getDataNumberValue(bufferingDelay), NormalizeData.getDataNumberValue(that.bufferingDelay));
			default:
				return true;
		}
	}

	/**
	 * This method is used to compare object in specify OutputResolution
	 */
	public boolean equalsByOutputResolution(Object o) {
		OutputResolution outputResolutionEnum = OutputResolution.getByAPIStatsName(getDefaultValueForNullData(getOutputResolution(), DecoderConstant.EMPTY));
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DecoderConfig that = (DecoderConfig) o;
		switch (outputResolutionEnum.getResolutionCategory()) {
			case DecoderConstant.AUTOMATIC_RESOLUTION:
			case DecoderConstant.TV_RESOLUTION:
			case DecoderConstant.COMPUTER_RESOLUTION:
				return Objects.equals(outputFrameRate, that.outputFrameRate);
			case DecoderConstant.NATIVE_RESOLUTION:
			default:
				return true;
		}
	}

	/**
	 * This method is used to compare object in specify BufferingMode
	 */
	public boolean equalsByStillImage(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DecoderConfig that = (DecoderConfig) o;
		if (this.stillImage.equals(StillImage.CUSTOM.getApiName())) {
			return stillFile.equals(that.getStillFile());
		}
		return true;
	}

	/**
	 * This method is used to create command for decoder SDI controlling: update
	 *
	 * @return String CLI command
	 */
	public String contributeCommand(String command, Integer decoderID, String action) {
		StringBuilder request = new StringBuilder();
		request.append(command)
				.append(DecoderConstant.SPACE)
				.append(decoderID.toString())
				.append(DecoderConstant.SPACE)
				.append(action);
		if (!StringUtils.isNullOrEmpty(primaryStream)) {
			String tempStream = primaryStream;
			if (primaryStream.equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
				tempStream = DecoderConstant.NONE;
			}
			request.append(" streamId=").append(tempStream);
		}
		if (!StringUtils.isNullOrEmpty(secondaryStream)) {
			String tempStream = secondaryStream;
			if (secondaryStream.equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
				tempStream = DecoderConstant.NONE;
			}
			request.append(" altStreamId=").append(tempStream);
		}
		if (!StringUtils.isNullOrEmpty(stillImage)) {
			if (stillImage.equals(StillImage.SELECT_IMAGE.getApiName())) {
				stillImage = StillImage.CUSTOM.getApiName();
			}
			request.append(" stillImage=").append(StillImage.getByAPIName(stillImage).getApiName());
		}
		if (!StringUtils.isNullOrEmpty(stillFile) && stillImage.equals(StillImage.CUSTOM.getApiName())) {
			request.append(" stillFile=").append(stillFile);
		}
		if (!StringUtils.isNullOrEmpty(stillImageDelay)) {
			request.append(" stillDelay=").append(NormalizeData.getDataNumberValue(stillImageDelay));
		}
		if (!StringUtils.isNullOrEmpty(enableBuffering)) {
			request.append(" syncMode=").append(enableBuffering);
		}
		if (!StringUtils.isNullOrEmpty(outputResolution)) {
			request.append(" resolution=").append(OutputResolution.getByAPIStatsName(outputResolution).getApiConfigName());
		}
		if (!StringUtils.isNullOrEmpty(outputFrameRate)) {
			request.append(" frameRate=").append(outputFrameRate);
		}
		if (!StringUtils.isNullOrEmpty(bufferingMode)) {
			request.append(" buffering=").append(bufferingMode);
		}
		if (!StringUtils.isNullOrEmpty(bufferingDelay)) {
			if (bufferingMode.equals(BufferingMode.FIXED.getApiName())) {
				request.append(" delay=").append(NormalizeData.getDataNumberValue(bufferingDelay));
			}
			if (bufferingMode.equals(BufferingMode.MULTI_SYNC.getApiName())) {
				request.append(" multiSyncDelay=").append(NormalizeData.getDataNumberValue(bufferingDelay));
			}
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
		DecoderConfig that = (DecoderConfig) o;
		return Objects.equals(primaryStream, that.primaryStream)
				&& Objects.equals(secondaryStream, that.secondaryStream)
				&& Objects.equals(stillImage, that.stillImage)
				&& Objects.equals(NormalizeData.getDataNumberValue(stillImageDelay), NormalizeData.getDataNumberValue(that.stillImageDelay))
				&& Objects.equals(enableBuffering, that.enableBuffering)
				&& Objects.equals(bufferingMode, that.bufferingMode)
				&& Objects.equals(NormalizeData.getDataNumberValue(bufferingDelay), NormalizeData.getDataNumberValue(that.bufferingDelay))
				&& Objects.equals(outputResolution, that.outputResolution)
				&& Objects.equals(outputFrameRate, that.outputFrameRate)
				&& Objects.equals(state, that.state);
	}

	@Override
	public int hashCode() {
		return Objects.hash(primaryStream, secondaryStream, stillImage, stillImageDelay, enableBuffering, bufferingMode, bufferingDelay, outputResolution, outputFrameRate, state);
	}
}

