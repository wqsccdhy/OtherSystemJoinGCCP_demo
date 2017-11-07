package com.example.gccp.exchange;

import java.io.IOException;

import com.seeyon.v3x.plugin.gccp.exchange.esp.GCCPExchangeServiceResponse;
import com.seeyon.v3x.plugin.gccp.transfers.GCCPResponse;
import com.seeyon.v3x.plugin.gccp.transfers.IGCCPResponseHandle;

/**
 * <p>Title: 数据交换</p>
 * <p>Description: 对交换响应对象进行简单处理，返回成功(true)或者失败(false)</p>
 * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
 * <p>Company: 北京致远协创软件有限公司</p>
 * 2016-6-27上午10:37:01
 */
public class SimpleResponseHandle implements IGCCPResponseHandle<Boolean> {

    @Override
    public Boolean handle(GCCPResponse response) throws IOException, Exception {
        GCCPExchangeServiceResponse exchangeServiceResponse = new GCCPExchangeServiceResponse();
        exchangeServiceResponse.loadInputStream(response.getResponseStream().getInputStream());
        return "200".equals(exchangeServiceResponse.getServiceCode());
    }

}
