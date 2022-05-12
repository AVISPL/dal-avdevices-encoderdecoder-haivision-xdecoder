/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.dto.streamstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.common.stream.monitoringmetric.StreamMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.common.DecoderConstant;

/**
 * Stream info wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/19/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamStatsWrapper {

	@JsonAlias("SRT")
	private SRT srt;

	@JsonAlias("Configuration")
	private StreamConfig streamConfig;

	@JsonAlias("Statistics")
	private StreamStats streamStats;

	@JsonAlias("streamallgetall")
	private Stream streamConversion;

	@JsonAlias("StreamConversion")
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
	 * Retrieves {@code {@link #streamConfig }}
	 *
	 * @return value of {@link #streamConfig}
	 */
	public StreamConfig getStreamConfig() {
		return streamConfig;
	}

	/**
	 * Sets {@code streamConfigInfo}
	 *
	 * @param streamConfig the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConfigInfo} field
	 */
	public void setStreamConfig(StreamConfig streamConfig) {
		this.streamConfig = streamConfig;
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
	 * @return String value of Stream monitoring properties by metric
	 */
	public String getValueByStreamMonitoringMetric(StreamMonitoringMetric streamMonitoringMetric) {
		if (StreamMonitoringMetric.NAME.equals(streamMonitoringMetric)) {
			return stream.getStreamName();
		}
		if (StreamMonitoringMetric.STREAM_ID.equals(streamMonitoringMetric)) {
			return stream.getStreamId();
		}
		if (StreamMonitoringMetric.ENCAPSULATION.equals(streamMonitoringMetric)) {
			return streamConfig.getEncapsulation();
		}
		return DecoderConstant.NONE;
	}
}
