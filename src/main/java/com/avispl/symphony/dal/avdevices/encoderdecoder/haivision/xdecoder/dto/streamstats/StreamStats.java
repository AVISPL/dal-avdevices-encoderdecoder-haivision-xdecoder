/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamStatsMonitoringMetric;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Stream statistics
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/19/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamStats {

	@JsonAlias("Output")
	private String output;

	@JsonAlias("State")
	private String state;

	@JsonAlias("SourceAddress")
	private String sourceAddress;

	@JsonAlias("Bitrate")
	private String bitRate;

	@JsonAlias("ReceivedPackets")
	private String receivedPackets;

	@JsonAlias("ReceivedBytes")
	private String receivedBytes;

	@JsonAlias("LastReceived")
	private String lastReceived;

	@JsonAlias("UpTime")
	private String upTime;

	@JsonAlias("MPEG2TSLostPackets")
	private String mpeg2TSLostPackets;

	@JsonAlias("CorruptedFrames")
	private String corruptedFrames;

	@JsonAlias("Pauses")
	private String pauses;

	@JsonAlias("LastError")
	private String lastError;

	@JsonAlias("Occurred")
	private String errorOccurred;

	/**
	 * Retrieves {@code {@link #output }}
	 *
	 * @return value of {@link #output}
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * Sets {@code decoderId}
	 *
	 * @param output the {@code java.lang.String} field
	 */
	public void setOutput(String output) {
		this.output = output;
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
	 * @param state the {@code java.lang.Integer} field
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Retrieves {@code {@link #sourceAddress}}
	 *
	 * @return value of {@link #sourceAddress}
	 */
	public String getSourceAddress() {
		return sourceAddress;
	}

	/**
	 * Sets {@code sourceAddress}
	 *
	 * @param sourceAddress the {@code java.lang.String} field
	 */
	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	/**
	 * Retrieves {@code {@link #bitRate}}
	 *
	 * @return value of {@link #bitRate}
	 */
	public String getBitRate() {
		return bitRate;
	}

	/**
	 * Sets {@code bitRate}
	 *
	 * @param bitRate the {@code java.lang.String} field
	 */
	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}

	/**
	 * Retrieves {@code {@link #receivedPackets}}
	 *
	 * @return value of {@link #receivedPackets}
	 */
	public String getReceivedPackets() {
		return receivedPackets;
	}

	/**
	 * Sets {@code receivedPackets}
	 *
	 * @param receivedPackets the {@code java.lang.String} field
	 */
	public void setReceivedPackets(String receivedPackets) {
		this.receivedPackets = receivedPackets;
	}

	/**
	 * Retrieves {@code {@link #receivedBytes}}
	 *
	 * @return value of {@link #receivedBytes}
	 */
	public String getReceivedBytes() {
		return receivedBytes;
	}

	/**
	 * Sets {@code receivedBytes}
	 *
	 * @param receivedBytes the {@code java.lang.String} field
	 */
	public void setReceivedBytes(String receivedBytes) {
		this.receivedBytes = receivedBytes;
	}

	/**
	 * Retrieves {@code {@link #lastReceived}}
	 *
	 * @return value of {@link #lastReceived}
	 */
	public String getLastReceived() {
		return lastReceived;
	}

	/**
	 * Sets {@code lastReceived}
	 *
	 * @param lastReceived the {@code java.lang.String} field
	 */
	public void setLastReceived(String lastReceived) {
		this.lastReceived = lastReceived;
	}

	/**
	 * Retrieves {@code {@link #upTime}}
	 *
	 * @return value of {@link #upTime}
	 */
	public String getUpTime() {
		return upTime;
	}

	/**
	 * Sets {@code upTime}
	 *
	 * @param upTime the {@code java.lang.String} field
	 */
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	/**
	 * Retrieves {@code {@link #mpeg2TSLostPackets}}
	 *
	 * @return value of {@link #mpeg2TSLostPackets}
	 */
	public String getMpeg2TSLostPackets() {
		return mpeg2TSLostPackets;
	}

	/**
	 * Sets {@code mpeg2TSLostPackets}
	 *
	 * @param mpeg2TSLostPackets the {@code java.lang.String} field
	 */
	public void setMpeg2TSLostPackets(String mpeg2TSLostPackets) {
		this.mpeg2TSLostPackets = mpeg2TSLostPackets;
	}

	/**
	 * Retrieves {@code {@link #corruptedFrames}}
	 *
	 * @return value of {@link #corruptedFrames}
	 */
	public String getCorruptedFrames() {
		return corruptedFrames;
	}

	/**
	 * Sets {@code corruptedFrames}
	 *
	 * @param corruptedFrames the {@code java.lang.String} field
	 */
	public void setCorruptedFrames(String corruptedFrames) {
		this.corruptedFrames = corruptedFrames;
	}

	/**
	 * Retrieves {@code {@link #pauses}}
	 *
	 * @return value of {@link #pauses}
	 */
	public String getPauses() {
		return pauses;
	}

	/**
	 * Sets {@code pauses}
	 *
	 * @param pauses the {@code java.lang.String} field
	 */
	public void setPauses(String pauses) {
		this.pauses = pauses;
	}

	/**
	 * Retrieves {@code {@link #lastError}}
	 *
	 * @return value of {@link #lastError}
	 */
	public String getLastError() {
		return lastError;
	}

	/**
	 * Sets {@code lastError}
	 *
	 * @param lastError the {@code java.lang.String} field
	 */
	public void setLastError(String lastError) {
		this.lastError = lastError;
	}

	/**
	 * Retrieves {@code {@link #errorOccurred}}
	 *
	 * @return value of {@link #errorOccurred}
	 */
	public String getErrorOccurred() {
		return errorOccurred;
	}

	/**
	 * Sets {@code errorOccurred}
	 *
	 * @param errorOccurred the {@code java.lang.String} field
	 */
	public void setErrorOccurred(String errorOccurred) {
		this.errorOccurred = errorOccurred;
	}

	/**
	 * @return String value of Stream monitoring properties by metric
	 */
	public String getValueByStreamMonitoringMetric(StreamStatsMonitoringMetric streamStatsMonitoringMetric) {
		switch (streamStatsMonitoringMetric) {
			case STATE:
				return getState();
			case OUTPUT:
				return getOutput();
			case SOURCE_ADDRESS:
				return getSourceAddress();
			case BIT_RATE:
				return NormalizeData.extractNumbers(getBitRate());
			case RECEIVED_PACKET:
				return NormalizeData.convertToNumberValue(getReceivedPackets());
			case RECEIVED_BYTES:
				return NormalizeData.convertToNumberValue(getReceivedBytes());
			case LAST_RECEIVED:
				return getLastReceived();
			case UP_TIME:
				return NormalizeData.formatTimeData(getUpTime());
			case ERROR_MPEG_2_TS_LOST_PACKETS:
				return getMpeg2TSLostPackets();
			case ERROR_CORRUPTED_FRAMES:
				return getCorruptedFrames();
			case ERROR_PAUSES:
				return getPauses();
			case LAST_ERROR:
				return getLastError();
			case ERROR_OCCURRED:
				return NormalizeData.formatTimeData(getErrorOccurred());
			default:
				return DecoderConstant.NONE;
		}
	}
}
