package com.munch.accounts.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by: Fuxing
 * Date: 9/2/2016
 * Time: 6:48 PM
 * Project: vital-core
 */
public class PasswordUtil {

    private static final int SALT_COMPLEXITY = 10;

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(SALT_COMPLEXITY));
    }

    public boolean check(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
