package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.talkback;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Set of talkback config info
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/24/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalkBackConfig {

	private String activeStreamID;
	private String state;
	private String destinationAddress;

	@JsonAlias("UDPPort")
	private String udpPort;

	@JsonAlias("DecoderID")
	private String decoderID;

	/**
	 * Non-parameterized constructor
	 */
	public TalkBackConfig() {
	}

	/**
	 * This constructor is used for deep clone object
	 *
	 * @param talkBackConfig hdmi config info
	 */
	public TalkBackConfig(TalkBackConfig talkBackConfig) {
		udpPort = talkBackConfig.getUdpPort();
		decoderID = talkBackConfig.getDecoderID();
		state = talkBackConfig.getState();
		udpPort = talkBackConfig.getUdpPort();
	}

	/**
	 * Retrieves {@code {@link #udpPort }}
	 *
	 * @return value of {@link #udpPort}
	 */
	public String getUdpPort() {
		return udpPort;
	}

	/**
	 * Sets {@code udpPort}
	 *
	 * @param udpPort the {@code java.lang.String} field
	 */
	public void setUdpPort(String udpPort) {
		this.udpPort = udpPort;
	}


	/**
	 * Retrieves {@code {@link #decoderID }}
	 *
	 * @return value of {@link #decoderID}
	 */
	public String getDecoderID() {
		return decoderID;
	}

	/**
	 * Sets {@code decoderID}
	 *
	 * @param decoderID the {@code java.lang.String} field
	 */
	public void setDecoderID(String decoderID) {
		this.decoderID = decoderID;
	}

	/**
	 * Retrieves {@code {@link #activeStreamID }}
	 *
	 * @return value of {@link #activeStreamID}
	 */
	public String getActiveStreamID() {
		return activeStreamID;
	}

	/**
	 * Sets {@code activeStreamName}
	 *
	 * @param activeStreamName the {@code java.lang.String} field
	 */
	public void setActiveStreamID(String activeStreamName) {
		this.activeStreamID = activeStreamName;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TalkBackConfig that = (TalkBackConfig) o;
		return Objects.equals(activeStreamID, that.activeStreamID) && Objects.equals(udpPort, that.udpPort) && Objects.equals(decoderID, that.decoderID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(activeStreamID, udpPort, decoderID);
	}
}
