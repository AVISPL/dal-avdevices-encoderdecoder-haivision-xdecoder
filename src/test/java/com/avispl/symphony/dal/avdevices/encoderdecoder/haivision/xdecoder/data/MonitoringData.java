/*
 *  Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.data;

/**
 * This class used to define all values of device info monitoring metrics
 *
 * @author Harry
 * @since 1.0
 */
public enum MonitoringData {
	SERIAL_NUMBER(" HAI-031428030014"),
	BOOT_VERSION(" \"U-Boot 2010.06 (Mar 19 2014 - 10:37:19)-MakitoXD 0.9.14\""),
	CARD_TYPE(" \"Makito2 Decoder\""),
	CPLD_REVISION(" 5 (Official, Internal flash)"),
	FIRMWARE_DATE(" \"Feb 28 2022\""),
	FIRMWARE_OPTIONS(" \"KLV, SRT\""),
	FIRMWARE_VERSION(" 2.5.1-9"),
	HARDWARE_COMPATIBILITY(" -001G"),
	HARDWARE_VERSION(" --"),
	PART_NUMBER(" B-292D-HD2");

	private final String data;

	/**
	 * Parameterized constructor
	 *
	 * @param data response data of monitoring
	 */
	MonitoringData(String data) {
		this.data = data;
	}

	/**
	 * retrieve {@code {@link #data }}
	 *
	 * @return value of {@link #data}
	 */
	public String getData() {
		return this.data;
	}
}
