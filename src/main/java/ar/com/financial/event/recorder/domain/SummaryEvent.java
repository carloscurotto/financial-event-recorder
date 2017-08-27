package ar.com.financial.event.recorder.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class SummaryEvent {

    @Id
    @GeneratedValue
    private int id;

    private Date startSessionTime;
    private Date endSessionTime;
    private String quantityMessagesSent;
    private String quantityMessagesReceived;
    private String firstMessageSentSequence;
    private String lastMessageSentSequence;
    private String firstMessageReceivedSequence;
    private String lastMessageReceivedSequence;

    /**
     * Do not use, framework usage only.
     */
    @Deprecated
    public SummaryEvent() {}

    public SummaryEvent(final Date startSessionTime,
                        final Date endSessionTime,
                        final String quantityMessagesSent,
                        final String quantityMessagesReceived,
                        final String firstMessageSentSequence,
                        final String lastMessageSentSequence,
                        final String firstMessageReceivedSequence,
                        final String lastMessageReceivedSequence) {
        this.startSessionTime = startSessionTime;
        this.endSessionTime = endSessionTime;
        this.quantityMessagesSent = quantityMessagesSent;
        this.quantityMessagesReceived = quantityMessagesReceived;
        this.firstMessageSentSequence = firstMessageSentSequence;
        this.lastMessageSentSequence = lastMessageSentSequence;
        this.firstMessageReceivedSequence = firstMessageReceivedSequence;
        this.lastMessageReceivedSequence = lastMessageReceivedSequence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "SummaryEvent{" +
                "id=" + id +
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
