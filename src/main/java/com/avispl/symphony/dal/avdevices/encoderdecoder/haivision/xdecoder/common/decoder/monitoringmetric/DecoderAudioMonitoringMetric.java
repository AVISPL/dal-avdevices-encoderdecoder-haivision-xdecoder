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
public enum DecoderAudioMonitoringMetric {

	// Static metric
	AUDIO_ALGORITHM("AudioAlgorithm"),
	AUDIO_STATE("AudioState"),
	AUDIO_BITRATE("AudioBitrate(kbps)"),
	AUDIO_SAMPLE_RATE("AudioSampleRate(kHz)"),
	AUDIO_NUMBER_OF_PAIR("AudioNumberOfPair"),
	AUDIO_INPUT_LAYOUT_1("AudioInputLayout1"),
	AUDIO_OUTPUT_FRAME("AudioOutputFrame"),
	AUDIO_OUTPUT_FRAME_PERCENT("AudioOutputFramePercent"),
	AUDIO_DECODED_FRAMES("AudioDecodedFrames"),
	AUDIO_DECODED_FRAMES_PERCENT("AudioDecodedFramesPercent"),
	AUDIO_OUTPUT_LAYOUT("AudioOutputLayout"),
	AUDIO_SKIPPED_FRAMES("AudioSkippedFrames"),
	AUDIO_SKIPPED_FRAMES_PERCENT("AudioSkippedFramesPercent");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	DecoderAudioMonitoringMetric(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return this.name;
	}

}

