package asd;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import static java.lang.Math.sqrt;

/**
 * This class representation logic actions of Calculator
 */
public class CalculatorLogic {

    /**
     * Length to mantissa
     */
    private static final int lengthToMantissa = 15;

    /**
     * Mantissa length in output format
     */
    private static final int mantissaLength = 4;


    /**
     * Formatter vor value
     */
    private static DecimalFormatSymbols symbols = new DecimalFormatSymbols();

    /**
     * Used pattern
     */
    private static final String pattern = "#,##0.0#";

    /**
     * Set separators for formatter
     */
    static {
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
    }

    /**
     * Used decimal format
     */
    private static final DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);

    /**
     * Maximum number of digits
     */
    private static final int lengthToStopInputFormat = 18;


    /**
     * Number digits after point to change formatter
     */
    private static final int numberOfDigitsForFormat = 10;

    /**
     * MathContexts used in calculation for multiplication
     */
    private static final MathContext forMultiply = new MathContext(14, RoundingMode.HALF_EVEN);

    /**
     * MathContexts used in calculation for square root
     */
    private static final MathContext forSquareRoot = new MathContext(15, RoundingMode.HALF_EVEN);


    /**
     * MathContexts used in calculation for one divide
     */
    private static final MathContext forOneDivide = new MathContext(30, RoundingMode.HALF_EVEN);

    /**
     * Big Decimal one hundred
     */
    private static final BigDecimal oneHundred = new BigDecimal(100);

    /**
     * One rank(to change scale per unit)
     */
    private static final int oneRank = 10;

    /**
     * String constant
     */
    private static final String zeroPointZero = "0.0";

    /**
     * String constant
     */
    private static final String zero = "0";

    /**
     * String constant
     */
    private static final String impossible = "Impossible";

    /**
     * String pattern using in number format
     */
    private static final String decimalTextFormat = "0.0E0";

    /**
     * Basic value which transmitted to output
     */
    private BigDecimal value = BigDecimal.ZERO;

    /**
     * String representation of value.
     */
    private String converter = zero;

    /**
     * Stores value of all operation till this
     */
    private BigDecimal stack = BigDecimal.ZERO;

    /**
     * Stores memory
     */
    private BigDecimal memory = BigDecimal.ZERO;

    /**
     * Stores action
     */
    private Action switcher;

    /**
     * Count scale after point.
     */
    private int point;

    /**
     * Indicate negate method.
     * Positive when false
     * Negative when true
     */
    private boolean negate;

    /**
     * Indicate the impossibility of action
     */
    private boolean stop;

    /**
     * Indicate possibility of backspace
     */
    private boolean backSpace;

    /**
     * Indicate simple or rational number
     */
    private boolean rationalNumber;

    /**
     * Indicate math context of one divide value action
     * true -change Math Context to 64
     * false -change MathContext forOneDivide
     */
    private boolean oneDivide;

    /**
     * Additional switch to save past action
     */
    private Action additionalSwitch;

    /**
     * Using when equal repeated
     */
    private BigDecimal history;

    /**
     * It is used when you want to perform mathematical operations of addition, multiplication, division, subtraction, equality.
     */
    public void switchOperation() {

        backSpace = false;
        rationalNumber = false;
        point = 0;
        negate = false;

        if (switcher == null) {
            stack = value;
            return;
        }

        if (switcher == Action.DIVIDE && value.compareTo(BigDecimal.ZERO) == 0) {
            stack = BigDecimal.ZERO;
            converter = impossible;
            return;
        }

        switch (switcher) {
            case MULTIPLY:
                stack = value.multiply(stack, forMultiply);
                break;
            case PLUS:
                stack = value.add(stack, MathContext.DECIMAL64);
                break;
            case MINUS:
                stack = stack.subtract(value, MathContext.DECIMAL64);
                break;
            case DIVIDE:
                stack = stack.divide(value, MathContext.DECIMAL128);
                break;
        }
        value = stack;
        converter = changeFormat(value);
    }

    /**
     * Returns a BigDecimal which is numerically equal to this one but with any trailing zeros removed from the representation.
     *
     * @param n - BigDecimal
     * @return - BigDecimal without trailing zeros
     */
    public BigDecimal doWithoutZeros(BigDecimal n) {
        return n.stripTrailingZeros();
    }


    /**
     * Changes sign of value
     */
    public void negate() {
        if (!stop) {
            int compare = value.compareTo(BigDecimal.ZERO);
            if (compare > 0) {
                value = value.negate(MathContext.DECIMAL128);

            } else {
                value = value.abs(MathContext.DECIMAL128);
            }
            negate = compare != 0 && !negate;
            converter = changeFormat(value);
        }

    }

    /**
     * Divides one on BigDecimal
     *
     * @param l - Math Context
     */
    void oneDivideValue(MathContext l) {
        value = BigDecimal.ONE.divide(value, l);
    }

    /**
     * Divides one on value.
     */
    public void oneDivideValue() {

        if (value.compareTo(BigDecimal.ZERO) != 0) {
            if (oneDivide) {
                oneDivideValue(MathContext.DECIMAL64);
            } else {
                oneDivideValue(forOneDivide);
            }
            oneDivide = !oneDivide;
            stack = value;

            converter = changeFormat(value);
            rationalNumber = false;
        } else {
            converter = impossible;
            stop = true;
        }
    }

    /**
     * Clear screen and set all variables to the initial values
     */
    public void clear() {
        stack = BigDecimal.ZERO;
        value = BigDecimal.ZERO;
        converter = changeFormat(value);
        stop = false;
        switcher = null;
        rationalNumber = false;
        point = 0;
        negate = false;
        oneDivide = false;
    }

    /**
     * Get square root from this value
     */
    public void squareRoot() {
        if (!stop) {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                value = new BigDecimal(sqrt(value.doubleValue()), forSquareRoot);
                converter = changeFormat(value);
                if (switcher == null || switcher == Action.EQUAL) {
                    stack = value;
                }
            } else {
                converter = "Invalid input";
            }
            rationalNumber = false;
            backSpace = false;
        }
    }

    /**
     * Method which do math actions with percents of stack
     * This value is number of percents of stack
     */
    public void percent() {

        if (!stop) {
            if (switcher == Action.PLUS || switcher == Action.MINUS || switcher == Action.DIVIDE || switcher == Action.MULTIPLY) {
                BigDecimal k = stack.divide(oneHundred, MathContext.DECIMAL64);
                value = value.multiply(k);
                switchOperation();
                switcher = null;
                return;
            }
            if (switcher == Action.EQUAL) {
                value = value.divide(oneHundred, MathContext.DECIMAL128).multiply(value, MathContext.DECIMAL64);
            } else {
                value = BigDecimal.ZERO;
            }
            converter = changeFormat(value);
        }
    }

    /**
     * This method delete last digit of this value.
     */
    public void backspace() {
        if (!stop) {
            int converterLength = converter.length();
            String substringConverter = converter.substring(0, converterLength - 1);
            if (backSpace) {
                if (converterLength == 1 || substringConverter.equals("-")) {
                    value = BigDecimal.ZERO;
                    converter = changeFormat(value);
                } else {
                    if (value.compareTo(BigDecimal.ZERO) == 0) {
                        converter = substringConverter;
                        point = point / 10;

                    } else {
                        converter = value.toString().substring(0, converterLength - 1);
                        value = new BigDecimal(converter);
                    }
                    decimalFormatter();
                }
            }
        }
    }


    /**
     * Action for equal button
     */
    public void equalsAction() {
        if (!stop) {
            int valueCompareToZero = value.compareTo(BigDecimal.ZERO);
            /**When using after some operation*/
            if (switcher != Action.EQUAL) {
                history = value;
                if (valueCompareToZero > 0) {
                    doWithoutZeros(value);
                }
                switchOperation();
                additionalSwitch = switcher;
                /**Start when recall equals*/
            } else {
                value = history;
                switcher = additionalSwitch;
                switchOperation();
            }
            switcher = Action.EQUAL;

        }
    }

    /**
     * Action for deduct operation
     */
    public void deductAction() {
        if (!stop) {
            if (rationalNumber) {
                switchOperation();
            }
            switcher = Action.MINUS;
        }
    }

    /**
     * Multiply operation
     */
    public void multiplyAction() {
        if (!stop) {
            if (rationalNumber) {
                switchOperation();
            }
            switcher = Action.MULTIPLY;
        }
    }

    /**
     * Divide operation
     */
    public void divideAction() {
        if (!stop) {
            if (rationalNumber) {
                switchOperation();
            }
            switcher = Action.DIVIDE;
        }
    }

    /**
     * Negate operation
     */
    public void negateAction() {
        if (!stop) {
            negate();
        }
    }

    /**
     * Action for  point call
     */
    public void pointAction() {
        if (!stop) {
            if (point == 0) {
                point = 1;
                converter = converter + ".";
            }
        }
    }


    /**
     * Add value to stack Value
     */
    public void addAction() {
        if (!stop) {
            if (rationalNumber) {
                switchOperation();
            }
            switcher = Action.PLUS;
        }
    }

    /**
     * Add value to memory
     */
    public void memoryAddAction() {
        if (!stop) {
            if (memory == null) {
                memory = BigDecimal.ZERO;
            }
            memory = memory.add(value, MathContext.DECIMAL128);
            converter = changeFormat(value);
            if (stack.compareTo(BigDecimal.ZERO) == 0) {
                stack = stack.add(value);
            }
            rationalNumber = false;
            if (memory.compareTo(BigDecimal.ZERO) == 0) {
                memory = null;
            }
        }
    }

    /**
     * Screen clear action
     */
    public void actionCE() {
        value = BigDecimal.ZERO;
        converter = changeFormat(value);
        stop = false;
        rationalNumber = false;
        point = 0;
        negate = false;
    }

    /**
     * Subtract value from memory
     */
    public void memorySubtractAction() {
        if (!stop) {
            if (memory == null) {
                memory = BigDecimal.ZERO;
            }
            memory = memory.subtract(value, MathContext.DECIMAL128);//
            if (stack.compareTo(BigDecimal.ZERO) == 0) {
                stack = stack.add(value);
            }
            converter = changeFormat(value);
            rationalNumber = false;
            if (memory.compareTo(BigDecimal.ZERO) == 0) {
                memory = null;
            }
        }
    }

    /**
     * Memory call action
     * /
     */
    public void actionMC() {
        if (!stop) {
            memory = null;
        }
    }

    /**
     * Memory recall action
     */
    public void actionMR() {
        if (!stop) {
            if (memory != null) {
                converter = changeFormat(memory);
                value = memory;
                if (switcher == null || switcher == Action.EQUAL) {
                    stack = value;
                }
                rationalNumber = false;
            }
        }
    }

    /**
     * Memory store action
     */
    public void actionMS() {
        if (!stop) {
            memory = value;
            stack = value;
            converter = changeFormat(memory);
            if (memory.compareTo(BigDecimal.ZERO) == 0) {
                memory = null;
            }
        }
    }

    /**
     * Set memory value to plain mode
     *
     * @return memory value in Plain string mode
     */
    public String toPlainMode(BigDecimal n) {
        return doWithoutZeros(n).toPlainString();
    }

    /**
     * Set value to engineering mode
     *
     * @return value in Engineering string mode
     */
    public String toEngineMode(BigDecimal n) {
        return doWithoutZeros(n).toEngineeringString();
    }

    /**
     * Change output format of memory value
     *
     * @return formatted memory value
     */
    public String changeFormat(BigDecimal n) {

        String formattedValue = format(n);
        String returnedFormatted = toPlainMode(n);
        String mantissaText = "E";

        int mantissaIndex = formattedValue.indexOf(mantissaText);
        int fullLength = formattedValue.length();
        int numberOfDigitsAfterMantissa;

        if (returnedFormatted.length() > lengthToStopInputFormat) {
            String engine = toEngineMode(n);

            String mantissaPositiveText = "E+";
            String mantissa = formattedValue.substring(mantissaIndex + 1, fullLength);
            if (engine.length() > lengthToMantissa) {
                returnedFormatted = engine.substring(0, lengthToMantissa);
                if (formattedValue.indexOf("-", mantissaIndex) >= 0) {
                    returnedFormatted += formattedValue.substring(mantissaIndex, fullLength);
                } else {
                    returnedFormatted += mantissaPositiveText + mantissa;
                }
            } else {
                returnedFormatted = engine;
            }
        }
        numberOfDigitsAfterMantissa = fullLength - mantissaIndex - 1;
        /**Check for overflow*/
        if (formattedValue.substring(mantissaIndex, fullLength).contains("-")) {
            numberOfDigitsAfterMantissa--;
        }

        if (numberOfDigitsAfterMantissa > mantissaLength) {
            stop = true;
            returnedFormatted = "Overflow";
        }
        return returnedFormatted;
    }


    /**
     * Format value from string
     */
    public void decimalFormatter() {
        if (converter.length() > numberOfDigitsForFormat) {
            decimalFormat.setParseBigDecimal(true);
            try {
                value = (BigDecimal) decimalFormat.parse(converter);
            } catch (ParseException e) {
                /**
                 * If not digit in converter.Ca
                 */
                value = new BigDecimal(converter);
            }
        }
    }


    /**
     * Format value
     *
     * @param x - value
     * @return - formatted value
     */
    public String format(BigDecimal x) {
        NumberFormat formatter = new DecimalFormat(decimalTextFormat);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits((x.scale() > 0) ? x.precision() : x.scale());
        return formatter.format(x);
    }

    /**
     * Action for digits.
     *
     * @param n - number of buttons used
     */
    public void positiveFraction(int n) {
        if (!rationalNumber) {
            value = new BigDecimal(n);
            value = value.divide(BigDecimal.TEN, MathContext.DECIMAL128);
            rationalNumber = true;
            if (n == 0) {
                converter = zeroPointZero;
            } else {
                converter = value.toString();
            }
            point = oneRank;
        } else {
            if (converter.length() > lengthToStopInputFormat) {
                return;
            }
            /**If using point.Value length more then 10*/
            if (converter.length() > numberOfDigitsForFormat) {
                converter = converter + n;
                decimalFormatter();
            } else {
                point = point * oneRank;
                value = value.add(new BigDecimal(n).divide(new BigDecimal(point), MathContext.DECIMAL128));
                if (n == 0) {
                    converter = converter + zero;
                } else {
                    converter = changeFormat(value);
                }
            }
        }
        backSpace = true;
    }

    /**
     * Set action when introduced negative fraction
     *
     * @param n - number of buttons used
     */
    public void negativeFraction(int n) {

        if (converter.length() > lengthToStopInputFormat) {
            return;
        }
        if (value.toString().length() > numberOfDigitsForFormat) {
            converter = converter + n;
            decimalFormatter();

        } else {
            point = point * oneRank;
            value = value.subtract(new BigDecimal(n).divide(new BigDecimal(point), MathContext.DECIMAL128));
            converter = changeFormat(value);
        }
        backSpace = true;
    }


    /**
     * Set action when introduced integer button
     *
     * @param n - number of buttons used
     */
    public void valueInteger(int n) {
        BigDecimal newBigDecimal = new BigDecimal(n);
        if (!rationalNumber && !negate) {//If simple positive
            value = newBigDecimal;
            converter = value.toString();
            rationalNumber = true;

        } else {
            if (converter.length() > lengthToStopInputFormat) {
                return;
            }
            if (value.toString().length() > numberOfDigitsForFormat) {
                converter = converter + n;
                decimalFormatter();
            } else {
                BigDecimal alreadyMovedPoint = value.movePointRight(1);
                if (negate) {
                    value = alreadyMovedPoint.subtract(newBigDecimal, MathContext.DECIMAL128);
                } else {
                    value = alreadyMovedPoint.add(newBigDecimal, MathContext.DECIMAL128);
                }
                converter = changeFormat(value);
            }
        }
        backSpace = true;
    }


    /**
     * All digit actions
     *
     * @param j - number of buttons used
     */
    public void digitActions(int j) {
        /**If nothing blocked*/
        if (!stop) {
            oneDivide = false;
            /**If not using point*/
            if (point != 0) {
                if (!negate) {
                    positiveFraction(j);
                } else {
                    negativeFraction(j);
                }
            } else {
                oneDivide = false;
                /**Addition of integer numbers*/
                valueInteger(j);
            }

        }
    }

    /**
     * Getter for string value which transmit to Interface
     *
     * @return string value
     */
    public String getConverter() {
        return converter;
    }

    /**
     * Used for comparing memory value with zero(to delete memory screen when not need)
     *
     * @return comparing
     */
    public boolean compareMemoryToNull() {
        return memory == null;
    }

    /**
     * Set stack value for first calculator action
     */
    public void setStackIfFirstAction() {
        if (switcher == null || (switcher == Action.EQUAL)) {
            stack = value;
        }
    }

    /**
     * Setter for value
     *
     * @param n - value
     */
    public void setValue(BigDecimal n) {
        this.value = n;
    }

}

