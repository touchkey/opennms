/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.syslogd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.statsd.RelativeTime;


public class SyslogParser {
    private static Pattern m_pattern = Pattern.compile("^.*$");
    private Matcher m_matcher = null;
    private final String m_text;
    private Boolean m_found = null;
    private Boolean m_matched = null;
    private boolean m_traceEnabled = false;

    protected SyslogParser(final String text) {
        m_text = text;
        m_traceEnabled = LogUtils.isTraceEnabled(this);
    }

    public boolean find() {
        if (m_found == null) {
            getMatcher().reset();
            m_found = getMatcher().find();
        }
        return m_found;
    }

    public boolean matches() {
        if (m_matched == null) {
            getMatcher().reset();
            m_matched = getMatcher().matches();
        }
        return m_matched;
    }

    protected Boolean matched() {
        return m_matched;
    }

    protected String getText() {
        return m_text;
    }

    protected boolean traceEnabled() {
        return m_traceEnabled;
    }

    /* override this to return your own class */
    public static SyslogParser getParser(final String text) throws SyslogParserException {
        return new SyslogParser(text);
    }

    /* override this to get your custom pattern */
    protected Pattern getPattern() {
        return m_pattern;
    }

    /* override this to parse data from the matcher */
    public SyslogMessage parse() throws SyslogParserException {
        final SyslogMessage message = new SyslogMessage();
        message.setMessage(getMatcher().group());
        return message;
    }

    protected Matcher getMatcher() {
        if (m_matcher == null) {
            m_matcher = getPattern().matcher(m_text);
        }
        return m_matcher;
    }

    @SuppressWarnings("deprecation")
    protected Date parseDate(final String dateString) {
        Date date;
        try {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = df.parse(dateString);
        } catch (final Exception e) {
            try {
                final DateFormat df = new SimpleDateFormat("MMM d HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                
                // Ugh, what's the non-lame way of forcing it to parse to "this year"?
                date = df.parse(dateString);
                date.setYear(RelativeTime.THISYEAR.getStart().getYear());
            } catch (final Exception e2) {
                LogUtils.debugf(this, e2, "Unable to parse date '%s'", dateString);
                date = null;
            }
        }
        return date;
    }

}
