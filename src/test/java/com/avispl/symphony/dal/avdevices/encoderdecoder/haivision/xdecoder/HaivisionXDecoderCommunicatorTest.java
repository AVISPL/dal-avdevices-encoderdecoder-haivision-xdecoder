/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder;

import java.util.Map;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.ControllingMetricGroup;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.audio.controllingmetric.AudioControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.DecoderControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.OutputFrameRate;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.OutputResolution;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.StillImage;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.AudioOutput;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.HDMIControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.SurroundSound;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.VideoSource;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Fec;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.NetworkType;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.SRTMode;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.StreamControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.talkback.controllingmetric.TalkbackControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.service.controllingmetric.ServiceControllingMetric;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DeviceInfoMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Encapsulation;

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
		haivisionXDecoderCommunicator.setLogin("harry");
		haivisionXDecoderCommunicator.setPassword("123456789");
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
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIBufferingMode() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set enable buffering to enabled
		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.SYNC_MODE.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set buffering mode to Fixed
		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_MODE.getName();
		propertyValue = "Fixed";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: symphony will show the buffering delay numeric control
		Assertions.assertNotNull(stats.get("DecoderSDI1#" + DecoderControllingMetric.BUFFERING_DELAY.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: buffering delay max value
	 *
	 *Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIBufferingDelayToMaxValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set enable buffering to enabled
		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.SYNC_MODE.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set buffering mode to Fixed
		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_MODE.getName();
		propertyValue = "Fixed";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set buffering delay value to 100000
		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_DELAY.getName();
		propertyValue = "100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: symphony will set value to max of fixed buffering delay when the input greater than max value
		Assertions.assertEquals(DecoderConstant.MAX_BUFFERING_DELAY.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty stream control: buffering delay min value
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIBufferingDelayToMinValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set enable buffering to enabled
		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.SYNC_MODE.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set buffering mode to Fixed
		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_MODE.getName();
		propertyValue = "Fixed";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set buffering delay value to -100000
		propertyName = "DecoderSDI1#" + DecoderControllingMetric.BUFFERING_DELAY.getName();
		propertyValue = "-10000000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: symphony will set value to max of fixed buffering delay when the input greater than max value
		Assertions.assertEquals(DecoderConstant.MIN_BUFFERING_DELAY.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Native Output Resolution
	 *
	 * Expected: control successfully
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

		// Expected: Symphony will not show Output Frame Rate dropdown control
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
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIStillDelayToMaxValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set still image delay value to 100000
		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.STILL_IMAGE_DELAY.getName();
		String propertyValue = "100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: symphony will set value to max of Still Image Delay when the input greater than max value
		Assertions.assertEquals(DecoderConstant.MAX_STILL_IMAGE_DELAY.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty DecoderSDI control: Still Image Delay min value
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetDecoderSDIStillDelayToMinValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set still image delay value to -100000
		String propertyName = "DecoderSDI1#" + DecoderControllingMetric.STILL_IMAGE_DELAY.getName();
		String propertyValue = "-100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		//Expected: symphony will set value to min of Still Image Delay when the input greater than max value
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

		// set network type to Multicast
		String propertyName = "CreateStream#" + StreamControllingMetric.NETWORK_TYPE.getName();
		String propertyValue = NetworkType.MULTI_CAST.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set network type to Unicast
		propertyName = "CreateStream#" + StreamControllingMetric.NETWORK_TYPE.getName();
		propertyValue = NetworkType.UNI_CAST.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set network type to Multicast
		propertyName = "CreateStream#" + StreamControllingMetric.NETWORK_TYPE.getName();
		propertyValue = NetworkType.MULTI_CAST.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: the Multicast Address and Source Address Will be reset to Empty the When we switch back and forth between Unicast and Multicast
		Assertions.assertEquals(DecoderConstant.EMPTY, stats.get("CreateStream#" + StreamControllingMetric.MULTICAST_ADDRESS.getName()));
		Assertions.assertEquals(DecoderConstant.EMPTY, stats.get("CreateStream#" + StreamControllingMetric.SOURCE_ADDRESS.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: port max value
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamPortToMaxValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set port value to 100000
		String propertyName = "CreateStream#" + StreamControllingMetric.PORT.getName();
		String propertyValue = "100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: symphony will set value to max of Still Image Delay when the input greater than max value
		Assertions.assertEquals(DecoderConstant.MAX_PORT.toString(), stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: port min value
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamPortToMinValue() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set port value to -100000
		String propertyName = "CreateStream#" + StreamControllingMetric.PORT.getName();
		String propertyValue = "-100000";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: symphony will set value to min of port when the input greater than max value
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

		// Expected: control successfully
		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: TS-SRT protocol
	 *
	 * Expected: control successfully
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

		// Expected: Symphony will show default config when switch from TS-UDP to TS-SRT
		Assertions.assertEquals(SRTMode.LISTENER.getUiName(), stats.get("CreateStream#" + StreamControllingMetric.SRT_MODE.getName()));
		Assertions.assertEquals("125", stats.get("CreateStream#" + StreamControllingMetric.LATENCY.getName()));
		Assertions.assertEquals("0", stats.get("CreateStream#" + StreamControllingMetric.ENCRYPTED.getName()));
		Assertions.assertEquals("0", stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName()));
		Assertions.assertNotNull(stats.get("CreateStream#" + StreamControllingMetric.PORT.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: TS-SRT protocol/ Listener SRT mode/ SRT to UDP Stream Conversion
	 *
	 * Expected: control successfully
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

		// set srt to udp stream conversion to enabled
		propertyName = "CreateStream#" + StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: Symphony will show default config when switch from TS-UDP to TS-SRT and SRT to UDP Stream Conversion is enabled
		Assertions.assertEquals("0x80", stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_TOS.getName()));
		Assertions.assertEquals("64", stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_TTL.getName()));
		Assertions.assertNotNull(stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName()));
		Assertions.assertNotNull(stats.get("CreateStream#" + StreamControllingMetric.SRT_TO_UDP_PORT.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: TS-SRT protocol/ Listener SRT mode/ SRT to UDP Stream Conversion is enabled
	 *
	 * Expected: control successfully
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

		// set encrypted to enabled
		propertyName = "CreateStream#" + StreamControllingMetric.ENCRYPTED.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: Symphony will show Reject Unencrypted Callers control and Passphrase control
		Assertions.assertEquals("1", stats.get("CreateStream#" + StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName()));
		Assertions.assertNotNull(stats.get("CreateStream#" + StreamControllingMetric.PASSPHRASE.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: TS-SRT protocol/ Rendezvous SRT mode
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamTSOverSRTProtocolCase3() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set Protocol to TS-SRT
		String propertyName = "CreateStream#" + StreamControllingMetric.ENCAPSULATION.getName();
		String propertyValue = Encapsulation.TS_OVER_SRT.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set SRT mode to Rendezvous
		propertyName = "CreateStream#" + StreamControllingMetric.SRT_MODE.getName();
		propertyValue = "Rendevous";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set destination port to 1025
		propertyName = "CreateStream#" + StreamControllingMetric.DESTINATION_PORT.getName();
		propertyValue = "1025";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// expected: source port equal destination port
		Assertions.assertEquals(propertyValue, stats.get("CreateStream#" + StreamControllingMetric.SOURCE_PORT.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: Cancel changes
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamTSOverSRTProtocolCase4() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set Protocol to TS-SRT
		String propertyName = "CreateStream#" + StreamControllingMetric.ENCAPSULATION.getName();
		String propertyValue = Encapsulation.TS_OVER_SRT.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set SRT mode to Rendezvous
		propertyName = "CreateStream#" + StreamControllingMetric.SRT_MODE.getName();
		propertyValue = "Rendevous";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// push cancel button
		propertyName = "CreateStream#" + StreamControllingMetric.CANCEL.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// expected: symphony will not show cancel button and edited field is false
		Assertions.assertNull(stats.get("CreateStream#" + StreamControllingMetric.CANCEL.getName()));
		Assertions.assertEquals(DecoderConstant.FALSE_VALUE, stats.get("CreateStream#" + StreamControllingMetric.EDITED.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Create Stream control: Create Stream
	 *
	 * Expected: Symphony will show Reject Unencrypted Callers control and Passphrase control
	 */
	@Tag("RealDevice")
	@Test
	void testSetCreateStreamTSOverSRTProtocolCase5() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// delete stream name "Harry"
		String propertyName = "Stream#" + "Harry" + StreamControllingMetric.DELETE;
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set Protocol to TS-SRT
		propertyName = "CreateStream#" + StreamControllingMetric.ENCAPSULATION.getName();
		propertyValue = Encapsulation.TS_OVER_SRT.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set Stream Name to Harry
		propertyName = "CreateStream#" + StreamControllingMetric.ENCAPSULATION.getName();
		propertyValue = Encapsulation.TS_OVER_SRT.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// set port to 1025
		propertyName = "CreateStream#" + StreamControllingMetric.PORT.getName();
		propertyValue = "1089";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// expected: stream is created
		Assertions.assertNull(stats.get("Stream#" + "Harry" + StreamControllingMetric.DELETE));
		Assertions.assertNotNull(stats.get("Stream#" + "Harry" + StreamControllingMetric.DELETE));
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
	 * Test HaivisionXDecoder.controlProperty HDMI control: Source
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetHDMISource1() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "HDMI#" + HDMIControllingMetric.VIDEO_SOURCE.getName();
		String propertyValue = VideoSource.DEFAULT_VIDEO_SOURCE.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);

		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "HDMI#" + HDMIControllingMetric.APPLY_CHANGE.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);

		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "HDMI#" + HDMIControllingMetric.VIDEO_SOURCE.getName();
		propertyValue = VideoSource.DECODER_2.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);

		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "HDMI#" + HDMIControllingMetric.APPLY_CHANGE.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		haivisionXDecoderCommunicator.getMultipleStatistics();


		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty HDMI control: Cancel
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetHDMICancel() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "HDMI#" + HDMIControllingMetric.VIDEO_SOURCE.getName();
		String propertyValue = VideoSource.DECODER_1.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "HDMI#" + HDMIControllingMetric.CANCEL.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: symphony will not show apply changes/ cancel changes button and edited field is false
		Assertions.assertNull(stats.get("HDMI#" + HDMIControllingMetric.CANCEL.getName()));
		Assertions.assertNull(stats.get("HDMI#" + HDMIControllingMetric.APPLY_CHANGE.getName()));
		Assertions.assertEquals(DecoderConstant.FALSE_VALUE, stats.get("HDMI#" + HDMIControllingMetric.EDITED.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty HDMI control: Apply changes
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetHDMIApplyChanges() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "HDMI#" + HDMIControllingMetric.VIDEO_SOURCE.getName();
		String propertyValue = VideoSource.DECODER_1.getUiName();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "HDMI#" + HDMIControllingMetric.APPLY_CHANGE.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// Expected: control successfully
		Assertions.assertEquals(VideoSource.DECODER_1.getUiName(), stats.get("HDMI#" + HDMIControllingMetric.VIDEO_SOURCE.getName()));
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

	/**
	 * Test HaivisionXDecoderCommunicator.controlProperty to set audio config case: audio level
	 * Expected set audio successfully
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorSetAudioCaseAudioLevel() {
		haivisionXDecoderCommunicator.getMultipleStatistics();
		ControllableProperty property = new ControllableProperty();
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.AUDIO_ZERO_DBFS_AUDIO_LEVEL.getName());
		property.setValue("5");
		haivisionXDecoderCommunicator.controlProperty(property);
		haivisionXDecoderCommunicator.getMultipleStatistics();
		property.setValue("1");
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.APPLY_CHANGE.getName());
		haivisionXDecoderCommunicator.controlProperty(property);
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Assertions.assertEquals("5", extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName()
				+ DecoderConstant.HASH + AudioControllingMetric.AUDIO_ZERO_DBFS_AUDIO_LEVEL.getName()));
	}

	/**
	 * Test HaivisionXDecoderCommunicator.controlProperty to set audio config case: audio channel
	 * Expected set audio successfully
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorSetAudioCaseAudioChannels() {
		haivisionXDecoderCommunicator.getMultipleStatistics();
		ControllableProperty property = new ControllableProperty();
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.AUDIO_CHANNELS.getName());
		property.setValue("3&4");
		haivisionXDecoderCommunicator.controlProperty(property);
		haivisionXDecoderCommunicator.getMultipleStatistics();
		property.setValue("1");
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.APPLY_CHANGE.getName());
		haivisionXDecoderCommunicator.controlProperty(property);
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Assertions.assertEquals("3&4", extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName()
				+ DecoderConstant.HASH + AudioControllingMetric.AUDIO_CHANNELS.getName()));
	}

	/**
	 * Test HaivisionXDecoderCommunicator.controlProperty to set audio config case: audio source
	 * Expected set audio successfully
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorSetAudioCaseAudioSource() {
		haivisionXDecoderCommunicator.getMultipleStatistics();
		ControllableProperty property = new ControllableProperty();
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.AUDIO_SOURCE.getName());
		property.setValue("SDI2");
		haivisionXDecoderCommunicator.controlProperty(property);
		property.setValue("1");
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.APPLY_CHANGE.getName());
		haivisionXDecoderCommunicator.controlProperty(property);
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		// Expect 'Source' is SDI2
		Assertions.assertEquals("SDI2", extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName()
				+ DecoderConstant.HASH + AudioControllingMetric.AUDIO_SOURCE.getName()));
		// Expect 'Edited' = false
		Assertions.assertEquals("False", extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.EDITED.getName()));
	}

	/**
	 * Test HaivisionXDecoderCommunicator.controlProperty to set audio config case: no property is edited
	 * Expected set audio successfully
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorSetAudioCaseNoPropertyIsEdited() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		// ApplyChanges is null
		Assertions.assertNull(extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.APPLY_CHANGE.getName()));
		// CancelChanges button is null
		Assertions.assertNull(extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.CANCEL.getName()));
		// Edited is false
		Assertions.assertEquals("False", extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.EDITED.getName()));
	}

	/**
	 * Test HaivisionXDecoderCommunicator.controlProperty to set audio config case: ApplyChanges button is hidden after setting audio source
	 * Expected set audio successfully
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorSetAudioCaseApplyChangeButtonIsHiddenAfterSetAudioSource() {
		haivisionXDecoderCommunicator.getMultipleStatistics();
		ControllableProperty property = new ControllableProperty();
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.AUDIO_SOURCE.getName());
		property.setValue("SDI2");
		haivisionXDecoderCommunicator.controlProperty(property);
		haivisionXDecoderCommunicator.getMultipleStatistics();
		property.setValue("1");
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.APPLY_CHANGE.getName());
		haivisionXDecoderCommunicator.controlProperty(property);
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		// Expect AudioSource is SDI2
		Assertions.assertEquals("SDI2", extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName()
				+ DecoderConstant.HASH + AudioControllingMetric.AUDIO_SOURCE.getName()));
		// ApplyChanges button is null
		Assertions.assertNull(extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.APPLY_CHANGE.getName()));
		// CancelChanges button is null
		Assertions.assertNull(extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.CANCEL.getName()));
	}

	/**
	 * Test HaivisionXDecoderCommunicator.controlProperty to set audio config case: CancelChanges button is clicked.
	 * Expected set audio successfully
	 */
	@Tag("RealDevice")
	@Test
	void testHaivisionX4DecoderCommunicatorSetAudioCaseCancel() {
		haivisionXDecoderCommunicator.getMultipleStatistics();
		ControllableProperty property = new ControllableProperty();
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.AUDIO_SOURCE.getName());
		property.setValue("SDI2");
		haivisionXDecoderCommunicator.controlProperty(property);
		haivisionXDecoderCommunicator.getMultipleStatistics();
		property.setValue("1");
		property.setProperty(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.CANCEL.getName());
		haivisionXDecoderCommunicator.controlProperty(property);
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Assertions.assertEquals("SDI2", extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName()
				+ DecoderConstant.HASH + AudioControllingMetric.AUDIO_SOURCE.getName()));
		// ApplyChanges button is null
		Assertions.assertNull(extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.APPLY_CHANGE.getName()));
		// CancelChanges button is null
		Assertions.assertNull(extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.CANCEL.getName()));
		// Expect 'Edited' = false
		Assertions.assertEquals("False", extendedStatistics.getStatistics().get(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.EDITED.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Service control: enable ems
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testEnableEms() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "Services#" + ServiceControllingMetric.EMS.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Service control: enable ems
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testDisableEms() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "Services#" + ServiceControllingMetric.EMS.getName();
		String propertyValue = "0";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		haivisionXDecoderCommunicator.getMultipleStatistics();
		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Service control: enable ems
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetServiceEnableTalkback() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "Services#" + ServiceControllingMetric.TALK_BACK.getName();
		String propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		haivisionXDecoderCommunicator.getMultipleStatistics();
		haivisionXDecoderCommunicator.getMultipleStatistics();

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty Service control: enable ems
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetServiceDisableTalkback() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "Services#" + ServiceControllingMetric.TALK_BACK.getName();
		String propertyValue = "0";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty talkback control: active stream name
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetTalkBackActiveStreamName() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "Talkback#" + TalkbackControllingMetric.ACTIVE_DECODER_SDI.getName();
		String propertyValue = "SDI1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);
		haivisionXDecoderCommunicator.getMultipleStatistics();

		propertyName = "Talkback#" + TalkbackControllingMetric.ACTIVE.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "Talkback#" + TalkbackControllingMetric.APPLY_CHANGE.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "Talkback#" + TalkbackControllingMetric.ACTIVE.getName();
		propertyValue = "0";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "Talkback#" + TalkbackControllingMetric.ACTIVE_DECODER_SDI.getName();
		propertyValue = "SDI2";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "Talkback#" + TalkbackControllingMetric.APPLY_CHANGE.getName();
		propertyValue = "0";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		haivisionXDecoderCommunicator.getMultipleStatistics();
		haivisionXDecoderCommunicator.getMultipleStatistics();

		Assertions.assertEquals(propertyValue, stats.get(propertyName));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty talkback control: port
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetTalkbackPort() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		String propertyName = "Talkback#" + TalkbackControllingMetric.PORT.getName();
		String propertyValue = "1975";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);
		haivisionXDecoderCommunicator.getMultipleStatistics();

		// expected: control successful, symphony will show apply changes, cancel changes button and the edited field is true
		Assertions.assertEquals(propertyValue, stats.get(propertyName));
		Assertions.assertNotNull(stats.get("Talkback#" + TalkbackControllingMetric.APPLY_CHANGE.getName()));
		Assertions.assertNotNull(stats.get("Talkback#" + TalkbackControllingMetric.CANCEL.getName()));
		Assertions.assertEquals(DecoderConstant.TRUE_VALUE, stats.get("Talkback#" + TalkbackControllingMetric.EDITED.getName()));
	}

	/**
	 * Test HaivisionXDecoder.controlProperty talkback control: ApplyChanges
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetTalkbackApplyChanges() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set port to 1975
		String propertyName = "Talkback#" + TalkbackControllingMetric.PORT.getName();
		String propertyValue = "9177";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "Talkback#" + TalkbackControllingMetric.APPLY_CHANGE.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// expected: symphony will not show apply changes, cancel changes button and the edited field is true
		Assertions.assertNull(stats.get("Talkback#" + TalkbackControllingMetric.APPLY_CHANGE.getName()));
		Assertions.assertNull(stats.get("Talkback#" + TalkbackControllingMetric.CANCEL.getName()));
		Assertions.assertEquals(DecoderConstant.FALSE_VALUE, stats.get("Talkback#" + TalkbackControllingMetric.EDITED.getName()));
	}


	/**
	 * Test HaivisionXDecoder.controlProperty talkback control: CancelChanges
	 *
	 * Expected: control successfully
	 */
	@Tag("RealDevice")
	@Test
	void testSetTalkbackCancelChanges() {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) haivisionXDecoderCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();

		// set sound mode to surround
		String propertyName = "Talkback#" + TalkbackControllingMetric.PORT.getName();
		String propertyValue = "1975";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		propertyName = "Talkback#" + TalkbackControllingMetric.CANCEL.getName();
		propertyValue = "1";
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(propertyValue);
		haivisionXDecoderCommunicator.controlProperty(controllableProperty);

		// expected: symphony will not show apply changes, cancel changes button and the edited field is true
		Assertions.assertNull(stats.get("Talkback#" + TalkbackControllingMetric.APPLY_CHANGE.getName()));
		Assertions.assertNull(stats.get("Talkback#" + TalkbackControllingMetric.CANCEL.getName()));
		Assertions.assertEquals(DecoderConstant.FALSE_VALUE, stats.get("Talkback#" + TalkbackControllingMetric.EDITED.getName()));
	}

}
