/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric;

/**
 * Set of srt monitoring metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum SRTMonitoringMetric {

	// Static metric
	RECONNECTIONS("SRTReconnections"),
	SNCRYPTION("SRTAESncryption"),
	KEY_LENGTH("SRTKeyLength"),
	DECRYPTION("SRTDecryption"),
	LOST_PACKETS("SRTLostPackets"),
	SKIPPED_PACKETS("SRTSkippedPackets"),
	SENT_ACKS("SRTSentAcks"),
	SENT_NAKS("SRTSentNaks"),
	LINK_BANDWIDTH("SRTLinkBandwidth"),
	RTT("SRTRtt"),
	BUFFER("SRTBuffer"),
	LATENCY("SRTLatency");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of Decoder monitoring metric
	 */
	SRTMonitoringMetric(String name) {
		this.name = name;
	}

	/**
	 * retrieve {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return this.name;
	}
}
