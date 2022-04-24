/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Device info
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 22/4/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceInfoWrapper {
	@JsonAlias("haiversion")
	private DeviceInfo deviceInfo;

	/**
	 * Retrieves {@code {@link #deviceInfo}}
	 *
	 * @return value of {@link #deviceInfo}
	 */
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * Sets {@code deviceInfo}
	 *
	 * @param deviceInfo the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfo} field
	 */
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
