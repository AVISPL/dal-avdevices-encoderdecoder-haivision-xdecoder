/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamMonitoringMetric;

/**
 * Stream SRT
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SRT {

	@JsonAlias("Reconnections")
	private String reconnections;

	@JsonAlias("AESncryption")
	private String aeSncryption;

	@JsonAlias("KeyLength")
	private String keyLength;

	@JsonAlias("Decryption")
	private String decryption;

	@JsonAlias("LostPackets")
	private String lostPackets;

	@JsonAlias("SkippedPackets")
	private String skippedPackets;

	@JsonAlias("SentAcks")
	private String sentAcks;

	@JsonAlias("SentNaks")
	private String sentNaks;

	@JsonAlias("LinkBandwidth")
	private String linkBandwidth;

	@JsonAlias("Rtt")
	private String rtt;

	@JsonAlias("Buffer")
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
	 * Retrieves {@code {@link #aeSncryption}}
	 *
	 * @return value of {@link #aeSncryption}
	 */
	public String getAeSncryption() {
		return aeSncryption;
	}

	/**
	 * Sets {@code aeSncryption}
	 *
	 * @param aeSncryption the {@code java.lang.String} field
	 */
	public void setAeSncryption(String aeSncryption) {
		this.aeSncryption = aeSncryption;
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
	 * @param streamMonitoringMetric
	 *
	 * @return String value of Stream monitoring properties by metric
	 */
	public String getValueByStreamMonitoringMetric(StreamMonitoringMetric streamMonitoringMetric) {
		switch (streamMonitoringMetric) {
			case RECONNECTIONS:
				return getReconnections();
			case SNCRYPTION:
				return getAeSncryption();
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
				return getLinkBandwidth();
			case RTT:
				return getRtt();
			case BUFFER:
				return getBuffer();
			case LATENCY:
				return getLatency();
			default:
				return DecoderConstant.NONE;
		}
	}
}
