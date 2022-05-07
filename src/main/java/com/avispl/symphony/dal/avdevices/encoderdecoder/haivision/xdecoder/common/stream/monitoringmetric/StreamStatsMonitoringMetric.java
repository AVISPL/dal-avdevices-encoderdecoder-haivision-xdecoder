/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric;

/**
 * Set of stream monitoring metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum StreamStatsMonitoringMetric {

	// Static metric
	STATE("StatsState"),
	OUTPUT("StatsOutput"),
	SOURCE_ADDRESS("StatsSourceAddress"),
	BIT_RATE("StatsBitRate(kbps)"),
	RECEIVED_PACKET("StatsReceivedPacket"),
	RECEIVED_BYTES("StatsReceivedBytes"),
	LAST_RECEIVED("StatsLastReceived"),
	UP_TIME("StatsUpTime"),
	ERROR_MPEG_2_TS_LOST_PACKETS("ErrorMPEG2TSLostPackets"),
	ERROR_CORRUPTED_FRAMES("ErrorCorruptedFrames"),
	ERROR_PAUSES("ErrorPauses"),
	LAST_ERROR("ErrorLastError"),
	ERROR_OCCURRED("ErrorOccurred");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of Decoder monitoring metric
	 */
	StreamStatsMonitoringMetric(String name) {
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

