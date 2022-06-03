package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.talkback;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Talkback statistic
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/24/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalkBackStats {

	@JsonAlias("State")
	private String state;

	@JsonAlias("DestinationAddress")
	private String destinationAddress;

	/**
	 * Retrieves {@code {@link #state }}
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
	 * Retrieves {@code {@link #destinationAddress }}
	 *
	 * @return value of {@link #destinationAddress}
	 */
	public String getDestinationAddress() {
		return destinationAddress;
	}

	/**
	 * Sets {@code destinationAddress}
	 *
	 * @param destinationAddress the {@code java.lang.String} field
	 */
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
}
