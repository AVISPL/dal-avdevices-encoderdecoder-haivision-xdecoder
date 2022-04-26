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
	public static final String COMMA = ",";
	public static final String COLON = ":";
	public static final String DASH = "-";
	public static final String SPACE =  " ";
	public static final String EMPTY = "";
	public static final String NONE = "None";
	public static final String GETTING_DEVICE_INFO_ERR = "Failed to get device info";
	public static final String GETTING_DECODER_STATS_ERR = "Failed to get decoder statistic";
	public static final String GETTING_STREAM_STATS_ERR = "Failed to get stream statistic";
	public static final String PORT_NUMBER_ERROR = "Invalid port number";
	public static final int MIN_DECODER_ID = 1;
	public static final int MAX_DECODER_ID = 2;
	public static final int MIN_NUMBER_OF_FAILED_MONITOR_METRIC = 3;

}