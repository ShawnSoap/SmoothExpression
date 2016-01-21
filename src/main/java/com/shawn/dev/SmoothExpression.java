package com.shawn.dev;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmoothExpression {

    public SmoothExpression(final Pattern pattern) {
        this.pattern = pattern;
    }

    public String getRegularExpression() {
        return this.pattern.pattern();
    }

    public boolean matches(String target) {
        return pattern.matcher(target).matches();
    }

    public List<String> findGroups(final String toTest, final int group) {
        List<String> groups = new ArrayList<String>();
        Matcher m = pattern.matcher(toTest);
        while (m.find()) {
            groups.add(m.group(group));
        }
        return groups;
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

        private ExpressionBuilder add(String content) {
            patternContent.append("(?:" + content + ")");
            return this;
        }

        public ExpressionBuilder anything() {
            this.add("[\\s\\S]*");
            return this;
        }

        public ExpressionBuilder anythingBut(String content) {
            this.add("[^" +  content + "]*");
            return this;
        }

        public ExpressionBuilder startOfLine() {
            this.add("^");
            return this;
        }

        public ExpressionBuilder endOfLine() {
            this.add("$");
            return this;
        }

        public ExpressionBuilder singleDigit() {
            this.add("\\d");
            return this;
        }

        public ExpressionBuilder integerNumber() {
            this.add("\\d+");
            return this;
        }

        public ExpressionBuilder floatNumber() {
            this.add("-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)");
            return this;
        }

        public ExpressionBuilder wordChar() {
            this.add("[a-zA-Z]");
            return this;
        }

        public ExpressionBuilder nonWordChar() {
            this.add("[^a-zA-z]");
            return this;
        }

        public ExpressionBuilder then(final String content) {
            this.add(content);
            return this;
        }

        public ExpressionBuilder capture() {
            patternContent.append("(");
            return this;
        }

        public ExpressionBuilder endCapture() {
            patternContent.append(")");
            return this;
        }

        public ExpressionBuilder oneOrMore() {
            this.add("+");
            return this;
        }

        public ExpressionBuilder zeroOrMore() {
            this.add("*");
            return this;
        }

        public ExpressionBuilder maybe(final String content) {
            this.add("(?:" + content + ")?");
            return this;
        }

        public ExpressionBuilder oneOf(final String... contents) {
            this.add(String.join("|", contents));
            return this;
        }

        public ExpressionBuilder multiple(final String content, Integer min, Integer max) {
            if (min == null && max == null) {
                this.patternContent.append("(?:" + content + ")+");
                return this;
            } else if (min == null && max != null) {
                this.patternContent.append("(?:" + content + "){1," + max + "}");
                return this;
            } else if (min != null && max == null) {
                this.patternContent.append("(?:" + content + "){" + min + ",}");
                return this;
            } else if (min != null && max != null) {
                this.patternContent.append("(?:" + content + "){" + min + "," + max + "}");
                return this;
            }
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
            Pattern pattern = Pattern.compile(
                    this.prefix.toString() +
                    this.patternContent.toString() +
                    this.suffix.toString(),
                    modifiers);
            return new SmoothExpression(pattern);
        }

    }

    public static void main(String[] args) {
        SmoothExpression exp = SmoothExpression.regex().capture().integerNumber().endCapture().then("aa").build();
        System.out.println(exp.getRegularExpression());
        System.out.println(exp.matches("11 12 11"));
        List<String> list = exp.findGroups("11aa12aa", 0);
        for (String s : list) {
            System.out.println(s);
        }
    }

}