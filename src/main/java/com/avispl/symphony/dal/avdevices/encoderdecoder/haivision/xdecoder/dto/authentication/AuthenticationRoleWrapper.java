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
	private AuthenticationRole authenticationRole;

	/**
	 * Retrieves {@code {@link #authenticationRole }}
	 *
	 * @return value of {@link #authenticationRole}
	 */
	public AuthenticationRole getAuthenticationRole() {
		return authenticationRole;
	}

	/**
	 * Sets {@code authenticationWrapper}
	 *
	 * @param authenticationRole the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication.AuthenticationRole} field
	 */
	public void setAuthenticationRole(AuthenticationRole authenticationRole) {
		this.authenticationRole = authenticationRole;
	}
}
