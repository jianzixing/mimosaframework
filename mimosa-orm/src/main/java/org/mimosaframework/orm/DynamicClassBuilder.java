package org.mimosaframework.orm;

import org.mimosaframework.core.asm.*;

import java.util.ArrayList;
import java.util.List;

import static org.mimosaframework.core.asm.Opcodes.*;

public class DynamicClassBuilder {
    private List<DynamicTableItem> items = new ArrayList<>();
    private String className;
    private String packageClassName;

    private ClassWriter cw = new ClassWriter(0);
    private FieldVisitor fv;
    private MethodVisitor mv;
    private AnnotationVisitor av0;

    /**
     * @param packageClassName org.xxx.HelloWorld
     */
    public DynamicClassBuilder(String packageClassName) {
        this.className = packageClassName.replaceAll("\\.", "/");
        this.packageClassName = packageClassName;
        cw.visit(V1_7, ACC_PUBLIC + ACC_FINAL + ACC_SUPER + ACC_ENUM, className,
                "Ljava/lang/Enum<L" + className + ";>;", "java/lang/Enum", null);

        {
            av0 = cw.visitAnnotation("Lorg/mimosaframework/orm/annotation/Table;", true);
            av0.visitEnd();
        }
    }

    public void addItem(DynamicTableItem item) {
        items.add(item);
    }

    public Class getEnumClass() throws ClassNotFoundException {
        final byte[] bs = this.getEnumBytes();

        ClassLoader classLoader = new ClassLoader(Thread.currentThread().getContextClassLoader()) {
            @Override
            protected Class<?> findClass(String name)
                    throws ClassNotFoundException {
                return defineClass(name, bs, 0, bs.length);
            }
        };
        // 如果class失去Root引用那么就会自动GC掉
        Class c = classLoader.loadClass(this.packageClassName);
        return c;
    }

    public byte[] getEnumBytes() {

        for (DynamicTableItem item : items) {
            String itemName = item.getFieldName();
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM, itemName, "L" + className + ";", null, null);
            {
                av0 = fv.visitAnnotation("Lorg/mimosaframework/orm/annotation/Column;", true);
                if (item.getType() != null) {
                    av0.visit("type", item.getASMType());
                }
                if (item.getLength() != 0) {
                    av0.visit("length", item.getLength());
                }
                if (item.getDecimalDigits() != 0) {
                    av0.visit("decimalDigits", item.getDecimalDigits());
                }
                if (item.isNullable()) {
                    av0.visit("nullable", item.isNullable());
                }
                if (item.isPk()) {
                    av0.visit("pk", item.isPk());
                }
                if (item.isIndex()) {
                    av0.visit("index", item.isIndex());
                }
                if (item.isUnique()) {
                    av0.visit("unique", item.isUnique());
                }
                if (item.getComment() != null) {
                    av0.visit("comment", item.getComment());
                }
                if (item.isTimeForUpdate()) {
                    av0.visit("timeForUpdate", item.isTimeForUpdate());
                }
                if (item.getDefaultValue() != null) {
                    av0.visit("defaultValue", item.getDefaultValue());
                }
                if (item.getStrategy() != null) {
                    String strategyName = item.getStrategy().getName();
                    av0.visit("strategy", Type.getType("L" + strategyName.replaceAll("\\.", "/") + ";"));
                }
                av0.visitEnd();
            }
            fv.visitEnd();
        }

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC + ACC_SYNTHETIC, "$VALUES", "[L" + className + ";", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "values", "()[L" + className + ";", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, className, "$VALUES", "[L" + className + ";");
            mv.visitMethodInsn(INVOKEVIRTUAL, "[L" + className + ";", "clone", "()Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "[L" + className + ";");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "valueOf", "(Ljava/lang/String;)L" + className + ";", null, null);
            mv.visitCode();
            mv.visitLdcInsn(Type.getType("L" + className + ";"));
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Enum", "valueOf", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;", false);
            mv.visitTypeInsn(CHECKCAST, className);
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

        mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        mv.visitCode();

        int count = 0;
        for (DynamicTableItem item : items) {
            String itemName = item.getFieldName();

            int ICONST = count;
            if (count == 0) ICONST = ICONST_0;
            if (count == 1) ICONST = ICONST_1;
            if (count == 2) ICONST = ICONST_2;
            if (count == 3) ICONST = ICONST_3;
            if (count == 4) ICONST = ICONST_4;
            if (count == 5) ICONST = ICONST_5;


            mv.visitTypeInsn(NEW, className);
            mv.visitInsn(DUP);
            mv.visitLdcInsn(itemName);
            if (count <= 5) {
                mv.visitInsn(ICONST);
            } else {
                mv.visitIntInsn(BIPUSH, count);
            }
            mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, className, itemName, "L" + className + ";");
            count++;
        }

        mv.visitIntInsn(BIPUSH, count);
        mv.visitTypeInsn(ANEWARRAY, className);

        count = 0;
        for (DynamicTableItem item : items) {
            String itemName = item.getFieldName();

            int ICONST = count;
            if (count == 0) ICONST = ICONST_0;
            if (count == 1) ICONST = ICONST_1;
            if (count == 2) ICONST = ICONST_2;
            if (count == 3) ICONST = ICONST_3;
            if (count == 4) ICONST = ICONST_4;
            if (count == 5) ICONST = ICONST_5;


            mv.visitInsn(DUP);
            if (count <= 5) {
                mv.visitInsn(ICONST);
            } else {
                mv.visitIntInsn(BIPUSH, count);
            }
            mv.visitFieldInsn(GETSTATIC, className, itemName, "L" + className + ";");
            mv.visitInsn(AASTORE);

            count++;
        }


        mv.visitFieldInsn(PUTSTATIC, className, "$VALUES", "[L" + className + ";");
        mv.visitInsn(RETURN);
        mv.visitMaxs(4, 0);
        mv.visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }
}
