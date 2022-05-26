/*
 *  Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

import java.util.UUID;

import com.avispl.symphony.dal.util.StringUtils;

/**
 * Normalizing Data
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/4/2022
 * @since 1.0.0
 */
public class NormalizeData {
	private static final String UUID_DAY = UUID.randomUUID().toString().replace(DecoderConstant.DASH, "");

	private NormalizeData(){
	}

	/**
	 * Format time data
	 *
	 * @param time the time is String
	 * @return String
	 */
	public static String formatTimeData(String time) {
		if(time == null){
			return DecoderConstant.EMPTY;
		}
		if (DecoderConstant.NONE.equals(time)) {
			return DecoderConstant.NONE;
		}
		int index = time.indexOf(DecoderConstant.SPACE);
		if (index > -1) {
			time = time.substring(0, index + 1);
		}
		return time.replace("d", UUID_DAY).replace("s", DecoderConstant.SECOND).replace(UUID_DAY, DecoderConstant.DAY)
				.replace("h", DecoderConstant.HOUR).replace("m", DecoderConstant.MINUTE);
	}

	/**
	 * get value contain number only, eg: 1,000 => 1000
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String convertToNumberValue(String data) {
		if(data == null){
			return DecoderConstant.EMPTY;
		}
		return data.replaceAll(DecoderConstant.REGEX_ONLY_GET_DOUBLE, DecoderConstant.EMPTY);
	}

	/**
	 * get data  number value in string, eg: 128 kbps => 128
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataNumberValue(String data) {
		if (data == null) {
			return DecoderConstant.EMPTY;
		}
		String[] spiltDataList = data.split(DecoderConstant.SPACE, 2);
		int dataNumberIndex = 0;
		if (StringUtils.isNullOrEmpty(spiltDataList[dataNumberIndex])) {
			return DecoderConstant.EMPTY;
		}
		return spiltDataList[dataNumberIndex].replaceAll(DecoderConstant.REGEX_ONLY_GET_DOUBLE, DecoderConstant.EMPTY);
	}

	/**
	 * get data extra info in string in case the extra data is behind the "at" keyword, eg: 7 (0.00%) last one at 2019-01-17 13:40:31.322 => 2019-01-17 13:40:31.322
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataExtraInfo(String data) {
		if (data == null) {
			return DecoderConstant.EMPTY;
		}
		String[] spiltDataList = data.split(DecoderConstant.AT);
		int extraInfoIndex = 1;
		if (extraInfoIndex >= spiltDataList.length || StringUtils.isNullOrEmpty(spiltDataList[extraInfoIndex]))
			return DecoderConstant.EMPTY;
		return spiltDataList[extraInfoIndex];
	}

	/**
	 * get data value from specify space index in string, eg: 7 (0.00%) last one at 2019-01-17 13:40:31.322 / spaceIndex = 1  => 0.00
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataValueBySpaceIndex(String data, int spaceIndex) {
		if (data == null) {
			return DecoderConstant.EMPTY;
		}
		String[] spiltDataList = data.split(DecoderConstant.SPACE, 4);
		if (spaceIndex >= spiltDataList.length || StringUtils.isNullOrEmpty(spiltDataList[spaceIndex])) {
			return DecoderConstant.EMPTY;
		}
		return spiltDataList[spaceIndex].replaceAll(DecoderConstant.REGEX_ONLY_GET_DOUBLE, DecoderConstant.EMPTY);
	}
}