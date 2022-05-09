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

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DeviceInfoMetric;

/**
 * Unit test for HaivisionXDecoderCommunicator
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 3/8/2022
 * @version 1.0
 * @since 1.0
 */
class HaivisionXDecoderCommunicatorTest {
	private HaivisionXDecoderCommunicator haivisionXDecoderCommunicator = new HaivisionXDecoderCommunicator();

	@BeforeEach()
	public void setUp() throws Exception {
		haivisionXDecoderCommunicator.setHost("10.8.51.55");
		haivisionXDecoderCommunicator.setPort(22);
		haivisionXDecoderCommunicator.setLogin("admintma");
		haivisionXDecoderCommunicator.setPassword("12345678");
		haivisionXDecoderCommunicator.setCommandErrorList(Collections.singletonList("~"));
		haivisionXDecoderCommunicator.setCommandSuccessList(Collections.singletonList("~$ "));
		haivisionXDecoderCommunicator.setLoginSuccessList(Collections.singletonList("~$ "));
		haivisionXDecoderCommunicator.init();
		haivisionXDecoderCommunicator.connect();
	}

	@AfterEach()
	public void destroy() throws Exception {
		haivisionXDecoderCommunicator.disconnect();
	}

	/**
	 * Test HaivisionXDecoderCommunicator.getMultipleStatistics successful with valid username password
	 * Expected retrieve valid device monitoring data
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorGetMonitoringDataSuccessful() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

		Assertions.assertEquals("HAI-031428030014", stats.get(DeviceInfoMetric.SERIAL_NUMBER.getName()));
		Assertions.assertEquals("\"U-Boot 2010.06 (Mar 19 2014 - 10:37:19)-MakitoXD 0.9.14\"", stats.get(DeviceInfoMetric.BOOT_VERSION.getName()));
		Assertions.assertEquals("\"Makito2 Decoder\"", stats.get(DeviceInfoMetric.CARD_TYPE.getName()));
		Assertions.assertEquals("5 (Official, Internal flash)", stats.get(DeviceInfoMetric.CPLD_VERSION.getName()));
		Assertions.assertEquals("\"Feb 28 2022\"", stats.get(DeviceInfoMetric.FIRMWARE_DATE.getName()));
		Assertions.assertEquals("\"KLV, SRT\"", stats.get(DeviceInfoMetric.FIRMWARE_OPTIONS.getName()));
		Assertions.assertEquals("2.5.1-9", stats.get(DeviceInfoMetric.FIRMWARE_VERSION.getName()));
		Assertions.assertEquals("-001G", stats.get(DeviceInfoMetric.HARDWARE_COMPATIBILITY.getName()));
		Assertions.assertEquals("--", stats.get(DeviceInfoMetric.HARDWARE_VERSION.getName()));
		Assertions.assertEquals("B-292D-HD2", stats.get(DeviceInfoMetric.PART_NUMBER.getName()));
	}


	/**
	 * Test HaivisionX4Decoder.controlProperty decoder control: output control (switch control)
	 */
	@Tag("RealDevice")
	@Test
	void testSetOutputControl() {
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty("DecoderSDI2" + "#OutputResolution");
		controllableProperty.setValue("1280x1024 (SXGA)");

		haivisionXDecoderCommunicator.getMultipleStatistics();
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);
		haivisionXDecoderCommunicator.getMultipleStatistics();


		controllableProperty.setProperty("DecoderSDI2" + "#EnableBuffering");
		controllableProperty.setValue("1");

		haivisionXDecoderCommunicator.getMultipleStatistics();
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);
		haivisionXDecoderCommunicator.getMultipleStatistics();


		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

	}
}
