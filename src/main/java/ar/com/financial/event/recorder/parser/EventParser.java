package ar.com.financial.event.recorder.parser;

import ar.com.financial.event.recorder.domain.Event;
import org.apache.commons.lang3.Validate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventParser {

    public Event parse(final String data) {
        Validate.notBlank(data, "The event cannot be blank");
        final String event = prepare(data);
        final Date arrivalTime = extractArrivalTime(event);
        final Date originTime = extractOriginTime(event);
        final int code = extractCode(event);
        final String inputOutput = extractInputOutput(event);
        final String remoteBic = extractRemoteBic(event);
        final String type = extractType(event);
        final long suffix = extractSuffix(event);
        final Long session = extractSession(event);
        final Long sequence = extractSequence(event);
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

    private int extractCode(final String event) {
        final String codeData = event.split("\\|")[21];
        return Integer.parseInt(codeData);
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
        try {
            final String messageData = event.split("\\|")[33];
            final String umidData = messageData.split(" ")[2].replace(",", "").trim();
            final String type = umidData.substring(umidData.lastIndexOf("X") + 1, umidData.length());
            /*
            if (type == null || type.trim().equals("")) {
                System.out.println("blank type");
            }
            */
            return type;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private long extractSuffix(final String event) {
        final String messageData = event.split("\\|")[33];
        final String suffixData = messageData.split(",")[1].trim();
        return Long.parseLong(suffixData.substring(7, suffixData.indexOf(':')));
    }

    private Long extractSession(final String event) {
        final String messageData = event.split("\\|")[33];
        final String suffixData = messageData.split(",")[1].trim();
        if (!suffixData.contains("Quit")) {
            final String sessionData = suffixData.split("\\.")[1].trim();
            return Long.parseLong(sessionData.substring(8, sessionData.length()));
        } else {
            return null;
        }
    }

    private Long extractSequence(final String event) {
        final String messageData = event.split("\\|")[33];
        if (!messageData.contains("Quit")) {
            final String sequenceData = messageData.split(",")[2].trim();
            int sequenceEnd = sequenceData.indexOf('.') != -1 ? sequenceData.indexOf('.') : sequenceData.indexOf('\n');
            return Long.parseLong(sequenceData.substring(4, sequenceEnd));
        } else {
            return null;
        }
    }

    private String extractLocalBic(final String event) {
        final String messageData = event.split("\\|")[33];
        final int startIndex = messageData.indexOf('{') + 6;
        final int endIndex = startIndex + 8;
        return messageData.substring(startIndex, endIndex);
    }

}
