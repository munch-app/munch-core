package com.munch.accounts.spark;

/**
 * Created by: Fuxing
 * Date: 10/12/2016
 * Time: 11:16 AM
 * Project: corpus-catalyst
 */
public class ParamException extends Exception {

    ParamException(String name, String message) {
        super(name + " is required but not valid. reason: " + message);
    }
}
