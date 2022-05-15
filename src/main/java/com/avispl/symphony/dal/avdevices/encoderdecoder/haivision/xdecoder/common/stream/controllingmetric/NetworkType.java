/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of network type option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum NetworkType {

	UNI_CAST("Unicast"),
	MULTI_CAST("Multicast");

	private final String uiName;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of decoder monitoring metric
	 */
	NetworkType(String name) {
		this.uiName = name;
	}

	/**
	 * retrieve {@code {@link #uiName }}
	 *
	 * @return value of {@link #uiName}
	 */
	public String getUiName() {
		return this.uiName;
	}

	/**
	 * This method is used to get network type by name
	 *
	 * @param name is the name of network type that want to get
	 * @return Type is the network type that want to get
	 */
	public static NetworkType getByUiName(String name) {
		Optional<NetworkType> type = Arrays.stream(NetworkType.values()).filter(com -> com.getUiName().equals(name)).findFirst();
		return type.orElse(NetworkType.UNI_CAST);
	}
}

