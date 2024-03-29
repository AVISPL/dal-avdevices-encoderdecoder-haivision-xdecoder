/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

/**
 * Set of device info metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum DeviceInfoMetric {

	// Static metric
	SERIAL_NUMBER("SerialNumber"),
	BOOT_VERSION("BootVersion"),
	CARD_TYPE("CardType"),
	CPLD_VERSION("CpldVersion"),
	FIRMWARE_DATE("FirmwareDate"),
	FIRMWARE_OPTIONS("FirmwareOptions"),
	FIRMWARE_VERSION("FirmwareVersion"),
	HARDWARE_COMPATIBILITY("HardwareCompatibility"),
	HARDWARE_VERSION("HardwareVersion"),
	PART_NUMBER("PartNumber"),
	TEMPERATURE("Temperature");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of Decoder monitoring metric
	 */
	DeviceInfoMetric(String name) {
		this.name = name;
	}

	/**
	 * retrieve {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return this.name;
	}
}

