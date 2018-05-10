package me.rayzr522.decoheads.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * @author Rayzr
 */
class DateCodeFormat extends DateFormat {
    private static final long serialVersionUID = 7184172355822547096L;

    @SuppressWarnings("deprecation")
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return toAppendTo
                .append(date.getHours())
                .append(date.getMinutes())
                .append(date.getSeconds()).append("_")
                .append(date.getDate())
                .append(date.getMonth())
                .append(String.valueOf(date.getYear() + 1900).substring(2));
    }

    /*
     * (non-Javadoc)
     *
     * @see java.text.DateFormat#parse(java.lang.String,
     * java.text.ParsePosition)
     */
    @Override
    public Date parse(String source, ParsePosition pos) {
        return null;
    }
}
