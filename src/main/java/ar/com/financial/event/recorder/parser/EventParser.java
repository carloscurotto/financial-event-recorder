package ar.com.financial.event.recorder.parser;

import ar.com.financial.event.recorder.domain.Event;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventParser {

    public static void main(final String[] args) {
        final String data = "10:54:44 07/24/17|0|192.168.7.54|1|1.3.6.1.4.1.18494.2|6|1|1.3.6.1.4.1.18494.2.1.1|String|saasbarv7|1.3.6.1.4.1.18494.2.1.2|String|24/07/2017|1.3.6.1.4.1.18494.2.1.3|String|10:54:30|1.3.6.1.4.1.18494.2.1.4|String|SIS|1.3.6.1.4.1.18494.2.1.5|Integer|8065|1.3.6.1.4.1.18494.2.1.6|String|Info|1.3.6.1.4.1.18494.2.1.7|String|Message|1.3.6.1.4.1.18494.2.1.8|String|FIN Message acked|1.3.6.1.4.1.18494.2.1.9|String|Message UMID IBFOFNL2RXXX9400000000000001273, Suffix 170722452382: acked by SWIFT. Session 4363, ISN 86798\n" +
                "Ack received: {1:F21MARIARBAAXXX4363086798}{4:{177:1707241054}{451:0}}|";

        EventParser parser = new EventParser();
        Event event = parser.parse(data);

        System.out.println(event);
    }

    public Event parse(final String data) {
        Validate.notBlank(data, "The event cannot be blank");
        final String event = prepare(data);
        final Date arrivalTime = extractArrivalTime(event);
        final Date originTime = extractOriginTime(event);
        final String code = extractCode(event);
        final String inputOutput = extractInputOutput(event);
        final String remoteBic = extractRemoteBic(event);
        final String type = extractType(event);
        final long suffix = extractSuffix(event);
        final String session = extractSession(event);
        final String sequence = extractSequence(event);
        final String localBic = extractLocalBic(event);
        return new Event(arrivalTime, originTime, code, inputOutput, remoteBic, type, suffix, session, sequence, localBic);
    }

    private String prepare(String data) {
        return data.trim();
    }

    private Date extractArrivalTime(final String event) {
        try {
            final DateFormat formatter = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
            final String arrivalDateData = event.split("\\|")[0];
            return formatter.parse(arrivalDateData);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date extractOriginTime(final String event) {
        try {
            final DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
            final String originDateDay = event.split("\\|")[12];
            final String originDateHour = event.split("\\|")[15];
            final String originDateData = originDateHour + " " + originDateDay;
            return formatter.parse(originDateData);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractCode(final String event) {
        final String codeData = event.split("\\|")[21];
        if (StringUtils.isNotBlank(codeData)) {
            return codeData.trim();
        } else {
            return StringUtils.EMPTY;
        }
    }

    private String extractInputOutput(final String event) {
        final String messageData = event.split("\\|")[33];
        final String umidData = messageData.split(" ")[2];
        return Character.toString(umidData.charAt(0));
    }

    private String extractRemoteBic(final String event) {
        final String messageData = event.split("\\|")[33];
        final String umidData = messageData.split(" ")[2];
        return umidData.substring(1, 9);
    }

    private String extractType(final String event) {
        final String messageData = event.split("\\|")[33];
        Pattern pattern = Pattern.compile("(.+UMID )(.{12})([0-9]{1,3})");
        Matcher matcher = pattern.matcher(messageData);
        if (matcher.find()) {
            return matcher.group(3);
        } else {
            return null;
        }
    }

    private long extractSuffix(final String event) {
        final String messageData = event.split("\\|")[33];
        final String suffixData = messageData.split(",")[1].trim();
        return Long.parseLong(suffixData.substring(7, suffixData.indexOf(':')));
    }

    private String extractSession(final String event) {
        final String messageData = event.split("\\|")[33];
        final String suffixData = messageData.split(",")[1].trim();
        if (!suffixData.contains("Quit")) {
            final String sessionData = suffixData.split("\\.")[1].trim();
            if (StringUtils.isNotBlank(sessionData)) {
                return sessionData.substring(8, sessionData.length()).trim();
            } else {
                return StringUtils.EMPTY;
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

    private String extractSequence(final String event) {
        final String messageData = event.split("\\|")[33];
        if (!messageData.contains("Quit")) {
            final String sequenceData = messageData.split(",")[2].trim();
            if (StringUtils.isNotBlank(sequenceData)) {
                int sequenceEnd = sequenceData.indexOf('.') != -1 ?
                        sequenceData.indexOf('.') : sequenceData.indexOf(System.lineSeparator());
                return sequenceData.substring(4, sequenceEnd).trim();
            } else {
                return StringUtils.EMPTY;
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

    private String extractLocalBic(final String event) {
        final String messageData = event.split("\\|")[33];
        final int startIndex = messageData.indexOf('{') + 6;
        final int endIndex = startIndex + 8;
        return messageData.substring(startIndex, endIndex);
    }

}