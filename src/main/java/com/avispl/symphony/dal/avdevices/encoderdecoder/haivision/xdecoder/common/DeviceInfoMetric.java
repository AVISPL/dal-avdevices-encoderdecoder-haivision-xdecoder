/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

/**
 * Set of device info metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
 * @since 1.0.0
 */
public enum DeviceInfoMetric {

	// Static metric
	BOOT_VERSION("BootVersion"),
	CARD_TYPE("CardType"),
	CPLD_REVISION("CpldRevision"),
	FIRMWARE_DATE("FirmwareDate"),
	FIRMWARE_OPTIONS("FirmwareOptions"),
	FIRMWARE_VERSION("FirmwareVersion"),
	FIRMWARE_TIME("FirmwareTime"),
	HARDWARE_COMPATIBILITY("HardwareCompatibility"),
	HARDWARE_VERSION("HardwareVersion"),
	PART_NUMBER("PartNumber"),

	// TODO: Can be Historical metric in next version
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

