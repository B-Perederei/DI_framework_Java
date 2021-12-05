package org.fpm.di.example;

import org.fpm.di.Binder;
import org.fpm.di.Container;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


public class DummyBinder implements Binder {
    private final HashMap<Class<?>, Object> Singletons = new HashMap<>();
    private final HashMap<Class<?>, Object> DependencyObjects = new HashMap<>();
    private final HashMap<Class<?>, Class<?>> DependencyClasses = new HashMap<>();
    private final List<Class<?>> Prototypes = new ArrayList<>();

    @Override
    public <T> void bind(Class<T> clazz) {
       final Annotation[] Annotations = clazz.getAnnotations();
       for (int i = 0; i < Annotations.length; i++) {
           if (Annotations[i].annotationType().equals(Singleton.class))
           {

           }
       }
    }

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {

    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {

    }

    public Container getContainer()
    {
        return new DummyContainer(Singletons, DependencyObjects, DependencyClasses, Prototypes);
    }
}
/*
    Якщо біндить не можна після конфігурації, то створити мапу в біндері, з нею працювати
 */