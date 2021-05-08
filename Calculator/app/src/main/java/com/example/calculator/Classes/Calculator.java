package com.example.calculator.Classes;

import android.annotation.SuppressLint;

import com.example.calculator.R;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Stack;

public class Calculator {
    private BigDecimal result = null;
    private BigDecimal memory = null;
    StringBuilder inputStr = new StringBuilder();

    ArrayList<String> OPZ = new ArrayList<>();
    Stack<String> operations = new Stack<>();
    private int maxCount = 15;

    private State state;

    private enum State {
        InputStr,
        ClearStr
    }

    private int countNum = 0;
    private int countBracket = 0;
    private boolean dot = true;
    private boolean operation = false;
    private boolean bracket = false;
    private boolean num = true;

    public Calculator() {
        state = State.InputStr;
        inputStr.setLength(0);
    }

    @SuppressLint("NonConstantResourceId")
    public void onNumPressed(int buttonId) {
        if (state == State.ClearStr) { //если мы показывали результат операции
            result = null;
            state = State.InputStr; // то переходим к вводу аргумента
            inputStr.setLength(0);
        }

        if(countNum < maxCount && num) {
            switch (buttonId) {
                case R.id.buttonZero:
                    if (inputStr.length() != 0) {
                        inputStr.append("0");
                        countNum++;
                    }
                    break;
                case R.id.buttonOne:
                    inputStr.append("1");
                    break;
                case R.id.buttonTwo:
                    inputStr.append("2");
                    break;
                case R.id.buttonThree:
                    inputStr.append("3");
                    break;
                case R.id.buttonFour:
                    inputStr.append("4");
                    break;
                case R.id.buttonFive:
                    inputStr.append("5");
                    break;
                case R.id.buttonSix:
                    inputStr.append("6");
                    break;
                case R.id.buttonSeven:
                    inputStr.append("7");
                    break;
                case R.id.buttonEight:
                    inputStr.append("8");
                    break;
                case R.id.buttonNine:
                    inputStr.append("9");
                    break;
            }
            countNum++;
        }
        if (buttonId == R.id.buttonDot) {
            if (dot) {
                if (inputStr.length() == 0 || inputStr.charAt(inputStr.length() - 1) == ' ') {
                    inputStr.append("0");
                    countNum++;
                }
                inputStr.append(".");
                dot = false;
                maxCount = 19;
            }
        }
        operation = true;
        bracket = true;
    }

