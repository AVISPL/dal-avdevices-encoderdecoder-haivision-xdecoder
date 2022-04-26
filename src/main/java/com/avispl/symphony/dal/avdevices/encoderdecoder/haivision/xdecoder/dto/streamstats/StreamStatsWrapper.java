/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Stream info wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/11/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamStatsWrapper {

	@JsonAlias("SRT")
	private SRT srt;

	@JsonAlias("Configuration")
	private StreamConfig streamConfigInfo;

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
	public StreamConfig getStreamConfigInfo() {
		return streamConfigInfo;
	}

	/**
	 * Sets {@code streamConfigInfo}
	 *
	 * @param streamConfigInfo the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConfigInfo} field
	 */
	public void setStreamConfigInfo(StreamConfig streamConfigInfo) {
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
}
