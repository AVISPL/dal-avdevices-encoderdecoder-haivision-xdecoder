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
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.SRTMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamStatsMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.Deserializer;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication.AuthenticationRoleWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderStatsWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfo;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfoWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.Stream;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConfig;
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
	private boolean isUpdateLocalDecoderControl = false;
	private boolean isUpdateLocalStreamControl = false;
	private boolean isEmergencyDelivery = false;
	private ExtendedStatistics localExtendedStatistics;

	// Decoder and stream DTO
	private List<DecoderConfig> realtimeDecoderConfigs;
	private List<DecoderConfig> cachedDecoderConfigs;
	private List<StreamConfig> realtimeStreamConfigs;
	private List<StreamConfig> cachedStreamConfigs;
	private List<String> stillImages;

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
			if (!isEmergencyDelivery) {
				populateDecoderMonitoringMetrics(stats);
				if (isUpdateLocalDecoderControl || cachedDecoderConfigs.isEmpty()) {
					cachedDecoderConfigs = realtimeDecoderConfigs.stream().map(decoderConfig -> new DecoderConfig(decoderConfig)).collect(Collectors.toList());
					isUpdateLocalDecoderControl = false;
				}
				if (isUpdateLocalStreamControl || cachedStreamConfigs.size() != filteredStreamIDSet.size()) {
					cachedStreamConfigs.clear();
					cachedStreamConfigs = realtimeStreamConfigs.stream().map(streamInfo -> new StreamConfig(streamInfo))
							.filter(streamInfo -> filteredStreamIDSet.contains(streamInfo.getId())).collect(Collectors.toList());
					isUpdateLocalStreamControl = false;
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
			ControllingMetricGroup controllingGroup = ControllingMetricGroup.getByName(splitProperty[0]);

			switch (controllingGroup) {
				case DECODER:
					String name = splitProperty[0].substring(7);
					Integer decoderID = Integer.parseInt(name);
					decoderControl(stats, advancedControllableProperties, decoderID, splitProperty[1], value);
					break;
				default:
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("Controlling group %s is not supported.", controllingGroup.getName()));
					}
					throw new IllegalStateException(String.format("Controlling group %s is not supported.", controllingGroup.getName()));
			}
		} finally {
			reentrantLock.unlock();
		}
	}

	@Override
	public void controlProperties(List<ControllableProperty> list) {
		// ToDo:
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

		for (int decoderID = DecoderConstant.MIN_DECODER_ID; decoderID <= DecoderConstant.MAX_DECODER_ID; decoderID++) {
			retrieveDecoderStats(stats, decoderID);
		}

		if (failedMonitor.size() == getNoOfFailedMonitorMetricGroup()) {
			StringBuilder errBuilder = new StringBuilder();
			for (Map.Entry<String, String> failedMetric : failedMonitor.entrySet()) {
				errBuilder.append(failedMetric.getValue());
				errBuilder.append(DecoderConstant.NEXT_LINE);
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
		for (Integer decoderID = DecoderConstant.MIN_DECODER_ID; decoderID < DecoderConstant.MAX_DECODER_ID; decoderID++) {
			populateDecoderControl(stats, advancedControllableProperties, decoderID);
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
				AuthenticationRoleWrapper authenticationRoleWrapper = objectMapper.convertValue(responseMap, AuthenticationRoleWrapper.class);
				if (authenticationRoleWrapper.getAuthenticationRole() != null){
					role = authenticationRoleWrapper.getAuthenticationRole().getRole();
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
				String[] splitResponses = response.split(DecoderConstant.COLON);
				int stillImageDataIndex = 1;
				if (stillImageDataIndex > splitResponses.length || StringUtils.isNullOrEmpty(splitResponses[1])) {
					stillImages = new ArrayList<>();
					stillImages = Arrays.asList(splitResponses[stillImageDataIndex].split("\r\n"));
					stillImages.addAll(DropdownList.names(StillImage.class));

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
		DecoderConfig decoderInfo = decoderInfoWrapper.getDecoderConfigInfo();

		if (cachedDecoderConfigs.size() > decoderID) {
			DecoderConfig localDecoderInfo = this.cachedDecoderConfigs.get(decoderID);
			DecoderConfig decoderInfoDTO = this.realtimeDecoderConfigs.get(decoderID);
			if (decoderInfoDTO.equals(localDecoderInfo) && !decoderInfo.equals(decoderInfoDTO)) {
				this.realtimeDecoderConfigs.set(decoderID, decoderInfo);
				this.isUpdateLocalDecoderControl = true;
			}
		}
		if (!isUpdateLocalDecoderControl) {
			if (this.realtimeDecoderConfigs.size() > decoderID) {
				this.realtimeDecoderConfigs.set(decoderID, decoderInfo);
			} else {
				this.realtimeDecoderConfigs.add(decoderInfo);
			}
		}
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
		String streamName = streamStatsWrapper.getStream().getStreamName();
		if (StringUtils.isNullOrEmpty(streamName) || streamName.equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
			streamName = streamStatsWrapper.getStreamConfig().getDefaultStreamName();
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

		Optional<StreamConfig> streamInfoDTO = this.realtimeStreamConfigs.stream().filter(st -> streamID.toString().equals(st.getId())).findFirst();
		Optional<StreamConfig> localStreamInfo = this.cachedStreamConfigs.stream().filter(st -> streamID.toString().equals(st.getId())).findFirst();
		if (localStreamInfo.isPresent() && streamInfoDTO.isPresent() && localStreamInfo.get().equals(streamInfoDTO.get()) && !streamInfoDTO.get().equals(streamConfigInfo)) {
			this.realtimeStreamConfigs.remove(streamInfoDTO.get());
			this.realtimeStreamConfigs.add(streamConfigInfo);
			this.isUpdateLocalStreamControl = true;
		}

		if (!isUpdateLocalStreamControl) {
			streamInfoDTO.ifPresent(config -> this.realtimeStreamConfigs.remove(config));
			this.realtimeStreamConfigs.add(streamConfigInfo);
		}
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

	//region populate decoder SDI control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for populate all Decoder control properties:
	 * Buffering Mode: Auto
	 * <li>Stream ID</li>
	 * <li>Still Image</li>
	 * <li>Still Image Delay</li>
	 * <li>Buffering Mode</li>
	 * <li>Hdr Dynamic Range</li>
	 * <li>Output 1</li>
	 * <li>Output 2</li>
	 * <li>Output 3</li>
	 * <li>Output 4</li>
	 * <li>Output Frame Rate</li>
	 * <li>Quad Mode</li>
	 *
	 * Buffering Mode: Fixed
	 * <li>Stream ID</li>
	 * <li>Still Image</li>
	 * <li>Still Image Delay</li>
	 * <li>Buffering Mode</li>
	 * <li>Buffering Delay</li>
	 * <li>Hdr Dynamic Range</li>
	 * <li>Output 1</li>
	 * <li>Output 2</li>
	 * <li>Output 3</li>
	 * <li>Output 4</li>
	 * <li>Output Frame Rate</li>
	 * <li>Quad Mode</li>
	 *
	 * Buffering Mode: MultiSync
	 * <li>Stream ID</li>
	 * <li>Still Image</li>
	 * <li>Still Image Delay</li>
	 * <li>Buffering Mode</li>
	 * <li>Multi Sync Buffering Delay</li>
	 * <li>Hdr Dynamic Range</li>
	 * <li>Output 1</li>
	 * <li>Output 2</li>
	 * <li>Output 3</li>
	 * <li>Output 4</li>
	 * <li>Output Frame Rate</li>
	 * <li>Quad Mode</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param decoderID ID of decoder
	 */
	private void populateDecoderControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, Integer decoderID) {
		// Get controllable property current value
		DecoderConfig cachedDecoderConfig = this.cachedDecoderConfigs.get(decoderID);

		String primaryStreamID = getDefaultValueForNullData(cachedDecoderConfig.getPrimaryStream(), DecoderConstant.NONE);
		String primaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
		if (this.cachedStreamConfigs != null) {
			for (StreamConfig cachedStreamInfo : cachedStreamConfigs) {
				if (primaryStreamID.equals(cachedStreamInfo.getId())) {
					primaryStreamName = cachedStreamInfo.getName();
					if (StringUtils.isNullOrEmpty(primaryStreamName)) {
						primaryStreamName = cachedStreamInfo.getDefaultStreamName();
					}
					break;
				}
			}
		}

		String secondaryStreamID = getDefaultValueForNullData(cachedDecoderConfig.getSecondaryStream(), DecoderConstant.NONE);
		String secondaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
		if (this.cachedStreamConfigs != null) {
			for (StreamConfig cachedStreamInfo : cachedStreamConfigs) {
				if (secondaryStreamID.equals(cachedStreamInfo.getId())) {
					secondaryStreamName = cachedStreamInfo.getName();
					if (StringUtils.isNullOrEmpty(secondaryStreamName)) {
						secondaryStreamName = cachedStreamInfo.getDefaultStreamName();
					}
					break;
				}
			}
		}

		StillImage stillImage = StillImage.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getStillImage(), DecoderConstant.EMPTY));
		String stillImageDelay = getDefaultValueForNullData(cachedDecoderConfig.getStillImageDelay(), DecoderConstant.EMPTY);
		SyncMode enableBuffering = SyncMode.getByName(getDefaultValueForNullData(cachedDecoderConfig.getEnableBuffering(), DecoderConstant.EMPTY));
		OutputResolution outputResolution = OutputResolution.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getOutputResolution(), DecoderConstant.EMPTY));
		OutputFrameRate outputFrameRate = OutputFrameRate.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getOutputFrameRate(), DecoderConstant.EMPTY));
		State decoderSDIState = State.getByName(getDefaultValueForNullData(cachedDecoderConfig.getState(), DecoderConstant.EMPTY));

		// Get list values of controllable property (dropdown list)
		List<String> resolutionModes = DropdownList.names(OutputResolution.class);
		List<String> frameRateModes = new ArrayList<>();
		List<String> streamNames = new ArrayList<>();

		switch (outputResolution.getResolutionCategory()) {
			case DecoderConstant.AUTOMATIC_RESOLUTION:
				frameRateModes = DropdownList.names(OutputFrameRate.class);
				break;
			case DecoderConstant.TV_RESOLUTION:
				switch (outputResolution) {
					case TV_RESOLUTIONS_1080P:
						frameRateModes = DropdownList.names(OutputFrameRate.class);
						frameRateModes.remove(OutputFrameRate.OUTPUT_FRAME_RATE_75);
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

		streamNames.add(DecoderConstant.NONE);
		if (this.cachedStreamConfigs != null) {
			for (StreamConfig streamConfig : cachedStreamConfigs) {
				if (!StringUtils.isNullOrEmpty(streamConfig.getName())) {
					streamNames.add(streamConfig.getName());
				} else {
					streamNames.add(streamConfig.getDefaultStreamName());
				}
			}
		}

		String decoderControllingGroup = ControllingMetricGroup.DECODER.getName() + decoderID + DecoderConstant.HASH;

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties, createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.PRIMARY_STREAM.getName(), streamNames, primaryStreamName));

		addAdvanceControlProperties(advancedControllableProperties, createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.SECONDARY_STREAM.getName(), streamNames, secondaryStreamName));

		addAdvanceControlProperties(advancedControllableProperties, createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.STILL_IMAGE.getName(), stillImages, stillImage.getUiName()));

		addAdvanceControlProperties(advancedControllableProperties, createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.STILL_IMAGE_DELAY.getName(), stillImageDelay));

		if (enableBuffering.isEnable()) {
			populateDecoderControlBufferingMode(stats, advancedControllableProperties, cachedDecoderConfig, decoderID);
		} else {
			stats.remove(decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName());
			stats.remove(decoderControllingGroup + DecoderControllingMetric.BUFFERING_DELAY.getName());
			stats.remove(decoderControllingGroup + DecoderControllingMetric.MULTI_SYNC_BUFFERING_DELAY.getName());
		}

		advancedControllableProperties.add(
				createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.OUTPUT_RESOLUTION.getName(), resolutionModes, outputFrameRate.getUiName()));

		advancedControllableProperties.add(
				createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.OUTPUT_FRAME_RATE.getName(), frameRateModes, outputFrameRate.getUiName()));

		advancedControllableProperties.add(createSwitch(stats, decoderControllingGroup + DecoderControllingMetric.STATE.getName(), decoderSDIState.getCode(),
				DecoderConstant.OFF, DecoderConstant.ON));

		populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
	}

	/**
	 * This method is used for populate all buffering mode of decoder control:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedDecoderInfo set of decoder configuration
	 */
	private void populateDecoderControlBufferingMode(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, DecoderConfig cachedDecoderInfo, Integer decoderID) {
		// Get controllable property current value
		BufferingMode bufferingMode = BufferingMode.getByAPIName(getDefaultValueForNullData(cachedDecoderInfo.getBufferingMode(), DecoderConstant.EMPTY));
		String bufferingDelay = getDefaultValueForNullData(cachedDecoderInfo.getBufferingDelay(), DecoderConstant.EMPTY);

		// Get list values of controllable property (dropdown)
		List<String> bufferingModeList = DropdownList.names(BufferingMode.class);
		String decoderControllingGroup = ControllingMetricGroup.DECODER.getName() + decoderID + DecoderConstant.HASH;

		// remove unused keys
		stats.remove(decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName());
		stats.remove(decoderControllingGroup + DecoderControllingMetric.BUFFERING_DELAY.getName());
		stats.remove(decoderControllingGroup + DecoderControllingMetric.MULTI_SYNC_BUFFERING_DELAY.getName());
		switch (bufferingMode) {
			case AUTO:
			case ADAPTIVE_LOW_LATENCY:
				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName(), bufferingModeList, bufferingMode.getUiName()));
				break;
			case FIXED:
				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName(), bufferingModeList, bufferingMode.getUiName()));

				addAdvanceControlProperties(advancedControllableProperties, createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_DELAY.getName(), bufferingDelay));
				break;
			case MULTI_SYNC:
				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_MODE.getName(), bufferingModeList, bufferingMode.getUiName()));

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.MULTI_SYNC_BUFFERING_DELAY.getName(), bufferingDelay));
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Buffering mode %s is not supported.", bufferingMode.getUiName()));
				}
				break;
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
		DecoderConfig cachedDecoderConfig = this.cachedDecoderConfigs.get(decoderID);
		DecoderConfig realtimeDecoderConfig = this.realtimeDecoderConfigs.get(decoderID);

		String applyChange = ControllingMetricGroup.DECODER.getName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.APPLY_CHANGE.getName();
		String cancel = ControllingMetricGroup.DECODER.getName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.CANCEL.getName();

		if (!cachedDecoderConfig.deepEquals(realtimeDecoderConfig)) {
			stats.put(ControllingMetricGroup.DECODER.getName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.EDITED.getName(), "True");
			stats.put(applyChange, DecoderConstant.EMPTY);
			stats.put(cancel, DecoderConstant.EMPTY);
			addAdvanceControlProperties(advancedControllableProperties, createButton(applyChange, DecoderConstant.APPLY, DecoderConstant.APPLYING));
			addAdvanceControlProperties(advancedControllableProperties, createButton(cancel, DecoderConstant.CANCEL, DecoderConstant.CANCELLING));
		} else {
			stats.remove(applyChange);
			stats.remove(cancel);
			stats.put(ControllingMetricGroup.DECODER.getName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.EDITED.getName(), "False");

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

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform decoder SDI control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for calling control all Decoder control properties in case:
	 * <li>Stream ID</li>
	 * <li>Still Image</li>
	 * <li>Still Image Delay</li>
	 * <li>Buffering Mode</li>
	 * <li>Buffering Delay</li>
	 * <li>Multi Sync Buffering Delay</li>
	 * <li>Hdr Dynamic Range</li>
	 * <li>Output 1</li>
	 * <li>Output 2</li>
	 * <li>Output 3</li>
	 * <li>Output 4</li>
	 * <li>Output Frame Rate</li>
	 * <li>Quad Mode</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param decoderID ID of decoder
	 * @param controllableProperty name of controllable property
	 * @param value value of controllable property
	 */
	private void decoderControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, Integer decoderID, String controllableProperty, String value) {
		DecoderControllingMetric decoderControllingMetric = DecoderControllingMetric.getByName(controllableProperty);
		DecoderConfig cachedDecoderConfig = this.cachedDecoderConfigs.get(decoderID);
		OutputResolution outputResolution = OutputResolution.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getOutputResolution(), DecoderConstant.EMPTY));

		// Get list values of controllable property (dropdown)
		List<String> resolutionModes = DropdownList.names(OutputResolution.class);
		List<String> frameRateModes = new ArrayList<>();
		List<String> streamNames = new ArrayList<>();

		switch (outputResolution.getResolutionCategory()) {
			case DecoderConstant.AUTOMATIC_RESOLUTION:
				frameRateModes = DropdownList.names(OutputFrameRate.class);
				break;
			case DecoderConstant.TV_RESOLUTION:
				switch (outputResolution) {
					case TV_RESOLUTIONS_1080P:
						frameRateModes = DropdownList.names(OutputFrameRate.class);
						frameRateModes.remove(OutputFrameRate.OUTPUT_FRAME_RATE_75);
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

		streamNames.add(DecoderConstant.NONE);
		if (this.cachedStreamConfigs != null) {
			for (StreamConfig streamConfig : cachedStreamConfigs) {
				if (!StringUtils.isNullOrEmpty(streamConfig.getName())) {
					streamNames.add(streamConfig.getName());
				} else {
					streamNames.add(streamConfig.getDefaultStreamName());
				}
			}
		}

		String decoderControllingGroup = ControllingMetricGroup.DECODER.getName() + decoderID + DecoderConstant.HASH;

		switch (decoderControllingMetric) {
			case PRIMARY_STREAM:
				String primaryStreamID = DecoderConstant.DEFAULT_STREAM_ID;
				String primaryStreamName = DecoderConstant.NONE;
				for (StreamConfig cachedStreamConfig : cachedStreamConfigs) {
					if (value.equals(cachedStreamConfig.getName()) || value.equals(cachedStreamConfig.getDefaultStreamName())) {
						primaryStreamID = cachedStreamConfig.getId();
						primaryStreamName = value;
						break;
					}
				}
				cachedDecoderConfig.setPrimaryStream(primaryStreamID);
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				addAdvanceControlProperties(advancedControllableProperties, createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.PRIMARY_STREAM.getName(), streamNames, primaryStreamName));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SECONDARY_STREAM:
				String secondaryStreamID = DecoderConstant.DEFAULT_STREAM_ID;
				String secondaryStreamName = DecoderConstant.NONE;
				for (StreamConfig cachedStreamConfig : cachedStreamConfigs) {
					if (value.equals(cachedStreamConfig.getName()) || value.equals(cachedStreamConfig.getDefaultStreamName())) {
						secondaryStreamID = cachedStreamConfig.getId();
						secondaryStreamName = value;
						break;
					}
				}
				cachedDecoderConfig.setPrimaryStream(secondaryStreamID);
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.PRIMARY_STREAM.getName(), streamNames, secondaryStreamName));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case STILL_IMAGE:
				StillImage stillImage = StillImage.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getStillImage(), DecoderConstant.EMPTY));
				cachedDecoderConfig.setStillImage(stillImage.getApiName());
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.STILL_IMAGE.getName(), stillImages, stillImage.getUiName()));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case STILL_IMAGE_DELAY:
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
						logger.warn("Invalid still image delay value");
					}
				}
				cachedDecoderConfig.setStillImageDelay(value);
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.STILL_IMAGE_DELAY.getName(), stillImageDelay.toString()));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case SYNC_MODE:
				boolean isEnableSyncMode = mapSwitchControlValue(value);
				String enableSyncMode = SyncMode.ENABLE_SYNC_MODE.getName();
				if (!isEnableSyncMode) {
					enableSyncMode = SyncMode.DISABLE_SYNC_MODE.getName();
				}
				cachedDecoderConfig.setEnableBuffering(enableSyncMode);
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				addAdvanceControlProperties(advancedControllableProperties, createSwitch(stats, decoderControllingGroup + DecoderControllingMetric.SYNC_MODE.getName(), Integer.parseInt(value),
						DecoderConstant.DISABLE, DecoderConstant.ENABLE));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case BUFFERING_MODE:
				BufferingMode bufferingMode = BufferingMode.getByUiName(value);
				cachedDecoderConfig.setBufferingMode(bufferingMode.getApiName());
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				populateDecoderControlBufferingMode(stats, advancedControllableProperties, cachedDecoderConfig, decoderID);
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case BUFFERING_DELAY:
				Integer bufferingDelay = DecoderConstant.MIN_BUFFERING_DELAY;
				try {
					bufferingDelay = Integer.parseInt(value);
					if (bufferingDelay < DecoderConstant.MIN_BUFFERING_DELAY) {
						bufferingDelay = DecoderConstant.MIN_BUFFERING_DELAY;
					}
					if (bufferingDelay > DecoderConstant.MAX_BUFFERING_DELAY) {
						bufferingDelay = DecoderConstant.MAX_BUFFERING_DELAY;
					}
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("Invalid buffering delay value");
					}
				}
				cachedDecoderConfig.setBufferingDelay(value);
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				advancedControllableProperties.add(createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.BUFFERING_DELAY.getName(), bufferingDelay.toString()));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case MULTI_SYNC_BUFFERING_DELAY:
				Integer multiSyncBufferingDelay = DecoderConstant.DEFAULT_MULTI_SYNC_BUFFERING_DELAY;
				try {
					multiSyncBufferingDelay = Integer.parseInt(value);
					if (multiSyncBufferingDelay < DecoderConstant.MIN_MULTI_SYNC_BUFFERING_DELAY) {
						multiSyncBufferingDelay = DecoderConstant.MIN_MULTI_SYNC_BUFFERING_DELAY;
					}
					if (multiSyncBufferingDelay > DecoderConstant.MAX_MULTI_SYNC_BUFFERING_DELAY) {
						multiSyncBufferingDelay = DecoderConstant.MAX_MULTI_SYNC_BUFFERING_DELAY;
					}
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("Invalid buffering delay value");
					}
				}
				cachedDecoderConfig.setBufferingDelay(value);
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				advancedControllableProperties.add(
						createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.MULTI_SYNC_BUFFERING_DELAY.getName(), multiSyncBufferingDelay.toString()));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case OUTPUT_FRAME_RATE:
				OutputFrameRate outputFrameRate = OutputFrameRate.getByAPIName(getDefaultValueForNullData(cachedDecoderConfig.getOutputFrameRate(), DecoderConstant.EMPTY));
				cachedDecoderConfig.setOutputFrameRate(outputFrameRate.getApiName());
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.OUTPUT_FRAME_RATE.getName(), frameRateModes, outputFrameRate.getUiName()));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case OUTPUT_RESOLUTION:
				cachedDecoderConfig.setOutputFrameRate(outputResolution.getApiName());
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				addAdvanceControlProperties(advancedControllableProperties,
						createDropdown(stats, decoderControllingGroup + DecoderControllingMetric.OUTPUT_RESOLUTION.getName(), resolutionModes, outputResolution.getUiName()));
				populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
				populateLocalExtendedStats(stats, advancedControllableProperties);
				break;
			case STATE:
				DecoderConfig realtimeDecoderConfig = this.realtimeDecoderConfigs.get(decoderID);
				State state = State.getByCode(Integer.parseInt(value));
				cachedDecoderConfig.setState(state.getName());
				this.cachedDecoderConfigs.set(decoderID, cachedDecoderConfig);
				switch (state) {
					case STOPPED:
						boolean isStopSuccessfully = performActiveDecoderSDIControl(cachedDecoderConfig, decoderID, CommandOperation.STOP.getName());

						if (!cachedDecoderConfig.deepEquals(realtimeDecoderConfig)) {
							this.cachedDecoderConfigs.set(decoderID, realtimeDecoderConfig);
						}
						if (!isStopSuccessfully) {
							reverseActiveControlAfterControlFailed(cachedDecoderConfig, decoderID);
						}
						populateDecoderControl(stats, advancedControllableProperties, decoderID);
						break;
					case START:
						// update decoder SDI if properties of decoder SDI is changed
						if (!cachedDecoderConfig.deepEquals(realtimeDecoderConfig)) {
							String response = performDecoderSDIUpdateControl(cachedDecoderConfig, decoderID);

							if (response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
								this.realtimeDecoderConfigs.set(decoderID, cachedDecoderConfig);
							} else {
								throw new ResourceNotReachableException("failed to start decoder SDI " + decoderID + DecoderConstant.NEXT_LINE + response);
							}
						}

						// start decoder SDI
						boolean isStartSuccessfully = performActiveDecoderSDIControl(cachedDecoderConfig, decoderID, CommandOperation.START.getName());
						if (!isStartSuccessfully) {
							reverseActiveControlAfterControlFailed(cachedDecoderConfig, decoderID);
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
				String response = performDecoderSDIUpdateControl(cachedDecoderConfig, decoderID);

				if (response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
					this.realtimeDecoderConfigs.set(decoderID, cachedDecoderConfig);
					populateDecoderControl(stats, advancedControllableProperties, decoderID);
				} else {
					throw new ResourceNotReachableException("failed to update decoder SDI " + decoderID + DecoderConstant.NEXT_LINE + response);
				}
				break;
			case CANCEL:
				this.cachedDecoderConfigs.clear();
				this.cachedDecoderConfigs.addAll(this.realtimeDecoderConfigs);
				populateDecoderControl(stats, advancedControllableProperties, decoderID);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Operation %s with value %s is not supported.", controllableProperty, value));
				}
				throw new IllegalStateException(String.format("Operation %s with value %s is not supported.", controllableProperty, value));
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
	 * This method is used to perform decoder SDI control: update
	 *
	 * @param decoderConfig set of decoder config info
	 * @param decoderID is ID of decoder
	 * @return String response
	 */
	private String performDecoderSDIUpdateControl(DecoderConfig decoderConfig, Integer decoderID) {
		try {
			String request = decoderConfig.contributeCommand(CommandOperation.OPERATION_VIDDEC.getName(), decoderID, CommandOperation.SET.getName());
			String response = send(request);
			if (!StringUtils.isNullOrEmpty(response)) {
				return response;
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.DECODER_CONTROL_ERR + DecoderConstant.NEXT_LINE + e.getMessage(), e);
		}
		return DecoderConstant.EMPTY;
	}

	/**
	 * This method is used to perform decoder SDI control: start/ stop
	 *
	 * @param decoderID is ID of decoder
	 * @param active start/ stop activation
	 * @return Boolean control result
	 */
	private boolean performActiveDecoderSDIControl(DecoderConfig decoderConfig, Integer decoderID, String active) {
		try {
			String request = CommandOperation.OPERATION_VIDDEC.getName()
					.concat(DecoderConstant.SPACE)
					.concat(decoderID.toString())
					.concat(DecoderConstant.SPACE)
					.concat(active);
			String response = send(request);
			return !StringUtils.isNullOrEmpty(response) && response.contains(DecoderConstant.SUCCESSFUL_RESPONSE);
		} catch (Exception e) {
			reverseActiveControlAfterControlFailed(decoderConfig, decoderID);
			throw new ResourceNotReachableException(DecoderConstant.DECODER_CONTROL_ERR + active + DecoderConstant.NEXT_LINE + e.getMessage(), e);
		}
	}

	/**
	 * This method is used to reverse active control value after control failed
	 *
	 * @param decoderConfig list of decoder data
	 */
	private void reverseActiveControlAfterControlFailed(DecoderConfig decoderConfig, Integer decoderID) {
		State state = State.getByName(getDefaultValueForNullData(decoderConfig.getState(), DecoderConstant.EMPTY));
		switch (state) {
			case STOPPED:
				state = State.START;
				decoderConfig.setState(state.getName());
				this.cachedDecoderConfigs.set(decoderID, decoderConfig);
				break;
			case START:
				state = State.STOPPED;
				decoderConfig.setState(state.getName());
				this.cachedDecoderConfigs.set(decoderID, decoderConfig);
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Decoder state %s is not supported.", state.getName()));
				}
				break;
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform stream control
	//--------------------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion
	//region Populate advanced controllable properties
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Add advancedControllableProperties if  advancedControllableProperties different empty
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
