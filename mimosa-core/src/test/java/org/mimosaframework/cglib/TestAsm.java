package org.mimosaframework.cglib;

import org.mimosaframework.core.asm.ClassWriter;
import org.mimosaframework.core.asm.MethodVisitor;
import org.mimosaframework.core.asm.Opcodes;

import java.lang.reflect.Method;

public class TestAsm {
    public static void main(String[] args) throws Exception {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classWriter.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, "Test", null, "java/lang/Object", null);
        {
            /**
             * 构造方法
             */
            MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, " ", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);//0 表示当前对象
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
                    "java/lang/Object", " ", "()V");
            methodVisitor.visitInsn(Opcodes.RETURN);
            methodVisitor.visitMaxs(0, 0);
            methodVisitor.visitEnd();
        }

        {
            /**
             * 一个计算两个数之和的cal方法
             */
            MethodVisitor methodVisitor =
                    classWriter.visitMethod(Opcodes.ACC_PUBLIC, "cal", "(II)I", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
            methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
            methodVisitor.visitInsn(Opcodes.IADD);
            methodVisitor.visitInsn(Opcodes.IRETURN);
            methodVisitor.visitMaxs(2, 2);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();
        /**
         * 从classwrite 获得这个类的byte 数组。然后加载这个数组为一个class
         * 利用反射调用方法
         */
        final byte[] bs = classWriter.toByteArray();
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name)
                    throws ClassNotFoundException {
                return defineClass(name, bs, 0, bs.length);
            }
        };
        Class class1 = classLoader.loadClass("Test");
        Method method = class1.getMethod("cal", int.class, int.class);
        // System.out.println(method.invoke(class1.newInstance(), 1, 2));
    }
}