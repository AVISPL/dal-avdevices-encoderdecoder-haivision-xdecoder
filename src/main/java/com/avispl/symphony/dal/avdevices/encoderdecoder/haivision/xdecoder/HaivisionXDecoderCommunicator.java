/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command.Account;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.command.Haiversion;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DeviceInfoMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.MonitoringMetricGroup;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.decoder.monitoringmetric.DecoderMonitoringMetric;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.Deserializer;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication.AuthenticationRole;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.authentication.AuthenticationRoleWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderConfigInfo;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.decoderstats.DecoderInfoWrapper;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfo;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.deviceinfo.DeviceInfoWrapper;
import com.avispl.symphony.dal.communicator.SshCommunicator;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * HaivisionXEncoderCommunicator
 *
 * @author Ivan / Symphony Dev Team<br>
 * Created on 4/6/2022
 * @since 1.0.0
 */
public class HaivisionXDecoderCommunicator extends SshCommunicator implements Monitorable, Controller {
	ObjectMapper objectMapper;
	AuthenticationRole authenticationRole;
	private Map<String, String> failedMonitor;
	private boolean isUpdateLocalDecoderControl = false;

	// Decoder and stream DTO
	private List<DecoderConfigInfo> decoderConfigInfoDTOList;
	private List<DecoderConfigInfo> localDecoderConfigInfoList;

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

	@Override
	public List<Statistics> getMultipleStatistics() {
		final ExtendedStatistics extendedStatistics = new ExtendedStatistics();
		final Map<String, String> stats = new HashMap<>();

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
	private String retrieveUserRole() {
		objectMapper = new ObjectMapper();
		try {
			String request = Account.ACCOUNT.getName()
					.concat(DecoderConstant.SPACE)
					.concat(getLogin())
					.concat(DecoderConstant.SPACE)
					.concat(Account.GET.getName());

			String response = send(request);
			String role = null;
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.popularConvertDataToObject(response, request);
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
				Map<String, Object> responseMap = Deserializer.popularConvertDataToObject(response, request);
				DeviceInfoWrapper deviceInfoWrapper = objectMapper.convertValue(responseMap, DeviceInfoWrapper.class);
				DeviceInfo deviceInfo = deviceInfoWrapper.getDeviceInfo();

				if (deviceInfo != null) {
					stats.put(DeviceInfoMetric.BOOT_VERSION.getName(), checkForNullData(deviceInfo.getBootVersion()));
					stats.put(DeviceInfoMetric.CARD_TYPE.getName(), checkForNullData(deviceInfo.getCardType()));
					stats.put(DeviceInfoMetric.CPLD_REVISION.getName(), checkForNullData(deviceInfo.getCpldRevision()));
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
				Map<String, Object> responseMap = Deserializer.popularConvertDataToObject(response, request);
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

		if (localDecoderConfigInfoList.size() > decoderID) {
			DecoderConfigInfo localDecoderInfo = this.localDecoderConfigInfoList.get(decoderID);
			DecoderConfigInfo decoderInfoDTO = this.decoderConfigInfoDTOList.get(decoderID);
			if (decoderInfoDTO.equals(localDecoderInfo) && !decoderInfo.equals(decoderInfoDTO)) {
				this.decoderConfigInfoDTOList.set(decoderID, decoderInfo);
				this.isUpdateLocalDecoderControl = true;
			}
		}
		if (!isUpdateLocalDecoderControl) {
			if (this.decoderConfigInfoDTOList.size() > decoderID) {
				this.decoderConfigInfoDTOList.set(decoderID, decoderInfo);
			} else {
				this.decoderConfigInfoDTOList.add(decoderID, decoderInfo);
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
			String request = "viddec 1 get all";
			String response = send(request);
			if (response != null) {
				Map<String, Object> responseMap = Deserializer.popularConvertDataToObject(response, request);
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

//region perform controls
//--------------------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------------------
//endregion
}
