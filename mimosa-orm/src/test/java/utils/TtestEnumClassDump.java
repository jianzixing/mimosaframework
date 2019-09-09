package utils;

import org.mimosaframework.core.asm.*;

public class TtestEnumClassDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC + ACC_FINAL + ACC_SUPER + ACC_ENUM,
                "org/mimosaframework/cglib/TtestEnumClass", "Ljava/lang/Enum<Lorg/mimosaframework/cglib/TtestEnumClass;>;", "java/lang/Enum", null);

        {
            av0 = cw.visitAnnotation("Ljavax/annotation/Resource;", true);
            av0.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "id", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name1", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            {
                av0 = fv.visitAnnotation("Ljavax/annotation/Resource;", true);
                av0.visit("name", "abc");
                av0.visit("type", Type.getType("Lorg/mimosaframework/core/server/Server;"));
                av0.visitEnd();
            }
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name2", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name3", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name4", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name5", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name6", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name7", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name8", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name9", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name10", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name11", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name12", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name13", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name14", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name15", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name16", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, "name17", "Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC + ACC_SYNTHETIC, "$VALUES", "[Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "values", "()[Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "$VALUES", "[Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "[Lorg/mimosaframework/cglib/TtestEnumClass;", "clone", "()Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "[Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "valueOf", "(Ljava/lang/String;)Lorg/mimosaframework/cglib/TtestEnumClass;", null, null);
            mv.visitCode();
            mv.visitLdcInsn(Type.getType("Lorg/mimosaframework/cglib/TtestEnumClass;"));
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Enum", "valueOf", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;", false);
            mv.visitTypeInsn(CHECKCAST, "org/mimosaframework/cglib/TtestEnumClass");
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

            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("id");
            mv.visitInsn(ICONST_0);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "id", "Lorg/mimosaframework/cglib/TtestEnumClass;");

            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name");
            mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name1");
            mv.visitInsn(ICONST_2);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name1", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name2");
            mv.visitInsn(ICONST_3);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name2", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name3");
            mv.visitInsn(ICONST_4);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name3", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name4");
            mv.visitInsn(ICONST_5);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name4", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name5");
            mv.visitIntInsn(BIPUSH, 6);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name5", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name6");
            mv.visitIntInsn(BIPUSH, 7);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name6", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name7");
            mv.visitIntInsn(BIPUSH, 8);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name7", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name8");
            mv.visitIntInsn(BIPUSH, 9);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name8", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name9");
            mv.visitIntInsn(BIPUSH, 10);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name9", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name10");
            mv.visitIntInsn(BIPUSH, 11);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name10", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name11");
            mv.visitIntInsn(BIPUSH, 12);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name11", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name12");
            mv.visitIntInsn(BIPUSH, 13);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name12", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name13");
            mv.visitIntInsn(BIPUSH, 14);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name13", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name14");
            mv.visitIntInsn(BIPUSH, 15);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name14", "Lorg/mimosaframework/cglib/TtestEnumClass;");

            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name15");
            mv.visitIntInsn(BIPUSH, 16);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name15", "Lorg/mimosaframework/cglib/TtestEnumClass;");

            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name16");
            mv.visitIntInsn(BIPUSH, 17);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name16", "Lorg/mimosaframework/cglib/TtestEnumClass;");

            mv.visitTypeInsn(NEW, "org/mimosaframework/cglib/TtestEnumClass");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("name17");
            mv.visitIntInsn(BIPUSH, 18);
            mv.visitMethodInsn(INVOKESPECIAL, "org/mimosaframework/cglib/TtestEnumClass", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name17", "Lorg/mimosaframework/cglib/TtestEnumClass;");

            mv.visitIntInsn(BIPUSH, 19);
            mv.visitTypeInsn(ANEWARRAY, "org/mimosaframework/cglib/TtestEnumClass");

            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "id", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);

            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_1);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);

            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_2);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name1", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);

            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_3);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name2", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_4);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name3", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_5);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name4", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);

            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 6);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name5", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);

            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 7);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name6", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 8);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name7", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 9);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name8", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 10);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name9", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 11);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name10", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 12);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name11", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 13);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name12", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 14);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name13", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 15);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name14", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 16);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name15", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 17);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name16", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);

            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, 18);
            mv.visitFieldInsn(GETSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "name17", "Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(AASTORE);

            mv.visitFieldInsn(PUTSTATIC, "org/mimosaframework/cglib/TtestEnumClass", "$VALUES", "[Lorg/mimosaframework/cglib/TtestEnumClass;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(4, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}