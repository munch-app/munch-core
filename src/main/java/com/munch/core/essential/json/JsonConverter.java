package com.munch.core.essential.json;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 12:54 AM
 * Project: struct
 */
public interface JsonConverter {

    String toJson(Object object);

    <T> T fromJson(Class<T> clazz, String content);
}
