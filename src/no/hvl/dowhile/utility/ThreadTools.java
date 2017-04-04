package no.hvl.dowhile.utility;

public class ThreadTools {
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            System.err.println(Messages.ERROR_THREAD.get());
        }
    }
}
