/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderStatsMonitoringMetric;

/**
 * Decoder statistics
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecoderStats {

	@JsonAlias("UpTime")
	private String uptime;

	@JsonAlias("NumberofRestarts")
	private String restarts;

	@JsonAlias("HdmiOutput")
	private String  hdmiOutput ;


	/**
	 * Retrieves {@code {@link #uptime}}
	 *
	 * @return value of {@link #uptime}
	 */
	public String getUptime() {
		return uptime;
	}

	/**
	 * Sets {@code uptime}
	 *
	 * @param uptime the {@code java.lang.String} field
	 */
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	/**
	 * Retrieves {@code {@link #restarts}}
	 *
	 * @return value of {@link #restarts}
	 */
	public String getRestarts() {
		return restarts;
	}

	/**
	 * Sets {@code restarts}
	 *
	 * @param restarts the {@code java.lang.String} field
	 */
	public void setRestarts(String restarts) {
		this.restarts = restarts;
	}

	/**
	 * Retrieves {@code {@link #hdmiOutput}}
	 *
	 * @return value of {@link #hdmiOutput}
	 */
	public String getHdmiOutput() {
		return hdmiOutput;
	}

	/**
	 * Sets {@code hdmiOutput}
	 *
	 * @param hdmiOutput the {@code java.lang.String} field
	 */
	public void setHdmiOutput(String hdmiOutput) {
		this.hdmiOutput = hdmiOutput;
	}

	/**
	 * @return String value of decoder monitoring properties by metric
	 */
	public String getValueByDecoderMonitoringMetric(DecoderStatsMonitoringMetric decoderStatsMonitoringMetric) {
		switch (decoderStatsMonitoringMetric) {
			case UPTIME:
				return getUptime();
			case RESTARTS:
				return getRestarts();
			default:
				return DecoderConstant.EMPTY;
		}
	}
}
