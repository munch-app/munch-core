package com.munch.accounts.objects;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created By: Fuxing Loh
 * Date: 5/2/2017
 * Time: 11:50 AM
 * Project: munch-core
 */
public final class AuthProvider {

    public enum Type {
        EmailPassword,
        Facebook,
        Instagram
    }


    private String id;
    private Type type;


    // TODO json?

    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
