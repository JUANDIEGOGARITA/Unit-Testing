package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExerciseSolution1 {

  NegativeNumberValidator SUT;

  @Before
  public void setup() {
    SUT = new NegativeNumberValidator();
  }

  @Test
  public void test1() {
    boolean result = SUT.isNegative(-1);
    assertTrue(result);
  }

  @Test
  public void test2() {
    boolean result = SUT.isNegative(0);
    assertFalse(result);
  }

  @Test
  public void test3() {
    boolean result = SUT.isNegative(1);
    assertFalse(result);
  }
}