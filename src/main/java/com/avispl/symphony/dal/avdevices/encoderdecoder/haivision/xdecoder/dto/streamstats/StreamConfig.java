/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Encapsulation;

/**
 * Set of stream configuration properties
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamConfig {

	@JsonAlias("name")
	private String name;

	@JsonAlias("id")
	private Integer id;

	@JsonAlias("decoderId")
	private String decoderId;

	@JsonAlias("Encapsulation")
	private String encapsulation;

	@JsonAlias("userData")
	private String userData;

	@JsonAlias("Address")
	private String address;

	@JsonAlias("UDPPort")
	private String port;

	@JsonAlias("sourceIp")
	private String sourceIp;

	@JsonAlias("latency")
	private String latency;

	@JsonAlias("srtMode")
	private Integer srtMode;

	@JsonAlias("sourcePort")
	private String sourcePort;

	@JsonAlias("strictMode")
	private Boolean strictMode;

	@JsonAlias("passphrase")
	private String passphrase;

	@JsonAlias("passphraseSet")
	private Boolean passphraseSet;

	@JsonAlias("srtToUdp")
	private Boolean srtToUdp;

	@JsonAlias("srtToUdp_address")
	private String srtToUdpAddress;

	@JsonAlias("srtToUdp_port")
	private String srtToUdpPort;

	@JsonAlias("srtToUdp_tos")
	private String srtToUdpTos;

	@JsonAlias("srtToUdp_ttl")
	private String srtToUdpTtl;

	@JsonAlias("fecRtp")
	private Integer fecRtp;

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
	 * Retrieves {@code {@link #id}}
	 *
	 * @return value of {@link #id}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets {@code id}
	 *
	 * @param id the {@code java.lang.Integer} field
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Retrieves {@code {@link #decoderId}}
	 *
	 * @return value of {@link #decoderId}
	 */
	public String getDecoderId() {
		return decoderId;
	}

	/**
	 * Sets {@code decoderId}
	 *
	 * @param decoderId the {@code java.lang.String} field
	 */
	public void setDecoderId(String decoderId) {
		this.decoderId = decoderId;
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
	 * @param encapsulation the {@code java.lang.Integer} field
	 */
	public void setEncapsulation(String encapsulation) {
		this.encapsulation = encapsulation;
	}

	/**
	 * Retrieves {@code {@link #userData}}
	 *
	 * @return value of {@link #userData}
	 */
	public String getUserData() {
		return userData;
	}

	/**
	 * Sets {@code userData}
	 *
	 * @param userData the {@code java.lang.String} field
	 */
	public void setUserData(String userData) {
		this.userData = userData;
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
	 * Retrieves {@code {@link #sourceIp}}
	 *
	 * @return value of {@link #sourceIp}
	 */
	public String getSourceIp() {
		return sourceIp;
	}

	/**
	 * Sets {@code sourceIp}
	 *
	 * @param sourceIp the {@code java.lang.String} field
	 */
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
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
	public Integer getSrtMode() {
		return srtMode;
	}

	/**
	 * Sets {@code srtMode}
	 *
	 * @param srtMode the {@code java.lang.Integer} field
	 */
	public void setSrtMode(Integer srtMode) {
		this.srtMode = srtMode;
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
	 * Retrieves {@code {@link #strictMode}}
	 *
	 * @return value of {@link #strictMode}
	 */
	public Boolean getStrictMode() {
		return strictMode;
	}

	/**
	 * Sets {@code strictMode}
	 *
	 * @param strictMode the {@code java.lang.Boolean} field
	 */
	public void setStrictMode(Boolean strictMode) {
		this.strictMode = strictMode;
	}

	/**
	 * Retrieves {@code {@link #passphrase}}
	 *
	 * @return value of {@link #passphrase}
	 */
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * Sets {@code passphrase}
	 *
	 * @param passphrase the {@code java.lang.String} field
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * Retrieves {@code {@link #passphraseSet}}
	 *
	 * @return value of {@link #passphraseSet}
	 */
	public Boolean getPassphraseSet() {
		return passphraseSet;
	}

	/**
	 * Sets {@code passphraseSet}
	 *
	 * @param passphraseSet the {@code java.lang.Boolean} field
	 */
	public void setPassphraseSet(Boolean passphraseSet) {
		this.passphraseSet = passphraseSet;
	}

	/**
	 * Retrieves {@code {@link #srtToUdp}}
	 *
	 * @return value of {@link #srtToUdp}
	 */
	public Boolean getSrtToUdp() {
		return srtToUdp;
	}

	/**
	 * Sets {@code srtToUdp}
	 *
	 * @param srtToUdp the {@code java.lang.Boolean} field
	 */
	public void setSrtToUdp(Boolean srtToUdp) {
		this.srtToUdp = srtToUdp;
	}

	/**
	 * Retrieves {@code {@link #srtToUdpAddress}}
	 *
	 * @return value of {@link #srtToUdpAddress}
	 */
	public String getSrtToUdpAddress() {
		return srtToUdpAddress;
	}

	/**
	 * Sets {@code srtToUdpAddress}
	 *
	 * @param srtToUdpAddress the {@code java.lang.String} field
	 */
	public void setSrtToUdpAddress(String srtToUdpAddress) {
		this.srtToUdpAddress = srtToUdpAddress;
	}

	/**
	 * Retrieves {@code {@link #srtToUdpPort}}
	 *
	 * @return value of {@link #srtToUdpPort}
	 */
	public String getSrtToUdpPort() {
		return srtToUdpPort;
	}

	/**
	 * Sets {@code srtToUdpPort}
	 *
	 * @param srtToUdpPort the {@code java.lang.String} field
	 */
	public void setSrtToUdpPort(String srtToUdpPort) {
		this.srtToUdpPort = srtToUdpPort;
	}

	/**
	 * Retrieves {@code {@link #srtToUdpTos}}
	 *
	 * @return value of {@link #srtToUdpTos}
	 */
	public String getSrtToUdpTos() {
		return srtToUdpTos;
	}

	/**
	 * Sets {@code srtToUdpTos}
	 *
	 * @param srtToUdpTos the {@code java.lang.String} field
	 */
	public void setSrtToUdpTos(String srtToUdpTos) {
		this.srtToUdpTos = srtToUdpTos;
	}

	/**
	 * Retrieves {@code {@link #srtToUdpTtl}}
	 *
	 * @return value of {@link #srtToUdpTtl}
	 */
	public String getSrtToUdpTtl() {
		return srtToUdpTtl;
	}

	/**
	 * Sets {@code srtToUdpTtl}
	 *
	 * @param srtToUdpTtl the {@code java.lang.String} field
	 */
	public void setSrtToUdpTtl(String srtToUdpTtl) {
		this.srtToUdpTtl = srtToUdpTtl;
	}

	/**
	 * Retrieves {@code {@link #fecRtp}}
	 *
	 * @return value of {@link #fecRtp}
	 */
	public Integer getFecRtp() {
		return fecRtp;
	}

	/**
	 * Sets {@code fecRtp}
	 *
	 * @param fecRtp the {@code java.lang.Integer} field
	 */
	public void setFecRtp(Integer fecRtp) {
		this.fecRtp = fecRtp;
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
