package ar.com.financial.event.recorder.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class RawEvent {

    private static final String SUMMARY_EVENT_CODE = "8061";

    // Simple Attributes
    private Date originTime;
    private Date arrivalTime;
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

    public RawEvent(final Date originTime,
                    final Date arrivalTime,
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
        Validate.notNull(originTime, "The origin time cannot be null");
        Validate.notNull(arrivalTime, "The arrival time cannot be null");
        Validate.notNull(inputOutput, "The input output cannot be null");
        Validate.notBlank(remoteBic, "The remote bic cannot be blank");
        Validate.notBlank(localBic, "The local bic cannot be blank");
        this.originTime = originTime;
        this.arrivalTime = arrivalTime;
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

    public boolean isSummary() {
        return this.code.equalsIgnoreCase(SUMMARY_EVENT_CODE);
    }

    public SummaryEvent toSummary() {
        return new SummaryEvent(session, startSessionTime, endSessionTime, quantityMessagesSent, quantityMessagesReceived,
                firstMessageSentSequence, lastMessageSentSequence, firstMessageReceivedSequence, lastMessageReceivedSequence);
    }

    public SimpleEvent toSimple() {
        return new SimpleEvent(originTime, arrivalTime, code, inputOutput, remoteBic, type, suffix, session, sequence, localBic);
    }

}
