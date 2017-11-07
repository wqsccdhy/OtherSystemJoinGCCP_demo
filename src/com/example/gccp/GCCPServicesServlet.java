package com.example.gccp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seeyon.v3x.plugin.gccp.common.ContextManager;
import com.seeyon.v3x.plugin.gccp.services.ServicesManager;

/**
 * <p>Title: GCCP服务</p>
 * <p>Description: GCCP服务总入口</p>
 * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
 * <p>Company: 北京致远协创软件有限公司</p>
 * @author yangc
 * 2014-8-21下午7:40:27 
 */
public class GCCPServicesServlet extends HttpServlet {

    /** */

    private static final long serialVersionUID = -5764075117095335776L;
    private ServicesManager   servicesManager  = new ServicesManager();

    @Override
    public void init() throws ServletException {
        try {
            Content content = Content.getInstance();
            ContextManager.getInstance().setContext(content);
            //注册服务
            servicesManager.registService(content.getExchangeService());//数据交换服务
            servicesManager.registService(content.getHeartbeatService());//心跳服务
        } catch (Exception e) {
            throw new ServletException(e);
        }
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	//TODO打印日志
        servicesManager.service(req, resp);
    }

}
