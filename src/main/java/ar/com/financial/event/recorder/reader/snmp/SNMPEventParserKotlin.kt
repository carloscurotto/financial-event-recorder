package ar.com.financial.event.recorder.reader.snmp

import ar.com.financial.event.recorder.domain.RawEvent
import org.apache.log4j.Logger
import org.snmp4j.CommandResponderEvent
import org.snmp4j.smi.OID
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class SNMPEventParserKotlin(private val eventCodes: List<String>) {

    private val logger = Logger.getLogger(this.javaClass)

    init {
        require(this.eventCodes.isNotEmpty(), { "The list of event codes cannot be empty" })
    }

    fun parse(data: CommandResponderEvent): RawEvent? {
        if (!isValidEvent(data)) return null

        val originTime = getOriginTime(data)
        val code = getCode(data)
        val inputOutput = getInputOutput(data)
        val remoteBic = getRemoteBic(data)
        val type = getType(data)
        val suffix = getSuffix(data)
        val session = getSession(data)
        val sequence = getSequence(data)
        val localBic = getLocalBic(data)
        val startSessionTime = getStartSessionTime(data)
        // TODO: complete with remaining fields
        return null
    }

    private fun isValidEvent(data: CommandResponderEvent): Boolean {
        logger.debug(String.format("Message received in parser [%s].", data))
        val code = data.getMessageData("1.3.6.1.4.1.18494.2.1.5")
        if (!this.eventCodes.contains(code)) {
            logger.debug("Message received in parser with code [$code]. Not recognized.")
            return false
        }
        return true
    }

    private fun getOriginTime(data: CommandResponderEvent): Date? {
        val time = data.getMessageData("1.3.6.1.4.1.18494.2.1.3")
        val date = data.getMessageData("1.3.6.1.4.1.18494.2.1.2")
        return parseDate("$time $date", "HH:mm:ss dd/MM/yyyy")
    }

    private fun getCode(data: CommandResponderEvent): String {
        return data.getMessageData("1.3.6.1.4.1.18494.2.1.5")
    }

    private fun getInputOutput(data: CommandResponderEvent): String? {
        val messageData = data.getMessageData("1.3.6.1.4.1.18494.2.1.9")
        return messageData.extract("(.+UMID )([A-Z]{1})")
    }

    private fun getRemoteBic(data: CommandResponderEvent): String? {
        val messageData = data.getMessageData("1.3.6.1.4.1.18494.2.1.9")
        return messageData.extract("(.+UMID )([A-Z]{1})([A-Z0-9]{8})", 3)
    }

    private fun getType(data: CommandResponderEvent): String? {
        val messageData = data.getMessageData("1.3.6.1.4.1.18494.2.1.9")
        return messageData.extract("(.+UMID )([A-Z]{1})([A-Z0-9]{11})([0-9]{1,3})", 4)
    }

    private fun getSuffix(data: CommandResponderEvent): String? {
        val messageData = data.getMessageData("1.3.6.1.4.1.18494.2.1.9")
        return messageData.extract("(.+UMID )(.+Suffix )([0-9]+)", 3)
    }

    private fun getSession(data: CommandResponderEvent): String? {
        val messageData = data.getMessageData("1.3.6.1.4.1.18494.2.1.9")
        if (messageData.contains("Quit")) {
            return messageData.extract("(\\{1:.{15})([0-9]{4})", 2)
        }
        return messageData.extract("(.+UMID )(.+Suffix )([0-9]+)(:.+Session )([0-9]+)(, [A-Za-z]+ )([0-9]+)", 5)
    }

    private fun getSequence(data: CommandResponderEvent): String? {
        val messageData = data.getMessageData("1.3.6.1.4.1.18494.2.1.9")
        if (messageData.contains("Quit")) {
            return messageData.extract("(\\{1:.{15})([0-9]{4})([0-9]{6})", 3)
        }
        return messageData.extract("(.+UMID )(.+Suffix )([0-9]+)(:.+Session )([0-9]+)(, [A-Za-z]+ )([0-9]+)", 7)
    }

    private fun getLocalBic(data: CommandResponderEvent): String? {
        val messageData = data.getMessageData("1.3.6.1.4.1.18494.2.1.9")
        return messageData.extract("(\\{1:.{3})([A-Z0-9]{8})", 2)
    }

    private fun getStartSessionTime(data: CommandResponderEvent): Date? {
        val messageData = data.getMessageData("1.3.6.1.4.1.18494.2.1.9")
        val date = messageData.extract("(\\{331:.{4})([0-9]{10})", 2)
        return date?.let { parseDate(it, "yyMMddHHmm") }
    }

}

private fun String.extract(regex: String, group: Int = 2): String? {
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return if (matcher.find()) matcher.group(group) else null
}

private fun parseDate(date: String, pattern: String): Date? {
    val formatter = SimpleDateFormat(pattern)
    return formatter.parse(date)

}

private fun CommandResponderEvent.getMessageData(oid: String): String {
    return this.pdu.getVariable(OID(oid)).toString()
}
