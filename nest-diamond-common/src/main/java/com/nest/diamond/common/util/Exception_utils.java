package com.nest.diamond.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exception_utils {

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
