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
public enum DecoderMonitoringMetric {

	// Static metric
	ID("DecoderID"),
	UPTIME("Uptime"),
	RESTARTS("Restarts"),
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
	VIDEO_REPLAYED_OUTPUT_FRAMES("VideoReplayedOutputFrames"),
	AUDIO_ALGORITHM("AudioAlgorithm"),
	AUDIO_STATE("AudioState"),
	AUDIO_BITRATE("AudioBitrate"),
	AUDIO_SAMPLE_RATE("AudioSampleRate"),
	AUDIO_NUMBER_OF_PAIR("AudioNumberOfPair"),
	AUDIO_INPUT_LAYOUT_1("AudioInputLayout1"),
	AUDIO_DECODED_FRAMES("AudioDecodedFrames"),
	AUDIO_OUTPUT_FRAME("AudioOutputFrame"),
	AUDIO_OUTPUT_LAYOUT("AudioOutputLayout"),
	AUDIO_SKIPPED_FRAMES("AudioSkippedFrames"),
	KLV("KeyLengthValue"),
	TC("Timecode"),
	TIMECODE_STATE("TimecodeState"),
	TIMECODE_DISPLAYED_FRAMES("TimecodeDisplayedFrames"),
	TIMECODE_PROCESSED_BYTES("TimecodeProcessedBytes"),
	CURRENT_TIMECODE("CurrentTimecode"),
	CC("ClosedCaptioning"),
	AFD("AActiveFormatDescription");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	DecoderMonitoringMetric(String name) {
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

