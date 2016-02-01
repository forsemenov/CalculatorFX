package asd;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;

import static javafx.scene.text.Font.font;

/**
 * Main Calculator class which store interface and other class Object`s
 */
public class CalculatorInterface extends Application {

    CalculatorLogic logic = new CalculatorLogic();

    /**
     * Size`s of screen font which changed when changed number of digits(13)
     */
    private final static int fontMaxSize = 20;

    /**
     * Size`s of screen font which changed when changed number of digits(13)
     */
    private final static int fontMinSize = 14;

    /**
     * Symbols for formatter
     */
    private final static DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    /**
     * Used pattern
     */
    private final static String pattern = "#,##0.0#";

    /**
     * Text on memory screen when empty
     */
    private final static String emptyMemoryScreen = "";

    /**
     * Text on memory screen when not empty
     */
    private final static String activatedMemoryScreen = "M";

    /**
     * Number of digits to change font.
     */
    private final static int maxCharsSmallFont = 13;

    /**
     * Interface buttons with digits IDs
     */
    private static final String digitButtonID = "digitButtonStyle";

    /**
     * Interface buttons with operations IDs
     */
    private static final String memoryButtonID = "memoryButton";

    /**
     * Interface buttons with memory IDs
     */
    private static final String operationButtonID = "operationStyle";

    /**
     * Set separators for formatter
     */
    static {
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
    }

