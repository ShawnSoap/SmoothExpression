package com.shawn.dev;

import java.util.regex.Pattern;

public class SmoothExpression {

    public SmoothExpression(final Pattern pattern) {
        this.pattern = pattern;
    }

    public String getRegularExression() {
        return this.pattern.pattern();
    }

    private final Pattern pattern;

    public static ExpressionBuilder regex() {
        return new ExpressionBuilder();
    }

    public static class ExpressionBuilder {
        private StringBuilder prefix = new StringBuilder();
        private StringBuilder patternContent = new StringBuilder();
        private StringBuilder suffix = new StringBuilder();
        private int modifiers = Pattern.MULTILINE;

        ExpressionBuilder() {

        }

        public ExpressionBuilder add(String content) {
            patternContent.append("(?:" + content + ")");
            return this;
        }

        public ExpressionBuilder anything() {
            return this;
        }

        public ExpressionBuilder anythingBut(String content) {
            return this;
        }

        public ExpressionBuilder startOfLine() {
            patternContent.append("^");
            return this;
        }

        public ExpressionBuilder endOfLine() {
            patternContent.append("$");
            return this;
        }

        public ExpressionBuilder singleDigit() {
            return this;
        }

        public ExpressionBuilder integerNumber() {
            return this;
        }

        public ExpressionBuilder floatNumber() {
            return this;
        }

        public ExpressionBuilder wordChar() {
            return this;
        }

        public ExpressionBuilder nonWordChar() {
            return this;
        }

        public ExpressionBuilder then(final String content) {
            this.add(content);
            return this;
        }

        public ExpressionBuilder then(SmoothExpression expression) {
            this.add(expression.getRegularExression());
            return this;
        }

        public ExpressionBuilder capture() {
            return this;
        }

        public ExpressionBuilder endCapture() {
            return this;
        }

        public ExpressionBuilder oneOrMore() {
            return this;
        }

        public ExpressionBuilder zeroOrMore() {
            return this;
        }

        public ExpressionBuilder maybe(final String content) {
            return this;
        }

        public ExpressionBuilder addModifier(final char pModifier) {
            switch (pModifier) {
                case 'd':
                    modifiers |= Pattern.UNIX_LINES;
                    break;
                case 'i':
                    modifiers |= Pattern.CASE_INSENSITIVE;
                    break;
                case 'x':
                    modifiers |= Pattern.COMMENTS;
                    break;
                case 'm':
                    modifiers |= Pattern.MULTILINE;
                    break;
                case 's':
                    modifiers |= Pattern.DOTALL;
                    break;
                case 'u':
                    modifiers |= Pattern.UNICODE_CASE;
                    break;
                case 'U':
                    modifiers |= Pattern.UNICODE_CHARACTER_CLASS;
                    break;
                default:
                    break;
            }

            return this;
        }

        public ExpressionBuilder removeModifier(final char pModifier) {
            switch (pModifier) {
                case 'd':
                    modifiers &= ~Pattern.UNIX_LINES;
                    break;
                case 'i':
                    modifiers &= ~Pattern.CASE_INSENSITIVE;
                    break;
                case 'x':
                    modifiers &= ~Pattern.COMMENTS;
                    break;
                case 'm':
                    modifiers &= ~Pattern.MULTILINE;
                    break;
                case 's':
                    modifiers &= ~Pattern.DOTALL;
                    break;
                case 'u':
                    modifiers &= ~Pattern.UNICODE_CASE;
                    break;
                case 'U':
                    modifiers &= ~Pattern.UNICODE_CHARACTER_CLASS;
                    break;
                default:
                    break;
            }

            return this;
        }

        public SmoothExpression build() {
            Pattern pattern = Pattern.compile("");
            return new SmoothExpression(pattern);
        }

    }



}