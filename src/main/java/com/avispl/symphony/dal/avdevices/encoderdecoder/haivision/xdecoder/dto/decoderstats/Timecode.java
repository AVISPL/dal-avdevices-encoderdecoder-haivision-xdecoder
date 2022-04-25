/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Time code
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/22/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Timecode {

	@JsonAlias ("State")
	private String state;

	@JsonAlias ("DisplayedFrames")
	private String displayedFrames;

	@JsonAlias("Processed Bytes")
	private String processedBytes;

	@JsonAlias ("Statistics")
	private String currentTimecode;

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
	 * Retrieves {@code {@link #displayedFrames}}
	 *
	 * @return value of {@link #displayedFrames}
	 */
	public String getDisplayedFrames() {
		return displayedFrames;
	}

	/**
	 * Sets {@code displayedFrames}
	 *
	 * @param displayedFrames the {@code java.lang.String} field
	 */
	public void setDisplayedFrames(String displayedFrames) {
		this.displayedFrames = displayedFrames;
	}

	/**
	 * Retrieves {@code {@link #processedBytes}}
	 *
	 * @return value of {@link #processedBytes}
	 */
	public String getProcessedBytes() {
		return processedBytes;
	}

	/**
	 * Sets {@code processedBytes}
	 *
	 * @param processedBytes the {@code java.lang.String} field
	 */
	public void setProcessedBytes(String processedBytes) {
		this.processedBytes = processedBytes;
	}

	/**
	 * Retrieves {@code {@link #currentTimecode}}
	 *
	 * @return value of {@link #currentTimecode}
	 */
	public String getCurrentTimecode() {
		return currentTimecode;
	}

	/**
	 * Sets {@code currentTimecode}
	 *
	 * @param currentTimecode the {@code java.lang.String} field
	 */
	public void setCurrentTimecode(String currentTimecode) {
		this.currentTimecode = currentTimecode;
	}
}
