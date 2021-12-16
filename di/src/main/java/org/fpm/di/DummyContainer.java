package org.fpm.di;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.*;
import java.util.*;


public class DummyContainer implements Container {
    private final HashMap<Class<?>, Object> DependencyObjects;
    private final HashMap<Class<?>, Class<?>> DependencyClasses;

    public DummyContainer(HashMap<Class<?>, Object> DependencyObjects, HashMap<Class<?>, Class<?>> DependencyClasses) {
        this.DependencyObjects = DependencyObjects;
        this.DependencyClasses = DependencyClasses;
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        if (DependencyObjects.containsKey(clazz)) {
            if (DependencyObjects.get(clazz) == null) {
                if (clazz.getAnnotation(Singleton.class) != null) {
                    // Creating Singleton and saving it
                    createSafeSingleton(clazz);
                    return clazz.cast(DependencyObjects.get(clazz));
                }
                // Creating Prototype
                return clazz.cast(createObj(clazz));
            }
            return clazz.cast(DependencyObjects.get(clazz));
        }
        else if (DependencyClasses.containsKey(clazz)) {
            return clazz.cast(getComponent(DependencyClasses.get(clazz)));
        }
        // Logic when bind class-class (A, B) and B class is not here

        throw new RuntimeException("Class " + clazz.getName() + " is missing in container");
    }

    private synchronized <T> void createSafeSingleton(Class<T> clazz) {
        if (DependencyObjects.get(clazz) == null) {
            DependencyObjects.put(clazz, createObj(clazz));
        }
    }

    private <T> T createObj(Class<T> clazz) {
        try {
            // Getting all constructors with @Inject
            Constructor<?>[] Constructors = clazz.getConstructors();
            List<Constructor<?>> ConstructorsWithInjection = new ArrayList<>();
            for (Constructor<?> constructor : Constructors) {
                if (constructor.getAnnotation(Inject.class) != null) {
                    ConstructorsWithInjection.add(constructor);
                }
            }

            // Class doesn't have any @Inject constructor
            if (ConstructorsWithInjection.isEmpty()) {
                Constructor<T> constructor = clazz.getConstructor();
                return constructor.newInstance();
            }

            // Sorting Constructors by number of parameters
            ConstructorsWithInjection.sort(Comparator.comparingInt(Constructor::getParameterCount));
            Collections.reverse(ConstructorsWithInjection);

            // Checking what constructor we can create
            for (Constructor<?> constructor : ConstructorsWithInjection) {
                boolean possibleToCreate = true;
                Class<?>[] parameters = constructor.getParameterTypes();
                List<Object> objParameters = new ArrayList<>();

                for (Class<?> parameter : parameters) {
                    try {
                        objParameters.add(getComponent(parameter));
                    }
                    catch (RuntimeException ex) {
                        // Can't create object in container
                        possibleToCreate = false;
                        break;
                    }
                }

                if (possibleToCreate) {
                    Constructor<T> constructorToCreate = clazz.getConstructor(parameters);
                    return constructorToCreate.newInstance(objParameters.toArray());
                }
            }
            // Can't create any constructor
            throw new RuntimeException("Can't create object of class '" + clazz.getName() + "' as any public " +
                    "constructor don't have injection in container");

        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException("Not possible to create object of '" + clazz.getName() +
                                        "', all constructors are private");
        }
    }
}