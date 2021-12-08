package org.fpm.di.example;
import org.fpm.di.Container;

import java.util.HashMap;

import javax.inject.Singleton;
import java.lang.reflect.*;


public class DummyContainer implements Container {
    private final HashMap<Class<?>, Object> DependencyObjects;
    private final HashMap<Class<?>, Class<?>> DependencyClasses;

    public DummyContainer (HashMap<Class<?>, Object> DependencyObjects, HashMap<Class<?>, Class<?>> DependencyClasses) {
        this.DependencyObjects = DependencyObjects;
        this.DependencyClasses = DependencyClasses;
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        if (DependencyObjects.containsKey(clazz)) {
            if (DependencyObjects.get(clazz) == null) {
                if (clazz.getAnnotation(Singleton.class) != null) {
                    // Creating Singleton and saving it
                    DependencyObjects.put(clazz, createObj(clazz));
                    return clazz.cast(DependencyObjects.get(clazz));
                }
                // Creating Prototype
                return clazz.cast(createObj(clazz));
            }
            return clazz.cast(DependencyObjects.get(clazz));
        }
        else if (DependencyClasses.containsKey(clazz)){
            return clazz.cast(getComponent(DependencyClasses.get(clazz)));
        }
        // Logic when bind class-class (A, B) and B class is not here

        throw new RuntimeException("Class " + clazz.getName() + " is missing in container");
    }

    private <T> T createObj(Class<T> clazz) {
        // TODO: Make injection logic, processing of Exceptions, multiple constructors logic
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
