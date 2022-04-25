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
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command.Account;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command.CommonCommand;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command.Haiversion;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command.StreamCommand;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command.Viddec;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DeviceInfoMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.MonitoringMetricGroup;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.monitoringmetric.StreamMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.Deserializer;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication.AuthenticationRoleWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderConfigInfo;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderInfoWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfo;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfoWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.Stream;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConfigInfo;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamInfoWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamStats;
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
 * @author Ivan / Symphony Dev Team<br>
 * Created on 4/6/2022
 * @since 1.0.0
 */
public class HaivisionXDecoderCommunicator extends SshCommunicator implements Monitorable, Controller {
	ObjectMapper objectMapper;
	private Map<String, String> failedMonitor;
	private Set<Integer> filteredStreamIDSet;
	private Set<String> streamsNameFiltered;
	private Set<String> streamsStatusFiltered;
	private Set<String> portNumbersFiltered;
	private boolean isUpdateLocalDecoderControl = false;
	private boolean isUpdateLocalStreamControl = false;

	// Decoder and stream DTO
	private List<DecoderConfigInfo> decoderConfigInfosDTO;
	private List<DecoderConfigInfo> localDecoderConfigInfos;
	private List<StreamConfigInfo> streamConfigDTOInfos;
	private List<StreamConfigInfo> localStreamConfigInfos;

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
		filteredStreamIDSet = new HashSet<>();

		if (decoderConfigInfosDTO == null) {
			decoderConfigInfosDTO = new ArrayList<>();
		}
		if (streamConfigDTOInfos == null) {
			streamConfigDTOInfos = new ArrayList<>();
		}
		if (localDecoderConfigInfos == null) {
			localDecoderConfigInfos = new ArrayList<>();
		}
		if (localStreamConfigInfos == null) {
			localStreamConfigInfos = new ArrayList<>();
		}
		populateDecoderMonitoringMetrics(stats);

