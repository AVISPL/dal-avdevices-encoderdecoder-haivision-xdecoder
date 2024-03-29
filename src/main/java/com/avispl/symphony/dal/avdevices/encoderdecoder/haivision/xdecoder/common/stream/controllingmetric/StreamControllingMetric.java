/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric;

import java.util.Arrays;
import java.util.Optional;

/**
 * Set of stream controlling metric keys
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/8/2022
 * @since 1.0.0
 */
public enum StreamControllingMetric {

	STREAM_NAME("StreamName"),
	ENCAPSULATION("Protocol"),
	RTSP_URL("RTSP URL"),
	MULTICAST_ADDRESS("MulticastAddress"),
	SOURCE_ADDRESS("SourceAddress"),
	REJECT_UNENCRYPTED_CALLERS("RejectUnencryptedCallers"),
	NETWORK_TYPE("Type"),
	ADDRESS("Address"),
	FEC("Fec"),
	ID("Id"),
	LATENCY("Latency(ms)"),
	PASSPHRASE("Passphrase"),
	PORT("ConnectionPort"),
	SOURCE_PORT("ConnectionSourcePort"),
	DESTINATION_PORT("ConnectionDestinationPort"),
	SRT_MODE("ConnectionMode"),
	SRT_TO_UDP_STREAM_CONVERSION("SrtToUdpStreamConversion"),
	SRT_TO_UDP_ADDRESS("SrtToUdpAddress"),
	SRT_TO_UDP_PORT("SrtToUdpPort"),
	SRT_TO_UDP_TOS("SrtToUdpTos"),
	SRT_TO_UDP_TTL("SrtToUdpTtl"),
	ENCRYPTED("Encrypted"),
	CREATE("Create"),
	DELETE("Delete"),
	UPDATE("Update"),
	APPLY_CHANGE("ApplyChanges"),
	CANCEL("CancelChanges"),
	EDITED("Edited");

	private final String name;

	/**
	 * Parameterized constructor
	 *
	 * @param name Name of stream monitoring metric
	 */
	StreamControllingMetric(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method is used to get stream controlling metric by name
	 *
	 * @param name is the name of stream controlling metric that want to get
	 * @return DecoderControllingMetric is the decoder controlling metric that want to get
	 */
	public static StreamControllingMetric getByName(String name) {
		Optional<StreamControllingMetric> streamControllingMetric = Arrays.stream(StreamControllingMetric.values()).filter(controllingMetric -> controllingMetric.getName().equals(name)).findFirst();
		if (streamControllingMetric.isPresent()) {
			return streamControllingMetric.get();
		}
		throw new IllegalArgumentException("Could not find the controlling metric group with name: " + name);
	}
}

