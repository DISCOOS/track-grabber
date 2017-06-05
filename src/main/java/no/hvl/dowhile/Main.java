package no.hvl.dowhile;

import no.hvl.dowhile.core.OperationManager;

/**
 * Main class responsible of starting the application.
 */
public class Main {
    /**
     * Main method starting the operation manager.
     *
     * @param args arguments passed on command line.
     * @see OperationManager
     */
    public static void main(String[] args) {
        OperationManager operationManager = new OperationManager();
        operationManager.start();
    }
}
