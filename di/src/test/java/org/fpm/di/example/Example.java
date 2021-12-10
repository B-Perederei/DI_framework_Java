package org.fpm.di.example;

import org.fpm.di.Container;
import org.fpm.di.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Example {

    private Container container;

    @Before
    public void setUp() {
        Environment env = new DummyEnvironment();
        // Container = new DummyContainer() build on MyConfiguration
        container = env.configure(new MyConfiguration());
    }

    @Test
    public void shouldInjectSingleton() {
        assertSame(container.getComponent(MySingleton.class), container.getComponent(MySingleton.class));
    }

    @Test
    public void shouldInjectPrototype() {
        assertNotSame(container.getComponent(MyPrototype.class), container.getComponent(MyPrototype.class));
    }

    @Test
    public void shouldBuildInjectionGraph() {
        /*
        binder.bind(A.class, B.class);
        binder.bind(B.class, new B());
        */
        final B bAsSingleton = container.getComponent(B.class);
        assertSame(container.getComponent(A.class), bAsSingleton);
        assertSame(container.getComponent(B.class), bAsSingleton);
    }

    @Test
    public void shouldBuildInjectDependencies() {
        final UseA hasADependency = container.getComponent(UseA.class);
        assertSame(hasADependency.getDependency(), container.getComponent(B.class));
    }

    @Test
    public void shouldThrowExceptionPrivateConstructors() {
        try {
            container.getComponent(UseB.class);
            fail("Should throw Runtime Exception");
        }
        catch (RuntimeException ex) {
            String message = ex.getMessage();
            if(!message.contains("constructors are private")) {
                fail("Incorrect output of exception: should print 'constructors are private', but printed: " +
                                ex.getMessage());
            }
        }
    }

    @Test
    public void shouldThrowExceptionNotInContainer() {
        try {
            container.getComponent(C.class);
            fail("Should throw Runtime Exception");
        }
        catch (RuntimeException ex) {
            String message = ex.getMessage();
            if(!message.contains("is missing in container")) {
                fail("Incorrect output of exception: should print 'is missing in container', but printed: " +
                        ex.getMessage());
            }
        }
    }

    @Test
    public void shouldThrowExceptionConstructorsWithoutInjection() {
        try {
            container.getComponent(UseC.class);
            fail("Should throw Runtime Exception");
        }
        catch (RuntimeException ex) {
            String message = ex.getMessage();
            if(!message.contains("constructor don't have injection in container")) {
                fail("Incorrect output of exception: should print 'constructor don't have injection in container', " +
                        "but printed: " + ex.getMessage());
            }
        }
    }
}
