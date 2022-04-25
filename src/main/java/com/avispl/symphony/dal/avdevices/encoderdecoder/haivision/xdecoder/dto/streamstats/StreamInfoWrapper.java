/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamMonitoringMetric;

/**
 * Stream info wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/11/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamInfoWrapper {

	@JsonAlias("SRT")
	private SRT srt;

	@JsonAlias("Configuration")
	private StreamConfigInfo streamConfigInfo;

	@JsonAlias("Statistics")
	private StreamStats streamStats;

	@JsonAlias("streamallgetall")
	private Stream stream;

	/**
	 * Retrieves {@code {@link #srt}}
	 *
	 * @return value of {@link #srt}
	 */
	public SRT getSrt() {
		return srt;
	}

	/**
	 * Sets {@code srt}
	 *
	 * @param srt the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.SRT} field
	 */
	public void setSrt(SRT srt) {
		this.srt = srt;
	}

	/**
	 * Retrieves {@code {@link #streamConfigInfo}}
	 *
	 * @return value of {@link #streamConfigInfo}
	 */
	public StreamConfigInfo getStreamConfigInfo() {
		return streamConfigInfo;
	}

	/**
	 * Sets {@code streamConfigInfo}
	 *
	 * @param streamConfigInfo the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConfigInfo} field
	 */
	public void setStreamConfigInfo(StreamConfigInfo streamConfigInfo) {
		this.streamConfigInfo = streamConfigInfo;
	}

	/**
	 * Retrieves {@code {@link #streamStats}}
	 *
	 * @return value of {@link #streamStats}
	 */
	public StreamStats getStreamStats() {
		return streamStats;
	}

	/**
	 * Sets {@code streamStats}
	 *
	 * @param streamStats the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamStats} field
	 */
	public void setStreamStats(StreamStats streamStats) {
		this.streamStats = streamStats;
	}

	/**
	 * Retrieves {@code {@link #stream}}
	 *
	 * @return value of {@link #stream}
	 */
	public Stream getStream() {
		return stream;
	}

	/**
	 * Sets {@code stream}
	 *
	 * @param stream the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.Stream} field
	 */
	public void setStream(Stream stream) {
		this.stream = stream;
	}

	/**
	 * @param streamMonitoringMetric
	 *
	 * @return String value of Stream monitoring properties by metric
	 */
	public String getValueByStreamMonitoringMetric(StreamMonitoringMetric streamMonitoringMetric) {

		switch (streamMonitoringMetric) {
			case NAME:
				return stream.getStreamName();
			case ENCAPSULATION:
				return streamStats.getEncapsulation();
			case STATE:
				return streamStats.getState();
			case OUTPUT:
				return streamStats.getOutput();
			case SOURCE_ADDRESS:
				return streamStats.getSourceAddress();
			case BIT_RATE:
				return streamStats.getBitRate();
			case RECEIVED_PACKET:
				return streamStats.getReceivedPackets();
			case RECEIVED_BYTES:
				return streamStats.getReceivedBytes();
			case LAST_RECEIVED:
				return streamStats.getLastReceived();
			case UP_TIME:
				return streamStats.getUpTime();
			case RECONNECTIONS:
				return srt.getReconnections();
			case SNCRYPTION:
				return srt.getAeSncryption();
			case KEY_LENGTH:
				return srt.getKeyLength();
			case DECRYPTION:
				return srt.getDecryption();
			case LOST_PACKETS:
				return srt.getLostPackets();
			case SKIPPED_PACKETS:
				return srt.getSkippedPackets();
			case SENT_ACKS:
				return srt.getSentAcks();
			case SENT_NAKS:
				return srt.getSentNaks();
			case LINK_BANDWIDTH:
				return srt.getLinkBandwidth();
			case RTT:
				return srt.getRtt();
			case BUFFER:
				return srt.getBuffer();
			case LATENCY:
				return srt.getLatency();
			case ERROR_MPEG_2_TS_LOST_PACKETS:
				return streamStats.getMpeg2TSLostPackets();
			case ERROR_CORRUPTED_FRAMES:
				return streamStats.getCorruptedFrames();
			case ERROR_PAUSES:
				return streamStats.getPauses();
			case LAST_ERROR:
				return streamStats.getLastError();
			case ERROR_OCCURRED:
				return streamStats.getErrorOccurred();
			default:
				return DecoderConstant.NONE;
		}
	}
}
