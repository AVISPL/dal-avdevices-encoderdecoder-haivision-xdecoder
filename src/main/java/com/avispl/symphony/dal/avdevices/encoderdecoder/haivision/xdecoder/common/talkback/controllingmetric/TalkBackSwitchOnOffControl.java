/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.talkback.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of service switch on/off mode option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum TalkBackSwitchOnOffControl {

	ON("TRANSMITTING", "start", 1),
	OFF("STOPPED", "stop", 0),
	DISABLE("DISABLED", "DISABLED", -1);

	private final String apiStatsName;
	private final String apiConfigName;
	private final int code;

	/**
	 * Parameterized constructor
	 *
	 * @param apiStatsName api stats name of switch on/off mode
	 * @param apiConfigName api config name of switch on/off mode
	 * @param code code of switch on/off mode
	 */
	TalkBackSwitchOnOffControl(String apiStatsName, String apiConfigName, int code) {
		this.apiStatsName = apiStatsName;
		this.apiConfigName = apiConfigName;
		this.code = code;
	}

	/**
	 * Retrieves {@code {@link #apiStatsName }}
	 *
	 * @return value of {@link #apiStatsName}
	 */
	public String getApiStatsName() {
		return apiStatsName;
	}

	/**
	 * Retrieves {@code {@link #apiConfigName }}
	 *
	 * @return value of {@link #apiConfigName}
	 */
	public String getApiConfigName() {
		return this.apiConfigName;
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
	 * This method is used to get switch on/off mode by api stats name
	 *
	 * @param name is the name of switch on/off mode that want to get
	 * @return TalkBackSwitchOnOffControl is the switch on/off mode that want to get
	 */
	public static TalkBackSwitchOnOffControl getByApiStatsName(String name) {
		Optional<TalkBackSwitchOnOffControl> talkBackSwitchOnOffControl = Arrays.stream(TalkBackSwitchOnOffControl.values()).filter(control -> control.getApiStatsName().equals(name)).findFirst();
		return talkBackSwitchOnOffControl.orElse(TalkBackSwitchOnOffControl.OFF);
	}

	/**
	 * This method is used to get switch on/off mode by api config name
	 *
	 * @param name is the name of switch on/off mode that want to get
	 * @return TalkBackSwitchOnOffControl is the switch on/off mode that want to get
	 */
	public static TalkBackSwitchOnOffControl getByApiName(String name) {
		Optional<TalkBackSwitchOnOffControl> talkBackSwitchOnOffControl = Arrays.stream(TalkBackSwitchOnOffControl.values()).filter(control -> control.getApiConfigName().equals(name)).findFirst();
		return talkBackSwitchOnOffControl.orElse(TalkBackSwitchOnOffControl.OFF);
	}

	/**
	 * This method is used to get switch on/off code
	 *
	 * @param code is the code of switch on/off mode that want to get
	 * @return TalkBackSwitchOnOffControl is the switch on/off mode that want to get
	 */
	public static TalkBackSwitchOnOffControl getByCode(Integer code) {
		Optional<TalkBackSwitchOnOffControl> talkBackSwitchOnOffControl = Arrays.stream(TalkBackSwitchOnOffControl.values()).filter(control -> control.getCode().equals(code)).findFirst();
		return talkBackSwitchOnOffControl.orElse(TalkBackSwitchOnOffControl.OFF);
	}
}

