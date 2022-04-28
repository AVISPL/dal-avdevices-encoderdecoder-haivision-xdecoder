/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.CommandOperation;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DeviceInfoMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.MonitoringMetricGroup;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
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
	private Set<Integer> filteredStreamsID;
	private Set<String> streamsNameFiltered;
	private Set<String> streamsStatusFiltered;
	private Set<String> portNumbersFiltered;

	// ToDo: This variable is used in decoder controlling part
	private boolean isUpdateLocalDecoderControl = false;
	// ToDo: This variable is used in decoder controlling part
	private boolean isUpdateLocalStreamControl = false;

	// Decoder and stream DTO
	private List<DecoderConfig> decoderConfigsDTO;
	private List<DecoderConfig> localDecoderConfigs;
	private List<StreamConfig> streamConfigsDTO;
	private List<StreamConfig> localStreamConfigs;

	//Adapter Properties
	private String streamNameFilter;
	private String portNumberFilter;
	private String streamStatusFilter;
	private String configManagement;

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

		final ExtendedStatistics extendedStatistics = new ExtendedStatistics();
		final Map<String, String> stats = new HashMap<>();
		failedMonitor = new HashMap<>();
		filteredStreamsID = new HashSet<>();

		if (decoderConfigsDTO == null) {
			decoderConfigsDTO = new ArrayList<>();
		}
		if (streamConfigsDTO == null) {
			streamConfigsDTO = new ArrayList<>();
		}
		if (localDecoderConfigs == null) {
			localDecoderConfigs = new ArrayList<>();
		}
		if (localStreamConfigs == null) {
			localStreamConfigs = new ArrayList<>();
		}
		populateDecoderMonitoringMetrics(stats);

		extendedStatistics.setStatistics(stats);
		return Collections.singletonList(extendedStatistics);
	}

	@Override
	public void controlProperty(ControllableProperty controllableProperty) {
		// ToDo:
	}

	@Override
	public void controlProperties(List<ControllableProperty> list) {
		// ToDo:
	}

	//region populate decoder monitoring metrics
	//--------------------------------------------------------------------------------------------------------------------------------

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

	/**
	 * This method is used to populate all monitoring properties:
	 * <li>Decoders statistic</li>
	 * <li>Streams statistic</li>
	 *
	 * @param stats list statistic property
	 * @throws ResourceNotReachableException when failedMonitor said all device monitoring data are failed to get
	 */
	private void populateDecoderMonitoringMetrics(Map<String, String> stats) {
		Objects.requireNonNull(stats);

		// Retrieving all device info
		retrieveDeviceInfo(stats);

		// Retrieving device temperature
		retrieveDeviceTemperature(stats);

		// Retrieving all decoders stats
		for (int decoderID = DecoderConstant.MIN_DECODER_ID; decoderID <= DecoderConstant.MAX_DECODER_ID; decoderID++) {
			retrieveDecoderStats(stats, decoderID);
		}

		// Retrieving all streams stats
		retrieveStreamStats(stats);

		if (failedMonitor.size() == getNoOfFailedMonitorMetricGroup()) {
			StringBuilder errBuilder = new StringBuilder();
			for (Map.Entry<String, String> failedMetric : failedMonitor.entrySet()) {
				errBuilder.append(failedMetric.getValue());
				errBuilder.append(DecoderConstant.NEXT_LINE);
			}
			throw new ResourceNotReachableException(errBuilder.toString());
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
					stats.put(DeviceInfoMetric.SERIAL_NUMBER.getName(), getDefaultValueForNullData(deviceInfo.getSerialNumber()));
					stats.put(DeviceInfoMetric.BOOT_VERSION.getName(), getDefaultValueForNullData(deviceInfo.getBootVersion()));
					stats.put(DeviceInfoMetric.CARD_TYPE.getName(), getDefaultValueForNullData(deviceInfo.getCardType()));
					stats.put(DeviceInfoMetric.CPLD_REVISION.getName(), getDefaultValueForNullData(deviceInfo.getCpldVersion()));
					stats.put(DeviceInfoMetric.FIRMWARE_DATE.getName(), getDefaultValueForNullData(deviceInfo.getFirmwareDate()));
					stats.put(DeviceInfoMetric.FIRMWARE_OPTIONS.getName(), getDefaultValueForNullData(deviceInfo.getFirmwareOptions()));
					stats.put(DeviceInfoMetric.FIRMWARE_VERSION.getName(), getDefaultValueForNullData(deviceInfo.getFirmwareVersion()));
					stats.put(DeviceInfoMetric.HARDWARE_COMPATIBILITY.getName(), getDefaultValueForNullData(deviceInfo.getHardwareCompatibility()));
					stats.put(DeviceInfoMetric.HARDWARE_VERSION.getName(), getDefaultValueForNullData(deviceInfo.getHardwareVersion()));
					stats.put(DeviceInfoMetric.PART_NUMBER.getName(), getDefaultValueForNullData(deviceInfo.getPartNumber()));
				} else {
					updateDeviceInfoFailedMonitor();
				}
			}
		} catch (Exception e) {
				logger.error("Error while retrieve device info: ", e);
			updateDeviceInfoFailedMonitor();
		}
	}

	/**
	 * This method is used to retrieve device info by send command "temperature get"
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
					stats.put(DeviceInfoMetric.TEMPERATURE.getName(), getDefaultValueForNullData(NormalizeData.getDataNumberValue(deviceInfo.getTemperatureStatus().getTemperature())));
				} else {
					updateDeviceInfoFailedMonitor();
				}
			}
		} catch (Exception e) {
				logger.error("Error while retrieve device info: ", e);
			updateDeviceInfoFailedMonitor();
		}
	}

	/**
	 * Update failedMonitor with getting device info error message
	 */
	private void updateDeviceInfoFailedMonitor() {
		failedMonitor.put(MonitoringMetricGroup.DEVICE_INFO.getName(), DecoderConstant.GETTING_DEVICE_INFO_ERR);
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
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getDecoder().getValueByDecoderMonitoringMetric(item)));
			}
		}
		if (decoderInfoWrapper.getDecoderStats() != null) {
			for (DecoderStatsMonitoringMetric item : DecoderStatsMonitoringMetric.values()) {
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getDecoderStats().getValueByDecoderMonitoringMetric(item)));
			}
		}
		if (decoderInfoWrapper.getTimecode() != null) {
			for (DecoderTimeCodeMonitoringMetric item : DecoderTimeCodeMonitoringMetric.values()) {
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getTimecode().getValueByDecoderMonitoringMetric(item)));
			}
		}
		if (decoderInfoWrapper.getAudio() != null) {
			for (DecoderAudioMonitoringMetric item : DecoderAudioMonitoringMetric.values()) {
				stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getAudio().getValueByDecoderMonitoringMetric(item)));
			}
		}
		if (decoderInfoWrapper.getVideo() != null) {
			for (DecoderVideoMonitoringMetric item : DecoderVideoMonitoringMetric.values()) {
			stats.put(decoderStatisticGroup + item.getName(), getDefaultValueForNullData(decoderInfoWrapper.getVideo().getValueByDecoderMonitoringMetric(item)));
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

		if (localDecoderConfigs.size() > decoderID) {
			DecoderConfig localDecoderInfo = this.localDecoderConfigs.get(decoderID);
			DecoderConfig decoderInfoDTO = this.decoderConfigsDTO.get(decoderID);
			if (decoderInfoDTO.equals(localDecoderInfo) && !decoderInfo.equals(decoderInfoDTO)) {
				this.decoderConfigsDTO.set(decoderID, decoderInfo);
				this.isUpdateLocalDecoderControl = true;
			}
		}
		if (!isUpdateLocalDecoderControl) {
			if (this.decoderConfigsDTO.size() > decoderID) {
				this.decoderConfigsDTO.set(decoderID, decoderInfo);
			} else {
				this.decoderConfigsDTO.add(decoderInfo);
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
				updateStreamStatisticsFailedMonitor();
			}
		} catch (Exception e) {
				logger.error("Error while retrieve stream statistics: ", e);
			updateStreamStatisticsFailedMonitor();
		}
	}

	/**
	 * Update failedMonitor with getting stream statistics error message
	 */
	private void updateStreamStatisticsFailedMonitor() {
		failedMonitor.put(MonitoringMetricGroup.STREAM_STATISTICS.getName(), DecoderConstant.GETTING_STREAM_STATS_ERR);
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
				stats.put(streamStatisticGroup + item.getName(), getDefaultValueForNullData(streamStatsWrapper.getValueByStreamMonitoringMetric(item)));
			}
		}
		if (streamStatsWrapper.getStreamStats() != null) {
			for (StreamStatsMonitoringMetric item : StreamStatsMonitoringMetric.values()) {
				stats.put(streamStatisticGroup + item.getName(), getDefaultValueForNullData(streamStatsWrapper.getStreamStats().getValueByStreamMonitoringMetric(item)));
			}
		}
		if (streamStatsWrapper.getSrt() != null) {
			for (SRTMonitoringMetric item : SRTMonitoringMetric.values()) {
				stats.put(streamStatisticGroup + item.getName(), getDefaultValueForNullData(streamStatsWrapper.getSrt().getValueByStreamMonitoringMetric(item)));
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

		Optional<StreamConfig> streamInfoDTO = this.streamConfigsDTO.stream().filter(st -> streamID.equals(st.getId())).findFirst();
		Optional<StreamConfig> localStreamInfo = this.localStreamConfigs.stream().filter(st -> streamID.equals(st.getId())).findFirst();
		if (localStreamInfo.isPresent() && streamInfoDTO.isPresent() && localStreamInfo.get().equals(streamInfoDTO.get()) && !streamInfoDTO.get().equals(streamConfigInfo)) {
			this.streamConfigsDTO.remove(streamInfoDTO.get());
			this.streamConfigsDTO.add(streamConfigInfo);
			this.isUpdateLocalStreamControl = true;
		}

		if (!isUpdateLocalStreamControl) {
			streamInfoDTO.ifPresent(config -> this.streamConfigsDTO.remove(config));
			this.streamConfigsDTO.add(streamConfigInfo);
		}
		filteredStreamsID.add(streamID);
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

	/**
	 * get default value for null data
	 *
	 * @param value value of monitoring properties
	 * @return String (none/value)
	 */
	private String getDefaultValueForNullData(String value) {
		return StringUtils.isNullOrEmpty(value) ? DecoderConstant.NONE : value;
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

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate decoder controlling metric
	//--------------------------------------------------------------------------------------------------------------------------------


	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform decoder control
	//--------------------------------------------------------------------------------------------------------------------------------


	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform stream control
	//--------------------------------------------------------------------------------------------------------------------------------


	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion
}
