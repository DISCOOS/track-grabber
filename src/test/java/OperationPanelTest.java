import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.gui.Window;
import no.hvl.dowhile.utility.Messages;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class OperationPanelTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
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
                operationManager.setupLocalFolders(temporaryFolder.newFolder("TEST_C_FOLDER_" + System.currentTimeMillis()));
                windowFixture = new FrameFixture(window);
            }
        });
    }

    @Test
    public void errorMessageOnInvalidCharactersInOperationName() {
        windowFixture.button("newOperationButton").click();
        windowFixture.textBox("operationNameInput").enterText("TestWrong-.-");
        windowFixture.button("registerNewButton").click();
        windowFixture.label("errorMessageLabel").requireText(Messages.INVALID_OPERATION_NAME.get());
    }

    @Test
    public void newOperationFieldsIsVisibleAfterClick() {
        JButtonFixture newOperationButton = windowFixture.button("newOperationButton");
        JLabelFixture operationNameLabel = windowFixture.label("operationNameLabel");
        JTextComponentFixture operationNameInput = windowFixture.textBox("operationNameInput");
        JLabelFixture operationDateLabel = windowFixture.label("operationDateLabel");
        JButtonFixture registerNewButton = windowFixture.button("registerNewButton");

        newOperationButton.requireVisible();

        operationNameLabel.requireNotVisible();
        operationNameInput.requireNotVisible();
        operationDateLabel.requireNotVisible();
        registerNewButton.requireNotVisible();

        newOperationButton.click();

        operationNameLabel.requireVisible();
        operationNameInput.requireVisible();
        operationDateLabel.requireVisible();
        registerNewButton.requireVisible();
    }

    @Test
    public void newOperationFieldsIsHiddenAfterBackButtonClick() {

    }

    @Test
    public void existingOperationFieldsIsVisibleAfterClick() {
        windowFixture.button("existingOperationButton").click();
        windowFixture.comboBox("existingOperationInput").requireVisible();
    }

    @Test
    public void existingOperationFieldsIsHiddenAfterBackButtonClick() {

    }

    /*@Test
    public void errorMessageWhenTryingToCreateExistingNameOnOperation() {
        windowFixture.button("newOperationButton").click();
        windowFixture.textBox("operationNameInput").enterText("Tester GUI på Voss");
        windowFixture.button("registerNewButton").click();
        windowFixture.label("errorMessageLabel").requireText(Messages.OPERATION_NAME_ALREADY_EXISTS.get());
    }*/

    @After
    public void tearDown() {
        windowFixture.cleanUp();
    }
}
