package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Service config info wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/20/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceConfigWrapper {

	@JsonAlias("serviceallstatus")
	private ServiceConfig serviceConfig;

	/**
	 * Retrieves {@code {@link #serviceConfig }}
	 *
	 * @return value of {@link #serviceConfig}
	 */
	public ServiceConfig getServiceConfig() {
		return serviceConfig;
	}

	/**
	 * Sets {@code audioConfig}
	 *
	 * @param serviceConfig the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.x4decoder.dto.audio.AudioConfig} field
	 */
	public void setServiceConfig(ServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}
}
