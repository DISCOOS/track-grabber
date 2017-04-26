package no.hvl.dowhile.tests.guitests;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.gui.OperationPanel;
import no.hvl.dowhile.core.gui.Window;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;


/**
 * Created by JonKjetil on 26.04.2017.
 */
public class OperationPanelTest {
    private FrameFixture window;
    private OperationManager operationManager;

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setUp() throws IOException {
        operationManager = new OperationManager();
        Window w = GuiActionRunner.execute(() ->new Window(operationManager));
        w.openOperationPanel();
        window = new FrameFixture(w);
        window.show();
    }

    @Test
    public void test() {


    }

    @After
    public void tearDown() {
        window.cleanUp();
    }
}