		extendedStatistics.setStatistics(stats);
		return Collections.singletonList(extendedStatistics);
	}

	@Override
	public void controlProperty(ControllableProperty controllableProperty) {

	}

	@Override
	public void controlProperties(List<ControllableProperty> list) {

	}

	/**
	 * This method is used to retrieve User Role by send command "account {accountName} get"
	 *
	 * @throws ResourceNotReachableException When there is no valid User Role data or having an Exception
	 */
	public String retrieveUserRole() {
		objectMapper = new ObjectMapper();
		try {
			String request = Account.ACCOUNT.getName()
					.concat(DecoderConstant.SPACE)
					.concat(getLogin())
					.concat(DecoderConstant.SPACE)
					.concat(CommonCommand.GET.getName());

			String response = send(request);
			String role = null;
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				AuthenticationRoleWrapper authenticationRoleWrapper = objectMapper.convertValue(responseMap, AuthenticationRoleWrapper.class);
				role = authenticationRoleWrapper.getAuthenticationWrapper().getRole();
			}
			if (StringUtils.isNullOrEmpty(role)) {
				throw new ResourceNotReachableException("Role based is empty");
			}
			return role;
		} catch (Exception e) {
			throw new ResourceNotReachableException("Retrieve role based error: " + e.getMessage());
		}
	}

	/**
	 * Check for null data
	 *
	 * @param value value of monitoring properties
	 * @return String (none/value)
	 */
	private String checkForNullData(String value) {
		return value == null || value.equals(DecoderConstant.EMPTY) ? DecoderConstant.NONE : value;
	}

	/**
	 * Update failedMonitor with getting device info error message
	 *
	 * @param failedMonitor list statistics property
	 */
	private void updateDeviceInfoFailedMonitor(Map<String, String> failedMonitor) {
		failedMonitor.put(MonitoringMetricGroup.DEVICE_INFO.getName(), DecoderConstant.GETTING_DEVICE_INFO_ERR);
	}

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
			objectMapper = new ObjectMapper();
			String request = Haiversion.HAIVERSION.getName();
			String response = send(request);

			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				DeviceInfoWrapper deviceInfoWrapper = objectMapper.convertValue(responseMap, DeviceInfoWrapper.class);
				DeviceInfo deviceInfo = deviceInfoWrapper.getDeviceInfo();

				if (deviceInfo != null) {
					stats.put(DeviceInfoMetric.SERIAL_NUMBER.getName(), checkForNullData(deviceInfo.getSerialNumber()));
					stats.put(DeviceInfoMetric.BOOT_VERSION.getName(), checkForNullData(deviceInfo.getBootVersion()));
					stats.put(DeviceInfoMetric.CARD_TYPE.getName(), checkForNullData(deviceInfo.getCardType()));
					stats.put(DeviceInfoMetric.CPLD_REVISION.getName(), checkForNullData(deviceInfo.getCpldVersion()));
					stats.put(DeviceInfoMetric.FIRMWARE_DATE.getName(), checkForNullData(deviceInfo.getFirmwareDate()));
					stats.put(DeviceInfoMetric.FIRMWARE_OPTIONS.getName(), checkForNullData(deviceInfo.getFirmwareOptions()));
					stats.put(DeviceInfoMetric.FIRMWARE_VERSION.getName(), checkForNullData(deviceInfo.getFirmwareVersion()));
					stats.put(DeviceInfoMetric.FIRMWARE_TIME.getName(), checkForNullData(deviceInfo.getFirmwareTime()));
					stats.put(DeviceInfoMetric.HARDWARE_COMPATIBILITY.getName(), checkForNullData(deviceInfo.getHardwareCompatibility()));
					stats.put(DeviceInfoMetric.HARDWARE_VERSION.getName(), checkForNullData(deviceInfo.getHardwareVersion()));
					stats.put(DeviceInfoMetric.PART_NUMBER.getName(), checkForNullData(deviceInfo.getPartNumber()));
				} else {
					updateDeviceInfoFailedMonitor(failedMonitor);
				}
			}
		} catch (Exception e) {
			updateDeviceInfoFailedMonitor(failedMonitor);
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
			objectMapper = new ObjectMapper();
			String request = Haiversion.TEMPERATURE.getName();
			String response = send(request);

			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				DeviceInfo deviceInfo = objectMapper.convertValue(responseMap, DeviceInfo.class);

				if (deviceInfo != null) {
					stats.put(DeviceInfoMetric.TEMPERATURE.getName(), checkForNullData(deviceInfo.getTemperatureStatus().getTemperature()));
				} else {
					updateDeviceInfoFailedMonitor(failedMonitor);
				}
			}
		} catch (Exception e) {
			updateDeviceInfoFailedMonitor(failedMonitor);
		}
	}

	/**
	 * Update failedMonitor with Getting decoder stats error message
	 *
	 * @param failedMonitor list statistics property
	 * @param decoderID ID of the decoder
	 */
	private void updateDecoderStatisticsFailedMonitor(Map<String, String> failedMonitor, Integer decoderID) {
		failedMonitor.put(MonitoringMetricGroup.DECODER_STATISTICS.getName() + decoderID, DecoderConstant.GETTING_DECODER_STATS_ERR + decoderID);
	}

	/**
	 * This method is used update decoder statistic from DTO
	 *
	 * @param stats list statistics property
	 * @param decoderInfoWrapper pair of decoder config and stats
	 * @param decoderID ID of decoder
	 */
	private void populateDecoderStats(Map<String, String> stats, DecoderInfoWrapper decoderInfoWrapper, Integer decoderID) {
		String decoderStatisticGroup = MonitoringMetricGroup.DECODER_STATISTICS.getName() + decoderID + DecoderConstant.HASH;

		for (DecoderMonitoringMetric item : DecoderMonitoringMetric.values()) {
			stats.put(decoderStatisticGroup + item.getName(), checkForNullData(decoderInfoWrapper.getValueByDecoderMonitoringMetric(item)));
		}
	}

	/**
	 * This method is used update localDecoderConfigInfo statistic from DTO
	 *
	 * @param decoderInfoWrapper pair of decoder config and stats
	 * @param decoderID ID of decoder
	 */
	private void updateLocalDecoderConfigInfo(DecoderInfoWrapper decoderInfoWrapper, Integer decoderID) {
		DecoderConfigInfo decoderInfo = decoderInfoWrapper.getDecoderConfigInfo();

		if (localDecoderConfigInfos.size() > decoderID) {
			DecoderConfigInfo localDecoderInfo = this.localDecoderConfigInfos.get(decoderID);
			DecoderConfigInfo decoderInfoDTO = this.decoderConfigInfosDTO.get(decoderID);
			if (decoderInfoDTO.equals(localDecoderInfo) && !decoderInfo.equals(decoderInfoDTO)) {
				this.decoderConfigInfosDTO.set(decoderID, decoderInfo);
				this.isUpdateLocalDecoderControl = true;
			}
		}
		if (!isUpdateLocalDecoderControl) {
			if (this.decoderConfigInfosDTO.size() > decoderID) {
				this.decoderConfigInfosDTO.set(decoderID, decoderInfo);
			} else {
				this.decoderConfigInfosDTO.add(decoderID, decoderInfo);
			}
		}
	}

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
			objectMapper = new ObjectMapper();
			String request = Viddec.VIDDEC.getName()
					.concat(DecoderConstant.SPACE)
					.concat(decoderID.toString())
					.concat(DecoderConstant.SPACE)
					.concat(CommonCommand.GET.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommonCommand.ALL.getName());

			String response = send(request);
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				DecoderInfoWrapper decoderInfoWrapper = objectMapper.convertValue(responseMap, DecoderInfoWrapper.class);

				if (decoderInfoWrapper != null) {
					populateDecoderStats(stats, decoderInfoWrapper, decoderID);
					updateLocalDecoderConfigInfo(decoderInfoWrapper, decoderID);
				} else {
					updateDecoderStatisticsFailedMonitor(failedMonitor, decoderID);
				}
			} else {
				updateDecoderStatisticsFailedMonitor(failedMonitor, decoderID);
			}
		} catch (Exception e) {
			updateDecoderStatisticsFailedMonitor(failedMonitor, decoderID);
		}
	}

	/**
	 * Update failedMonitor with getting stream statistics error message
	 *
	 * @param failedMonitor list statistics property
	 */
	private void updateStreamStatisticsFailedMonitor(Map<String, String> failedMonitor) {
		failedMonitor.put(MonitoringMetricGroup.STREAM_STATISTICS.getName(), DecoderConstant.GETTING_STREAM_STATS_ERR);
	}

	/**
	 * This method is used update stream statistic from DTO
	 *
	 * @param stats list statistics property
	 * @param stream pair of stream config and stats
	 */
	private void populateStreamStats(Map<String, String> stats, StreamInfoWrapper stream) {
		String streamStatisticGroup = MonitoringMetricGroup.STREAM_STATISTICS.getName() + stream.getStream().getStreamName() + DecoderConstant.HASH;
		for (StreamMonitoringMetric item : StreamMonitoringMetric.values()) {
			stats.put(streamStatisticGroup + item.getName(), checkForNullData(stream.getValueByStreamMonitoringMetric(item)));
		}
	}

	/**
	 * This method is used update localDecoderConfigInfo statistic from DTO
	 *
	 * @param streamInfoWrapper pair of decoder config and stats
	 * @param streamID ID of decoder
	 */
	private void updateLocalStreamConfigInfo(StreamInfoWrapper streamInfoWrapper, Integer streamID) {
		StreamConfigInfo streamConfigInfo = streamInfoWrapper.getStreamConfigInfo();

		Optional<StreamConfigInfo> streamInfoDTO = this.streamConfigDTOInfos.stream().filter(st -> streamID.equals(st.getId())).findFirst();
		Optional<StreamConfigInfo> localStreamInfo = this.localStreamConfigInfos.stream().filter(st -> streamID.equals(st.getId())).findFirst();
		if (localStreamInfo.isPresent() && localStreamInfo.get().equals(streamInfoDTO.get()) && !streamInfoDTO.get().equals(streamConfigInfo)) {
			this.streamConfigDTOInfos.remove(streamInfoDTO.get());
			this.streamConfigDTOInfos.add(streamConfigInfo);
			this.isUpdateLocalStreamControl = true;
		}

		if (!isUpdateLocalStreamControl) {
			if (streamInfoDTO.isPresent()) {
				this.streamConfigDTOInfos.remove(streamInfoDTO.get());
			}
			this.streamConfigDTOInfos.add(streamConfigInfo);
		}
		filteredStreamIDSet.add(streamID);
	}

	/**
	 * This method is used to handle  input from adapter properties and convert it to Set of String  for control
	 *
	 * @return Set<String> is the Set of String of filter element
	 */
	public Set<String> handleAdapterPropertiesInputFromUser(String input) {
		if (!StringUtils.isNullOrEmpty(input)) {
			String[] listAdapterPropertyElement = input.split(DecoderConstant.COMMA);

			// Remove start and end spaces of each adapterProperty
			Set<String> setAdapterPropertiesElement = new HashSet<>();
			for (String adapterPropertyElement : listAdapterPropertyElement) {
				setAdapterPropertiesElement.add(adapterPropertyElement.trim());
			}
			return setAdapterPropertiesElement;
		}
		return null;
	}

	/**
	 * This method is used to handle  input from adapter properties (port, port range)
	 * When the input is an Integer value this method will check whether it is match with port from stream stats or not
	 * When the input is a range value this method will convert the range to min/ max port value and check whether it covers port from stream stats or not
	 *
	 * @return boolean the port and port range filtering result
	 */
	public boolean handleAdapterPropertyPortRangeFromUser(Integer portNumber) {
		int minPortNumber = 0;
		int maxPortNumber = 0;
		try {
			for (String portNumberFromAdapterProperties : portNumbersFiltered) {

				// Port range filtering
				if (portNumberFromAdapterProperties.contains(DecoderConstant.DASH)) {
					String[] rangeList = portNumberFromAdapterProperties.split(DecoderConstant.DASH);
					minPortNumber = Integer.parseInt(rangeList[0]);
					maxPortNumber = Integer.parseInt(rangeList[1]);

					// Swapping if min value > max value
					if (minPortNumber > maxPortNumber) {
						int temp = minPortNumber;
						minPortNumber = maxPortNumber;
						maxPortNumber = temp;
					}
					if (portNumber >= minPortNumber && portNumber <= maxPortNumber) {
						portNumbersFiltered.contains(portNumberFromAdapterProperties);
						return true;
					}

					// Port filtering
				} else if (portNumberFromAdapterProperties.equals(portNumber.toString())) {
					portNumbersFiltered.contains(portNumberFromAdapterProperties);
					return true;
				}
			}
		} catch (NumberFormatException f) {
			throw new ResourceNotReachableException(DecoderConstant.PORT_NUMBER_ERROR);
		}
		return false;
	}

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
			streamsNameFiltered = handleAdapterPropertiesInputFromUser(this.streamNameFilter);
		}
		if (this.streamStatusFilter != null && streamsStatusFiltered == null) {
			streamsStatusFiltered = handleAdapterPropertiesInputFromUser(this.streamStatusFilter.toUpperCase());
		}
		if (this.portNumberFilter != null && portNumbersFiltered == null) {
			portNumbersFiltered = handleAdapterPropertiesInputFromUser(this.portNumberFilter);
		}

		try {
			String request = StreamCommand.STREAM.getName()
					.concat(DecoderConstant.SPACE)
					.concat(CommonCommand.ALL.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommonCommand.GET.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommonCommand.ALL.getName());

			String response = send(request);

			if (response != null) {
				String[] responsesSplit = response.split("\r\n\r");

				for (String responseSplit : responsesSplit) {
					Map<String, Object> responseMap = Deserializer.convertDataToObject(responseSplit, request);
					StreamInfoWrapper streamInfoWrapper = objectMapper.convertValue(responseMap, StreamInfoWrapper.class);
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
						Integer port = Integer.parseInt(streamInfoWrapper.getStreamConfigInfo().getPort());
						boolean isValidPort = handleAdapterPropertyPortRangeFromUser(port);
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
						updateLocalStreamConfigInfo(streamInfoWrapper, Integer.parseInt(stream.getStreamId()));
					}
				}
			} else {
				updateStreamStatisticsFailedMonitor(failedMonitor);
			}
		} catch (Exception e) {
			updateStreamStatisticsFailedMonitor(failedMonitor);
		}
	}

	/**
	 * Counting metric group is failed to monitor
	 *
	 * @return number failed monitoring metric group in the metric
	 */
	private int getNoOfFailedMonitorMetricGroup() {
		int noOfFailedMonitorMetric = 3;
		noOfFailedMonitorMetric += EnumSet.allOf(MonitoringMetricGroup.class).stream().count();
		return noOfFailedMonitorMetric;
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

//region perform controls
//--------------------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------------------
//endregion
}
