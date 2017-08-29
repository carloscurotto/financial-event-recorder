package ar.com.financial.event.recorder.reader.file;

import ar.com.financial.event.recorder.domain.RawEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileEventParser {

    public static void main(final String[] args) {
        final String data = "21:00:20 07/24/17|0|192.168.7.54|1|1.3.6.1.4.1.18494.2|6|1|1.3.6.1.4.1.18494.2.1.1|String|saasbarv7|1.3.6.1.4.1.18494.2.1.2|String|24/07/2017|1.3.6.1.4.1.18494.2.1.3|String|21:00:06|1.3.6.1.4.1.18494.2.1.4|String|SIS|1.3.6.1.4.1.18494.2.1.5|Integer|8061|1.3.6.1.4.1.18494.2.1.6|String|Info|1.3.6.1.4.1.18494.2.1.7|String|Communication|1.3.6.1.4.1.18494.2.1.8|String|Quit ACK Received|1.3.6.1.4.1.18494.2.1.9|String|Message UMID IXXXXXXXXXXX05, Suffix 170724452499: Quit ACK received: {1:F25MARIARBAAXXX4363086810}{4:{331:436317072406011707242100000000013000058086798086810113504113561}}|";

        FileEventParser parser = new FileEventParser();
        RawEvent event = parser.parse(data);

        System.out.println(event);
    }

    public RawEvent parse(final String data) {
        Validate.notBlank(data, "The event cannot be blank");
        final String event = prepare(data);
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
        return new RawEvent(arrivalTime, originTime, code, inputOutput, remoteBic, type, suffix, session, sequence,
                localBic, startSessionTime, endSessionTime, quantityMessagesSent, quantityMessagesReceived,
                firstMessageSentSequence, lastMessageSentSequence, firstMessageReceivedSequence, lastMessageReceivedSequence);
    }

    private String prepare(String data) {
        final String rawData = data != null ? data.trim() : null;
        if (rawData == null) {
            return null;
        } else {
            return rawData;
        }
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

    private String extractSuffix(final String event) {
        final String messageData = event.split("\\|")[33];
        final String suffixData = messageData.split(",")[1].trim();
        final String suffix = suffixData.substring(7, suffixData.indexOf(':'));
        if (StringUtils.isNotBlank(suffix)) {
            return suffix.trim();
        } else {
            return StringUtils.EMPTY;
        }
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

    private Date extractStartSessionTime(final String event) {
        try {
            Pattern pattern = Pattern.compile("(\\{331:.{4})([0-9]{10})");
            Matcher matcher = pattern.matcher(event);
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

    private Date extractEndSessionTime(final String event) {
        try {
            Pattern pattern = Pattern.compile("(\\{331:.{14})([0-9]{10})");
            Matcher matcher = pattern.matcher(event);
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

    private String extractQuantityMessagesSent(final String event) {
        Pattern pattern = Pattern.compile("(\\{331:.{27})([0-9]{6})");
        Matcher matcher = pattern.matcher(event);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractQuantityMessagesReceived(final String event) {
        Pattern pattern = Pattern.compile("(\\{331:.{33})([0-9]{6})");
        Matcher matcher = pattern.matcher(event);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractFirstMessageSentSequence(final String event) {
        Pattern pattern = Pattern.compile("(\\{331:.{39})([0-9]{6})");
        Matcher matcher = pattern.matcher(event);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractLastMessageSentSequence(final String event) {
        Pattern pattern = Pattern.compile("(\\{331:.{45})([0-9]{6})");
        Matcher matcher = pattern.matcher(event);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractFirstMessageReceivedSequence(final String event) {
        Pattern pattern = Pattern.compile("(\\{331:.{51})([0-9]{6})");
        Matcher matcher = pattern.matcher(event);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

    private String extractLastMessageReceivedSequence(final String event) {
        Pattern pattern = Pattern.compile("(\\{331:.{57})([0-9]{6})");
        Matcher matcher = pattern.matcher(event);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return null;
        }
    }

}