    private void check() {
        if (inputStr.length() != 0) {
            if (inputStr.charAt(inputStr.length() - 1) == '.')
                inputStr.append("0 ");
            if (inputStr.charAt(inputStr.length() - 1) != ' ')
                inputStr.append(" ");
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onActionPressed(int buttonId) {
        if (state == State.ClearStr) { //если мы показывали результат операции
            result = null;
            state = State.InputStr; // то переходим к вводу аргумента
            inputStr.setLength(0);
        }

        num = true;
        dot = true;
        countNum = 0;
        maxCount = 15;

        switch (buttonId) {
            case R.id.buttonPlus:
                if (!operation && inputStr.length() > 1) {
                    if (inputStr.charAt(inputStr.length() - 2) == '(')
                        inputStr.append("0 ");
                    if (inputStr.charAt(inputStr.length() - 2) == '*' || inputStr.charAt(inputStr.length() - 2) == '/' || inputStr.charAt(inputStr.length() - 2) == '-') {
                        inputStr.setCharAt(inputStr.length() - 2, '+');
                        break;
                    }
                }
                if (operation || inputStr.length() == 0) {
                    if (inputStr.length() == 0)
                        inputStr.append("0 ");
                    check();
                    inputStr.append("+ ");
                    bracket = false;
                    operation = false;
                }
                break;
            case R.id.buttonMinus:
                if (!operation && inputStr.length() > 1) {
                    if (inputStr.charAt(inputStr.length() - 2) == '*' || inputStr.charAt(inputStr.length() - 2) == '/')
                        inputStr.append("( ");
                    if(inputStr.charAt(inputStr.length() - 2) == '+')
                        inputStr.setCharAt(inputStr.length() - 2, '-');
                    if (inputStr.charAt(inputStr.length() - 2) == '(')
                        inputStr.append("0 ");
                    if (inputStr.charAt(inputStr.length() - 2) == '-')
                        break;
                }

                    if (inputStr.length() == 0)
                        inputStr.append("0 ");
                    check();
                    inputStr.append("- ");
                    bracket = false;
                    operation = false;

                break;
            case R.id.buttonMultiplication:
                if (!operation && inputStr.length() > 1) {
                    if (inputStr.charAt(inputStr.length() - 2) == '(') {
                        inputStr.append("1 ");
                        inputStr.append("* ");
                    }
                    if (inputStr.charAt(inputStr.length() - 2) == '+' || inputStr.charAt(inputStr.length() - 2) == '/' || inputStr.charAt(inputStr.length() - 2) == '-') {
                        inputStr.setCharAt(inputStr.length() - 2, '*');
                        break;
                    }
                }
                if (operation || inputStr.length() == 0) {
                    if (inputStr.length() == 0)
                        inputStr.append("1 ");
                    check();
                    inputStr.append("* ");
                }
                bracket = false;
                operation = false;
                break;
            case R.id.buttonDivision:
                if (!operation && inputStr.length() > 1) {
                    if (inputStr.charAt(inputStr.length() - 2) == '(') {
                        inputStr.append("1 ");
                        inputStr.append("/ ");
                    }
                    if (inputStr.charAt(inputStr.length() - 2) == '*' || inputStr.charAt(inputStr.length() - 2) == '+' || inputStr.charAt(inputStr.length() - 2) == '-') {
                        inputStr.setCharAt(inputStr.length() - 2, '/');
                        break;
                    }
                }

                if (operation || inputStr.length() == 0) {
                    if (inputStr.length() == 0)
                        inputStr.append("1 ");
                    check();
                    inputStr.append("/ ");
                    bracket = false;
                    operation = false;
                }
                break;
            case R.id.buttonResult:
                if(inputStr.length() == 0){
                    state = State.ClearStr;
                    inputStr.setLength(0);
                    countBracket = 0;
                    operation = false;
                    break;
                }
                if (inputStr.length() > 1) {
                    if (inputStr.charAt(inputStr.length() - 2) == '*' || inputStr.charAt(inputStr.length() - 2) == '/')
                        inputStr.append("1 ");
                    if (inputStr.charAt(inputStr.length() - 2) == '+' || inputStr.charAt(inputStr.length() - 2) == '-')
                        inputStr.append("0 ");
                    if(inputStr.charAt(inputStr.length() - 2) == '(') {
                        state = State.ClearStr;
                        inputStr.setLength(0);
                        inputStr.append("ERROR");
                        countBracket = 0;
                        operation = false;
                        break;
                    }
                }
                if (countBracket > 0) {
                    for (int i = 0; i < countBracket; i++)
                        inputStr.append(" )");
                    countBracket = 0;
                }
                if (countBracket == 0 && inputStr.length() != 0) {
                    String str = inputStr.toString();
                    String[] elements = str.split(" ");
                    TranslationIntoReversePolishNotation(elements);
                    try {
                        Calculate();
                    } catch (Exception e) {
                        inputStr.setLength(0);
                        inputStr.append(e.getMessage());
                        state = State.ClearStr;
                        break;
                    }
                    inputStr.setLength(0);
                    inputStr.append(result);
                    countNum = inputStr.length();
                    if(isInt(inputStr.toString())){
                        maxCount = 15;
                    }
                    else {
                        countNum--;
                        maxCount = 19;
                    }
                } else {
                    state = State.ClearStr;
                    inputStr.setLength(0);
                    inputStr.append("ERROR");
                    countBracket = 0;
                }
                bracket = false;
                operation = true;
                break;
            case R.id.buttonClear:
                state = State.InputStr;
                inputStr.setLength(0);
                countBracket = 0;
                bracket = false;
                operation = false;
                break;
            case R.id.buttonBracketL:
                if (!operation || inputStr.length() == 0) {
                    check();
                    inputStr.append("( ");
                    countBracket++;
                    System.out.println(countBracket);
                }
                break;
            case R.id.buttonBracketR:
                if (bracket && countBracket > 0) {
                    check();
                    inputStr.append(") ");
                    countBracket--;
                    operation = true;
                    num = false;
                }
                break;
            case R.id.buttonSin:
                if (!operation) {
                    check();
                    inputStr.append("sin ( ");
                    operation = true;
                    countBracket++;
                }
                break;
            case R.id.buttonCos:
                if (!operation) {
                    check();
                    inputStr.append("cos ( ");
                    operation = true;
                    countBracket++;
                }
                break;
        }
    }
    private static boolean isInt(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isDouble(String s) throws NumberFormatException {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @SuppressLint("NonConstantResourceId")
    public void onMemoryPressed(int buttonId) {


        switch (buttonId) {
            case R.id.buttonMS:
                if(isDouble(inputStr.toString())) {
                    result = BigDecimal.valueOf(Double.parseDouble(inputStr.toString()));
                    memory = result;
                }
                break;
            case R.id.buttonMR:
                inputStr.setLength(0);
                inputStr.append(memory);
                break;
            case R.id.buttonMC:
                memory = null;
                break;
            case R.id.buttonMPlus:
                if(isDouble(inputStr.toString())) {
                    result = BigDecimal.valueOf(Double.parseDouble(inputStr.toString()));
                    memory = memory.add(result);
                }
                break;
            case R.id.buttonMMinus:
                if(isDouble(inputStr.toString())) {
                    result = BigDecimal.valueOf(Double.parseDouble(inputStr.toString()));
                    memory = memory.add(result.negate());
                }
                break;
        }
        if (memory != null)
            operation = true;
        else
            state = State.ClearStr;
    }

    public String getText() {
        return inputStr.toString();
    }

    private void Calculate() throws Exception {
        result = null;
        Stack<BigDecimal> calculate = new Stack<>();
        String str;
        BigDecimal inf1, inf2;

        if (OPZ.size() == 1) {
            result = new BigDecimal(OPZ.get(0));
            OPZ.clear();
            return;
        }

        for (int i = 0; i < OPZ.size(); i++) {
            str = OPZ.get(i);
            if (str.charAt(0) >= '0' && str.charAt(0) <= '9') {
                calculate.add(new BigDecimal(str));
                continue;
            }            if(str.length() > 1)
                if(str.charAt(0) == '-' && (str.charAt(1) >= '0' && str.charAt(1) <= '9')){
                    calculate.add(new BigDecimal(str));
                    continue;
                }

            if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/")) {
                inf2 = calculate.peek();
                calculate.pop();
                inf1 = calculate.peek();
                calculate.pop();
                switch (str) {
                    case "+":
                        result = inf1.add(inf2);
                        break;
                    case "-":
                        result = inf1.add(inf2.negate());
                        break;
                    case "*":
                        result = inf1.multiply(inf2);
                        break;
                    case "/":
                        if(inf2.compareTo(BigDecimal.ZERO) == 0){
                            operations.clear();
                            calculate.clear();
                            OPZ.clear();
                            throw new Exception("ERROR");
                        }
                        else
                            result = inf1.divide(inf2, MathContext.DECIMAL32);
                        break;
                }
                calculate.add(result);
            }
            if (str.equals("sin") || str.equals("cos")) {
                inf1 = calculate.peek();
                calculate.pop();
                switch (str) {

                    case "sin":
                        result = new BigDecimal(Math.sin(Double.parseDouble(String.valueOf(inf1))), MathContext.DECIMAL32);
                        break;
                    case "cos":
                        result = new BigDecimal(Math.cos(Double.parseDouble(String.valueOf(inf1))), MathContext.DECIMAL32);
                        break;
                }
                calculate.add(result);
            }
        }

        operations.clear();
        calculate.clear();
        OPZ.clear();
    }

    int Prior(String c) {
        switch (c) {
            case "sin":
            case "cos":
                return 4;
            case "*":
            case "/":
                return 3;
            case "-":
            case "+":
                return 2;
            case "(":
                return 1;
            default:
                return 0;
        }
    }

    void TranslationIntoReversePolishNotation(String[] InfixForm) {
        int i;
        String str;
        String operation;

        for (i = 0; i < InfixForm.length; i++) {
            str = InfixForm[i];
            if (str.charAt(0) >= '0' && str.charAt(0) <= '9') {
                OPZ.add(str);
                continue;
            }
            if(str.length() > 1)
                if(str.charAt(0) == '-' && (str.charAt(1) >= '0' && str.charAt(1) <= '9')){
                    OPZ.add(str);
                    continue;
                }
            if (str.equals("(")) {
                operations.add(str);
                continue;
            }
            if (str.equals(")")) {
                while (!operations.peek().equals("(")) {
                    operation = operations.peek();
                    operations.pop();
                    OPZ.add(operation);
                }
                operations.pop();
                continue;
            }
            if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("sin") || str.equals("cos")) {
                while (!operations.empty() && Prior(operations.peek()) >= Prior(str)) {
                    operation = operations.peek();
                    operations.pop();
                    OPZ.add(operation);
                }
                operations.add(str);
            }
        }

        while (!operations.empty()) {
            operation = operations.peek();
            operations.pop();
            OPZ.add(operation);
        }
    }
}