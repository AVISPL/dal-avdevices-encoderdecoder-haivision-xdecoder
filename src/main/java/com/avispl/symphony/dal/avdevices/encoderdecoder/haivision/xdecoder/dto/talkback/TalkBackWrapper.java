package com.avispl.symphony.dal.avdevices.encoderdecoder.haivision.xdecoder.dto.talkback;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Talkback info wrapper
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 5/24/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalkBackWrapper {

    @JsonAlias("Configuration")
    private TalkBackConfig talkBackConfig;

    @JsonAlias("Statistics")
    private TalkBackStats talkBackStats;

    /**
     * Retrieves {@code {@link #talkBackConfig }}
     *
     * @return value of {@link #talkBackConfig}
     */
    public TalkBackConfig getTalkBackConfig() {
        return talkBackConfig;
    }

    /**
     * Sets {@code activeStreamName}
     *
     * @param talkBackConfig the {@code java.lang.String} field
     */
    public void setTalkBackConfig(TalkBackConfig talkBackConfig) {
        this.talkBackConfig = talkBackConfig;
    }

    /**
     * Retrieves {@code {@link #talkBackStats}}
     *
     * @return value of {@link #talkBackStats}
     */
    public TalkBackStats getTalkBackStats() {
        return talkBackStats;
    }

    /**
     * Sets {@code activeStreamName}
     *
     * @param talkBackStats the {@code java.lang.String} field
     */
    public void setTalkBackStats(TalkBackStats talkBackStats) {
        this.talkBackStats = talkBackStats;
    }
}
