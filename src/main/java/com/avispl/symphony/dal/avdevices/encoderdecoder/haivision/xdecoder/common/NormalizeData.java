/*
 *  Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

import java.util.Optional;
import java.util.UUID;

/**
 * Normalizing Data
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
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
	 * get value contain number only
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String convertToNumberValue(String data) {
		if(data == null){
			return DecoderConstant.EMPTY;
		}
		return data.replaceAll("[^0-9?!\\.]", DecoderConstant.EMPTY);
	}

	/**
	 * get data  number value in string, eg: 128 kbps
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataNumberValue(String data) {
		if (data == null) {
			return DecoderConstant.EMPTY;
		}
		String[] spiltDataList = data.split(DecoderConstant.SPACE, 2);
		if (spiltDataList[0] == null) {
			return DecoderConstant.EMPTY;
		}
		return spiltDataList[0].replaceAll("[^0-9?!\\.]", DecoderConstant.EMPTY);
	}

	/**
	 * get data extra info in string in case the extra data is behind the "at" keyword, eg: 7 (0.00%) last one at 2019-01-17 13:40:31.322
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataExtraInfoCase1(String data) {
		if (data == null) {
			return DecoderConstant.EMPTY;
		}
		String[] spiltDataList = data.split(DecoderConstant.AT);
		Optional<String> result = Optional.ofNullable(spiltDataList[1]);

		return result.orElse(DecoderConstant.EMPTY);
	}

	/**
	 * get data percent value in string
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataPercentValue(String data) {
		if (data == null) {
			return DecoderConstant.EMPTY;
		}
		String[] spiltDataList = data.split(DecoderConstant.SPACE, 3);
		if (spiltDataList[1] == null) {
			return DecoderConstant.EMPTY;
		}
		return spiltDataList[1].replaceAll("[^0-9?!\\.]", DecoderConstant.EMPTY);
	}
}
