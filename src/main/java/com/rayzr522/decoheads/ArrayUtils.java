
package com.rayzr522.decoheads;

public class ArrayUtils {

    public static String concat(Object[] arr, String filler) {

        if (arr == null || arr.length < 1) {
            return "";
        }

        filler = filler == null ? "" : filler;

        String output = arr[0].toString();

        for (int i = 1; i < arr.length; i++) {

            output += filler + arr[i].toString();

        }

        return output;

    }

}
