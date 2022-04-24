/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit test for HaivisionX4DecoderCommunicator
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
 * @version 1.0
 * @since 1.0
 */
public class HaivisionXDecoderCommunicatorTest {
	private HaivisionXDecoderCommunicator haivisionX4DecoderCommunicator = new HaivisionXDecoderCommunicator();

	@BeforeEach()
	public void setUp() throws Exception {
		haivisionX4DecoderCommunicator.setHost("***REMOVED***");
		haivisionX4DecoderCommunicator.setPort(22);
		haivisionX4DecoderCommunicator.setLogin("admin");
		haivisionX4DecoderCommunicator.setPassword("AVIadm1n");
		haivisionX4DecoderCommunicator.setCommandErrorList(Collections.singletonList("~"));
		haivisionX4DecoderCommunicator.setCommandSuccessList(Collections.singletonList("~$ "));
		haivisionX4DecoderCommunicator.setLoginSuccessList(Collections.singletonList("~$ "));
		haivisionX4DecoderCommunicator.init();
		haivisionX4DecoderCommunicator.connect();
	}

	@AfterEach()
	public void destroy() throws Exception {
		haivisionX4DecoderCommunicator.disconnect();
	}

	/**
	 * Test HaivisionX4DecoderCommunicator.getMultipleStatistics successful with valid username password
	 * Expected retrieve valid device monitoring data
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorGetMonitoringDataSuccessful() throws Exception {
		Map<String, String> stats = new HashMap<>();
		Long currentTime = System.currentTimeMillis();

		haivisionX4DecoderCommunicator.retrieveDecoderInfo(stats);
		System.out.println(System.currentTimeMillis() - currentTime);
	}
}
