/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

/**
 * Set of monitoring metric group keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public enum MonitoringMetricGroup {

	DECODER_STATISTICS("Decoder Statistics SDI "),
	STREAM_STATISTICS("Stream Statistics "),
	DEVICE_INFO("DeviceInfo"),
	TEMPERATURE("Temperature"),
	STILL_IMAGE("Still Image");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of Decoder monitoring metric
	 */
	MonitoringMetricGroup(String name) {
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
