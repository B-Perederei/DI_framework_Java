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
        if (classIsInContainer(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is already in container");
        }
       final Annotation[] Annotations = clazz.getAnnotations();
       if (checkSingletonInArray(Annotations)) {
           Singletons.put(clazz, null);
       }
       else {
           Prototypes.add(clazz);
       }
    }

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        if (classIsInContainer(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is already in container");
        }
    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        if (classIsInContainer(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is already in container");
        }
    }

    private <T> boolean classIsInContainer(Class<T> clazz) {
        return Singletons.containsKey(clazz) || DependencyObjects.containsKey(clazz)
                || DependencyClasses.containsKey(clazz) || Prototypes.contains(clazz);
    }

    private boolean checkSingletonInArray(Annotation[] Annotations) {
        for (Annotation Anon : Annotations) {
            if (Anon.annotationType().equals(Singleton.class)) {
                return true;
            }
        }
        return  false;
    }

    public Container getContainer()
    {
        return new DummyContainer(Singletons, DependencyObjects, DependencyClasses, Prototypes);
    }
}
