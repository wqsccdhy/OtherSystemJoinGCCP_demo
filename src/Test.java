import net.sf.json.JSONObject;

import com.seeyon.v3x.exchange.domain.EdocRecieveRecord;

public class Test {

	public static void main(String[] args) {
		
		EdocRecieveRecord recieveRecord = new EdocRecieveRecord();
		JSONObject fromObject = JSONObject.fromObject(recieveRecord);
		System.out.println(recieveRecord);
		
		
		String value = "Account|8408292373439008472";
		String id = "";
		String[] split = value.split("\\|");
		if (split !=null) {
			int length = split.length;
			if (length == 1) {
				id = split[0];
			}else {
				id = split[1];
			}
		}
		System.out.println(id);
		
		/*List<GCCPResponseDO> responseDOList = new ArrayList<GCCPResponseDO>();
		GCCPResponseDO respdo = new GCCPResponseDO();
		respdo.setAccountId(7782l);// 设置公文送往单Account|8408292373439008472
		respdo.setId(UUIDLong.longUUID());
		respdo.setReceiverType(ExchangeSenderOrReceiveTypeEnum.DEPARTMENT.getKey());
		respdo.setReceiverId(7782L);// 接收人员曾芳
		respdo.setReceiverName("省政府办公厅");// 公文送往单位名称
		responseDOList.add(respdo);
		GCCPExchangePackage cep = new GCCPExchangePackage();
		cep.setTo(ProtocolUtil.getToHeader(responseDOList));
		
		List requestId = ProtocolUtil.getCEPAdresses(cep.getTo());
		System.out.println(requestId);*/
		

	}

}
