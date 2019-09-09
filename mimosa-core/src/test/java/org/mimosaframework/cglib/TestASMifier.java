package org.mimosaframework.cglib;

import jdk.internal.org.objectweb.asm.util.ASMifier;

public class TestASMifier {
    public static void main(String[] args) throws Exception {
        // ASMifier.main(new String[]{"D:\\Other\\mimosa\\mimosa-core\\target\\test-classes\\org\\mimosaframework\\cglib\\TtestEnumClass.class"});
        ASMifier.main(new String[]{"D:\\Other\\mimosa\\mimosa-core\\target\\classes\\org\\mimosaframework\\core\\utils\\HttpUtils.class"});
    }
}
