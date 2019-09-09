package utils;

import java.util.*;

import org.mimosaframework.core.asm.*;

public class AutoIncrementCreatorDump2 implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC + ACC_FINAL + ACC_SUPER + ACC_ENUM,
                "creator/AutoIncrementCreator",
                "Ljava/lang/Enum<Lcreator/AutoIncrementCreator;>;",
                "java/lang/Enum", null);

        {
            av0 = cw.visitAnnotation("Lorg/mimosaframework/orm/annotation/Table;", true);
            av0.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "id", "Lcreator/AutoIncrementCreator;", null, null);
            {
                av0 = fv.visitAnnotation("Lorg/mimosaframework/orm/annotation/Column;", true);
                av0.visit("type", Type.getType("J"));
                av0.visit("pk", Boolean.TRUE);
                av0.visit("strategy", Type.getType("Lorg/mimosaframework/orm/strategy/AutoIncrementStrategy;"));
                av0.visitEnd();
            }
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "value", "Lcreator/AutoIncrementCreator;", null, null);
            {
                av0 = fv.visitAnnotation("Lorg/mimosaframework/orm/annotation/Column;", true);
                av0.visit("type", Type.getType("B"));
                av0.visitEnd();
            }
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC + ACC_SYNTHETIC, "$VALUES", "[Lcreator/AutoIncrementCreator;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "values", "()[Lcreator/AutoIncrementCreator;", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "creator/AutoIncrementCreator", "$VALUES", "[Lcreator/AutoIncrementCreator;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "[Lcreator/AutoIncrementCreator;", "clone", "()Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "[Lcreator/AutoIncrementCreator;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "valueOf", "(Ljava/lang/String;)Lcreator/AutoIncrementCreator;", null, null);
            mv.visitCode();
            mv.visitLdcInsn(Type.getType("Lcreator/AutoIncrementCreator;"));
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Enum", "valueOf", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;", false);
            mv.visitTypeInsn(CHECKCAST, "creator/AutoIncrementCreator");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE, "<init>", "(Ljava/lang/String;I)V", "()V", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Enum", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();

            mv.visitTypeInsn(NEW, "creator/AutoIncrementCreator");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("id");
            mv.visitInsn(ICONST_0);
            mv.visitMethodInsn(INVOKESPECIAL, "creator/AutoIncrementCreator", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "creator/AutoIncrementCreator", "id", "Lcreator/AutoIncrementCreator;");

            mv.visitTypeInsn(NEW, "creator/AutoIncrementCreator");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("value");
            mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKESPECIAL, "creator/AutoIncrementCreator", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "creator/AutoIncrementCreator", "value", "Lcreator/AutoIncrementCreator;");

            mv.visitInsn(ICONST_2);
            mv.visitTypeInsn(ANEWARRAY, "creator/AutoIncrementCreator");

            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitFieldInsn(GETSTATIC, "creator/AutoIncrementCreator", "id", "Lcreator/AutoIncrementCreator;");
            mv.visitInsn(AASTORE);

            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_1);
            mv.visitFieldInsn(GETSTATIC, "creator/AutoIncrementCreator", "value", "Lcreator/AutoIncrementCreator;");
            mv.visitInsn(AASTORE);

            mv.visitFieldInsn(PUTSTATIC, "creator/AutoIncrementCreator", "$VALUES", "[Lcreator/AutoIncrementCreator;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(4, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}