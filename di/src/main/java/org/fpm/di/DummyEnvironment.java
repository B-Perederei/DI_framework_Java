package org.fpm.di;

public class DummyEnvironment implements Environment {

    @Override
    public Container configure(Configuration configuration) {
        DummyBinder NewBinder = new DummyBinder();
        configuration.configure(NewBinder);
        return new DummyContainer(NewBinder.getDependencyObjects(), NewBinder.getDependencyClasses());
    }
}
