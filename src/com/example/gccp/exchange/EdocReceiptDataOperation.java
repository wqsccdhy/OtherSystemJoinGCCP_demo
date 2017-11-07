package com.example.gccp.exchange;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.seeyon.v3x.exchange.domain.EdocRecieveRecord;
import com.seeyon.v3x.exchange.domain.EdocSendDetail;
import com.seeyon.v3x.exchange.util.Constants;
import com.seeyon.v3x.plugin.gccp.common.GCCPEnum.ExchangeEnum;
import com.seeyon.v3x.plugin.gccp.exception.GCCPException;
import com.seeyon.v3x.plugin.gccp.exchange.IExchangeOperation;
import com.seeyon.v3x.plugin.gccp.exchange.domain.GCCPRequestDO;
import com.seeyon.v3x.plugin.gccp.exchange.domain.GCCPResponseDO;
import com.seeyon.v3x.plugin.gccp.standard.StandardData;
import com.seeyon.v3x.plugin.gccp.standard.StandardData.DataPart;

public class EdocReceiptDataOperation implements IExchangeOperation {
    private static Logger log = Logger.getLogger(EdocReceiptDataOperation.class);

    @Override
    public StandardData readSandard(GCCPRequestDO gccpRequestDO) throws GCCPException {
        Long dataId = gccpRequestDO.getDataId();//建议该值为公文签收对象id，在收文EdocDataOperation.writeStandard方法中存在该对象，请保存后使用
        StandardData standardData = new StandardData();
        //此处建议只做对象的获取与封装传递，签收、登记、退回、撤销不同的动作放在业务系统自身进行设置，例如调用发送交换请求处，但本示例为了达到业务说明，将不同的操作在此处进行说明
        EdocRecieveRecord edocRecieveRecord = new EdocRecieveRecord();//TODO 根据id获取签收对象，示例代码为了后续演示不做保存和获取代码实现，请自行实现该逻辑  
        edocRecieveRecord.setId(dataId);
//        edocRecieveRecord.setStatus(Constants.C_iStatus_Recieved);//TODO 签收
//        edocRecieveRecord.setStatus(Constants.C_iStatus_Registered);//TODO 登记
//        edocRecieveRecord.setEdocId(edocId);//公文id
//        edocRecieveRecord.setRemark("请设置签收或者登记意见，当然也可以不设置");
          edocRecieveRecord.setStatus(Constants.C_iStatus_Receive_StepBacked); //TODO 退回
          edocRecieveRecord.setRecUser("recUser");
//        edocRecieveRecord.setReplyId("replyId");//TODO 退回 replyId的值为文件EdocDataOperation中的writeStandard方法有个对象EdocRecieveRecord replyId=EdocRecieveRecord.getReplyId()
//        edocRecieveRecord.setRecUserId(recUserId);//TODO 退回  谁回退的公文，就为该人员的id
//        edocRecieveRecord.setExchangeOrgId(exchangeOrgId);//TODO 退回 交换单位id 比如文件是省直属发送给财政厅  exchangeOrgId为财政厅单位id
//        edocRecieveRecord.setStepBackInfo("请设退回意见，当然也可以不设置");
//        edocRecieveRecord.setStatus(Constants.C_iStatus_withdraw);//TODO 撤销 ，指发送后，当对方未处理，可以进行撤回，传递的对象是自身系统EdocDataOperation.readSandard产生的对象。
        standardData.addData(edocRecieveRecord);
        return standardData;
    }

    @Override
    public void writeStandard(GCCPResponseDO gccpResponseDO, StandardData standardData) throws GCCPException {

        try {
            DataPart dataPart = null;
            while ((dataPart = standardData.nextData()) != null) {
                Object object = dataPart.getContent();
                if (object instanceof EdocRecieveRecord) {
                    EdocRecieveRecord recieveRecord = (EdocRecieveRecord) object;
                    int status = recieveRecord.getStatus();
                    recieveRecord.getRecUser();//获取处理人名称
                    recieveRecord.getRecUserId();//获取处理人id
                    recieveRecord.getEdocId();//获取公文对象id
                    recieveRecord.getReplyId();
                    Timestamp recTime = recieveRecord.getRecTime();
                    String recUser = recieveRecord.getRecUser();
                    switch (status) {
                        case Constants.C_iStatus_Recieved:
                            //TODO 签收
                        	System.out.println("签收");
                            break;
                        case Constants.C_iStatus_Registered:
                            //TODO 登记
                        	System.out.println("登记");
                            break;
                        case Constants.C_iStatus_Receive_StepBacked:
                            //TODO 退回
                        	System.out.println("退回");
                            break;
                        case Constants.C_iStatus_withdraw:
                        	System.out.println("撤销");
                            //TODO 撤销
                            break;
                        default:
                            //do nothing
                            break;
                    }

                }else if(object instanceof EdocSendDetail){
                	EdocSendDetail edocSendDetail = (EdocSendDetail) object;
                	String recOrgName = edocSendDetail.getRecOrgName();//签收单位
                	Timestamp recTime = edocSendDetail.getRecTime();//签收时间
                	String recUserName = edocSendDetail.getRecUserName();//签收人员
                	String recOrgId = edocSendDetail.getRecOrgId();//接收单位id
                	Long id = edocSendDetail.getId();
                	System.out.println("recOrgName：" + recOrgName);
                	System.out.println("recTime：" + recTime);
                	System.out.println("recUserName：" + recUserName);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public ExchangeEnum operationExchangeCategory() {
        return ExchangeEnum.edoc_receipt;
    }
    

}
