package ar.com.financial.event.recorder.reader.snmp;

import ar.com.financial.event.recorder.domain.RawEvent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matcher.*;
import static org.mockito.Mockito.*;

public class SNMPEventParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createWithNullCodes() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("The event codes cannot be empty");

        new SNMPEventParser(null);
    }

    @Test
    public void createWithEmptyCodes() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The event codes cannot be empty");

        new SNMPEventParser(Collections.emptyList());
    }

    @Test
    public void parseMessageNull() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("The event cannot be null");

        final SNMPEventParser parser = new SNMPEventParser(Collections.singletonList("8066"));

        parser.parse(null);
    }

    @Test
    public void parseMessage8066() {
        final SNMPEventParser parser = new SNMPEventParser(Collections.singletonList("8066"));

        RawEvent actualRawEent = parser.parse(createMessage8066());

        final Date expectedArrivalTime = new Date();

//        RawEvent actualRawEvent = new RawEvent();

        /*
        * final Date originTime,
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
                    final String lastMessageReceivedSequence
        *
        * */

        assertThat(actualRawEent, is(notNullValue()));
        assertThat(actualRawEent.isSummary(), is(false));
    }

    private CommandResponderEvent createMessage8066() {
        MessageDispatcher mockDispatcher = mock(MessageDispatcher.class);

        PDU trap = new PDU();
        trap.setType(PDU.TRAP);
        trap.add(new VariableBinding(new OID("1.3.6.1.4.1.18494.2.1.2"), new OctetString("27/08/2017")));
        trap.add(new VariableBinding(new OID("1.3.6.1.4.1.18494.2.1.3"), new OctetString("19:18:40")));
        trap.add(new VariableBinding(new OID("1.3.6.1.4.1.18494.2.1.5"), new OctetString("8066")));
        trap.add(new VariableBinding(new OID("1.3.6.1.4.1.18494.2.1.9"), new OctetString("Message UMID OEANDXXXXXXX082, Suffix 170724452394: Received. Session 1969, OSN 43001.\n" +
                "Message: message text not journalised\n" +
                "ACK sent: {1:F21BPNEARBAAXXX1969043001}{4:{177:1707240601}{451:0}}")));

        return new CommandResponderEvent(mockDispatcher, null, null,
                0, 0, "test-security-name".getBytes(), 0, null,
                trap, 0, null);
    }

}
