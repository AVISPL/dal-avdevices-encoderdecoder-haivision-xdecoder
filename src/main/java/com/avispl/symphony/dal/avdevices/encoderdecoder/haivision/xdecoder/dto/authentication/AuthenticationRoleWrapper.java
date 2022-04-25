/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Authentication role wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 22/4/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRoleWrapper {

	@JsonAlias("accountadminget")
	private AuthenticationRole authenticationWrapper;

	/**
	 * Retrieves {@code {@link #authenticationWrapper}}
	 *
	 * @return value of {@link #authenticationWrapper}
	 */
	public AuthenticationRole getAuthenticationWrapper() {
		return authenticationWrapper;
	}

	/**
	 * Sets {@code authenticationWrapper}
	 *
	 * @param authenticationWrapper the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication.AuthenticationRole} field
	 */
	public void setAuthenticationWrapper(AuthenticationRole authenticationWrapper) {
		this.authenticationWrapper = authenticationWrapper;
	}
}
