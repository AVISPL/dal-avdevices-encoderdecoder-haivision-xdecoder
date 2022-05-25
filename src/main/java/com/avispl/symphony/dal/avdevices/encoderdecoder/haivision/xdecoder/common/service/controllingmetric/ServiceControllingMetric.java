/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.service.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of service controlling metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/3/2022
 * @since 1.0.0
 */
public enum ServiceControllingMetric {

	EMS("ems"),
	HTTP("http"),
	SAP("sap"),
	SNMP("snmp"),
	SSH("ssh"),
	TALK_BACK("talkback"),
	TELNET("telnet");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of service monitoring metric
	 */
	ServiceControllingMetric(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method is used to get service controlling metric by name
	 *
	 * @param name is the name of service controlling metric that want to get
	 * @return ServiceControllingMetric is the service controlling metric that want to get
	 */
	public static ServiceControllingMetric getByName(String name) {
		Optional<ServiceControllingMetric> decoderControllingMetric = Arrays.stream(ServiceControllingMetric.values()).filter(controllingMetric -> controllingMetric.getName().equals(name)).findFirst();
		if(decoderControllingMetric.isPresent()) {
			return decoderControllingMetric.get();
		}
		throw new IllegalArgumentException("Could not find the controlling metric group with name: " + name);
	}
}

