package com.example.gccp;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.gccp.exchange.EdocDataOperation;
import com.example.gccp.exchange.EdocReceiptDataOperation;
import com.seeyon.v3x.plugin.gccp.common.IContext;
import com.seeyon.v3x.plugin.gccp.exchange.ExchangeManager;
import com.seeyon.v3x.plugin.gccp.exchange.ExchangeRequestService;
import com.seeyon.v3x.plugin.gccp.register.domain.RegistConfig;
import com.seeyon.v3x.plugin.gccp.services.ExchangeService;
import com.seeyon.v3x.plugin.gccp.services.HeartbeatService;

public class Content implements IContext {

    /**
     * 本系统注册ID，自行设置，可以随机生成，但注册后就不能更改该值。
     */
	private Long                 systemId         = 9912345678906L;
    /**
     * 公文交换平台基础信息配置对象
     */
    private RegistConfig        serverSystem     = new RegistConfig();
    /**
     * 本系统基础信息配置对象
     */
    private RegistConfig        selfSystem       = new RegistConfig();

    /**
     * 交换服务实体类，不需要修改
     */
    private ExchangeService     exchangeService  = new ExchangeService();
    /**
     * 心跳检测服务实体类，不需要修改
     */
    private HeartbeatService    heartbeatService = new HeartbeatService();
    /**
     * 交换服务管理类，不需要修改
     */
    private ExchangeManager     exchangeManager  = new ExchangeManager();
    /**
     * 单例实体
     */
    private static Content      instance         = new Content();
    /**
     * 参数集合
     */
    private Map<String, String> contextmap       = new ConcurrentHashMap<String, String>();

    /**
     * 获取实例对象
     * @return 实例对象
     */
    public static Content getInstance() {
        return instance;
    }

    private Content() {
        //TODO 按照自己的具体情况实现下面参数内容 异构系统端消息
        contextmap.put(IP, "10.5.2.252");
        contextmap.put(PORT, "8080");
        contextmap.put(APPLICATION_NAME, "OtherSystemJoinGCCP_demo");//一定要写工程或者服务的名称
        contextmap.put(SYSTEM_ID, systemId + "");

        //设置交换平台对象，设置地址和IP,客户生产环境对应值由我方提供 gccp信息
        serverSystem.setIp("59.215.200.39");
        serverSystem.setPort(80);
        serverSystem.setAppName("seeyon");
        Date currentTime = new Date();
        //设置当前系统的信息
        selfSystem.setIp(getContextParameter(IP));
        selfSystem.setPort(Integer.valueOf(getContextParameter(PORT)));
        selfSystem.setAppName(getContextParameter(APPLICATION_NAME));
        selfSystem.setRegisterCode(systemId);
        selfSystem.setConfigType(1);
        selfSystem.setOrgName("测试一级单位_wq_cd");//系统名称
        selfSystem.setShortName("测试一级单位_wq_cd");//系统简称
        selfSystem.setRegisterDate(currentTime);
        selfSystem.setModifyDate(currentTime);
        selfSystem.setPassword("123456");//服务密码，自行设置
        selfSystem.setDescription("测试一级单位_wq_cd");
        selfSystem.setSystemVersion("Test V1.0_cd");

        //注册交换服务具体操作
        exchangeManager.registExchangeOperation(new EdocDataOperation());
        exchangeManager.registExchangeOperation(new EdocReceiptDataOperation());
        //实例化交换管理对象，这部分代码不用调整
        ExchangeRequestService exchangeRequestService = new ExchangeRequestService();
        exchangeRequestService.setExchangeManager(exchangeManager);
        exchangeManager.setExchangeService(exchangeRequestService);
        exchangeService.setExchangeManager(exchangeManager);

    }

    public RegistConfig getServerSystem() {
        return serverSystem;
    }

    public RegistConfig getSelfSystem() {
        return selfSystem;
    }

    public ExchangeService getExchangeService() {
        return exchangeService;
    }

    public HeartbeatService getHeartbeatService() {
        return heartbeatService;
    }

    public ExchangeManager getExchangeManager() {
        return exchangeManager;
    }

    @Override
    public String getContextParameter(String name) {
        return contextmap.get(name);
    }

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
    
    

}
