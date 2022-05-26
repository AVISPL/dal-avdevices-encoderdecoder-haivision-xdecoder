package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.hdmiconfig;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * HDMI config info wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/16/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HDMIConfigWrapper {

	@JsonAlias("DecoderHdmiInformation")
	private HDMIConfig hdmiConfig;

	/**
	 * Retrieves {@code {@link #hdmiConfig }}
	 *
	 * @return value of {@link #hdmiConfig}
	 */
	public HDMIConfig getHdmiConfig() {
		return hdmiConfig;
	}

	/**
	 * Sets {@code audioConfig}
	 *
	 * @param hdmiConfig the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.x4decoder.dto.audio.AudioConfig} field
	 */
	public void setHdmiConfig(HDMIConfig hdmiConfig) {
		this.hdmiConfig = hdmiConfig;
	}
}
