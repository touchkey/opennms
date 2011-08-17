/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2011 The OpenNMS Group, Inc.
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

package org.opennms.web.controller.ksc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opennms.netmgt.config.KSC_PerformanceReportFactory;
import org.opennms.web.springframework.security.Authentication;
import org.opennms.web.svclayer.KscReportService;
import org.opennms.web.svclayer.ResourceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * <p>IndexController class.</p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class IndexController extends AbstractController implements InitializingBean {
    
    private ResourceService m_resourceService;
    private KscReportService m_kscReportService;

    /** {@inheritDoc} */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reloadConfig = request.getParameter("reloadConfig");
        if (reloadConfig != null && Boolean.parseBoolean(reloadConfig)) {
            KSC_PerformanceReportFactory.getInstance().reload();
        }

        ModelAndView modelAndView = new ModelAndView("KSC/index");

        modelAndView.addObject("kscReadOnly", ( (!request.isUserInRole( Authentication.ROLE_ADMIN )) || request.isUserInRole(Authentication.ROLE_READONLY)) || (request.getRemoteUser() == null));
        modelAndView.addObject("reports", getKscReportService().getReportList());
        modelAndView.addObject("nodeResources", getResourceService().findNodeResources());
        modelAndView.addObject("domainResources", getResourceService().findDomainResources());
        
        return modelAndView;
    }

    /**
     * <p>getResourceService</p>
     *
     * @return a {@link org.opennms.web.svclayer.ResourceService} object.
     */
    public ResourceService getResourceService() {
        return m_resourceService;
    }

    /**
     * <p>setResourceService</p>
     *
     * @param resourceService a {@link org.opennms.web.svclayer.ResourceService} object.
     */
    public void setResourceService(ResourceService resourceService) {
        m_resourceService = resourceService;
    }
    
    /**
     * <p>getKscReportService</p>
     *
     * @return a {@link org.opennms.web.svclayer.KscReportService} object.
     */
    public KscReportService getKscReportService() {
        return m_kscReportService;
    }

    /**
     * <p>setKscReportService</p>
     *
     * @param kscReportService a {@link org.opennms.web.svclayer.KscReportService} object.
     */
    public void setKscReportService(KscReportService kscReportService) {
        m_kscReportService = kscReportService;
    }

    /**
     * <p>afterPropertiesSet</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void afterPropertiesSet() throws Exception {
        Assert.state(m_resourceService != null, "property resourceService must be set");
        Assert.state(m_kscReportService != null, "property kscReportService must be set");
    }
}
