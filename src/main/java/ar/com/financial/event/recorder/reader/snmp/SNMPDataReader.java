package ar.com.financial.event.recorder.reader.snmp;

import ar.com.financial.event.recorder.reader.Reader;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SNMPDataReader implements Reader<CommandResponderEvent>, CommandResponder {

    private static final Logger logger = Logger.getLogger(SNMPDataReader.class);

    private static final int EVENT_QUEUE_CAPACITY = 100;
    private static final int DISPATCHER_POOL_SIZE = 10;
    private static final String COMMUNITY = "public";

    private String host;
    private String port;
    private BlockingQueue<CommandResponderEvent> queue;
    private AbstractTransportMapping transport;
    private ThreadPool pool;
    private MessageDispatcher dispatcher;
    private Snmp snmp;

    public SNMPDataReader(final String host, final String port) {
        Validate.notBlank(host, "The host cannot be blank");
        Validate.notBlank(port, "The port cannot be blank");
        this.host = host;
        this.port = port;
    }

    @Override
    public void open() {
        try {
            SNMP4JSettings.setAllowSNMPv2InV1(true);

            queue = new ArrayBlockingQueue<>(EVENT_QUEUE_CAPACITY);

            UdpAddress address = new UdpAddress(host + "/" + port);

            transport = new DefaultUdpTransportMapping(address);

            pool = ThreadPool.create("DispatcherPool", DISPATCHER_POOL_SIZE);
            dispatcher = new MultiThreadedMessageDispatcher(pool, new MessageDispatcherImpl());

            // add message processing models
            dispatcher.addMessageProcessingModel(new MPv1());
            dispatcher.addMessageProcessingModel(new MPv2c());

            // add all security protocols
            SecurityProtocols.getInstance().addDefaultProtocols();
            SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

            //Create Target
            CommunityTarget target = new CommunityTarget();
            target.setCommunity( new OctetString(COMMUNITY));

            snmp = new Snmp(dispatcher, transport);
            snmp.addCommandResponder(this);

            transport.listen();
            logger.info("Listening on " + address);
        } catch (Exception e) {
            throw new RuntimeException("Error opening snmp data reader.", e);
        }
    }

    @Override
    public void close() {
        try {
            snmp.close();
            snmp.removeCommandResponder(this);
            dispatcher.removeCommandResponder(this);
            pool.cancel();
            transport.close();
        } catch (Exception e) {
            logger.warn("Error closing snmp reader.", e);
        }
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public CommandResponderEvent read() {
        try {
            final CommandResponderEvent event = queue.take();
            logger.debug(String.format("Reading event [%s] from snmp.", event));
            return event;
        } catch (Exception e) {
            logger.warn(String.format("Error reading event [%s]", e.getMessage()));
            return null;
        }
    }

    /**
     * This method will be called whenever a pdu is received on the given port specified in the listen() method
     */
    public synchronized void processPdu(final CommandResponderEvent event) {
        try {
            logger.debug(String.format("Processing event [%s] from snmp.", event));
            queue.put(event);
        } catch (Exception e) {
            logger.warn(String.format("Error processing event [%s]", e.getMessage()));
        }
    }

}
