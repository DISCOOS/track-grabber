package no.hvl.dowhile.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for making it easier to work with threads.
 */
public class ThreadTools {

    private static final Logger logger = LoggerFactory.getLogger(ThreadTools.class);

    /**
     * Makes the thread sleep for the specified amount of seconds.
     *
     * @param seconds amount of seconds to sleep.
     */
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            logger.error(Messages.ERROR_THREAD.get());
        }
    }
}
