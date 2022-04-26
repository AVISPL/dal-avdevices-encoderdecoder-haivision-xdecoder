/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Set of decoder configuration properties
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecoderConfig {

	@JsonAlias("id")
	private String id;

	@JsonAlias("streamId")
	private Integer streamId;

	@JsonAlias("altStreamId")
	private Integer altStreamId;

	@JsonAlias("state")
	private Integer state;

	@JsonAlias("latency")
	private String latency;

	@JsonAlias("stillImage")
	private Integer stillImage;

	@JsonAlias("stillImageDelay")
	private String stillImageDelay;

	@JsonAlias("szStillImageFileName")
	private String szStillImageFileName;

	@JsonAlias("enableBuffering")
	private String enableBuffering;

	@JsonAlias("bufferingMode")
	private Integer bufferingMode;

	@JsonAlias("bufferingDelay")
	private String bufferingDelay;

	@JsonAlias("multisyncBufferingDelay")
	private String multisyncBufferingDelay;

	@JsonAlias("hdrDynamicRange")
	private Integer hdrDynamicRange;

	@JsonAlias("nNumOfOutputs")
	private String nNumOfOutputs;

	@JsonAlias("output1")
	private Boolean output1;

	@JsonAlias("output2")
	private Boolean output2;

	@JsonAlias("output3")
	private Boolean output3;

	@JsonAlias("output4")
	private Boolean output4;

	@JsonAlias("outputFrameRate")
	private Integer outputFrameRate;

	@JsonAlias("previewEnabled")
	private String previewEnabled;

	@JsonAlias("previewIntervalSec")
	private String previewIntervalSec;

	@JsonAlias("quadMode")
	private Integer quadMode;

	/**
	 * Retrieves {@code {@link #id}}
	 *
	 * @return value of {@link #id}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets {@code id}
	 *
	 * @param id the {@code java.lang.String} field
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Retrieves {@code {@link #streamId}}
	 *
	 * @return value of {@link #streamId}
	 */
	public Integer getStreamId() {
		return streamId;
	}

	/**
	 * Sets {@code streamId}
	 *
	 * @param streamId the {@code java.lang.Integer} field
	 */
	public void setStreamId(Integer streamId) {
		this.streamId = streamId;
	}

	/**
	 * Retrieves {@code {@link #altStreamId}}
	 *
	 * @return value of {@link #altStreamId}
	 */
	public Integer getAltStreamId() {
		return altStreamId;
	}

	/**
	 * Sets {@code altStreamId}
	 *
	 * @param altStreamId the {@code java.lang.Integer} field
	 */
	public void setAltStreamId(Integer altStreamId) {
		this.altStreamId = altStreamId;
	}

	/**
	 * Retrieves {@code {@link #state}}
	 *
	 * @return value of {@link #state}
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * Sets {@code state}
	 *
	 * @param state the {@code java.lang.Integer} field
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * Retrieves {@code {@link #latency}}
	 *
	 * @return value of {@link #latency}
	 */
	public String getLatency() {
		return latency;
	}

	/**
	 * Sets {@code latency}
	 *
	 * @param latency the {@code java.lang.String} field
	 */
	public void setLatency(String latency) {
		this.latency = latency;
	}

	/**
	 * Retrieves {@code {@link #stillImage}}
	 *
	 * @return value of {@link #stillImage}
	 */
	public Integer getStillImage() {
		return stillImage;
	}

	/**
	 * Sets {@code stillImage}
	 *
	 * @param stillImage the {@code java.lang.Integer} field
	 */
	public void setStillImage(Integer stillImage) {
		this.stillImage = stillImage;
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
	 * Retrieves {@code {@link #szStillImageFileName}}
	 *
	 * @return value of {@link #szStillImageFileName}
	 */
	public String getSzStillImageFileName() {
		return szStillImageFileName;
	}

	/**
	 * Sets {@code szStillImageFileName}
	 *
	 * @param szStillImageFileName the {@code java.lang.String} field
	 */
	public void setSzStillImageFileName(String szStillImageFileName) {
		this.szStillImageFileName = szStillImageFileName;
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
	public Integer getBufferingMode() {
		return bufferingMode;
	}

	/**
	 * Sets {@code bufferingMode}
	 *
	 * @param bufferingMode the {@code java.lang.Integer} field
	 */
	public void setBufferingMode(Integer bufferingMode) {
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
	 * Retrieves {@code {@link #multisyncBufferingDelay}}
	 *
	 * @return value of {@link #multisyncBufferingDelay}
	 */
	public String getMultisyncBufferingDelay() {
		return multisyncBufferingDelay;
	}

	/**
	 * Sets {@code multisyncBufferingDelay}
	 *
	 * @param multisyncBufferingDelay the {@code java.lang.String} field
	 */
	public void setMultisyncBufferingDelay(String multisyncBufferingDelay) {
		this.multisyncBufferingDelay = multisyncBufferingDelay;
	}

	/**
	 * Retrieves {@code {@link #hdrDynamicRange}}
	 *
	 * @return value of {@link #hdrDynamicRange}
	 */
	public Integer getHdrDynamicRange() {
		return hdrDynamicRange;
	}

	/**
	 * Sets {@code hdrDynamicRange}
	 *
	 * @param hdrDynamicRange the {@code java.lang.Integer} field
	 */
	public void setHdrDynamicRange(Integer hdrDynamicRange) {
		this.hdrDynamicRange = hdrDynamicRange;
	}

	/**
	 * Retrieves {@code {@link #nNumOfOutputs}}
	 *
	 * @return value of {@link #nNumOfOutputs}
	 */
	public String getnNumOfOutputs() {
		return nNumOfOutputs;
	}

	/**
	 * Sets {@code nNumOfOutputs}
	 *
	 * @param nNumOfOutputs the {@code java.lang.String} field
	 */
	public void setnNumOfOutputs(String nNumOfOutputs) {
		this.nNumOfOutputs = nNumOfOutputs;
	}

	/**
	 * Retrieves {@code {@link #output1}}
	 *
	 * @return value of {@link #output1}
	 */
	public Boolean getOutput1() {
		return output1;
	}

	/**
	 * Sets {@code output1}
	 *
	 * @param output1 the {@code java.lang.Boolean} field
	 */
	public void setOutput1(Boolean output1) {
		this.output1 = output1;
	}

	/**
	 * Retrieves {@code {@link #output2}}
	 *
	 * @return value of {@link #output2}
	 */
	public Boolean getOutput2() {
		return output2;
	}

	/**
	 * Sets {@code output2}
	 *
	 * @param output2 the {@code java.lang.Boolean} field
	 */
	public void setOutput2(Boolean output2) {
		this.output2 = output2;
	}

	/**
	 * Retrieves {@code {@link #output3}}
	 *
	 * @return value of {@link #output3}
	 */
	public Boolean getOutput3() {
		return output3;
	}

	/**
	 * Sets {@code output3}
	 *
	 * @param output3 the {@code java.lang.Boolean} field
	 */
	public void setOutput3(Boolean output3) {
		this.output3 = output3;
	}

	/**
	 * Retrieves {@code {@link #output4}}
	 *
	 * @return value of {@link #output4}
	 */
	public Boolean getOutput4() {
		return output4;
	}

	/**
	 * Sets {@code output4}
	 *
	 * @param output4 the {@code java.lang.Boolean} field
	 */
	public void setOutput4(Boolean output4) {
		this.output4 = output4;
	}

	/**
	 * Retrieves {@code {@link #outputFrameRate}}
	 *
	 * @return value of {@link #outputFrameRate}
	 */
	public Integer getOutputFrameRate() {
		return outputFrameRate;
	}

	/**
	 * Sets {@code outputFrameRate}
	 *
	 * @param outputFrameRate the {@code java.lang.Integer} field
	 */
	public void setOutputFrameRate(Integer outputFrameRate) {
		this.outputFrameRate = outputFrameRate;
	}

	/**
	 * Retrieves {@code {@link #previewEnabled}}
	 *
	 * @return value of {@link #previewEnabled}
	 */
	public String getPreviewEnabled() {
		return previewEnabled;
	}

	/**
	 * Sets {@code previewEnabled}
	 *
	 * @param previewEnabled the {@code java.lang.String} field
	 */
	public void setPreviewEnabled(String previewEnabled) {
		this.previewEnabled = previewEnabled;
	}

	/**
	 * Retrieves {@code {@link #previewIntervalSec}}
	 *
	 * @return value of {@link #previewIntervalSec}
	 */
	public String getPreviewIntervalSec() {
		return previewIntervalSec;
	}

	/**
	 * Sets {@code previewIntervalSec}
	 *
	 * @param previewIntervalSec the {@code java.lang.String} field
	 */
	public void setPreviewIntervalSec(String previewIntervalSec) {
		this.previewIntervalSec = previewIntervalSec;
	}

	/**
	 * Retrieves {@code {@link #quadMode}}
	 *
	 * @return value of {@link #quadMode}
	 */
	public Integer getQuadMode() {
		return quadMode;
	}

	/**
	 * Sets {@code quadMode}
	 *
	 * @param quadMode the {@code java.lang.Integer} field
	 */
	public void setQuadMode(Integer quadMode) {
		this.quadMode = quadMode;
	}
}

