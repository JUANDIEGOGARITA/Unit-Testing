package com.techyourchance.unittestingfundamentals.example1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PositiveNumberValidatorTest {

  PositiveNumberValidator SUT;

  @Before
  public void setup() {
    SUT = new PositiveNumberValidator();
  }

  @Test
  public void test1() {
    boolean result = SUT.isPositive(-1);
    assertFalse(result);
  }

  @Test
  public void test2() {
    boolean result = SUT.isPositive(0);
    assertFalse(result);
  }

  @Test
  public void test3() {
    boolean result = SUT.isPositive(1);
    assertTrue(result);
  }
}