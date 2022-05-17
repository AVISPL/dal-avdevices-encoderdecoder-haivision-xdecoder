/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of controlling metric group keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/19/2022
 * @since 1.0.0
 */
public enum ControllingMetricGroup {

	DECODER_SDI("DecoderSDI", "Decoder"),
	CREATE_STREAM("CreateStream", "Create"),
	STREAM("Stream", "Stream");

	private final String uiName;
	private final String apiName;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of decoder controlling metric group
	 * @param apiName api name of decoder controlling metric group
	 */
	ControllingMetricGroup(String uiName, String apiName) {
		this.uiName = uiName;
		this.apiName = apiName;
	}

	/**
	 * Retrieves {@code {@link #uiName }}
	 *
	 * @return value of {@link #uiName}
	 */
	public String getUiName() {
		return this.uiName;
	}

	/**
	 * Retrieves {@code {@link #apiName}}
	 *
	 * @return value of {@link #apiName}
	 */
	public String getApiName() {
		return apiName;
	}

	/**
	 * This method is used to get controlling metric group by name
	 *
	 * @param apiName is the api name of controlling metric that want to get
	 * @return ControllingMetric is the controlling metric group that want to get
	 */
	public static ControllingMetricGroup getByApiName(String apiName) {
		Optional<ControllingMetricGroup> controllingMetricGroup = Arrays.stream(ControllingMetricGroup.values()).filter(c -> apiName.contains(c.getApiName())).findFirst();
		if (controllingMetricGroup.isPresent()) {
			return controllingMetricGroup.get();
		}
		throw new IllegalArgumentException("Could not find the controlling metric group with name: " + apiName);
	}

}
