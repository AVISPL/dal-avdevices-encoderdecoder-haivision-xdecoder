package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto;

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

	public static Map<String, Object> convertDataToObject(String responseData, String request) {

		try {
			String[] fields = responseData.split("\r\n");

			Map<String, Object> rootObject = new HashMap<>();
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
					rootObject.put(key.replaceAll("[1-9\\s+]", ""), object);
				} else {
					object.put(key.replaceAll("\\s+", ""), fieldElement[1]);
				}
			}
			return rootObject;
		} catch (Exception e) {
			return null;
		}
	}

}
