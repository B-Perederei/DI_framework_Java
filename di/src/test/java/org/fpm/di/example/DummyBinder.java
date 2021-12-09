package org.fpm.di.example;

import org.fpm.di.Binder;
import org.fpm.di.Container;

import java.util.*;

public class DummyBinder implements Binder {
    private final HashMap<Class<?>, Object> DependencyObjects = new HashMap<>();
    private final HashMap<Class<?>, Class<?>> DependencyClasses = new HashMap<>();

    public HashMap<Class<?>, Object> getDependencyObjects() {
        return  DependencyObjects;
    }
    public HashMap<Class<?>, Class<?>> getDependencyClasses() {
        return  DependencyClasses;
    }

    @Override
    public <T> void bind(Class<T> clazz) {
        if (classIsInContainer(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is already in container");
        }
       DependencyObjects.put(clazz, null);
    }

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        if (classIsInContainer(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is already in container");
        }
        DependencyClasses.put(clazz, implementation);
    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        if (classIsInContainer(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is already in container");
        }
        DependencyObjects.put(clazz, instance);
    }

    private <T> boolean classIsInContainer(Class<T> clazz) {
        return DependencyObjects.containsKey(clazz)|| DependencyClasses.containsKey(clazz);
    }

}
