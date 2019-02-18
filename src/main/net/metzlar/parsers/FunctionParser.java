package net.metzlar.parsers;

import net.objecthunter.exp4j.ExpressionBuilder;

public class FunctionParser {
    public double parseFunction(String string) {
        if (string.charAt(0) != '$') {
            return Double.parseDouble(string);
        }

        return new ExpressionBuilder(string.substring(1))
                .variables("x")
                .build()
                .setVariable("x", 1)
                .evaluate();
    }
}
