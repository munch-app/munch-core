/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util;

import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Fuxing
 * Date: 27/1/2015
 * Time: 11:15 PM
 * Project: PuffinCore
 */
public class DateTimeTest {
    @Test
    public void timeFunctions() {
        assertThat(DateTime.instanceNow()).isNotNull();
        assertThat(DateTime.localNow()).isNotNull();

        assertThat(DateTime.millisNow()).isGreaterThan(0);
        assertThat(DateTime.now()).isNotNull();

        assertThat(DateTime.timeNow()).isNotNull();
        assertThat(DateTime.zonedNow()).isNotNull();

        assertThat(DateTime.toSimple(DateTime.now())).isNotNull();
    }

    @Test
    public void sgTime() {
        assertThat(DateTime.Sg.toSimple(DateTime.now())).isNotNull();
    }

    @Test
    public void timeConversion() {
        System.out.println(DateTime.now().toInstant());
        System.out.println(DateTime.localNow());
        System.out.println(System.currentTimeMillis());
        System.out.println(DateTime.Sg.toSimple(DateTime.now()));
    }
}
