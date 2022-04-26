/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Decoder audio
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Audio {

	@JsonAlias("Algorithm")
	private String algorithm;

	@JsonAlias("State")
	private String state;

	@JsonAlias("Bitrate")
	private String bitrate;

	@JsonAlias("SampleRate")
	private String sampleRate;

	@JsonAlias("NumberofPair")
	private String numberOfPair;

	@JsonAlias("Audio1InputLayout")
	private String inputLayout1DecodedFrames;

	@JsonAlias("AudioOutputLayout")
	private String outputLayout;

	@JsonAlias("DecodedFrames")
	private String decodedFrames;

	@JsonAlias("OutputFrames")
	private String outputFrames;

	@JsonAlias("SkippedFrames")
	private String skippedFrames;

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
	 * Retrieves {@code {@link #sampleRate}}
	 *
	 * @return value of {@link #sampleRate}
	 */
	public String getSampleRate() {
		return sampleRate;
	}

	/**
	 * Sets {@code sampleRate}
	 *
	 * @param sampleRate the {@code java.lang.String} field
	 */
	public void setSampleRate(String sampleRate) {
		this.sampleRate = sampleRate;
	}

	/**
	 * Retrieves {@code {@link #numberOfPair}}
	 *
	 * @return value of {@link #numberOfPair}
	 */
	public String getNumberOfPair() {
		return numberOfPair;
	}

	/**
	 * Sets {@code numberOfPair}
	 *
	 * @param numberOfPair the {@code java.lang.String} field
	 */
	public void setNumberOfPair(String numberOfPair) {
		this.numberOfPair = numberOfPair;
	}

	/**
	 * Retrieves {@code {@link #inputLayout1DecodedFrames}}
	 *
	 * @return value of {@link #inputLayout1DecodedFrames}
	 */
	public String getInputLayout1DecodedFrames() {
		return inputLayout1DecodedFrames;
	}

	/**
	 * Sets {@code inputLayout1DecodedFrames}
	 *
	 * @param inputLayout1DecodedFrames the {@code java.lang.String} field
	 */
	public void setInputLayout1DecodedFrames(String inputLayout1DecodedFrames) {
		this.inputLayout1DecodedFrames = inputLayout1DecodedFrames;
	}

	/**
	 * Retrieves {@code {@link #outputLayout}}
	 *
	 * @return value of {@link #outputLayout}
	 */
	public String getOutputLayout() {
		return outputLayout;
	}

	/**
	 * Sets {@code outputLayout}
	 *
	 * @param outputLayout the {@code java.lang.String} field
	 */
	public void setOutputLayout(String outputLayout) {
		this.outputLayout = outputLayout;
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
	 * Retrieves {@code {@link #skippedFrames}}
	 *
	 * @return value of {@link #skippedFrames}
	 */
	public String getSkippedFrames() {
		return skippedFrames;
	}

	/**
	 * Sets {@code skippedFrames}
	 *
	 * @param skippedFrames the {@code java.lang.String} field
	 */
	public void setSkippedFrames(String skippedFrames) {
		this.skippedFrames = skippedFrames;
	}
}