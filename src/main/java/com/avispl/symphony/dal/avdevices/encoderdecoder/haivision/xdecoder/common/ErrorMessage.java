package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common;

import java.util.HashMap;
import java.util.Optional;

/**
 * ErrorMessage
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/15/2022
 * @since 1.0.0
 */
public class ErrorMessage {
	private ErrorMessage() {
	}

	private static HashMap<String, String> errorMessages;

	public static String getErrorMessage(String errorMessage) {
		if (errorMessages == null) {
			errorMessages = new HashMap<>();
			errorMessages.put("Decoder 2 configuration failed: \\\"Stream still in use\\\"\"}", "Please choose a different secondary stream");
		}
		Optional<String> result = Optional.ofNullable(errorMessages.get(errorMessages));
		if (result.isPresent()) {
			return result.get();
		}
		return errorMessage;
	}

}
