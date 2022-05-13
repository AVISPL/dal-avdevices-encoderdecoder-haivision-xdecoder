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
	public static final String REGEX_ONLY_GET_DOUBLE = "[^0-9?!\\\\.\\\\-]";
	public static final String REGEX_REMOVE_SPACE_AND_NUMBER = "[1-9\\s+]";
	public static final String REGEX_SPECIAL_CHARACTER = "[\\(\\)&]";
	public static final String REGEX_SPECIAL_CHARACTER_REPLACEMENT = "\\\\$0";
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
	public static final String CREATE = "Create";
	public static final String CREATING = "Creating";
	public static final String DELETE = "Delete";
	public static final String DELETING = "Deleting";
	public static final String ON = "On";
	public static final String OFF = "Off";
	public static final String ENABLE = "Enable";
	public static final String DISABLE = "Disable";
	public static final String SUCCESSFUL_RESPONSE = "successfully";
	public static final String OPERATOR_ROLE = "Operator";
	public static final String ADMIN_ROLE = "Administrator";
	public static final String STREAM_CONVERSION_OBJECT_RESPONSE = "Stream Flipping";
	public static final String STREAM_CONVERSION_ALT_OBJECT_RESPONSE = "StreamConversion:\r\nStream Flipping\t";
	public static final String TRUE_VALUE = "True";
	public static final String FALSE_VALUE = "False";
	public static final String DEFAULT_TOS = "0x80";
	public static final String MAX_OF_TOS = "FF";
	public static final String MIN_OF_TOS = "00";
	public static final String HEX_PREFIX = "0x";
	public static final int PERCENT_VALUE_DATA_INDEX = 1;
	public static final int SOURCE_ADDRESS_DATA_INDEX = 2;
	public static final int ADDRESS_DATA_INDEX = 0;
	public static final int MIN_DECODER_ID = 1;
	public static final int MAX_DECODER_ID = 3;
	public static final int MIN_NUMBER_OF_FAILED_MONITOR_METRIC = 3;
	public static final Integer MIN_STILL_IMAGE_DELAY = 1;
	public static final Integer DEFAULT_STILL_IMAGE_DELAY = 3;
	public static final Integer MAX_STILL_IMAGE_DELAY = 1000;
	public static final Integer MIN_BUFFERING_DELAY = 0;
	public static final Integer MAX_BUFFERING_DELAY = 2000;
	public static final Integer MIN_MULTI_SYNC_BUFFERING_DELAY = 0;
	public static final Integer DEFAULT_MULTI_SYNC_BUFFERING_DELAY = 1000;
	public static final Integer MAX_MULTI_SYNC_BUFFERING_DELAY = 10000;
	public static final Integer MIN_PORT = 1025;
	public static final Integer MAX_PORT = 65535;
	public static final Integer MIN_LATENCY = 20;
	public static final Integer MAX_LATENCY = 8000;
	public static final Integer DEFAULT_LATENCY = 125;
	public static final Integer MIN_TTL = 1;
	public static final Integer MAX_TTL = 255;
	public static final Integer DEFAULT_TTL = 64;
}
