package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.service;

import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.DecoderConstant;
import com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.common.hdmi.controllingmetric.SurroundSound;
import com.avispl.symphony.dal.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Service config info
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/20/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceConfig {

	@JsonAlias("ems")
	private String ems;

	@JsonAlias("http")
	private String http;

	@JsonAlias("sap")
	private String sap;

	@JsonAlias("snmp")
	private String snmp;

	@JsonAlias("ssh")
	private String ssh;

	@JsonAlias("talkback")
	private String talkback;

	@JsonAlias("telnet")
	private String telnet;

	/**
	 * Non-parameterized constructor
	 */
	public ServiceConfig() {
	}

	/**
	 * This constructor is used for deep clone object
	 *
	 * @param hdmiConfig hdmi config info
	 */
	public ServiceConfig(ServiceConfig hdmiConfig) {
		ems = hdmiConfig.getEms();
		http = hdmiConfig.getHttp();
		sap = hdmiConfig.getSap();
		snmp = hdmiConfig.getSnmp();
		ssh = hdmiConfig.getSsh();
		talkback = hdmiConfig.getTalkback();
		telnet = hdmiConfig.getTelnet();
	}

	/**
	 * Retrieves {@code {@link #ems }}
	 *
	 * @return value of {@link #ems}
	 */
	public String getEms() {
		return ems;
	}

	/**
	 * Sets {@code ems}
	 *
	 * @param ems the {@code java.lang.String} field
	 */
	public void setEms(String ems) {
		this.ems = ems;
	}

	/**
	 * Retrieves {@code {@link #http }}
	 *
	 * @return value of {@link #http}
	 */
	public String getHttp() {
		return http;
	}

	/**
	 * Sets {@code http}
	 *
	 * @param http the {@code java.lang.String} field
	 */
	public void setHttp(String http) {
		this.http = http;
	}

	/**
	 * Retrieves {@code {@link #sap }}
	 *
	 * @return value of {@link #sap}
	 */
	public String getSap() {
		return sap;
	}

	/**
	 * Sets {@code sap}
	 *
	 * @param sap the {@code java.lang.String} field
	 */
	public void setSap(String sap) {
		this.sap = sap;
	}

	/**
	 * Retrieves {@code {@link #snmp }}
	 *
	 * @return value of {@link #snmp}
	 */
	public String getSnmp() {
		return snmp;
	}

	/**
	 * Sets {@code snmp}
	 *
	 * @param snmp the {@code java.lang.String} field
	 */
	public void setSnmp(String snmp) {
		this.snmp = snmp;
	}

	/**
	 * Retrieves {@code {@link #ssh }}
	 *
	 * @return value of {@link #ssh}
	 */
	public String getSsh() {
		return ssh;
	}

	/**
	 * Sets {@code ssh}
	 *
	 * @param ssh the {@code java.lang.String} field
	 */
	public void setSsh(String ssh) {
		this.ssh = ssh;
	}

	/**
	 * Retrieves {@code {@link #talkback }}
	 *
	 * @return value of {@link #talkback}
	 */
	public String getTalkback() {
		return talkback;
	}

	/**
	 * Sets {@code talkback}
	 *
	 * @param talkback the {@code java.lang.String} field
	 */
	public void setTalkback(String talkback) {
		this.talkback = talkback;
	}

	/**
	 * Retrieves {@code {@link #telnet }}
	 *
	 * @return value of {@link #telnet}
	 */
	public String getTelnet() {
		return telnet;
	}

	/**
	 * Sets {@code telnet}
	 *
	 * @param telnet the {@code java.lang.String} field
	 */
	public void setTelnet(String telnet) {
		this.telnet = telnet;
	}

	/**
	 * This method is used to compare object in specify case
	 */
	public boolean deepEquals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SurroundSound surroundSoundEnum = SurroundSound.getByAPIName(getDefaultValueForNullData(sap, DecoderConstant.EMPTY));
		ServiceConfig that = (ServiceConfig) o;
		if (surroundSoundEnum.equals(SurroundSound.STEREO)) {
			return Objects.equals(ems, that.ems)
					&& Objects.equals(sap, that.sap)
					&& Objects.equals(http, that.http);
		}else
			return Objects.equals(ems, that.ems)
					&& Objects.equals(sap, that.sap);

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
		ServiceConfig that = (ServiceConfig) o;
		return Objects.equals(ems, that.ems) && Objects.equals(http, that.http) && Objects.equals(sap, that.sap)
				&& Objects.equals(snmp, that.snmp) && Objects.equals(ssh, that.ssh) && Objects.equals(talkback,
				that.talkback) && Objects.equals(telnet, that.telnet);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ems, http, sap, snmp, ssh, talkback, telnet);
	}
}
