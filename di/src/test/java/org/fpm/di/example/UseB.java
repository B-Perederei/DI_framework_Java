package org.fpm.di.example;

import javax.inject.Inject;

public class UseB {
    private final B dependency;

    @Inject
    private UseB(B b) {
        this.dependency = b;
    }

    public B getDependency() {
        return dependency;
    }
}
