/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats;

import java.util.Objects;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Fec;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.SRTMode;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.SwitchOnOffControl;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.NormalizeData;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.stream.controllingmetric.Encapsulation;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.avispl.symphony.dal.util.StringUtils;

/**
 * Set of stream configuration properties
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 4/19/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamConfig {
	private static final Log logger = LogFactory.getLog(StreamConfig.class);

	private String id;
	private String name;
	private String destinationAddress;
	private String sourceAddress;
	private String passphrase;
	private StreamConversion streamConversion;

	@JsonAlias("Encapsulation")
	private String encapsulation;

	@JsonAlias("Address")
	private String address;

	@JsonAlias("UDPPort")
	private String port;

	@JsonAlias("SourcePort")
	private String sourcePort;

	@JsonAlias("DestinationPort")
	private String destinationPort;

	@JsonAlias("Latency")
	private String latency;

	@JsonAlias("Mode")
	private String srtMode;

	@JsonAlias("AESEncryption")
	private String srtSettings;

	@JsonAlias("RejectUnencrypted")
	private String rejectUnencrypted;

	@JsonAlias("FEC")
	private String fec;

	@JsonAlias("StreamFlipping")
	private String  streamFlipping ;

	public StreamConfig() {
	}

	/**
	 * This constructor is used for deep clone object
	 *
	 * @param streamInfo Stream config info
	 */
	public StreamConfig(StreamConfig streamInfo) {
		this.name = streamInfo.getName();
		this.id = streamInfo.getId();
		this.encapsulation = streamInfo.getEncapsulation();
		this.address = streamInfo.getAddress();
		this.port = streamInfo.getPort();
		this.destinationPort = streamInfo.getDestinationPort();
		this.latency = streamInfo.getLatency();
		this.srtMode = streamInfo.getSrtMode();
		this.srtSettings = streamInfo.getSrtSettings();
		this.rejectUnencrypted = streamInfo.getRejectUnencrypted();
		this.sourcePort = streamInfo.getSourcePort();
		this.destinationAddress = streamInfo.getDestinationAddress();
		this.sourceAddress = streamInfo.getSourceAddress();
		this.streamConversion = streamInfo.getStreamConversion();
		this.fec = streamInfo.getFec();
		this.passphrase = streamInfo.getPassphrase();
		this.streamFlipping = streamInfo.getStreamFlipping();
	}

	/**
	 * Retrieves {@code {@link #id}}
	 *
	 * @return value of {@link #id}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets {@code id}
	 *
	 * @param id the {@code java.lang.String} field
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves {@code {@link #destinationAddress }}
	 *
	 * @return value of {@link #destinationAddress}
	 */
	public String getDestinationAddress() {
		return destinationAddress;
	}

	/**
	 * Sets {@code MulticastAddress}
	 *
	 * @param destinationAddress the {@code java.lang.String} field
	 */
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	/**
	 * Sets {@code name}
	 *
	 * @param name the {@code java.lang.String} field
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #sourceAddress}}
	 *
	 * @return value of {@link #sourceAddress}
	 */
	public String getSourceAddress() {
		return sourceAddress;
	}

	/**
	 * Sets {@code sourceAddress}
	 *
	 * @param sourceAddress the {@code java.lang.String} field
	 */
	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	/**
	 * Retrieves {@code {@link #streamConversion}}
	 *
	 * @return value of {@link #streamConversion}
	 */
	public StreamConversion getStreamConversion() {
		return streamConversion;
	}

	/**
	 * Sets {@code streamConversion}
	 *
	 * @param streamConversion the {@code com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.streamstats.StreamConversion} field
	 */
	public void setStreamConversion(StreamConversion streamConversion) {
		this.streamConversion = streamConversion;
	}

	/**
	 * Retrieves {@code {@link #encapsulation}}
	 *
	 * @return value of {@link #encapsulation}
	 */
	public String getEncapsulation() {
		return encapsulation;
	}

	/**
	 * Sets {@code encapsulation}
	 *
	 * @param encapsulation the {@code java.lang.String} field
	 */
	public void setEncapsulation(String encapsulation) {
		this.encapsulation = encapsulation;
	}

	/**
	 * Retrieves {@code {@link #address}}
	 *
	 * @return value of {@link #address}
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets {@code address}
	 *
	 * @param address the {@code java.lang.String} field
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Retrieves {@code {@link #port}}
	 *
	 * @return value of {@link #port}
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Sets {@code port}
	 *
	 * @param port the {@code java.lang.String} field
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * Retrieves {@code {@link #sourcePort}}
	 *
	 * @return value of {@link #sourcePort}
	 */
	public String getSourcePort() {
		return sourcePort;
	}

	/**
	 * Sets {@code sourcePort}
	 *
	 * @param sourcePort the {@code java.lang.String} field
	 */
	public void setSourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
	}

	/**
	 * Retrieves {@code {@link #destinationPort}}
	 *
	 * @return value of {@link #destinationPort}
	 */
	public String getDestinationPort() {
		return destinationPort;
	}

	/**
	 * Sets {@code destinationPort}
	 *
	 * @param destinationPort the {@code java.lang.String} field
	 */
	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	/**
	 * Retrieves {@code {@link #latency}}
	 *
	 * @return value of {@link #latency}
	 */
	public String getLatency() {
		return latency;
	}

	/**
	 * Sets {@code latency}
	 *
	 * @param latency the {@code java.lang.String} field
	 */
	public void setLatency(String latency) {
		this.latency = latency;
	}

	/**
	 * Retrieves {@code {@link #srtMode}}
	 *
	 * @return value of {@link #srtMode}
	 */
	public String getSrtMode() {
		return srtMode;
	}

	/**
	 * Sets {@code srtMode}
	 *
	 * @param srtMode the {@code java.lang.String} field
	 */
	public void setSrtMode(String srtMode) {
		this.srtMode = srtMode;
	}

	/**
	 * Retrieves {@code {@link #srtSettings}}
	 *
	 * @return value of {@link #srtSettings}
	 */
	public String getSrtSettings() {
		return srtSettings;
	}

	/**
	 * Sets {@code srtSettings}
	 *
	 * @param srtSettings the {@code java.lang.String} field
	 */
	public void setSrtSettings(String srtSettings) {
		this.srtSettings = srtSettings;
	}

	/**
	 * Retrieves {@code {@link #rejectUnencrypted}}
	 *
	 * @return value of {@link #rejectUnencrypted}
	 */
	public String getRejectUnencrypted() {
		return rejectUnencrypted;
	}

	/**
	 * Sets {@code rejectUnencrypted}
	 *
	 * @param rejectUnencrypted the {@code java.lang.String} field
	 */
	public void setRejectUnencrypted(String rejectUnencrypted) {
		this.rejectUnencrypted = rejectUnencrypted;
	}

	/**
	 * Retrieves {@code {@link #fec}}
	 *
	 * @return value of {@link #fec}
	 */
	public String getFec() {
		return fec;
	}

	/**
	 * Sets {@code fec}
	 *
	 * @param fec the {@code java.lang.String} field
	 */
	public void setFec(String fec) {
		this.fec = fec;
	}

	/**
	 * Retrieves {@code {@link #passphrase}}
	 *
	 * @return value of {@link #passphrase}
	 */
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * Sets {@code passphrase}
	 *
	 * @param passphrase the {@code java.lang.String} field
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * Retrieves {@code {@link #streamFlipping}}
	 *
	 * @return value of {@link #streamFlipping}
	 */
	public String getStreamFlipping() {
		return streamFlipping;
	}

	/**
	 * Sets {@code streamFlipping}
	 *
	 * @param streamFlipping the {@code java.lang.String} field
	 */
	public void setStreamFlipping(String streamFlipping) {
		this.streamFlipping = streamFlipping;
	}

	/**
	 * Retrieves default stream name when stream name is empty
	 *
	 * @return String default stream name
	 */
	public String getDefaultStreamName() {
		Encapsulation encapsulationEnum = Encapsulation.getByApiStatsName(getEncapsulation());
		String encapsulationShortName = encapsulationEnum.getShortName();
		if (getEncapsulation().equals(Encapsulation.RTSP.getApiStatsName())) {
			return getAddress().substring(0, 1).toUpperCase() + getAddress().substring(1);
		} else if (StringUtils.isNullOrEmpty(getDestinationAddress()) || getDestinationAddress().equals(DecoderConstant.ADDRESS_ANY.toUpperCase())) {
			return encapsulationShortName + DecoderConstant.AT_SIGN + DecoderConstant.LEFT_PARENTHESES + DecoderConstant.ADDRESS_ANY
					+ DecoderConstant.RIGHT_PARENTHESES +
					DecoderConstant.COLON + getPort();
		} else {
			return encapsulationShortName + DecoderConstant.AT_SIGN + NormalizeData.convertToNumberValue(getDestinationAddress()) +
					DecoderConstant.COLON + getPort();
		}
	}

	/**
	 * This method is used to create command for stream control: create
	 *
	 * @return String CLI command
	 */
	public String contributeCommand(String command, String action) {
		StringBuilder request = new StringBuilder();
		request.append(command)
				.append(DecoderConstant.SPACE)
				.append(action);

		Encapsulation encapsulationEnum = Encapsulation.getByApiConfigName(getDefaultValueForNullData(this.encapsulation, DecoderConstant.EMPTY));
		SRTMode srtModeEnum = SRTMode.getByName(getDefaultValueForNullData(this.srtMode, DecoderConstant.EMPTY));
		SwitchOnOffControl aeEncryptedEnum = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(this.srtSettings, DecoderConstant.EMPTY));
		SwitchOnOffControl streamFlippingEnum = SwitchOnOffControl.getByApiName(getDefaultValueForNullData(this.streamFlipping, DecoderConstant.EMPTY));

		if (!StringUtils.isNullOrEmpty(name)) {
			request.append(" name=\"").append(name).append(DecoderConstant.DOUBLE_QUOTATION);
		}
		if (!StringUtils.isNullOrEmpty(port) || !StringUtils.isNullOrEmpty(destinationPort)) {
			request.append(" port=").append(port);
		}
		if (!StringUtils.isNullOrEmpty(destinationAddress) && !destinationAddress.equals(DecoderConstant.ADDRESS_ANY)) {
			request.append(" addr=\"").append(destinationAddress).append(DecoderConstant.DOUBLE_QUOTATION);
		}
		if (!StringUtils.isNullOrEmpty(encapsulation)) {
			request.append(" encapsulation=").append(encapsulation);
		}
		switch (encapsulationEnum) {
			case RTSP:
				if (!StringUtils.isNullOrEmpty(address)) {
					request.append(" addr=").append(address);
				}
				break;
			case TS_OVER_UDP:
			case TS_OVER_RTP:
				if (!StringUtils.isNullOrEmpty(sourceAddress) && !sourceAddress.equals(DecoderConstant.ADDRESS_ANY)) {
					request.append(" sourceaddr=\"").append(sourceAddress).append(DecoderConstant.DOUBLE_QUOTATION);
				}
				if (!StringUtils.isNullOrEmpty(fec)) {
					Fec fecEnum = Fec.getByAPIStatsName(fec);
					request.append(" fec=").append(fecEnum.getApiConfigName());
				}
				break;
			case TS_OVER_SRT:
				if (aeEncryptedEnum.isEnable()) {
					if (!StringUtils.isNullOrEmpty(passphrase)) {
						request.append(" passphrase=\"").append(passphrase).append(DecoderConstant.DOUBLE_QUOTATION);
					}
					if (!StringUtils.isNullOrEmpty(rejectUnencrypted)) {
						request.append(" rejectunencrypted=").append(rejectUnencrypted);
					}
				}
				if (streamFlippingEnum.isEnable() && streamConversion != null) {
					String flipAddress = getDefaultValueForNullData(streamConversion.getAddress(), DecoderConstant.EMPTY);
					String flipPort = getDefaultValueForNullData(streamConversion.getUdpPort(), DecoderConstant.EMPTY);
					String flipTtl = getDefaultValueForNullData(streamConversion.getTtl(), DecoderConstant.DEFAULT_TTL.toString());
					String flipTos = getDefaultValueForNullData(streamConversion.getTos(), DecoderConstant.DEFAULT_TOS);
					if (!StringUtils.isNullOrEmpty(flipAddress)) {
						request.append(" flipaddr=\"").append(flipAddress).append(DecoderConstant.DOUBLE_QUOTATION);
					}
					if (!StringUtils.isNullOrEmpty(flipPort)) {
						request.append(" flipport=").append(flipPort);
					}
					if (!StringUtils.isNullOrEmpty(flipTtl)) {
						request.append(" flipttl=").append(flipTtl);
					}else {
						request.append(" flipttl=").append(DecoderConstant.DEFAULT_TTL);
					}
					if (!StringUtils.isNullOrEmpty(flipTos)) {
						request.append(" fliptos=").append(flipTos);
					}else {
						request.append(" fliptos=").append(DecoderConstant.DEFAULT_TOS);
					}
				}

				if (!StringUtils.isNullOrEmpty(srtMode)) {
					request.append(" mode=").append(srtMode);
				}
				if (!StringUtils.isNullOrEmpty(latency)) {
					request.append(" latency=").append(latency);
				}else {
					request.append(" latency=").append(DecoderConstant.DEFAULT_LATENCY);
				}
				switch (srtModeEnum) {
					case LISTENER:
					case RENDEZVOUS:
						break;
					case CALLER:
						if (!StringUtils.isNullOrEmpty(sourcePort)) {
							request.append(" sourceport=").append(sourcePort);
						}
						break;
					default:
						if (logger.isWarnEnabled()) {
							logger.warn(String.format("SRT mode %s is not supported.", srtModeEnum.getUiName()));
						}
						break;
				}
				break;
			default:
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Encapsulation mode %s is not supported.", encapsulationEnum.getUiName()));
				}
				break;
		}
		return request.toString();
	}

	/**
	 * get default value for null data
	 *
	 * @param value value of monitoring properties
	 * @return String (none/value)
	 */
	private String getDefaultValueForNullData(String value, String defaultValue) {
		return StringUtils.isNullOrEmpty(value) ? defaultValue : value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		StreamConfig that = (StreamConfig) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(destinationAddress, that.destinationAddress) && Objects.equals(
				sourceAddress, that.sourceAddress) && Objects.equals(streamConversion, that.streamConversion) && Objects.equals(encapsulation, that.encapsulation)
				&& Objects.equals(address, that.address) && Objects.equals(port, that.port) && Objects.equals(sourcePort, that.sourcePort) && Objects.equals(
				destinationPort, that.destinationPort) && Objects.equals(latency, that.latency) && Objects.equals(srtMode, that.srtMode) && Objects.equals(srtSettings,
				that.srtSettings) && Objects.equals(rejectUnencrypted, that.rejectUnencrypted) && Objects.equals(fec, that.fec);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, destinationAddress, sourceAddress, streamConversion, encapsulation, address, port, sourcePort, destinationPort, latency, srtMode, srtSettings, rejectUnencrypted, fec);
	}
}
