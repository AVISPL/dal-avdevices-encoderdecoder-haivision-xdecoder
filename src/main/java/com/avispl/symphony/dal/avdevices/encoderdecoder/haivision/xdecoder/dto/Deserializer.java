package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;

/**
 * Deserializer
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/19/2022
 * @since 1.0.0
 */
public class Deserializer {

	private static final Log logger = LogFactory.getLog(Deserializer.class);

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

				// get key of wrapper object
				if (!key.contains("\t") && (fieldElement.length == 1 || fieldElement[1].trim().isEmpty())) {
					if (!key.isEmpty()) {
						object = new HashMap<>();
					} else {
						// when wrapper object key is empty, replace empty key by default key
						key = request.replaceAll("[1-9\\s+]", "");
					}

					// put object to object wrapper
					objectWrapper.put(key.replaceAll("\\s+", ""), object);
				} else {
					// put data to object
					object.put(key.replaceAll("\\s+", ""), fieldElement[1]);
				}
			}
			return objectWrapper;
		} catch (Exception e) {
			logger.error("Error while convert data to object: ", e);
			return Collections.emptyMap();
		}
	}
}
