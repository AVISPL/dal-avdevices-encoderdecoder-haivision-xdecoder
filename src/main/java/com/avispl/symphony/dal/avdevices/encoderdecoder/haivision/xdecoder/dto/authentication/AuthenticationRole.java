/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Authentication role information
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 22/4/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRole {

	@JsonAlias("Role")
	private String role;

	/**
	 * Retrieves {@code {@link #role}}
	 *
	 * @return value of {@link #role}
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets {@code role}
	 *
	 * @param role the {@code java.lang.String} field
	 */
	public void setRole(String role) {
		this.role = role;
	}
}
