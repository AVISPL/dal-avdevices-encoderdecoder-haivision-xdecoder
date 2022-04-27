/*
 *  Copyright (c) 2021 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

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
	public static String getValueOnly(String data) {
		if(data == null){
			return DecoderConstant.EMPTY;
		}
		return data.replaceAll("[^0-9?!\\.]", DecoderConstant.EMPTY);
	}

	/**
	 * get data value number in string
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataValue(String data) {
		if(data == null){
			return DecoderConstant.EMPTY;
		}
		String[] spiltDataList = data.split(DecoderConstant.SPACE, 2);
		return spiltDataList[0].replaceAll("[^0-9?!\\.]", DecoderConstant.EMPTY);
	}

	/**
	 * get data extra info in string in case the extra data is behind the "last" keyword eg: 0 [0.00%] (Last: Never)
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataExtraInfoCase1(String data) {
		if(data == null){
			return DecoderConstant.EMPTY;
		}
		StringBuilder stringBuilder = new StringBuilder();
		String[] spiltDataList = data.split(DecoderConstant.SPACE);
		for (int i = 0; i < spiltDataList.length; ++i) {
			if (spiltDataList[i].contains("(")) {
				for (int j = i; j < spiltDataList.length; j++) {
					stringBuilder.append(spiltDataList[j] + DecoderConstant.SPACE);
				}
				break;
			}
		}
		return stringBuilder.toString().replace(DecoderConstant.RIGHT_PARENTHESES, DecoderConstant.EMPTY);
	}

	/**
	 * get data extra info in string in case the extra data is cover by parenthesis's  eg: (TC - System Time)
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataExtraInfoCase2(String data) {
		if(data == null){
			return DecoderConstant.EMPTY;
		}
		StringBuilder stringBuilder = new StringBuilder();
		String[] spiltDataList = data.split(DecoderConstant.SPACE);
		for (int i = 0; i < spiltDataList.length; i++) {
			if (spiltDataList[i].contains("(")) {
				for (int j = i; j < spiltDataList.length; j++) {
					stringBuilder.append(spiltDataList[j] + DecoderConstant.SPACE);
				}
				break;
			}
		}
		return stringBuilder.toString().replace(DecoderConstant.RIGHT_PARENTHESES, DecoderConstant.EMPTY).replace(DecoderConstant.LEFT_PARENTHESES, DecoderConstant.EMPTY);
	}

	/**
	 * get data percent value in string
	 *
	 * @param data the normalized data
	 * @return String
	 */
	public static String getDataPercentValue(String data) {
		if(data == null){
			return DecoderConstant.EMPTY;
		}
		String[] spiltDataList = data.split(DecoderConstant.SPACE, 3);
		return spiltDataList[1].replaceAll("[^0-9?!\\.]", DecoderConstant.EMPTY);
	}
}
