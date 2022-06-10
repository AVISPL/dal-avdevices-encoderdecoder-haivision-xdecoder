/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.SRTMonitoringMetric;
import com.avispl.symphony.dal.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Stream SRT
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/19/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SRT {

	@JsonAlias("Reconnections")
	private String reconnections;

	@JsonAlias("KeyLength")
	private String keyLength;

	@JsonAlias("Decryption")
	private String decryption;

	@JsonAlias("LostPackets")
	private String lostPackets;

	@JsonAlias("DroppedPackets")
	private String skippedPackets;

	@JsonAlias("SentACKs")
	private String sentAcks;

	@JsonAlias("SentNAKs")
	private String sentNaks;

	@JsonAlias("LinkBandwidth")
	private String linkBandwidth;

	@JsonAlias("RTT")
	private String rtt;

	@JsonAlias("LocalBufferLevel")
	private String buffer;

	@JsonAlias("Latency")
	private String latency;

	/**
	 * Retrieves {@code {@link #reconnections}}
	 *
	 * @return value of {@link #reconnections}
	 */
	public String getReconnections() {
		return reconnections;
	}

	/**
	 * Sets {@code reconnections}
	 *
	 * @param reconnections the {@code java.lang.String} field
	 */
	public void setReconnections(String reconnections) {
		this.reconnections = reconnections;
	}

	/**
	 * Retrieves {@code {@link #keyLength}}
	 *
	 * @return value of {@link #keyLength}
	 */
	public String getKeyLength() {
		return keyLength;
	}

	/**
	 * Sets {@code keyLength}
	 *
	 * @param keyLength the {@code java.lang.String} field
	 */
	public void setKeyLength(String keyLength) {
		this.keyLength = keyLength;
	}

	/**
	 * Retrieves {@code {@link #decryption}}
	 *
	 * @return value of {@link #decryption}
	 */
	public String getDecryption() {
		return decryption;
	}

	/**
	 * Sets {@code decryption}
	 *
	 * @param decryption the {@code java.lang.String} field
	 */
	public void setDecryption(String decryption) {
		this.decryption = decryption;
	}

	/**
	 * Retrieves {@code {@link #lostPackets}}
	 *
	 * @return value of {@link #lostPackets}
	 */
	public String getLostPackets() {
		return lostPackets;
	}

	/**
	 * Sets {@code lostPackets}
	 *
	 * @param lostPackets the {@code java.lang.String} field
	 */
	public void setLostPackets(String lostPackets) {
		this.lostPackets = lostPackets;
	}

	/**
	 * Retrieves {@code {@link #skippedPackets}}
	 *
	 * @return value of {@link #skippedPackets}
	 */
	public String getSkippedPackets() {
		return skippedPackets;
	}

	/**
	 * Sets {@code skippedPackets}
	 *
	 * @param skippedPackets the {@code java.lang.String} field
	 */
	public void setSkippedPackets(String skippedPackets) {
		this.skippedPackets = skippedPackets;
	}

	/**
	 * Retrieves {@code {@link #sentAcks}}
	 *
	 * @return value of {@link #sentAcks}
	 */
	public String getSentAcks() {
		return sentAcks;
	}

	/**
	 * Sets {@code sentAcks}
	 *
	 * @param sentAcks the {@code java.lang.String} field
	 */
	public void setSentAcks(String sentAcks) {
		this.sentAcks = sentAcks;
	}

	/**
	 * Retrieves {@code {@link #sentNaks}}
	 *
	 * @return value of {@link #sentNaks}
	 */
	public String getSentNaks() {
		return sentNaks;
	}

	/**
	 * Sets {@code sentNaks}
	 *
	 * @param sentNaks the {@code java.lang.String} field
	 */
	public void setSentNaks(String sentNaks) {
		this.sentNaks = sentNaks;
	}

	/**
	 * Retrieves {@code {@link #linkBandwidth}}
	 *
	 * @return value of {@link #linkBandwidth}
	 */
	public String getLinkBandwidth() {
		return linkBandwidth;
	}

	/**
	 * Sets {@code linkBandwidth}
	 *
	 * @param linkBandwidth the {@code java.lang.String} field
	 */
	public void setLinkBandwidth(String linkBandwidth) {
		this.linkBandwidth = linkBandwidth;
	}

	/**
	 * Retrieves {@code {@link #rtt}}
	 *
	 * @return value of {@link #rtt}
	 */
	public String getRtt() {
		return rtt;
	}

	/**
	 * Sets {@code rtt}
	 *
	 * @param rtt the {@code java.lang.String} field
	 */
	public void setRtt(String rtt) {
		this.rtt = rtt;
	}

	/**
	 * Retrieves {@code {@link #buffer}}
	 *
	 * @return value of {@link #buffer}
	 */
	public String getBuffer() {
		return buffer;
	}

	/**
	 * Sets {@code buffer}
	 *
	 * @param buffer the {@code java.lang.String} field
	 */
	public void setBuffer(String buffer) {
		this.buffer = buffer;
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
	 * @return String value of Stream monitoring properties by metric
	 */
	public String getValueByStreamMonitoringMetric(SRTMonitoringMetric srtMonitoringMetric) {
		switch (srtMonitoringMetric) {
			case RECONNECTIONS:
				return getReconnections();
			case KEY_LENGTH:
				return getKeyLength();
			case DECRYPTION:
				return getDecryption();
			case LOST_PACKETS:
				return getLostPackets();
			case SKIPPED_PACKETS:
				return getSkippedPackets();
			case SENT_ACKS:
				return getSentAcks();
			case SENT_NAKS:
				return getSentNaks();
			case LINK_BANDWIDTH:
				if(StringUtils.isNotNullOrEmpty(getLinkBandwidth()) && !getLinkBandwidth().matches(DecoderConstant.REGEX_ONLY_GET_NUMBER))
					return getLinkBandwidth();
				return NormalizeData.extractNumbers(getLinkBandwidth());
			case RTT:
				return NormalizeData.extractNumbers(getRtt());
			case BUFFER:
				return NormalizeData.extractNumbers(getBuffer());
			case LATENCY:
				return NormalizeData.extractNumbers(getLatency());
			default:
				return DecoderConstant.NONE;
		}
	}
}
