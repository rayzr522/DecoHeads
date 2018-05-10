package me.rayzr522.decoheads.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtils {

    public static String concat(Object[] arr, String filler) {
        return Arrays.stream(arr).map(Object::toString).collect(Collectors.joining(filler));
    }

    public static List<String> colorize(List<String> list) {
        if (list == null || list.size() < 1) {
            return list;
        }
        return list.stream().map(str -> "&r" + str).map(TextUtils::colorize).collect(Collectors.toList());
    }

}
