package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

import java.util.HashMap;
import java.util.Optional;

/**
 * Error message
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/15/2022
 * @since 1.0.0
 */
public class ErrorMessage {
	private ErrorMessage() {
	}

	private static HashMap<String, String> errorMessages;

	/**
	 * This method is used to convert error message to specify error message
	 * @param errorMessage error message that want to convert
	 * @return String converted error message
	 */
	public static String convertErrorMessage(String errorMessage) {
		if (errorMessages == null) {
			errorMessages = new HashMap<>();
			errorMessages.put("Decoder 1 configuration failed: \"Stream still in use\"", "Decoder 1 configuration failed: Please choose a different secondary stream");
			errorMessages.put("Decoder 2 configuration failed: \"Stream still in use\"", "Decoder 2 configuration failed: Please choose a different secondary stream");
		}
		Optional<String> result = Optional.ofNullable(errorMessages.get(errorMessage));
		return result.orElse(errorMessage);
	}

}
