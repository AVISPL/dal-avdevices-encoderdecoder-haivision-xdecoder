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

	public static final char HASH = '#';
	public static final char NEXT_LINE = '\n';
	public static final char SLASH = '/';
	public static final String COMMA = ",";
	public static final String COLON = ":";
	public static final String RIGHT_PARENTHESES = ")";
	public static final String LEFT_PARENTHESES = "(";
	public static final String DASH = "-";
	public static final String SPACE =  " ";
	public static final String EMPTY = "";
	public static final String NONE = "None";
	public static final String GETTING_DEVICE_INFO_ERR = "Failed to get device info";
	public static final String GETTING_DECODER_STATS_ERR = "Failed to get decoder statistic";
	public static final String GETTING_STREAM_STATS_ERR = "Failed to get stream statistic";
	public static final String PORT_NUMBER_ERROR = "Invalid port number";
	public static final String DAY = " day(s) ";
	public static final String HOUR = " hour(s) ";
	public static final String MINUTE = " minute(s) ";
	public static final String SECOND = " second(s) ";
	public static final String ADDRESS_ANY = "Any";
	public static final String SRT_TO_UDP_TOS = "0x80";
	public static final int MIN_DECODER_ID = 1;
	public static final int MAX_DECODER_ID = 2;
	public static final Integer MIN_STILL_IMAGE_DELAY = 1;
	public static final Integer DEFAULT_STILL_IMAGE_DELAY = 1;
	public static final Integer MAX_STILL_IMAGE_DELAY = 1000;
	public static final Integer MIN_BUFFERING_DELAY = 0;
	public static final Integer MAX_BUFFERING_DELAY = 3000;
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
	public static final Integer DEFAULT_STREAM_ID = -1;
}
