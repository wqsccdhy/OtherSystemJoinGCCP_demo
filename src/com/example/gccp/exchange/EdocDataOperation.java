package com.example.gccp.exchange;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.example.gccp.Content;
import com.seeyon.v3x.affair.domain.Affair;
import com.seeyon.v3x.common.constants.ApplicationCategoryEnum;
import com.seeyon.v3x.common.filemanager.Attachment;
import com.seeyon.v3x.common.filemanager.V3XFile;
import com.seeyon.v3x.edoc.domain.EdocBody;
import com.seeyon.v3x.edoc.domain.EdocSummary;
import com.seeyon.v3x.exchange.domain.EdocRecieveRecord;
import com.seeyon.v3x.exchange.domain.EdocSendDetail;
import com.seeyon.v3x.exchange.domain.EdocSendRecord;
import com.seeyon.v3x.exchange.util.Constants;
import com.seeyon.v3x.organization.domain.V3xOrgEntity;
import com.seeyon.v3x.plugin.gccp.common.GCCPEnum.ExchangeEnum;
import com.seeyon.v3x.plugin.gccp.common.IContext;
import com.seeyon.v3x.plugin.gccp.exception.GCCPException;
import com.seeyon.v3x.plugin.gccp.exchange.IExchangeOperation;
import com.seeyon.v3x.plugin.gccp.exchange.domain.GCCPRequestDO;
import com.seeyon.v3x.plugin.gccp.exchange.domain.GCCPResponseDO;
import com.seeyon.v3x.plugin.gccp.standard.StandardData;
import com.seeyon.v3x.plugin.gccp.standard.StandardData.DataPart;
import com.seeyon.v3x.util.FileUtil;

public class EdocDataOperation implements IExchangeOperation {

