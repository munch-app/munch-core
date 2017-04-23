package munch.catalyst.data;

import com.corpus.object.GroupField;
import com.corpus.object.GroupObject;
import com.corpus.object.ObjectUtils;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 10:02 PM
 * Project: munch-core
 */
public abstract class DataConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DataConsumer.class);

    private static final Set<String> actives = ImmutableSet.of("basic", "incomplete");
    private static final Set<String> inActives = ImmutableSet.of("deleted");

    /**
     * Consume group object if it belongs to you and pass it to your services
     *
     * @param list list of group object to consume
     */
    public abstract void consume(List<GroupObject> list);

    /**
     * @param list   list of group object to map
     * @param mapper mapper for object
     * @param <R>    Type
     * @return mapped active data only
     */
    protected static <R> List<R> mapActive(List<GroupObject> list, String type, Function<GroupObject, R> mapper) {
        return map(list, type, actives, mapper);
    }

    /**
     * @param list   list of group object to map
     * @param mapper mapper for object
     * @param <R>    Type
     * @return mapped inActive data only
     */
    protected static <R> List<R> mapInActive(List<GroupObject> list, String type, Function<GroupObject, R> mapper) {
        return map(list, type, inActives, mapper);
    }

    /**
     * List of group to map to place
     *
     * @param list list of group to map
     * @return mapped list of Place
     */
    protected static <R> List<R> map(List<GroupObject> list, String type, Set<String> status, Function<GroupObject, R> mapper) {
        return list.stream()
                .filter(group -> StringUtils.equals(getString(group, "Group.type"), type))
                .filter(group -> status.contains(group.getStatus()))
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * @param group group object
     * @param key   key of field
     * @return string value, return null if value not found
     */
    protected static String getString(GroupObject group, String key) {
        return ObjectUtils.getField(group, key).map(GroupField::getValue).orElse(null);
    }

    /**
     * @param group group object
     * @param key   key of field
     * @return double value, return null if value not found or parse-able
     */
    protected static Double getDouble(GroupObject group, String key) {
        String string = getString(group, key);
        if (string == null) return null;
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            logger.warn("Group: {} getDouble({}) NumberFormatException {} for group: {}", group.getGroupKey(), key, e, group);
            return null;
        }
    }
}
