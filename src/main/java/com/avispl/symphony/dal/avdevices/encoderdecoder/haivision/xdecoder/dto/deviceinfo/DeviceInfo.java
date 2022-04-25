/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Device info
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceInfo {

	@JsonAlias("SerialNumber")
	private String serialNumber;

	@JsonAlias("BootVersion")
	private String bootVersion;

	@JsonAlias("CardType")
	private String cardType;

	@JsonAlias("CPLDVersion")
	private String cpldVersion;

	@JsonAlias("FirmwareDate")
	private String firmwareDate;

	@JsonAlias("FirmwareOptions")
	private String firmwareOptions;

	@JsonAlias("FirmwareVersion")
	private String firmwareVersion;

	@JsonAlias("FirmwareTime")
	private String firmwareTime;

	@JsonAlias("HardwareCompatibility")
	private String hardwareCompatibility;

	@JsonAlias("HardwareVersion")
	private String hardwareVersion;

	@JsonAlias("PartNumber")
	private String partNumber;

	@JsonAlias("TemperatureStatus")
	private TemperatureStatus temperatureStatus;

	/**
	 * Retrieves {@code {@link #serialNumber}}
	 *
	 * @return value of {@link #serialNumber}
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets {@code serialNumber}
	 *
	 * @param serialNumber the {@code java.lang.String} field
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * Retrieves {@code {@link #bootVersion}}
	 *
	 * @return value of {@link #bootVersion}
	 */
	public String getBootVersion() {
		return bootVersion;
	}

	/**
	 * Sets {@code bootVersion}
	 *
	 * @param bootVersion the {@code java.lang.String} field
	 */
	public void setBootVersion(String bootVersion) {
		this.bootVersion = bootVersion;
	}

	/**
	 * Retrieves {@code {@link #cardType}}
	 *
	 * @return value of {@link #cardType}
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * Sets {@code cardType}
	 *
	 * @param cardType the {@code java.lang.String} field
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * Retrieves {@code {@link #cpldVersion }}
	 *
	 * @return value of {@link #cpldVersion}
	 */
	public String getCpldVersion() {
		return cpldVersion;
	}

	/**
	 * Sets {@code cpldRevision}
	 *
	 * @param cpldVersion the {@code java.lang.String} field
	 */
	public void setCpldVersion(String cpldVersion) {
		this.cpldVersion = cpldVersion;
	}

	/**
	 * Retrieves {@code {@link #firmwareDate}}
	 *
	 * @return value of {@link #firmwareDate}
	 */
	public String getFirmwareDate() {
		return firmwareDate;
	}

	/**
	 * Sets {@code firmwareDate}
	 *
	 * @param firmwareDate the {@code java.lang.String} field
	 */
	public void setFirmwareDate(String firmwareDate) {
		this.firmwareDate = firmwareDate;
	}

	/**
	 * Retrieves {@code {@link #firmwareOptions}}
	 *
	 * @return value of {@link #firmwareOptions}
	 */
	public String getFirmwareOptions() {
		return firmwareOptions;
	}

	/**
	 * Sets {@code firmwareOptions}
	 *
	 * @param firmwareOptions the {@code java.lang.String} field
	 */
	public void setFirmwareOptions(String firmwareOptions) {
		this.firmwareOptions = firmwareOptions;
	}

	/**
	 * Retrieves {@code {@link #firmwareVersion}}
	 *
	 * @return value of {@link #firmwareVersion}
	 */
	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	/**
	 * Sets {@code firmwareVersion}
	 *
	 * @param firmwareVersion the {@code java.lang.String} field
	 */
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	/**
	 * Retrieves {@code {@link #firmwareTime}}
	 *
	 * @return value of {@link #firmwareTime}
	 */
	public String getFirmwareTime() {
		return firmwareTime;
	}

	/**
	 * Sets {@code firmwareTime}
	 *
	 * @param firmwareTime the {@code java.lang.String} field
	 */
	public void setFirmwareTime(String firmwareTime) {
		this.firmwareTime = firmwareTime;
	}

	/**
	 * Retrieves {@code {@link #hardwareCompatibility}}
	 *
	 * @return value of {@link #hardwareCompatibility}
	 */
	public String getHardwareCompatibility() {
		return hardwareCompatibility;
	}

	/**
	 * Sets {@code hardwareCompatibility}
	 *
	 * @param hardwareCompatibility the {@code java.lang.String} field
	 */
	public void setHardwareCompatibility(String hardwareCompatibility) {
		this.hardwareCompatibility = hardwareCompatibility;
	}

	/**
	 * Retrieves {@code {@link #hardwareVersion}}
	 *
	 * @return value of {@link #hardwareVersion}
	 */
	public String getHardwareVersion() {
		return hardwareVersion;
	}

	/**
	 * Sets {@code hardwareVersion}
	 *
	 * @param hardwareVersion the {@code java.lang.String} field
	 */
	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	/**
	 * Retrieves {@code {@link #partNumber}}
	 *
	 * @return value of {@link #partNumber}
	 */
	public String getPartNumber() {
		return partNumber;
	}

	/**
	 * Sets {@code partNumber}
	 *
	 * @param partNumber the {@code java.lang.String} field
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * Retrieves {@code {@link #temperatureStatus }}
	 *
	 * @return value of {@link #temperatureStatus}
	 */
	public TemperatureStatus getTemperatureStatus() {
		return temperatureStatus;
	}

	/**
	 * Sets {@code temperature}
	 *
	 * @param temperatureStatus the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.TemperatureStatus} field
	 */
	public void setTemperatureStatus(TemperatureStatus temperatureStatus) {
		this.temperatureStatus = temperatureStatus;
	}
}
