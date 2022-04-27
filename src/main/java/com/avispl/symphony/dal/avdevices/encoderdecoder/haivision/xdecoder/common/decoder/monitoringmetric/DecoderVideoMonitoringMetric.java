/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric;

/**
 * Set of decoder monitoring metrics keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum DecoderVideoMonitoringMetric {

	// Static metric
	VIDEO_ALGORITHM("VideoAlgorithm"),
	VIDEO_PROFILE("VideoProfile"),
	VIDEO_LEVEL("VideoLevel"),
	VIDEO_STATE("VideoState"),
	VIDEO_BUFFERING_MODE("VideoBufferingMode"),
	MULTISYNC_STATUS("MultisyncStatus"),
	MULTISYNC_DELAY("MultisyncDelay"),
	MULTISYNC_DELAY_RANGE("MultisyncDelayRange"),
	MULTISYNC_DELAY_SET("MultisyncDelaySet"),
	VIDEO_INPUT_FORMAT("VideoInputFormat"),
	VIDEO_BITRATE("VideoBitrate"),
	VIDEO_DECODED_FRAMES("VideoDecodedFrames"),
	VIDEO_OUTPUT_FORMAT("VideoOutputFormat"),
	VIDEO_OUTPUT_FRAMES("VideoOutputFrames"),
	VIDEO_SKIPPED_OUTPUT_FRAMES("VideoSkippedOutputFrames"),
	VIDEO_REPLAYED_OUTPUT_FRAMES("VideoReplayedOutputFrames");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	DecoderVideoMonitoringMetric(String name) {
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

