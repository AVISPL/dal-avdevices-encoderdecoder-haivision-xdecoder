/*
 *  Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

/**
 * Set of constants
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/18/2022
 * @since 1.0.0
 */
public class DecoderConstant {

	private DecoderConstant(){
	}

	public static final char HASH = '#';
	public static final char NEXT_LINE = '\n';
	public static final char SLASH = '/';
	public static final char AT_SIGN = '@';
	public static final String COMMA = ",";
	public static final String COLON = ":";
	public static final String DASH = "-";
	public static final String RIGHT_PARENTHESES = ")";
	public static final String LEFT_PARENTHESES = "(";
	public static final String SPACE =  " ";
	public static final String EMPTY = "";
	public static final String AT = "at";
	public static final String REGEX = "[^0-9?!\\\\.]";
	public static final String ADDRESS_ANY = "Any";
	public static final String NONE = "None";
	public static final String DEFAULT_STREAM_NAME = "(None)";
	public static final String DAY = " day(s) ";
	public static final String HOUR = " hour(s) ";
	public static final String MINUTE = " minute(s) ";
	public static final String SECOND = " second(s) ";
	public static final String GETTING_DEVICE_INFO_ERR = "Failed to get device info";
	public static final String GETTING_DEVICE_TEMPERATURE_ERR = "Failed to get device temperature";
	public static final String GETTING_DEVICE_STILL_IMAGE_ERR = "Failed to get device still image";
	public static final String GETTING_DECODER_STATS_ERR = "Failed to get decoder statistic";
	public static final String GETTING_STREAM_STATS_ERR = "Failed to get stream statistic";
	public static final String DECODER_CONTROL_ERR = "Failed to control decoder: ";
	public static final String PORT_NUMBER_ERROR = "Invalid port number";
	public static final String DEFAULT_SKIPPED_FRAMES_VALUE = "0";
	public static final String AUTOMATIC_RESOLUTION = "Automatic";
	public static final String NATIVE_RESOLUTION = "Native";
	public static final String COMPUTER_RESOLUTION = "Computer";
	public static final String TV_RESOLUTION = "TV";
	public static final String APPLY = "Apply";
	public static final String CANCEL = "Cancel";
	public static final String APPLYING = "Applying";
	public static final String CANCELLING = "Canceling";
	public static final String ON = "On";
	public static final String OFF = "Off";
	public static final String ENABLE = "Enable";
	public static final String DISABLE = "Disable";
	public static final String DEFAULT_STREAM_ID = "None";
	public static final String SUCCESSFUL_RESPONSE = "successfully";
	public static final String OPERATOR_ROLE = "Operator";
	public static final String ADMIN_ROLE = "Administrator";
	public static final int MIN_DECODER_ID = 1;
	public static final int MAX_DECODER_ID = 3;
	public static final int MIN_NUMBER_OF_FAILED_MONITOR_METRIC = 3;
	public static final Integer MIN_STILL_IMAGE_DELAY = 1;
	public static final Integer DEFAULT_STILL_IMAGE_DELAY = 1;
	public static final Integer MAX_STILL_IMAGE_DELAY = 1000;
	public static final Integer MIN_BUFFERING_DELAY = 0;
	public static final Integer MAX_BUFFERING_DELAY = 3000;
	public static final Integer MIN_MULTI_SYNC_BUFFERING_DELAY = 0;
	public static final Integer DEFAULT_MULTI_SYNC_BUFFERING_DELAY = 1000;
	public static final Integer MAX_MULTI_SYNC_BUFFERING_DELAY = 10000;


}
