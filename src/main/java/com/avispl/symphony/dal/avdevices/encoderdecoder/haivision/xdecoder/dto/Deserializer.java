package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;

/**
 * Deserializer
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/19/2022
 * @since 1.0.0
 */
public class Deserializer {
	private Deserializer(){

	}

	/**
	 * This method is used to convert response data to object
	 * @param responseData
	 * @param request
	 * @return objectWrapper
	 */
	public static Map<String, Object> convertDataToObject(String responseData, String request) {
		try {
			String[] fields = responseData.split("\r\n");

			Map<String, Object> objectWrapper = new HashMap<>();
			Map<String, String> object = new HashMap<>();

			for (String field : fields) {
				String[] fieldElement = field.split(DecoderConstant.COLON, 2);
				String key = fieldElement[0];

				if (!key.contains("\t") && (fieldElement.length == 1 || fieldElement[1].trim().isEmpty())) {
					if (!key.isEmpty()) {
						object = new HashMap<>();
					} else {
						key = request.replaceAll("[1-9\\s+]", "");
					}
					objectWrapper.put(key.replaceAll("[1-9\\s+]", ""), object);
				} else {
					object.put(key.replaceAll("\\s+", ""), fieldElement[1]);
				}
			}
			return objectWrapper;
		} catch (Exception e) {
			return Collections.emptyMap();
		}
	}

}