	@Override
	public StandardData readSandard(GCCPRequestDO gccpRequestDO)
			throws GCCPException {

		// TODO 根据请求对象封装公文交换的标准对象
		// 注意:先封装对象，最后封装文件
		Long dataId = gccpRequestDO.getDataId();// TODO
												// 获取请求传递的业务id，根据该id获取自身业务的公文数据。

		StandardData standardData = new StandardData();// 生成标准平台的数据封装对象。
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 设置公文实体类
		EdocSummary summary = new EdocSummary();// 标准平台，公文对象
		summary.setId(dataId);// TODO 设置唯一标识id
		
		summary.setSubject("测试财政厅发文至办公厅" + sdf.format(new Date()));// 设置标题
		summary.setEdocType(0);
		summary.setStartTime(new Timestamp(System.currentTimeMillis()));
		summary.setCreateTime(new Timestamp(System.currentTimeMillis()));// TODO
																			// 设置创建时间
		summary.setCreatePerson("张三");// 创建人
		summary.setSendUnitId("Account|" + SendDataMain.sendOrgId);// 发送单位ID
		summary.setDocMark("测试《2016》1号");// 设置文号
		summary.setVarchar20(Content.getInstance().getContextParameter(
				IContext.SYSTEM_ID)
				+ "来文");// 设置公文描述
		summary.setCopies(10);// 份数
		summary.setKeywords("备注");// 备注
		// writeStringToFile("summaryArray", summary);

		//Long accountId = -6471354914145743078L;
		Long accountId = SendDataMain.recOrgId;
		// 设置公文交换发送对象
		EdocSendRecord sendRecord = new EdocSendRecord();
		sendRecord.setIdIfNew();
		sendRecord.setExchangeOrgId(SendDataMain.sendOrgId);// TODO 设置公文交换发文组织（单位或者部门）id
		sendRecord.setExchangeOrgName("贵州省财政厅");// TODO 设置发文组织名称
		sendRecord.setExchangeType(Constants.C_iExchangeType_Org);// TODO 单位
		// sendRecord.setExchangeType(Constants.C_iExchangeType_Dept);// TODO 部门
		// 设置发文组织类型，单位（Constants.C_iAccountType_Org）或者部门（Constants.C_iAccountType_Dept）
		sendRecord.setSendUnit("贵州省财政厅");//这个字段不能为空，必须要有值
		sendRecord.setEdocId(summary.getId());
		sendRecord.setDocMark(summary.getDocMark());
		sendRecord.setSendedNames("省政府办公厅");// TODO
														// 设置送往单位或者部门名称，以、隔开,如：单位1、部门2、单位2
		sendRecord
				.setSendedTypeIds("Account|" + accountId);// TODO
														 // 设置送往单位或者部门类型和id，以(,)隔开，如：Account|123456,Department|654321,Account|123654
		sendRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
		sendRecord.setSendTime(new Timestamp(System.currentTimeMillis()));
		sendRecord.setIssueDate(new Date());
		sendRecord.setSubject(summary.getSubject());
		sendRecord.setSendUserNames("财政厅公文收发员");// TODO 发送者，例子中为财政厅公文收发员
		//sendRecord.setSendUserId(-352442265819722256L);// TODO
														// 发送者id，例子中为财政厅公文收发员id
		
		sendRecord.setSendUserId(SendDataMain.sendMemberId);// TODO 发送者id
		
		sendRecord.setStatus(Constants.C_iStatus_Sent);// 设置交换对象为已发送状态
		sendRecord.setCopies(10);
		
		//发文时对公文标题进行encode处理
		sendRecord.setSubject(translateSubject(summary.getSubject()));
		summary.setSubject(translateSubject(summary.getSubject()));

		// writeStringToFile("sendRecordArray", sendRecord);

		ArrayList<EdocRecieveRecord> recieveRecords = createExchangeOtherObject(sendRecord);
		// writeStringToFile("recieveRecordsArray", recieveRecords);

		ArrayList<V3XFile> v3xFiles = new ArrayList<V3XFile>();
		ArrayList<File> files = new ArrayList<File>();

		// 设置正文对应文件实体对象
		String fileName = "C:/file/" + "深入分析Java中的中文编码问题.pdf";// TODO
		// fileName = "C:/" + "571.sp1跨系统.doc";
		File file2 = new File(fileName);
		V3XFile bodyFile = new V3XFile();
		bodyFile.setFilename(file2.getName());
		bodyFile.setSize(file2.length());// TODO 设置文件大小
		bodyFile.setAccountId(accountId);// TODO 设置创建单位id
		bodyFile.setIdIfNew();
		bodyFile.setMimeType(FileUtil.getMimeType(fileName));
		bodyFile.setType(com.seeyon.v3x.common.filemanager.Constants.ATTACHMENT_TYPE.FILE
				.ordinal());
		bodyFile.setCategory(ApplicationCategoryEnum.edoc.key());
		v3xFiles.add(bodyFile);
		files.add(file2);// TODO 设置文件

		// 设置正文实体类
		EdocBody edocBody = new EdocBody();
		edocBody.setIdIfNew();
		edocBody.setEdocSummary(summary);
		edocBody.setContent(bodyFile.getId().toString());
		// com.seeyon.v3x.common.constants.Constants.EDITOR_TYPE_OFFICE_WORD
		// com.seeyon.v3x.common.constants.Constants.EDITOR_TYPE_WPS_WORD
		edocBody.setContentType(com.seeyon.v3x.common.constants.Constants.EDITOR_TYPE_PDF);// TODO
																							// 正文类型，根据文件后缀名获取对应值
		edocBody.setContentNo(0);
		edocBody.setCreateTime(summary.getCreateTime());
		edocBody.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		summary.getEdocBodies().add(edocBody);

		// 设置附件
		String filename1 = "C:/file/" + "删除公文超期方法.docx";
		File file = new File(filename1);
		V3XFile attFile1 = new V3XFile();
		attFile1.setIdIfNew();
		attFile1.setFilename(file.getName());
		attFile1.setSize(file.length());// TODO 设置文件大小
		attFile1.setAccountId(accountId);// TODO 设置创建单位id
		attFile1.setMimeType(FileUtil.getMimeType(attFile1.getFilename()));
		attFile1.setType(com.seeyon.v3x.common.filemanager.Constants.ATTACHMENT_TYPE.FILE
				.ordinal());
		attFile1.setCategory(ApplicationCategoryEnum.edoc.key());
		v3xFiles.add(attFile1);
		files.add(file);// TODO 设置文件

		ArrayList<Attachment> attachments = new ArrayList<Attachment>();
		Attachment attachment1 = new Attachment();
		attachment1.setFilename(attFile1.getFilename());
		attachment1.setIdIfNew();
		attachment1.setReference(summary.getId());
		attachment1.setSubReference(summary.getId());
		attachment1.setCreatedate(new Date());
		attachment1.setFileUrl(attFile1.getId());
		attachment1.setMimeType(attFile1.getMimeType());
		attachment1.setSize(attFile1.getSize());
		attachment1.setCategory(attFile1.getCategory());
		attachment1.setType(attFile1.getType());

		attachments.add(attachment1);

		// 开发封装对象和文件至标准数据对象中
		standardData.addData(sendRecord);
		standardData.addData(new ArrayList<EdocSendDetail>(sendRecord
				.getSendDetailList()));
		standardData.addData(new ArrayList<EdocRecieveRecord>(recieveRecords));
		standardData.addData(summary);
		standardData.addData(attachments);
		standardData.addData(v3xFiles);
		standardData.addData(new ArrayList<EdocBody>(summary.getEdocBodies()));// 正文

		// writeStringToFile("v3xFilesArray", v3xFiles);
		// writeStringToFile("attachment1Array", attachment1);

		for (int i = 0; i < v3xFiles.size(); i++) {
			V3XFile v3xFile = v3xFiles.get(i);
			Long id = v3xFile.getId();
			String vFileName = v3xFile.getFilename();
			standardData.addDataFile(id + "|" + vFileName, files.get(i));
		}
		return standardData;
	}

