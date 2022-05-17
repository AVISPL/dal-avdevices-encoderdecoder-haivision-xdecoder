/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.CommandOperation;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.ControllingMetricGroup;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DeviceInfoMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DropdownList;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.ErrorMessage;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.MonitoringMetricGroup;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.BufferingMode;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.DecoderControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.OutputFrameRate;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.OutputResolution;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.State;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.StillImage;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.controllingmetric.SyncMode;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderAudioMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderStatsMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderTimeCodeMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderVideoMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Encapsulation;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Fec;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.NetworkType;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.RejectUnencrypted;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.SRTMode;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.StreamControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.SwitchOnOffControl;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.SRTMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamStatsMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.Deserializer;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.audioconfig.AudioConfigWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication.AuthenticationRole;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderStatsWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfo;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfoWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.Stream;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConversion;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamStats;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamStatsWrapper;
import com.avispl.symphony.dal.communicator.SshCommunicator;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * An implementation of RestCommunicator to provide communication and interaction with Haivision X Decoders
 * Supported features are:
 * <p>
 * Monitoring:
 * <li>DeviceInfo</li>
 * <li>DecoderStats</li>
 * <li>StreamStats</li>
 * <p>
 * Controlling:
 * <li>Start/Stop /Edit Decoder</li>
 * <li>Create/ Delete Stream</li>
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/6/2022
 * @since 1.0.0
 */
public class HaivisionXDecoderCommunicator extends SshCommunicator implements Monitorable, Controller {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private Map<String, String> failedMonitor;
	private Set<Integer> filteredStreamIDSet;
	private Set<String> streamsNameFiltered;
	private Set<String> streamsStatusFiltered;
	private Set<String> portNumbersFiltered;
	private boolean isUpdateCachedStreamControl = false;
	private boolean isEmergencyDelivery = false;
	private ExtendedStatistics localExtendedStatistics;

	// Decoder and stream DTO
	private List<DecoderConfig> realtimeDecoderConfigs;
	private List<DecoderConfig> cachedDecoderConfigs;
	private List<StreamConfig> realtimeStreamConfigs;
	private List<StreamConfig> cachedStreamConfigs;
	private StreamConfig createStream;
	private List<String> customStillImages;


	//Adapter Properties
	private String streamNameFilter;
	private String portNumberFilter;
	private String streamStatusFilter;
	private String configManagement;

	/**
	 * ReentrantLock to prevent null pointer exception to localExtendedStatistics when controlProperty method is called before GetMultipleStatistics method.
	 */
	private final ReentrantLock reentrantLock = new ReentrantLock();

	/**
	 * Constructor set command error and success list to be used as well the default camera ID
	 */
	public HaivisionXDecoderCommunicator() {
		super();
		// set list of login success strings (included at the end of response when command succeeds, typically ending with command prompt)
		setLoginSuccessList(Collections.singletonList("~$ "));

		// set list of command success strings (included at the end of response when command succeeds, typically ending with command prompt)
		this.setCommandSuccessList(Collections.singletonList("~$ "));

		// set list of error response strings (included at the end of response when command fails, typically ending with command prompt)
		this.setCommandErrorList(Collections.singletonList("~"));
	}

	/**
	 * Retrieves {@code {@link #streamNameFilter }}
	 *
	 * @return value of {@link #streamNameFilter}
	 */
	public String getStreamNameFilter() {
		return streamNameFilter;
	}

	/**
	 * Sets {@code streamsName}
	 *
	 * @param streamsName the {@code java.lang.String} field
	 */
	public void setStreamNameFilter(String streamsName) {
		this.streamNameFilter = streamsName;
	}

	/**
	 * Retrieves {@code {@link #portNumberFilter }}
	 *
	 * @return value of {@link #portNumberFilter}
	 */
	public String getPortNumberFilter() {
		return portNumberFilter;
	}

	/**
	 * Sets {@code portNumber}
	 *
	 * @param portNumberFilter the {@code java.lang.String} field
	 */
	public void setPortNumberFilter(String portNumberFilter) {
		this.portNumberFilter = portNumberFilter;
	}

	/**
	 * Retrieves {@code {@link #streamStatusFilter }}
	 *
	 * @return value of {@link #streamStatusFilter}
	 */
	public String getStreamStatusFilter() {
		return streamStatusFilter;
	}

	/**
	 * Sets {@code streamStatus}
	 *
	 * @param streamStatusFilter the {@code java.lang.String} field
	 */
	public void setStreamStatusFilter(String streamStatusFilter) {
		this.streamStatusFilter = streamStatusFilter;
	}

	/**
	 * Retrieves {@code {@link #configManagement }}
	 *
	 * @return value of {@link #configManagement}
	 */
	public String getConfigManagement() {
		return configManagement;
	}

	/**
	 * Sets {@code controllingCapabilitiesTrigger}
	 *
	 * @param configManagement the {@code java.lang.String} field
	 */
	public void setConfigManagement(String configManagement) {
		this.configManagement = configManagement;
	}

