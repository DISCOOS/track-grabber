package no.hvl.dowhile.utility;

/**
 * Utility methods for making it easier to work with threads.
 */
public class ThreadTools {
    /**
     * Make the thread sleep for the specified amount of seconds.
     *
     * @param seconds amount of seconds to sleep.
     */
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            System.err.println(Messages.ERROR_THREAD.get());
        }
    }
}
