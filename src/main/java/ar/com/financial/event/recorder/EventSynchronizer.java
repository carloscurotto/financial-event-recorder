package ar.com.financial.event.recorder;

import org.apache.log4j.Logger;

//TODO synchronizes events from a file to the database
public class EventSynchronizer {

    private static final Logger logger = Logger.getLogger(EventSynchronizer.class);

    public void start() {
        logger.info("Starting the event synchronizer...");
    }

    public void synchronize() {
        logger.info("Synchronizing events...");
    }

    public void stop() {
        logger.info("Stopping the event synchronizer...");
    }

}