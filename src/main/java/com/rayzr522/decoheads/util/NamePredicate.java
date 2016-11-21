package com.rayzr522.decoheads.util;

import java.util.function.Predicate;

import com.rayzr522.decoheads.Head;

/**
 * @author Rayzr
 *
 */
public class NamePredicate implements Predicate<Head> {

    private String filter;

    public NamePredicate(String filter) {
        this.filter = filter;
    }

    @Override
    public boolean test(Head h) {
        return h.getName().toLowerCase().contains(filter.toLowerCase());
    }

}
