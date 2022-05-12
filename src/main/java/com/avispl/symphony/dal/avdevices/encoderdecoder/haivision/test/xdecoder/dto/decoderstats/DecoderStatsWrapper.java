/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.dto.decoderstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Decoder info wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/22/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecoderStatsWrapper {

	@JsonAlias("viddecgetall")
	private Decoder decoder;

	@JsonAlias("Configuration")
	private DecoderConfig decoderConfigInfo;

	@JsonAlias ("Statistics")
	private DecoderStats decoderStats;

	@JsonAlias("Video")
	private VideoDecoder video;

	@JsonAlias("Audio")
	private Audio audio;

	@JsonAlias("TC")
	private Timecode timecode;

	/**
	 * Retrieves {@code {@link #decoder }}
	 *
	 * @return value of {@link #decoder}
	 */
	public Decoder getDecoder() {
		return decoder;
	}

	/**
	 * Sets {@code decoderID}
	 *
	 * @param decoder the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderID} field
	 */
	public void setDecoder(Decoder decoder) {
		this.decoder = decoder;
	}

	/**
	 * Retrieves {@code {@link #decoderConfigInfo }}
	 *
	 * @return value of {@link #decoderConfigInfo}
	 */
	public DecoderConfig getDecoderConfigInfo() {
		return decoderConfigInfo;
	}

	/**
	 * Sets {@code decoderInfo}
	 *
	 * @param decoderConfigInfo the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.x4encoder.dto.decoderstats.DecoderInfo} field
	 */
	public void setDecoderConfigInfo(DecoderConfig decoderConfigInfo) {
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
}
