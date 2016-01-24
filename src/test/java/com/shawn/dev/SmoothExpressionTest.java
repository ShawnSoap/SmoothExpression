package com.shawn.dev;

import org.junit.Assert;
import org.junit.Test;

public class SmoothExpressionTest {

    @Test
    public void testInit() {
        SmoothExpression se = SmoothExpression.regex().build();
    }

    @Test
    public void testStartAndEndOfLine() {
        SmoothExpression se = SmoothExpression.regex()
                .startOfLine()
                .space()
                .endOfLine()
                .build();
        Assert.assertTrue(se.matches(" "));
    }

    @Test
    public void testAnyChar() {
        SmoothExpression se = SmoothExpression.regex().anyChar().anyChar().build();
        Assert.assertTrue(se.matches("0_"));
        Assert.assertFalse(se.matches("aBs"));
    }

    @Test
    public void testAnyCharBut() {
        SmoothExpression se = SmoothExpression.regex().anyCharBut("abc").build();
        Assert.assertTrue(se.matches("e"));
        Assert.assertFalse(se.matches("a"));
        Assert.assertFalse(se.matches("qq"));
    }

}