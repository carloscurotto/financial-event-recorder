package ar.com.financial.event.recorder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@ToString
public class SummaryEvent {

    @Id
    private String session;
    private Date startSessionTime;
    private Date endSessionTime;
    private String quantityMessagesSent;
    private String quantityMessagesReceived;
    private String firstMessageSentSequence;
    private String lastMessageSentSequence;
    private String firstMessageReceivedSequence;
    private String lastMessageReceivedSequence;

    SummaryEvent(final String session,
                 final Date startSessionTime,
                 final Date endSessionTime,
                 final String quantityMessagesSent,
                 final String quantityMessagesReceived,
                 final String firstMessageSentSequence,
                 final String lastMessageSentSequence,
                 final String firstMessageReceivedSequence,
                 final String lastMessageReceivedSequence) {
        this.session = session;
        this.startSessionTime = startSessionTime;
        this.endSessionTime = endSessionTime;
        this.quantityMessagesSent = quantityMessagesSent;
        this.quantityMessagesReceived = quantityMessagesReceived;
        this.firstMessageSentSequence = firstMessageSentSequence;
        this.lastMessageSentSequence = lastMessageSentSequence;
        this.firstMessageReceivedSequence = firstMessageReceivedSequence;
        this.lastMessageReceivedSequence = lastMessageReceivedSequence;
    }

}
