/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Stream
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stream {

	@JsonAlias("StreamID")
	private String streamId;

	@JsonAlias("Name")
	private String streamName;

	/**
	 * Retrieves {@code {@link #streamId}}
	 *
	 * @return value of {@link #streamId}
	 */
	public String getStreamId() {
		return streamId;
	}

	/**
	 * Sets {@code streamId}
	 *
	 * @param streamId the {@code java.lang.String} field
	 */
	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	/**
	 * Retrieves {@code {@link #streamName}}
	 *
	 * @return value of {@link #streamName}
	 */
	public String getStreamName() {
		return streamName;
	}

	/**
	 * Sets {@code streamName}
	 *
	 * @param streamName the {@code java.lang.String} field
	 */
	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

}
