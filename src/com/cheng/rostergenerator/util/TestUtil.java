package com.cheng.rostergenerator.util;

public class TestUtil {
    public static boolean isRunningUnitTest() {  
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
          if (element.getClassName().startsWith("org.junit.")) {
            return true;
          }           
        }
        return false;
      }
}
