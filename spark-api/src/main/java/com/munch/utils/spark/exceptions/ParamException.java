package com.munch.utils.spark.exceptions;

/**
 * Created by: Fuxing
 * Date: 10/12/2016
 * Time: 11:16 AM
 * Project: corpus-catalyst
 */
public class ParamException extends Exception {

    public ParamException(String name, String message) {
        super(name + " is required but not valid. reason: " + message);
    }
}
