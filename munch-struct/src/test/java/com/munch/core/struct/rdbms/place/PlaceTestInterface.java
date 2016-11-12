package com.munch.core.struct.rdbms.place;

import com.munch.core.essential.source.MunchSource;
import com.munch.core.struct.rdbms.EntityTestInterface;
import com.munch.core.struct.rdbms.place.log.PlaceLog;

import java.time.LocalTime;

/**
 * Created by: Fuxing
 * Date: 13/11/2016
 * Time: 12:59 AM
 * Project: munch-core
 */
public interface PlaceTestInterface extends EntityTestInterface {

    default PlaceHour createHour(Integer day, LocalTime open, LocalTime close) {
        PlaceHour hour = new PlaceHour();
        hour.setDay(day);
        hour.setOpen(open);
        hour.setClose(close);
        return hour;
    }

    default PlaceHour createHour(Integer day, String open, String close) {
        return createHour(day, LocalTime.parse(open), LocalTime.parse(close));
    }

    default PlaceLog createLog(Integer through, Integer how, String by) {
        PlaceLog log = new PlaceLog();
        log.setAddedThrough(through);
        log.setAddedHow(how);
        log.setAddedBy(by);
        return log;
    }

    default PlaceLog createLogDefault() {
        return createLog(MunchSource.MUNCH_STAFF, PlaceLog.THROUGH_CORPUS_BLOG, "xander");
    }
}

