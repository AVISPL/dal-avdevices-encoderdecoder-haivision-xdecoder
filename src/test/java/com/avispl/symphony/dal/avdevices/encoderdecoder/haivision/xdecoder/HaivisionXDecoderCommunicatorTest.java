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
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DeviceInfoMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.DecoderControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.OutputFrameRate;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.OutputResolution;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.StillImage;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.AudioOutput;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.HDMIControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.SurroundSound;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.VideoSource;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Encapsulation;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Fec;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.NetworkType;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.SRTMode;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.StreamControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamStatsMonitoringMetric;

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
		haivisionXDecoderCommunicator.setHost("***REMOVED***");
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

		// Verify Device info monitoring metric
		Assertions.assertEquals("HAI-031428030014", stats.get(DeviceInfoMetric.SERIAL_NUMBER.getName()));
		Assertions.assertEquals("U-Boot 2010.06 (Mar 19 2014 - 10:37:19)-MakitoXD 0.9.14", stats.get(DeviceInfoMetric.BOOT_VERSION.getName()));
		Assertions.assertEquals("Makito2 Decoder", stats.get(DeviceInfoMetric.CARD_TYPE.getName()));
		Assertions.assertEquals("5 (Official, Internal flash)", stats.get(DeviceInfoMetric.CPLD_VERSION.getName()));
		Assertions.assertEquals("Feb 28 2022", stats.get(DeviceInfoMetric.FIRMWARE_DATE.getName()));
		Assertions.assertEquals("KLV, SRT", stats.get(DeviceInfoMetric.FIRMWARE_OPTIONS.getName()));
		Assertions.assertEquals("2.5.1-9", stats.get(DeviceInfoMetric.FIRMWARE_VERSION.getName()));
		Assertions.assertEquals("-001G", stats.get(DeviceInfoMetric.HARDWARE_COMPATIBILITY.getName()));
		Assertions.assertEquals("--", stats.get(DeviceInfoMetric.HARDWARE_VERSION.getName()));
		Assertions.assertEquals("B-292D-HD2", stats.get(DeviceInfoMetric.PART_NUMBER.getName()));
		Assertions.assertNotNull(stats.get(DeviceInfoMetric.TEMPERATURE.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Primary Stream
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIPrimaryStream() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.PRIMARY_STREAM.getName();
		String propertyValue = "(None)";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Secondary Stream
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDISecondaryStream() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.SECONDARY_STREAM.getName();
		String propertyValue = "(None)";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Enable Buffering
	 *
	 * Expected: symphony will show the buffering mode dropdown control
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIEnableBuffering() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.SYNC_MODE.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertNotNull(stats.get("DecoderSDI1#" + DecoderControllingMetric.BUFFERING_MODE.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Fixed Buffering Mode
	 *
	 * Expected: symphony will show the buffering delay numeric control
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIBufferingMode() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.SYNC_MODE.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_MODE.getName();
		propertyValue = "Fixed";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertNotNull(stats.get("DecoderSDI1#" + DecoderControllingMetric.BUFFERING_DELAY.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: buffering delay max value
	 *
	 * Expected: symphony will set value to max of fixed buffering delay when the input greater than max value
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIBufferingDelayToMaxValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.SYNC_MODE.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_MODE.getName();
		propertyValue = "Fixed";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_DELAY.getName();
		propertyValue = "100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(DecoderConstant.MAX_BUFFERING_DELAY.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty stream control: buffering delay min value
	 *
	 * Expected: symphony will set value to min of fixed buffering delay when the input lesser  than in value
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIBufferingDelayToMinValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.SYNC_MODE.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_MODE.getName();
		propertyValue = "Fixed";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_DELAY.getName();
		propertyValue = "-10000000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(DecoderConstant.MIN_BUFFERING_DELAY.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Native Output Resolution
	 *
	 * Expected: Symphony will not show Output Frame Rate dropdown control
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIOutputResolution() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.OUTPUT_RESOLUTION.getName();
		String propertyValue = OutputResolution.NATIVE.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertNull(stats.get(DecoderControllingMetric.OUTPUT_FRAME_RATE.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Output Frame Rate
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIOutputFrameRate() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set Output Resolution to Auto
		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.OUTPUT_RESOLUTION.getName();
		String propertyValue = OutputResolution.AUTOMATIC.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set Output Frame Rate to 60
		propertyName = "DecoderSDI1#" + DecoderControllingMetric.OUTPUT_FRAME_RATE.getName();
		propertyValue = OutputFrameRate.OUTPUT_FRAME_RATE_60.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Still Image
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIStillImage() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set Still Image to select Image
		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.STILL_IMAGE.getName();
		String propertyValue = StillImage.SELECT_IMAGE.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertNotNull(stats.get("DecoderSDI1#" + DecoderControllingMetric.SELECT_STILL_IMAGE.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Still Image Delay  max value
	 *
	 * Expected: symphony will set value to max of Still Image Delay when the input greater than max value
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIStillDelayToMaxValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.STILL_IMAGE_DELAY.getName();
		String propertyValue = "100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(DecoderConstant.MAX_STILL_IMAGE_DELAY.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Still Image Delay min value
	 *
	 * Expected: symphony will set value to min of Still Image Delay when the input greater than max value
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIStillDelayToMinValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.STILL_IMAGE_DELAY.getName();
		String propertyValue = "-100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(DecoderConstant.MIN_STILL_IMAGE_DELAY.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty stream control: StreamName
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamName() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "CreateStream#" + StreamControllingMetric.STREAM_NAME.getName();
		String propertyValue = "Harry";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty stream control: Multicast Type
	 *
	 * Expected: the Multicast Address and Source Address Will be reset to Empty the When we switch back and forth between Unicast and Multicast
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamMulticastType() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "CreateStream#" + StreamControllingMetric.NETWORK_TYPE.getName();
		String propertyValue = NetworkType.MULTI_CAST.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "CreateStream#" + StreamControllingMetric.NETWORK_TYPE.getName();
		propertyValue = NetworkType.UNI_CAST.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "CreateStream#" + StreamControllingMetric.NETWORK_TYPE.getName();
		propertyValue = NetworkType.MULTI_CAST.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(DecoderConstant.EMPTY, stats.get("CreateStream#" + StreamControllingMetric.MULTICAST_ADDRESS.getName()));
		Assertions.assertEquals(DecoderConstant.EMPTY, stats.get("CreateStream#" + StreamControllingMetric.SOURCE_ADDRESS.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: port  max value
	 *
	 * Expected: symphony will set value to max of Still Image Delay when the input greater than max value
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamPortToMaxValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "CreateStream#" + StreamControllingMetric.PORT.getName();
		String propertyValue = "100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(DecoderConstant.MAX_PORT.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: port min value
	 *
	 * Expected: symphony will set value to min of port when the input greater than max value
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamPortToMinValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "CreateStream#" + StreamControllingMetric.PORT.getName();
		String propertyValue = "-100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(DecoderConstant.MIN_PORT.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: Fec
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamFec() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set Protocol to TS-RTP
		String propertyName = "CreateStream#" + StreamControllingMetric.ENCAPSULATION.getName();
		String propertyValue = Encapsulation.TS_OVER_RTP.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set Fec to MPEG Pro
		propertyName = "CreateStream#" + StreamControllingMetric.FEC.getName();
		propertyValue = Fec.MPEG_PRO_FEC.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: TS-SRT protocol
	 *
	 * Expected: Symphony will show default config when switch from TS-UDP to TS-SRT
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamTSOverSRTProtocol() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set Protocol to TS-SRT
		String propertyName = "CreateStream#" + StreamControllingMetric.ENCAPSULATION.getName();
		String propertyValue = Encapsulation.TS_OVER_SRT.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(SRTMode.LISTENER.getUiName(), stats.get("CreateStream#" + StreamControllingMetric.SRT_MODE.getName()));
		Assertions.assertEquals("125", stats.get("CreateStream#" + StreamControllingMetric.LATENCY.getName()));
		Assertions.assertEquals("0", stats.get("CreateStream#" + StreamControllingMetric.ENCRYPTED.getName()));
		Assertions.assertEquals("0", stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName()));
		Assertions.assertNotNull(stats.get("CreateStream#" + StreamControllingMetric.PORT.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: TS-SRT protocol/ Listener SRT mode/ Encrypted is enabled
	 *
	 * Expected: Symphony will show Reject Unencrypted Callers control and Passphrase control
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamTSOverSRTProtocolCase1() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set Protocol to TS-SRT
		String propertyName = "CreateStream#" + StreamControllingMetric.ENCAPSULATION.getName();
		String propertyValue = Encapsulation.TS_OVER_SRT.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set Encrypted to enabled
		propertyName = "CreateStream#" + StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals("0x80", stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_TOS.getName()));
		Assertions.assertEquals("64", stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_TTL.getName()));
		Assertions.assertNotNull(stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName()));
		Assertions.assertNotNull(stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_PORT.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: TS-SRT protocol/ Listener SRT mode/ SRT to UDP Stream Conversion is enabled
	 *
	 * Expected: Symphony will show Reject Unencrypted Callers control and Passphrase control
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamTSOverSRTProtocolCase2() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set Protocol to TS-SRT
		String propertyName = "CreateStream#" + StreamControllingMetric.ENCAPSULATION.getName();
		String propertyValue = Encapsulation.TS_OVER_SRT.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set Encrypted to enabled
		propertyName = "CreateStream#" + StreamControllingMetric.ENCRYPTED.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals("1", stats.get("CreateStream#" + StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName()));
		Assertions.assertNotNull(stats.get("CreateStream#" + StreamControllingMetric.PASSPHRASE.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty HDMI control: Source
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetHDMISource() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "HDMI#" + HDMIControllingMetric.VIDEO_SOURCE.getName();
		String propertyValue = VideoSource.DECODER_1.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);

		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty HDMI control: Audio Out (dropdown)
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetHDMIAudioOut() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set sound mode to stereo
		String propertyName = "HDMI#" + HDMIControllingMetric.SOUND_MODE.getName();
		String propertyValue = SurroundSound.STEREO.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set audio out to 1 & 2
		propertyName = "HDMI#" + HDMIControllingMetric.AUDIO_OUT.getName();
		propertyValue = AudioOutput.CHANNEL_12.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty HDMI control: Surround Sound Mode
	 *
	 * Expected symphony will not show Audio Out dropdown control
	 */
	@Tag("RealDevice")
	@Test
	void testSetHDMISoundMode() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set sound mode to surround
		String propertyName = "HDMI#" + HDMIControllingMetric.SOUND_MODE.getName();
		String propertyValue = SurroundSound.SURROUND.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set audio out to 1 & 2
		propertyName = "HDMI#" + HDMIControllingMetric.AUDIO_OUT.getName();
		propertyValue = AudioOutput.CHANNEL_12.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertNull(stats.get(propertyName));
	}
}
