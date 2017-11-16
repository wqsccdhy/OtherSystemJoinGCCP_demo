package com.example.gccp.exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.gccp.Content;
import com.seeyon.v3x.common.utils.UUIDLong;
import com.seeyon.v3x.plugin.gccp.common.GCCPEnum;
import com.seeyon.v3x.plugin.gccp.common.GCCPEnum.ExchangeSenderOrReceiveTypeEnum;
import com.seeyon.v3x.plugin.gccp.exchange.IExchangeOperation;
import com.seeyon.v3x.plugin.gccp.exchange.cep.GCCPExchangePackage;
import com.seeyon.v3x.plugin.gccp.exchange.domain.GCCPRequestDO;
import com.seeyon.v3x.plugin.gccp.exchange.domain.GCCPResponseDO;
import com.seeyon.v3x.plugin.gccp.exchange.esp.GCCPExchangeServiceRequest;
import com.seeyon.v3x.plugin.gccp.standard.StandardData;
import com.seeyon.v3x.plugin.gccp.transfers.GCCPRequest;
import com.seeyon.v3x.plugin.gccp.transfers.IGCCPResponseHandle;
import com.seeyon.v3x.plugin.gccp.transfers.TransfersManager;
import com.seeyon.v3x.plugin.gccp.util.ProtocolUtil;

public class SendDataMain {
	//发文单位ID
	public static Long sendOrgId = 7630217338153749835L;
	
	//发文人员ID
	public static Long sendMemberId = 8713599982244744593L;
	
	//公文接收单位ID
	public static Long recOrgId = 5647708944559655583L;
	
	//公文接收人员ID
	public static Long recMemberId = -2114305908855324574L;
	
	public static void main(String[] args) throws Exception {
		SendDataMain dataMain = new SendDataMain();
		dataMain.sendDataToTarger();
	}

	public String sendDataToTarger() throws Exception {
		GCCPRequestDO gccpRequestDO = new GCCPRequestDO();
		Content content = Content.getInstance();
		gccpRequestDO.setGroupId(content.getSelfSystem().getRegisterCode());
		gccpRequestDO.setAccountId(sendOrgId);//发文单位ID
		gccpRequestDO.setTitle("测试财政厅发文至办公厅_wq");
		gccpRequestDO.setDataId(UUIDLong.longUUID());// TODO 业务id
		gccpRequestDO.setCreateTime(new Date());
		gccpRequestDO.setId(UUIDLong.longUUID());
		gccpRequestDO.setSenderAccountName("测试单位1_wq_cd");// 交换请求发送单位名称
		gccpRequestDO.setSenderName("测试单位1_wq_cd人员");// 交换请求发送者名称
		gccpRequestDO.setSenderId(sendMemberId);// TODO 发送者的id
		gccpRequestDO
				.setSenderType(GCCPEnum.ExchangeSenderOrReceiveTypeEnum.ACCOUNT
						.getKey());
		gccpRequestDO.setCategory(GCCPEnum.ExchangeEnum.edoc.getKey());// 公文交换数据请求，根据该值不同，会调用不同的IExchangeOperation生成和处理数据
		//gccpRequestDO.setCategory(GCCPEnum.ExchangeEnum.edoc_receipt.getKey());//公文回执操作
		
		
		List<GCCPResponseDO> responseDOList = new ArrayList<GCCPResponseDO>();
		/*GCCPResponseDO respdo = new GCCPResponseDO();
		respdo.setAccountId(8594828605452339470L);// 设置公文送往单位id
		respdo.setId(UUIDLong.longUUID());
		respdo.setReceiverType(ExchangeSenderOrReceiveTypeEnum.ACCOUNT.getKey());
		respdo.setReceiverId(-2114305908855324574L);// 接收人员曾芳
		respdo.setReceiverName("省政府办公厅");// 公文送往单位名称
*/		//responseDOList.add(respdo);

		/*GCCPResponseDO respdo1 = new GCCPResponseDO();
		respdo1.setAccountId(7782L);// 设置公文送往单位id
		respdo1.setId(UUIDLong.longUUID());
		respdo1.setReceiverType(ExchangeSenderOrReceiveTypeEnum.ACCOUNT
				.getKey());
		respdo1.setReceiverId(0L);// 接收人教育厅公文收发员
		respdo1.setReceiverName("省教育厅");// 公文送往单位名称
*/
		//responseDOList.add(respdo1);
		
		
		GCCPResponseDO respdo2 = new GCCPResponseDO();
		respdo2.setAccountId(recOrgId);// 设置公文送往单位id
		respdo2.setId(UUIDLong.longUUID());
		respdo2.setReceiverType(ExchangeSenderOrReceiveTypeEnum.ACCOUNT
				.getKey());
		respdo2.setReceiverId(recMemberId);// 接收人教育厅公文收发员
		respdo2.setReceiverName("测试");// 公文送往单位名称
		responseDOList.add(respdo2);
		
		
		gccpRequestDO.setResponseDOList(responseDOList);
		Boolean sendData = sendData(gccpRequestDO, new SimpleResponseHandle());
		// TODO 处理结果
		System.out.println("sendData:" + sendData);
		return String.valueOf(sendData);
	}

	private static <T> T sendData(GCCPRequestDO gccpRequestDO,
			IGCCPResponseHandle<T> handler) throws Exception {
		TransfersManager transfersManager = new TransfersManager();
		GCCPRequest request = new GCCPRequest();
		request.setHost(Content.getInstance().getServerSystem());
		request.setService(GCCPEnum.ServiceEnum.Server_ReceiveExchangeData);

		IExchangeOperation operation = Content
				.getInstance()
				.getExchangeManager()
				.getExchangeOperation(
						GCCPEnum.ExchangeEnum.getEnumByKey(gccpRequestDO
								.getCategory()));
		final StandardData standardData = operation.readSandard(gccpRequestDO);
		final GCCPExchangePackage cep = new GCCPExchangePackage();
		cep.setDataObject(standardData);
		cep.setTitle(gccpRequestDO.getTitle());
		cep.setCategory(gccpRequestDO.getCategory() + "");
		cep.setExchangeId(gccpRequestDO.getId() + "");
		cep.setAuthor(gccpRequestDO.getSenderName());
		// CEP包from字段格式groupId,accountId,departmentId,individualId
		cep.setFrom(ProtocolUtil.getFromHeader(gccpRequestDO));
		// CEP包to字段格式groupId,accountId,departmentId,individualId|groupId,accountId,departmentId,individualId|..
		List<GCCPResponseDO> gccpResponseDOs = gccpRequestDO
				.getResponseDOList();
		cep.setTo(ProtocolUtil.getToHeader(gccpResponseDOs));

		GCCPExchangeServiceRequest espreq = new GCCPExchangeServiceRequest();
		espreq.setDataObject(cep);
		espreq.setService(GCCPEnum.ExchangeServiceEnum.ServiceExchange);
		espreq.setTarget("0");
		espreq.setHost(cep.getFrom().substring(0, cep.getFrom().indexOf(",")));
		request.setResquestStream(espreq);
		return transfersManager.sendPackage(request, handler);
	}
}