	private ArrayList<EdocRecieveRecord> createExchangeOtherObject(
			EdocSendRecord sendRecord) {
		ArrayList<EdocRecieveRecord> result = new ArrayList<EdocRecieveRecord>();
		Long sendRecordId = sendRecord.getId();
		String sendedTypeIds = sendRecord.getSendedTypeIds();
		String sendedNames = sendRecord.getSendedNames();
		String[] typeIds = sendedTypeIds.split(V3xOrgEntity.ORG_ID_DELIMITER);
		String[] names = sendedNames.split("、");
		sendRecord.setSendDetailList(new ArrayList<EdocSendDetail>());
		for (int i = 0; i < typeIds.length; i++) {
			String[] split = typeIds[i].split("[|]");
			EdocSendDetail detail = new EdocSendDetail();
			detail.setIdIfNew();
			detail.setRecOrgId(split[1]);
			detail.setRecOrgType("Department".equalsIgnoreCase(split[0]) ? Constants.C_iAccountType_Dept
					: Constants.C_iAccountType_Org);
			detail.setRecOrgName(names[i]);
			detail.setSendType(Constants.C_iStatus_Send);
			detail.setStatus(Constants.C_iStatus_Torecieve);
			detail.setSendRecordId(sendRecordId);
			sendRecord.getSendDetailList().add(detail);

			EdocRecieveRecord recieveRecord = new EdocRecieveRecord();
			recieveRecord.setIdIfNew();
			//recieveRecord.setStatus(sendRecord.getStatus());
			recieveRecord.setCreateTime(sendRecord.getCreateTime());
			recieveRecord.setSender(sendRecord.getSendUserNames());
			recieveRecord.setSubject(sendRecord.getSubject());
			recieveRecord.setDocType(sendRecord.getDocType());
			recieveRecord.setDocMark(sendRecord.getDocMark());
			recieveRecord.setSecretLevel(sendRecord.getSecretLevel());
			recieveRecord.setUrgentLevel(sendRecord.getUrgentLevel());
			recieveRecord.setSendUnit(sendRecord.getSendUnit());
			recieveRecord.setIssuer(sendRecord.getIssuer());
			recieveRecord.setIssueDate(sendRecord.getIssueDate());
			recieveRecord.setSendTo(sendRecord.getSendedNames());
			recieveRecord.setEdocId(sendRecord.getEdocId());
			recieveRecord.setExchangeType(detail.getRecOrgType());
			recieveRecord.setExchangeOrgId(Long.valueOf(detail.getRecOrgId()));
			recieveRecord.setReplyId(detail.getId().toString());
			recieveRecord.setSendUnitType(Constants.C_iAccountType_Org);
			recieveRecord.setFromInternal(false);
			recieveRecord.setCopies(sendRecord.getCopies());
			result.add(recieveRecord);
		}

		return result;
	}

