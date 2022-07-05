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
import com.jcraft.jsch.JSchException;

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
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.audio.controllingmetric.AudioChannel;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.audio.controllingmetric.AudioControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.audio.controllingmetric.AudioLevel;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.audio.controllingmetric.AudioSource;
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
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.AudioOutput;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.HDMIControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.SurroundSound;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.VideoSource;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.service.controllingmetric.ServiceControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.service.controllingmetric.ServiceSwitchOnOffControl;
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
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.talkback.controllingmetric.TalkBackDecoderSDI;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.talkback.controllingmetric.TalkBackSwitchOnOffControl;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.talkback.controllingmetric.TalkbackControllingMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.Deserializer;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.audioconfig.AudioConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.audioconfig.AudioConfigWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication.AuthenticationRole;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderStatsWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfo;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfoWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.hdmiconfig.HDMIConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.hdmiconfig.HDMIConfigWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.service.ServiceConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.service.ServiceConfigWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.Stream;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConversion;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamStats;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamStatsWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.talkback.TalkBackConfig;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.talkback.TalkBackStats;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.talkback.TalkBackWrapper;
import com.avispl.symphony.dal.communicator.SshCommunicator;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * An implementation of SshCommunicator to provide communication and interaction with Haivision X Decoders
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
 * <li>Audio</li>
 * <li>HDMI</li>
 * <li>Service</li>
 * <li>Talkback</li>
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
	private List<String> customStillImages;
	private String roleBased;
	private boolean isUpdateCachedStreamControl = false;
	private boolean isEmergencyDelivery = false;
	private Boolean isConfigManagement;
	private ExtendedStatistics localExtendedStatistics;

	//Adapter Properties
	private String streamNameFilter;
	private String portNumberFilter;
	private String streamStatusFilter;
	private String configManagement = DecoderConstant.FALSE_VALUE;

	// flags for populate apply changes/ cancel changes
	private boolean isEditedForDecoderSDI1 = false;
	private boolean isEditedForDecoderSDI2 = false;
	private boolean isEditedForCreateStream = false;
	private boolean isEditedForAudio = false;
	private boolean isEditedForHDMI = false;
	private boolean isEditedForTalkback = false;


	/**
	 * store the list of decoder config info from device
	 */
	private List<DecoderConfig> realtimeDecoderConfigs;

	/**
	 * store the list of current decoder config info from symphony
	 */
	private List<DecoderConfig> cachedDecoderConfigs;

	/**
	 * store the list of stream config info (filtered) from device
	 */
	private List<StreamConfig> realtimeStreamConfigsFiltered;

	/**
	 * store the list of stream config info from device
	 */
	private List<StreamConfig> realtimeStreamConfigs;

	/**
	 * store the list of current stream config info (filtered) from symphony
	 */
	private List<StreamConfig> cachedStreamConfigsFiltered;

	/**
	 * store the current create stream config info from symphony
	 */
	private StreamConfig createStream;

	/**
	 * store the audio config info from device
	 */
	private AudioConfig realtimeAudioConfig;

	/**
	 * store the current audio config info from symphony
	 */
	private AudioConfig cachedAudioConfig;

	/**
	 * store the hdmi config info from device
	 */
	private HDMIConfig realtimeHDMIConfig;

	/**
	 * store the current hdmi config info from symphony
	 */
	private HDMIConfig cachedHDMIConfig;

	/**
	 * store the service config info from device
	 */
	private ServiceConfig realtimeServiceConfig;

	/**
	 * store the talkback config info from device
	 */
	private TalkBackConfig realtimeTalkbackConfig;

	/**
	 * store the current talkback config info from symphony
	 */
	private TalkBackConfig cachedTalkbackConfig;

	/**
	 * ReentrantLock to prevent null pointer exception to localExtendedStatistics when controlProperty method is called before GetMultipleStatistics method.
	 */
	private final ReentrantLock reentrantLock = new ReentrantLock();

	/**
	 * Prevent case where {@link HaivisionXDecoderCommunicator#controlProperty(ControllableProperty)} slow down -
	 * the getMultipleStatistics interval if it's fail to send the cmd
	 */
	private static final int CONTROL_SSH_TIMEOUT = 5000;

	/**
	 * Set back to default timeout value in {@link SshCommunicator}
	 */
	private static final int STATISTICS_SSH_TIMEOUT = 30000;

	/**
	 * Constructor set loginSuccess list, command success list, command error list
	 */
	public HaivisionXDecoderCommunicator() {
		super();
		// set list of login success strings (included at the end of response when command succeeds, typically ending with command prompt)
		setLoginSuccessList(Collections.singletonList("~$ "));

		// set list of command success strings (included at the end of response when command succeeds, typically ending with command prompt)
		this.setCommandSuccessList(Arrays.asList("~$ ", "(y,N): "));

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

// ToDo: removing controlling capabilities and config management
//	/**
//	 * Retrieves {@code {@link #configManagement }}
//	 *
//	 * @return value of {@link #configManagement}
//	 */
//	public String getConfigManagement() {
//		return configManagement;
//	}
//
//	/**
//	 * Sets {@code controllingCapabilitiesTrigger}
//	 *
//	 * @param configManagement the {@code java.lang.String} field
//	 */
//	public void setConfigManagement(String configManagement) {
//		this.configManagement = configManagement;
//	}

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
			if (realtimeStreamConfigsFiltered == null) {
				realtimeStreamConfigsFiltered = new ArrayList<>();
			}
			if (cachedDecoderConfigs == null) {
				cachedDecoderConfigs = new ArrayList<>();
			}
			if (cachedStreamConfigsFiltered == null) {
				cachedStreamConfigsFiltered = new ArrayList<>();
			}
			if (createStream == null) {
				createStream = defaultStream();
			}
			if (realtimeAudioConfig == null) {
				realtimeAudioConfig = new AudioConfig();
			}
			if (realtimeHDMIConfig == null) {
				realtimeHDMIConfig = new HDMIConfig();
			}
			if (realtimeServiceConfig == null) {
				realtimeServiceConfig = new ServiceConfig();
			}
			if (realtimeTalkbackConfig == null) {
				realtimeTalkbackConfig = new TalkBackConfig();
			}
			if (!isEmergencyDelivery) {
				realtimeStreamConfigs = new ArrayList<>();
				populateDecoderMonitoringMetrics(stats);
				if (cachedDecoderConfigs.isEmpty()) {
					cachedDecoderConfigs = realtimeDecoderConfigs.stream().map(DecoderConfig::new).collect(Collectors.toList());
				}
				if (isUpdateCachedStreamControl || cachedStreamConfigsFiltered.size() != filteredStreamIDSet.size()) {
					cachedStreamConfigsFiltered.clear();
					cachedStreamConfigsFiltered = realtimeStreamConfigsFiltered.stream().map(StreamConfig::new)
							.filter(streamInfo -> filteredStreamIDSet.contains(Integer.parseInt(streamInfo.getId()))).collect(Collectors.toList());
					isUpdateCachedStreamControl = false;
				}
				if (cachedAudioConfig == null) {
					cachedAudioConfig = new AudioConfig(realtimeAudioConfig);
				}
				if (cachedHDMIConfig == null) {
					cachedHDMIConfig = new HDMIConfig(realtimeHDMIConfig);
				}
				if (cachedTalkbackConfig == null) {
					cachedTalkbackConfig = new TalkBackConfig(realtimeTalkbackConfig);
				}

				if (isValidConfigManagement()) {
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
			this.timeout = CONTROL_SSH_TIMEOUT;
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
					String name = splitProperty[0].substring(DecoderConstant.INDEX_OF_DECODER_SDI_ID_IN_CONTROLLING_METRIC_GROUP);
					Integer decoderID = Integer.parseInt(name);
					decoderControl(stats, advancedControllableProperties, decoderID, splitProperty[1], value);
					break;
				case CREATE_STREAM:
					createStreamControl(stats, advancedControllableProperties, ControllingMetricGroup.CREATE_STREAM.getUiName() + DecoderConstant.HASH, splitProperty[1], value);
					break;
				case STREAM:
					String streamName = splitProperty[0].substring(DecoderConstant.INDEX_OF_STREAM_NAME_IN_CONTROLLING_METRIC_GROUP);
					streamControl(stats, advancedControllableProperties, streamName, splitProperty[1]);
					break;
				case AUDIO:
					audioControl(stats, advancedControllableProperties, ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH, splitProperty[1], value);
					break;
				case HDMI:
					hdmiControl(stats, advancedControllableProperties, splitProperty[1], value);
					break;
				case SERVICE:
					serviceControl(splitProperty[1], value);
					break;
				case TALKBACK:
					talkBackControl(stats, advancedControllableProperties, splitProperty[1], value);
					break;
				default:
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("Controlling group %s is not supported.", controllingGroup.getUiName()));
					}
					throw new IllegalStateException(String.format("Controlling group %s is not supported.", controllingGroup.getUiName()));
			}
		} finally {
			reentrantLock.unlock();
			this.timeout = STATISTICS_SSH_TIMEOUT;
		}
	}

	@Override
	public void controlProperties(List<ControllableProperty> list) {
		if (CollectionUtils.isEmpty(list)) {
			throw new IllegalArgumentException("MakitoXDecoderCommunicator: Controllable properties cannot be null or empty");
		}
		for (ControllableProperty controllableProperty : list) {
			controlProperty(controllableProperty);
		}
	}

	/**
	 * This method is used to retrieve and populate all monitoring properties:
	 * <li>Device info</li>
	 * <li>Decoders statistic</li>
	 * <li>Streams statistic</li>
	 * <li>Audio config</li>
	 * <li>HDMI config</li>
	 * <li>Service config</li>
	 * <li>Talkback config</li>
	 *
	 * @param stats list statistic property
	 * @throws ResourceNotReachableException when failedMonitor said all device monitoring data are failed to get
	 */
	private void populateDecoderMonitoringMetrics(Map<String, String> stats) {
		Objects.requireNonNull(stats);

		roleBased = retrieveUserRole();
		retrieveDeviceInfo(stats);
		retrieveDeviceTemperature(stats);
		retrieveStreamStats(stats);
		if (isValidConfigManagement()) {
			retrieveDeviceStillImage();
			retrieveAudioConfig();
			retrieveHDMIConfig();
			retrieveServiceConfig();
			retrieveTalkback();
		}

		for (int decoderID = DecoderConstant.MIN_DECODER_ID; decoderID < DecoderConstant.MAX_DECODER_ID; decoderID++) {
			retrieveDecoderStats(stats, decoderID);
		}

		if (failedMonitor.size() >= getNoOfFailedMonitorMetricGroup()) {
			StringBuilder errBuilder = new StringBuilder();
			for (Map.Entry<String, String> failedMetric : failedMonitor.entrySet()) {
				errBuilder.append(failedMetric.getValue());
				errBuilder.append(DecoderConstant.SPACE);
				errBuilder.append(DecoderConstant.NEXT_LINE);
			}
			throw new ResourceNotReachableException(errBuilder.toString());
		}
	}

	/**
	 * This method is used for populate all controlling properties:
	 * <li>Decoder SDI </li>
	 * <li>Create Stream</li>
	 * <li>Stream Control</li>
	 * <li>Audio</li>
	 * <li>HDMI</li>
	 * <li>Service</li>
	 * <li>Talkback</li>
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
		for (StreamConfig cachedStreamConfig : cachedStreamConfigsFiltered) {
			populateStreamConfig(stats, advancedControllableProperties, cachedStreamConfig);
		}

		// Audio control
		populateAudioControl(stats, advancedControllableProperties, realtimeAudioConfig, ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH);

		// HDMI control
		populateHDMIControl(stats, advancedControllableProperties);

		// Service
		if (realtimeServiceConfig != null && DecoderConstant.ADMIN_ROLE.equals(roleBased)) {
			populateServiceControl(stats, advancedControllableProperties);
		}

		// Talkback
		String talkbackStatus = getDefaultValueForNullData(realtimeTalkbackConfig.getState(), TalkBackSwitchOnOffControl.OFF.getApiStatsName());
		if (!talkbackStatus.equalsIgnoreCase(TalkBackSwitchOnOffControl.DISABLE.getApiStatsName())) {
			populateTalkBackControl(stats, advancedControllableProperties);
		}
	}

	/**
	 * This method is used to retrieve User Role by send command "account {accountName} get"
	 *
	 * @throws ResourceNotReachableException When there is no valid User Role data or having an Exception
	 */
	private String retrieveUserRole() {
		try {
			String request = CommandOperation.OPERATION_ACCOUNT.getName()
					.concat(DecoderConstant.SPACE)
					.concat(getLogin())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.GET.getName());

			String response = send(request);
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				Object objectResponse = responseMap.get(request.replaceAll(DecoderConstant.REGEX_REMOVE_SPACE_AND_NUMBER, DecoderConstant.EMPTY));
				AuthenticationRole authenticationRole = objectMapper.convertValue(objectResponse, AuthenticationRole.class);
				if (authenticationRole != null) {
					return authenticationRole.getRole();
				}
			}
			return DecoderConstant.GUEST_ROLE;
		} catch (JSchException j) {
			throw new ResourceNotReachableException(j.getMessage(), j);
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
			updateFailedMonitor(MonitoringMetricGroup.DEVICE_INFO.getName(), DecoderConstant.GETTING_DEVICE_INFO_ERR
					+ DecoderConstant.COMMA + DecoderConstant.SPACE + e.getMessage());
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
					stats.put(DeviceInfoMetric.TEMPERATURE.getName(), getDefaultValueForNullData(NormalizeData.extractNumbers(deviceInfo.getTemperatureStatus().getTemperature()), DecoderConstant.NONE));
				} else {
					updateFailedMonitor(MonitoringMetricGroup.TEMPERATURE.getName(), DecoderConstant.GETTING_DEVICE_TEMPERATURE_ERR);
				}
			} else {
				updateFailedMonitor(MonitoringMetricGroup.TEMPERATURE.getName(), DecoderConstant.GETTING_DEVICE_TEMPERATURE_ERR);
			}
		} catch (Exception e) {
			logger.error("Error while retrieve device temperature: ", e);
			updateFailedMonitor(MonitoringMetricGroup.TEMPERATURE.getName(), DecoderConstant.GETTING_DEVICE_TEMPERATURE_ERR +
					DecoderConstant.COMMA + DecoderConstant.SPACE + e.getMessage());
		}
	}

	/**
	 * This method is used to retrieve stillImage by send command "still list"
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveDeviceStillImage() {
		try {
			String request = CommandOperation.OPERATION_STILL_IMAGE.getName();
			String response = send(request);

			if (response != null) {
				String[] splitResponses = response.split(DecoderConstant.COLON + DecoderConstant.REGEX_TRAILING_OF_FIELD);
				int stillImageDataIndex = 1;
				if (stillImageDataIndex <= splitResponses.length) {
					customStillImages = new ArrayList<>();
					String[] deviceStillImage = splitResponses[stillImageDataIndex].split(DecoderConstant.REGEX_TRAILING_OF_FIELD);
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
			updateFailedMonitor(MonitoringMetricGroup.STILL_IMAGE.getName(), DecoderConstant.GETTING_DEVICE_STILL_IMAGE_ERR +
					DecoderConstant.COMMA + DecoderConstant.SPACE + e.getMessage());
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
					updateDecoderStatisticsFailedMonitor(decoderID, DecoderConstant.EMPTY);
				}
			} else {
				updateDecoderStatisticsFailedMonitor(decoderID, DecoderConstant.EMPTY);
			}
		} catch (Exception e) {
			logger.error("Error while retrieve decoder statistics: ", e);
			updateDecoderStatisticsFailedMonitor(decoderID, e.getMessage());
		}
	}

	/**
	 * Update failedMonitor with Getting decoder stats error message
	 *
	 * @param decoderID ID of the decoder
	 */
	private void updateDecoderStatisticsFailedMonitor(Integer decoderID, String errorMessage) {
		failedMonitor.put(MonitoringMetricGroup.DECODER_STATISTICS.getName() + decoderID,
				DecoderConstant.GETTING_DECODER_STATS_ERR + decoderID + DecoderConstant.COMMA + DecoderConstant.SPACE + errorMessage);
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

		// update cachedDecoderConfigs when decoder SDI config info is not edited on symphony but is edited on real device
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
				String[] responsesSplit = response.split(DecoderConstant.REGEX_TRAILING_OF_OBJECT);

				for (String responseSplit : responsesSplit) {
					responseSplit = responseSplit.replaceFirst(DecoderConstant.STREAM_CONVERSION_OBJECT_RESPONSE, DecoderConstant.STREAM_CONVERSION_ALT_OBJECT_RESPONSE);
					responseSplit = request.concat(DecoderConstant.REGEX_TRAILING_OF_FIELD).concat(responseSplit);
					Map<String, Object> responseMap = Deserializer.convertDataToObject(responseSplit, request);
					StreamStatsWrapper streamInfoWrapper = objectMapper.convertValue(responseMap, StreamStatsWrapper.class);

					// Check if converted object is not a stream
					if (streamInfoWrapper.getStream().getStreamId() == null) {
						break;
					}

					StreamStats streamStats = streamInfoWrapper.getStreamStats();
					StreamConfig streamConfigInfo = streamInfoWrapper.getStreamConfig();
					Stream stream = streamInfoWrapper.getStream();

					updateRealtimeStreamConfig(streamInfoWrapper);

					// Stream name filtering
					String streamName = stream.getStreamName();
					if (DecoderConstant.DEFAULT_STREAM_NAME.equals(streamName)) {
						streamName = streamConfigInfo.getDefaultStreamName();
					}
					if (this.streamNameFilter != null && streamsNameFiltered != null && streamsNameFiltered.contains(streamName)) {
						populateStreamStats(stats, streamInfoWrapper);
						updateLocalStreamConfigInfo(streamInfoWrapper, Integer.parseInt(stream.getStreamId()));
						continue;
					}

					// Stream status filtering
					if (StringUtils.isNotNullOrEmpty(this.streamStatusFilter) && streamsStatusFiltered != null && !streamsStatusFiltered.contains(streamStats.getState())) {
						continue;
					}

					// Port number filtering
					if (StringUtils.isNotNullOrEmpty(this.portNumberFilter) && portNumbersFiltered != null) {
						String port = getDefaultValueForNullData(streamConfigInfo.getPort(), DecoderConstant.EMPTY);
						if (port.isEmpty()) {
							streamConfigInfo.setPort(getDefaultValueForNullData(streamConfigInfo.getDestinationPort(), DecoderConstant.EMPTY));
						}
						Integer portIntValue = Integer.parseInt(streamInfoWrapper.getStreamConfig().getPort());
						boolean isValidPort = isValidPortRange(portIntValue);
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
					if (StringUtils.isNullOrEmpty(this.streamNameFilter)) {
						populateStreamStats(stats, streamInfoWrapper);
						updateLocalStreamConfigInfo(streamInfoWrapper, Integer.parseInt(stream.getStreamId().trim()));
					}
				}
			} else {
				updateFailedMonitor(MonitoringMetricGroup.STREAM_STATISTICS.getName(), DecoderConstant.GETTING_STREAM_STATS_ERR);
			}
		} catch (Exception e) {
			logger.error("Error while retrieve stream statistics: ", e);
			updateFailedMonitor(MonitoringMetricGroup.STREAM_STATISTICS.getName(), DecoderConstant.GETTING_STREAM_STATS_ERR +
					DecoderConstant.COMMA + DecoderConstant.SPACE + e.getMessage());
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

		String port = getDefaultValueForNullData(streamConfigInfo.getPort(), DecoderConstant.EMPTY);
		if (port.isEmpty()) {
			streamConfigInfo.setPort(getDefaultValueForNullData(streamConfigInfo.getDestinationPort(), DecoderConstant.EMPTY));
		}
		String tempAddress = getDefaultValueForNullData(streamConfigInfo.getAddress(), DecoderConstant.EMPTY);
		String tempDestinationAddress = NormalizeData.extractNumbersFromDataBySpaceIndex(tempAddress, DecoderConstant.ADDRESS_DATA_INDEX);
		String tempSourceAddress = NormalizeData.extractNumbersFromDataBySpaceIndex(tempAddress, DecoderConstant.SOURCE_ADDRESS_DATA_INDEX);
		if (tempDestinationAddress.isEmpty()) {
			tempDestinationAddress = NormalizeData.getAddressFromRawData(tempAddress);
		}
		if (tempSourceAddress.isEmpty()) {
			tempSourceAddress = NormalizeData.extractDataBySpaceIndex(tempAddress, DecoderConstant.SOURCE_ADDRESS_DATA_INDEX);
			tempSourceAddress = NormalizeData.getAddressFromRawData(tempSourceAddress);
		}
		streamConfigInfo.setDestinationAddress(tempDestinationAddress);
		streamConfigInfo.setSourceAddress(tempSourceAddress);

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
		if (streamConversion != null) {
			streamConfigInfo.setStreamConversion(streamConversion);
			streamConfigInfo.setStreamFlipping(streamConversion.getStreamFlipping());
		}

		// map value to DTO
		streamConfigInfo.setId(stream.getStreamId());
		streamConfigInfo.setName(stream.getStreamName());
		String port = getDefaultValueForNullData(streamConfigInfo.getPort(), DecoderConstant.EMPTY);
		if (port.isEmpty()) {
			streamConfigInfo.setPort(getDefaultValueForNullData(streamConfigInfo.getDestinationPort(), DecoderConstant.EMPTY));
		}
		String tempAddress = getDefaultValueForNullData(streamConfigInfo.getAddress(), DecoderConstant.EMPTY);
		String tempDestinationAddress = NormalizeData.extractNumbersFromDataBySpaceIndex(tempAddress, DecoderConstant.ADDRESS_DATA_INDEX);
		String tempSourceAddress = NormalizeData.extractNumbersFromDataBySpaceIndex(tempAddress, DecoderConstant.SOURCE_ADDRESS_DATA_INDEX);
		if (tempDestinationAddress.isEmpty()) {
			tempDestinationAddress = NormalizeData.getAddressFromRawData(tempAddress);
		}
		if (tempSourceAddress.isEmpty()) {
			tempSourceAddress = NormalizeData.extractDataBySpaceIndex(tempAddress, DecoderConstant.SOURCE_ADDRESS_DATA_INDEX);
			tempSourceAddress = NormalizeData.getAddressFromRawData(tempSourceAddress);
		}
		streamConfigInfo.setDestinationAddress(tempDestinationAddress);
		streamConfigInfo.setSourceAddress(tempSourceAddress);

		Optional<StreamConfig> realtimeStreamConfig = this.realtimeStreamConfigsFiltered.stream().filter(st -> streamID.toString().equals(st.getId())).findFirst();
		Optional<StreamConfig> cachedStreamConfig = this.cachedStreamConfigsFiltered.stream().filter(st -> streamID.toString().equals(st.getId())).findFirst();

		// update cachedStreamConfigs when stream config info is not edited on symphony but is edited on real device
		if (cachedStreamConfig.isPresent() && realtimeStreamConfig.isPresent() && cachedStreamConfig.get().equals(realtimeStreamConfig.get()) && !realtimeStreamConfig.get().equals(streamConfigInfo)) {
			this.cachedStreamConfigsFiltered.remove(cachedStreamConfig.get());
			this.cachedStreamConfigsFiltered.add(new StreamConfig(streamConfigInfo));
		}
		realtimeStreamConfig.ifPresent(config -> this.realtimeStreamConfigsFiltered.remove(config));
		this.realtimeStreamConfigsFiltered.add(streamConfigInfo);
		filteredStreamIDSet.add(streamID);
	}

	/**
	 * This method is used update realtime stream config info list from DTO
	 *
	 * @param streamStatsWrapper pair of stream config and stats
	 */
	private void updateRealtimeStreamConfig(StreamStatsWrapper streamStatsWrapper) {
		StreamConfig streamConfigInfo = streamStatsWrapper.getStreamConfig();
		Stream stream = streamStatsWrapper.getStream();
		streamConfigInfo.setId(stream.getStreamId());
		streamConfigInfo.setName(stream.getStreamName());
		String port = getDefaultValueForNullData(streamConfigInfo.getPort(), DecoderConstant.EMPTY);
		if (port.isEmpty()) {
			streamConfigInfo.setPort(getDefaultValueForNullData(streamConfigInfo.getDestinationPort(), DecoderConstant.EMPTY));
		}
		String tempAddress = getDefaultValueForNullData(streamConfigInfo.getAddress(), DecoderConstant.EMPTY);
		String tempDestinationAddress = NormalizeData.extractNumbersFromDataBySpaceIndex(tempAddress, DecoderConstant.ADDRESS_DATA_INDEX);
		String tempSourceAddress = NormalizeData.extractNumbersFromDataBySpaceIndex(tempAddress, DecoderConstant.SOURCE_ADDRESS_DATA_INDEX);
		if (tempDestinationAddress.isEmpty()) {
			tempDestinationAddress = NormalizeData.getAddressFromRawData(tempAddress);
		}
		if (tempSourceAddress.isEmpty()) {
			tempSourceAddress = NormalizeData.extractDataBySpaceIndex(tempAddress, DecoderConstant.SOURCE_ADDRESS_DATA_INDEX);
			tempSourceAddress = NormalizeData.getAddressFromRawData(tempSourceAddress);
		}
		streamConfigInfo.setDestinationAddress(tempDestinationAddress);
		streamConfigInfo.setSourceAddress(tempSourceAddress);
		realtimeStreamConfigs.add(streamConfigInfo);
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
	public boolean isValidPortRange(Integer portNumber) {
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

	/**
	 * This method is used to validate input config management from user
	 *
	 * @return boolean is configManagement
	 */
	public boolean isValidConfigManagement() {
		if (isConfigManagement != null) {
			return isConfigManagement;
		}
		isConfigManagement = !StringUtils.isNullOrEmpty(this.configManagement) && this.configManagement.equalsIgnoreCase(DecoderConstant.TRUE_VALUE);
		return isConfigManagement;
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region retrieve Audio/ HDMI/ Service/ Talkback config
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used to retrieve audio by send command "auddec get all"
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	public void retrieveAudioConfig() {
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
				} else {
					AudioConfig rawAudioConfig = audioConfigWrapper.getAudioConfig();
					String rawAudioConfigOutputSource = rawAudioConfig.getOutputSource();
					String[] rawOutputLevel = rawAudioConfig.getOutputLevel().split(DecoderConstant.SPACE);
					rawAudioConfig.setOutputLevel(rawOutputLevel[0]);
					// Example format of rawAudioConfigOutputSource: SDI1Ch3+4. Source is SDI1, audio channel: Ch3+4
					rawAudioConfig.setSource(rawAudioConfigOutputSource.substring(0, 4));
					// Formatted channel: Ch3+4 => 3&4 (remove 'Ch' and replace '+' with '&').
					String formattedChannel = rawAudioConfigOutputSource.substring(4).replace(DecoderConstant.TARGET_CH, DecoderConstant.EMPTY)
							.replace(DecoderConstant.PLUS_SIGN, DecoderConstant.AND_SIGN);
					rawAudioConfig.setChannels(formattedChannel);
					// Formatted output source: SDI1CH12
					rawAudioConfig.setOutputSource(rawAudioConfig.getOutputSource().replace(DecoderConstant.PLUS_SIGN, DecoderConstant.EMPTY));

					// update cachedAudioConfig when audio config info is not edited on symphony but is edited on real device
					if (cachedAudioConfig != null && cachedAudioConfig.equals(realtimeAudioConfig) && !realtimeAudioConfig.equals(rawAudioConfig)) {
						cachedAudioConfig = new AudioConfig(rawAudioConfig);
					}
					realtimeAudioConfig = new AudioConfig(rawAudioConfig);
				}
			}
		} catch (Exception e) {
			logger.error("Error while retrieve audio statistics: ", e);
			updateFailedMonitor(CommandOperation.OPERATION_AUDDEC.getName(), DecoderConstant.GETTING_AUDIO_CONFIG_ERR +
					DecoderConstant.COMMA + DecoderConstant.SPACE + e.getMessage());
		}
	}

	/**
	 * This method is used to retrieve hdmi by send command "hdmi get all"
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveHDMIConfig() {
		try {
			String request = CommandOperation.OPERATION_HDMI.getName()
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.GET.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.ALL.getName());

			String response = send(request);
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				HDMIConfigWrapper hdmiConfigWrapper = objectMapper.convertValue(responseMap, HDMIConfigWrapper.class);

				if (hdmiConfigWrapper == null) {
					updateFailedMonitor(CommandOperation.OPERATION_AUDDEC.getName(), DecoderConstant.GETTING_HDMI_CONFIG_ERR);
				} else {
					HDMIConfig hdmiConfig = hdmiConfigWrapper.getHdmiConfig();

					// update cachedHDMIConfig when hdmi config info is not edited on symphony but is edited on real device
					if (cachedHDMIConfig != null && cachedHDMIConfig.equals(realtimeHDMIConfig) && !realtimeHDMIConfig.equals(hdmiConfig)) {
						cachedHDMIConfig = new HDMIConfig(hdmiConfig);
					}
					realtimeHDMIConfig = new HDMIConfig(hdmiConfig);
				}
			}
		} catch (Exception e) {
			logger.error("Error while retrieve hdmi statistics: ", e);
			updateFailedMonitor(CommandOperation.OPERATION_AUDDEC.getName(), DecoderConstant.GETTING_HDMI_CONFIG_ERR +
					DecoderConstant.COMMA + DecoderConstant.SPACE + e.getMessage());
		}
	}

	/**
	 * This method is used to retrieve service by send command "service all status"
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	private void retrieveServiceConfig() {
		try {
			String request = CommandOperation.OPERATION_SERVICE.getName().
					concat(DecoderConstant.SPACE).
					concat(CommandOperation.ALL.getName()).
					concat(DecoderConstant.SPACE).
					concat(CommandOperation.OPERATION_STATUS.getName());
			String response = send(request);

			if (response != null) {

				// response eg: "service all status\r\n ems service is currently enabled\r\n ems service is enabled at system startup\r\n"
				String[] splitResponses = response.split(DecoderConstant.REGEX_TRAILING_OF_FIELD);
				List<String> responses = Arrays.stream(splitResponses).filter(s -> s.contains(DecoderConstant.CURRENTLY)).collect(Collectors.toList());
				String rawResponse = DecoderConstant.SERVICE_OBJECT_JSON_ALIAS;

				//list of split response eg: ["ems service is currently enabled", "ssh service is currently enabled"]
				for (String responsesElement : responses) {
					responsesElement = responsesElement.replace(DecoderConstant.SERVICE_STRING_REPLACED, DecoderConstant.COLON);

					//response converted eg: "serviceallstatus\r\n ems: enabled\r\n ssh: enabled\r\n"
					rawResponse = rawResponse.concat(responsesElement).concat(DecoderConstant.REGEX_TRAILING_OF_FIELD);
				}
				Map<String, Object> responseMap = Deserializer.convertDataToObject(rawResponse, request);
				ServiceConfigWrapper serviceConfigWrapper = objectMapper.convertValue(responseMap, ServiceConfigWrapper.class);

				if (serviceConfigWrapper == null) {
					updateFailedMonitor(CommandOperation.OPERATION_SERVICE.getName(), DecoderConstant.GETTING_SERVICE_CONFIG_ERR);
				} else {
					ServiceConfig serviceConfig = serviceConfigWrapper.getServiceConfig();
					realtimeServiceConfig = new ServiceConfig(serviceConfig);
				}
			}
		} catch (Exception e) {
			logger.error("Error while retrieve service statistics: ", e);
			updateFailedMonitor(CommandOperation.OPERATION_SERVICE.getName(), DecoderConstant.GETTING_SERVICE_CONFIG_ERR +
					DecoderConstant.COMMA + DecoderConstant.SPACE + e.getMessage());
		}
	}

	/**
	 * This method is used to retrieve talkback by send command "talkback get all"
	 *
	 * When there is no response data, the failedMonitor is going to update
	 * When there is an exception, the failedMonitor is going to update and exception is not populated
	 */
	public void retrieveTalkback() {
		try {
			String request = CommandOperation.OPERATION_TALKBACK.getName()
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.GET.getName())
					.concat(DecoderConstant.SPACE)
					.concat(CommandOperation.ALL.getName());

			String response = send(request);
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.convertDataToObject(response, request);
				TalkBackWrapper talkBackConfigWrapper = objectMapper.convertValue(responseMap, TalkBackWrapper.class);

				if (talkBackConfigWrapper == null) {
					updateFailedMonitor(CommandOperation.OPERATION_TALKBACK.getName(), DecoderConstant.GETTING_TALKBACK_CONFIG_ERR);
				} else {
					TalkBackConfig talkBackConfig = talkBackConfigWrapper.getTalkBackConfig();
					TalkBackStats talkBackStats = talkBackConfigWrapper.getTalkBackStats();
					talkBackConfig.setState(getDefaultValueForNullData(talkBackStats.getState(), DecoderConstant.EMPTY));

					// update cachedTalkBackConfig when talkback config info is not edited on symphony but is edited on real device
					if (cachedTalkbackConfig != null && cachedTalkbackConfig.equals(realtimeTalkbackConfig) && !realtimeTalkbackConfig.equals(talkBackConfig)) {
						cachedTalkbackConfig = new TalkBackConfig(talkBackConfig);
					}
					realtimeTalkbackConfig = new TalkBackConfig(talkBackConfig);
				}
			}
		} catch (Exception e) {
			logger.error("Error while retrieve talkback statistics: ", e);
			updateFailedMonitor(CommandOperation.OPERATION_TALKBACK.getName(), DecoderConstant.GETTING_TALKBACK_CONFIG_ERR +
					DecoderConstant.COMMA + DecoderConstant.SPACE + e.getMessage());
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
			if (this.realtimeStreamConfigs != null) {
				for (StreamConfig cachedStreamInfo : realtimeStreamConfigs) {
					if (primaryStreamID.equals(cachedStreamInfo.getId())) {
						primaryStreamName = cachedStreamInfo.getName();
						if (DecoderConstant.DEFAULT_STREAM_NAME.equals(primaryStreamName)) {
							primaryStreamName = cachedStreamInfo.getDefaultStreamName();
						}
						break;
					}
				}
			}

			String secondaryStreamID = getDefaultValueForNullData(cachedDecoderConfig.getSecondaryStream(), DecoderConstant.DEFAULT_STREAM_NAME);
			String secondaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
			if (this.realtimeStreamConfigs != null) {
				for (StreamConfig cachedStreamInfo : realtimeStreamConfigs) {
					if (secondaryStreamID.equals(cachedStreamInfo.getId())) {
						secondaryStreamName = cachedStreamInfo.getName();
						if (DecoderConstant.DEFAULT_STREAM_NAME.equals(secondaryStreamName)) {
							secondaryStreamName = cachedStreamInfo.getDefaultStreamName();
						}
						break;
					}
				}
			}

			String stillImageDelay = getDefaultValueForNullData(NormalizeData.extractNumbers(cachedDecoderConfig.getStillImageDelay()), DecoderConstant.DEFAULT_STILL_IMAGE_DELAY.toString());
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
			if (this.realtimeStreamConfigs != null) {
				for (StreamConfig streamConfig : realtimeStreamConfigs) {
					if (!DecoderConstant.DEFAULT_STREAM_NAME.equals(streamConfig.getName())) {
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
					createSwitch(stats, decoderControllingGroup + DecoderControllingMetric.SYNC_MODE.getName(), enableBuffering.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
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

					bufferingDelay = getDefaultValueForNullData(NormalizeData.extractNumbers(cachedDecoderConfig.getBufferingDelay()), DecoderConstant.MIN_BUFFERING_DELAY.toString());
					try {
						bufferingDelayIntValue = Integer.parseInt(bufferingDelay);
						if (bufferingDelayIntValue < DecoderConstant.MIN_BUFFERING_DELAY) {
							bufferingDelayIntValue = DecoderConstant.MIN_BUFFERING_DELAY;
						}
						if (bufferingDelayIntValue > DecoderConstant.MAX_BUFFERING_DELAY) {
							bufferingDelayIntValue = DecoderConstant.MAX_BUFFERING_DELAY;
						}
					} catch (Exception e) {
						if (bufferingDelay.contains(DecoderConstant.DASH)) {
							bufferingDelayIntValue = DecoderConstant.MIN_BUFFERING_DELAY;
						} else {
							bufferingDelayIntValue = DecoderConstant.MAX_BUFFERING_DELAY;
						}
						if (logger.isWarnEnabled()) {
							logger.warn("Invalid buffering delay value", e);
						}
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

					bufferingDelay = getDefaultValueForNullData(NormalizeData.extractNumbers(cachedDecoderConfig.getBufferingDelay()), DecoderConstant.DEFAULT_MULTI_SYNC_BUFFERING_DELAY.toString());
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
			String applyChange = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.APPLY_CHANGE.getName();
			String cancel = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.CANCEL.getName();
			boolean isEdited = isEditedForDecoderSDI1;
			if (decoderID.equals(2)) {
				isEdited = isEditedForDecoderSDI2;
			}
			if (isEdited) {
				stats.put(ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.EDITED.getName(), DecoderConstant.TRUE_VALUE);
				stats.put(applyChange, DecoderConstant.EMPTY);
				stats.put(cancel, DecoderConstant.EMPTY);
				addAdvanceControlProperties(advancedControllableProperties, createButton(applyChange, DecoderConstant.APPLY, DecoderConstant.APPLYING));
				addAdvanceControlProperties(advancedControllableProperties, createButton(cancel, DecoderConstant.CANCEL, DecoderConstant.CANCELLING));
			} else {
				stats.put(ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH + DecoderControllingMetric.EDITED.getName(), DecoderConstant.FALSE_VALUE);
				List<String> listKeyToBeRemove = new ArrayList<>();
				listKeyToBeRemove.add(applyChange);
				listKeyToBeRemove.add(cancel);
				removeUnusedStatsAndControls(stats, advancedControllableProperties, listKeyToBeRemove);
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
	 * @throws ResourceNotReachableException when start/ stop/ update decoder SDI is failed
	 */
	private void decoderControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, Integer decoderID, String controllableProperty, String value) {
		DecoderControllingMetric decoderControllingMetric = DecoderControllingMetric.getByName(controllableProperty);
		Optional<DecoderConfig> cachedDecoderConfigOptional = this.cachedDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
		if (cachedDecoderConfigOptional.isPresent()) {
			DecoderConfig cachedDecoderConfig = cachedDecoderConfigOptional.get();

			List<String> streamNames = new ArrayList<>();
			streamNames.add(DecoderConstant.DEFAULT_STREAM_NAME);
			if (this.realtimeStreamConfigs != null) {
				for (StreamConfig streamConfig : realtimeStreamConfigs) {
					if (!DecoderConstant.DEFAULT_STREAM_NAME.equals(streamConfig.getName())) {
						streamNames.add(streamConfig.getName());
					} else {
						streamNames.add(streamConfig.getDefaultStreamName());
					}
				}
			}

			String decoderControllingGroup = ControllingMetricGroup.DECODER_SDI.getUiName() + decoderID + DecoderConstant.HASH;

			isEmergencyDelivery = true;
			if (decoderID.equals(2)) {
				isEditedForDecoderSDI2 = true;
			}
			if (decoderID.equals(1)) {
				isEditedForDecoderSDI1 = true;
			}
			switch (decoderControllingMetric) {
				case PRIMARY_STREAM:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					String primaryStreamID = DecoderConstant.DEFAULT_STREAM_NAME;
					String primaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
					for (StreamConfig cachedStreamConfig : realtimeStreamConfigs) {
						if (!DecoderConstant.DEFAULT_STREAM_NAME.equals(value) && (value.equals(cachedStreamConfig.getName()) || value.equals(cachedStreamConfig.getDefaultStreamName()))) {
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
					break;
				case SECONDARY_STREAM:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					String secondaryStreamID = DecoderConstant.DEFAULT_STREAM_NAME;
					String secondaryStreamName = DecoderConstant.DEFAULT_STREAM_NAME;
					for (StreamConfig cachedStreamConfig : realtimeStreamConfigs) {
						if (!DecoderConstant.DEFAULT_STREAM_NAME.equals(value) && (value.equals(cachedStreamConfig.getName()) || value.equals(cachedStreamConfig.getDefaultStreamName()))) {
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
					break;
				case STILL_IMAGE:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);
					String stillImage = StillImage.getByUIName(value).getApiName();

					cachedDecoderConfig.setStillImage(stillImage);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					break;
				case SELECT_STILL_IMAGE:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					Optional<String> customStillImageOptional = customStillImages.stream().filter(st -> st.equals(value)).findFirst();

					customStillImageOptional.ifPresent(cachedDecoderConfig::setStillFile);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					break;
				case STILL_IMAGE_DELAY:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);
					Integer stillImageDelay;
					try {
						stillImageDelay = Integer.parseInt(value);
						if (stillImageDelay < DecoderConstant.MIN_STILL_IMAGE_DELAY) {
							stillImageDelay = DecoderConstant.MIN_STILL_IMAGE_DELAY;
						}
						if (stillImageDelay > DecoderConstant.MAX_STILL_IMAGE_DELAY) {
							stillImageDelay = DecoderConstant.MAX_STILL_IMAGE_DELAY;
						}
					} catch (Exception e) {
						if (value.contains(DecoderConstant.DASH)) {
							stillImageDelay = DecoderConstant.MIN_STILL_IMAGE_DELAY;
						} else {
							stillImageDelay = DecoderConstant.MAX_STILL_IMAGE_DELAY;
						}
						if (logger.isWarnEnabled()) {
							logger.warn(String.format("%s is invalid still image delay value, the still image delay must be integer value range to 1-1000", value), e);
						}
					}

					cachedDecoderConfig.setStillImageDelay(stillImageDelay.toString());
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					addAdvanceControlProperties(advancedControllableProperties,
							createNumeric(stats, decoderControllingGroup + DecoderControllingMetric.STILL_IMAGE_DELAY.getName(), stillImageDelay.toString()));
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					break;
				case SYNC_MODE:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					boolean isEnableSyncMode = convertSwitchControlValue(value);
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
					break;
				case BUFFERING_MODE:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					BufferingMode bufferingMode = BufferingMode.getByUiName(value);

					cachedDecoderConfig.setBufferingMode(bufferingMode.getApiName());
					cachedDecoderConfig.setBufferingDelay(DecoderConstant.EMPTY);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControlBufferingMode(stats, advancedControllableProperties, cachedDecoderConfig, decoderID);
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					break;
				case BUFFERING_DELAY:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					cachedDecoderConfig.setBufferingDelay(value);
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControlBufferingMode(stats, advancedControllableProperties, cachedDecoderConfig, decoderID);
					populateApplyChangeAndCancelButtonForDecoder(stats, advancedControllableProperties, decoderID);
					break;

				case OUTPUT_FRAME_RATE:
					cachedDecoderConfigs.remove(cachedDecoderConfig);

					OutputFrameRate outputFrameRate = OutputFrameRate.getByUIName(value);

					cachedDecoderConfig.setOutputFrameRate(outputFrameRate.getApiName());
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					break;
				case OUTPUT_RESOLUTION:
					this.cachedDecoderConfigs.remove(cachedDecoderConfig);

					OutputResolution outputResolutionControl = OutputResolution.getByUIName(value);

					cachedDecoderConfig.setOutputResolution(outputResolutionControl.getApiStatsName());
					this.cachedDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));

					populateDecoderControl(stats, advancedControllableProperties, decoderID);
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
							if (decoderID.equals(2)) {
								isEditedForDecoderSDI2 = false;
							}
							if (decoderID.equals(1)) {
								isEditedForDecoderSDI1 = false;
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
							if (decoderID.equals(2)) {
								isEditedForDecoderSDI2 = false;
							}
							if (decoderID.equals(1)) {
								isEditedForDecoderSDI1 = false;
							}
							populateDecoderControl(stats, advancedControllableProperties, decoderID);
							break;
						default:
							if (logger.isWarnEnabled()) {
								logger.warn(String.format("Decoder state %s is not supported.", state.getName()));
							}
							break;
					}
					isEmergencyDelivery = false;
					break;
				case APPLY_CHANGE:
					performActiveDecoderSDIControl(cachedDecoderConfig, decoderID, CommandOperation.SET);

					realtimeDecoderConfigOptional = this.realtimeDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();
					realtimeDecoderConfigOptional.ifPresent(config -> {
						this.realtimeDecoderConfigs.remove(config);
						this.realtimeDecoderConfigs.add(new DecoderConfig(cachedDecoderConfig));
					});
					if (decoderID.equals(2)) {
						isEditedForDecoderSDI2 = false;
					}
					if (decoderID.equals(1)) {
						isEditedForDecoderSDI1 = false;
					}
					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					isEmergencyDelivery = false;
					break;
				case CANCEL:
					realtimeDecoderConfigOptional = this.realtimeDecoderConfigs.stream().filter(st -> decoderID.toString().equals(st.getDecoderID())).findFirst();

					realtimeDecoderConfigOptional.ifPresent(config -> {
						this.cachedDecoderConfigs.remove(cachedDecoderConfig);
						this.cachedDecoderConfigs.add(new DecoderConfig(config));
					});
					if (decoderID.equals(2)) {
						isEditedForDecoderSDI2 = false;
					}
					if (decoderID.equals(1)) {
						isEditedForDecoderSDI1 = false;
					}
					populateDecoderControl(stats, advancedControllableProperties, decoderID);
					isEmergencyDelivery = false;
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
			throw new ResourceNotReachableException(DecoderConstant.DECODER_CONTROL_ERR + e.getMessage(), e);
		}
	}

	/**
	 * This method is used to add escape in front of special character
	 *
	 * @param input string input
	 * @return String converted
	 */
	private String escapeSpecialCharacters(String input) {
		List<String> specialCharacters = new ArrayList<>(Arrays.asList(DecoderConstant.LEFT_PARENTHESES, DecoderConstant.RIGHT_PARENTHESES, DecoderConstant.AND_SIGN));
		return Arrays.stream(input.split(DecoderConstant.EMPTY)).map(c -> {
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
	 * @param streamGroup is the stream group name
	 */
	private void populateCreateStreamControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig streamConfig, String streamGroup) {
		// Get controllable property current value
		Encapsulation encapsulation = Encapsulation.getByApiConfigName(getDefaultValueForNullData(streamConfig.getEncapsulation(), DecoderConstant.EMPTY));
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
	 * @param streamGroup is the stream group name
	 */
	private void populateCreateStreamControlCaseTSOverUDP(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		// Get controllable property current value
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		Fec fecFromApi = Fec.getByAPIStatsName(getDefaultValueForNullData(cachedStreamConfig.getFec(), DecoderConstant.EMPTY));
		Fec fecFromCurrentFecModes = Fec.DISABLE;

		// Get list values of controllable property (dropdown list)
		List<String> fecModes = DropdownList.getListOfEnumNames(Fec.class, Fec.MPEG_PRO_FEC);

		for (String fecElement : fecModes) {
			if (fecElement.equals(fecFromApi.getUiName())) {
				fecFromCurrentFecModes = fecFromApi;
				break;
			}
		}
		createStream.setFec(fecFromCurrentFecModes.getApiStatsName());

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.PORT.getName(), port));
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, streamGroup + StreamControllingMetric.FEC.getName(), fecModes, fecFromCurrentFecModes.getUiName()));

		populateNetWorkTypeStreamControl(stats, advancedControllableProperties, cachedStreamConfig, streamGroup);
	}

	/**
	 * This method is used for populate all create stream control properties:
	 * <li>Protocol: TS over RTP</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param cachedStreamConfig stream config info
	 * @param streamGroup is the stream group name
	 */
	private void populateCreateStreamControlCaseTSOverRTP(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		// Get controllable property current value
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		Fec fecFromApi = Fec.getByAPIStatsName(getDefaultValueForNullData(cachedStreamConfig.getFec(), DecoderConstant.EMPTY));
		Fec fecFromCurrentFecModes = Fec.DISABLE;

		// Get list values of controllable property (dropdown list)
		List<String> fecModes = DropdownList.getListOfEnumNames(Fec.class, Fec.VF);

		for (String fecElement : fecModes) {
			if (fecElement.equals(fecFromApi.getUiName())) {
				fecFromCurrentFecModes = fecFromApi;
				break;
			}
		}
		createStream.setFec(fecFromCurrentFecModes.getApiStatsName());

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.PORT.getName(), port));
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, streamGroup + StreamControllingMetric.FEC.getName(), fecModes, fecFromCurrentFecModes.getUiName()));

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
	 * @param streamGroup is the stream group name
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
	 * @param streamGroup is the stream group name
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
	 * @param streamGroup is the stream group name
	 */
	private void populateStreamCreateControlCaseTSOverSRTListener(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, streamGroup + StreamControllingMetric.PORT.getName(), cachedStreamConfig.getPort()));

		SwitchOnOffControl aeEncrypted = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(cachedStreamConfig.getSrtSettings(), DecoderConstant.EMPTY));
		if (aeEncrypted.isEnable()) {
			RejectUnencrypted rejectUnencrypted = RejectUnencrypted.getByApiName(
					getDefaultValueForNullData(cachedStreamConfig.getRejectUnencrypted(), RejectUnencrypted.ENABLE_REJECT_UNENCRYPTED.getApiName()));
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
	 * @param streamGroup is the stream group name
	 */
	private void populateCreateStreamControlCaseTSOverSRTStreamConversion(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		// Get controllable property current value
		StreamConversion streamConversion = cachedStreamConfig.getStreamConversion();
		SwitchOnOffControl streamFlipping = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(cachedStreamConfig.getStreamFlipping(), DecoderConstant.EMPTY));

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties, createSwitch(stats, streamGroup + StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName(), streamFlipping.getCode(),
				DecoderConstant.DISABLE, DecoderConstant.ENABLE));

		if (streamConversion != null) {
			streamFlipping = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(streamConversion.getStreamFlipping(), DecoderConstant.EMPTY));
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
	 * @param streamGroup is the stream group name
	 */
	private void populateCreateStreamControlCaseTSOverSRTEncrypted(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		SwitchOnOffControl aeEncrypted = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(cachedStreamConfig.getSrtSettings(), DecoderConstant.EMPTY));
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
	 * @param streamGroup is the stream group name
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
	 * @param streamGroup is the stream group name
	 */
	private void populateCreateStreamControlCaseTSOverSRTRendezvous(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig,
			String streamGroup) {
		// Get properties current value
		String address = getDefaultValueForNullData(cachedStreamConfig.getDestinationAddress(), DecoderConstant.EMPTY);
		if (address.equals(DecoderConstant.ADDRESS_ANY)) {
			address = DecoderConstant.EMPTY;
		}
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
	 * @param streamGroup is the stream group name
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
		String cancel = ControllingMetricGroup.CREATE_STREAM.getUiName() + DecoderConstant.HASH + StreamControllingMetric.CANCEL.getName();

		if (isEditedForCreateStream) {
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
		streamConfig.setEncapsulation(Encapsulation.TS_OVER_UDP.getApiConfigName());
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
	 * @param createStreamControllingGroup is the group name of create stream controllable property
	 * @param controllableProperty name of controllable property
	 * @param value value of controllable property
	 * @throws ResourceNotReachableException when fail to create stream
	 */
	private void createStreamControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, String createStreamControllingGroup,
			String controllableProperty, String value) {
		StreamControllingMetric streamControllingMetric = StreamControllingMetric.getByName(controllableProperty);

		isEmergencyDelivery = true;
		isEditedForCreateStream = true;
		switch (streamControllingMetric) {
			case STREAM_NAME:
				createStream.setName(value);

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, createStreamControllingGroup + StreamControllingMetric.STREAM_NAME.getName(), createStream.getName()));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case ENCAPSULATION:
				Encapsulation encapsulationEnum = Encapsulation.getByUiName(value);
				removeUnusedStatsAndControlByProtocol(stats, advancedControllableProperties, createStream, createStreamControllingGroup);
				createStream.setEncapsulation(encapsulationEnum.getApiConfigName());

				populateCreateStreamControl(stats, advancedControllableProperties, createStream, createStreamControllingGroup);

				break;
			case NETWORK_TYPE:
				NetworkType networkType = NetworkType.getByUiName(value);
				removeUnusedStatsAndControlByNetworkType(stats, advancedControllableProperties, createStream, createStreamControllingGroup);
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

				populateCreateStreamControl(stats, advancedControllableProperties, createStream, createStreamControllingGroup);
				break;
			case PORT:
				String port;
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
					if (value.contains(DecoderConstant.DASH)) {
						port = DecoderConstant.MIN_PORT.toString();
					} else {
						port = DecoderConstant.MAX_PORT.toString();
					}
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("%s is invalid port value, the port value must be Integer value range to 1025-65535 ", value), e);
					}
				}
				createStream.setPort(port);
				createStream.setDestinationPort(port);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, createStreamControllingGroup + StreamControllingMetric.PORT.getName(), port));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case ADDRESS:
				createStream.setDestinationAddress(value);

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, createStreamControllingGroup + StreamControllingMetric.ADDRESS.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case MULTICAST_ADDRESS:
				createStream.setDestinationAddress(value);

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, createStreamControllingGroup + StreamControllingMetric.MULTICAST_ADDRESS.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case SOURCE_ADDRESS:
				createStream.setSourceAddress(value);

				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, createStreamControllingGroup + StreamControllingMetric.SOURCE_ADDRESS.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case SOURCE_PORT:
				String sourcePort;
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
					if (value.contains(DecoderConstant.DASH)) {
						sourcePort = DecoderConstant.MIN_PORT.toString();
					} else {
						sourcePort = DecoderConstant.MAX_PORT.toString();
					}
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("%s is invalid port value, the port value must be Integer value range to 1025-65535", value), e);
					}
				}
				createStream.setSourcePort(sourcePort);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, createStreamControllingGroup + StreamControllingMetric.SOURCE_PORT.getName(), sourcePort));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case DESTINATION_PORT:
				String destinationPort;
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
					if (value.contains(DecoderConstant.DASH)) {
						destinationPort = DecoderConstant.MIN_PORT.toString();
					} else {
						destinationPort = DecoderConstant.MAX_PORT.toString();
					}
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("%s is invalid port value, the port value must be Integer value range to 1025-65535", value), e);
					}
				}
				createStream.setPort(destinationPort);
				createStream.setDestinationPort(destinationPort);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, createStreamControllingGroup + StreamControllingMetric.DESTINATION_PORT.getName(), destinationPort));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case FEC:
				Fec fec = Fec.getByUiName(value);
				createStream.setFec(fec.getApiStatsName());

				populateCreateStreamControl(stats, advancedControllableProperties, createStream, createStreamControllingGroup);
				break;
			case SRT_MODE:
				SRTMode srtMode = SRTMode.getByName(value);
				removeUnusedStatsAndControlBySRTMode(stats, advancedControllableProperties, createStream, createStreamControllingGroup);

				createStream.setSrtMode(srtMode.getUiName());
				populateCreateStreamControl(stats, advancedControllableProperties, createStream, createStreamControllingGroup);

				break;
			case LATENCY:
				String latency;
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
					if (value.contains(DecoderConstant.DASH)) {
						latency = DecoderConstant.MIN_LATENCY.toString();
					} else {
						latency = DecoderConstant.MAX_LATENCY.toString();
					}
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("%s is invalid latency value, the latency value must be Integer value range to 20-8000", value), e);
					}
				}
				createStream.setLatency(latency);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, createStreamControllingGroup + StreamControllingMetric.LATENCY.getName(), createStream.getLatency()));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case SRT_TO_UDP_STREAM_CONVERSION:
				StreamConversion streamConversion = createStream.getStreamConversion();
				SwitchOnOffControl streamFlipping = SwitchOnOffControl.getByCode(Integer.parseInt(value));
				if (streamConversion == null) {
					streamConversion = new StreamConversion();
					if (streamFlipping.isEnable()) {
						streamConversion.setAddress(DecoderConstant.EMPTY);
						streamConversion.setTos(DecoderConstant.DEFAULT_TOS);
						streamConversion.setAddress(DecoderConstant.EMPTY);
						streamConversion.setTtl(DecoderConstant.DEFAULT_TTL.toString());
						streamConversion.setUdpPort(DecoderConstant.EMPTY);
					}
				}
				removeUnusedStatsAndControlByStreamConversion(stats, advancedControllableProperties, createStream, createStreamControllingGroup);
				streamConversion.setStreamFlipping(streamFlipping.getApiName());
				createStream.setStreamConversion(streamConversion);
				createStream.setStreamFlipping(streamFlipping.getApiName());

				populateCreateStreamControl(stats, advancedControllableProperties, createStream, createStreamControllingGroup);
				break;
			case SRT_TO_UDP_ADDRESS:
				streamConversion = createStream.getStreamConversion();
				streamConversion.setAddress(value);
				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, createStreamControllingGroup + StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case SRT_TO_UDP_PORT:
				streamConversion = createStream.getStreamConversion();
				String srtToUDPPort;
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
					if (value.contains(DecoderConstant.DASH)) {
						srtToUDPPort = DecoderConstant.MIN_PORT.toString();
					} else {
						srtToUDPPort = DecoderConstant.MAX_PORT.toString();
					}
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("%s is invalid port value, the port value must be Integer value range to 1025-65535", value), e);
					}
				}
				streamConversion.setUdpPort(srtToUDPPort);
				createStream.setStreamConversion(streamConversion);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, createStreamControllingGroup + StreamControllingMetric.SRT_TO_UDP_PORT.getName(), srtToUDPPort));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
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
							createText(stats, createStreamControllingGroup + StreamControllingMetric.SRT_TO_UDP_TOS.getName(), tosHexValue));
					populateCancelButtonForCreateStream(stats, advancedControllableProperties);
					break;
				} catch (Exception var60) {
					throw new NumberFormatException(String.format("%s is invalid ParameterToS value. TOS must be hex value range to 00-FF", value));
				}
			case SRT_TO_UDP_TTL:
				streamConversion = createStream.getStreamConversion();
				String ttl = value;
				try {
					int ttlIntValue = Integer.parseInt(value);
					if (ttlIntValue < DecoderConstant.MIN_TTL) {
						ttl = DecoderConstant.MIN_TTL.toString();
					}
					if (ttlIntValue > DecoderConstant.MAX_TTL) {
						ttl = DecoderConstant.MAX_TTL.toString();
					}
				} catch (Exception e) {
					if (value.contains(DecoderConstant.DASH)) {
						ttl = DecoderConstant.MIN_TTL.toString();
					} else {
						ttl = DecoderConstant.MAX_TTL.toString();
					}
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("%s is invalid ttl value, the ttl value must be Integer value range to 1-255", value), e);
					}
				}
				streamConversion.setTtl(ttl);
				createStream.setStreamConversion(streamConversion);

				addAdvanceControlProperties(advancedControllableProperties,
						createNumeric(stats, createStreamControllingGroup + StreamControllingMetric.SRT_TO_UDP_TTL.getName(), ttl));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case ENCRYPTED:
				SwitchOnOffControl srtSettingEnum = SwitchOnOffControl.getByCode(Integer.parseInt(value));
				removeUnusedStatsAndControlByEncrypted(stats, advancedControllableProperties, createStream, createStreamControllingGroup);

				createStream.setSrtSettings(srtSettingEnum.getApiName());
				populateCreateStreamControl(stats, advancedControllableProperties, createStream, createStreamControllingGroup);
				break;
			case PASSPHRASE:
				createStream.setPassphrase(value);
				addAdvanceControlProperties(advancedControllableProperties,
						createText(stats, createStreamControllingGroup + StreamControllingMetric.PASSPHRASE.getName(), value));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case REJECT_UNENCRYPTED_CALLERS:
				RejectUnencrypted rejectUnencryptedCallers = RejectUnencrypted.getByCode(Integer.parseInt(value));
				createStream.setRejectUnencrypted(rejectUnencryptedCallers.getApiName());

				addAdvanceControlProperties(advancedControllableProperties, createSwitch(
						stats, createStreamControllingGroup + StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName(), rejectUnencryptedCallers.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
				populateCancelButtonForCreateStream(stats, advancedControllableProperties);
				break;
			case RTSP_URL:
				createStream.setAddress(value);
				populateCreateStreamControl(stats, advancedControllableProperties, createStream, createStreamControllingGroup);
				break;
			case CREATE:
				performCreateStreamControl();
				createStream = defaultStream();
				isEditedForCreateStream = false;
				populateCreateStreamControl(stats, advancedControllableProperties, defaultStream(), createStreamControllingGroup);
				isEmergencyDelivery = false;
				break;
			case CANCEL:
				createStream = defaultStream();
				isEditedForCreateStream = false;
				populateCreateStreamControl(stats, advancedControllableProperties, defaultStream(), createStreamControllingGroup);
				isEmergencyDelivery = false;
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
			request = escapeSpecialCharacters(request);
			String response = send(request);
			if (StringUtils.isNullOrEmpty(response) || !response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
				throw new ResourceNotReachableException(ErrorMessage.convertErrorMessage(Deserializer.getErrorMessage(response)));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.CREATE_STREAM_CONTROL_ERR + e.getMessage(), e);
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
		Encapsulation preEncapsulation = Encapsulation.getByApiConfigName(getDefaultValueForNullData(preStreamInfo.getEncapsulation(), DecoderConstant.EMPTY));
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
		SwitchOnOffControl streamFlipping = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(createStream.getStreamFlipping(), DecoderConstant.EMPTY));
		if (streamConversion != null) {
			streamFlipping = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(streamConversion.getStreamFlipping(), DecoderConstant.EMPTY));
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
		SwitchOnOffControl aeEncrypted = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(preStreamInfo.getSrtSettings(), DecoderConstant.EMPTY));
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
	 * @param advancedControllableProperties List of AdvancedControllableProperty
	 * @param cachedStreamConfig stream config info
	 */
	private void populateStreamConfig(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, StreamConfig cachedStreamConfig) {
		// Get controllable property current value
		String streamGroup;
		Encapsulation encapsulationEnum = Encapsulation.getByApiStatsName(getDefaultValueForNullData(cachedStreamConfig.getEncapsulation(), DecoderConstant.EMPTY));
		String streamName = getDefaultValueForNullData(cachedStreamConfig.getName(), DecoderConstant.EMPTY);
		if (StringUtils.isNullOrEmpty(streamName) || DecoderConstant.DEFAULT_STREAM_NAME.equals(streamName)) {
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
	 * @param streamGroup is the stream group name
	 */
	private void populateStreamConfigCaseTSOverUDPAndTSOverRTP(Map<String, String> stats, StreamConfig cachedStreamConfig, String streamGroup) {
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		String address = getDefaultValueForNullData(cachedStreamConfig.getDestinationAddress(), DecoderConstant.EMPTY);
		if (address.equalsIgnoreCase(DecoderConstant.ADDRESS_ANY)) {
			address = DecoderConstant.EMPTY;
		}
		String sourceAddress = getDefaultValueForNullData(cachedStreamConfig.getSourceAddress(), DecoderConstant.EMPTY);
		if (sourceAddress.equalsIgnoreCase(DecoderConstant.ADDRESS_ANY)) {
			sourceAddress = DecoderConstant.EMPTY;
		}
		Fec fecEnum = Fec.getByAPIStatsName(getDefaultValueForNullData(cachedStreamConfig.getFec(), DecoderConstant.EMPTY));
		NetworkType networkTypeEnum = NetworkType.UNI_CAST;
		if (!address.isEmpty()) {
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
	 * @param streamGroup is the stream group name
	 */
	private void populateStreamConfigCaseTSOverSRT(Map<String, String> stats, StreamConfig cachedStreamConfig, String streamGroup) {
		SRTMode srtMode = SRTMode.getByName(getDefaultValueForNullData(cachedStreamConfig.getSrtMode(), DecoderConstant.EMPTY));
		String port = getDefaultValueForNullData(cachedStreamConfig.getPort(), DecoderConstant.EMPTY);
		String latency = getDefaultValueForNullData(cachedStreamConfig.getLatency(), DecoderConstant.DEFAULT_LATENCY.toString());
		SwitchOnOffControl streamFlipping = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(cachedStreamConfig.getStreamFlipping(), DecoderConstant.EMPTY));
		StreamConversion streamConversion = cachedStreamConfig.getStreamConversion();
		String address = getDefaultValueForNullData(cachedStreamConfig.getDestinationAddress(), DecoderConstant.EMPTY);
		if (address.equals(DecoderConstant.ADDRESS_ANY)) {
			address = DecoderConstant.EMPTY;
		}

		// Populate relevant control when srt to udp is enabled/ disabled
		stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_STREAM_CONVERSION.getName(), streamFlipping.getUiName());
		if (streamConversion != null) {
			streamFlipping = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(streamConversion.getStreamFlipping(), DecoderConstant.EMPTY));
			if (streamFlipping.isEnable()) {
				String srtToUdpAddress = getDefaultValueForNullData(streamConversion.getAddress(), DecoderConstant.EMPTY);
				String normalizedAddress = NormalizeData.extractNumbersFromDataBySpaceIndex(srtToUdpAddress, DecoderConstant.ADDRESS_DATA_INDEX);
				if (normalizedAddress.isEmpty()) {
					normalizedAddress = NormalizeData.getAddressFromRawData(srtToUdpAddress);
				}
				String srtToUdpPort = getDefaultValueForNullData(streamConversion.getUdpPort(), DecoderConstant.EMPTY);
				String srtToUdpTos = getDefaultValueForNullData(streamConversion.getTos(), DecoderConstant.DEFAULT_TOS);
				String srtToUdpTtl = getDefaultValueForNullData(streamConversion.getTtl(), DecoderConstant.DEFAULT_TTL.toString());

				stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_ADDRESS.getName(), normalizedAddress);
				stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_PORT.getName(), srtToUdpPort);
				stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_TOS.getName(), srtToUdpTos);
				stats.put(streamGroup + StreamControllingMetric.SRT_TO_UDP_TTL.getName(), srtToUdpTtl);
			}
		}

		// Populate relevant control when encrypted is enabled/ disabled
		SwitchOnOffControl aeEncrypted = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(cachedStreamConfig.getSrtSettings(), DecoderConstant.EMPTY));
		stats.put(streamGroup + StreamControllingMetric.ENCRYPTED.getName(), aeEncrypted.getUiName());
		if (aeEncrypted.isEnable()) {
			String passphrase = getDefaultValueForNullData(cachedStreamConfig.getPassphrase(), DecoderConstant.DEFAULT_PASSPHRASE);
			stats.put(streamGroup + StreamControllingMetric.PASSPHRASE.getName(), passphrase);
		}

		// Populate relevant control when Encapsulation(protocol) is ts-srt
		stats.put(streamGroup + StreamControllingMetric.SRT_MODE.getName(), srtMode.getUiName());
		stats.put(streamGroup + StreamControllingMetric.LATENCY.getName(), NormalizeData.extractNumbers(latency));
		switch (srtMode) {
			case LISTENER:
				stats.put(streamGroup + StreamControllingMetric.PORT.getName(), port);
				if (aeEncrypted.isEnable()) {
					RejectUnencrypted rejectUnencrypted = RejectUnencrypted.getByApiName(getDefaultValueForNullData(cachedStreamConfig.getRejectUnencrypted(), DecoderConstant.EMPTY));
					stats.put(streamGroup + StreamControllingMetric.REJECT_UNENCRYPTED_CALLERS.getName(), rejectUnencrypted.getUiName());
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
	 * @throws ResourceNotReachableException when fail to control stream
	 */
	private void streamControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, String streamName, String controllableProperty) {
		StreamControllingMetric streamControllingMetric = StreamControllingMetric.getByName(controllableProperty);

		Optional<StreamConfig> cachedStreamConfig = cachedStreamConfigsFiltered.stream().filter(config -> config.getName().equals(streamName) || config.getDefaultStreamName().equals(streamName))
				.findFirst();
		String streamId;
		if (cachedStreamConfig.isPresent()) {
			streamId = cachedStreamConfig.get().getId();
		} else {
			throw new ResourceNotReachableException(String.format("Stream %s is not exist", streamName));
		}

		switch (streamControllingMetric) {
			case DELETE:
				performDeleteStreamControl(streamId);
				cachedStreamConfigsFiltered.remove(cachedStreamConfig.get());
				for (StreamConfig streamConfig : cachedStreamConfigsFiltered) {
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
	 * @param streamId is the ID of stream
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
				throw new ResourceNotReachableException(ErrorMessage.convertErrorMessage(Deserializer.getErrorMessage(response)));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.STREAM_CONTROL_ERR + e.getMessage(), e);
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate audio control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for populate all audio control properties
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param audioConfig audio source info
	 * @param audioGroup Group of audio
	 */
	private void populateAudioControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, AudioConfig audioConfig, String audioGroup) {
		// Get controllable property current value of AudioSource, AudioChannel, AudioLevel
		AudioSource audioSource = AudioSource.getByUiName(getDefaultValueForNullData(audioConfig.getSource(), DecoderConstant.EMPTY));
		AudioChannel audioChannel = AudioChannel.getByUiName(getDefaultValueForNullData(audioConfig.getChannels(), DecoderConstant.EMPTY));
		AudioLevel audioLevel = AudioLevel.getByUiName(getDefaultValueForNullData(audioConfig.getOutputLevel(), DecoderConstant.EMPTY));

		// Get list values of controllable property (dropdown list): audioSourceList, audioChannelList, audioLevelList
		List<String> audioSourceList = DropdownList.getListOfEnumNames(AudioSource.class);
		List<String> audioChannelList = DropdownList.getListOfEnumNames(AudioChannel.class);
		List<String> audioLevelList = DropdownList.getListOfEnumNames(AudioLevel.class);

		// Populate control: Audio source, audio channel,
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, audioGroup + AudioControllingMetric.AUDIO_SOURCE.getName(), audioSourceList, audioSource.getUiName()));

		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, audioGroup + AudioControllingMetric.AUDIO_CHANNELS.getName(), audioChannelList, audioChannel.getUiName()));

		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, audioGroup + AudioControllingMetric.AUDIO_ZERO_DBFS_AUDIO_LEVEL.getName(), audioLevelList, audioLevel.getUiName()));

		// Populate applyChanges/ Cancel button
		String applyChange = ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.APPLY_CHANGE.getName();
		String cancel = ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.CANCEL.getName();
		if (isEditedForAudio) {
			stats.put(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.EDITED.getName(), DecoderConstant.TRUE_VALUE);
			stats.put(applyChange, DecoderConstant.EMPTY);
			stats.put(cancel, DecoderConstant.EMPTY);
			addAdvanceControlProperties(advancedControllableProperties, createButton(applyChange, DecoderConstant.APPLY, DecoderConstant.APPLYING));
			addAdvanceControlProperties(advancedControllableProperties, createButton(cancel, DecoderConstant.CANCEL, DecoderConstant.CANCELLING));
		} else {
			stats.put(ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH + AudioControllingMetric.EDITED.getName(), DecoderConstant.FALSE_VALUE);
			List<String> listKeyToBeRemove = new ArrayList<>();
			listKeyToBeRemove.add(applyChange);
			listKeyToBeRemove.add(cancel);
			removeUnusedStatsAndControls(stats, advancedControllableProperties, listKeyToBeRemove);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform audio control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for calling API to control audio
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param audioControllingGroup Group name of audio controlling group
	 * @param controllableProperty name of controllable property
	 * @param value of controllable property
	 * @throws ResourceNotReachableException when fail to control stream
	 */
	private void audioControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, String audioControllingGroup, String controllableProperty, String value) {
		AudioControllingMetric audioControllingMetric = AudioControllingMetric.getByName(controllableProperty);
		String formattedChannel;
		isEmergencyDelivery = true;
		isEditedForAudio = true;
		switch (audioControllingMetric) {
			case AUDIO_SOURCE:
				cachedAudioConfig.setSource(value);
				// Formatted channel: Ch34
				formattedChannel = DecoderConstant.TARGET_CH + cachedAudioConfig.getChannels().replace(DecoderConstant.AND_SIGN, DecoderConstant.EMPTY);
				cachedAudioConfig.setOutputSource(value + formattedChannel);
				for (AdvancedControllableProperty control : advancedControllableProperties) {
					if (control.getName().equals(audioControllingGroup + controllableProperty)) {
						control.setValue(value);
						break;
					}
				}
				populateAudioControl(stats, advancedControllableProperties, cachedAudioConfig, ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH);
				break;
			case AUDIO_CHANNELS:
				cachedAudioConfig.setChannels(value);
				formattedChannel = DecoderConstant.TARGET_CH + value.replace(DecoderConstant.AND_SIGN, DecoderConstant.EMPTY);
				cachedAudioConfig.setOutputSource(cachedAudioConfig.getSource() + formattedChannel);
				for (AdvancedControllableProperty control : advancedControllableProperties) {
					if (control.getName().equals(audioControllingGroup + controllableProperty)) {
						control.setValue(value);
						break;
					}
				}
				populateAudioControl(stats, advancedControllableProperties, cachedAudioConfig, ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH);
				break;
			case AUDIO_ZERO_DBFS_AUDIO_LEVEL:
				cachedAudioConfig.setOutputLevel(value);
				for (AdvancedControllableProperty control : advancedControllableProperties) {
					if (control.getName().equals(audioControllingGroup + controllableProperty)) {
						control.setValue(value);
						break;
					}
				}
				populateAudioControl(stats, advancedControllableProperties, cachedAudioConfig, ControllingMetricGroup.AUDIO.getUiName() + DecoderConstant.HASH);
				break;
			case APPLY_CHANGE:
				performSetAudioControl(cachedAudioConfig);
				this.cachedAudioConfig = null;
				isEmergencyDelivery = false;
				isEditedForAudio = false;
				break;
			case CANCEL:
				this.cachedAudioConfig = null;
				isEmergencyDelivery = false;
				isEditedForAudio = false;
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Operation %s is not supported.", controllableProperty));
				}
				throw new IllegalStateException(String.format("Operation %s is not supported.", controllableProperty));
		}

	}

	/**
	 * This method is used to perform audio control
	 *
	 * @param audioConfig is set of audio config info
	 * @throws ResourceNotReachableException when fail to send CLI command
	 */
	private void performSetAudioControl(AudioConfig audioConfig) {
		try {
			String request = audioConfig.contributeCommand(CommandOperation.OPERATION_AUDDEC.getName());
			String response = send(request);
			if (StringUtils.isNullOrEmpty(response) || !response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
				throw new ResourceNotReachableException(ErrorMessage.convertErrorMessage(Deserializer.getErrorMessage(response)));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.AUDIO_CONTROL_ERR + e.getMessage(), e);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate hdmi control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for populate all HDMI control properties:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 */
	private void populateHDMIControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		// Get controllable property current value
		VideoSource videoSourceEnum = VideoSource.getByAPIName(getDefaultValueForNullData(cachedHDMIConfig.getVideoSource(), DecoderConstant.EMPTY));
		SurroundSound surroundSoundEnum = SurroundSound.getByAPIName(getDefaultValueForNullData(cachedHDMIConfig.getSurroundSound(), DecoderConstant.EMPTY));
		AudioOutput audioOutputEnum = AudioOutput.getByAPIName(getDefaultValueForNullData(cachedHDMIConfig.getAudioSource(), DecoderConstant.EMPTY));
		String currentFrameRate = getDefaultValueForNullData(cachedHDMIConfig.getCurrentFrameRate(), DecoderConstant.EMPTY);
		String currentResolution = getDefaultValueForNullData(cachedHDMIConfig.getCurrentResolution(), DecoderConstant.EMPTY);
		String decoderState = getDefaultValueForNullData(cachedHDMIConfig.getDecoderSDI1State(), DecoderConstant.EMPTY);
		if (decoderState.isEmpty()) {
			decoderState = getDefaultValueForNullData(cachedHDMIConfig.getDecoderSDI2State(), DecoderConstant.EMPTY);
		}

		// Get list values of controllable property (dropdown list)
		List<String> videoSourceModes = DropdownList.getListOfEnumNames(VideoSource.class);
		List<String> surroundSoundModes = DropdownList.getListOfEnumNames(SurroundSound.class);
		List<String> audioOutputModes = DropdownList.getListOfEnumNames(AudioOutput.class);

		String hdmiGroup = ControllingMetricGroup.HDMI.getUiName() + DecoderConstant.HASH;

		// Populate control
		stats.put(hdmiGroup + HDMIControllingMetric.RESOLUTION.getName(), currentResolution);
		stats.put(hdmiGroup + HDMIControllingMetric.FRAME_RATE.getName(), currentFrameRate);
		stats.put(hdmiGroup + HDMIControllingMetric.VIDEO_SOURCE_STATE.getName(), decoderState);
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, hdmiGroup + HDMIControllingMetric.VIDEO_SOURCE.getName(), videoSourceModes, videoSourceEnum.getUiName()));
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, hdmiGroup + HDMIControllingMetric.SOUND_MODE.getName(), surroundSoundModes, surroundSoundEnum.getUiName()));

		if (surroundSoundEnum.equals(SurroundSound.STEREO)) {
			addAdvanceControlProperties(advancedControllableProperties,
					createDropdown(stats, hdmiGroup + HDMIControllingMetric.AUDIO_OUT.getName(), audioOutputModes, audioOutputEnum.getUiName()));
		} else {
			for (AdvancedControllableProperty control : advancedControllableProperties) {
				if (control.getName().equals(hdmiGroup + HDMIControllingMetric.AUDIO_OUT.getName())) {
					advancedControllableProperties.remove(control);
					break;
				}
			}
			stats.remove(hdmiGroup + HDMIControllingMetric.AUDIO_OUT.getName());
		}

		// populate applyChange/ Cancel button
		String applyChange = ControllingMetricGroup.HDMI.getUiName() + DecoderConstant.HASH + HDMIControllingMetric.APPLY_CHANGE.getName();
		String cancel = ControllingMetricGroup.HDMI.getUiName() + DecoderConstant.HASH + HDMIControllingMetric.CANCEL.getName();
		if (isEditedForHDMI) {
			stats.put(ControllingMetricGroup.HDMI.getUiName() + DecoderConstant.HASH + HDMIControllingMetric.EDITED.getName(), DecoderConstant.TRUE_VALUE);
			stats.put(applyChange, DecoderConstant.EMPTY);
			stats.put(cancel, DecoderConstant.EMPTY);
			addAdvanceControlProperties(advancedControllableProperties, createButton(applyChange, DecoderConstant.APPLY, DecoderConstant.APPLYING));
			addAdvanceControlProperties(advancedControllableProperties, createButton(cancel, DecoderConstant.CANCEL, DecoderConstant.CANCELLING));
		} else {
			stats.put(ControllingMetricGroup.HDMI.getUiName() + DecoderConstant.HASH + HDMIControllingMetric.EDITED.getName(), DecoderConstant.FALSE_VALUE);

			List<String> listKeyTobeRemove = new ArrayList<>();
			listKeyTobeRemove.add(applyChange);
			listKeyTobeRemove.add(cancel);
			removeUnusedStatsAndControls(stats, advancedControllableProperties, listKeyTobeRemove);
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform hdmi control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for calling control all hdmi control properties in case:
	 * <li>Video source</li>
	 * <li>Sound mode</li>
	 * <li>Audio out</li>
	 * <li>Apply change</li>
	 * <li>Cancel</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param controllableProperty name of controllable property
	 * @param value value of controllable property
	 * @throws ResourceNotReachableException when fail to control hdmi
	 */
	private void hdmiControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, String controllableProperty, String value) {
		HDMIControllingMetric hdmiControllingMetric = HDMIControllingMetric.getByName(controllableProperty);

		isEmergencyDelivery = true;
		isEditedForHDMI = true;
		switch (hdmiControllingMetric) {
			case VIDEO_SOURCE:
				VideoSource videoSourceEnum = VideoSource.getByUiName(value);
				cachedHDMIConfig.setVideoSource(videoSourceEnum.getApiName());
				populateHDMIControl(stats, advancedControllableProperties);
				break;
			case SOUND_MODE:
				SurroundSound surroundSound = SurroundSound.getByUiName(value);
				cachedHDMIConfig.setSurroundSound(surroundSound.getApiName());
				populateHDMIControl(stats, advancedControllableProperties);
				break;
			case AUDIO_OUT:
				AudioOutput audioOutput = AudioOutput.getByUiName(value);
				cachedHDMIConfig.setAudioSource(audioOutput.getApiName());
				populateHDMIControl(stats, advancedControllableProperties);
				break;
			case APPLY_CHANGE:
				performHDMIControl();
				this.realtimeHDMIConfig = new HDMIConfig(cachedHDMIConfig);
				isEditedForHDMI = false;
				populateHDMIControl(stats, advancedControllableProperties);
				isEmergencyDelivery = false;
				break;
			case CANCEL:
				this.cachedHDMIConfig = new HDMIConfig(realtimeHDMIConfig);
				isEditedForHDMI = false;
				populateHDMIControl(stats, advancedControllableProperties);
				isEmergencyDelivery = false;
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
	private void performHDMIControl() {
		try {
			String request = cachedHDMIConfig.contributeCommand(CommandOperation.OPERATION_HDMI.getName(), CommandOperation.SET.getName());
			String response = send(request);
			if (StringUtils.isNullOrEmpty(response) || !response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
				throw new ResourceNotReachableException(ErrorMessage.convertErrorMessage(Deserializer.getErrorMessage(response)));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.HDMI_CONTROL_ERR + e.getMessage(), e);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate service control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for populate all HDMI control properties:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 */
	private void populateServiceControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		// Get controllable property current value
		ServiceSwitchOnOffControl emsSwitch = ServiceSwitchOnOffControl.getByApiName(getDefaultValueForNullData(realtimeServiceConfig.getEms(), DecoderConstant.EMPTY));
		ServiceSwitchOnOffControl httpSwitch = ServiceSwitchOnOffControl.getByApiName(getDefaultValueForNullData(realtimeServiceConfig.getHttp(), DecoderConstant.EMPTY));
		ServiceSwitchOnOffControl sapSwitch = ServiceSwitchOnOffControl.getByApiName(getDefaultValueForNullData(realtimeServiceConfig.getSap(), DecoderConstant.EMPTY));
		ServiceSwitchOnOffControl snmpSwitch = ServiceSwitchOnOffControl.getByApiName(getDefaultValueForNullData(realtimeServiceConfig.getSnmp(), DecoderConstant.EMPTY));
		ServiceSwitchOnOffControl sshSwitch = ServiceSwitchOnOffControl.getByApiName(getDefaultValueForNullData(realtimeServiceConfig.getSsh(), DecoderConstant.EMPTY));
		ServiceSwitchOnOffControl talkbackSwitch = ServiceSwitchOnOffControl.getByApiName(getDefaultValueForNullData(realtimeServiceConfig.getTalkback(), DecoderConstant.EMPTY));
		ServiceSwitchOnOffControl telnetSwitch = ServiceSwitchOnOffControl.getByApiName(getDefaultValueForNullData(realtimeServiceConfig.getTelnet(), DecoderConstant.EMPTY));

		String serviceGroup = ControllingMetricGroup.SERVICE.getUiName() + DecoderConstant.HASH;

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties,
				createSwitch(stats, serviceGroup + ServiceControllingMetric.EMS.getName(), emsSwitch.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
		addAdvanceControlProperties(advancedControllableProperties,
				createSwitch(stats, serviceGroup + ServiceControllingMetric.HTTP.getName(), httpSwitch.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
		addAdvanceControlProperties(advancedControllableProperties,
				createSwitch(stats, serviceGroup + ServiceControllingMetric.SAP.getName(), sapSwitch.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
		addAdvanceControlProperties(advancedControllableProperties,
				createSwitch(stats, serviceGroup + ServiceControllingMetric.SNMP.getName(), snmpSwitch.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
		addAdvanceControlProperties(advancedControllableProperties,
				createSwitch(stats, serviceGroup + ServiceControllingMetric.SSH.getName(), sshSwitch.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
		addAdvanceControlProperties(advancedControllableProperties,
				createSwitch(stats, serviceGroup + ServiceControllingMetric.TALK_BACK.getName(), talkbackSwitch.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
		addAdvanceControlProperties(advancedControllableProperties,
				createSwitch(stats, serviceGroup + ServiceControllingMetric.TELNET.getName(), telnetSwitch.getCode(), DecoderConstant.DISABLE, DecoderConstant.ENABLE));
	}
	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform service control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for calling control all service control properties in case:
	 * <li>ems</li>
	 * <li>http</li>
	 * <li>sap</li>
	 * <li>snmp</li>
	 * <li>ssh</li>
	 * <li>talkback</li>
	 * <li>telnet</li>
	 *
	 * @param controllableProperty name of controllable property
	 * @param value value of controllable property
	 * @throws ResourceNotReachableException when fail to control stream
	 */
	private void serviceControl(String controllableProperty, String value) {
		ServiceControllingMetric serviceControllingMetric = ServiceControllingMetric.getByName(controllableProperty);

		String action = mapSwitchOnOffControlValueToStartStopAction(value);
		switch (serviceControllingMetric) {
			case EMS:
				performServiceControl(ServiceControllingMetric.EMS.getName(), action);
				break;
			case HTTP:
				performServiceControl(ServiceControllingMetric.HTTP.getName(), action);
				break;
			case SAP:
				performServiceControl(ServiceControllingMetric.SAP.getName(), action);
				break;
			case SNMP:
				performServiceControl(ServiceControllingMetric.SNMP.getName(), action);
				break;
			case SSH:
				performServiceControl(ServiceControllingMetric.SSH.getName(), action);
				break;
			case TALK_BACK:
				performServiceControl(ServiceControllingMetric.TALK_BACK.getName(), action);
				break;
			case TELNET:
				performServiceControl(ServiceControllingMetric.TELNET.getName(), action);
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
	 * @param service is the service name
	 * @param action is the control action
	 * @throws ResourceNotReachableException when fail to send CLI command
	 */
	private void performServiceControl(String service, String action) {
		try {
			String request = CommandOperation.OPERATION_SERVICE.getName().
					concat(DecoderConstant.SPACE).
					concat(service).
					concat(DecoderConstant.SPACE).
					concat(action);

			String response = send(request);
			if (action.equals(CommandOperation.STOP.getName())) {
				response = send(CommandOperation.CONFIRM_STOP_SERVICE.getName());
			}
			if (StringUtils.isNullOrEmpty(response) || !response.contains(action)) {
				throw new ResourceNotReachableException(ErrorMessage.convertErrorMessage(Deserializer.getErrorMessage(response)));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.SERVICE_CONTROL_ERR + e.getMessage(), e);
		}
	}

	/**
	 * This method is used to map switch on/off control value to start/stop action
	 *
	 * @param value is the controllable properties value
	 * @return String control action start/ stop
	 * @throws ResourceNotReachableException when fail to send CLI command
	 */
	private String mapSwitchOnOffControlValueToStartStopAction(String value) {
		if (value.equals(DecoderConstant.CODE_OF_ENABLED_SWITCH)) {
			return CommandOperation.START.getName();
		}
		return CommandOperation.STOP.getName();
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region populate talkback control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for populate all HDMI control properties:
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 */
	private void populateTalkBackControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		// Get controllable property current value
		TalkBackSwitchOnOffControl talkBackSwitch = TalkBackSwitchOnOffControl.getByApiStatsName(getDefaultValueForNullData(cachedTalkbackConfig.getState(), DecoderConstant.EMPTY));
		String udpPort = getDefaultValueForNullData(cachedTalkbackConfig.getUdpPort(), DecoderConstant.EMPTY);
		TalkBackDecoderSDI decoderSDI = TalkBackDecoderSDI.getByApiConfigName(getDefaultValueForNullData(cachedTalkbackConfig.getDecoderID(), DecoderConstant.EMPTY));

		// get activated decoder name dropdown list
		List<String> activatedDecoders = DropdownList.getListOfEnumNames(TalkBackDecoderSDI.class);

		// populate primary stream and secondary stream
		String talkbackGroup = ControllingMetricGroup.TALKBACK.getUiName() + DecoderConstant.HASH;
		String primaryStreamPropertyName = talkbackGroup + TalkbackControllingMetric.PRIMARY_STREAM.getName();
		String secondaryStreamPropertyName = talkbackGroup + TalkbackControllingMetric.SECONDARY_STREAM.getName();
		for (DecoderConfig realtimeDecoderConfig : realtimeDecoderConfigs) {
			if (realtimeDecoderConfig.getDecoderID().equals(decoderSDI.getApiConfigName())) {

				// populate primary stream
				String primaryStreamID = realtimeDecoderConfig.getPrimaryStream();
				Optional<StreamConfig> cachedStreamConfig = this.realtimeStreamConfigs.stream().filter(st -> primaryStreamID.equals(st.getId())).findFirst();
				if (cachedStreamConfig.isPresent()) {
					String streamName = cachedStreamConfig.get().getName();
					if (DecoderConstant.DEFAULT_STREAM_NAME.equals(streamName)) {
						streamName = cachedStreamConfig.get().getDefaultStreamName();
					}
					stats.put(primaryStreamPropertyName, streamName);
				} else {
					stats.put(primaryStreamPropertyName, DecoderConstant.DEFAULT_STREAM_NAME);
				}

				// populate secondary stream
				String secondaryStreamID = realtimeDecoderConfig.getSecondaryStream();
				cachedStreamConfig = this.realtimeStreamConfigs.stream().filter(st -> secondaryStreamID.equals(st.getId())).findFirst();
				if (cachedStreamConfig.isPresent()) {
					String streamName = cachedStreamConfig.get().getName();
					if (DecoderConstant.DEFAULT_STREAM_NAME.equals(streamName)) {
						streamName = cachedStreamConfig.get().getDefaultStreamName();
					}
					stats.put(secondaryStreamPropertyName, streamName);
				} else {
					stats.put(secondaryStreamPropertyName, DecoderConstant.DEFAULT_STREAM_NAME);
				}
			}
		}
		// Populate talkback status
		stats.put(talkbackGroup + TalkbackControllingMetric.STATE.getName(), getDefaultValueForNullData(realtimeTalkbackConfig.getState(), DecoderConstant.EMPTY));

		// Populate control
		addAdvanceControlProperties(advancedControllableProperties,
				createSwitch(stats, talkbackGroup + TalkbackControllingMetric.ACTIVE.getName(), talkBackSwitch.getCode(), DecoderConstant.OFF, DecoderConstant.ON));
		addAdvanceControlProperties(advancedControllableProperties,
				createNumeric(stats, talkbackGroup + TalkbackControllingMetric.PORT.getName(), udpPort));
		addAdvanceControlProperties(advancedControllableProperties,
				createDropdown(stats, talkbackGroup + TalkbackControllingMetric.ACTIVE_DECODER_SDI.getName(), activatedDecoders, decoderSDI.getUiName()));

		// Populate apply change cancel
		String applyChange = ControllingMetricGroup.TALKBACK.getUiName() + DecoderConstant.HASH + TalkbackControllingMetric.APPLY_CHANGE.getName();
		String cancel = ControllingMetricGroup.TALKBACK.getUiName() + DecoderConstant.HASH + TalkbackControllingMetric.CANCEL.getName();
		if (isEditedForTalkback) {
			stats.put(ControllingMetricGroup.TALKBACK.getUiName() + DecoderConstant.HASH + TalkbackControllingMetric.EDITED.getName(), DecoderConstant.TRUE_VALUE);
			stats.put(applyChange, DecoderConstant.EMPTY);
			stats.put(cancel, DecoderConstant.EMPTY);
			addAdvanceControlProperties(advancedControllableProperties, createButton(applyChange, DecoderConstant.APPLY, DecoderConstant.APPLYING));
			addAdvanceControlProperties(advancedControllableProperties, createButton(cancel, DecoderConstant.CANCEL, DecoderConstant.CANCELLING));
		} else {
			stats.put(ControllingMetricGroup.TALKBACK.getUiName() + DecoderConstant.HASH + TalkbackControllingMetric.EDITED.getName(), DecoderConstant.FALSE_VALUE);

			List<String> listKeyTobeRemove = new ArrayList<>();
			listKeyTobeRemove.add(applyChange);
			listKeyTobeRemove.add(cancel);
			removeUnusedStatsAndControls(stats, advancedControllableProperties, listKeyTobeRemove);
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//endregion

	//region perform talkback control
	//--------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This method is used for calling control all talkback properties in case:
	 * <li>Port</li>
	 * <li>Decoder SDI</li>
	 * <li>Activate Talkback</li>
	 * <li>Apply change</li>
	 * <li>Cancel</li>
	 *
	 * @param stats is the map that store all statistics
	 * @param advancedControllableProperties is the list that store all controllable properties
	 * @param controllableProperty name of controllable property
	 * @param value value of controllable property
	 * @throws ResourceNotReachableException when fail to control talkback
	 */
	private void talkBackControl(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, String controllableProperty, String value) {
		TalkbackControllingMetric talkbackControllingMetric = TalkbackControllingMetric.getByName(controllableProperty);

		switch (talkbackControllingMetric) {
			case PORT:
				String port;
				try {
					Integer portIntValue = Integer.parseInt(value);
					if (portIntValue < DecoderConstant.MIN_TALKBACK_PORT) {
						portIntValue = DecoderConstant.MIN_TALKBACK_PORT;
					}
					if (portIntValue > DecoderConstant.MAX_PORT) {
						portIntValue = DecoderConstant.MAX_PORT;
					}
					port = portIntValue.toString();
				} catch (Exception e) {
					if (value.contains(DecoderConstant.DASH)) {
						port = DecoderConstant.MIN_TALKBACK_PORT.toString();
					} else {
						port = DecoderConstant.MAX_PORT.toString();
					}
					if (logger.isWarnEnabled()) {
						logger.warn(String.format("%s is invalid port value, the port value must be Integer value range to 1025-65535", value), e);
					}
				}
				cachedTalkbackConfig.setUdpPort(port);
				isEditedForTalkback = true;
				populateTalkBackControl(stats, advancedControllableProperties);
				isEmergencyDelivery = true;
				break;
			case ACTIVE_DECODER_SDI:
				TalkBackDecoderSDI talkBackDecoderSDI = TalkBackDecoderSDI.getByUiName(value);
				cachedTalkbackConfig.setDecoderID(talkBackDecoderSDI.getApiConfigName());
				isEditedForTalkback = true;
				populateTalkBackControl(stats, advancedControllableProperties);
				isEmergencyDelivery = true;
				break;
			case ACTIVE:
				TalkBackSwitchOnOffControl talkBackSwitch = TalkBackSwitchOnOffControl.getByCode(Integer.parseInt(value));
				cachedTalkbackConfig.setState(talkBackSwitch.getApiStatsName());
				isEditedForTalkback = true;
				populateTalkBackControl(stats, advancedControllableProperties);
				isEmergencyDelivery = true;
				break;
			case APPLY_CHANGE:
				String decoderID = cachedTalkbackConfig.getDecoderID();
				boolean isEditedDecoderSDI = !cachedTalkbackConfig.getDecoderID().equals(realtimeTalkbackConfig.getDecoderID());
				boolean isEditedState = !cachedTalkbackConfig.getState().equals(realtimeTalkbackConfig.getState());
				Optional<DecoderConfig> cachedDecoderConfig = this.cachedDecoderConfigs.stream().filter(st -> decoderID.equals(st.getDecoderID())).findFirst();
				if ((isEditedState || isEditedDecoderSDI) && cachedDecoderConfig.isPresent() && DecoderConstant.DEFAULT_STREAM_NAME.equals(cachedDecoderConfig.get().getPrimaryStream())
						&& cachedDecoderConfig.get().getSecondaryStream().equals(DecoderConstant.DEFAULT_STREAM_NAME)) {
					throw new ResourceNotReachableException(String.format("Primary stream and secondary stream is none. Please assign primary stream or secondary stream to Decoder SDI %s ", decoderID));
				}

				// apply port changing
				String cachedUdpPort = getDefaultValueForNullData(cachedTalkbackConfig.getUdpPort(), DecoderConstant.EMPTY);
				String realtimeUdpPort = getDefaultValueForNullData(realtimeTalkbackConfig.getUdpPort(), DecoderConstant.EMPTY);
				if (!cachedUdpPort.equals(realtimeUdpPort)) {
					performTalkbackControl(CommandOperation.SET);
				}

				// apply talkback activate changing
				talkBackSwitch = TalkBackSwitchOnOffControl.getByApiStatsName(getDefaultValueForNullData(cachedTalkbackConfig.getState(), DecoderConstant.EMPTY));
				if (isEditedState) {
					switch (talkBackSwitch) {
						case OFF:
							performTalkbackControl(CommandOperation.STOP);
							break;
						case ON:
							performTalkbackControl(CommandOperation.START);
							break;
						default:
							if (logger.isWarnEnabled()) {
								logger.warn(String.format("Decoder state %s is not supported.", talkBackSwitch.getApiStatsName()));
							}
							break;
					}
				}
				// apply decoder SDI changing
				if (isEditedDecoderSDI) {
					if (talkBackSwitch.equals(TalkBackSwitchOnOffControl.OFF)) {
						performTalkbackControl(CommandOperation.START);
						performTalkbackControl(CommandOperation.STOP);
					}
					if (talkBackSwitch.equals(TalkBackSwitchOnOffControl.ON)) {
						performTalkbackControl(CommandOperation.START);
					}
				}
				realtimeTalkbackConfig = new TalkBackConfig(cachedTalkbackConfig);
				isEditedForTalkback = false;
				break;
			case CANCEL:
				cachedTalkbackConfig = new TalkBackConfig(realtimeTalkbackConfig);
				isEditedForTalkback = false;
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
	 * @param action is the control action
	 * @throws ResourceNotReachableException when fail to send CLI command
	 */
	private void performTalkbackControl(CommandOperation action) {
		try {
			String request;
			switch (action) {
				case SET:
					String udpPort = cachedTalkbackConfig.getUdpPort();
					request = CommandOperation.OPERATION_TALKBACK.getName().
							concat(DecoderConstant.SPACE).
							concat(action.getName()).
							concat(DecoderConstant.SPACE).
							concat(TalkbackControllingMetric.PORT.getName()).
							concat(DecoderConstant.EQUAL).
							concat(udpPort);
					break;
				case START:
					String decoderID = cachedTalkbackConfig.getDecoderID();
					request = CommandOperation.OPERATION_TALKBACK.getName().
							concat(DecoderConstant.SPACE).
							concat(decoderID).
							concat(DecoderConstant.SPACE).
							concat(action.getName());
					break;
				case STOP:
					request = CommandOperation.OPERATION_TALKBACK.getName().
							concat(DecoderConstant.SPACE).
							concat(action.getName());
					break;
				default:
					throw new IllegalStateException("Unexpected talkback activation: " + action);
			}

			String response = send(request);
			if (StringUtils.isNullOrEmpty(response) || !response.contains(DecoderConstant.SUCCESSFUL_RESPONSE)) {
				if (!cachedTalkbackConfig.getDecoderID().equals(realtimeTalkbackConfig.getDecoderID())) {
					throw new ResourceNotReachableException("Talkback configuration Error, please re-configure Stream and Decoder SDI");
				}
				throw new ResourceNotReachableException(ErrorMessage.convertErrorMessage(Deserializer.getErrorMessage(response)));
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(DecoderConstant.TALKBACK_CONTROL_ERR + e.getMessage(), e);
		}
	}

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
	 * This method is used to convert switch control string value to boolean
	 *
	 * @param value value of switch control in String
	 * @return boolean value of switch control true/false
	 */
	public boolean convertSwitchControlValue(String value) {
		return value.equals(DecoderConstant.CODE_OF_ENABLED_SWITCH);
	}
}
