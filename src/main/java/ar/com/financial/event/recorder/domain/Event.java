package ar.com.financial.event.recorder.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Event {

    @Id
    @GeneratedValue
    private int id;
    private Date arrivalTime;
    private Date originTime;
    private String code;
    private String inputOutput;
    private String remoteBic;
    private String type;
    private long suffix;
    private String session;
    private String sequence;
    private String localBic;

    /**
     * Do not use, persistence only.
     */
    @Deprecated
    public Event() {}

    public Event(final Date arrivalTime,
                 final Date originTime,
                 final String code,
                 final String inputOutput,
                 final String remoteBic,
                 final String type,
                 final long suffix,
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

    public void setArrivalTime(final Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getOriginTime() {
        return originTime;
    }

    public void setOriginTime(final Date originTime) {
        this.originTime = originTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getInputOutput() {
        return inputOutput;
    }

    public void setInputOutput(final String inputOutput) {
        this.inputOutput = inputOutput;
    }

    public String getRemoteBic() {
        return remoteBic;
    }

    public void setRemoteBic(final String remoteBic) {
        this.remoteBic = remoteBic;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public long getSuffix() {
        return suffix;
    }

    public void setSuffix(final long suffix) {
        this.suffix = suffix;
    }

    public String getSession() {
        return session;
    }

    public void setSession(final String session) {
        this.session = session;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(final String sequence) {
        this.sequence = sequence;
    }

    public String getLocalBic() {
        return localBic;
    }

    public void setLocalBic(final String localBic) {
        this.localBic = localBic;
    }

    @Override
    public String toString() {
        return "Event{" +
                "arrivalTime=" + arrivalTime +
                ", originTime=" + originTime +
                ", code=" + code +
                ", inputOutput='" + inputOutput + '\'' +
                ", remoteBic='" + remoteBic + '\'' +
                ", type=" + type +
                ", suffix=" + suffix +
                ", session=" + session +
                ", sequence=" + sequence +
                ", localBic='" + localBic + '\'' +
                '}';
    }

}