	@Override
	public List<Statistics> getMultipleStatistics() {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Getting statistics from the device X4 decoder at host %s with port %s", this.host, this.getPort()));
		}
		reentrantLock.lock();
		try {
			final ExtendedStatistics extendedStatistics = new ExtendedStatistics();
			final Map<String, String> stats = new HashMap<>();
			final List<AdvancedControllableProperty> advancedControllableProperties = new ArrayList<>();
			failedMonitor = new HashMap<>();
			filteredStreamIDSet = new HashSet<>();

			if (realtimeDecoderConfigs == null) {
				realtimeDecoderConfigs = new ArrayList<>();
			}
			if (realtimeStreamConfigs == null) {
				realtimeStreamConfigs = new ArrayList<>();
			}
			if (cachedDecoderConfigs == null) {
				cachedDecoderConfigs = new ArrayList<>();
			}
			if (cachedStreamConfigs == null) {
				cachedStreamConfigs = new ArrayList<>();
			}
			if (createStream == null) {
				createStream = defaultStream();
			}
			if (!isEmergencyDelivery) {
				populateDecoderMonitoringMetrics(stats);
				if (cachedDecoderConfigs.isEmpty()) {
					cachedDecoderConfigs = realtimeDecoderConfigs.stream().map(DecoderConfig::new).collect(Collectors.toList());
				}
				if (isUpdateCachedStreamControl || cachedStreamConfigs.size() != filteredStreamIDSet.size()) {
					cachedStreamConfigs.clear();
					cachedStreamConfigs = realtimeStreamConfigs.stream().map(StreamConfig::new)
							.filter(streamInfo -> filteredStreamIDSet.contains(Integer.parseInt(streamInfo.getId()))).collect(Collectors.toList());
					isUpdateCachedStreamControl = false;
				}
				// check Role is Admin or Operator
				String role = retrieveUserRole();
				if (role.equals(DecoderConstant.OPERATOR_ROLE) || role.equals(DecoderConstant.ADMIN_ROLE)) {
					populateControllingMetrics(stats, advancedControllableProperties);
					extendedStatistics.setControllableProperties(advancedControllableProperties);
				}

				extendedStatistics.setStatistics(stats);
				localExtendedStatistics = extendedStatistics;
			}
			isEmergencyDelivery = false;
		} finally {
			reentrantLock.unlock();
		}
		return Collections.singletonList(localExtendedStatistics);
	}

	@Override
	public void controlProperty(ControllableProperty controllableProperty) {
		String property = controllableProperty.getProperty();
		String value = String.valueOf(controllableProperty.getValue());

		reentrantLock.lock();
		try {
			if (this.localExtendedStatistics == null) {
				return;
			}
			Map<String, String> stats = this.localExtendedStatistics.getStatistics();
			List<AdvancedControllableProperty> advancedControllableProperties = this.localExtendedStatistics.getControllableProperties();

			if (this.logger.isDebugEnabled()) {
				this.logger.debug("controlProperty property " + property);
				this.logger.debug("controlProperty value " + value);
			}
			// Decoder control
			String[] splitProperty = property.split(String.valueOf(DecoderConstant.HASH));
			if (splitProperty.length != 2) {
				throw new IllegalArgumentException("Unexpected length of control property");
			}
			ControllingMetricGroup controllingGroup = ControllingMetricGroup.getByApiName(splitProperty[0]);

			switch (controllingGroup) {
				case DECODER_SDI:
					String name = splitProperty[0].substring(10);
					Integer decoderID = Integer.parseInt(name);
					decoderControl(stats, advancedControllableProperties, decoderID, splitProperty[1], value);
					break;
				case CREATE_STREAM:
					createStreamControl(stats, advancedControllableProperties, ControllingMetricGroup.CREATE_STREAM.getUiName() + DecoderConstant.HASH, splitProperty[1], value);
					break;
				case STREAM:
					String streamName = splitProperty[0].substring(6);
					streamControl(stats, advancedControllableProperties, streamName, splitProperty[1]);
					break;
				default:
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("Controlling group %s is not supported.", controllingGroup.getUiName()));
					}
					throw new IllegalStateException(String.format("Controlling group %s is not supported.", controllingGroup.getUiName()));
			}
		} finally {
			reentrantLock.unlock();
		}
	}

	@Override
	public void controlProperties(List<ControllableProperty> list) {
		if (CollectionUtils.isEmpty(list)) {
			throw new IllegalArgumentException("NetGearCommunicator: Controllable properties cannot be null or empty");
		}
		for (ControllableProperty controllableProperty : list) {
			controlProperty(controllableProperty);
		}
	}

	/**
	 * This method is used to populate all monitoring properties:
	 * <li>Device info</li>
	 * <li>Decoders statistic</li>
	 * <li>Streams statistic</li>
	 *
	 * @param stats list statistic property
	 * @throws ResourceNotReachableException when failedMonitor said all device monitoring data are failed to get
	 */
	private void populateDecoderMonitoringMetrics(Map<String, String> stats) {
		Objects.requireNonNull(stats);

		retrieveDeviceInfo(stats);
		retrieveDeviceTemperature(stats);
		retrieveDeviceStillImage();
		retrieveStreamStats(stats);
		retrieveAudioConfig();

		for (int decoderID = DecoderConstant.MIN_DECODER_ID; decoderID < DecoderConstant.MAX_DECODER_ID; decoderID++) {
			retrieveDecoderStats(stats, decoderID);
		}

		if (failedMonitor.size() == getNoOfFailedMonitorMetricGroup()) {
			StringBuilder errBuilder = new StringBuilder();
			for (Map.Entry<String, String> failedMetric : failedMonitor.entrySet()) {
				errBuilder.append(failedMetric.getValue());
				errBuilder.append(DecoderConstant.SPACE);
			}
			throw new ResourceNotReachableException(errBuilder.toString());
		}
	}

	/**
	 * This method is used for populate all controlling properties:
	 * <li>Decoder controlling</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 */
	private void populateControllingMetrics(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		// Decoder control
		for (Integer decoderID = DecoderConstant.MIN_DECODER_ID; decoderID < DecoderConstant.MAX_DECODER_ID; decoderID++) {
			populateDecoderControl(stats, advancedControllableProperties, decoderID);
		}

		// Create stream control
		populateCreateStreamControl(stats, advancedControllableProperties, createStream, ControllingMetricGroup.CREATE_STREAM.getUiName() + DecoderConstant.HASH);

		// Stream control
		for (StreamConfig cachedStreamConfig : cachedStreamConfigs) {
			populateStreamConfig(stats, advancedControllableProperties, cachedStreamConfig);

		}
	}

	/**
	 * This method is used to retrieve User Role by send command "account {accountName} get"
	 * This method is used for UT
	 *
	 * @throws ResourceNotReachableException When there is no valid User Role data or having an Exception
	 */
	public String retrieveUserRole() {
		try {
			String request = CommandOperation.OPERATION_ACCOUNT.getName()
					.concat(DecoderConstant.SPACE)
					.concat(getLogin())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.GET.getName());

			String response = send(request);
			String role = null;
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				Object objectResponse = responseMap.get(request.replaceAll(DecoderConstant.REGEX_REMOVE_SPACE_AND_NUMBER, DecoderConstant.EMPTY));
				AuthenticationRole authenticationRole = objectMapper.convertValue(objectResponse, AuthenticationRole.class);
				if (authenticationRole != null) {
					role = authenticationRole.getRole();
				}
			}
			if (StringUtils.isNullOrEmpty(role)) {
				throw new ResourceNotReachableException("Role based is empty");
			}
			return role;
		} catch (Exception e) {
			throw new ResourceNotReachableException("Retrieve role based error: " + e.getMessage(), e);
		}
	}

	//region retrieve device info
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used to retrieve temperature by send command "haiversion"
	 *
	 * @param stats list statistics property
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveDeviceInfo(Map<String, String> stats) {
		try {
			String request = CommandOperation.OPERATION_HAIVERSION.getName();
			String response = send(request);

			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				DeviceInfoWrapper deviceInfoWrapper = objectMapper.convertValue(responseMap, DeviceInfoWrapper.class);
				DeviceInfo deviceInfo = deviceInfoWrapper.getDeviceInfo();

				if (deviceInfo != null) {
					stats.put(DeviceInfoMetric.SERIAL_NUMBER.getName(), getDefaultValueForNullData(deviceInfo.getSerialNumber(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.BOOT_VERSION.getName(), getDefaultValueForNullData(deviceInfo.getBootVersion(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.CARD_TYPE.getName(), getDefaultValueForNullData(deviceInfo.getCardType(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.CPLD_VERSION.getName(), getDefaultValueForNullData(deviceInfo.getCpldVersion(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.FIRMWARE_DATE.getName(), getDefaultValueForNullData(deviceInfo.getFirmwareDate(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.FIRMWARE_OPTIONS.getName(), getDefaultValueForNullData(deviceInfo.getFirmwareOptions(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.FIRMWARE_VERSION.getName(), getDefaultValueForNullData(deviceInfo.getFirmwareVersion(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.HARDWARE_COMPATIBILITY.getName(), getDefaultValueForNullData(deviceInfo.getHardwareCompatibility(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.HARDWARE_VERSION.getName(), getDefaultValueForNullData(deviceInfo.getHardwareVersion(), DecoderConstant.NONE));
					stats.put(DeviceInfoMetric.PART_NUMBER.getName(), getDefaultValueForNullData(deviceInfo.getPartNumber(), DecoderConstant.NONE));
				} else {
					updateFailedMonitor(MonitoringMetricGroup.DEVICE_INFO.getName(), DecoderConstant.GETTING_DEVICE_INFO_ERR);
				}
			}
		} catch (Exception e) {
			logger.error("Error while retrieve device info: ", e);
			updateFailedMonitor(MonitoringMetricGroup.DEVICE_INFO.getName(), DecoderConstant.GETTING_DEVICE_INFO_ERR);
		}
	}

	/**
	 * This method is used to retrieve device temperature by send command "temperature get"
	 *
	 * @param stats list statistics property
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveDeviceTemperature(Map<String, String> stats) {
		try {
			String request = CommandOperation.OPERATION_TEMPERATURE.getName();
			String response = send(request);

			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				DeviceInfo deviceInfo = objectMapper.convertValue(responseMap, DeviceInfo.class);

				if (deviceInfo.getTemperatureStatus() != null) {
					stats.put(DeviceInfoMetric.TEMPERATURE.getName(), getDefaultValueForNullData(NormalizeData.getDataNumberValue(deviceInfo.getTemperatureStatus().getTemperature()), DecoderConstant.NONE));
				} else {
					updateFailedMonitor(MonitoringMetricGroup.TEMPERATURE.getName(), DecoderConstant.GETTING_DEVICE_TEMPERATURE_ERR);
				}
			} else {
				updateFailedMonitor(MonitoringMetricGroup.TEMPERATURE.getName(), DecoderConstant.GETTING_DEVICE_TEMPERATURE_ERR);
			}
		} catch (Exception e) {
			logger.error("Error while retrieve device temperature: ", e);
			updateFailedMonitor(MonitoringMetricGroup.TEMPERATURE.getName(), DecoderConstant.GETTING_DEVICE_TEMPERATURE_ERR);
		}
	}

	/**
	 * This method is used to retrieve stillImage by send command "still list"
	 *
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveDeviceStillImage() {
		try {
			String request = CommandOperation.OPERATION_STILL_IMAGE.getName();
			String response = send(request);

			if (response != null) {
				String[] splitResponses = response.split(DecoderConstant.COLON + "\r\n");
				int stillImageDataIndex = 1;
				if (stillImageDataIndex <= splitResponses.length) {
					customStillImages = new ArrayList<>();
					String[] deviceStillImage = splitResponses[stillImageDataIndex].split("\r\n");
					for (int i = 0; i < deviceStillImage.length; i++) {
						if (StringUtils.isNullOrEmpty(deviceStillImage[i])) {
							break;
						}
						customStillImages.add(deviceStillImage[i].trim());
					}
				} else {
					updateFailedMonitor(MonitoringMetricGroup.STILL_IMAGE.getName(), DecoderConstant.GETTING_DEVICE_STILL_IMAGE_ERR);
				}
			} else {
				updateFailedMonitor(MonitoringMetricGroup.STILL_IMAGE.getName(), DecoderConstant.GETTING_DEVICE_STILL_IMAGE_ERR);
			}
		} catch (Exception e) {
			logger.error("Error while retrieve device info: ", e);
			updateFailedMonitor(MonitoringMetricGroup.STILL_IMAGE.getName(), DecoderConstant.GETTING_DEVICE_STILL_IMAGE_ERR);
		}
	}

	/**
	 * Update failedMonitor with getting device info error message
	 *
	 * @param monitoringGroup is monitoring metric group
	 * @param errorMessage is error message
	 */
	private void updateFailedMonitor(String monitoringGroup, String errorMessage) {
		failedMonitor.put(monitoringGroup, errorMessage);
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region retrieve decoder statistic
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used to retrieve decoder statistic by send command "viddec {DecoderID} get all"
	 *
	 * @param stats list statistics property
	 * @param decoderID ID of the decoder
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveDecoderStats(Map<String, String> stats, Integer decoderID) {
		try {
			String request = CommandOperation.OPERATION_VIDDEC.getName()
					.concat(DecoderConstant.SPACE)
					.concat(decoderID.toString())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.GET.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.ALL.getName());

			String response = send(request);
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				DecoderStatsWrapper decoderInfoWrapper = objectMapper.convertValue(responseMap, DecoderStatsWrapper.class);

				if (decoderInfoWrapper != null) {
					populateDecoderStats(stats, decoderInfoWrapper, decoderID);
					updateLocalDecoderConfigInfo(decoderInfoWrapper, decoderID);
				} else {
					updateDecoderStatisticsFailedMonitor(decoderID);
				}
			} else {
				updateDecoderStatisticsFailedMonitor(decoderID);
			}
		} catch (Exception e) {
			logger.error("Error while retrieve decoder statistics: ", e);
			updateDecoderStatisticsFailedMonitor(decoderID);
		}
	}

	/**
	 * Update failedMonitor with Getting decoder stats error message
	 *
	 * @param decoderID ID of the decoder
	 */
	private void updateDecoderStatisticsFailedMonitor(Integer decoderID) {
		failedMonitor.put(MonitoringMetricGroup.DECODER_STATISTICS.getName() + decoderID, DecoderConstant.GETTING_DECODER_STATS_ERR + decoderID);
	}

	/**
	 * This method is used update decoder statistic from DTO
	 *
	 * @param stats list statistics property
	 * @param decoderInfoWrapper pair of decoder config and stats
	 * @param decoderID ID of decoder
	 */
	private void populateDecoderStats(Map<String, String> stats, DecoderStatsWrapper decoderInfoWrapper, Integer decoderID) {
		String decoderStatisticGroup = MonitoringMetricGroup.DECODER_STATISTICS.getName() + decoderID + DecoderConstant.HASH;

		if (decoderInfoWrapper.getDecoder() != null) {
			for (DecoderMonitoringMetric item : DecoderMonitoringMetric.values()) {
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getDecoder().getValueByDecoderMonitoringMetric(item), DecoderConstant.NONE));
			}
		}
		if (decoderInfoWrapper.getDecoderStats() != null) {
			for (DecoderStatsMonitoringMetric item : DecoderStatsMonitoringMetric.values()) {
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getDecoderStats().getValueByDecoderMonitoringMetric(item), DecoderConstant.NONE));
			}
		}
		if (decoderInfoWrapper.getTimecode() != null) {
			for (DecoderTimeCodeMonitoringMetric item : DecoderTimeCodeMonitoringMetric.values()) {
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getTimecode().getValueByDecoderMonitoringMetric(item), DecoderConstant.NONE));
			}
		}
		if (decoderInfoWrapper.getAudio() != null) {
			for (DecoderAudioMonitoringMetric item : DecoderAudioMonitoringMetric.values()) {
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getAudio().getValueByDecoderMonitoringMetric(item), DecoderConstant.NONE));
			}
		}
		if (decoderInfoWrapper.getVideo() != null) {
			for (DecoderVideoMonitoringMetric item : DecoderVideoMonitoringMetric.values()) {
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getVideo().getValueByDecoderMonitoringMetric(item), DecoderConstant.NONE));
			}
		}
	}

	/**
	 * This method is used update localDecoderConfigInfo statistic from DTO
	 *
	 * @param decoderInfoWrapper pair of decoder config and stats
	 * @param decoderID ID of decoder
	 */
	private void updateLocalDecoderConfigInfo(DecoderStatsWrapper decoderInfoWrapper, Integer decoderID) {
		DecoderConfig decoderConfig = decoderInfoWrapper.getDecoderConfigInfo();
		decoderConfig.setDecoderID(decoderID.toString());

		Optional<DecoderConfig> realtimeDecoderConfig = this.realtimeDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
		Optional<DecoderConfig> cachedDecoderConfig = this.cachedDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();

		if (cachedDecoderConfig.isPresent() && realtimeDecoderConfig.isPresent() && cachedDecoderConfig.get().equals(realtimeDecoderConfig.get()) && !realtimeDecoderConfig.get().equals(decoderConfig)) {
			this.cachedDecoderConfigs.remove(cachedDecoderConfig.get());
			this.cachedDecoderConfigs.add(new DecoderConfig(decoderConfig));
		}

		realtimeDecoderConfig.ifPresent(config -> this.realtimeDecoderConfigs.remove(config));
		this.realtimeDecoderConfigs.add(decoderConfig);
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion


	//region retrieve stream statistics
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used to retrieve streams statistics by send command "stream all get all"
	 *
	 * @param stats list statistics property
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveStreamStats(Map<String, String> stats) {

		// Retrieve Adapter Properties
		if (this.streamNameFilter != null && streamsNameFiltered == null) {
			streamsNameFiltered = convertUserInput(this.streamNameFilter);
		}
		if (this.streamStatusFilter != null && streamsStatusFiltered == null) {
			streamsStatusFiltered = convertUserInput(this.streamStatusFilter.toUpperCase());
		}
		if (this.portNumberFilter != null && portNumbersFiltered == null) {
			portNumbersFiltered = convertUserInput(this.portNumberFilter);
		}

		try {
			String request = CommandOperation.OPERATION_STREAM.getName()
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.ALL.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.GET.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.ALL.getName());

			String response = send(request);

			if (response != null) {
				String[] responsesSplit = response.split("\r\n\r");

				for (String responseSplit : responsesSplit) {
					responseSplit = responseSplit.replaceFirst(DecoderConstant.STREAM_CONVERSION_OBJECT_RESPONSE, DecoderConstant.STREAM_CONVERSION_ALT_OBJECT_RESPONSE);
					responseSplit = request.concat("\r\n").concat(responseSplit);
					Map<String, Object> responseMap = Deserializer.convertDataToObject(responseSplit, request);
					StreamStatsWrapper streamInfoWrapper = objectMapper.convertValue(responseMap, StreamStatsWrapper.class);

					// Check if converted object is not a stream
					if (streamInfoWrapper.getStream().getStreamId() == null){
						break;
					}

					StreamStats streamStats = streamInfoWrapper.getStreamStats();
					Stream stream = streamInfoWrapper.getStream();

					// Stream name filtering
					if (this.streamNameFilter != null && streamsNameFiltered != null && streamsNameFiltered.contains(stream.getStreamName())) {
						populateStreamStats(stats, streamInfoWrapper);
						updateLocalStreamConfigInfo(streamInfoWrapper, Integer.parseInt(stream.getStreamId()));
						continue;
					}

					// Stream status filtering
					if (this.streamStatusFilter != null && streamsStatusFiltered != null && !streamsStatusFiltered.contains(streamStats.getState())) {
						continue;
					}

					// Port number filtering
					if (this.portNumberFilter != null && portNumbersFiltered != null) {
						Integer port = Integer.parseInt(streamInfoWrapper.getStreamConfig().getPort());
						boolean isValidPort = validatePortRange(port);
						if (!isValidPort) {
							continue;
						}
					}
					if (this.streamStatusFilter != null) {
						populateStreamStats(stats, streamInfoWrapper);
						updateLocalStreamConfigInfo(streamInfoWrapper, Integer.parseInt(stream.getStreamId()));
					}
					if (this.portNumberFilter != null) {
						populateStreamStats(stats, streamInfoWrapper);
						updateLocalStreamConfigInfo(streamInfoWrapper, Integer.parseInt(stream.getStreamId()));
					}
					if (this.streamNameFilter == null) {
						populateStreamStats(stats, streamInfoWrapper);
						updateLocalStreamConfigInfo(streamInfoWrapper, Integer.parseInt(stream.getStreamId().trim()));
					}
				}
			} else {
				updateFailedMonitor(MonitoringMetricGroup.STREAM_STATISTICS.getName(), DecoderConstant.GETTING_STREAM_STATS_ERR);
			}
		} catch (Exception e) {
			logger.error("Error while retrieve stream statistics: ", e);
			updateFailedMonitor(MonitoringMetricGroup.STREAM_STATISTICS.getName(), DecoderConstant.GETTING_STREAM_STATS_ERR );
		}
	}

	/**
	 * This method is used update stream statistic from DTO
	 *
	 * @param stats list statistics property
	 * @param streamStatsWrapper pair of stream config and stats
	 */
	private void populateStreamStats(Map<String, String> stats, StreamStatsWrapper streamStatsWrapper) {
		StreamConfig streamConfigInfo = streamStatsWrapper.getStreamConfig();

		String tempAddress = getDefaultValueForNullData(streamConfigInfo.getAddress(), DecoderConstant.EMPTY);
		streamConfigInfo.setDestinationAddress(NormalizeData.getDataValueBySpaceIndex(tempAddress, DecoderConstant.ADDRESS_DATA_INDEX));
		streamConfigInfo.setSourceAddress(NormalizeData.getDataValueBySpaceIndex(tempAddress, DecoderConstant.SOURCE_ADDRESS_DATA_INDEX));
		String streamName = streamStatsWrapper.getStream().getStreamName();

		if (StringUtils.isNullOrEmpty(streamName) || streamName.equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
			streamName = streamConfigInfo.getDefaultStreamName();
		}
		String streamStatisticGroup = MonitoringMetricGroup.STREAM_STATISTICS.getName() + streamName + DecoderConstant.HASH;

		if (streamStatsWrapper.getStream() != null) {
			for (StreamMonitoringMetric item : StreamMonitoringMetric.values()) {
				stats.put(streamStatisticGroup + item.getName(), getDefaultValueForNullData(streamStatsWrapper.getValueByStreamMonitoringMetric(item), DecoderConstant.NONE));
			}
		}
		if (streamStatsWrapper.getStreamStats() != null) {
			for (StreamStatsMonitoringMetric item : StreamStatsMonitoringMetric.values()) {
				stats.put(streamStatisticGroup + item.getName(), getDefaultValueForNullData(streamStatsWrapper.getStreamStats().getValueByStreamMonitoringMetric(item), DecoderConstant.NONE));
			}
		}
		if (streamStatsWrapper.getSrt() != null) {
			for (SRTMonitoringMetric item : SRTMonitoringMetric.values()) {
				stats.put(streamStatisticGroup + item.getName(), getDefaultValueForNullData(streamStatsWrapper.getSrt().getValueByStreamMonitoringMetric(item), DecoderConstant.NONE));
			}
		}
	}

	/**
	 * This method is used update localDecoderConfigInfo statistic from DTO
	 *
	 * @param streamInfoWrapper pair of decoder config and stats
	 * @param streamID ID of decoder
	 */
	private void updateLocalStreamConfigInfo(StreamStatsWrapper streamInfoWrapper, Integer streamID) {
		StreamConfig streamConfigInfo = streamInfoWrapper.getStreamConfig();
		Stream stream = streamInfoWrapper.getStream();
		StreamConversion streamConversion = streamInfoWrapper.getStreamConversion();
		if (streamConversion != null){
			streamConfigInfo.setStreamConversion(streamConversion);
			streamConfigInfo.setStreamFlipping(streamConversion.getStreamFlipping());
		}

		// map value to DTO
		streamConfigInfo.setId(stream.getStreamId());
		streamConfigInfo.setName(stream.getStreamName());
		String port = getDefaultValueForNullData(streamConfigInfo.getPort(), DecoderConstant.EMPTY);
		if(port.isEmpty()) {
			streamConfigInfo.setPort(getDefaultValueForNullData(streamConfigInfo.getDestinationPort(), DecoderConstant.EMPTY));
		}
		String tempAddress = getDefaultValueForNullData(streamConfigInfo.getAddress(), DecoderConstant.EMPTY);
		streamConfigInfo.setDestinationAddress(NormalizeData.getDataValueBySpaceIndex(tempAddress, DecoderConstant.ADDRESS_DATA_INDEX));
		streamConfigInfo.setSourceAddress(NormalizeData.getDataValueBySpaceIndex(tempAddress, DecoderConstant.SOURCE_ADDRESS_DATA_INDEX));

		Optional<StreamConfig> realtimeStreamConfig = this.realtimeStreamConfigs.stream().filter(st -> streamID.toString().equals(st.getId())).findFirst();
		Optional<StreamConfig> cachedStreamConfig = this.cachedStreamConfigs.stream().filter(st -> streamID.toString().equals(st.getId())).findFirst();

		if (cachedStreamConfig.isPresent() && realtimeStreamConfig.isPresent() && cachedStreamConfig.get().equals(realtimeStreamConfig.get()) && !realtimeStreamConfig.get().equals(streamConfigInfo)) {
			this.cachedStreamConfigs.remove(cachedStreamConfig.get());
			this.cachedStreamConfigs.add(new StreamConfig(streamConfigInfo));
		}
		realtimeStreamConfig.ifPresent(config -> this.realtimeStreamConfigs.remove(config));
		this.realtimeStreamConfigs.add(streamConfigInfo);
		filteredStreamIDSet.add(streamID);

	}

	/**
	 * This method is used to handle input from adapter properties and convert it to Set of String for control
	 *
	 * @return Set<String> is the Set of String of filter element
	 */
	public Set<String> convertUserInput(String input) {
		if (!StringUtils.isNullOrEmpty(input)) {
			String[] listAdapterPropertyElement = input.split(DecoderConstant.COMMA);

			// Remove start and end spaces of each adapterProperty
			Set<String> setAdapterPropertiesElement = new HashSet<>();
			for (String adapterPropertyElement : listAdapterPropertyElement) {
				setAdapterPropertiesElement.add(adapterPropertyElement.trim());
			}
			return setAdapterPropertiesElement;
		}
		return Collections.emptySet();
	}

	/**
	 * This method is used to validate port, port range input from user
	 * When the input is an Integer value this method will check whether it is match with port from stream stats or not
	 * When the input is a range value this method will convert the range to min/ max port value and check whether it covers port from stream stats or not
	 *
	 * @return boolean the port and port range filtering result
	 */
	public boolean validatePortRange(Integer portNumber) {
		try {
			for (String portNumberFromAdapterProperties : portNumbersFiltered) {

				// Port range filtering
				if (portNumberFromAdapterProperties.contains(DecoderConstant.DASH)) {
					String[] rangeList = portNumberFromAdapterProperties.split(DecoderConstant.DASH);
					int minPortNumber = Integer.parseInt(rangeList[0]);
					int maxPortNumber = Integer.parseInt(rangeList[1]);

					// Swapping if min value > max value
					if (minPortNumber > maxPortNumber) {
						int temp = minPortNumber;
						minPortNumber = maxPortNumber;
						maxPortNumber = temp;
					}
					if (portNumber >= minPortNumber && portNumber <= maxPortNumber) {
						return true;
					}

					// Port filtering
				} else if (portNumberFromAdapterProperties.equals(portNumber.toString())) {
					return true;
				}
			}
		} catch (NumberFormatException f) {
			throw new ResourceNotReachableException(DecoderConstant.PORT_NUMBER_ERROR, f);
		}
		return false;
	}
	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region audio config
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used to retrieve audio by send command "auddec get all"
	 *
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveAudioConfig() {
		try {
			String request = CommandOperation.OPERATION_AUDDEC.getName()
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.GET.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.ALL.getName());

			String response = send(request);
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				AudioConfigWrapper audioConfigWrapper = objectMapper.convertValue(responseMap, AudioConfigWrapper.class);

				if (audioConfigWrapper == null) {
					updateFailedMonitor(CommandOperation.OPERATION_AUDDEC.getName(), DecoderConstant.GETTING_AUDIO_CONFIG_ERR);
				}
			}
		} catch (Exception e) {
			logger.error("Error while retrieve decoder statistics: ", e);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate decoder SDI control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for populate all Decoder control properties:
	 * <li>Primary Stream</li>
	 * <li>Secondary Stream</li>
	 * <li>Still Image</li>
	 * <li>Still Image Delay</li>
	 * <li>Enable Buffering</li>
	 * <li>Buffering Mode</li>
	 * <li>Buffering Delay</li>
	 * <li>Output Frame Rate</li>
	 * <li>Output Resolution/li>
	 * <li>Select Still Image/li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param decoderID ID of decoder
	 */
	private void populateDecoderControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, Integer decoderID) {
		// Get controllable property current value
		Optional<DecoderConfig> cachedDecoderConfigOptional = this.cachedDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
		Optional<DecoderConfig> realtimeDecoderConfigOptional = this.realtimeDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();

		if (cachedDecoderConfigOptional.isPresent() && realtimeDecoderConfigOptional.isPresent()) {
			DecoderConfig cachedDecoderConfig = cachedDecoderConfigOptional.get();
			String primaryStreamID = getDefaultValueForNullData(cachedDecoderConfig.getPrimaryStream(), DecoderConstant.DEFAULT_STREAM_NAME);
			String primaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
			if (this.cachedStreamConfigs != null) {
				for (StreamConfig cachedStreamInfo : cachedStreamConfigs) {
					if (primaryStreamID.equals(cachedStreamInfo.getId())) {
						primaryStreamName = cachedStreamInfo.getName();
						if (primaryStreamName.equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
							primaryStreamName = cachedStreamInfo.getDefaultStreamName();
						}
						break;
					}
				}
			}

			String secondaryStreamID = getDefaultValueForNullData(cachedDecoderConfig.getSecondaryStream(), DecoderConstant.DEFAULT_STREAM_NAME);
			String secondaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
			if (this.cachedStreamConfigs != null) {
				for (StreamConfig cachedStreamInfo : cachedStreamConfigs) {
					if (secondaryStreamID.equals(cachedStreamInfo.getId())) {
						secondaryStreamName = cachedStreamInfo.getName();
						if (secondaryStreamName.equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
							secondaryStreamName = cachedStreamInfo.getDefaultStreamName();
						}
						break;
					}
				}
			}

			String stillImageDelay = getDefaultValueForNullData(NormalizeData.getDataNumberValue(cachedDecoderConfig.getStillImageDelay()), DecoderConstant.DEFAULT_STILL_IMAGE_DELAY.toString());
			SyncMode enableBuffering = SyncMode.getByName(getDefaultValueForNullData(cachedDecoderConfig.getEnableBuffering(), DecoderConstant.EMPTY));
			OutputResolution outputResolution = OutputResolution.getByAPIStatsName(getDefaultValueForNullData(cachedDecoderConfig.getOutputResolution(), DecoderConstant.EMPTY));
			OutputFrameRate outputFrameRate = OutputFrameRate.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getOutputFrameRate(), DecoderConstant.EMPTY));
			State decoderSDIState = State.getByName(getDefaultValueForNullData(cachedDecoderConfig.getState(), DecoderConstant.EMPTY));

			// Get list values of controllable property (dropdown list)
			List<String> resolutionModes = DropdownList.getListOfEnumNames(OutputResolution.class);
			List<String> frameRateModes = new ArrayList<>();
			List<String> streamNames = new ArrayList<>();

			switch (outputResolution.getResolutionCategory()) {
				case DecoderConstant.AUTOMATIC_RESOLUTION:
					frameRateModes = DropdownList.getListOfEnumNames(OutputFrameRate.class);
					break;
				case DecoderConstant.TV_RESOLUTION:
					switch (outputResolution) {
						case TV_RESOLUTIONS_1080P:
							frameRateModes = DropdownList.getListOfEnumNames(OutputFrameRate.class);
							frameRateModes.remove(OutputFrameRate.OUTPUT_FRAME_RATE_75.getUiName());
							break;
						case TV_RESOLUTIONS_1080I:
							frameRateModes.add(OutputFrameRate.AUTO.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_30.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_29.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_25.getUiName());
							break;
						case TV_RESOLUTIONS_720P:
							frameRateModes.add(OutputFrameRate.AUTO.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_60.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_59.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_50.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_30.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_29.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_25.getUiName());
							break;
						case TV_RESOLUTIONS_576P:
							frameRateModes.add(OutputFrameRate.AUTO.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_50.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_25.getUiName());
							break;
						case TV_RESOLUTIONS_576I:
							frameRateModes.add(OutputFrameRate.AUTO.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_25.getUiName());
							break;
						case TV_RESOLUTIONS_480P:
							frameRateModes.add(OutputFrameRate.AUTO.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_60.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_59.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_30.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_29.getUiName());
							break;
						case TV_RESOLUTIONS_480I:
							frameRateModes.add(OutputFrameRate.AUTO.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_30.getUiName());
							frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_29.getUiName());
							break;
						default:
							if (logger.isWarnEnabled()) {
								logger.warn(String.format("Output resolution %s is not supported.", outputResolution.getUiName()));
							}
							break;
					}
					break;
				case DecoderConstant.COMPUTER_RESOLUTION:
					frameRateModes.add(OutputFrameRate.AUTO.getUiName());
					frameRateModes.add(OutputFrameRate.OUTPUT_FRAME_RATE_60.getUiName());
					break;
				case DecoderConstant.NATIVE_RESOLUTION:
					break;
				default:
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("Output resolution %s is not supported.", outputResolution.getUiName()));
					}
					break;
			}

			streamNames.add(DecoderConstant.DEFAULT_STREAM_NAME);
			if (this.cachedStreamConfigs != null) {
				for (StreamConfig streamConfig : cachedStreamConfigs) {
					if (!streamConfig.getName().equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
						streamNames.add(streamConfig.getName());
					} else {
						streamNames.add(streamConfig.getDefaultStreamName());
					}
				}
			}

			String decoderControllingGroup = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH;

			// Populate control
			addAdvanceControlProperties(advancedControllableProperties,
					createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.PRIMARY_STREAM.getName(), streamNames, primaryStreamName));
			addAdvanceControlProperties(advancedControllableProperties,
					createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.SECONDARY_STREAM.getName(), streamNames, secondaryStreamName));
			addAdvanceControlProperties(advancedControllableProperties,
					createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.STILL_IMAGE_DELAY.getName(), stillImageDelay));
			addAdvanceControlProperties(advancedControllableProperties,
					createSwitch(stats, decoderControllingGroup + DecoderControllingMetric.SYNC_MODE.getName(), enableBuffering.getCode(), DecoderConstant.OFF, DecoderConstant.ON));
			addAdvanceControlProperties(advancedControllableProperties,
					createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.OUTPUT_RESOLUTION.getName(), resolutionModes, outputResolution.getUiName()));
			addAdvanceControlProperties(advancedControllableProperties,
					createSwitch(stats, decoderControllingGroup + DecoderControllingMetric.STATE.getName(), decoderSDIState.getCode(), DecoderConstant.OFF, DecoderConstant.ON));

			if (!frameRateModes.isEmpty()) {
				// get output frame rate from output frame rate list
				String outputFrameRateTemp = frameRateModes.stream().filter(ofr -> ofr.equals(outputFrameRate.getApiName())).findFirst().orElse(OutputFrameRate.AUTO.getUiName());

				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.OUTPUT_FRAME_RATE.getName(), frameRateModes, outputFrameRateTemp));

				this.cachedDecoderConfigs.remove(cachedDecoderConfig);
				cachedDecoderConfig.setOutputFrameRate(OutputFrameRate.getByUIName(outputFrameRateTemp).getApiName());
				this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));
			} else {
				stats.remove(decoderControllingGroup + DecoderControllingMetric.OUTPUT_FRAME_RATE.getName());
			}

			populateDecoderControlStillImage(stats, advancedControllableProperties, decoderID);
			populateDecoderControlBufferingMode(stats, advancedControllableProperties, cachedDecoderConfig, decoderID);
			populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
		}
	}

	/**
	 * This method is used for populate all buffering mode of decoder control:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedDecoderConfig set of decoder configuration
	 * @param decoderID ID of decoder SDI
	 */
	private void populateDecoderControlBufferingMode(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, DecoderConfig cachedDecoderConfig, Integer decoderID) {
		// Get controllable property current value
		BufferingMode bufferingMode = BufferingMode.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getBufferingMode(), DecoderConstant.EMPTY));
		SyncMode enableBuffering = SyncMode.getByName(getDefaultValueForNullData(cachedDecoderConfig.getEnableBuffering(), DecoderConstant.EMPTY));
		String bufferingDelay;
		Integer bufferingDelayIntValue;

		// Get list values of controllable property (dropdown)
		List<String> bufferingModeList = DropdownList.getListOfEnumNames(BufferingMode.class);
		String decoderControllingGroup = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH;

		// remove unused keys
		stats.remove(decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName());
		stats.remove(decoderControllingGroup + DecoderControllingMetric.BUFFERING_DELAY.getName());
		stats.remove(decoderControllingGroup + DecoderControllingMetric.MULTI_SYNC_BUFFERING_DELAY.getName());

		// populate control
		if (enableBuffering.isEnable()) {
			switch (bufferingMode) {
				case AUTO:
				case ADAPTIVE_LOW_LATENCY:
					addAdvanceControlProperties(advancedControllableProperties,
							createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName(), bufferingModeList, bufferingMode.getUiName()));
					break;
				case FIXED:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					bufferingDelay = getDefaultValueForNullData(NormalizeData.getDataNumberValue(cachedDecoderConfig.getBufferingDelay()), DecoderConstant.MIN_BUFFERING_DELAY.toString());
					bufferingDelayIntValue = Integer.parseInt(bufferingDelay);
					if (bufferingDelayIntValue < DecoderConstant.MIN_BUFFERING_DELAY) {
						bufferingDelayIntValue = DecoderConstant.MIN_BUFFERING_DELAY;
					}
					if (bufferingDelayIntValue > DecoderConstant.MAX_BUFFERING_DELAY) {
						bufferingDelayIntValue = DecoderConstant.MAX_BUFFERING_DELAY;
					}

					cachedDecoderConfig.setBufferingDelay(bufferingDelayIntValue.toString());
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					addAdvanceControlProperties(advancedControllableProperties,
							createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName(), bufferingModeList, bufferingMode.getUiName()));
					addAdvanceControlProperties(advancedControllableProperties,
							createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_DELAY.getName(), bufferingDelayIntValue.toString()));
					break;
				case MULTI_SYNC:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					bufferingDelay = getDefaultValueForNullData(NormalizeData.getDataNumberValue(cachedDecoderConfig.getBufferingDelay()), DecoderConstant.DEFAULT_MULTI_SYNC_BUFFERING_DELAY.toString());
					bufferingDelayIntValue = Integer.parseInt(bufferingDelay);
					if (bufferingDelayIntValue < DecoderConstant.MIN_MULTI_SYNC_BUFFERING_DELAY) {
						bufferingDelayIntValue = DecoderConstant.MIN_MULTI_SYNC_BUFFERING_DELAY;
					}
					if (bufferingDelayIntValue > DecoderConstant.MAX_MULTI_SYNC_BUFFERING_DELAY) {
						bufferingDelayIntValue = DecoderConstant.MAX_MULTI_SYNC_BUFFERING_DELAY;
					}

					cachedDecoderConfig.setBufferingDelay(bufferingDelayIntValue.toString());
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					addAdvanceControlProperties(advancedControllableProperties,
							createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName(), bufferingModeList, bufferingMode.getUiName()));
					addAdvanceControlProperties(advancedControllableProperties,
							createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_DELAY.getName(), bufferingDelayIntValue.toString()));
					break;
				default:
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("Buffering mode %s is not supported.", bufferingMode.getUiName()));
					}
					break;
			}
		}
	}

	/**
	 * This method is used for populate all still image mode of decoder control:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param decoderID ID of decoder SDI
	 */
	private void populateDecoderControlStillImage(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, Integer decoderID) {
		Optional<DecoderConfig> cachedDecoderConfigOptional = this.cachedDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
		if (cachedDecoderConfigOptional.isPresent()) {
			DecoderConfig cachedDecoderConfig = cachedDecoderConfigOptional.get();

			// Get controllable property current value
			String stillImage;
			Optional<String> customStillImageOptional = customStillImages.stream().filter(st -> st.equals(cachedDecoderConfig.getStillFile())).findFirst();
			StillImage stillImageEnum = StillImage.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getStillImage(), DecoderConstant.EMPTY));

			// Get list values of controllable property (dropdown)
			List<String> defaultStillImages = DropdownList.getListOfEnumNames(StillImage.class, StillImage.CUSTOM);

			String decoderControllingGroup = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH;

			// remove unused keys
			stats.remove(decoderControllingGroup + DecoderControllingMetric.SELECT_STILL_IMAGE.getName());

			// populate control
			if (stillImageEnum.equals(StillImage.SELECT_IMAGE) && !customStillImages.isEmpty()) {
				if (customStillImageOptional.isPresent()) {
					stillImage = customStillImageOptional.get();
				} else {
					stillImage = customStillImages.get(0);
				}

				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.SELECT_STILL_IMAGE.getName(), customStillImages, stillImage));

				this.cachedDecoderConfigs.remove(cachedDecoderConfig);
				cachedDecoderConfig.setStillFile(stillImage);
				this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));
				defaultStillImages.remove(StillImage.SELECT_IMAGE.getUiName());
				defaultStillImages.add(stillImage);
				defaultStillImages.add(StillImage.SELECT_IMAGE.getUiName());
				stillImage = StillImage.SELECT_IMAGE.getUiName();
			} else {
				if (customStillImageOptional.isPresent() && stillImageEnum.equals(StillImage.CUSTOM)) {
					stillImage = customStillImageOptional.get();

					this.cachedDecoderConfigs.remove(cachedDecoderConfig);
					cachedDecoderConfig.setStillFile(stillImage);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));
					defaultStillImages.remove(StillImage.SELECT_IMAGE.getUiName());
					defaultStillImages.add(stillImage);
					defaultStillImages.add(StillImage.SELECT_IMAGE.getUiName());
				} else {
					stillImage = stillImageEnum.getUiName();
				}
			}
			addAdvanceControlProperties(advancedControllableProperties,
					createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.STILL_IMAGE.getName(), defaultStillImages, stillImage));
		}
	}

	/**
	 * This method is used for populate apply change button and cancel button of decoder control:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param decoderID ID of decoder
	 */
	private void populateApplyChangeAndCancelButtonForDecoder(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, Integer decoderID) {
		Optional<DecoderConfig> cachedDecoderConfigOptional = this.cachedDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
		Optional<DecoderConfig> realtimeDecoderConfigOptional = this.realtimeDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();

		if (cachedDecoderConfigOptional.isPresent() && realtimeDecoderConfigOptional.isPresent()) {
			DecoderConfig cachedDecoderConfig = cachedDecoderConfigOptional.get();
			DecoderConfig realtimeDecoderConfig = realtimeDecoderConfigOptional.get();

			String applyChange = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.APPLY_CHANGE.getName();
			String cancel = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.CANCEL.getName();

			if (!cachedDecoderConfig.deepEquals(realtimeDecoderConfig)) {
				stats.put(ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.EDITED.getName(), DecoderConstant.TRUE_VALUE);
				stats.put(applyChange, DecoderConstant.EMPTY);
				stats.put(cancel, DecoderConstant.EMPTY);
				addAdvanceControlProperties(advancedControllableProperties, createButton(applyChange, DecoderConstant.APPLY, DecoderConstant.APPLYING));
				addAdvanceControlProperties(advancedControllableProperties, createButton(cancel, DecoderConstant.CANCEL, DecoderConstant.CANCELLING));
			} else {
				stats.remove(applyChange);
				stats.remove(cancel);
				stats.put(ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.EDITED.getName(), DecoderConstant.FALSE_VALUE);

				for (AdvancedControllableProperty controllableProperty : advancedControllableProperties) {
					if (controllableProperty.getName().equals(applyChange)) {
						advancedControllableProperties.remove(controllableProperty);
						break;
					}
				}
				for (AdvancedControllableProperty controllableProperty : advancedControllableProperties) {
					if (controllableProperty.getName().equals(cancel)) {
						advancedControllableProperties.remove(controllableProperty);
						break;
					}
				}
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform decoder SDI control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for calling control all Decoder control properties in case:
	 * <li>Primary Stream</li>
	 * <li>Secondary Stream</li>
	 * <li>Still Image</li>
	 * <li>Still Image Delay</li>
	 * <li>Enable Buffering</li>
	 * <li>Buffering Mode</li>
	 * <li>Buffering Delay</li>
	 * <li>Output Frame Rate</li>
	 * <li>Output Resolution/li>
	 * <li>Select Still Image/li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param decoderID ID of decoder
	 * @param controllableProperty name of controllable property
	 * @param value value of controllable property
	 *
	 * @throws ResourceNotReachableException when start/ stop/ update decoder SDI is failed
	 */
	private void decoderControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, Integer decoderID, String controllableProperty, String value) {
		DecoderControllingMetric decoderControllingMetric = DecoderControllingMetric.getByName(controllableProperty);
		Optional<DecoderConfig> cachedDecoderConfigOptional = this.cachedDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
		if (cachedDecoderConfigOptional.isPresent()) {
			DecoderConfig cachedDecoderConfig = cachedDecoderConfigOptional.get();

			List<String> streamNames = new ArrayList<>();
			streamNames.add(DecoderConstant.DEFAULT_STREAM_NAME);
			if (this.cachedStreamConfigs != null) {
				for (StreamConfig streamConfig : cachedStreamConfigs) {
					if (!streamConfig.getName().equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
						streamNames.add(streamConfig.getName());
					} else {
						streamNames.add(streamConfig.getDefaultStreamName());
					}
				}
			}

			String decoderControllingGroup = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH;

			switch (decoderControllingMetric) {
				case PRIMARY_STREAM:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					String primaryStreamID = DecoderConstant.DEFAULT_STREAM_NAME;
					String primaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
					for (StreamConfig cachedStreamConfig : cachedStreamConfigs) {
						if (!value.equals(DecoderConstant.DEFAULT_STREAM_NAME) && (value.equals(cachedStreamConfig.getName()) || value.equals(cachedStreamConfig.getDefaultStreamName()))) {
							primaryStreamID = cachedStreamConfig.getId();
							primaryStreamName = value;
							break;
						}
					}

					cachedDecoderConfig.setPrimaryStream(primaryStreamID);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					addAdvanceControlProperties(advancedControllableProperties,
							createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.PRIMARY_STREAM.getName(), streamNames, primaryStreamName));
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case SECONDARY_STREAM:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					String secondaryStreamID = DecoderConstant.DEFAULT_STREAM_NAME;
					String secondaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
					for (StreamConfig cachedStreamConfig : cachedStreamConfigs) {
						if (!value.equals(DecoderConstant.DEFAULT_STREAM_NAME) && (value.equals(cachedStreamConfig.getName()) || value.equals(cachedStreamConfig.getDefaultStreamName()))) {
							secondaryStreamID = cachedStreamConfig.getId();
							secondaryStreamName = value;
							break;
						}
					}

					cachedDecoderConfig.setSecondaryStream(secondaryStreamID);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					addAdvanceControlProperties(advancedControllableProperties,
							createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.SECONDARY_STREAM.getName(), streamNames, secondaryStreamName));
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case STILL_IMAGE:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);
					String stillImage = StillImage.getByUIName(value).getApiName();

					cachedDecoderConfig.setStillImage(stillImage);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case SELECT_STILL_IMAGE:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					Optional<String> customStillImageOptional = customStillImages.stream().filter(st -> st.equals(value)).findFirst();

					customStillImageOptional.ifPresent(cachedDecoderConfig::setStillFile);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case STILL_IMAGE_DELAY:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					Integer stillImageDelay = DecoderConstant.DEFAULT_STILL_IMAGE_DELAY;
					try {
						stillImageDelay = Integer.parseInt(value);
						if (stillImageDelay < DecoderConstant.MIN_STILL_IMAGE_DELAY) {
							stillImageDelay = DecoderConstant.MIN_STILL_IMAGE_DELAY;
						}
						if (stillImageDelay > DecoderConstant.MAX_STILL_IMAGE_DELAY) {
							stillImageDelay = DecoderConstant.MAX_STILL_IMAGE_DELAY;
						}
					} catch (Exception e) {
						if (logger.isWarnEnabled()) {
							logger.warn("Invalid still image delay value", e);
						}
					}

					cachedDecoderConfig.setStillImageDelay(stillImageDelay.toString());
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					addAdvanceControlProperties(advancedControllableProperties,
							createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.STILL_IMAGE_DELAY.getName(), stillImageDelay.toString()));
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case SYNC_MODE:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					boolean isEnableSyncMode = mapSwitchControlValue(value);
					String enableSyncMode = SyncMode.ENABLE_SYNC_MODE.getName();
					if (!isEnableSyncMode) {
						enableSyncMode = SyncMode.DISABLE_SYNC_MODE.getName();
					}

					cachedDecoderConfig.setEnableBuffering(enableSyncMode);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					addAdvanceControlProperties(advancedControllableProperties,
							createSwitch(stats, decoderControllingGroup + DecoderControllingMetric.SYNC_MODE.getName(), Integer.parseInt(value), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
					populateDecoderControlBufferingMode(stats, advancedControllableProperties, cachedDecoderConfig, decoderID);
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case BUFFERING_MODE:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					BufferingMode bufferingMode = BufferingMode.getByUiName(value);

					cachedDecoderConfig.setBufferingMode(bufferingMode.getApiName());
					cachedDecoderConfig.setBufferingDelay(DecoderConstant.EMPTY);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControlBufferingMode(stats, advancedControllableProperties, cachedDecoderConfig, decoderID);
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case BUFFERING_DELAY:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					cachedDecoderConfig.setBufferingDelay(value);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControlBufferingMode(stats, advancedControllableProperties, cachedDecoderConfig, decoderID);
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;

				case OUTPUT_FRAME_RATE:
					cachedDecoderConfigs.remove(cachedDecoderConfig);

					OutputFrameRate outputFrameRate = OutputFrameRate.getByUIName(value);

					cachedDecoderConfig.setOutputFrameRate(outputFrameRate.getApiName());
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case OUTPUT_RESOLUTION:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					OutputResolution outputResolutionControl = OutputResolution.getByUIName(value);

					cachedDecoderConfig.setOutputResolution(outputResolutionControl.getApiStatsName());
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				case STATE:
					Optional<DecoderConfig> realtimeDecoderConfigOptional = this.realtimeDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
					State state = State.getByCode(Integer.parseInt(value));
					switch (state) {
						case STOPPED:
							performActiveDecoderSDIControl(cachedDecoderConfig, decoderID, CommandOperation.STOP);

							if (realtimeDecoderConfigOptional.isPresent()) {
								this.realtimeDecoderConfigs.remove(realtimeDecoderConfigOptional.get());
								realtimeDecoderConfigOptional.get().setState(state.getName());
								this.realtimeDecoderConfigs.add(new DecoderConfig(realtimeDecoderConfigOptional.get()));
								this.cachedDecoderConfigs.remove(cachedDecoderConfig);
								this.cachedDecoderConfigs.add(new DecoderConfig(realtimeDecoderConfigOptional.get()));
							} else {
								throw new ResourceNotReachableException("failed to stop decoder SDI " + decoderID);
							}
							populateDecoderControl(stats, advancedControllableProperties, decoderID);
							break;
						case START:
							// update decoder SDI if properties of decoder SDI is changed
							performActiveDecoderSDIControl(cachedDecoderConfig, decoderID, CommandOperation.SET);

							// start decoder SDI
							performActiveDecoderSDIControl(cachedDecoderConfig, decoderID, CommandOperation.START);

							if (realtimeDecoderConfigOptional.isPresent()) {
								this.realtimeDecoderConfigs.remove(realtimeDecoderConfigOptional.get());
								this.cachedDecoderConfigs.remove(cachedDecoderConfig);
								cachedDecoderConfig.setState(state.getName());
								this.realtimeDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));
								this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));
							} else {
								throw new ResourceNotReachableException("failed to start decoder SDI " + decoderID);
							}

							populateDecoderControl(stats, advancedControllableProperties, decoderID);
							break;
						default:
							if (logger.isWarnEnabled()) {
								logger.warn(String.format("Decoder state %s is not supported.", state.getName()));
							}
							break;
					}
					break;
				case APPLY_CHANGE:
					performActiveDecoderSDIControl(cachedDecoderConfig, decoderID, CommandOperation.SET);

					realtimeDecoderConfigOptional = this.realtimeDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
					realtimeDecoderConfigOptional.ifPresent(config -> {
						this.realtimeDecoderConfigs.remove(config);
						this.realtimeDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));
					});
					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					break;
				case CANCEL:
					realtimeDecoderConfigOptional = this.realtimeDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();

					realtimeDecoderConfigOptional.ifPresent(config -> {
						this.cachedDecoderConfigs.remove(cachedDecoderConfig);
						this.cachedDecoderConfigs.add(new DecoderConfig(config));
					});
					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					break;
				default:
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("Operation %s with value %s is not supported.", controllableProperty, value));
					}
					throw new IllegalStateException(String.format("Operation %s with value %s is not supported.", controllableProperty, value));
			}
		}
	}

	/**
	 * This method is used for populate local extended stats when emergency delivery:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 */
	private void populateLocalExtendedStats(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		this.localExtendedStatistics.setStatistics(stats);
		this.localExtendedStatistics.setControllableProperties(advancedControllableProperties);
		isEmergencyDelivery = true;
	}

	/**
	 * This method is used to perform decoder SDI control: start/ stop/ update
	 *
	 * @param decoderConfig is set of decoder config info
	 * @param decoderID is ID of decoder
	 * @param active start/ stop/ update activation
	 * @throws ResourceNotReachableException when fail to send CLI command
	 */
	private void performActiveDecoderSDIControl(DecoderConfig decoderConfig, Integer decoderID, CommandOperation active) {
		try {
			String request;
			switch (active) {
				case SET:
					request = decoderConfig.contributeCommand(CommandOperation.OPERATION_VIDDEC.getName(), decoderID, CommandOperation.SET.getName());
					request = escapeSpecialCharacters(request);
					break;
				case START:
				case STOP:
					request = CommandOperation.OPERATION_VIDDEC.getName()
							.concat(DecoderConstant.SPACE)
							.concat(decoderID.toString())
							.concat(DecoderConstant.SPACE)
							.concat(active.getName());
					break;
				default:
					throw new IllegalStateException("Unexpected decoder SDI activation: " + active);
			}
			String response = send(request);
			if (StringUtils.isNullOrEmpty(response) || !response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
				throw new ResourceNotReachableException(DecoderConstant.SPACE + ErrorMessage.convertErrorMessage(Deserializer.getErrorMessage(response)));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.DECODER_CONTROL_ERR + DecoderConstant.SPACE + e.getMessage(), e);
		}
	}

	/**
	 * This method is used to add escape in front of special character
	 *
	 * @param input string input
	 * @return String converted
	 */
	private String escapeSpecialCharacters(String input) {
		List<String> specialCharacters = new ArrayList<>(Arrays.asList("(", ")", "&"));
		return Arrays.stream(input.split("")).map(c -> {
			if (specialCharacters.contains(c)) {
				return "\\" + c;
			} else {
				return c;
			}
		}).collect(Collectors.joining());
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate create stream control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for populate all create stream control properties:
	 * <li>Protocol: TS over UDP</li>
	 * <li>Protocol: TS over RTP</li>
	 * <li>Protocol: TS over SRT</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param streamConfig stream config info
	 */
	private void populateCreateStreamControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig streamConfig, String streamGroup) {
		// Get controllable property current value
		Encapsulation encapsulation = Encapsulation.getByApiName(getDefaultValueForNullData(streamConfig.getEncapsulation(), DecoderConstant.EMPTY));
		String streamName = streamConfig.getName();

		// Get list values of controllable property (dropdown list)
		List<String> encapsulationList = DropdownList.getListOfEnumNames(Encapsulation.class);

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties,
				createText(stats, streamGroup + StreamControllingMetric.STREAM_NAME.getName(), streamName));
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, streamGroup + StreamControllingMetric.ENCAPSULATION.getName(), encapsulationList, encapsulation.getUiName()));
		addAdvanceControlProperties(advancedControllableProperties,
				createButton(streamGroup + DecoderConstant.CREATE, DecoderConstant.CREATE, DecoderConstant.CREATING));
		stats.put(streamGroup + DecoderConstant.CREATE, DecoderConstant.EMPTY);

		switch (encapsulation) {
			case TS_OVER_UDP:
				populateCreateStreamControlCaseTSOverUDP(stats, advancedControllableProperties, streamConfig, streamGroup);
				break;
			case TS_OVER_RTP:
				populateCreateStreamControlCaseTSOverRTP(stats, advancedControllableProperties, streamConfig, streamGroup);
				break;
			case TS_OVER_SRT:
				populateCreateStreamControlCaseTSOverSRT(stats, advancedControllableProperties, streamConfig, streamGroup);
				break;
			case RTSP:
				populateCreateStreamControlCaseRTSP(stats, advancedControllableProperties, streamConfig, streamGroup);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Encapsulation mode %s is not supported.", encapsulation.getUiName()));
				}
				break;
		}
		populateCancelButtonForCreateStream(stats, advancedControllableProperties);
	}

	/**
	 * This method is used for populate all create stream control properties:
	 * <li>Protocol: TS over UDP</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateCreateStreamControlCaseTSOverUDP(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		// Get controllable property current value
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		Fec fecEnum = Fec.getByAPIStatsName(getDefaultValueForNullData(cachedStreamConfig.getFec(), DecoderConstant.EMPTY));
		Fec fecEnumCopy = Fec.DISABLE;

		// Get list values of controllable property (dropdown list)
		List<String> fecModes = DropdownList.getListOfEnumNames(Fec.class, Fec.MPEG_PRO_FEC);

		for (String fecElement : fecModes) {
			if (fecElement.equals(fecEnum.getUiName())) {
				fecEnumCopy = fecEnum;
				break;
			}
		}
		createStream.setFec(fecEnumCopy.getApiStatsName());

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.PORT.getName(), port));
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, streamGroup + StreamControllingMetric.FEC.getName(), fecModes, fecEnumCopy.getUiName()));

		populateNetWorkTypeStreamControl(stats, advancedControllableProperties, cachedStreamConfig, streamGroup);
	}

	/**
	 * This method is used for populate all create stream control properties:
	 * <li>Protocol: TS over RTP</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateCreateStreamControlCaseTSOverRTP(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		// Get controllable property current value
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		Fec fecEnum = Fec.getByAPIStatsName(getDefaultValueForNullData(cachedStreamConfig.getFec(), DecoderConstant.EMPTY));
		Fec fecEnumCopy = Fec.DISABLE;

		// Get list values of controllable property (dropdown list)
		List<String> fecModes = DropdownList.getListOfEnumNames(Fec.class, Fec.VF);

		for (String fecElement : fecModes) {
			if (fecElement.equals(fecEnum.getUiName())) {
				fecEnumCopy = fecEnum;
				break;
			}
		}
		createStream.setFec(fecEnumCopy.getApiStatsName());

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.PORT.getName(), port));
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, streamGroup + StreamControllingMetric.FEC.getName(), fecModes, fecEnumCopy.getUiName()));

		populateNetWorkTypeStreamControl(stats, advancedControllableProperties, cachedStreamConfig, streamGroup);
	}

	/**
	 * This method is used for populate create stream control properties case NetworkType:
	 * <li>Network type: Multicast</li>
	 * <li>Network type: Uni-cast</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateNetWorkTypeStreamControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig, String streamGroup) {
		// Get controllable property current value
		String multicastAddress = getDefaultValueForNullData(cachedStreamConfig.getDestinationAddress(), DecoderConstant.EMPTY);
		String sourceAddress = getDefaultValueForNullData(cachedStreamConfig.getSourceAddress(), DecoderConstant.EMPTY);

		// Get list values of controllable property (dropdown list)
		List<String> netWorkTypes = DropdownList.getListOfEnumNames(NetworkType.class);

		// Populate network type control
		if (multicastAddress.equals(DecoderConstant.ADDRESS_ANY) && sourceAddress.equals(DecoderConstant.ADDRESS_ANY)) {
			addAdvanceControlProperties(advancedControllableProperties, createDropdown(stats, streamGroup + StreamControllingMetric.NETWORK_TYPE.getName(), netWorkTypes, NetworkType.UNI_CAST.getUiName()));
		} else {
			if (sourceAddress.equals(DecoderConstant.ADDRESS_ANY)) {
				sourceAddress = DecoderConstant.EMPTY;
			}
			addAdvanceControlProperties(advancedControllableProperties,
					createDropdown(stats, streamGroup + StreamControllingMetric.NETWORK_TYPE.getName(), netWorkTypes, NetworkType.MULTI_CAST.getUiName()));
			addAdvanceControlProperties(advancedControllableProperties,
					createText(stats, streamGroup + StreamControllingMetric.MULTICAST_ADDRESS.getName(), multicastAddress));
			addAdvanceControlProperties(advancedControllableProperties,
					createText(stats, streamGroup + StreamControllingMetric.SOURCE_ADDRESS.getName(), sourceAddress));
		}
	}

	/**
	 * This method is used for populate all create stream control properties:
	 * <li>Protocol: TS over SRT</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateCreateStreamControlCaseTSOverSRT(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		SRTMode srtMode = SRTMode.getByName(getDefaultValueForNullData(cachedStreamConfig.getSrtMode(), DecoderConstant.EMPTY));
		String latency = getDefaultValueForNullData(cachedStreamConfig.getLatency(), DecoderConstant.DEFAULT_LATENCY.toString());
		List<String> srtModeList = DropdownList.getListOfEnumNames(SRTMode.class);

		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, streamGroup + StreamControllingMetric.SRT_MODE.getName(), srtModeList, srtMode.getUiName()));
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.LATENCY.getName(), latency));

		switch (srtMode) {
			case LISTENER:
				populateStreamCreateControlCaseTSOverSRTListener(stats, advancedControllableProperties, cachedStreamConfig, streamGroup);
				break;
			case CALLER:
				populateCreateStreamControlCaseTSOverSRTCaller(stats, advancedControllableProperties, cachedStreamConfig, streamGroup);
				break;
			case RENDEZVOUS:
				populateCreateStreamControlCaseTSOverSRTRendezvous(stats, advancedControllableProperties, cachedStreamConfig, streamGroup);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("SRT mode %s is not supported.", srtMode.getUiName()));
				}
				break;
		}
		populateCreateStreamControlCaseTSOverSRTStreamConversion(stats, advancedControllableProperties, cachedStreamConfig, streamGroup);
		populateCreateStreamControlCaseTSOverSRTEncrypted(stats, advancedControllableProperties, cachedStreamConfig, streamGroup);
	}

	/**
	 * This method is used for populate all create stream control properties :
	 * <li>Protocol: TS over SRT</li>
	 * <li>SRT mode: Listener</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateStreamCreateControlCaseTSOverSRTListener(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.PORT.getName(), cachedStreamConfig.getPort()));

		SwitchOnOffControl aeEncrypted = SwitchOnOffControl.getByName(getDefaultValueForNullData(cachedStreamConfig.getSrtSettings(), DecoderConstant.EMPTY));
		if (aeEncrypted.isEnable()) {
			RejectUnencrypted rejectUnencrypted = RejectUnencrypted.getByName(getDefaultValueForNullData(cachedStreamConfig.getRejectUnencrypted(), DecoderConstant.EMPTY));
			addAdvanceControlProperties(advancedControllableProperties, createSwitch(stats, streamGroup + StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName(), rejectUnencrypted.getCode(),
					DecoderConstant.DISABLE, DecoderConstant.ENABLE));
		}
	}

	/**
	 * This method is used for populate all create stream control properties :
	 * <li>Protocol: TS over SRT</li>
	 * <li>Stream conversion</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateCreateStreamControlCaseTSOverSRTStreamConversion(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		// Get controllable property current value
		StreamConversion streamConversion = cachedStreamConfig.getStreamConversion();
		SwitchOnOffControl streamFlipping = SwitchOnOffControl.getByName(getDefaultValueForNullData(cachedStreamConfig.getStreamFlipping(), DecoderConstant.EMPTY));

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties, createSwitch(stats, streamGroup + StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName(), streamFlipping.getCode(),
				DecoderConstant.DISABLE, DecoderConstant.ENABLE));

		if (streamConversion != null) {
			streamFlipping = SwitchOnOffControl.getByName(getDefaultValueForNullData(streamConversion.getStreamFlipping(), DecoderConstant.EMPTY));
			if (streamFlipping.isEnable()) {
				String srtToUdpAddress = getDefaultValueForNullData(streamConversion.getAddress(), DecoderConstant.EMPTY);
				String srtToUdpPort = getDefaultValueForNullData(streamConversion.getUdpPort(), DecoderConstant.EMPTY);
				String srtToUdpTos = getDefaultValueForNullData(streamConversion.getTos(), DecoderConstant.DEFAULT_TOS);
				String srtToUdpTtl = getDefaultValueForNullData(streamConversion.getTtl(), DecoderConstant.DEFAULT_TTL.toString());

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, streamGroup + StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName(), srtToUdpAddress));
				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, streamGroup + StreamControllingMetric.SRT_TO_UDP_PORT.getName(), srtToUdpPort));
				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, streamGroup + StreamControllingMetric.SRT_TO_UDP_TOS.getName(), srtToUdpTos));
				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, streamGroup + StreamControllingMetric.SRT_TO_UDP_TTL.getName(), srtToUdpTtl));
			}
		}
	}

	/**
	 * This method is used for populate all create stream control properties :
	 * <li>Protocol: TS over SRT</li>
	 * <li>Encrypted</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateCreateStreamControlCaseTSOverSRTEncrypted(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		SwitchOnOffControl aeEncrypted = SwitchOnOffControl.getByName(getDefaultValueForNullData(cachedStreamConfig.getSrtSettings(), DecoderConstant.EMPTY));
		String passphrase = getDefaultValueForNullData(cachedStreamConfig.getPassphrase(), DecoderConstant.EMPTY);

		addAdvanceControlProperties(advancedControllableProperties, createSwitch(stats, streamGroup + StreamControllingMetric.ENCRYPTED.getName(), aeEncrypted.getCode(),
				DecoderConstant.DISABLE, DecoderConstant.ENABLE));

		// Populate relevant control when encrypted is enabled
		if (aeEncrypted.isEnable()) {
			addAdvanceControlProperties(advancedControllableProperties,
					createText(stats, streamGroup + StreamControllingMetric.PASSPHRASE.getName(), passphrase));
		}
	}

	/**
	 * This method is used for populate all create stream control properties :
	 * <li>Protocol: TS over SRT</li>
	 * <li>SRT mode: caller</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateCreateStreamControlCaseTSOverSRTCaller(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		String address = getDefaultValueForNullData(cachedStreamConfig.getDestinationAddress(), DecoderConstant.EMPTY);
		if (address.equals(DecoderConstant.ADDRESS_ANY)) {
			address = DecoderConstant.EMPTY;
		}
		String sourcePort = getDefaultValueForNullData(cachedStreamConfig.getSourcePort(), DecoderConstant.EMPTY);
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		if (StringUtils.isNullOrEmpty(sourcePort)) {
			port = getDefaultValueForNullData(cachedStreamConfig.getDestinationPort(), DecoderConstant.EMPTY);
		}

		addAdvanceControlProperties(advancedControllableProperties,
				createText(stats, streamGroup + StreamControllingMetric.ADDRESS.getName(), address));
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.SOURCE_PORT.getName(), sourcePort));
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.DESTINATION_PORT.getName(), port));
	}

	/**
	 * This method is used for populate all create stream control properties :
	 * <li>Protocol: TS over SRT</li>
	 * <li>SRT mode: Rendezvous</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateCreateStreamControlCaseTSOverSRTRendezvous(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		// Get properties current value
		String address = getDefaultValueForNullData(cachedStreamConfig.getDestinationAddress(), DecoderConstant.EMPTY);
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties,
				createText(stats, streamGroup + StreamControllingMetric.ADDRESS.getName(), address));
		stats.put(streamGroup + StreamControllingMetric.SOURCE_PORT.getName(), port);
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.DESTINATION_PORT.getName(), port));
	}

	/**
	 * This method is used for populate all create stream control properties :
	 * <li>Protocol: RTSP</li>
	 * <li>SRT mode: Rendezvous</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 */
	private void populateCreateStreamControlCaseRTSP(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig, String streamGroup) {
		String rtspAddress = getDefaultValueForNullData(cachedStreamConfig.getAddress(), DecoderConstant.DEFAULT_RTSP_URL);
		addAdvanceControlProperties(advancedControllableProperties,
				createText(stats, streamGroup + StreamControllingMetric.RTSP_URL.getName(), rtspAddress));
	}

	/**
	 * This method is used for populate cancel button of create stream control:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 */
	private void populateCancelButtonForCreateStream(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		StreamConfig streamInfoDTO = defaultStream();
		String cancel = ControllingMetricGroup.CREATE_STREAM.getUiName() + DecoderConstant.HASH + StreamControllingMetric.CANCEL.getName();

		if (!streamInfoDTO.equals(createStream)) {
			stats.put(ControllingMetricGroup.CREATE_STREAM.getUiName() + DecoderConstant.HASH + StreamControllingMetric.EDITED.getName(), DecoderConstant.TRUE_VALUE);
			stats.put(cancel, DecoderConstant.EMPTY);
			addAdvanceControlProperties(advancedControllableProperties, createButton(cancel, DecoderConstant.CANCEL, DecoderConstant.CANCELLING));
		} else {
			stats.remove(cancel);
			stats.put(ControllingMetricGroup.CREATE_STREAM.getUiName() + DecoderConstant.HASH + StreamControllingMetric.EDITED.getName(), DecoderConstant.FALSE_VALUE);

			for (AdvancedControllableProperty controllableProperty : advancedControllableProperties) {
				if (controllableProperty.getName().equals(cancel)) {
					advancedControllableProperties.remove(controllableProperty);
					break;
				}
			}
		}
	}

	/**
	 * This method is used to add default value of stream config to localStreamInfoList
	 */
	private StreamConfig defaultStream() {
		StreamConfig streamConfig = new StreamConfig();
		streamConfig.setName(DecoderConstant.EMPTY);
		streamConfig.setPort(DecoderConstant.EMPTY);
		streamConfig.setFec(DecoderConstant.EMPTY);
		streamConfig.setEncapsulation(Encapsulation.TS_OVER_UDP.getApiName());
		streamConfig.setDestinationAddress(DecoderConstant.ADDRESS_ANY);
		streamConfig.setSourceAddress(DecoderConstant.ADDRESS_ANY);
		return streamConfig;
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform create stream control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for calling control all create stream control properties in case:
	 * <li>Stream Name</li>
	 * <li>Encapsulation</li>
	 * <li>Network type</li>
	 * <li>Port</li>
	 * <li>Multicast address</li>
	 * <li>Source address</li>
	 * <li>Source port</li>
	 * <li>Destination port</li>
	 * <li>FecRTP</li>
	 * <li>SRT mode</li>
	 * <li>Latency</li>
	 * <li>Stream conversion</li>
	 * <li>SrtToUdpAddress</li>
	 * <li>SrtToUdpPort</li>
	 * <li>SrtToUdpTos</li>
	 * <li>SrtToUdpTtl</li>
	 * <li>Encrypted</li>
	 * <li>Passphrase</li>
	 * <li>Reject unencrypted caller</li>
	 * <li>RTSP URL</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param controllableProperty name of controllable property
	 * @param value value of controllable property
	 *
	 * @throws ResourceNotReachableException when fail to create stream
	 */
	private void createStreamControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, String streamControllingGroup,
			String controllableProperty, String value) {
		StreamControllingMetric streamControllingMetric = StreamControllingMetric.getByName(controllableProperty);

		switch (streamControllingMetric) {
			case STREAM_NAME:
				createStream.setName(value);

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, streamControllingGroup + StreamControllingMetric.STREAM_NAME.getName(), createStream.getName()));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case ENCAPSULATION:
				Encapsulation encapsulationEnum = Encapsulation.getByUiName(value);
				removeUnusedStatsAndControlByProtocol(stats, advancedControllableProperties, createStream, streamControllingGroup);
				createStream.setEncapsulation(encapsulationEnum.getApiName());

				populateCreateStreamControl(stats, advancedControllableProperties, createStream, streamControllingGroup);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case NETWORK_TYPE:
				NetworkType networkType = NetworkType.getByUiName(value);
				removeUnusedStatsAndControlByNetworkType(stats, advancedControllableProperties, createStream, streamControllingGroup);
				switch (networkType) {
					case UNI_CAST:
						createStream.setDestinationAddress(DecoderConstant.ADDRESS_ANY);
						createStream.setSourceAddress(DecoderConstant.ADDRESS_ANY);
						break;
					case MULTI_CAST:
						createStream.setDestinationAddress(DecoderConstant.EMPTY);
						createStream.setSourceAddress(DecoderConstant.EMPTY);
						break;
					default:
						if (logger.isWarnEnabled()) {
							logger.warn(String.format("SRT mode %s is not supported.", networkType.getUiName()));
						}
						break;
				}

				populateCreateStreamControl(stats, advancedControllableProperties, createStream, streamControllingGroup);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case PORT:
				String port = DecoderConstant.EMPTY;
				try {
					Integer portIntValue = Integer.parseInt(value);
					if (portIntValue < DecoderConstant.MIN_PORT) {
						portIntValue = DecoderConstant.MIN_PORT;
					}
					if (portIntValue > DecoderConstant.MAX_PORT) {
						portIntValue = DecoderConstant.MAX_PORT;
					}
					port = portIntValue.toString();
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("Invalid port value", e);
					}
				}
				createStream.setPort(port);
				createStream.setDestinationPort(port);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, streamControllingGroup + StreamControllingMetric.PORT.getName(), port));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case ADDRESS:
				createStream.setDestinationAddress(value);

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, streamControllingGroup + StreamControllingMetric.ADDRESS.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case MULTICAST_ADDRESS:
				createStream.setDestinationAddress(value);

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, streamControllingGroup + StreamControllingMetric.MULTICAST_ADDRESS.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SOURCE_ADDRESS:
				createStream.setSourceAddress(value);

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, streamControllingGroup + StreamControllingMetric.SOURCE_ADDRESS.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SOURCE_PORT:
				String sourcePort = DecoderConstant.EMPTY;
				try {
					Integer sourcePortIntValue = Integer.parseInt(value);
					if (sourcePortIntValue < DecoderConstant.MIN_PORT) {
						sourcePortIntValue = DecoderConstant.MIN_PORT;
					}
					if (sourcePortIntValue > DecoderConstant.MAX_PORT) {
						sourcePortIntValue = DecoderConstant.MAX_PORT;
					}
					sourcePort = sourcePortIntValue.toString();
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("Invalid port value", e);
					}
				}
				createStream.setSourcePort(sourcePort);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, streamControllingGroup + StreamControllingMetric.SOURCE_PORT.getName(), sourcePort));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case DESTINATION_PORT:
				String destinationPort = DecoderConstant.EMPTY;
				try {
					Integer portIntValue = Integer.parseInt(value);
					if (portIntValue < DecoderConstant.MIN_PORT) {
						portIntValue = DecoderConstant.MIN_PORT;
					}
					if (portIntValue > DecoderConstant.MAX_PORT) {
						portIntValue = DecoderConstant.MAX_PORT;
					}
					destinationPort = portIntValue.toString();
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("Invalid port value", e);
					}
				}
				createStream.setPort(destinationPort);
				createStream.setDestinationPort(destinationPort);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, streamControllingGroup + StreamControllingMetric.DESTINATION_PORT.getName(), destinationPort));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case FEC:
				Fec fec = Fec.getByUiName(value);
				createStream.setFec(fec.getApiStatsName());

				populateCreateStreamControl(stats, advancedControllableProperties, createStream, streamControllingGroup);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SRT_MODE:
				SRTMode srtMode = SRTMode.getByName(value);
				removeUnusedStatsAndControlBySRTMode(stats, advancedControllableProperties, createStream, streamControllingGroup);

				createStream.setSrtMode(srtMode.getUiName());
				populateCreateStreamControl(stats, advancedControllableProperties, createStream, streamControllingGroup);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case LATENCY:
				String latency = DecoderConstant.DEFAULT_LATENCY.toString();
				try {
					Integer latencyIntValue = Integer.parseInt(value);
					if (latencyIntValue < DecoderConstant.MIN_LATENCY) {
						latencyIntValue = DecoderConstant.MIN_LATENCY;
					}
					if (latencyIntValue > DecoderConstant.MAX_LATENCY) {
						latencyIntValue = DecoderConstant.MAX_LATENCY;
					}
					latency = latencyIntValue.toString();
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("Invalid port value", e);
					}
				}
				createStream.setLatency(latency);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, streamControllingGroup + StreamControllingMetric.LATENCY.getName(), createStream.getLatency()));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SRT_TO_UDP_STREAM_CONVERSION:
				StreamConversion streamConversion = createStream.getStreamConversion();
				SwitchOnOffControl streamFlipping = SwitchOnOffControl.getByCode(Integer.parseInt(value));
				if (streamConversion == null) {
					streamConversion = new StreamConversion();
					if(streamFlipping.isEnable()) {
						streamConversion.setAddress(DecoderConstant.EMPTY);
						streamConversion.setTos(DecoderConstant.DEFAULT_TOS);
						streamConversion.setAddress(DecoderConstant.EMPTY);
						streamConversion.setTtl(DecoderConstant.DEFAULT_TTL.toString());
						streamConversion.setUdpPort(DecoderConstant.EMPTY);
					}
				}
				removeUnusedStatsAndControlByStreamConversion(stats, advancedControllableProperties, createStream, streamControllingGroup);
				streamConversion.setStreamFlipping(streamFlipping.getName());
				createStream.setStreamConversion(streamConversion);
				createStream.setStreamFlipping(streamFlipping.getName());

				populateCreateStreamControl(stats, advancedControllableProperties, createStream, streamControllingGroup);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SRT_TO_UDP_ADDRESS:
				streamConversion = createStream.getStreamConversion();
				streamConversion.setAddress(value);
				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, streamControllingGroup + StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SRT_TO_UDP_PORT:
				streamConversion = createStream.getStreamConversion();
				String srtToUDPPort = DecoderConstant.EMPTY;
				try {
					Integer portIntValue = Integer.parseInt(value);
					if (portIntValue < DecoderConstant.MIN_PORT) {
						portIntValue = DecoderConstant.MIN_PORT;
					}
					if (portIntValue > DecoderConstant.MAX_PORT) {
						portIntValue = DecoderConstant.MAX_PORT;
					}
					srtToUDPPort = portIntValue.toString();
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("Invalid port value", e);
					}
				}
				streamConversion.setUdpPort(srtToUDPPort);
				createStream.setStreamConversion(streamConversion);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, streamControllingGroup + StreamControllingMetric.SRT_TO_UDP_PORT.getName(), srtToUDPPort));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SRT_TO_UDP_TOS:
				streamConversion = createStream.getStreamConversion();
				try {
					Integer tosIntValue;
					if (value.startsWith(DecoderConstant.HEX_PREFIX)) {
						tosIntValue = Integer.parseInt(value.replace(DecoderConstant.HEX_PREFIX, ""), 16);
					} else {
						tosIntValue = (int) Float.parseFloat(value);
					}
					String tosHexValue = DecoderConstant.HEX_PREFIX + String.format("%02X", 0xFF & tosIntValue);
					if (tosIntValue < Integer.parseInt(DecoderConstant.MIN_OF_TOS, 16)) {
						tosHexValue = DecoderConstant.HEX_PREFIX + DecoderConstant.MIN_OF_TOS;
					}
					if (tosIntValue > Integer.parseInt(DecoderConstant.MAX_OF_TOS, 16)) {
						tosHexValue = DecoderConstant.HEX_PREFIX + DecoderConstant.MAX_OF_TOS;
					}
					streamConversion.setTos(tosHexValue);
					createStream.setStreamConversion(streamConversion);

					addAdvanceControlProperties(advancedControllableProperties,
							createText(stats, streamControllingGroup + StreamControllingMetric.SRT_TO_UDP_TOS.getName(), tosHexValue));
					populateCancelButtonForCreateStream(stats, advancedControllableProperties);
					populateLocalExtendedStats(stats, advancedControllableProperties);
					break;
				} catch (Exception var60) {
					throw new NumberFormatException("Value of ParameterToS is invalid. TOS must be hex value range to 00-FF");
				}
			case SRT_TO_UDP_TTL:
				streamConversion = createStream.getStreamConversion();
				String ttl = DecoderConstant.DEFAULT_TTL.toString();
				try {
					int ttlIntValue = Integer.parseInt(value);
					if (ttlIntValue < DecoderConstant.MIN_TTL) {
						ttl = DecoderConstant.MIN_TTL.toString();
					}
					if (ttlIntValue > DecoderConstant.MAX_TTL) {
						ttl = DecoderConstant.MAX_TTL.toString();
					}
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("Invalid ttl value", e);
					}
				}
				streamConversion.setTtl(ttl);
				createStream.setStreamConversion(streamConversion);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, streamControllingGroup + StreamControllingMetric.SRT_TO_UDP_TTL.getName(), ttl));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case ENCRYPTED:
				SwitchOnOffControl srtSettingEnum = SwitchOnOffControl.getByCode(Integer.parseInt(value));
				removeUnusedStatsAndControlByEncrypted(stats, advancedControllableProperties, createStream, streamControllingGroup);

				createStream.setSrtSettings(srtSettingEnum.getName());
				populateCreateStreamControl(stats, advancedControllableProperties, createStream, streamControllingGroup);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case PASSPHRASE:
				createStream.setPassphrase(value);
				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, streamControllingGroup + StreamControllingMetric.PASSPHRASE.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case REJECT_UNENCRYPTED_CALLERS:
				RejectUnencrypted rejectUnencryptedCallers = RejectUnencrypted.getByCode(Integer.parseInt(value));
				createStream.setRejectUnencrypted(rejectUnencryptedCallers.getName());

				addAdvanceControlProperties(advancedControllableProperties, createSwitch(
						stats, streamControllingGroup + StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName(), rejectUnencryptedCallers.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case RTSP_URL:
				createStream.setAddress(value);
				populateCreateStreamControl(stats, advancedControllableProperties, createStream, streamControllingGroup);
				break;
			case CREATE:
				performCreateStreamControl();
				createStream = defaultStream();
				populateCreateStreamControl(stats, advancedControllableProperties, defaultStream(), streamControllingGroup);
				break;
			case CANCEL:
				createStream = defaultStream();
				populateCreateStreamControl(stats, advancedControllableProperties, defaultStream(), streamControllingGroup);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Operation %s with value %s is not supported.", controllableProperty, value));
				}
				throw new IllegalStateException(String.format("Operation %s with value %s is not supported.", controllableProperty, value));
		}
	}

	/**
	 * This method is used to perform create stream control
	 *
	 * @throws ResourceNotReachableException when fail to send CLI command
	 */
	private void performCreateStreamControl() {
		try {
			String request = createStream.contributeCommand(CommandOperation.OPERATION_STREAM.getName(), CommandOperation.OPERATION_CREATE.getName());
			String response = send(request);
			if (StringUtils.isNullOrEmpty(response) || !response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
				throw new ResourceNotReachableException(DecoderConstant.SPACE + Deserializer.getErrorMessage(response));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.DECODER_CONTROL_ERR + DecoderConstant.SPACE + e.getMessage(), e);
		}
	}

	/**
	 * This method is used to remove unused statistics/AdvancedControllableProperty based on protocol:
	 * <li>TS over UDP</li>
	 * <li>TS over RTP</li>
	 * <li>TS over SRT</li>
	 *
	 * @param stats Map of statistics
	 * @param controls List of AdvancedControllableProperty
	 * @param preStreamInfo previous stream info
	 * @param groupName group name
	 */
	private void removeUnusedStatsAndControlByProtocol(Map<String, String> stats, List<AdvancedControllableProperty> controls, StreamConfig preStreamInfo, String groupName) {
		Encapsulation preEncapsulation = Encapsulation.getByApiName(getDefaultValueForNullData(preStreamInfo.getEncapsulation(), DecoderConstant.EMPTY));
		List<String> listKeyToBeRemove = new ArrayList<>();
		switch (preEncapsulation) {
			case TS_OVER_UDP:
			case TS_OVER_RTP:
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.STREAM_NAME.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.ENCAPSULATION.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.DELETE.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.EDITED.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.APPLY_CHANGE.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.CANCEL.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.NETWORK_TYPE.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.PORT.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.FEC.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.MULTICAST_ADDRESS.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SOURCE_ADDRESS.getName()));
				removeUnusedStatsAndControls(stats, controls, listKeyToBeRemove);
				break;
			case TS_OVER_SRT:
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.STREAM_NAME.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.ENCAPSULATION.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.DELETE.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.EDITED.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.APPLY_CHANGE.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.CANCEL.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_MODE.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.PORT.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.LATENCY.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_PORT.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_TOS.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_TTL.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.ENCRYPTED.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.PASSPHRASE.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.ADDRESS.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SOURCE_PORT.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.DESTINATION_PORT.getName()));
				removeUnusedStatsAndControls(stats, controls, listKeyToBeRemove);
				break;
			case RTSP:
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.STREAM_NAME.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.ENCAPSULATION.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.RTSP_URL.getName()));
				removeUnusedStatsAndControls(stats, controls, listKeyToBeRemove);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Encapsulation mode %s is not supported.", preEncapsulation.getUiName()));
				}
				break;
		}
	}

	/**
	 * This method is used to remove unused statistics/AdvancedControllableProperty based on Network type:
	 * <li>Uni-cast</li>
	 * <li>Multicast</li>
	 *
	 * @param stats Map of statistics
	 * @param controls List of AdvancedControllableProperty
	 * @param preStreamInfo previous stream info
	 * @param groupName group name
	 */
	private void removeUnusedStatsAndControlByNetworkType(Map<String, String> stats, List<AdvancedControllableProperty> controls, StreamConfig preStreamInfo, String groupName) {
		String address = getDefaultValueForNullData(preStreamInfo.getDestinationAddress(), DecoderConstant.EMPTY);
		String sourceAddress = getDefaultValueForNullData(preStreamInfo.getSourceAddress(), DecoderConstant.EMPTY);
		List<String> listKeyToBeRemove = new ArrayList<>();
		if (!address.equals(DecoderConstant.ADDRESS_ANY) || !sourceAddress.equals(DecoderConstant.ADDRESS_ANY)) {
			listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.MULTICAST_ADDRESS.getName()));
			listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SOURCE_ADDRESS.getName()));
			removeUnusedStatsAndControls(stats, controls, listKeyToBeRemove);
		}
	}

	/**
	 * This method is used to remove unused statistics/AdvancedControllableProperty based on SRT mode:
	 * <li>Listener</li>
	 * <li>Caller</li>
	 * <li>Rendezvous</li>
	 *
	 * @param stats Map of statistics
	 * @param controls List of AdvancedControllableProperty
	 * @param preStreamInfo previous stream info
	 * @param groupName group name
	 */
	private void removeUnusedStatsAndControlBySRTMode(Map<String, String> stats, List<AdvancedControllableProperty> controls, StreamConfig preStreamInfo, String groupName) {
		List<String> listKeyToBeRemove = new ArrayList<>();
		SRTMode preSRTMode = SRTMode.getByName(getDefaultValueForNullData(preStreamInfo.getSrtMode(), DecoderConstant.EMPTY));
		switch (preSRTMode) {
			case LISTENER:
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.PORT.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName()));
				removeUnusedStatsAndControls(stats, controls, listKeyToBeRemove);
				break;
			case CALLER:
			case RENDEZVOUS:
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.ADDRESS.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SOURCE_PORT.getName()));
				listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.DESTINATION_PORT.getName()));
				removeUnusedStatsAndControls(stats, controls, listKeyToBeRemove);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("SRT mode %s is not supported.", preSRTMode.getUiName()));
				}
				break;
		}
	}

	/**
	 * This method is used to remove unused statistics/AdvancedControllableProperty based on SRT mode:
	 *
	 * @param stats Map of statistics
	 * @param controls List of AdvancedControllableProperty
	 * @param preStreamInfo previous stream info
	 * @param groupName group name
	 */
	private void removeUnusedStatsAndControlByStreamConversion(Map<String, String> stats, List<AdvancedControllableProperty> controls, StreamConfig preStreamInfo, String groupName) {
		List<String> listKeyToBeRemove = new ArrayList<>();
		StreamConversion streamConversion = preStreamInfo.getStreamConversion();
		SwitchOnOffControl streamFlipping = SwitchOnOffControl.getByName(getDefaultValueForNullData(createStream.getStreamFlipping(), DecoderConstant.EMPTY));
		if (streamConversion != null) {
			streamFlipping = SwitchOnOffControl.getByName(getDefaultValueForNullData(streamConversion.getStreamFlipping(), DecoderConstant.EMPTY));
		}
		if (streamFlipping.isEnable()) {
			listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName()));
			listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_PORT.getName()));
			listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_TOS.getName()));
			listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.SRT_TO_UDP_TTL.getName()));
			removeUnusedStatsAndControls(stats, controls, listKeyToBeRemove);
		}
	}

	/**
	 * This method is used to remove unused statistics/AdvancedControllableProperty based on Encrypted:
	 *
	 * @param stats Map of statistics
	 * @param controls List of AdvancedControllableProperty
	 * @param preStreamInfo previous stream info
	 * @param groupName group name
	 */
	private void removeUnusedStatsAndControlByEncrypted(Map<String, String> stats, List<AdvancedControllableProperty> controls, StreamConfig preStreamInfo, String groupName) {
		List<String> listKeyToBeRemove = new ArrayList<>();
		SwitchOnOffControl aeEncrypted = SwitchOnOffControl.getByName(getDefaultValueForNullData(preStreamInfo.getSrtSettings(), DecoderConstant.EMPTY));
		if (aeEncrypted.isEnable()) {
			listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.PASSPHRASE.getName()));
			listKeyToBeRemove.add(String.format("%s%s", groupName, StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName()));
			removeUnusedStatsAndControls(stats, controls, listKeyToBeRemove);
		}
	}

	/**
	 * This method is used to remove unused statistics/AdvancedControllableProperty from {@link HaivisionXDecoderCommunicator#localExtendedStatistics}
	 *
	 * @param stats Map of statistics that contains statistics to be removed
	 * @param controls List of controls that contains AdvancedControllableProperty to be removed
	 * @param listKeys list key of statistics to be removed
	 */
	private void removeUnusedStatsAndControls(Map<String, String> stats, List<AdvancedControllableProperty> controls, List<String> listKeys) {
		for (String key : listKeys) {
			stats.remove(key);
			controls.removeIf(advancedControllableProperty -> advancedControllableProperty.getName().equals(key));
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate stream control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for populate all create stream control properties:
	 * <li>Protocol: TS over UDP</li>
	 * <li>Protocol: TS over RTP</li>
	 * <li>Protocol: TS over SRT</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param cachedStreamConfig stream config info
	 */
	private void populateStreamConfig(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig) {
		// Get controllable property current value
		String streamGroup;
		Encapsulation encapsulationEnum = Encapsulation.getByUiName(getDefaultValueForNullData(cachedStreamConfig.getEncapsulation(), DecoderConstant.EMPTY));
		String streamName = getDefaultValueForNullData(cachedStreamConfig.getName(), DecoderConstant.EMPTY);
		if (StringUtils.isNullOrEmpty(streamName) || streamName.equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
			streamGroup = ControllingMetricGroup.STREAM.getUiName() + cachedStreamConfig.getDefaultStreamName() + DecoderConstant.HASH;
		} else {
			streamGroup = ControllingMetricGroup.STREAM.getUiName() + streamName + DecoderConstant.HASH;
		}

		// Populate stream config stats
		stats.put(streamGroup + StreamControllingMetric.STREAM_NAME.getName(), streamName);
		stats.put(streamGroup + StreamControllingMetric.ENCAPSULATION.getName(), encapsulationEnum.getUiName());
		addAdvanceControlProperties(advancedControllableProperties, createButton(streamGroup + DecoderConstant.DELETE, DecoderConstant.DELETE, DecoderConstant.DELETING));
		stats.put(streamGroup + DecoderConstant.DELETE, DecoderConstant.EMPTY);

		switch (encapsulationEnum) {
			case TS_OVER_UDP:
			case TS_OVER_RTP:
				populateStreamConfigCaseTSOverUDPAndTSOverRTP(stats, cachedStreamConfig, streamGroup);
				break;
			case TS_OVER_SRT:
				populateStreamConfigCaseTSOverSRT(stats, cachedStreamConfig, streamGroup);
				break;
			case RTSP:
				String rtspAddress = getDefaultValueForNullData(cachedStreamConfig.getAddress(), DecoderConstant.DEFAULT_RTSP_URL);
				stats.put(streamGroup + StreamControllingMetric.RTSP_URL.getName(), rtspAddress);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Encapsulation mode %s is not supported.", encapsulationEnum.getUiName()));
				}
				break;
		}
	}

	/**
	 * This method is used for populate all stream control properties:
	 * <li>Protocol: TS over UDP & TS over RTP</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param cachedStreamConfig stream config info
	 */
	private void populateStreamConfigCaseTSOverUDPAndTSOverRTP(Map<String, String> stats, StreamConfig cachedStreamConfig, String streamGroup) {
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		String address = getDefaultValueForNullData(cachedStreamConfig.getDestinationAddress(), DecoderConstant.EMPTY);
		String sourceAddress = getDefaultValueForNullData(cachedStreamConfig.getSourceAddress(), DecoderConstant.EMPTY);
		Fec fecEnum = Fec.getByAPIStatsName(getDefaultValueForNullData(cachedStreamConfig.getFec(), DecoderConstant.EMPTY));
		NetworkType networkTypeEnum = NetworkType.UNI_CAST;
		if (!StringUtils.isNullOrEmpty(address)) {
			networkTypeEnum = NetworkType.MULTI_CAST;
			stats.put(streamGroup + StreamControllingMetric.MULTICAST_ADDRESS.getName(), address);
			stats.put(streamGroup + StreamControllingMetric.SOURCE_ADDRESS.getName(), sourceAddress);
		}
		stats.put(streamGroup + StreamControllingMetric.PORT.getName(), port);
		stats.put(streamGroup + StreamControllingMetric.FEC.getName(), fecEnum.getUiName());
		stats.put(streamGroup + StreamControllingMetric.NETWORK_TYPE.getName(), networkTypeEnum.getUiName());
	}


	/**
	 * This method is used for populate all stream control properties:
	 * <li>Protocol: TS over SRT</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param cachedStreamConfig stream config info
	 */
	private void populateStreamConfigCaseTSOverSRT(Map<String, String> stats, StreamConfig cachedStreamConfig, String streamGroup) {
		SRTMode srtMode = SRTMode.getByName(getDefaultValueForNullData(cachedStreamConfig.getSrtMode(), DecoderConstant.EMPTY));
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		String latency = getDefaultValueForNullData(cachedStreamConfig.getLatency(), DecoderConstant.DEFAULT_LATENCY.toString());
		SwitchOnOffControl streamFlipping = SwitchOnOffControl.getByName(getDefaultValueForNullData(cachedStreamConfig.getStreamFlipping(), DecoderConstant.EMPTY));
		StreamConversion streamConversion = cachedStreamConfig.getStreamConversion();
		String address = getDefaultValueForNullData(cachedStreamConfig.getDestinationAddress(), DecoderConstant.EMPTY);
		if (address.equals(DecoderConstant.ADDRESS_ANY)) {
			address = DecoderConstant.EMPTY;
		}

		// Populate relevant control when srt to udp is enabled/ disabled
		stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName(), streamFlipping.getName());
		if (streamConversion != null) {
			streamFlipping = SwitchOnOffControl.getByName(getDefaultValueForNullData(streamConversion.getStreamFlipping(), DecoderConstant.EMPTY));
			if (streamFlipping.isEnable()) {
				String srtToUdpAddress = getDefaultValueForNullData(streamConversion.getAddress(), DecoderConstant.EMPTY);
				String srtToUdpPort = getDefaultValueForNullData(streamConversion.getUdpPort(), DecoderConstant.EMPTY);
				String srtToUdpTos = getDefaultValueForNullData(streamConversion.getTos(), DecoderConstant.DEFAULT_TOS);
				String srtToUdpTtl = getDefaultValueForNullData(streamConversion.getTtl(), DecoderConstant.DEFAULT_TTL.toString());

				stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName(), srtToUdpAddress);
				stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_PORT.getName(), srtToUdpPort);
				stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_TOS.getName(), srtToUdpTos);
				stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_TTL.getName(), srtToUdpTtl);
			}
		}

		// Populate relevant control when encrypted is enabled/ disabled
		SwitchOnOffControl aeEncrypted = SwitchOnOffControl.getByName(getDefaultValueForNullData(cachedStreamConfig.getSrtSettings(), DecoderConstant.EMPTY));
		stats.put(streamGroup + StreamControllingMetric.ENCRYPTED.getName(), aeEncrypted.getName());
		if (aeEncrypted.isEnable()) {
			String passphrase = getDefaultValueForNullData(cachedStreamConfig.getPassphrase(), DecoderConstant.DEFAULT_PASSPHRASE);
			stats.put(streamGroup + StreamControllingMetric.PASSPHRASE.getName(), passphrase);
		}

		// Populate relevant control when Encapsulation(protocol) is ts-srt
		stats.put(streamGroup + StreamControllingMetric.SRT_MODE.getName(), srtMode.getUiName());
		stats.put(streamGroup + StreamControllingMetric.LATENCY.getName(), latency);
		switch (srtMode) {
			case LISTENER:
				stats.put(streamGroup + StreamControllingMetric.PORT.getName(), port);
				stats.put(streamGroup + StreamControllingMetric.ENCRYPTED.getName(), aeEncrypted.getName());
				if (aeEncrypted.isEnable()) {
					RejectUnencrypted rejectUnencrypted = RejectUnencrypted.getByName(getDefaultValueForNullData(cachedStreamConfig.getRejectUnencrypted(), DecoderConstant.EMPTY));
					stats.put(streamGroup + StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName(), rejectUnencrypted.getName());
				}
				break;
			case CALLER:
				String sourcePort = getDefaultValueForNullData(cachedStreamConfig.getSourcePort(), DecoderConstant.DEFAULT_SOURCE_PORT);
				stats.put(streamGroup + StreamControllingMetric.ADDRESS.getName(), address);
				stats.put(streamGroup + StreamControllingMetric.SOURCE_PORT.getName(), sourcePort);
				stats.put(streamGroup + StreamControllingMetric.DESTINATION_PORT.getName(), port);
				break;
			case RENDEZVOUS:
				stats.put(streamGroup + StreamControllingMetric.ADDRESS.getName(), address);
				stats.put(streamGroup + StreamControllingMetric.SOURCE_PORT.getName(), port);
				stats.put(streamGroup + StreamControllingMetric.DESTINATION_PORT.getName(), port);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("SRT mode %s is not supported.", srtMode.getUiName()));
				}
				break;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform stream control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for calling control all stream control properties in case:
	 * <li>Delete stream</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param streamName name of stream
	 * @param controllableProperty name of controllable property
	 *
	 * @throws ResourceNotReachableException when fail to control stream
	 */
	private void streamControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, String streamName,
			String controllableProperty) {
		StreamControllingMetric streamControllingMetric = StreamControllingMetric.getByName(controllableProperty);

		Optional<StreamConfig> cachedStreamConfig = cachedStreamConfigs.stream().filter(config -> config.getName().equals(streamName) || config.getDefaultStreamName().equals(streamName)).findFirst();
		String streamId;
		if (cachedStreamConfig.isPresent()) {
			streamId = cachedStreamConfig.get().getId();
		}else {
			throw new ResourceNotReachableException(String.format("Stream %s is not exist", streamName));
		}

		switch (streamControllingMetric) {
			case DELETE:
				performDeleteStreamControl(streamId);
				cachedStreamConfigs.remove(cachedStreamConfig.get());
				for (StreamConfig streamConfig : cachedStreamConfigs) {
					populateStreamConfig(stats, advancedControllableProperties, streamConfig);
				}
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Operation %s is not supported.", controllableProperty));
				}
				throw new IllegalStateException(String.format("Operation %s is not supported.", controllableProperty));
		}

	}

	/**
	 * This method is used to perform delete stream control
	 *
	 * @throws ResourceNotReachableException when fail to send CLI command
	 */
	private void performDeleteStreamControl(String streamId) {
		try {
			String request = CommandOperation.OPERATION_STREAM.getName().
					concat(DecoderConstant.SPACE).
					concat(streamId).
					concat(DecoderConstant.SPACE).
					concat(CommandOperation.OPERATION_DELETE.getName());
			String response = send(request);
			if (StringUtils.isNullOrEmpty(response) || !response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
				throw new ResourceNotReachableException(DecoderConstant.SPACE + Deserializer.getErrorMessage(response));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.DECODER_CONTROL_ERR + DecoderConstant.SPACE + e.getMessage(), e);
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion


	//region populate audio control
	//--------------------------------------------------------------------------------------------------------------------------------


	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion


	//region perform audio control
	//--------------------------------------------------------------------------------------------------------------------------------


	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate advanced controllable properties
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Add advancedControllableProperties if advancedControllableProperties different empty
	 *
	 * @param advancedControllableProperties advancedControllableProperties is the list that store all controllable properties
	 * @param property the property is item advancedControllableProperties
	 */
	private void addAdvanceControlProperties(List<AdvancedControllableProperty> advancedControllableProperties, AdvancedControllableProperty property) {
		if (property != null) {
			for (AdvancedControllableProperty controllableProperty : advancedControllableProperties) {
				if (controllableProperty.getName().equals(property.getName())) {
					advancedControllableProperties.remove(controllableProperty);
					break;
				}
			}
			advancedControllableProperties.add(property);
		}
	}

	/**
	 * Instantiate Text controllable property
	 *
	 * @param name name of the property
	 * @param label default button label
	 * @return AdvancedControllableProperty button instance
	 */
	private AdvancedControllableProperty createButton(String name, String label, String labelPressed) {
		AdvancedControllableProperty.Button button = new AdvancedControllableProperty.Button();
		button.setLabel(label);
		button.setLabelPressed(labelPressed);
		button.setGracePeriod(0L);
		return new AdvancedControllableProperty(name, new Date(), button, "");
	}

	/**
	 * Create a switch controllable property
	 *
	 * @param name name of the switch
	 * @param status initial switch state (0|1)
	 * @return AdvancedControllableProperty button instance
	 */
	private AdvancedControllableProperty createSwitch(Map<String, String> stats, String name, int status, String labelOff, String labelOn) {
		AdvancedControllableProperty.Switch toggle = new AdvancedControllableProperty.Switch();
		toggle.setLabelOff(labelOff);
		toggle.setLabelOn(labelOn);
		stats.put(name, String.valueOf(status));
		return new AdvancedControllableProperty(name, new Date(), toggle, status);
	}

	/**
	 * Create a controllable property Text
	 *
	 * @param name the name of property
	 * @param stringValue character string
	 * @return AdvancedControllableProperty Text instance
	 */
	private AdvancedControllableProperty createText(Map<String, String> stats, String name, String stringValue) {
		if (stringValue == null) {
			stringValue = DecoderConstant.EMPTY;
		}
		stats.put(name, stringValue);
		AdvancedControllableProperty.Text text = new AdvancedControllableProperty.Text();
		return new AdvancedControllableProperty(name, new Date(), text, stringValue);
	}

	/***
	 * Create AdvancedControllableProperty preset instance
	 * @param name name of the control
	 * @param initialValue initial value of the control
	 * @return AdvancedControllableProperty preset instance
	 */
	private AdvancedControllableProperty createDropdown(Map<String, String> stats, String name, List<String> values, String initialValue) {
		stats.put(name, initialValue);
		AdvancedControllableProperty.DropDown dropDown = new AdvancedControllableProperty.DropDown();
		dropDown.setOptions(values.toArray(new String[0]));
		dropDown.setLabels(values.toArray(new String[0]));

		return new AdvancedControllableProperty(name, new Date(), dropDown, initialValue);
	}

	/**
	 * Create a controllable property Numeric
	 *
	 * @param name the name of property
	 * @param initialValue character String
	 * @return AdvancedControllableProperty Text instance
	 */
	private AdvancedControllableProperty createNumeric(Map<String, String> stats, String name, String initialValue) {
		stats.put(name, initialValue);
		AdvancedControllableProperty.Numeric numeric = new AdvancedControllableProperty.Numeric();
		return new AdvancedControllableProperty(name, new Date(), numeric, initialValue);
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	/**
	 * get default value for null data
	 *
	 * @param value value of monitoring properties
	 * @return String (none/value)
	 */
	private String getDefaultValueForNullData(String value, String defaultValue) {
		return StringUtils.isNullOrEmpty(value) ? defaultValue : value;
	}

	/**
	 * Counting metric group is failed to monitor
	 *
	 * @return number failed monitoring metric group in the metric
	 */
	private int getNoOfFailedMonitorMetricGroup() {
		int noOfFailedMonitorMetric = DecoderConstant.MIN_NUMBER_OF_FAILED_MONITOR_METRIC;
		noOfFailedMonitorMetric += EnumSet.allOf(MonitoringMetricGroup.class).stream().count();
		return noOfFailedMonitorMetric;
	}

	/**
	 * This method is used to map switch control value from string to boolean
	 *
	 * @param value value of switch control in String
	 * @return boolean value of switch control true/false
	 */
	public boolean mapSwitchControlValue(String value) {
		return value.equals("1");
	}
}
