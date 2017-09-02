package ar.com.financial.event.recorder.reader.snmp;

import ar.com.financial.event.recorder.domain.RawEvent;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.smi.OID;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SNMPEventParser {

    private static final Logger logger = Logger.getLogger(SNMPEventParser.class);

    private final Collection<String> eventCodes;

    public SNMPEventParser(final Collection<String> eventCodes) {
        Validate.notEmpty(eventCodes, "The event codes cannot be empty");
        this.eventCodes = new ArrayList<>(eventCodes);
    }

    public RawEvent parse(final CommandResponderEvent data) {
            Validate.notNull(data, "The event cannot be null");
        final CommandResponderEvent event = prepare(data);
        if (event == null) {
            return null;
        }
        final Date arrivalTime = extractArrivalTime(event);
        final Date originTime = extractOriginTime(event);
        final String code = extractCode(event);
        final String inputOutput = extractInputOutput(event);
        final String remoteBic = extractRemoteBic(event);
        final String type = extractType(event);
        final String suffix = extractSuffix(event);
        final String session = extractSession(event);
        final String sequence = extractSequence(event);
        final String localBic = extractLocalBic(event);
        final Date startSessionTime = extractStartSessionTime(event);
        final Date endSessionTime = extractEndSessionTime(event);
        final String quantityMessagesSent = extractQuantityMessagesSent(event);
        final String quantityMessagesReceived = extractQuantityMessagesReceived(event);
        final String firstMessageSentSequence = extractFirstMessageSentSequence(event);
        final String lastMessageSentSequence = extractLastMessageSentSequence(event);
        final String firstMessageReceivedSequence = extractFirstMessageReceivedSequence(event);
        final String lastMessageReceivedSequence = extractLastMessageReceivedSequence(event);
        return new RawEvent(originTime, arrivalTime, code, inputOutput, remoteBic, type, suffix, session, sequence,
                localBic, startSessionTime, endSessionTime, quantityMessagesSent, quantityMessagesReceived,
                firstMessageSentSequence, lastMessageSentSequence, firstMessageReceivedSequence, lastMessageReceivedSequence);
    }

    private CommandResponderEvent prepare(final CommandResponderEvent data) {
        logger.debug(String.format("Message received in parser [%s].", data));
        final String code = extractCode(data);
        if (code == null || !eventCodes.contains(code)) {
            logger.debug("Message received in parser with code [" +  code + "]. Not recognized.");
            return null;
        }
        logger.debug("Message received in parser with code [" +  code + "]. Ready to be processed.");
        return data;
    }

    private Date extractArrivalTime(final CommandResponderEvent event) {
        return new Date();
    }

    private Date extractOriginTime(final CommandResponderEvent event) {
        try {
            final DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            final String time = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.3")).toString();
            final String date = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.2")).toString();
            final String arrivalDateData = time + " " + date;
            return formatter.parse(arrivalDateData);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractCode(final CommandResponderEvent event) {
        return event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.5")).toString();
    }

    private String extractInputOutput(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(.+UMID )([A-Z]{1})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractRemoteBic(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(.+UMID )([A-Z]{1})([A-Z0-9]{8})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(3);
        } else {
            return null;
        }
    }

    private String extractType(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(.+UMID )([A-Z]{1})([A-Z0-9]{11})([0-9]{1,3})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(4);
        } else {
            return null;
        }
    }

    private String extractSuffix(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(.+UMID )(.+Suffix )([0-9]+)");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(3);
        } else {
            return null;
        }
    }

    private String extractSession(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        if (!messageData.contains("Quit")) {
            Pattern pattern = Pattern.compile("(.+UMID )(.+Suffix )([0-9]+)(:.+Session )([0-9]+)(, [A-Za-z]+ )([0-9]+)");
            Matcher matcher = pattern.matcher(messageData);
            if (matcher.find()) {
                return matcher.group(5);
            } else {
                return null;
            }
        } else {
                Pattern pattern = Pattern.compile("(\\{1:.{15})([0-9]{4})");
            Matcher matcher = pattern.matcher(messageData);
            if (matcher.find()) {
                return matcher.group(2);
            } else {
                return null;
            }
        }
    }

    private String extractSequence(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        if (!messageData.contains("Quit")) {
            Pattern pattern = Pattern.compile("(.+UMID )(.+Suffix )([0-9]+)(:.+Session )([0-9]+)(, [A-Za-z]+ )([0-9]+)");
            Matcher matcher = pattern.matcher(messageData);
            if (matcher.find()) {
                return matcher.group(7);
            } else {
                return null;
            }
        } else {
            Pattern pattern = Pattern.compile("(\\{1:.{15})([0-9]{4})([0-9]{6})");
            Matcher matcher = pattern.matcher(messageData);
            if (matcher.find()) {
                return matcher.group(3);
            } else {
                return null;
            }
        }
    }

    private String extractLocalBic(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(\\{1:.{3})([A-Z0-9]{8})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private Date extractStartSessionTime(final CommandResponderEvent event) {
        try {
            final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
            Pattern pattern = Pattern.compile("(\\{331:.{4})([0-9]{10})");
            Matcher matcher = pattern.matcher(messageData);
            if (matcher.find()) {
                final String startSessionTimeData = matcher.group(2);
                final DateFormat formatter = new SimpleDateFormat("yyMMddHHmm");
                return formatter.parse(startSessionTimeData);
            } else {
                return null;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date extractEndSessionTime(final CommandResponderEvent event) {
        try {
            final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
            Pattern pattern = Pattern.compile("(\\{331:.{14})([0-9]{10})");
            Matcher matcher = pattern.matcher(messageData);
            if (matcher.find()) {
                final String endSessionTimeData = matcher.group(2);
                final DateFormat formatter = new SimpleDateFormat("yyMMddHHmm");
                return formatter.parse(endSessionTimeData);
            } else {
                return null;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractQuantityMessagesSent(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(\\{331:.{27})([0-9]{6})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractQuantityMessagesReceived(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(\\{331:.{33})([0-9]{6})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractFirstMessageSentSequence(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(\\{331:.{39})([0-9]{6})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractLastMessageSentSequence(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(\\{331:.{45})([0-9]{6})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractFirstMessageReceivedSequence(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(\\{331:.{51})([0-9]{6})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractLastMessageReceivedSequence(final CommandResponderEvent event) {
        final String messageData = event.getPDU().getVariable(new OID("1.3.6.1.4.1.18494.2.1.9")).toString();
        Pattern pattern = Pattern.compile("(\\{331:.{57})([0-9]{6})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

}