    /**
     * Formatter for value
     */
    private final static DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);

    /**
     * Calculator`s menu bar
     */
    private MenuBar menuBar = new MenuBar();

    /**
     * Calculator`s menu
     */
    private Menu menu = new Menu("Menu");

    /**
     * Menu item Copy
     */
    private MenuItem menuItemA = new MenuItem("Copy Ctrl + C");

    /**
     * Menu item Paste
     */
    private MenuItem menuItemB = new MenuItem("Paste Ctrl + V");

    /**
     * Base class for layout panes which need to expose the children list as public so that users of the subclass can freely add/remove children.
     */
    private Pane root = new Pane();

    /**
     * Main Calculator screen
     */
    private TextField screen = new TextField();

    /**
     * Memory screen
     */
    private TextField memoryScreen = new TextField();

    /**
     * Collection with buttons and actions
     */
    private Map<Action, Button> buttonAndActionMapping = new LinkedHashMap<>();
    /**
     * Button with digit 0
     */
    private Button buttonZero = new Button();

    /**
     * Button with digit 1
     */
    private Button buttonOne = new Button();

    /**
     * Button with digit 2
     */
    private Button buttonTwo = new Button();

    /**
     * Button with digit 3
     */
    private Button buttonThree = new Button();

    /**
     * Button with digit 4
     */
    private Button buttonFour = new Button();

    /**
     * Button with digit 5
     */
    private Button buttonFive = new Button();

    /**
     * Button with digit 6
     */
    private Button buttonSix = new Button();

    /**
     * Button with digit 7
     */
    private Button buttonSeven = new Button();

    /**
     * Button with digit 8
     */
    private Button buttonEight = new Button();

    /**
     * Button with digit 9
     */
    private Button buttonNine = new Button();

    /**
     * Button with point
     */
    private Button buttonPoint = new Button();
    /**
     * Button plus
     */
    private Button buttonPlus = new Button();

    /**
     * Button equal
     */
    private Button buttonEqual = new Button();

    /**
     * Button minus
     */
    private Button buttonMinus = new Button();

    /**
     * Button multiply
     */
    private Button buttonMultiply = new Button();

    /**
     * Button 1/x
     */
    private Button buttonOneDivide = new Button();

    /**
     * Button divide
     */
    private Button buttonDivide = new Button();

    /**
     * Button %
     */
    private Button buttonPercent = new Button();

    /**
     * Button <--
     */
    private Button buttonBackSpace = new Button();

    /**
     * Button CE
     */
    private Button buttonCE = new Button();

    /**
     * Button Clear
     */
    private Button buttonC = new Button();

    /**
     * Button Negate
     */
    private Button buttonNegate = new Button();

    /**
     * Button square root
     */
    private Button buttonSQRT = new Button();


    /**
     * Button Memory Clear
     */
    private Button buttonMC = new Button();

    /**
     * Button Memory Recall
     */
    private Button buttonMR = new Button();

    /**
     * Button MemoryStore
     */
    private Button buttonMS = new Button();

    /**
     * Button Memory Add
     */
    private Button buttonMemPlus = new Button();

    /**
     * Button Memory Minus
     */
    private Button buttonMemMinus = new Button();

    /**
     * Fonts used in screen
     * Large for number of digits < 13;
     */
    private static final Font fontLarge = font(fontMaxSize);

    /**
     * Fonts used in screen
     * Small - for number of digits > 13
     */
    private static final Font fontSmall = font(fontMinSize);


    /**
     * Set key`s from Action and values - buttons
     */
    public void buttonAndActionCollectionInitialize() {
        buttonAndActionMapping.put(Action.ZERO, buttonZero);
        buttonAndActionMapping.put(Action.ONE, buttonOne);
        buttonAndActionMapping.put(Action.TWO, buttonTwo);
        buttonAndActionMapping.put(Action.THREE, buttonThree);
        buttonAndActionMapping.put(Action.FOUR, buttonFour);
        buttonAndActionMapping.put(Action.FIVE, buttonFive);
        buttonAndActionMapping.put(Action.SIX, buttonSix);
        buttonAndActionMapping.put(Action.SEVEN, buttonSeven);
        buttonAndActionMapping.put(Action.EIGHT, buttonEight);
        buttonAndActionMapping.put(Action.NINE, buttonNine);
        buttonAndActionMapping.put(Action.POINT, buttonPoint);

        buttonAndActionMapping.put(Action.PLUS, buttonPlus);
        buttonAndActionMapping.put(Action.MINUS, buttonMinus);
        buttonAndActionMapping.put(Action.MULTIPLY, buttonMultiply);
        buttonAndActionMapping.put(Action.DIVIDE, buttonDivide);
        buttonAndActionMapping.put(Action.EQUAL, buttonEqual);

        buttonAndActionMapping.put(Action.SQUARE_ROOT, buttonSQRT);
        buttonAndActionMapping.put(Action.ONE_DIVIDE, buttonOneDivide);
        buttonAndActionMapping.put(Action.PERCENT, buttonPercent);
        buttonAndActionMapping.put(Action.CLEAR, buttonC);
        buttonAndActionMapping.put(Action.CE, buttonCE);
        buttonAndActionMapping.put(Action.BACKSPACE, buttonBackSpace);
        buttonAndActionMapping.put(Action.NEGATE, buttonNegate);

        buttonAndActionMapping.put(Action.MEMORY_MINUS, buttonMemMinus);
        buttonAndActionMapping.put(Action.MEMORY_PLUS, buttonMemPlus);
        buttonAndActionMapping.put(Action.MR, buttonMR);
        buttonAndActionMapping.put(Action.MC, buttonMC);
        buttonAndActionMapping.put(Action.MS, buttonMS);
    }


    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Calculator");
        stage.setScene(new Scene(root, 205, 290));
        stage.getScene().getStylesheets().add("File:src/asd/asd.css");
        stage.setResizable(false);
        stage.show();

        memoryScreen.relocate(10, 32);
        memoryScreen.setPrefSize(30, 18);
        memoryScreen.setEditable(false);
        memoryScreen.setId("memoryScreen");
        memoryScreen.setAlignment(Pos.TOP_LEFT);

        memoryScreen.setText(emptyMemoryScreen);
        root.getChildren().add(screen);
        root.getChildren().add(memoryScreen);

        screen.setText(logic.getConverter());
        screen.relocate(7.5, 29);
        screen.setPrefSize(200, 50);
        screen.setAlignment(Pos.BOTTOM_RIGHT);
        screen.setEditable(false);
        screen.setId("screen");
        screen.setFont(fontLarge);


        stage.getIcons().add(new Image("File:resourses/images/CALCUL.png"));

        buttonAndActionCollectionInitialize();
        setButtonId();

        createButton();

        createMenu();
        buttonEqual.setDefaultButton(true);//Default Button is ENTER
        /**Make filter for keyboard action*/
        stage.addEventFilter(KeyEvent.KEY_PRESSED, this::keyboardEventListener);

    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Create button and set actions
     */
    public void createButton() {

        for (Map.Entry<Action, Button> entry : buttonAndActionMapping.entrySet()) {
            Button value = entry.getValue();
            Action key = entry.getKey();
            value.setPrefSize(35, 22);
            root.getChildren().add(value);
            value.setOnAction(new EventForButtons(key));
        }
        locateButtonSetTextAndSize();

    }


    /**
     * Locate buttons on stage and set size and text
     */
    public void locateButtonSetTextAndSize() {
        buttonZero.setPrefSize(75, 18);
        buttonZero.relocate(10, 260);
        buttonZero.setText("0");

        buttonPoint.relocate(90, 260);
        buttonPoint.setText(".");

        buttonPlus.relocate(130, 260);
        buttonPlus.setText("+");

        buttonEqual.setPrefSize(35, 60);
        buttonEqual.relocate(170, 225);
        buttonEqual.setText("=");

        buttonOne.relocate(10, 225);
        buttonOne.setText("1");

        buttonTwo.relocate(50, 225);
        buttonTwo.setText("2");

        buttonThree.relocate(90, 225);
        buttonThree.setText("3");

        buttonMinus.relocate(130, 225);
        buttonMinus.setText("-");

        buttonFour.relocate(10, 190);
        buttonFour.setText("4");

        buttonFive.relocate(50, 190);
        buttonFive.setText("5");

        buttonSix.relocate(90, 190);
        buttonSix.setText("6");

        buttonMultiply.relocate(130, 190);
        buttonMultiply.setText("*");

        buttonOneDivide.relocate(170, 190);
        buttonOneDivide.setText("1/x");

        buttonSeven.relocate(10, 155);
        buttonSeven.setText("7");

        buttonEight.relocate(50, 155);
        buttonEight.setText("8");

        buttonNine.relocate(90, 155);
        buttonNine.setText("9");

        buttonDivide.relocate(130, 155);
        buttonDivide.setText("/");

        buttonPercent.relocate(170, 155);
        buttonPercent.setText("%");

        buttonBackSpace.relocate(10, 120);
        buttonBackSpace.setText("\u2190\n");

        buttonCE.relocate(50, 120);
        buttonCE.setText("CE");

        buttonC.relocate(90, 120);
        buttonC.setText("C");

        buttonNegate.relocate(130, 120);
        buttonNegate.setText("\u00B1");

        buttonSQRT.relocate(170, 120);
        buttonSQRT.setText("\u221A\n");

        buttonMC.relocate(10, 85);
        buttonMC.setText("MC");

        buttonMR.relocate(50, 85);
        buttonMR.setText("MR");

        buttonMS.relocate(90, 85);
        buttonMS.setText("MS");

        buttonMemPlus.relocate(130, 85);
        buttonMemPlus.setText("M+");

        buttonMemMinus.relocate(170, 85);
        buttonMemMinus.setText("M-");
    }

    /**
     * Set button Id for css
     */
    public void setButtonId() {


        for (Button b : Arrays.asList(buttonOne, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine, buttonZero, buttonPoint)) {
            b.setId(digitButtonID);
        }
        for (Button b : Arrays.asList(buttonPlus, buttonMinus, buttonEqual, buttonMultiply, buttonDivide, buttonSQRT, buttonOneDivide, buttonPercent, buttonC, buttonCE, buttonBackSpace, buttonNegate)) {
            b.setId(operationButtonID);
        }
        for (Button b : Arrays.asList(buttonMemPlus, buttonMemMinus, buttonMC, buttonMR, buttonMS)) {
            b.setId(memoryButtonID);
        }

    }

    /**
     * Set font for small, number of digits(<13)
     */

    public void setFontMinSize() {
        screen.setFont(fontSmall);
    }

    /**
     * Set font for bigger, number of digits(>13)
     */
    public void setFontMaxSize() {
        screen.setFont(fontLarge);
    }

    /**
     * Get number of digits on the screen
     *
     * @return number of digits on the screen
     */
    public int getScreenLength() {
        return screen.getText().length();
    }

    /**
     * Change font depending on the number of digits
     */
    public void changeFont() {

        if (getScreenLength() > maxCharsSmallFont) {
            setFontMinSize();
        } else {
            setFontMaxSize();
        }
    }

    /**
     * Listener for keyboard input
     *
     * @param keyEvent keyboard event
     */
    public void keyboardEventListener(KeyEvent keyEvent) {

        KeyCode eventCode = keyEvent.getCode();
        for (Map.Entry<Action, Button> entry : buttonAndActionMapping.entrySet()) {
            Button value = entry.getValue();
            if (value.getText().equals(keyEvent.getText())) {
                value.fire();
                break;
            }
        }
        if (eventCode == KeyCode.BACK_SPACE) {
            buttonBackSpace.fire();
        } else if (eventCode == KeyCode.DELETE || eventCode == KeyCode.ESCAPE) {
            buttonC.fire();
        }

        if (keyEvent.isControlDown()) {//Set key combination for calling Copy and Paste
            if (eventCode == KeyCode.C) {
                menuItemA.fire();//Ctrl + C for copy
            } else if (eventCode == KeyCode.V) {
                menuItemB.fire();//Ctrl + V for paste
            }
        }

    }

    /**
     * Bind buttons with calculator logic actions
     *
     * @param action - calculator action
     */
    public void performAction(Action action) {

        switch (action) {
            case CLEAR:
                logic.clear();
                break;
            case ONE:
                logic.digitActions(1);
                break;
            case TWO:
                logic.digitActions(2);
                break;
            case THREE:
                logic.digitActions(3);
                break;
            case FOUR:
                logic.digitActions(4);
                break;
            case FIVE:
                logic.digitActions(5);
                break;
            case SIX:
                logic.digitActions(6);
                break;
            case SEVEN:
                logic.digitActions(7);
                break;
            case EIGHT:
                logic.digitActions(8);
                break;
            case NINE:
                logic.digitActions(9);
                break;
            case ZERO:
                logic.digitActions(0);
                break;
            case POINT:
                logic.pointAction();
                break;
            case MULTIPLY:
                logic.multiplyAction();
                break;
            case DIVIDE:
                logic.divideAction();
                break;
            case PLUS:
                logic.addAction();
                break;
            case MINUS:
                logic.deductAction();
                break;
            case EQUAL:
                logic.equalsAction();
                break;
            case SQUARE_ROOT:
                logic.squareRoot();
                break;
            case ONE_DIVIDE:
                logic.oneDivideValue();
                break;
            case PERCENT:
                logic.percent();
                break;
            case BACKSPACE:
                logic.backspace();
                break;
            case CE:
                logic.actionCE();
                break;
            case NEGATE:
                logic.negateAction();
                break;
            case MEMORY_MINUS:
                logic.memorySubtractAction();
                setMemScreenText();
                break;
            case MEMORY_PLUS:
                logic.memoryAddAction();
                setMemScreenText();
                break;
            case MR:
                logic.actionMR();
                setMemScreenText();
                break;
            case MS:
                logic.actionMS();
                setMemScreenText();
                break;
            case MC:
                logic.actionMC();
                setMemScreenText();
                break;
        }
        screen.setText(logic.getConverter());
    }

    /**
     * Change text on memory screen
     */
    public void setMemScreenText() {
        String memSignal;
        if (logic.compareMemoryToNull()) {
            memSignal = emptyMemoryScreen;
        } else {
            memSignal = activatedMemoryScreen;
        }
        memoryScreen.setText(memSignal);
    }


    /**
     * Create menu and set style and size
     */
    public void createMenu() {

        Clipboard clipboard = Clipboard.getSystemClipboard();//set calculator clipboard as a system clipboard
        ClipboardContent content = new ClipboardContent();

        menu.getItems().addAll(menuItemA, menuItemB);

        menuBar.getMenus().add(menu);
        menuBar.prefWidthProperty().bind(root.widthProperty());

        root.getChildren().add(menuBar);

        menuItemA.setOnAction(new EventForMenuCopy(content, clipboard));
        menuItemB.setOnAction(new EventForMenuPaste(clipboard));
    }

    /**
     * Getter for collection
     *
     * @return collection of buttons
     */
    public Map<Action, Button> getButtonAndActionMapping() {
        return buttonAndActionMapping;
    }

    /**
     * Getter for textField
     *
     * @return screen
     */
    public TextField getScreen() {
        return screen;
    }

    /**
     * Event handler for menu copy
     */
    private class EventForMenuCopy implements EventHandler<ActionEvent> {
        /**
         * Clipboard content
         */
        ClipboardContent content;

        /**
         * Calculator clipboard
         */
        Clipboard clipboard;

        EventForMenuCopy(ClipboardContent content, Clipboard clipboard) {
            this.content = content;
            this.clipboard = clipboard;
        }

        @Override
        public void handle(ActionEvent e) {
            screen.requestFocus();
            content.putString(screen.getText());
            clipboard.setContent(content);
        }
    }

    /**
     * Event handler for menu paste
     */
    private class EventForMenuPaste implements EventHandler<ActionEvent> {
        /**
         * Calculator clipboard
         */
        Clipboard clipboard;

        EventForMenuPaste(Clipboard clipboard) {
            this.clipboard = clipboard;
        }

        @Override
        public void handle(ActionEvent n) {
            screen.setText(clipboard.getString());
            decimalFormat.setParseBigDecimal(true);
            try {
                logic.setValue((BigDecimal) decimalFormat.parse(screen.getText()));
            } catch (ParseException e) {//if can`t parse to BigDecimal, or unsuited input format(if clipboard content already contains some object)
                screen.setText(logic.getConverter());//set screen which were before "try"
            }
            logic.setStackIfFirstAction();
            changeFont();
        }
    }

    /**
     * Event handler for buttons.
     */
    private class EventForButtons implements EventHandler<ActionEvent> {
        /**
         * Calculator action
         */
        Action action;

        EventForButtons(Action action) {
            this.action = action;
        }

        @Override
        public void handle(ActionEvent event) {
            performAction(action);
            changeFont();
        }
    }

}


