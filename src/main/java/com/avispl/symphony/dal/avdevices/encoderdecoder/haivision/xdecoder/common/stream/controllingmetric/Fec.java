/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of fec option
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum Fec {

	DISABLE("(None)", "", "no"),
	MPEG_PRO_FEC("MPEG PRO FEC", "Pro-MPEG", "yes" ),
	VF("VF","Furnace", "yes" );

	private final String uiName;
	private final String apiConfigName;
	private final String apiStatsName;

	/**
	 * Parameterized constructor
	 *
	 * @param uiName ui name of fec
	 * @param apiConfigName api config name of fec
	 * @param apiStatsName api stats name of fec
	 */
	Fec(String uiName, String apiConfigName, String apiStatsName) {
		this.uiName = uiName;
		this.apiConfigName = apiConfigName;
		this.apiStatsName = apiStatsName;
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
	 * Retrieves {@code {@link #apiStatsName}}
	 *
	 * @return value of {@link #apiStatsName}
	 */
	public String getApiConfigName() {
		return apiConfigName;
	}

	/**
	 * Retrieves {@code {@link #apiConfigName }}
	 *
	 * @return value of {@link #apiConfigName}
	 */
	public String getApiStatsName() {
		return apiStatsName;
	}

	/**
	 * This method is used to get fec rtp mode by name
	 *
	 * @param apiStatsName is the api stats name of fec rtp mode that want to get
	 * @return Fec is the fec mode that want to get
	 */
	public static Fec getByAPIStatsName(String apiStatsName) {
		Optional<Fec> fecRTP = Arrays.stream(Fec.values()).filter(com -> com.getApiStatsName().equals(apiStatsName)).findFirst();
		return fecRTP.orElse(Fec.DISABLE);
	}

	/**
	 * This method is used to get fec rtp mode by name
	 *
	 * @param apiConfigName is the api config name of fec rtp mode that want to get
	 * @return FecRTP is the fec mode that want to get
	 */
	public static Fec getByAPIConfigName(String apiConfigName) {
		Optional<Fec> fecRTP = Arrays.stream(Fec.values()).filter(com -> com.getApiConfigName().equals(apiConfigName)).findFirst();
		return fecRTP.orElse(Fec.DISABLE);
	}

	/**
	 * This method is used to get fec rtp mode by name
	 *
	 * @param uiName is the Ui name of fec rtp mode that want to get
	 * @return FecRTP is the fec mode that want to get
	 */
	public static Fec getByUiName(String uiName) {
		Optional<Fec> fecRTP = Arrays.stream(Fec.values()).filter(com -> com.getUiName().equals(uiName)).findFirst();
		return fecRTP.orElse(Fec.DISABLE);
	}
}

