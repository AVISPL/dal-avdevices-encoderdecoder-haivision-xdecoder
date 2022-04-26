/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Decoder Data
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/22/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Decoder {

	@JsonAlias("KLV")
	private String keyLengthValue;

	@JsonAlias("CC")
	private String closedCaptioning;

	@JsonAlias("TC")
	private String timecode;

	@JsonAlias("AFD")
	private String activeFormatDescription;

	/**
	 * Retrieves {@code {@link #keyLengthValue }}
	 *
	 * @return value of {@link #keyLengthValue}
	 */
	public String getKeyLengthValue() {
		return keyLengthValue;
	}

	/**
	 * Sets {@code klv}
	 *
	 * @param keyLengthValue the {@code java.lang.String} field
	 */
	public void setKeyLengthValue(String keyLengthValue) {
		this.keyLengthValue = keyLengthValue;
	}

	/**
	 * Retrieves {@code {@link #closedCaptioning }}
	 *
	 * @return value of {@link #closedCaptioning}
	 */
	public String getClosedCaptioning() {
		return closedCaptioning;
	}

	/**
	 * Sets {@code cc}
	 *
	 * @param closedCaptioning the {@code java.lang.String} field
	 */
	public void setClosedCaptioning(String closedCaptioning) {
		this.closedCaptioning = closedCaptioning;
	}

	/**
	 * Retrieves {@code {@link #timecode }}
	 *
	 * @return value of {@link #timecode}
	 */
	public String getTimecode() {
		return timecode;
	}

	/**
	 * Sets {@code tc}
	 *
	 * @param timecode the {@code java.lang.String} field
	 */
	public void setTimecode(String timecode) {
		this.timecode = timecode;
	}

	/**
	 * Retrieves {@code {@link #activeFormatDescription }}
	 *
	 * @return value of {@link #activeFormatDescription}
	 */
	public String getActiveFormatDescription() {
		return activeFormatDescription;
	}

	/**
	 * Sets {@code afd}
	 *
	 * @param activeFormatDescription the {@code java.lang.String} field
	 */
	public void setActiveFormatDescription(String activeFormatDescription) {
		this.activeFormatDescription = activeFormatDescription;
	}
}