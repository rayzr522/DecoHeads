/**
 *
 */
package me.rayzr522.decoheads.command;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Rayzr
 */
class MatchComparator implements Comparator<String>, Serializable {
    private static final long serialVersionUID = 3701492487376136251L;

    private String filter;

    MatchComparator(String filter) {
        this.filter = filter;
    }

    public int compare(String o1, String o2) {
        System.out.println(String.format("Distance from '%s' to '%s': %d", filter, o1, StringUtils.getLevenshteinDistance(filter, o1)));
        System.out.println(String.format("Distance from '%s' to '%s': %d", filter, o2, StringUtils.getLevenshteinDistance(filter, o2)));

        return Integer.compare(
                StringUtils.getLevenshteinDistance(filter, o1),
                StringUtils.getLevenshteinDistance(filter, o2)
        );
    }
}
