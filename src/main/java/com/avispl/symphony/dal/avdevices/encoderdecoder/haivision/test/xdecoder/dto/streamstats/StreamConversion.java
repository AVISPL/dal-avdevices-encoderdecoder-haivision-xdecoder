/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.test.xdecoder.dto.streamstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Set of stream conversion properties
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/7/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamConversion {

	@JsonAlias("Address")
	private String address;

	@JsonAlias("TOS")
	private String tos;

	@JsonAlias("UDPPort")
	private String udpPort;

	@JsonAlias("StreamFlipping")
	private String streamFlipping;

	@JsonAlias("TTL")
	private String ttl;

	/**
	 * Retrieves {@code {@link #address}}
	 *
	 * @return value of {@link #address}
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets {@code address}
	 *
	 * @param address the {@code java.lang.String} field
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Retrieves {@code {@link #tos}}
	 *
	 * @return value of {@link #tos}
	 */
	public String getTos() {
		return tos;
	}

	/**
	 * Sets {@code tos}
	 *
	 * @param tos the {@code java.lang.String} field
	 */
	public void setTos(String tos) {
		this.tos = tos;
	}

	/**
	 * Retrieves {@code {@link #udpPort}}
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
	 * Retrieves {@code {@link #streamFlipping}}
	 *
	 * @return value of {@link #streamFlipping}
	 */
	public String getStreamFlipping() {
		return streamFlipping;
	}

	/**
	 * Sets {@code streamFlipping}
	 *
	 * @param streamFlipping the {@code java.lang.String} field
	 */
	public void setStreamFlipping(String streamFlipping) {
		this.streamFlipping = streamFlipping;
	}

	/**
	 * Retrieves {@code {@link #ttl}}
	 *
	 * @return value of {@link #ttl}
	 */
	public String getTtl() {
		return ttl;
	}

	/**
	 * Sets {@code ttl}
	 *
	 * @param ttl the {@code java.lang.String} field
	 */
	public void setTtl(String ttl) {
		this.ttl = ttl;
	}
}
