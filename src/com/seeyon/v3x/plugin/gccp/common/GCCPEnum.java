package com.seeyon.v3x.plugin.gccp.common;

import com.chantsoft.core.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Arrays;


/**
 * <p>Title: 应用模块名称</p>
 * <p>Description: 代码描述</p>
 * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
 * <p>Company: 北京致远协创软件有限公司</p>
 *
 * @author yangc
 *         2014-7-29下午5:50:18
 */

public final class GCCPEnum {

    private GCCPEnum() {

    }

    /**
     * <p>Title: 通用的枚举</p>
     * <p>Description: 网络连接等其他</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-7-29下午7:22:06
     */

    public static enum CommonEnum {

        HttpContentType_Form(1, "application/x-www-form-urlencoded; charset=utf-8");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private CommonEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static CommonEnum getEnumByKey(int key) {
            CommonEnum[] es = CommonEnum.values();
            for (CommonEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }

    /**
     * <p>Title: 通用的枚举</p>
     * <p>Description: 网络连接等其他</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-7-29下午7:22:06
     */

    public static enum ServiceEnum {

        Server_Regist(10001, "POST:/regist", "服务器系统注册服务", false),
        Server_OrgSyn(10002, "POST:/org/syn", "服务器组织机构同步服务", true),
        Server_OrgAccess(10003, "GET:/org/access", "服务器某组织的访问服务", true),
        Server_ReceiveExchangeData(10004, "POST:/receiveData", "服务器接收交换数据", true),
        Server_GetShareInfoModule(10005,"POST:/getShareInfoModule","服务器的共享版块信息",true),
        Server_SaveShareNewsData(10006,"POST:/saveShareNewsData","服务器保存共享新闻数据到版块",true),
        Server_SaveShareBulData(10007,"POST:/saveShareBulData","服务器保存共享公告数据到版块",true),
        Server_Heartbeat(10008, "POST:/heartbeat", "系统间的心跳服务", true),
        //贵州项目组  新增枚举 客服端地址查询服务 2015年5月29日16:02:39 zyg
        Server_QueryClientAddress(10009,"POST:/QueryClientAddInfoService", "通过注册码查询客服端地址",true),
        Client_Xxxx(20001, "GET:/org/{0}", "客户端XX服务", true),
        Client_UpdRegistInfoState(20004, "POST:/regist/updateState", "客服端提供的更新注册对象服务", true),
        Client_DelRegistInfo(20006, "POST:/regist/delRegist", "客服端提供的删除注册对象服务", true),
        Client_ReceiveExchangeData(20007, "POST:/receiveData", "客服端接收交换数据", true),
        Client_UpdateOrgData(20008, "GET:/org/updateData", "客服端组织机构同步", true),
        Client_SendAffair(20009, "POST:/col/sendAffair", "客户端收取待办事项", true),
        Client_SendMsg(20010,"POST:/common/sendMsg","客户端接收信息",true),
        Client_ListCol(20011,"GET:/col/listCol","客户端获取协同数据",true),
        Client_GetSyn(20013,"POST:/org/getsyn","客户端对组织迁移成功后返回值的处理",true),
        Client_Verification(20012,"POST:/verification/anything","服务器验证数据",true),
        Client_GETEDOCINFO(20014,"POST:/edoc/info","服务器获取客户端公文数据",true),
        Client_SyncOrg(20015,"POST:/org/sync","客户端组织机构更新同步",true);
        public static final String SERVICE_ROOT_PATH = "gccp";

        private int key;

        private String value;

        /**
         * 是否做身份检测
         */

        private boolean check;

        private String valueMethod;

        private String valueURI;

        private String valueParam;

        private String des;

        private ServiceEnum(int k, String v, String s, boolean check) {
            this.key = k;
            this.value = v;
            this.des = s;
            this.check = check;
            this.valueMethod = this.value.split(":")[0];
            String part = this.value.split(":")[1];
            String[] parts = part.split("\\?");
            this.valueURI = parts[0];
            this.valueParam = parts.length > 1 ? parts[1] : "";
        }

        public static ServiceEnum getEnumByKey(int key) {
            ServiceEnum[] es = ServiceEnum.values();
            for (ServiceEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }


        public String getValue() {
            return value;
        }

        public boolean isCheck() {
            return check;
        }

        public String toURI(String... s) {
            return MessageFormat.format(this.value, s);
        }

        /**
         * 根据实际请求判断该请求是不是符合本枚举定义的请求
         *
         * @param request
         * @return
         */
        public boolean isThisRequest(HttpServletRequest request) {
            boolean r = false;
            if (request.getMethod().toLowerCase().equals(this.valueMethod.toLowerCase())) {
                String uri = request.getRequestURI();
                String[] paths = uri.split("\\/");
                String[] spaths = this.valueURI.split("\\/");
                int rootIndex = Arrays.asList(paths).indexOf(SERVICE_ROOT_PATH);
                if (rootIndex >= 0) {
                    paths = Arrays.copyOfRange(paths, rootIndex + 1, paths.length);
                    spaths = StringUtil.isEmpty(spaths[0]) ? Arrays.copyOfRange(spaths, 1, spaths.length) : spaths;
                    if (paths.length == spaths.length) {
                        for (int nowi = 0; nowi < spaths.length; nowi++) {
                            if (spaths[nowi].indexOf("{") >= 0) {
                                spaths[nowi] = paths[nowi];
                            }
                        }
                        r = Arrays.equals(spaths, paths);
                        if (r && StringUtil.isNotEmpty(this.valueParam)) {
                            String[] sparams = this.valueParam.split("&");
                            for (String sp : sparams) {
                                if (!StringUtil.isEmpty(sp) && sp.indexOf("=") >= 0) {
                                    String[] kv = sp.split("=");
                                    if (kv[1].indexOf("{") < 0) {
                                        r = kv[1].equals(request.getParameter(kv[0]));
                                    }
                                }
                                if (!r) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return r;
        }

        /**
         * 根据实际请求获取value中下标为index的对应位置的值
         * TODO 参数中的值还未处理
         *
         * @param request 请求(假设这个请求就是本枚举对应的请求)
         * @param index   下标号
         * @return 下标号在实际请求中对应的值
         */
        public String getFormatPartValue(HttpServletRequest request, int index) {
            String uri = request.getRequestURI();
            String[] paths = uri.split("\\/");
            String[] spaths = this.valueURI.split("\\/");
            int rootIndex = Arrays.asList(paths).indexOf(SERVICE_ROOT_PATH);
            int i = 0;
            if (rootIndex >= 0) {
                paths = Arrays.copyOfRange(paths, rootIndex + 1, paths.length);
                spaths = StringUtil.isEmpty(spaths[0]) ? Arrays.copyOfRange(spaths, 1, spaths.length) : spaths;
                for (int nowi = 0; nowi < spaths.length; nowi++) {
                    if (spaths[nowi].indexOf("{") >= 0) {
                        if (i == index) {
                            return paths[nowi];
                        }
                        i++;
                    }
                }
                if (StringUtil.isNotEmpty(this.valueParam)) {
                    String[] sparams = this.valueParam.split("&");
                    for (String sp : sparams) {
                        if (!StringUtil.isEmpty(sp) && sp.indexOf("=") >= 0) {
                            String[] kv = sp.split("=");
                            if (kv[1].indexOf("{") < 0) {
                                if (i == index) {
                                    return request.getParameter(kv[0]);
                                }
                                i++;
                            }
                        }
                    }
                }
            }
            return null;
        }

        public static ServiceEnum getEnumByValue(String v) {
            ServiceEnum[] es = ServiceEnum.values();
            for (ServiceEnum e : es) {
                if (e.getValue().equals(v)) {
                    return e;
                }
            }
            return null;
        }

    }

    /**
     * <p>Title: 注册需要的枚举</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-7-29下午5:56:11
     */

    public static enum RegistTypeEnum {

        Self(0, "本系统"),
        Internal(1, "内部系统"),
        External(2, "外部系统");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private RegistTypeEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static RegistTypeEnum getEnumByKey(int key) {
        	RegistTypeEnum[] es = RegistTypeEnum.values();
            for (RegistTypeEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }

    /**
     * <p>Title: 标准对象类型枚举</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-8-23上午10:41:47
     */
    public static enum StandardEnum {
        Org(100, "组织对象"),
        col(200, "协同对象"),
        edoc(300, "公文对象"),
        doc(400, "文档对象"),
        news(500, "新闻对象"),
        bull(600, "公告对象");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private StandardEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static StandardEnum getEnumByKey(int key) {
            StandardEnum[] es = StandardEnum.values();
            for (StandardEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }


    /**
     * <p>Title: 组织机构需要的枚举</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-7-29下午6:17:07
     */

    public static enum OrgEnum {

        Example(0001, "示例枚举"),
        //huzy 组织机构访问需要用到的
        AccessGroup(1001, "访问集团"),
        AccessAccount(1002, "访问单位"),
        AccessDept(1003, "访问部门"),
        AccessMember(1004, "访问人员"),
        AccessPost(1005, "访问岗位"),
        AccessLevel(1006, "访问职级"),
        //huzy end
        //huzy 组织机构更新操作
        Add(1007,"新增"),
        Update(1008,"更新"),
        Delete(1009,"删除"),
        ORG_DATA(1010,"组织数据"),
        ORG_OPERATION(1011,"操作类型"),
        ORG_Register_Code(1012,"组织注册代码");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private OrgEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static OrgEnum getEnumByKey(int key) {
            OrgEnum[] es = OrgEnum.values();
            for (OrgEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }


    /**
     * <p>Title: 交换需要的枚举</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-7-29下午6:24:39
     */

    public static enum ExchangeEnum {
        col(StandardEnum.col.getKey(), "协同"),
        edoc(StandardEnum.edoc.getKey(), "公文"),
        edoc_receipt(StandardEnum.edoc.getKey() + 1, "公文回执对象"),
        doc(StandardEnum.doc.getKey(), "文档"),
        news(StandardEnum.news.getKey(), "新闻"),
        bull(StandardEnum.bull.getKey(), "公告");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private ExchangeEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static ExchangeEnum getEnumByKey(int key) {
            ExchangeEnum[] es = ExchangeEnum.values();
            for (ExchangeEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }

    /**
     * <p>Title: 本系统状态需要的枚举</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     * 2014-7-29下午6:24:39
     */

    public static enum SystemStateEnum {
        isRegister(1000, "是否注册"),
        isUploadOrg(1001, "是否上传组织机构"),
        isDownLoadOrg(1002, "是否下载组织机构"),
        isEnable(1003, "是否被CPP停用"),
        isConnServer(1004, "是否连接上服务器"),
        hasExAccoun(1005,"是否有可选择的外单位数据");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private SystemStateEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static SystemStateEnum getEnumByKey(int key) {
            SystemStateEnum[] es = SystemStateEnum.values();
            for (SystemStateEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }


    /**
     * <p>Title: 交换服务枚举</p>
     * <p>Description: 用来描述esp协议支持的服务</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-8-19下午5:33:30
     */
    public static enum ExchangeServiceEnum {

        /**
         * 交换服务
         */

        ServiceExchange(1000, "ServiceExchange"),

        /**
         * 应答服务
         */

        ServiceReceipt(1001, "ServiceReceipt"),

        /**
         * 状态改变服务：暂时用不到
         */
        @Deprecated
        ServiceStatue(1002, "ServiceStatue");


        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private ExchangeServiceEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static ExchangeServiceEnum getEnumByKey(int key) {
            ExchangeServiceEnum[] es = ExchangeServiceEnum.values();
            for (ExchangeServiceEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }

    /**
     * <p>Title: 发送信息状态</p>
     * <p>Description: 用同一代码描述发送端和接收端的对应状态</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author Gong.SJ 2014-8-28 16:40:22
     */
    public static enum SendInfoStateEnum {
        UnSend(2000, "未发送", "未接收"), Send(2001, "已发送", "已接受"), SendError(2002, "发送错误", "接收错误");

        private int key;

        private String senderDesc;

        private String receiverDesc;

        SendInfoStateEnum(int key, String senderDesc, String receiverDesc) {
            this.key = key;
            this.senderDesc = senderDesc;
            this.receiverDesc = receiverDesc;
        }

        public static SendInfoStateEnum getEnumByKey(int key) {
            SendInfoStateEnum[] es = SendInfoStateEnum.values();
            for (SendInfoStateEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getSenderDesc() {
            return senderDesc;
        }

        public void setSenderDesc(String senderDesc) {
            this.senderDesc = senderDesc;
        }

        public String getReceiverDesc() {
            return receiverDesc;
        }

        public void setReceiverDesc(String receiverDesc) {
            this.receiverDesc = receiverDesc;
        }
    }

    /**
     * <p>Title: 发送者状态</p>
     * <p>Description: G6各端状态信息</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-8-17下午7:24:01
     */
    public static enum ExchangeSenderStateEnum {

        StateUnSended(2000, "未发送", "已接受未发送至接收端"),

        StateSended(2001, "已发送", "已发送");

        private int key;

        private String senderDes;
        private String receiverDes;

        public String getSenderDes() {
            return senderDes;
        }

        public String getReceiverDes() {
            return receiverDes;
        }

        public int getKey() {
            return key;
        }

        ExchangeSenderStateEnum(int key, String senderDes, String receiverDes) {
            this.key = key;
            this.senderDes = senderDes;
            this.receiverDes = receiverDes;
        }

        public static ExchangeSenderStateEnum getEnumByKey(int key) {
            ExchangeSenderStateEnum[] es = ExchangeSenderStateEnum.values();
            for (ExchangeSenderStateEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }

    /**
     * <p>Title: 响应数据状态</p>
     * <p>Description: 响应数据在各个端的描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author Gong.SJ 2014-8-30 15:20:22
     */
    public static enum ExchangeResponseStateEnum {
        UnSend(3000, "发送端未发送数据", "服务器成功接收发送端数据", "接收端未发送响应数据到服务器"),
        HasSendReq(3001, "发送端成功发送数据到服务器", "服务器成功发送数据到接收端", ""),
        HasReceiveResp(3002, "发送端收到服务器响应数据", "服务器接收到接收端的响应数据", ""),
        HasSendResp(3003, "", "服务器成功发送响应数据到发起端", "接收端已发送响应数据到服务器");

        private int key;
        private String senderDes;
        private String serverDes;
        private String receiverDes;

        ExchangeResponseStateEnum(int key, String senderDes, String serverDes, String receiverDes) {
            this.key = key;
            this.senderDes = senderDes;
            this.serverDes = serverDes;
            this.receiverDes = receiverDes;
        }

        public static ExchangeResponseStateEnum getEnumByKey(int key) {
            ExchangeResponseStateEnum[] es = ExchangeResponseStateEnum.values();
            for (ExchangeResponseStateEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

        public String getSenderDes() {
            return senderDes;
        }

        public String getServerDes() {
            return serverDes;
        }

        public String getReceiverDes() {
            return receiverDes;
        }

        public int getKey() {
            return key;
        }
    }

    /**
     * <p>Title: 接收者状态</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-8-19下午9:20:22
     */
    /*public static enum ExchangeReceiverStateEnum {

        //响应在发送端状态
        *//**
         * 响应初始状态：1.在发送端表示发起端默认状态 2.在服务器端表示接收到发送端数据的初始状态
         * 服务器上响应数据这个状态表示未发送，需要轮询发送
         *//*
        StateUnReceive(3000, "发送端未发送数据/服务器成功接收发送端数据"),

        *//**
         * 发送端发送数据后收到CPP回执，表示发送成功
         *//*
        StateReturn(3001, "发送端成功发送数据到服务器"),

        *//**
         * 发送端收到接收端回执，表示交换成功
         *//*
        StateEnd(3002, "交换完成"),

        //响应在服务器端状态
        *//**
         * 服务器成功发送数据到接收端
         *//*
        CPPSendToReceiver(3003, "服务器成功发送数据到接收端"),

        *//**
         * 服务器收到接收端的回执且没有发送给发送端 需要轮询发送
         *//*
        CPPReceiveResp(3004, "服务器接收到接收端的响应数据"),

        *//**
         * 服务器收到接收端的回执并返回给客户端 状态终止
         *//*
        CPPStateEnd(3005, "服务器成功发送响应数据到发起端"),

        //响应端状态
        *//**
         * 接收端接收数据响应未发送到CPP 需要轮询发送
         *//*
        StateResponseNotToCPP(3006, "接收端未发送响应数据到服务器"),

        *//**
         * 接收端响应成功
         *//*
        StateResponse(3007, "接收端已发送响应数据到服务器");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private ExchangeReceiverStateEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static ExchangeReceiverStateEnum getEnumByKey(int key) {
            ExchangeReceiverStateEnum[] es = ExchangeReceiverStateEnum.values();
            for (ExchangeReceiverStateEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }*/

    /**
     * <p>Title: 数据发送和接收者类型枚举</p>
     * <p>Description: 用来描述发送端或接收端是集团、单位、部门还是个人</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author Gong.SJ
     *         2014-8-21 15:45:30
     */
    public static enum ExchangeSenderOrReceiveTypeEnum {
        GROUP(-1, "集团"), ACCOUNT(0, "单位"), DEPARTMENT(1, "部门"), INDIVIDUAL(2, "个人");

        private int key;

        private String des;

        private ExchangeSenderOrReceiveTypeEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }
        
        public static ExchangeSenderOrReceiveTypeEnum getEnumByKey(int key) {
        	ExchangeSenderOrReceiveTypeEnum[] es = ExchangeSenderOrReceiveTypeEnum.values();
            for (ExchangeSenderOrReceiveTypeEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }
    }


    /**
     * <p>Title: 多单位协同需要的枚举</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-7-29下午6:25:00
     */

    public static enum ColEnum {

        Example(0001, "示例枚举");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private ColEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static ColEnum getEnumByKey(int key) {
            ColEnum[] es = ColEnum.values();
            for (ColEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }


    /**
     * <p>Title: 大协同需要的枚举</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-7-29下午6:25:17
     */

    public static enum BigColEnum {

        Example(0001, "示例枚举");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private BigColEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static BigColEnum getEnumByKey(int key) {
            BigColEnum[] es = BigColEnum.values();
            for (BigColEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }


    /**
     * <p>Title: 数据共享需要的枚举</p>
     * <p>Description: 代码描述</p>
     * <p>Copyright: Copyright (C) 2012 Seeyon, Inc. All rights reserved. </p>
     * <p>Company: 北京致远协创软件有限公司</p>
     *
     * @author yangc
     *         2014-7-29下午6:25:30
     */

    public static enum ShareEnum {

        Example(0001, "示例枚举");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private ShareEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static ShareEnum getEnumByKey(int key) {
            ShareEnum[] es = ShareEnum.values();
            for (ShareEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }
    
    /**
     * G6端更新单位信息后，是否需要同步更新CPP视图信息
     * 表gccp_view_enum autoUpdate
     * @author Xiang.chen
     *
     */
    public static enum ViewAutoUpdateEnum {

        disauto(0, "不自动更新"),
        auto(1, "自动更新");

        private int key;

        private String des;


        public int getKey() {
            return key;
        }

        public String getDes() {
            return des;
        }

        private ViewAutoUpdateEnum(int k, String s) {
            this.key = k;
            this.des = s;
        }

        public static ViewAutoUpdateEnum getEnumByKey(int key) {
        	ViewAutoUpdateEnum[] es = ViewAutoUpdateEnum.values();
            for (ViewAutoUpdateEnum e : es) {
                if (e.getKey() == key) {
                    return e;
                }
            }
            return null;
        }

    }
}
