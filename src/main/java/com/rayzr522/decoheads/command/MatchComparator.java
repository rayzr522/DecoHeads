/**
 * 
 */
package com.rayzr522.decoheads.command;

import java.util.Comparator;

/**
 * @author Rayzr
 *
 */
public class MatchComparator implements Comparator<String> {
    private String filter;

    public MatchComparator(String filter) {
        this.filter = filter;
    }

    public int compare(String o1, String o2) {
        boolean s1 = o1.startsWith(filter);
        boolean s2 = o2.startsWith(filter);
        if (s1 && s2) {
            return Integer.compare(o1.replace(filter, "").length(), o2.replace(filter, "").length());
        }
        return Boolean.compare(s2, s1);
    }
}
