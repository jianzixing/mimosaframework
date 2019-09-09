package org.mimosaframework.cglib;

import org.mimosaframework.core.asm.ClassWriter;
import org.mimosaframework.core.asm.Opcodes;

public class TestEnumAsm {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classWriter.visit(Opcodes.V1_6, Opcodes.ACC_ENUM, "com.test.TestEnum", null, "java/lang/Object", null);


        final byte[] bs = classWriter.toByteArray();
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name)
                    throws ClassNotFoundException {
                return defineClass(name, bs, 0, bs.length);
            }
        };
        Class class1 = classLoader.loadClass("com.test.TestEnum");
    }
}
