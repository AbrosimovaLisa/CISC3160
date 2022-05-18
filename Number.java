import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Number {
    public static void main(String[] args) throws Exception {

        System.out.println("Give expression");

        Scanner scanner = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String currentConsoleLine = scanner.nextLine();
            if (currentConsoleLine.equals("")) {
                break;
            }
            stringBuilder.append(currentConsoleLine);
        }


        /*
        a = 1;
        a;
        b = 2 + 1 (!!! ; )
        */
        String tokens = stringBuilder.toString();
        if (tokens.charAt(tokens.length() - 1) != ';') {
            throw new Exception("You forgot ; at the end of the line");
        }

        String cleanTokens = tokens
            .replaceAll("\n", "")
            .replaceAll(" ", "");

        String[] operations = cleanTokens.split(";");

        /*
        a = 1;
        a; (!!! = )
        b = 2 + 1;
        */
        for (String operation: operations) {
            Pattern pattern = Pattern.compile("=");
            Matcher matcher = pattern.matcher(operation);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            if (count != 1) {
                throw new Exception("You forgot or wrote extra = \"" + operation + "\"");
            }
        }

        /*
        a = 1;
        a = +--a; (!!! +- )
        b = 2 + 1;
        */
        for (String operation: operations) {
            Pattern pattern = Pattern.compile("-\\+");
            Matcher matcher = pattern.matcher(operation);
            if (matcher.find()) {
                throw new Exception("You can't use -+ in \"" + operation + "\"");
            }
        }
        /*
        a = 1;
        1a = -a; (!!! 1a )
        b = 2 + 1;
        */
//        "[a-zA-Z][a-zA-Z0-9_]*"
        for (String operation: operations) {
            String input = operation.split("=")[0];
            if (input.length()>1) {
                Pattern pattern = Pattern.compile("(^[a-z]|^[_])\\w+$");
                Matcher matcher = pattern.matcher(input);
                if (!matcher.find()) {
                    throw new Exception("You can't use this name for variable in \"" + operation + "\"");
                }
            } else {
                if (!Character.isAlphabetic(operation.charAt(0))) {
                    throw new Exception("You can't use this name for variable in \"" + operation + "\"");
                }
            }
        }

        ArithmeticParserService arithmeticParserService = new ArithmeticParserService();

        HashMap<String, Integer> nameOfVariableToValue = new HashMap<>();
        for (String operation: operations) {
            String nameOfVariable = operation.split("=")[0];
            int result = arithmeticParserService.calculate(operation.split("=")[1], nameOfVariableToValue);
            nameOfVariableToValue.put(nameOfVariable, result);
            System.out.println(nameOfVariable + " = " + result);
        }

    }
}