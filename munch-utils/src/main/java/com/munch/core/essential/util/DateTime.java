/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * All date time are default to utc
 * Created by Fuxing
 * Date: 21/1/2015
 * Time: 12:51 AM
 * Project: PuffinCore
 */
public final class DateTime {
    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";

    private DateTime() {/* NOT Suppose to init */}

    /**
     * @return Current millis
     */
    public static long millisNow() {
        return System.currentTimeMillis();
    }

    /**
     * @return Current date
     */
    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    public static Instant instanceNow() {
        return zonedNow().toInstant();
    }

    public static ZonedDateTime zonedNow() {
        return ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public static LocalDateTime localNow() {
        return zonedNow().toLocalDateTime();
    }

    public static LocalTime timeNow() {
        return zonedNow().toLocalTime();
    }

    public static ZonedDateTime toZonedDateTime(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
    }

    public static String toSimple(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static String toTime(Date date) {
        return new SimpleDateFormat(TIME_FORMAT).format(date);
    }

    public final static class Sg {
        private Sg() {/* NOT Suppose to init */}

        public static String toSimple(Date date) {
            return toZonedDateTime(date).format(DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH).withZone(ZoneId.of("GMT+8")));
        }

        public static String toTime(Date date) {
            return toZonedDateTime(date).format(DateTimeFormatter.ofPattern(TIME_FORMAT, Locale.ENGLISH).withZone(ZoneId.of("GMT+8")));
        }
    }
}
