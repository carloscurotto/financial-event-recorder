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
@AllArgsConstructor
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

}
