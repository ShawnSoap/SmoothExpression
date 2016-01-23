package com.shawn.dev;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utilization class to construct regular expressions
 * e.g SmoothExpression.regex().startOfLine().anythingBut(" ").endOfLine().build()
 *
 * @author Qijun
 * @since v1.0
 */
public class SmoothExpression {

    public SmoothExpression(final Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the actual regular expression that is built
     */
    public String getRegularExpression() {
        return this.pattern.pattern();
    }

    /**
     * check if target matches the specified expression
     * @param target the target string to check
     * @return true for match and false for not
     */
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

    /**
     * start an actual builder to build the expression
     * e.g Expression.regex().startOfLine().then("hello world").endOfLine().build()
     * @return a ExpressionBuilder object to build the SmoothExpression itself
     */
    public static ExpressionBuilder regex() {
        return new ExpressionBuilder();
    }

    /**
     * An static inner class which is used to build the SmoothExpression
     */
    public static class ExpressionBuilder {
        private StringBuilder prefix = new StringBuilder();
        private StringBuilder patternContent = new StringBuilder();
        private StringBuilder suffix = new StringBuilder();
        private int modifiers = Pattern.MULTILINE;

        /**
         * protected constructor that can only
         * be called inside the package scope
         */
        ExpressionBuilder() {

        }

        /**
         * A private quick method to add a piece of pattern that should
         * not be captured in the group capturing, the added pattern with
         * be wrapped with (?: )
         *
         * @param content the content of the piece of pattern
         * @return the builder
         */
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

        public ExpressionBuilder space() {
            this.add("\\s");
            return this;
        }

        public ExpressionBuilder nonSpace() {
            this.add("\\S");
            return this;
        }

        public ExpressionBuilder anyOf(final String content) {
            patternContent.append("[" + content + "]");
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

        public ExpressionBuilder find(final String content) {
            this.patternContent.append("(" + content + ")");
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
            patternContent.append("(?:" + content + ")?");
            return this;
        }

        public ExpressionBuilder oneOf(final String... contents) {
            this.add(String.join("|", contents));
            return this;
        }

        /**
         * Append a pattern of multiple times to the pattern in construction.
         * Minimum and maximum number of replications can be set to null for no such demands
         *
         * @param content the pattern itself
         * @param min minimum time that the pattern should appear
         * @param max maximum time that the pattern should appear
         * @return the builder
         */
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

        /**
         * @param modifier the usual modifier of Java Pattern modifiers to add
         * @return the builder
         */
        public ExpressionBuilder addModifier(final char modifier) {
            switch (modifier) {
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

        /**
         * @param modifier the usual modifier of Java Pattern modifiers to remove
         * @return the builder
         */
        public ExpressionBuilder removeModifier(final char modifier) {
            switch (modifier) {
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

        /**
         * build the SmoothExpression that can be used for matching and grouping
         * @return the built SmoothExpression object
         */
        public SmoothExpression build() {
            Pattern pattern = Pattern.compile(
                    this.prefix.toString() +
                    this.patternContent.toString() +
                    this.suffix.toString(),
                    modifiers);
            return new SmoothExpression(pattern);
        }

    }

    /**
     * main function for test only
     * @param args no use
     */
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