	@Override
	public void writeStandard(GCCPResponseDO gccpResponseDO,
			StandardData standardData) throws GCCPException {
		// writeStringToFile("开始接收数据", "开始接收数据");
		List<Attachment> attachments = new ArrayList<Attachment>();
		Map<String, EdocBody> edocBodyMap = new HashMap<String, EdocBody>();
		List<EdocRecieveRecord> edocRecieveRecords = new ArrayList<EdocRecieveRecord>();
		List<EdocSendDetail> sendDetails = new ArrayList<EdocSendDetail>();
		Map<String, File> fileMapping = new HashMap<String, File>();
		EdocSummary edocSummary = null;
		EdocSendRecord edocSendRecord = null;
		// TODO 针对standardata进行业务数据解析
		DataPart part = null;
		try {
			while ((part = standardData.nextData()) != null) {
				if (StandardData.DataPart.CONTENT_TYPE_OBJECT.equals(part
						.getContentType())) {
					Object obj = null;
					try {
						obj = part.getContent();
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
					if (obj instanceof EdocSummary) {
						edocSummary = (EdocSummary) obj;
						// writeStringToFile("edocSummary", edocSummary);
						String sendUnitId = edocSummary.getSendUnitId();
						edocSummary.getSendUnit();
						Set<EdocBody> edocBodies = edocSummary.getEdocBodies();
						// writeStringToFile("edocBodies", edocBodies);
					} else if (obj instanceof EdocSendRecord) {
						edocSendRecord = (EdocSendRecord) obj;
						// writeStringToFile("edocSendRecord", edocSendRecord);
					} else if (obj instanceof List) {
						List<Serializable> list = (List<Serializable>) obj;
						for (Serializable serializable : list) {
							if (serializable instanceof Attachment) {
								// 附件
								attachments.add((Attachment) serializable);
							} else if (serializable instanceof EdocRecieveRecord) {
								EdocRecieveRecord record = (EdocRecieveRecord) serializable;
								record.getSubject();
								long exchangeOrgId = record.getExchangeOrgId();//接收单位id
								//找到对应的接收单位
								if(SendDataMain.sendOrgId.equals(exchangeOrgId)){
									Long id = record.getId();
									String replyId = record.getReplyId();
								}
								
								/*edocRecieveRecords
										.add((EdocRecieveRecord) serializable);*/
								
							} else if (serializable instanceof Affair) {
								// affairs.add((Affair) serializable);
							} else if (serializable instanceof EdocSendDetail) {
								sendDetails.add((EdocSendDetail) serializable);
							} else if (serializable instanceof EdocBody) {
								// 正文
								EdocBody eb = (EdocBody) serializable;
								edocBodyMap.put(eb.getContent(), eb);
							}
						}
					}
				} else if (StandardData.DataPart.CONTENT_TYPE_FILE.equals(part
						.getContentType())) {
					String transFileName[] = part.getContentName().split("\\|");
					String fileId = transFileName[0];// v3xFile的id
					String fileName = transFileName[1];
					InputStream inputStream = part.getContentStream();
					// TODO 对文件进行处理 包括附件和正文
					// LocalFileManager manager = new LocalFileManager();
					// String localId = manager.save(inputStream);
					// fileMapping.put(fileId, manager.getFile(localId));
				}

			}
			
			//接收到数据后，需要对文件标题进行decode操作
			edocSummary.setSubject(reversalSubject(edocSummary.getSubject()));
	        edocSendRecord.setSubject(reversalSubject(edocSendRecord.getSubject()));
			
			for (Entry<String, EdocBody> entry : edocBodyMap.entrySet()) {
				EdocBody edocBody = entry.getValue();
				edocBody.getContent();
				//正文类型为标准
				if ("HTML".equals(edocBody.getContentType())) {
					//正文内容
					String htmlCode = edocBody.getContent();
					System.out.println(htmlCode);
				}
			}

			System.out.println(String.format("收到\"%s\"公文：《%s》",
					edocSummary.getSendUnit(), edocSummary.getSubject()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new GCCPException(e);
		}

	}

	@Override
	public ExchangeEnum operationExchangeCategory() {
		return ExchangeEnum.edoc;
	}

	private void writeStringToFile(String fileName, Object obj) {
		/*
		 * String substring = getPath(); JSONArray sendRecordArray =
		 * JSONArray.fromObject(obj); StringBuffer buffer = new StringBuffer();
		 * buffer.append(fileName + "：").append(sendRecordArray); File filex =
		 * new File(substring + "/file/" + fileName + ".txt"); try {
		 * FileUtils.writeStringToFile(filex, buffer.toString(), "UTF-8"); }
		 * catch (IOException e) { // TODO e.printStackTrace(); }
		 */

	}

	private String getPath() {
		String path = this.getClass().getClassLoader().getResource("")
				.getPath();
		int indexOf = path.indexOf("WEB-INF");
		String substring = path.substring(0, indexOf);
		return substring;
	}
	
	/**
	 * 处理标题中的特殊字符
	 * @param subject
	 * @return
	 */
    private String translateSubject(String subject) {
    	String value = "";
		if (subject!=null && !subject.isEmpty()) {
			try {
				value = URLEncoder.encode(subject, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				value = "";
				System.out.println("记录日志");
			}
		}
		return value;
	}
    
    /**
     * 处理标题中的特殊字符
     * @param subject
     * @return
     */
    private String reversalSubject(String subject) {
		String value = "";
		if (subject!=null && !subject.isEmpty()) {
			try {
				value = URLDecoder.decode(subject, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				value = "";
				System.out.println("记录日志");
			}
		}
		return value;
		
	}
}
