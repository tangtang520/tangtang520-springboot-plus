package com.tfy.framework.common.utils;

import com.tfy.framework.common.exception.GException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ParamsUtils {

    public static <T> void checkNotEmpty(T t, String message) {
        if (t == null) {
            throw new GException(message);
        }
        if (t instanceof String && StringUtils.isEmpty((String) t)) {
            throw new GException(message);
        }
    }

    public static <T> void checkNotEmpty(Collection<T> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new GException(message);
        }
    }

    public static <S, T> void checkNotEmpty(Map<S, T> map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new GException(message);
        }
    }

    public static <T> boolean isEmpty(T t) {
        if (null == t) {
            return true;
        }
        if (t instanceof String && StringUtils.isEmpty((String) t)) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyStrIncludeNull(String str) {
        return StringUtils.isEmpty(str) || StringUtils.equals("null", str);
    }

    public static boolean isNotEmptyStrIncludeNull(String str) {
        return !isEmptyStrIncludeNull(str);
    }


    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }


    public static <T> boolean isEmpty(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static <S, T> boolean isEmpty(Map<S, T> map) {
        return MapUtils.isEmpty(map);
    }

    public static <S, T> boolean isNotEmpty(Map<S, T> map) {
        return !isEmpty(map);
    }

    /**
     * @param s a string like "1,2,3"
     * @param separator separator (allow regex)
     * @return separated integer list
     */
    public static List<Integer> separateStringToIntegerList(String s, String separator) {
        if (StringUtils.isEmpty(s)) {
            return new ArrayList<>();
        }
        return Arrays.stream(s.split(separator)).map(Integer::parseInt).collect(Collectors.toList());
    }

    /**
     * Do the opposite of the previous method.
     */
    public static <T extends Collection<Integer>> String joinIntegerCollectionToString(T collection, String joiner) {
        if (collection == null || collection.size() == 0) {
            return "";
        }
        return collection.stream().filter(Objects::nonNull).map(Objects::toString).collect(Collectors.joining(joiner));
    }

}
