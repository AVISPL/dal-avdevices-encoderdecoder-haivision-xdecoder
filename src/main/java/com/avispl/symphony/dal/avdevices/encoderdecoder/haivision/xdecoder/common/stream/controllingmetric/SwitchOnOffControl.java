/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of switch on/off modes
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum SwitchOnOffControl {

	ON("Enabled", "On", true, 1),
	OFF("Disabled", "Off", false, 0);

	private final String uiName;
	private final String apiName;
	private final boolean isEnable;
	private final int code;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of switch on/off mode
	 * @param apiName api name of switch on/off mode
	 * @param isEnable status of switch on/off mode
	 * @param code code of switch on/off mode
	 */
	SwitchOnOffControl(String uiName, String apiName, boolean isEnable, int code) {
		this.uiName = uiName;
		this.apiName = apiName;
		this.isEnable = isEnable;
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
	 * Retrieves {@code {@link #isEnable }}
	 *
	 * @return value of {@link #isEnable}
	 */
	public boolean isEnable() {
		return isEnable;
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
	 * @return SwitchOnOffControl is the switch on/off mode that want to get
	 */
	public static SwitchOnOffControl getByApiName(String name) {
		Optional<SwitchOnOffControl> switchOnOffControl = Arrays.stream(SwitchOnOffControl.values()).filter(control -> control.getApiName().equals(name)).findFirst();
		return switchOnOffControl.orElse(SwitchOnOffControl.OFF);
	}

	/**
	 * This method is used to get switch on/off code
	 *
	 * @param code is the code of switch on/off mode that want to get
	 * @return SwitchOnOffControl is the switch on/off mode that want to get
	 */
	public static SwitchOnOffControl getByCode(Integer code) {
		Optional<SwitchOnOffControl> switchOnOffControl = Arrays.stream(SwitchOnOffControl.values()).filter(control -> control.getCode().equals(code)).findFirst();
		return switchOnOffControl.orElse(SwitchOnOffControl.OFF);
	}
}

