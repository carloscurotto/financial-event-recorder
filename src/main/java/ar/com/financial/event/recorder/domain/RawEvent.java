package ar.com.financial.event.recorder.domain;

import org.apache.commons.lang3.Validate;

import java.util.Date;

public class RawEvent {

    private static final String SUMMARY_EVENT_CODE = "8061";

    // Simple Attributes
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

    // Summary Attributes
    private Date startSessionTime;
    private Date endSessionTime;
    private String quantityMessagesSent;
    private String quantityMessagesReceived;
    private String firstMessageSentSequence;
    private String lastMessageSentSequence;
    private String firstMessageReceivedSequence;
    private String lastMessageReceivedSequence;

    public RawEvent(final Date arrivalTime,
                    final Date originTime,
                    final String code,
                    final String inputOutput,
                    final String remoteBic,
                    final String type,
                    final String suffix,
                    final String session,
                    final String sequence,
                    final String localBic,
                    final Date startSessionTime,
                    final Date endSessionTime,
                    final String quantityMessagesSent,
                    final String quantityMessagesReceived,
                    final String firstMessageSentSequence,
                    final String lastMessageSentSequence,
                    final String firstMessageReceivedSequence,
                    final String lastMessageReceivedSequence) {
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
        this.startSessionTime = startSessionTime;
        this.endSessionTime = endSessionTime;
        this.quantityMessagesSent = quantityMessagesSent;
        this.quantityMessagesReceived = quantityMessagesReceived;
        this.firstMessageSentSequence = firstMessageSentSequence;
        this.lastMessageSentSequence = lastMessageSentSequence;
        this.firstMessageReceivedSequence = firstMessageReceivedSequence;
        this.lastMessageReceivedSequence = lastMessageReceivedSequence;
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

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(final String suffix) {
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

    public Date getStartSessionTime() {
        return startSessionTime;
    }

    public void setStartSessionTime(Date startSessionTime) {
        this.startSessionTime = startSessionTime;
    }

    public Date getEndSessionTime() {
        return endSessionTime;
    }

    public void setEndSessionTime(Date endSessionTime) {
        this.endSessionTime = endSessionTime;
    }

    public String getQuantityMessagesSent() {
        return quantityMessagesSent;
    }

    public void setQuantityMessagesSent(String quantityMessagesSent) {
        this.quantityMessagesSent = quantityMessagesSent;
    }

    public String getQuantityMessagesReceived() {
        return quantityMessagesReceived;
    }

    public void setQuantityMessagesReceived(String quantityMessagesReceived) {
        this.quantityMessagesReceived = quantityMessagesReceived;
    }

    public String getFirstMessageSentSequence() {
        return firstMessageSentSequence;
    }

    public void setFirstMessageSentSequence(String firstMessageSentSequence) {
        this.firstMessageSentSequence = firstMessageSentSequence;
    }

    public String getLastMessageSentSequence() {
        return lastMessageSentSequence;
    }

    public void setLastMessageSentSequence(String lastMessageSentSequence) {
        this.lastMessageSentSequence = lastMessageSentSequence;
    }

    public String getFirstMessageReceivedSequence() {
        return firstMessageReceivedSequence;
    }

    public void setFirstMessageReceivedSequence(String firstMessageReceivedSequence) {
        this.firstMessageReceivedSequence = firstMessageReceivedSequence;
    }

    public String getLastMessageReceivedSequence() {
        return lastMessageReceivedSequence;
    }

    public void setLastMessageReceivedSequence(String lastMessageReceivedSequence) {
        this.lastMessageReceivedSequence = lastMessageReceivedSequence;
    }

    public boolean isSummary() {
        return this.code.equalsIgnoreCase(SUMMARY_EVENT_CODE);
    }

    public SummaryEvent toSummary() {
        return new SummaryEvent(session, startSessionTime, endSessionTime, quantityMessagesSent, quantityMessagesReceived,
                firstMessageSentSequence, lastMessageSentSequence, firstMessageReceivedSequence, lastMessageReceivedSequence);
    }

    public SimpleEvent toSimple() {
        return new SimpleEvent(arrivalTime, originTime, code, inputOutput, remoteBic, type, suffix, session, sequence, localBic);
    }

    @Override
    public String toString() {
        return "RawEvent{" +
                "arrivalTime=" + arrivalTime +
                ", originTime=" + originTime +
                ", code='" + code + '\'' +
                ", inputOutput='" + inputOutput + '\'' +
                ", remoteBic='" + remoteBic + '\'' +
                ", type='" + type + '\'' +
                ", suffix='" + suffix + '\'' +
                ", session='" + session + '\'' +
                ", sequence='" + sequence + '\'' +
                ", localBic='" + localBic + '\'' +
                ", startSessionTime=" + startSessionTime +
                ", endSessionTime=" + endSessionTime +
                ", quantityMessagesSent='" + quantityMessagesSent + '\'' +
                ", quantityMessagesReceived='" + quantityMessagesReceived + '\'' +
                ", firstMessageSentSequence='" + firstMessageSentSequence + '\'' +
                ", lastMessageSentSequence='" + lastMessageSentSequence + '\'' +
                ", firstMessageReceivedSequence='" + firstMessageReceivedSequence + '\'' +
                ", lastMessageReceivedSequence='" + lastMessageReceivedSequence + '\'' +
                '}';
    }

}
