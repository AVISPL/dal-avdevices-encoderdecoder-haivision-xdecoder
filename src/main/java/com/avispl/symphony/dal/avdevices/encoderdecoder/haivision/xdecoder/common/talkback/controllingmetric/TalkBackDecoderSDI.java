/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.talkback.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of decoder SDI
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum TalkBackDecoderSDI {

	DECODER_SDI_1("SDI1", "1"),
	DECODER_SDI_2("SDI2", "2");

	private final String uiName;
	private final String apiConfigName;

	/**
	 * Parameterized constructor
	 *
	 * @param apiStatsName api stats name of decoder SDI mode
	 * @param apiConfigName api config name of decoder SDI mode
	 */
	TalkBackDecoderSDI(String apiStatsName, String apiConfigName) {
		this.uiName = apiStatsName;
		this.apiConfigName = apiConfigName;
	}

	/**
	 * Retrieves {@code {@link #uiName }}
	 *
	 * @return value of {@link #uiName}
	 */
	public String getUiName() {
		return uiName;
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
	 * This method is used to get decoder SDI mode by api stats name
	 *
	 * @param name is the name of decoder SDI mode that want to get
	 * @return TalkBackDecoderSDI is the decoder SDI mode that want to get
	 */
	public static TalkBackDecoderSDI getByUiName(String name) {
		Optional<TalkBackDecoderSDI> talkBackDecoderSDI = Arrays.stream(TalkBackDecoderSDI.values()).filter(com -> com.getUiName().equals(name)).findFirst();
		return talkBackDecoderSDI.orElse(TalkBackDecoderSDI.DECODER_SDI_1);
	}

	/**
	 * This method is used to get decoder SDI mode by api config name
	 *
	 * @param name is the name of decoder SDI mode that want to get
	 * @return TalkBackDecoderSDI is the decoder SDI mode that want to get
	 */
	public static TalkBackDecoderSDI getByApiConfigName(String name) {
		Optional<TalkBackDecoderSDI> talkBackDecoderSDI = Arrays.stream(TalkBackDecoderSDI.values()).filter(com -> com.getApiConfigName().equals(name)).findFirst();
		return talkBackDecoderSDI.orElse(TalkBackDecoderSDI.DECODER_SDI_1);
	}

}

