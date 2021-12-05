package org.fpm.di.example;

import org.fpm.di.Container;
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
        return null;
    }
}
