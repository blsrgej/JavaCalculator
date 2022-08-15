import java.util.Scanner;
import java.util.*;
public class Calculator {
    private final String[] validArabNumbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    private final String[] validRomNumbers = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
    private final String[] validOperations = { "+", "-", "*", "/" };
    private static final int NUMERAL_TYPE = 0;
    private static final int NOT_FOUND = -1;
    private static final int FIRST_NUMERAL = 1;
    private static final int SECOND_NUMERAL = 2;
    private static final int OPERATOR = 3;

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var task = scanner.nextLine();
        var calculator = new Calculator();

        try {
            calculator.calculate(task);
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    /**
     *
     * @param task -> задание от пользователя в виде строки вида "1 (+,-,*,-) 2" или "I (+,-,*,/) IV".
     * @throws Exception -> выбрасывается исключение, если строка с заданием не соответствует условиям.
     */
    public void calculate(String task) throws  Exception{

        var params = this.getParameters(task,
                this.validArabNumbers,
                this.validRomNumbers,
                this.validOperations);

        if (params.get(NUMERAL_TYPE).equals("Arabic numerals"))
            System.out.println(arabicNumCalculate(params));
        else if(params.get(NUMERAL_TYPE).equals("Roman numerals"))
            System.out.println(romanNumCalculate(params, validRomNumbers));
    }

    /**
     *
     * @param -> task - строка с цифрами и оператором, полученная от пользователя.
     * @param -> validArabNumbers - массив с допустимыми арабскими цифрами.
     * @param -> validRomNumbers - массив с допустимыми римскими цифрами.
     * @param -> validOperations - массив в допустимыми операторами.
     * @return -> метод возвращает ArrayList<String>, первый элемент - 1-ая цифра; второй элемент - 2-ая цифра; третий элемент - оператор.
     * @throws Exception -> выбрасывается исключение, если строка с заданием не соответствует условиям.
     */
    private ArrayList<String> getParameters(String task,
                                            String[] validArabNumbers,
                                            String[] validRomNumbers,
                                            String[] validOperations) throws  Exception{
        ArrayList<String> params = new ArrayList<>();

        if (!(task == null || task.isEmpty())) {
            String t = task.toUpperCase().trim().replace(" ", "");
            String operator;
            int operatorIndex = NOT_FOUND;

            for (var vo : validOperations) {
                operatorIndex = t.indexOf(vo);
                if(operatorIndex > 0)
                    break;
            }

            if (operatorIndex == NOT_FOUND)
                throw new Exception("Input parameters are set incorrectly!");
            else
                operator = Character.toString(t.charAt(operatorIndex));

            String separator = String.format("\\%s", operator);
            ArrayList<String> parts= new ArrayList<>(List.of(t.split(separator)));
            parts.add(operator);

            int index = getArabicNumeralIndex(parts.get(0), validArabNumbers);

            if (index != NOT_FOUND) {
                params.add(NUMERAL_TYPE,"Arabic numerals");
                params.add(FIRST_NUMERAL, validArabNumbers[index]);

                index = getArabicNumeralIndex(parts.get(1), validArabNumbers);

                if (index != NOT_FOUND) {
                    params.add(SECOND_NUMERAL, validArabNumbers[index]);
                } else {
                    throw new Exception("Input parameters are set incorrectly!");
                }
            }

            index = getRomanNumeralIndex(parts.get(0), validRomNumbers);

            if (index != NOT_FOUND) {
                params.add(NUMERAL_TYPE,"Roman numerals");
                params.add(FIRST_NUMERAL, validRomNumbers[index]);

                index = getRomanNumeralIndex(parts.get(1), validRomNumbers);

                if (index != NOT_FOUND) {
                    params.add(SECOND_NUMERAL, validRomNumbers[index]);
                } else {
                    throw new Exception("Input parameters are set incorrectly!");
                }
            }

            for (var s : validOperations) {
                if (s.equals(parts.get(2))) {
                    params.add(s);
                    break;
                }
            }
            return params;
        } else {
            throw new Exception("Input parameters are set incorrectly!");
        }
    }

    /**
     * Метод поиска цифры в массиве допустимых арабский цифер.
     * @param numeral -> строка, которая содержит одну цифру.
     * @param validArabNumbers -> массив допустимых арабских цифер.
     * @return метод возвращает индекс цифры(параметр numeral) в массиве (параметр validArabNumbers).
     */
    private Integer getArabicNumeralIndex(String numeral, String[] validArabNumbers) {
        for (int i = 0; i < validArabNumbers.length; i++) {
            if (validArabNumbers[i].equals(numeral)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     *  Метод поиска цифры в массиве допустимых римских цифер.
     * @param numeral -> строка, которая содержит одну цифру.
     * @param validRomNumbers -> массив допустимых римских цифер.
     * @return метод возвращает индекс цифры(параметр numeral) в массиве (параметр validArabNumbers)
     */
    private  Integer getRomanNumeralIndex(String numeral, String[] validRomNumbers) {
        for (int i = 0; i < validArabNumbers.length; i++) {
            if (validRomNumbers[i].equals(numeral)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Метод для совершения операций с арабскими цифрами.
     * @param params
     * @return
     */
    private Integer arabicNumCalculate(ArrayList<String> params) {
        int a = Integer.parseInt(params.get(FIRST_NUMERAL));
        int b = Integer.parseInt(params.get(SECOND_NUMERAL));
        int result = 0;

        switch (params.get(OPERATOR)) {
            case "+": result =  a + b; break;
            case "-": result = a - b; break;
            case "*": result = a * b; break;
            case "/": result = a / b; break;
        }
        return  result;
    }

    /**
     * Метод для совершения операций с римскими цифрами.
     * @param params
     * @param validRomNumbers
     * @return -> результат работы метова строка, которая обозначает римское число.
     * @throws Exception -> исключение выбрасывается, если результат вычисление меньше 0.
     */
    private String romanNumCalculate(ArrayList<String> params,
                                     String[] validRomNumbers) throws  Exception{
        int[] values = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        String[] romanLetters = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
        StringBuilder roman = new StringBuilder();

        int a = 0;
        int b = 0;
        int result = 0;

        for (int i = 0; i < validRomNumbers.length; i++) {
            if (validRomNumbers[i].equals(params.get(FIRST_NUMERAL))) {
                a = i + 1;
                break;
            }
        }

        for (int i = 0; i < validRomNumbers.length; i++) {
            if (validRomNumbers[i].equals(params.get(SECOND_NUMERAL))) {
                b = i + 1;
                break;
            }
        }

        switch (params.get(OPERATOR)) {
            case "+": result =  a + b; break;
            case "-": result = a - b; break;
            case "*": result = a * b; break;
            case "/": result = a / b; break;
        }

        for (int i = 0; i < values.length; i++)
        {
            while (result >= values[i])
            {
                result = result - values[i];
                roman.append(romanLetters[i]);
            }
        }

        if (result < 0) {
            throw new Exception("Input parameters are set incorrectly!");
        } else {
            return roman.toString();
        }
    }
}
