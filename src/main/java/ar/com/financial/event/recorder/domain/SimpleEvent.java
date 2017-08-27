package ar.com.financial.event.recorder.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class SimpleEvent {

    @Id
    @GeneratedValue
    private int id;

    private Date arrivalTime;
    private Date originTime;
    private String code;
    private String inputOutput;
    private String remoteBic;
    private String type;
    private String suffix;
    private String session;
    private String sequence;
    private String localBic;

    /**
     * Do not use, framework usage only.
     */
    @Deprecated
    public SimpleEvent() {}

    public SimpleEvent(final Date arrivalTime,
                       final Date originTime,
                       final String code,
                       final String inputOutput,
                       final String remoteBic,
                       final String type,
                       final String suffix,
                       final String session,
                       final String sequence,
                       final String localBic) {
        Validate.notNull(arrivalTime, "The arrival time cannot be null");
        Validate.notNull(originTime, "The origin time cannot be null");
        Validate.notNull(inputOutput, "The intput output cannot be null");
        Validate.notBlank(remoteBic, "The remote bic cannot be blank");
        Validate.notBlank(localBic, "The local bic cannot be blank");
        this.arrivalTime = arrivalTime;
        this.originTime = originTime;
        this.code = code;
        this.inputOutput = inputOutput;
        this.remoteBic = remoteBic;
        this.type = type;
        this.suffix = suffix;
        this.session = session;
        this.sequence = sequence;
        this.localBic = localBic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getOriginTime() {
        return originTime;
    }

    public void setOriginTime(Date originTime) {
        this.originTime = originTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInputOutput() {
        return inputOutput;
    }

    public void setInputOutput(String inputOutput) {
        this.inputOutput = inputOutput;
    }

    public String getRemoteBic() {
        return remoteBic;
    }

    public void setRemoteBic(String remoteBic) {
        this.remoteBic = remoteBic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getLocalBic() {
        return localBic;
    }

    public void setLocalBic(String localBic) {
        this.localBic = localBic;
    }

    @Override
    public String toString() {
        return "SimpleEvent{" +
                "id=" + id +
                ", arrivalTime=" + arrivalTime +
                ", originTime=" + originTime +
                ", code='" + code + '\'' +
                ", inputOutput='" + inputOutput + '\'' +
                ", remoteBic='" + remoteBic + '\'' +
                ", type='" + type + '\'' +
                ", suffix='" + suffix + '\'' +
                ", session='" + session + '\'' +
                ", sequence='" + sequence + '\'' +
                ", localBic='" + localBic + '\'' +
                '}';
    }

}
