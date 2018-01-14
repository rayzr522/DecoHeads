package me.rayzr522.decoheads.util;

import me.rayzr522.decoheads.data.Head;

import java.util.function.Predicate;

/**
 * @author Rayzr
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
