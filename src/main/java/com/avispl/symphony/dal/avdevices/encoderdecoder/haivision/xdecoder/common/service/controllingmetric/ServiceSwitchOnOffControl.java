/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.service.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of service switch on/off modes
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum ServiceSwitchOnOffControl {

	ON("Enabled", "enabled", 1),
	OFF("Disabled", "disabled", 0);

	private final String uiName;
	private final String apiName;
	private final int code;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of switch on/off mode
	 * @param apiName api name of switch on/off mode
	 * @param code code of switch on/off mode
	 */
	ServiceSwitchOnOffControl(String uiName, String apiName, int code) {
		this.uiName = uiName;
		this.apiName = apiName;
		this.code = code;
	}

	/**
	 * Retrieves {@code {@link #uiName}}
	 *
	 * @return value of {@link #uiName}
	 */
	public String getUiName() {
		return uiName;
	}

	/**
	 * Retrieves {@code {@link #apiName }}
	 *
	 * @return value of {@link #apiName}
	 */
	public String getApiName() {
		return this.apiName;
	}

	/**
	 * Retrieves {@code {@link #code}}
	 *
	 * @return value of {@link #code}
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * This method is used to get switch on/off mode by name
	 *
	 * @param name is the name of switch on/off mode that want to get
	 * @return ServiceSwitchOnOffControl is the switch on/off mode that want to get
	 */
	public static ServiceSwitchOnOffControl getByApiName(String name) {
		Optional<ServiceSwitchOnOffControl> serviceSwitchOnOffControl = Arrays.stream(ServiceSwitchOnOffControl.values()).filter(com -> com.getApiName().equals(name)).findFirst();
		return serviceSwitchOnOffControl.orElse(ServiceSwitchOnOffControl.OFF);
	}
}

