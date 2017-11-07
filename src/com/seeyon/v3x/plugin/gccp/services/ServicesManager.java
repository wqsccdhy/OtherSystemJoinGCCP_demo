package com.seeyon.v3x.plugin.gccp.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.seeyon.v3x.plugin.gccp.common.GCCPEnum;
import com.seeyon.v3x.plugin.gccp.exception.GCCPException;


  	/**
      * <p>Title: 服务模块</p>
      * <p>Description: 当本系统充当服务端时通过注册服务来对外提供</p>
      * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
      * <p>Company: 北京致远协创软件有限公司</p>
      * @author yangc 
      * 2014-7-30上午10:41:00 
      */
  
public class ServicesManager {
	
	private static final Log log = LogFactory.getLog(ServicesManager.class);
	
	private Map<GCCPEnum.ServiceEnum,IGCCPService> serviceMap = new HashMap<GCCPEnum.ServiceEnum, IGCCPService>();

    /** 
     * @description 注册服务到本分发类中
     * @param service
     * @throws GCCPException
     * @author 
     */
    public synchronized void registService(IGCCPService service) throws GCCPException{
        if(service != null){
        	List<GCCPEnum.ServiceEnum> es = service.defintions();
        	for(GCCPEnum.ServiceEnum e:es){
        		serviceMap.put(e, service);
        	}
        }
    }
    
    /** 
     * @description 服务分发类
     * @param arg0
     * @param arg1
     * @throws IOException
     * @author 
     */
    public void service(HttpServletRequest arg0, HttpServletResponse arg1) throws IOException{
    	GCCPEnum.ServiceEnum e = null;
    	try{
    		e = getServiceDefintion(arg0);
    		//TODO打印日志
    		if(e == null){
    			//TODO打印日志
    			log.error("服务未找到:uri="+arg0.getRequestURI());
    			arg1.sendError(HttpServletResponse.SC_NOT_FOUND);
    		}else{
    			IGCCPService s = this.serviceMap.get(e);
    			GCCPServiceRequest request = new GCCPServiceRequest(e, arg0);
    			GCCPServiceResponse response = new GCCPServiceResponse(arg1);
    			s.service(e, request, response);
    			response.flush();
    		}
    	}catch (Exception ex) {
    		//TODO打印日志
    		log.error("服务发生错误:service="+e, ex);
    		arg1.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
    }
    
    /** 
     * @description 根据请求获取注册的服务
     * @param arg0
     * @return
     * @author 
     */
    private GCCPEnum.ServiceEnum getServiceDefintion(HttpServletRequest arg0){
		for(GCCPEnum.ServiceEnum e:this.serviceMap.keySet()){
			if(e.isThisRequest(arg0)){
				return e;
			}
		}
		return null;
	}
    
}
