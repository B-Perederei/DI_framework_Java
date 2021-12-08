package org.fpm.di.example;

import org.fpm.di.Container;
import java.lang.reflect.*;
import java.util.*;

public class DummyContainer implements Container {

    private final HashMap<Class<?>, Object> Singletons;
    private final HashMap<Class<?>, Object> DependencyObjects;
    private final HashMap<Class<?>, Class<?>> DependencyClasses;
    private final List<Class<?>> Prototypes;

    public DummyContainer (HashMap<Class<?>, Object> Singletons, HashMap<Class<?>, Object> DependencyObjects,
                           HashMap<Class<?>, Class<?>> DependencyClasses,  List<Class<?>> Prototypes) {
        this.Singletons = Singletons;
        this.DependencyObjects = DependencyObjects;
        this.DependencyClasses = DependencyClasses;
        this.Prototypes = Prototypes;
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        if (Singletons.containsKey(clazz)) {
            if (Singletons.get(clazz) == null) {
                Singletons.put(clazz, createObj(clazz));
            }
            return clazz.cast(Singletons.get(clazz));
        }
        else if (Prototypes.contains(clazz)) {
            return clazz.cast(createObj(clazz));
        }
        else {
            throw new RuntimeException("Class " + clazz.getName() + " is missing in container");
        }
    }

    private <T> T createObj(Class<T> clazz) {
        try {
            Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
            declaredConstructors[0].setAccessible(true);
            Object obj = declaredConstructors[0].newInstance();
            return clazz.cast(obj);
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            System.out.println(e);
            return null;
        }
    }
}
