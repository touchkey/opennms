/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.config.mailtransporttest;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.core.xml.ValidateUsing;
import org.opennms.netmgt.config.utils.ConfigUtils;

/**
 * Configure user based authentication.
 */

@XmlRootElement(name="user-auth")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("mail-transport-test.xsd")
public class UserAuth implements Serializable {
    private static final long serialVersionUID = 2L;

    @XmlAttribute(name="user-name")
    private String m_userName;

    @XmlAttribute(name="password")
    private String m_password;

    public UserAuth() {
    }


    public UserAuth(final String username, final String password) {
        setUserName(username);
        setPassword(password);
    }


    @Override()
    public boolean equals(final Object obj) {
        if ( this == obj ) return true;

        if (obj instanceof UserAuth) {
            final UserAuth that = (UserAuth)obj;
            return Objects.equals(this.m_userName, that.m_userName) &&
                    Objects.equals(this.m_password, that.m_password);
        }
        return false;
    }

    public String getPassword() {
        return m_password == null? "opennms" : m_password;
    }

    public String getUserName() {
        return m_userName == null? "opennms" : m_userName;
    }

    public int hashCode() {
        return Objects.hash(m_userName, m_password);
    }

    public void setPassword(final String password) {
        m_password = ConfigUtils.normalizeString(password);
    }

    public void setUserName(final String userName) {
        m_userName = ConfigUtils.normalizeString(userName);
    }

}
