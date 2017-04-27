import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.gui.Window;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class OperationPanelTest {
    private OperationManager operationManager;
    private Window window;
    private FrameFixture windowFixture;

    @BeforeClass
    public static void setupOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setup() throws IOException {
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                operationManager = new OperationManager();
                window = new Window(operationManager);
                windowFixture = new FrameFixture(window);
            }
        });
    }

    @Test
    public void test() {
        windowFixture.button("newOperationButton").click();
        windowFixture.textBox("operationNameInput").enterText("Tester GUI på Voss");
        windowFixture.button("registerNewButton").click();
        windowFixture.label("operationInfoName").requireText("Tester GUI på Voss");
    }

    @After
    public void tearDown() {
        windowFixture.cleanUp();
    }
}
