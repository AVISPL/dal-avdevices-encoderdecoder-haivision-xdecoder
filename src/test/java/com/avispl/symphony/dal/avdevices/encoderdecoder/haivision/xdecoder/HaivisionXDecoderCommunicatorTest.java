/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DeviceInfoMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.MonitoringMetricGroup;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.data.MonitoringData;

/**
 * Unit test for HaivisionXDecoderCommunicator
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
	 * Test HaivisionXDecoderCommunicator.getMultipleStatistics successful with valid username password
	 * Expected retrieve valid device monitoring data
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorGetMonitoringDataSuccessful() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionX4DecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

		Assertions.assertEquals(MonitoringData.SERIAL_NUMBER.getData(), stats.get(DeviceInfoMetric.SERIAL_NUMBER.getName()));
		Assertions.assertEquals(MonitoringData.BOOT_VERSION.getData(), stats.get(DeviceInfoMetric.BOOT_VERSION.getName()));
		Assertions.assertEquals(MonitoringData.CARD_TYPE.getData(), stats.get(DeviceInfoMetric.CARD_TYPE.getName()));
		Assertions.assertEquals(MonitoringData.CPLD_REVISION.getData(), stats.get(DeviceInfoMetric.CPLD_REVISION.getName()));
		Assertions.assertEquals(MonitoringData.FIRMWARE_DATE.getData(), stats.get(DeviceInfoMetric.FIRMWARE_DATE.getName()));
		Assertions.assertEquals(MonitoringData.FIRMWARE_OPTIONS.getData(), stats.get(DeviceInfoMetric.FIRMWARE_OPTIONS.getName()));
		Assertions.assertEquals(MonitoringData.FIRMWARE_VERSION.getData(), stats.get(DeviceInfoMetric.FIRMWARE_VERSION.getName()));
		Assertions.assertEquals(MonitoringData.HARDWARE_COMPATIBILITY.getData(), stats.get(DeviceInfoMetric.HARDWARE_COMPATIBILITY.getName()));
		Assertions.assertEquals(MonitoringData.HARDWARE_VERSION.getData(), stats.get(DeviceInfoMetric.HARDWARE_VERSION.getName()));
		Assertions.assertEquals(MonitoringData.PART_NUMBER.getData(), stats.get(DeviceInfoMetric.PART_NUMBER.getName()));
	}
}
