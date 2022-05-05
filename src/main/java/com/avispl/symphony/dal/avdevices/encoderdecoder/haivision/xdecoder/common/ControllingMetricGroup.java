/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

/**
 * Set of controlling metric group keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
 * @since 1.0.0
 */
public enum ControllingMetricGroup {

	DECODER("DecoderSDI"),
	CREATE_STREAM("CreateStream");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder controlling metric group
	 */
	ControllingMetricGroup(String name) {
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

	/**
	 * This method is used to get controlling metric group by name
	 *
	 * @param name is the name of controlling metric that want to get
	 * @return ControllingMetric is the controlling metric group that want to get
	 */
	public static ControllingMetricGroup getByName(String name) {
		if (name.contains("DecoderSDI")) {
			return ControllingMetricGroup.DECODER;
		}
		if (name.contains("Create")) {
			return ControllingMetricGroup.CREATE_STREAM;
		} else {
			return null;
		}
	}

}