package com.seeyon.v3x.plugin.gccp.services;

import java.util.ArrayList;
import java.util.List;

import com.seeyon.v3x.plugin.gccp.common.ContextManager;
import com.seeyon.v3x.plugin.gccp.common.GCCPEnum;
import com.seeyon.v3x.plugin.gccp.common.IContext;
import com.seeyon.v3x.plugin.gccp.exception.GCCPException;

public class HeartbeatService implements IGCCPService {
	public static final String PARAM_ACTION = "action";
	public static final String PARAM_ACTION_ONLINE = "online";
	public static final String PARAM_ACTION_OFFLINE = "offline";
	public static final String PARAM_SYSTEMID = "systemId";
	public static final String PARAM_SYSTEMSTATE = "systemState";
	private ContextManager contextManager = ContextManager.getInstance();

	public List<GCCPEnum.ServiceEnum> defintions() {
		List result = new ArrayList();
		result.add(GCCPEnum.ServiceEnum.Server_Heartbeat);
		return result;
	}

	public void service(GCCPEnum.ServiceEnum service,
			GCCPServiceRequest request, GCCPServiceResponse response)
			throws GCCPException {
		IContext context = this.contextManager.getContext();
		Long systemIdLong = null;
		try {
			String systemId = context.getContextParameter("systemId");
			systemIdLong = Long.valueOf(systemId);
		} catch (Exception e) {
			System.out.println("请在ContextManager中设置系统id!");
		}
		String action = request.getParameter("action");
		if ("online".equals(action))
			response.setResponseObject("welcome");
		else if ("offline".equals(action))
			response.setResponseObject("bye");
		else
			response.setResponseObject("who are you?");
	}
}