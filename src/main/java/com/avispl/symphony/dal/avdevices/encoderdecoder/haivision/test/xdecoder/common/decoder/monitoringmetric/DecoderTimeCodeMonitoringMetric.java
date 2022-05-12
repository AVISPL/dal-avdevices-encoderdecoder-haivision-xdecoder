/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.common.decoder.monitoringmetric;

/**
 * Set of decoder monitoring metrics keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum DecoderTimeCodeMonitoringMetric {

	// Static metric
	TIMECODE_STATE("TimecodeState"),
	TIMECODE_DISPLAYED_FRAMES("TimecodeDisplayedFrames"),
	TIMECODE_PROCESSED_BYTES("TimecodeProcessedBytes"),
	CURRENT_TIMECODE("CurrentTimecode");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	DecoderTimeCodeMonitoringMetric(String name) {
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

