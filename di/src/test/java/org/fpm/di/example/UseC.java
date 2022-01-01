package org.fpm.di.example;

import javax.inject.Inject;

public class UseC {
    private final C dependency;

    @Inject
    private UseC(C c) {
        this.dependency = c;
    }

    @Inject
    public UseC(B b, C c) {this.dependency = c;}
    public C getDependency() {
        return dependency;
    }
}
