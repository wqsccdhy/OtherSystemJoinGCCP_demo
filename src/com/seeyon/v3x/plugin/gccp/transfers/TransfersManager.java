package com.seeyon.v3x.plugin.gccp.transfers;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.seeyon.v3x.common.constants.SystemProperties;
import com.seeyon.v3x.plugin.gccp.common.GCCPCodeEnum;
import com.seeyon.v3x.plugin.gccp.common.ILifeCycle;
import com.seeyon.v3x.plugin.gccp.exception.GCCPException;

  

/**
  * <p>Title: 基础通讯模块(客户端模块)</p>
  * <p>Description: 当本系统充当客户端时，使用本模块来发起服务访问</p>
  * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
  * <p>Company: 北京致远协创软件有限公司</p>
  * @author yangc 
  * 2014-7-29下午5:11:55 
  */
  
public class TransfersManager implements ILifeCycle {
	
	private static final Logger log     = Logger.getLogger(TransfersManager.class);
	
	public static final String BEAN_NAME = "transfersManager";
	
	private static final String MAX_CONN = "gccp.http.maxConn";
	
	private HttpClient httpClient = null;
	
	/** 
     * 基于HTTP网络发包
     * @description 发送请求
     * @param request
     * @param handler
     * @return
     * @throws GCCPException
     * @author 
     */
	public <T> T sendPackage(GCCPRequest request,IGCCPResponseHandle<T> handler) throws GCCPException{
        return sendPackage(request, handler, -1);
	}
	
	
	
	public <T> T sendPackage(GCCPRequest request,IGCCPResponseHandle<T> handler , int socketTimeout) throws GCCPException{
		T result = null;
    	if(request != null){
    		GCCPResponse gres = null;
    		HttpUriRequest req = null;
    		HttpResponse res = null;
    		try{
    			req = request.getHttpRequest();
    			Builder builder = RequestConfig.custom();
    			// 2秒连接超时
    			builder.setConnectTimeout(2000);
                if (socketTimeout >= 0) {
                    builder.setSocketTimeout(socketTimeout);
                }
    			RequestConfig config = builder.build();
    			((HttpRequestBase)req).setConfig(config);
    			try{
    				res = getHttpClient().execute(req);
    			}finally{
    				if(req != null){
    					if(req instanceof HttpEntityEnclosingRequestBase){
    						EntityUtils.consume(((HttpEntityEnclosingRequestBase)req).getEntity());
    					}
    				}
    			}
    			gres = new GCCPResponse(request.getHost(),res);
    			int code = gres.getResponseCode();
    			log.info("code:" + code);
    			switch(code){
	    			case HttpServletResponse.SC_OK:
	    			case HttpServletResponse.SC_NOT_MODIFIED:
	        			if(handler != null){
	        				result =  handler.handle(gres);
	        			}
	    				break;
	    			case HttpServletResponse.SC_NOT_FOUND:
	    				throw new GCCPException(GCCPCodeEnum.BottomCodeEnum.ClientErrorRes404.getKey(),GCCPCodeEnum.BottomCodeEnum.ClientErrorRes404.getDes());
	    			case HttpServletResponse.SC_SERVICE_UNAVAILABLE:
	    				throw new GCCPException(GCCPCodeEnum.BottomCodeEnum.ClientErrorRes50X.getKey(),GCCPCodeEnum.BottomCodeEnum.ClientErrorRes50X.getDes());
    				default:
    					throw new GCCPException(GCCPCodeEnum.BottomCodeEnum.ClientErrorResOther.getKey(),GCCPCodeEnum.BottomCodeEnum.ClientErrorResOther.getDes()+":"+code);
    			}
    		}catch (ClientProtocolException e) {
    			log.info("ClientProtocolException:" + e);
    			throw new GCCPException(GCCPCodeEnum.BottomCodeEnum.ClientErrorProtocol.getKey(),e);
			}catch (IOException e) {
				log.info("IOException:" + e);
				throw new GCCPException(GCCPCodeEnum.BottomCodeEnum.ClientErrorNet.getKey(),e);
			}catch (Exception e) {
				log.info("Exception:" + e);
				throw new GCCPException(e);
			}finally{
				try{
					if(res != null){
						EntityUtils.consume(res.getEntity());
						if(res instanceof Closeable){
							((Closeable)res).close();
						}
					}
				}catch (IOException e) {
					log.info("IOException:" + e);
					throw new GCCPException(GCCPCodeEnum.BottomCodeEnum.ClientErrorNet.getKey(),e);
				}
			}
    	}
    	return result;
	}
    	
    
    public HttpClient getHttpClient()
    {
    	if (null == httpClient){
			synchronized (this) {
				if(httpClient == null){
					HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
						public boolean retryRequest(IOException exception, int executionCount,HttpContext context) {
							if (executionCount >= 3) {// 如果超过最大重试次数，那么就不要继续了 
								return false;
							}
							if (exception instanceof NoHttpResponseException) { // 如果服务器丢掉了连接，那么就重试 
								return true;
							}
							return false; 
						};
					};
					HttpClientBuilder builder = HttpClients.custom();
					builder.setRetryHandler(retryHandler);
					int maxConn = SystemProperties.getInstance().getIntegerProperty(MAX_CONN, 100);
					builder.setMaxConnTotal(maxConn);
					builder.setMaxConnPerRoute(maxConn);
					builder.setDefaultConnectionConfig(ConnectionConfig.custom().build());
					builder.setDefaultSocketConfig(SocketConfig.custom().build());
					httpClient = builder.build();
				}
			}
    	}
 	return httpClient;
	}

	@Override
	public void initializ(Map<?, ?> context) throws Exception{
		//getHttpClient();
	}

	@Override
	public void destroy(Map<?, ?> context) throws Exception{
		if(this.httpClient != null){
			((CloseableHttpClient)this.httpClient).close();
		}
	}
}
