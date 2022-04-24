/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderMonitoringMetric;

/**
 * Decoder Data
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/22/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecoderInfoWrapper {

	@JsonAlias ("viddegetall")
	private Viddec viddec;

	@JsonAlias("Configuration")
	private DecoderConfigInfo decoderConfigInfo;

	@JsonAlias ("Statistics")
	private DecoderStats decoderStats;

	@JsonAlias("Video")
	private VideoDecoder video;

	@JsonAlias("Audio")
	private Audio audio;

	@JsonAlias("TC")
	private Timecode timecode;

	/**
	 * Retrieves {@code {@link #viddec }}
	 *
	 * @return value of {@link #viddec}
	 */
	public Viddec getViddec() {
		return viddec;
	}

	/**
	 * Sets {@code decoderID}
	 *
	 * @param viddec the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderID} field
	 */
	public void setViddec(Viddec viddec) {
		this.viddec = viddec;
	}

	/**
	 * Retrieves {@code {@link #decoderConfigInfo }}
	 *
	 * @return value of {@link #decoderConfigInfo}
	 */
	public DecoderConfigInfo getDecoderConfigInfo() {
		return decoderConfigInfo;
	}

	/**
	 * Sets {@code decoderInfo}
	 *
	 * @param decoderConfigInfo the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.x4encoder.dto.decoderstats.DecoderInfo} field
	 */
	public void setDecoderConfigInfo(DecoderConfigInfo decoderConfigInfo) {
		this.decoderConfigInfo = decoderConfigInfo;
	}

	/**
	 * Retrieves {@code {@link #decoderStats}}
	 *
	 * @return value of {@link #decoderStats}
	 */
	public DecoderStats getDecoderStats() {
		return decoderStats;
	}

	/**
	 * Sets {@code decoderStats}
	 *
	 * @param decoderStats the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.x4encoder.dto.decoderstats.DecoderStats} field
	 */
	public void setDecoderStats(DecoderStats decoderStats) {
		this.decoderStats = decoderStats;
	}

	/**
	 * Retrieves {@code {@link #video}}
	 *
	 * @return value of {@link #video}
	 */
	public VideoDecoder getVideo() {
		return video;
	}

	/**
	 * Sets {@code video}
	 *
	 * @param video the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.x4encoder.dto.decoderstats.VideoDecoder} field
	 */
	public void setVideo(VideoDecoder video) {
		this.video = video;
	}

	/**
	 * Retrieves {@code {@link #audio}}
	 *
	 * @return value of {@link #audio}
	 */
	public Audio getAudio() {
		return audio;
	}

	/**
	 * Sets {@code audio}
	 *
	 * @param audio the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.x4encoder.dto.decoderstats.Audio} field
	 */
	public void setAudio(Audio audio) {
		this.audio = audio;
	}

	/**
	 * Retrieves {@code {@link #timecode}}
	 *
	 * @return value of {@link #timecode}
	 */
	public Timecode getTimecode() {
		return timecode;
	}

	/**
	 * Sets {@code timecode}
	 *
	 * @param timecode the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.Timecode} field
	 */
	public void setTimecode(Timecode timecode) {
		this.timecode = timecode;
	}

		/**
	 * @param decoderMonitoringMetric
	 *
	 * @return String value of decoder monitoring properties by metric
	 */
	public String getValueByDecoderMonitoringMetric(DecoderMonitoringMetric decoderMonitoringMetric) {
		switch (decoderMonitoringMetric) {
			case UPTIME:
				return decoderStats.getUptime();
			case RESTARTS:
				return decoderStats.getRestarts();
			case VIDEO_ALGORITHM:
				return video.getAlgorithm();
			case VIDEO_PROFILE:
				return video.getProfile();
			case VIDEO_LEVEL:
				return video.getLevel();
			case VIDEO_STATE:
				return video.getState();
			case VIDEO_BUFFERING_MODE:
				return video.getBufferingMode();
			case MULTISYNC_STATUS:
				return video.getMultisyncStatus();
			case MULTISYNC_DELAY:
				return video.getMultisyncDelay();
			case MULTISYNC_DELAY_RANGE:
				return video.getMultisyncDelayRange();
			case MULTISYNC_DELAY_SET:
				return video.getMultisyncDelaySet();
			case VIDEO_INPUT_FORMAT:
				return video.getInputFormat();
			case VIDEO_BITRATE:
				return video.getBitrate();
			case VIDEO_DECODED_FRAMES:
				return video.getDecodedFrames();
			case VIDEO_OUTPUT_FORMAT:
				return video.getOutputFormat();
			case VIDEO_OUTPUT_FRAMES:
				return video.getOutputFrames();
			case VIDEO_SKIPPED_OUTPUT_FRAMES:
				return video.getSkippedOutputFrames();
			case VIDEO_REPLAYED_OUTPUT_FRAMES:
				return video.getReplayedOutputFrames();
			case AUDIO_ALGORITHM:
				return audio.getAlgorithm();
			case AUDIO_STATE:
				return audio.getState();
			case AUDIO_BITRATE:
				return audio.getBitrate();
			case AUDIO_SAMPLE_RATE:
				return audio.getSampleRate();
			case AUDIO_NUMBER_OF_PAIR:
				return audio.getNumberOfPair();
			case AUDIO_INPUT_LAYOUT_1:
				return audio.getInputLayout1DecodedFrames();
			case AUDIO_DECODED_FRAMES:
				return audio.getDecodedFrames();
			case AUDIO_OUTPUT_FRAME:
				return audio.getOutputFrames();
			case AUDIO_OUTPUT_LAYOUT:
				return audio.getOutputLayout();
			case AUDIO_SKIPPED_FRAMES:
				return audio.getSkippedFrames();
			case KLV:
				return viddec.getKeyLengthValue();
			case TC:
				return viddec.getTimecode();
			case TIMECODE_STATE:
				return timecode.getState();
			case CURRENT_TIMECODE:
				return timecode.getCurrentTimecode();
			case TIMECODE_PROCESSED_BYTES:
				return timecode.getProcessedBytes();
			case TIMECODE_DISPLAYED_FRAMES:
				return timecode.getDisplayedFrames();
			case CC:
				return viddec.getClosedCaptioning();
			case AFD:
				return viddec.getActiveFormatDescription();
			default:
				return DecoderConstant.EMPTY;
		}
	}
}
