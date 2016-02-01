package asd;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


import java.awt.*;


import java.util.Map;


public class CalculatorInterfaceTest {
    static {
        new JFXPanel();//initializing javafx environment
    }

    private static CalculatorInterface calculator = new CalculatorInterface();
    TextField screen = calculator.getScreen();

    String negate = "\u00B1";
    String sqrt = "\u221A\n";
    String backSpace = "\u2190\n";


    private Robot robot ;

    {
        try {
            robot =new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }


    private static void startCalc() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    calculator.start(stage);
                    stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @BeforeClass
    public static void startBefore() {
        startCalc();
    }

    private void fireButton(Action button) throws InterruptedException {
        final boolean[] flag = {true};
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                calculator.getButtonAndActionMapping().get(button).fire();
                screen = calculator.getScreen();
                flag[0] = false;
            }
        });

        while (flag[0]) {
            Thread.sleep(3);
        }
        Thread.sleep(10);
    }

    public String fire(String... args) throws InterruptedException {
        fireButton(Action.CLEAR);
        for (String n : args) {
            for (Map.Entry<Action, Button> entry : calculator.getButtonAndActionMapping().entrySet()) {
                Button value = entry.getValue();
                Action key = entry.getKey();
                if (value.getText().equals(n)) {
                    fireButton(key);
                }
            }

        }
        return screen.getText();
    }


    public String fireChar(String t) throws InterruptedException {

        String n;
        String[] args = new String[t.length()];
        char[] a = t.toCharArray();
        for (int i = 0; i < t.length(); i++) {
            n = Character.toString(a[i]);
            switch (n) {
                case "�":
                    n = negate;
                    break;
                case "?":
                    n = backSpace;
                    break;
                case "S":
                    n = sqrt;
                    i = i + 3;
                    break;
                case "x":
                    n = "1/x";
                    break;
                case "M":
                    n = n + Character.toString(a[i + 1]);
                    i++;
                    break;
                case "C":
                    if (Character.toString(a[i + 1]).equals("E")) {
                        n = n + Character.toString(a[i + 1]);
                        i++;
                        break;
                    }
            }
            args[i] = n;

        }
        return fire(args);
    }


    @Test
    public void testBackspace() throws InterruptedException {
        assertEquals("Impossible", fireChar("?x"));
        assertEquals("5", fireChar("5*5/5=???"));

        assertEquals("0", fireChar("1?"));
        assertEquals("-5.1", fireChar("5�.123??"));
        assertEquals("-5", fireChar("5�.?"));
        assertEquals("5", fireChar("5.?"));

        assertEquals("10", fireChar("5+55?="));
        assertEquals("25", fireChar("5*55.55????="));
        assertEquals("0", fireChar("0.0??="));
        assertEquals("0.5", fireChar("0.0?5="));

        assertEquals("26.4", fireChar("12*2.222??="));
        assertEquals("0", fireChar("9999999999999999999999999999999999999?????????????????????????????????????????????????????????????"));
        assertEquals("0", fireChar("999999999999999999�999999999999999999?????????????????????????????????????????????????????????????"));
        assertEquals("Overflow", fireChar("9999999999999999999+=*=*=*=*=*=*=*=*=*=*100000000000000========*10000=-==-==-==-==-==-===-==-===-====-==-==-==-==-==-==-==-==-==-==-===========????????????"));
    }

    @Test
    public void testSqrt() throws InterruptedException {

        assertEquals("5", fireChar("5 Sqrt Sqrt *=*="));
        assertEquals("7", fireChar("7 Sqrt Sqrt Sqrt*=*=*="));
        assertEquals("4", fireChar("1+9 Sqrt ="));

        assertEquals("Invalid input", fireChar("-6= Sqrt "));

        assertEquals("2.3", fireChar("5.29 Sqrt "));
        assertEquals("5", fireChar("5 Sqrt Sqrt *=*="));
        assertEquals("7", fireChar("5+2*=Sqrt"));

        assertEquals("0.541229427257326", fireChar("0.2929292929292929292929 Sqrt "));
        assertEquals("0.29292929292929", fireChar("0.2929292929292929292929 Sqrt *="));
        assertEquals("1.29292929292929", fireChar("0.2929292929292929292929 Sqrt *=+1="));

        assertEquals("Overflow", fireChar("9999999999999999999+=*=*=*=*=*=*=*=*=*=*100000000000000========*10000=-==-==-==-==-==-===-==-===-====-==-==-==-==-==-==-==-==-==-==-===========SqrtSqrtSqrt"));
    }


    @Test
    public void testPercent() throws InterruptedException {
        assertEquals("-180", fireChar("200�-10%"));
        assertEquals("34", fireChar("100-66%="));

        assertEquals("-40.2", fireChar("1�00.5-60%="));

        assertEquals("114.86825", fireChar("122.5-6.23%="));
        assertEquals("3.24", fireChar("20-2=%"));
        assertEquals("0.104976", fireChar("20-2=%%"));

        assertEquals("0", fireChar("80%"));
        assertEquals("0.005", fireChar("1*0.5%"));

        assertEquals("200", fireChar("1/0.5%"));
        assertEquals("1", fireChar("100000000000000000/100%"));
        assertEquals("100", fireChar("10*100%"));

    }

    @Test
    public void testOneDivide() throws InterruptedException {

        assertEquals("Impossible", fireChar("?x"));

        assertEquals("0.01", fireChar("C 100 x"));
        assertEquals("100", fireChar("C 100 x x"));
        assertEquals("3", fireChar("C3 x x"));

        assertEquals("1000", fireChar("0.001 x"));
        assertEquals("-1000", fireChar("0.001 � x"));
        assertEquals("-1000", fireChar("0.001 � x"));
        assertEquals("0.333", fireChar("0.333 x x"));

        assertEquals("Impossible", fireChar(".2/.2-1=x"));

    }

    @Test
    public void testMemory() throws InterruptedException {
        assertEquals("25", fireChar("CMC5M+*MR="));
        assertEquals("-200", fireChar("CMC100M--MRM+MR"));

        assertEquals("0.1", fireChar("CMC1.5/5-M+0.5+MR="));
        assertEquals("0", fireChar("MC MR M-MR"));

        assertEquals("54", fireChar("5MS9-MR="));
        assertEquals("25", fireChar("5MS*9MR="));
        assertEquals("17", fireChar("MC 5 + 6 M+ =+MR="));

        assertEquals("-8.999999999989E+156", fireChar("9999999999999*=*=*=M-M-M-M-M-M-M-M-M-M-M+MR="));
        assertEquals("109.99999999999E+137", fireChar("99999999999999999*=*=*=M+M+M+M+M+M+M+M+M+M+M+MR"));
        assertEquals("Overflow", fireChar("9999999999999999999+=*=*=*=*=*=*=*=*=*=*100000000000000=======*10M+M+M+M+M+M+M+M+M+M+M+M+M+M+M+M+M+M+M+M+M+M+MR="));

    }

    @Test
    public void testCE() throws InterruptedException {
        assertEquals("11", fireChar("5+5CE6="));
        assertEquals("2", fireChar("C100CE2"));
        assertEquals("2", fireChar("C100�CE2"));
        assertEquals("2", fireChar("C100.11CE2"));
        assertEquals("2", fireChar("C100.11?CE2"));
        assertEquals("200.22", fireChar("C100.11*5CE2="));

    }

    @Test
    public void testDivide() throws InterruptedException {
        assertEquals("Impossible", fireChar("1/0="));
        assertEquals("0", fireChar("/5="));
        assertEquals("1", fireChar("5*5/="));

        assertEquals("-20", fireChar("05/0.25�="));
        assertEquals("1", fireChar("05/0.25=/="));

        assertEquals("0.0000000000001", fireChar("+0.0000000000001/1="));
        assertEquals("-0.0000000000001", fireChar("-0.0000000000001/1="));

        assertEquals("99999999990", fireChar("9999999999/0.1="));
        assertEquals("99.999999999999E+20", fireChar("99999999999999999999/0.1="));

        assertEquals("218472537974794E+19", fireChar("051234567895123456789/0.2345126228223451262282="));
        assertEquals("1", fireChar("051234567895123456789/0.2345126228223451262282=/="));
        assertEquals("-52643984799858E+18", fireChar("1234567895123456789/0.2345126228223451262282�="));
        assertEquals("4", fireChar("05/0.25MS/MR="));

        assertEquals("1", fireChar("99999999999999999999/="));
        assertEquals("100.00000000000E-760", fireChar("99999999999999999999/========================================="));
        assertEquals("Overflow", fireChar("99999999999999999999/=========================================*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*="));
    }

    @Test
    public void testEquals() throws InterruptedException {
        assertEquals("125", fireChar("5*5=="));
        assertEquals("100", fireChar("5*5=*2+="));
        assertEquals("0", fireChar("=="));
        assertEquals("1", fireChar("1*=="));
    }

    @Test
    public void testNegate() throws InterruptedException {
        assertEquals("-1", fireChar("1 �"));

        assertEquals("-0.12", fireChar("0.12�"));
        assertEquals("-20", fireChar("25-5=�"));
        assertEquals("20", fireChar("25-5=��"));
        assertEquals("75", fireChar("�25-50�="));
        assertEquals("-50", fireChar("25-50�"));
        assertEquals("-25", fireChar("�25-50="));

        assertEquals("-0.1566", fireChar("0.15�66"));

        assertEquals("49.5", fireChar("�0.5-50=�"));
        assertEquals("-0.9999999999999", fireChar("�0.9999999999999�"));

        assertEquals("-1", fireChar("1/3�*3="));
        assertEquals("-1", fireChar("1/0.3�*0.3="));
        assertEquals("1", fireChar("1/0.3�*0.3�="));

        assertEquals("-283.4504342907E-7", fireChar("�0.888888888888888888888888�*=*=*=*=*=*=*=�"));
        assertEquals("283.45043429074E-7", fireChar("�0.888888888888888888888888�*=*=*=*=*=*=*=��"));


    }

    @Test
    public void testPlus() throws InterruptedException {

        assertEquals("10", fireChar("5+5="));
        assertEquals("5", fireChar("+5="));
        assertEquals("-10", fireChar("-5+-5="));
        assertEquals("-10", fireChar("-5=+-5="));
        assertEquals("10", fireChar("+5++++++="));

        assertEquals("4.75", fireChar("05+0.25�="));

        assertEquals("1", fireChar("1+0.000000000000000001="));
        assertEquals("1", fireChar("1-0.000000000000000001="));

        assertEquals("1.0000000000001", fireChar("+0.0000000000001+1="));
        assertEquals("0.9999999999999", fireChar("-0.0000000000001+1="));

        assertEquals("9999999999.1", fireChar("9999999999+0.1="));
        assertEquals("9999999999999999", fireChar("9999999999999999+0.1="));
        assertEquals("10E+18", fireChar("999999999999999999999999999999+0.1======"));
        assertEquals("9999999999999999", fireChar("9999999999999999+0.4="));
        assertEquals("10E+18", fireChar("999999999999999999999+0.5="));

        assertEquals("-11.11", fireChar("-5.555=+5.555�="));
        assertEquals("60.095", fireChar("65.655?+5.555�="));
        assertEquals("-1.499", fireChar("00.000?1+1..5�="));
        assertEquals("-0.5", fireChar("0.?00?1+1..5�="));
        assertEquals("20E+18", fireChar("999999999999999999999+100%"));


        assertEquals("5", fireChar("MC1M+M+CMR+3="));
        assertEquals("1", fireChar("2M-M-CMR+3="));

        assertEquals("0.7468579134632346", fireChar(".51234567895123456789+0.234512234512="));
        assertEquals("-1.775420324469136", fireChar("-1.34567895123456789+0.223456789+0.234512=+="));
        assertEquals("0.2245796755308642", fireChar("1-1.34567895123456789+0.223456789+0.234512=+="));
        assertEquals("1.28E+21", fireChar("9999999999999999999 += += += += += += +="));
        assertEquals("51234567895123.23", fireChar("51234567895123+0.23451221111111="));
        assertEquals("200E+2430", fireChar("9999999999999999999 *= *= *= *= *= *= *= +="));
        assertEquals("-10.24E+21", fireChar("9999999999999999999 � + = + = + = += += += += += += +="));
        assertEquals("Overflow", fireChar("9999999999999999999+=*=*=*=*=*=*=*=*=*=*100000000000000=======+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+="));
        assertEquals("Overflow", fireChar("9999999999999999999+=*=*=*=*=*=*=*=*=*=*100000000000000 � =======+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+="));
    }

    @Test
    public void testMinus() throws InterruptedException {
        assertEquals("0", fireChar("5-5="));
        assertEquals("-5", fireChar("-5="));
        assertEquals("5.1234567895123E+18", fireChar("51234567895123456789-0.234512234512="));

        assertEquals("5.25", fireChar("05-0.25�="));
        assertEquals("1", fireChar("1-0.000000000000000001="));

        assertEquals("-11.11", fireChar("-5.555=+5.555�="));
        assertEquals("71.205", fireChar("65.655?-5.555�="));
        assertEquals("1.501", fireChar("00.000?1-1..5�="));
        assertEquals("2.5", fireChar("0.?00?1-1..5�="));
        assertEquals("0", fireChar("9999999999999999-100%"));

        assertEquals("-3", fireChar("1M-M+CMR-3="));
        assertEquals("-7", fireChar("2M-M-CMR-3="));

        assertEquals("0.2778334444392346", fireChar(".51234567895123456789-0.234512234512="));
        assertEquals("-2.669247480469136", fireChar("-1.34567895123456789-0.223456789+0.234512=+="));
        assertEquals("-0.100111740234E-1", fireChar("1-1.34567895123456789-0.223456789+0.234512=="));
        assertEquals("-80E+18", fireChar("9999999999999999999 -========="));
        assertEquals("51234567895122.77", fireChar("51234567895123-0.23451221111111="));
        assertEquals("0", fireChar("9999999999999999999 *= *= *= *= *= *= *= -="));

        assertEquals("Overflow", fireChar("9999999999999999999+=*=*=*=*=*=*=*=*=*=*100000000000000========*10000=-==-==-==-==-==-===-==-===-====-==-==-==-==-==-==-==-==-==-==-==========="));
    }

    @Test
    public void testPoint() throws InterruptedException {
        assertEquals("0.0001", fireChar(".0001"));
        assertEquals("-0.0001", fireChar("C-.0001="));

        assertEquals("-0.555555555555", fireChar("-.555555555555*1="));
        assertEquals("-0.44444444444444", fireChar("-.4444444444444444444444444444*1="));

        assertEquals("-5.01111111111111", fireChar("5�.01111111111111"));
        assertEquals("5.4", fireChar("5�.5�-0.1="));
        assertEquals("5555555555555555555.", fireChar("5555555555555555555555555555.55555555"));
        assertEquals("55.", fireChar("55......"));

        assertEquals("55", fireChar("55.?"));
        assertEquals("55", fireChar("55.55???"));
        assertEquals("55.", fireChar("55.55??"));
    }


    private String fireRobot(int... args) throws InterruptedException {
        final boolean[] flag = {true};
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                calculator.getButtonAndActionMapping().get(Action.CLEAR).fire();
                for (int n : args) {
                    robotBOT(n);
                }
                flag[0] = false;
            }
        });

        while (flag[0]) {
            Thread.sleep(100);
        }
        Thread.sleep(1000);
        return screen.getText();
    }


    public void robotBOT(int... args) {

        for (int n : args) {
            robot.keyPress(n);
        }

    }

    @Test
    public void testKey() throws InterruptedException {

        assertEquals("23", fireRobot('2', '3'));
        assertEquals("1234567890", fireRobot('1', '2', '3', '4', '5', '6', '7', '8', '9', '0'));
        assertEquals("1234567890", fireRobot('1', '2', '3', '4', '5', '6', '7', '8', '9', '0'));
        assertEquals("123456789", fireRobot('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', java.awt.event.KeyEvent.VK_BACK_SPACE));
        assertEquals("0", fireRobot('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', java.awt.event.KeyEvent.VK_DELETE));
    }

    private String fireRobotWithCopyPaste(int... args) throws InterruptedException {
        final boolean[] flag = {true};
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                calculator.getButtonAndActionMapping().get(Action.CLEAR).fire();
                robotInput102();
                robotCopy();
                robotInput1();
                robotPaste();
                flag[0] = false;
            }
        });
        while (flag[0]) {
            Thread.sleep(100);
        }
        Thread.sleep(1000);
        return screen.getText();
    }


    public void robotInput102() {
        robot.keyPress('1');
        robot.keyRelease('1');
        robot.keyPress('0');
        robot.keyRelease('0');
        robot.keyPress('2');
        robot.keyRelease('2');
    }
    public void robotInput1() {
        robot.keyPress('1');
        robot.keyRelease('1');
    }
    public void robotPaste() {
        robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
        robot.keyPress(java.awt.event.KeyEvent.VK_V);
        robot.keyRelease(java.awt.event.KeyEvent.VK_V);
        robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
    }
    public void robotCopy() {
        robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
        robot.keyPress(java.awt.event.KeyEvent.VK_C);
        robot.keyRelease(java.awt.event.KeyEvent.VK_C);
        robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
    }

    @Test
    public void testCopyPaste() throws InterruptedException {
        Thread.sleep(1000);
        assertEquals("102", fireRobotWithCopyPaste());
    }




    @Test
    public void testOverflow() throws InterruptedException {
        assertEquals("Overflow", fire("9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "*", "9", "9", "9", "9", "9", "9", "9", "9",
                "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "="));
        assertEquals("Overflow", fire("1", "/", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "/", "9", "9", "9", "9", "9", "9", "9", "9",
                "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=", "*", "=",
                "*", "=", "*", "=", "*", "=", "*", "=", "*", "="));


        assertEquals("Overflow", (fire("0", ".", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1",
                "/", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "=",
                "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=",
                "*", "=", "*", "=", "*", "=", "*", "="

        )));
        assertEquals("1", (fire("0", ".", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1",
                "/", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "=", "/", "="
        )));
    }


}




