/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Encapsulation;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;

/**
 * Set of stream configuration properties
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/19/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamConfig {

	private String id;
	private String name;
	private String sourceAddress;
	private StreamConversion streamConversion;

	@JsonAlias("Encapsulation")
	private String encapsulation;

	@JsonAlias("Address")
	private String address;

	@JsonAlias("UDPPort")
	private String port;

	@JsonAlias("SourcePort")
	private String sourcePort;

	@JsonAlias("DestinationPort")
	private String destinationPort;

	@JsonAlias("Latency")
	private String latency;

	@JsonAlias("Mode")
	private String srtMode;

	@JsonAlias("AESEncryption")
	private String srtSettings;

	@JsonAlias("RejectUnencrypted")
	private String rejectUnencrypted;

	@JsonAlias("FEC")
	private String fec;

	public StreamConfig() {
	}

	/**
	 * This constructor is used for deep clone object
	 *
	 * @param streamInfo Stream config info
	 */
	public StreamConfig(StreamConfig streamInfo) {
		this.name = streamInfo.getName();
		this.id = streamInfo.getId();
		this.encapsulation = streamInfo.getEncapsulation();
		this.address = streamInfo.getAddress();
		this.port = streamInfo.getPort();
		this.latency = streamInfo.getLatency();
		this.srtMode = streamInfo.getSrtMode();
		this.sourcePort = streamInfo.getSourcePort();
	}

	/**
	 * Retrieves {@code {@link #id}}
	 *
	 * @return value of {@link #id}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets {@code id}
	 *
	 * @param id the {@code java.lang.String} field
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets {@code name}
	 *
	 * @param name the {@code java.lang.String} field
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #sourceAddress}}
	 *
	 * @return value of {@link #sourceAddress}
	 */
	public String getSourceAddress() {
		return sourceAddress;
	}

	/**
	 * Sets {@code sourceAddress}
	 *
	 * @param sourceAddress the {@code java.lang.String} field
	 */
	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	/**
	 * Retrieves {@code {@link #streamConversion}}
	 *
	 * @return value of {@link #streamConversion}
	 */
	public StreamConversion getStreamConversion() {
		return streamConversion;
	}

	/**
	 * Sets {@code streamConversion}
	 *
	 * @param streamConversion the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConversion} field
	 */
	public void setStreamConversion(StreamConversion streamConversion) {
		this.streamConversion = streamConversion;
	}

	/**
	 * Retrieves {@code {@link #encapsulation}}
	 *
	 * @return value of {@link #encapsulation}
	 */
	public String getEncapsulation() {
		return encapsulation;
	}

	/**
	 * Sets {@code encapsulation}
	 *
	 * @param encapsulation the {@code java.lang.String} field
	 */
	public void setEncapsulation(String encapsulation) {
		this.encapsulation = encapsulation;
	}

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
	 * Retrieves {@code {@link #port}}
	 *
	 * @return value of {@link #port}
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Sets {@code port}
	 *
	 * @param port the {@code java.lang.String} field
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * Retrieves {@code {@link #sourcePort}}
	 *
	 * @return value of {@link #sourcePort}
	 */
	public String getSourcePort() {
		return sourcePort;
	}

	/**
	 * Sets {@code sourcePort}
	 *
	 * @param sourcePort the {@code java.lang.String} field
	 */
	public void setSourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
	}

	/**
	 * Retrieves {@code {@link #destinationPort}}
	 *
	 * @return value of {@link #destinationPort}
	 */
	public String getDestinationPort() {
		return destinationPort;
	}

	/**
	 * Sets {@code destinationPort}
	 *
	 * @param destinationPort the {@code java.lang.String} field
	 */
	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	/**
	 * Retrieves {@code {@link #latency}}
	 *
	 * @return value of {@link #latency}
	 */
	public String getLatency() {
		return latency;
	}

	/**
	 * Sets {@code latency}
	 *
	 * @param latency the {@code java.lang.String} field
	 */
	public void setLatency(String latency) {
		this.latency = latency;
	}

	/**
	 * Retrieves {@code {@link #srtMode}}
	 *
	 * @return value of {@link #srtMode}
	 */
	public String getSrtMode() {
		return srtMode;
	}

	/**
	 * Sets {@code srtMode}
	 *
	 * @param srtMode the {@code java.lang.String} field
	 */
	public void setSrtMode(String srtMode) {
		this.srtMode = srtMode;
	}

	/**
	 * Retrieves {@code {@link #srtSettings}}
	 *
	 * @return value of {@link #srtSettings}
	 */
	public String getSrtSettings() {
		return srtSettings;
	}

	/**
	 * Sets {@code srtSettings}
	 *
	 * @param srtSettings the {@code java.lang.String} field
	 */
	public void setSrtSettings(String srtSettings) {
		this.srtSettings = srtSettings;
	}

	/**
	 * Retrieves {@code {@link #rejectUnencrypted}}
	 *
	 * @return value of {@link #rejectUnencrypted}
	 */
	public String getRejectUnencrypted() {
		return rejectUnencrypted;
	}

	/**
	 * Sets {@code rejectUnencrypted}
	 *
	 * @param rejectUnencrypted the {@code java.lang.String} field
	 */
	public void setRejectUnencrypted(String rejectUnencrypted) {
		this.rejectUnencrypted = rejectUnencrypted;
	}

	/**
	 * Retrieves {@code {@link #fec}}
	 *
	 * @return value of {@link #fec}
	 */
	public String getFec() {
		return fec;
	}

	/**
	 * Sets {@code fec}
	 *
	 * @param fec the {@code java.lang.String} field
	 */
	public void setFec(String fec) {
		this.fec = fec;
	}

	/**
	 * Retrieves default stream name when stream name is empty
	 *
	 * @return String default stream name
	 */
	public String getDefaultStreamName() {
		Encapsulation encapsulationEnum = Encapsulation.getByName(getEncapsulation());
		String encapsulationShortName = encapsulationEnum.getShortName();
		if (getEncapsulation().equals(Encapsulation.RTSP.getName())) {
			return getAddress();
		} else if (getAddress().equals(DecoderConstant.ADDRESS_ANY.toUpperCase()) || getAddress().equals(DecoderConstant.EMPTY)) {
			return encapsulationShortName + DecoderConstant.COLON + DecoderConstant.SLASH + DecoderConstant.SLASH + DecoderConstant.AT_SIGN + DecoderConstant.LEFT_PARENTHESES + DecoderConstant.ADDRESS_ANY
					+ DecoderConstant.RIGHT_PARENTHESES +
					DecoderConstant.COLON + getPort();
		} else {
			return encapsulationShortName + DecoderConstant.COLON + DecoderConstant.SLASH + DecoderConstant.SLASH + DecoderConstant.AT_SIGN + NormalizeData.convertToNumberValue(getAddress()) +
					DecoderConstant.COLON + getPort();
		}
	}
}
