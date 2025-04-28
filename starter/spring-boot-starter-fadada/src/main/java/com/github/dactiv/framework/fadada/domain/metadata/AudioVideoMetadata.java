package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class AudioVideoMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -6854124323750894177L;

    private String audioText;
    private String answerText;
    private Boolean skipVerification;

    public AudioVideoMetadata() {
    }

    public String getAudioText() {
        return this.audioText;
    }

    public void setAudioText(String audioText) {
        this.audioText = audioText;
    }

    public String getAnswerText() {
        return this.answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getSkipVerification() {
        return this.skipVerification;
    }

    public void setSkipVerification(Boolean skipVerification) {
        this.skipVerification = skipVerification;
    }
}
