package com.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.xidea.el.json.JSONDecoder;

import com.example.gccp.Content;
import com.seeyon.v3x.login.principal.domain.JetspeedCredential;
import com.seeyon.v3x.login.principal.domain.JetspeedPrincipal;
import com.seeyon.v3x.organization.domain.V3xOrgAccount;
import com.seeyon.v3x.organization.domain.V3xOrgDepartment;
import com.seeyon.v3x.organization.domain.V3xOrgLevel;
import com.seeyon.v3x.organization.domain.V3xOrgMember;
import com.seeyon.v3x.organization.domain.V3xOrgPost;
import com.seeyon.v3x.plugin.gccp.common.GCCPCodeEnum;
import com.seeyon.v3x.plugin.gccp.common.GCCPEnum;
import com.seeyon.v3x.plugin.gccp.organization.domain.GCCPOrgAccount;
import com.seeyon.v3x.plugin.gccp.register.domain.RegistConfig;
import com.seeyon.v3x.plugin.gccp.transfers.GCCPRequest;
import com.seeyon.v3x.plugin.gccp.transfers.GCCPResponse;
import com.seeyon.v3x.plugin.gccp.transfers.IGCCPResponseHandle;
import com.seeyon.v3x.plugin.gccp.transfers.TransfersManager;

/**
 * 第三方系统同步组织机构到cppDemo
 * 
 * @author Administrator
 * 
 */
public class SysOrgnizationDemo {

	public static void main(String[] args) {
		SysOrgnizationDemo demo = new SysOrgnizationDemo();

		Content content = Content.getInstance();

		// 组织机构对象
		GCCPOrgAccount account = new GCCPOrgAccount();
		long systemId = content.getSystemId();// 注册到cpp的id
		account.setSystemId(systemId);
		account.setIsbasesyn(false);

		//设置组织机构根节点
		V3xOrgAccount root = demo.createV3xOrgAccount(-1730833917365171641l,
				"一级单位组织机构同步测试wqcd", "org-admin", "组织", 1, true, "org-admin",
				new Date(), new Date(), "描述", -1L, false, 1, false);
		account.setAccount(root);// 根节点
		
		//设置组织机构根节点下所挂单位
		V3xOrgAccount accunt1 = demo.createV3xOrgAccount(-6471354914145743078l,
				"测试单位1wqcd", "1", "测试单位1wqcd", 2, true, "csdw1_admin",
				new Date(), new Date(), "描述", -1730833917365171641L, false, 1, false);
		V3xOrgAccount accunt2 = demo.createV3xOrgAccount(-2139993721284966654l,
				"测试单位2wqcd", "2dd", "测试单位2wqcd", 3, true, "csdw2_admin",
				new Date(), new Date(), "描述", -6471354914145743078L, false, 1, false);
		ArrayList<V3xOrgAccount> accountList = new ArrayList<V3xOrgAccount>();
		accountList.add(accunt1);
		accountList.add(accunt2);
		account.setAccountList(accountList);//单位
		
		
		//设置单位下的部门
		V3xOrgDepartment department1 =  demo.createV3xOrgDepartment(6021288266847044712l, "测试部门1wqcd", "0.1", true, true,
				1, new Date(), new Date(), "description", -6471354914145743078l,
				false, 1, "测试部门1wqcd");
		V3xOrgDepartment department2 =  demo.createV3xOrgDepartment(7026818100748635576l, "测试部门2wqcd", "0.2", true, true,
				2, new Date(), new Date(), "description", -6471354914145743078l,
				false, 1, "测试部门2wqcd");
		V3xOrgDepartment department21 =  demo.createV3xOrgDepartment(-6719280739191534022l, "部门21wqcd", "0.2.1", true, true,
				5, new Date(), new Date(), "description", -6471354914145743078l,
				false, 1, "部门21wqcd");
		V3xOrgDepartment department3 =  demo.createV3xOrgDepartment(7363727902672045503l, "测试部门3wqcd", "0.3", true, true,
				3, new Date(), new Date(), "description", -6471354914145743078l,
				false, 1, "测试部门3wqcd");
		V3xOrgDepartment departmentOne =  demo.createV3xOrgDepartment(5527434053364633089l, "测试单位测试部门1wqcd", "0.1", true, true,
				1, new Date(), new Date(), "description", -2139993721284966654l,
				false, 1, "测试单位测试部门1wqcd");
		V3xOrgDepartment departmentTwo =  demo.createV3xOrgDepartment(-1867729282097070021l, "测试单位测试部门2wqcd", "0.2", true, true,
				2, new Date(), new Date(), "description", -2139993721284966654l,
				false, 1, "测试单位测试部门2wqcd");
		ArrayList<V3xOrgDepartment> deptList = new ArrayList<V3xOrgDepartment>();
		ArrayList<V3xOrgDepartment> deptList1 = new ArrayList<V3xOrgDepartment>();
		deptList.add(department1);
		deptList.add(department2);
		deptList.add(department21);
		deptList.add(department3);
		deptList.add(departmentOne);
		deptList.add(departmentTwo);
		//account.setDeptList(deptList);// 部门
		account.setDeptList(deptList1);// 部门
		
		//设置岗位
		V3xOrgPost orgPost1 = demo.createV3xOrgPost(-6937970716481204555l,
				"测试岗位1wqcd", true, 1, new Date(), new Date(), "desciption",
				-6471354914145743078l, false, 1);
		V3xOrgPost orgPost2 = demo.createV3xOrgPost(-682354585021038541l,
				"测试岗位2wqcd", true, 2, new Date(), new Date(), "desciption",
				-6471354914145743078l, false, 1);
		V3xOrgPost orgPost3 = demo.createV3xOrgPost(-8916787309264613421l,
				"测试单位测试部门测试岗位1wqcd", true, 1, new Date(), new Date(), "desciption",
				-2139993721284966654l, false, 1);
		V3xOrgPost orgPost4 = demo.createV3xOrgPost(-6473621059985848579l,
				"测试单位测试部门测试岗位2wqcd", true, 2, new Date(), new Date(), "desciption",
				-2139993721284966654l, false, 1);
		ArrayList<V3xOrgPost> postList = new ArrayList<V3xOrgPost>();
		ArrayList<V3xOrgPost> postList1 = new ArrayList<V3xOrgPost>();
		postList.add(orgPost1);
		postList.add(orgPost2);
		postList.add(orgPost3);
		postList.add(orgPost4);
		//account.setPostList(postList);// 岗位
		account.setPostList(postList1);// 岗位
		
		
		//设置职务
		V3xOrgLevel orgLevel1 = demo.createV3xOrgLevel(-3106547692452950914l,
				"测试职务1wqcd", 0, true, new Date(), new Date(), "description",
				-6471354914145743078l, false, 1, 1);
		V3xOrgLevel orgLevel2 = demo.createV3xOrgLevel(1746143710321725353l,
				"测试职务2wqcd", 0, true, new Date(), new Date(), "description",
				-6471354914145743078l, false, 2, 1);
		V3xOrgLevel orgLevel3 = demo.createV3xOrgLevel(-2414325906928089396l,
				"测试单位测试部门测试职务1wqcd", 0, true, new Date(), new Date(), "description",
				-2139993721284966654l, false, 2, 1);
		V3xOrgLevel orgLevel4 = demo.createV3xOrgLevel(-4375987250679952180l,
				"测试单位测试部门测试职务2wqcd", 0, true, new Date(), new Date(), "description",
				-6471354914145743078l, false, 3, 1);
		ArrayList<V3xOrgLevel> levelList = new ArrayList<V3xOrgLevel>();
		ArrayList<V3xOrgLevel> levelList1 = new ArrayList<V3xOrgLevel>();
		levelList.add(orgLevel1);
		levelList.add(orgLevel2);
		levelList.add(orgLevel3);
		levelList.add(orgLevel4);
		//account.setLevelList(levelList);// 职务
		account.setLevelList(levelList1);// 职务
		
		//设置人员
		V3xOrgMember orgMember1 = demo.createV3xOrgMember(5725175934914479521l, "单位管理员", true,
				0, Byte.valueOf("1"), true, true, new Date(), new Date(),
				-1730833917365171641l, -1l, -1730833917365171641l, -1l,
				false, 1);
		V3xOrgMember orgMember2 = demo.createV3xOrgMember(6465132461553043189l, "人员B_wq", true,
				2, Byte.valueOf("1"), true, false, new Date(), new Date(),
				5527434053364633089l, -4375987250679952180l, -2139993721284966654l, -6473621059985848579l,
				false, 1);
		V3xOrgMember orgMember3 = demo.createV3xOrgMember(-8517315041651637970l, "人员A_wq", true,
				1, Byte.valueOf("1"), true, false, new Date(), new Date(),
				5527434053364633089l, -4375987250679952180l, -2139993721284966654l, -8916787309264613421l,
				false, 1);
		V3xOrgMember orgMember4 = demo.createV3xOrgMember(-946423943270675968l, "单位管理员", true,
				0, Byte.valueOf("1"), true, true, new Date(), new Date(),
				-2139993721284966654l, -1l, -2139993721284966654l, -1l,
				false, 1);
		V3xOrgMember orgMember5 = demo.createV3xOrgMember(975422430634854530l, "人员4_wq", true,
				4, Byte.valueOf("1"), true, false, new Date(), new Date(),
				7026818100748635576l, 1746143710321725353l, -6471354914145743078l, -682354585021038541l,
				false, 1);
		V3xOrgMember orgMember6 = demo.createV3xOrgMember(-2283092018843660485l, "人员3_wq", true,
				3, Byte.valueOf("1"), true, false, new Date(), new Date(),
				7026818100748635576l, 1746143710321725353l, -6471354914145743078l, -682354585021038541l,
				false, 1);
		V3xOrgMember orgMember7 = demo.createV3xOrgMember(-6885631433644488606l, "人员2_wq", true,
				2, Byte.valueOf("1"), true, false, new Date(), new Date(),
				6021288266847044712l, 1746143710321725353l, -6471354914145743078l, -6937970716481204555l,
				false, 1);
		V3xOrgMember orgMember8 = demo.createV3xOrgMember(842758006892938686l, "人员1_wq", true,
				1, Byte.valueOf("1"), true, false, new Date(), new Date(),
				6021288266847044712l, 1746143710321725353l, -6471354914145743078l, -6937970716481204555l,
				false, 1);
		V3xOrgMember orgMember9 = demo.createV3xOrgMember(-2802500105417375015l, "单位管理员", true,
				0, Byte.valueOf("1"), true, true, new Date(), new Date(),
				-6471354914145743078l, -1l, -6471354914145743078l, -1l,
				false, 1);
		ArrayList<V3xOrgMember> memberList = new ArrayList<V3xOrgMember>();
		ArrayList<V3xOrgMember> memberList1 = new ArrayList<V3xOrgMember>();
		memberList.add(orgMember5);
		/*memberList.add(orgMember1);
		memberList.add(orgMember2);
		memberList.add(orgMember3);
		memberList.add(orgMember4);
		memberList.add(orgMember5);
		memberList.add(orgMember6);
		memberList.add(orgMember7);
		memberList.add(orgMember8);
		memberList.add(orgMember9);*/
		account.setMemberList(memberList);// 人员
		//account.setMemberList(memberList1);// 人员
		
		JetspeedPrincipal principal1 = demo.createJetspeedPrincipal(
				1631775194000475005l, "org.apache.jetspeed.security.InternalUserPrincipalImpl", 1, 0, "/user/ryb_wq",
				new Date(), new Date(), 6465132461553043189l, -2139993721284966654l);
		JetspeedPrincipal principal2 = demo.createJetspeedPrincipal(
				4047614659561866440l, "org.apache.jetspeed.security.InternalUserPrincipalImpl", 1, 0, "/user/rya_wq",
				new Date(), new Date(), -8517315041651637970l, -2139993721284966654l);
		JetspeedPrincipal principal3 = demo.createJetspeedPrincipal(
				6607076874809984178l, "org.apache.jetspeed.security.InternalUserPrincipalImpl", 1, 0, "/user/csdw2_admin_wq",
				new Date(), new Date(), -946423943270675968l, -2139993721284966654l);
		JetspeedPrincipal principal4 = demo.createJetspeedPrincipal(
				6496543608728535391l, "org.apache.jetspeed.security.InternalUserPrincipalImpl", 1, 0, "/user/ry4_wq",
				new Date(), new Date(), 975422430634854530l, -6471354914145743078l);
		JetspeedPrincipal principal5 = demo.createJetspeedPrincipal(
				-33474185583508599l, "org.apache.jetspeed.security.InternalUserPrincipalImpl", 1, 0, "/user/ry3_wq",
				new Date(), new Date(), -2283092018843660485l, -6471354914145743078l);
		JetspeedPrincipal principal6 = demo.createJetspeedPrincipal(
				-7158622000235001112l, "org.apache.jetspeed.security.InternalUserPrincipalImpl", 1, 0, "/user/ry2_wq",
				new Date(), new Date(), -6885631433644488606l, -6471354914145743078l);
		JetspeedPrincipal principal7 = demo.createJetspeedPrincipal(
				5945402171994031882l, "org.apache.jetspeed.security.InternalUserPrincipalImpl", 1, 0, "/user/ry1_wq",
				new Date(), new Date(), 842758006892938686l, -6471354914145743078l);
		JetspeedPrincipal principal8 = demo.createJetspeedPrincipal(
				-3079150233464977701l, "org.apache.jetspeed.security.InternalUserPrincipalImpl", 1, 0, "/user/csdw1_admin_wq",
				new Date(), new Date(), -2802500105417375015l, -6471354914145743078l);
		ArrayList<JetspeedPrincipal> jPList = new ArrayList<JetspeedPrincipal>();
		ArrayList<JetspeedPrincipal> jPList1 = new ArrayList<JetspeedPrincipal>();
		jPList.add(principal1);
		jPList.add(principal2);
		jPList.add(principal3);
		jPList.add(principal4);
		jPList.add(principal5);
		jPList.add(principal6);
		jPList.add(principal7);
		jPList.add(principal8);
		//account.setjPList(jPList);// 用户帐号
		account.setjPList(jPList1);// 用户帐号
		
		JetspeedCredential credential1 = demo
				.createJetspeedCredential(
						5392287299988571897l,
						1631775194000475005l,
						"Aw4Yv/jNVFD4kkre9fDnyI06xHw=",
						0,
						"org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl",
						0, 1, 1, 0, 0, new Date(), new Date(), new Date(),
						new Date());
		JetspeedCredential credential2 = demo
				.createJetspeedCredential(
						500339459374927612l,
						4047614659561866440l,
						"Aw4Yv/jNVFD4kkre9fDnyI06xHw=",
						0,
						"org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl",
						0, 1, 1, 0, 0, new Date(), new Date(), new Date(),
						new Date());
		JetspeedCredential credential3 = demo
				.createJetspeedCredential(
						-66314018764275228l,
						6607076874809984178l,
						"Aw4Yv/jNVFD4kkre9fDnyI06xHw=",
						0,
						"org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl",
						0, 1, 1, 0, 0, new Date(), new Date(), new Date(),
						new Date());
		JetspeedCredential credential4 = demo
				.createJetspeedCredential(
						2429071668994723255l,
						6496543608728535391l,
						"Aw4Yv/jNVFD4kkre9fDnyI06xHw=",
						0,
						"org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl",
						0, 1, 1, 0, 0, new Date(), new Date(), new Date(),
						new Date());
		JetspeedCredential credential5 = demo
				.createJetspeedCredential(
						6300685313583105180l,
						-33474185583508599l,
						"Aw4Yv/jNVFD4kkre9fDnyI06xHw=",
						0,
						"org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl",
						0, 1, 1, 0, 0, new Date(), new Date(), new Date(),
						new Date());
		JetspeedCredential credential6 = demo
				.createJetspeedCredential(
						6429755038281059337l,
						-7158622000235001112l,
						"Aw4Yv/jNVFD4kkre9fDnyI06xHw=",
						0,
						"org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl",
						0, 1, 1, 0, 0, new Date(), new Date(), new Date(),
						new Date());
		JetspeedCredential credential7 = demo
				.createJetspeedCredential(
						-6887670104199508786l,
						5945402171994031882l,
						"Aw4Yv/jNVFD4kkre9fDnyI06xHw=",
						0,
						"org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl",
						0, 1, 1, 0, 0, new Date(), new Date(), new Date(),
						new Date());
		JetspeedCredential credential8 = demo
				.createJetspeedCredential(
						-5932237477300515711l,
						-3079150233464977701l,
						"Aw4Yv/jNVFD4kkre9fDnyI06xHw=",
						0,
						"org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl",
						0, 1, 1, 0, 0, new Date(), new Date(), new Date(),
						new Date());
		ArrayList<JetspeedCredential> jcList = new ArrayList<JetspeedCredential>();
		ArrayList<JetspeedCredential> jcList1 = new ArrayList<JetspeedCredential>();
		jcList.add(credential1);
		jcList.add(credential2);
		jcList.add(credential3);
		jcList.add(credential4);
		jcList.add(credential5);
		jcList.add(credential6);
		jcList.add(credential7);
		jcList.add(credential8);
		//account.setJcList(jcList);// 用户密码
		account.setJcList(jcList1);// 用户密码

		RegistConfig config = content.getServerSystem();
		TransfersManager transfersManager = new TransfersManager();

		try {
			GCCPRequest gccprequest = new GCCPRequest(config).setService(
					GCCPEnum.ServiceEnum.Server_OrgSyn).setRequestObject(
					account);
			String str = transfersManager.sendPackage(gccprequest,new IGCCPResponseHandle<String>() {

						@Override
						public String handle(GCCPResponse gresponse)throws IOException, Exception {
							String restr = (String) gresponse.getResponseObject();
							return restr;
						}
					});
			System.out.println("str:" + str);
			String restar="";
			Map<String, String> map = jsonToObject(str);
			if (String.valueOf(
					//上传组织失败，同步组织机构时与CPP数据冲突
					GCCPCodeEnum.OrgCodeEnum.SYNCORG_VALIFAIL.getKey()).equals(
					map.get("code"))) {
				List<String> list = getList(map.get("accounts"), "--");
				System.out.println(list);
			} else if (String.valueOf(
					//组织机构同步成功
					GCCPCodeEnum.OrgCodeEnum.SYNCORG_SUCCESS.getKey()).equals(
					map.get("code"))) {
				restar = GCCPCodeEnum.OrgCodeEnum.SYNCORG_SUCCESS.getDes();
			} else if (String.valueOf(
					//组织机构同步失败
					GCCPCodeEnum.OrgCodeEnum.SYNCORG_FAIL.getKey()).equals(
					map.get("code"))) {
				restar = GCCPCodeEnum.OrgCodeEnum.SYNCORG_FAIL.getDes()
						+ "，原因：<br>" + map.get("message");
			} else if (String.valueOf(
					//同步未完成,等待进一步处理
					GCCPCodeEnum.OrgCodeEnum.SYNCORG_WAITING.getKey()).equals(
					map.get("code"))) {
				restar = GCCPCodeEnum.OrgCodeEnum.SYNCORG_WAITING.getDes();
			}
			System.out.println("restar:" + restar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

	/**
	 * 
	 * @param id
	 *            主键
	 * @param name
	 *            单位名称
	 * @param code
	 *            编码 ,若没有，则值填写为单位名称
	 * @param shortName
	 *            单位简称
	 * @param sortId
	 *            排序号
	 * @param enabled
	 *            是否有效 false:失效 true：有效
	 * @param adminName
	 *            管理员名称
	 * @param createTime
	 *            生成日期
	 * @param updateTime
	 *            更新日期
	 * @param decription
	 *            描述
	 * @param superior
	 *            上级单位ID 根节点为-1
	 * @param isRoot
	 *            是否是根节点 true:是 false：否   均设置为false
	 * @param status
	 *            单位状态 1. 正常 2.申请停用 3. 申请删除
	 * @param isDeleted
	 *            是否已删除 false未删除 true:删除
	 * @return
	 */
	public V3xOrgAccount createV3xOrgAccount(Long id, String name, String code,
			String shortName, int sortId, Boolean enabled, String adminName,
			Date createTime, Date updateTime, String decription, Long superior,
			Boolean isRoot, Integer status, Boolean isDeleted) {
		V3xOrgAccount account = new V3xOrgAccount();
		account.setId(id);
		account.setName(name);
		account.setCode(code);
		account.setShortname(shortName);
		account.setSortId(sortId);
		account.setEnabled(enabled);
		account.setAdminName(adminName);
		account.setCreateTime(createTime);
		account.setUpdateTime(updateTime);
		account.setDecription(decription);
		account.setSuperior(superior);
		account.setAccessPermission(0);//TODO 访问权限设置为0
		account.setIsRoot(isRoot);//TODO 均设置为false
		account.setStatus(status);
		account.setIsDeleted(isDeleted);
		return account;

	}

	
	/**
	 * 
	 * @param id 主键
	 * @param name 部门名称
	 * @param path 部门路径,表示目录层级 一级目录用0.X 表示  二级目录用0.X.X表示    X为数字  如一级目录 为0.1  该目录下的二级目录则为 0.1.1
	 * @param enabled  是否有效 false：无效 true：有效
	 * @param isInternal 是否内部 true:是  false:不是
	 * @param sortId 排序号
	 * @param createTime 创建时间
	 * @param updateTime 更新时间
	 * @param description 描述
	 * @param orgAccountId 所属单位 
	 * @param isDeleted 是否已删除 true：也删除  false：未删除
 	 * @param status 部门状态  1. 正常  2.申请停用  3. 申请删除 
	 * @param deptShortName 部门简称
	 * @return
	 */
	public V3xOrgDepartment createV3xOrgDepartment(Long id, String name,
			String path, Boolean enabled, Boolean isInternal, Integer sortId,
			Date createTime, Date updateTime, String description,
			Long orgAccountId, Boolean isDeleted, Integer status,
			String deptShortName) {
		V3xOrgDepartment department = new V3xOrgDepartment();
		department.setId(id);
		department.setName(name);
		department.setPath(path);
		department.setEnabled(enabled);
		department.setIsInternal(isInternal);
		department.setSortId(sortId);
		department.setCreateTime(createTime);
		department.setUpdateTime(updateTime);
		department.setDescription(description);
		department.setOrgAccountId(orgAccountId);
		department.setIsDeleted(isDeleted);
		department.setStatus(status);
		department.setDeptShortName(deptShortName);
		return department;
	}

	/**
	 * 
	 * @param id 主键
	 * @param name 名称
	 * @param enabled 是否有效 true：有效 false：无效
	 * @param sortId 排序号
	 * @param createTime 创建日期
	 * @param updateTime 更新日期
	 * @param desciption 描述
	 * @param orgAccountId 所属单位
	 * @param isDeleted 是否已删除 true:也删除 false：未删除
	 * @param status 岗位状态  1. 正常  2.申请停用  3. 申请删除
	 * @return
	 */
	public V3xOrgPost createV3xOrgPost(Long id, String name, Boolean enabled,
			Integer sortId, Date createTime, Date updateTime,
			String desciption, Long orgAccountId, Boolean isDeleted,
			Integer status) {
		V3xOrgPost orgPost = new V3xOrgPost();
		orgPost.setId(id);
		orgPost.setName(name);
		orgPost.setEnabled(enabled);
		orgPost.setSortId(sortId);
		orgPost.setCreateTime(createTime);
		orgPost.setUpdateTime(updateTime);
		orgPost.setDesciption(desciption);
		orgPost.setOrgAccountId(orgAccountId);
		orgPost.setIsDeleted(isDeleted);
		orgPost.setStatus(status);
		return orgPost;
	}

	/**
	 * 
	 * @param id 主键
	 * @param name 名称
	 * @param sortId 排序号 
	 * @param enabled 是否有效 true：有效  false：无效
	 * @param createTime 创建日期 
	 * @param updateTime 更新日期
	 * @param description 描述
	 * @param orgAccountId 所属单位
	 * @param isDeleted 是否已删除 true：删除   false：未删除
	 * @param levelId 级别序号
	 * @param status 级别状态 1. 正常  2.申请停用  3. 申请删除
	 * @return 
	 */
	public V3xOrgLevel createV3xOrgLevel(Long id, String name, Integer sortId,
			Boolean enabled, Date createTime, Date updateTime,
			String description, Long orgAccountId, Boolean isDeleted,
			Integer levelId, Integer status) {
		V3xOrgLevel orgLevel = new V3xOrgLevel();
		orgLevel.setId(id);
		orgLevel.setName(name);
		orgLevel.setSortId(sortId);
		orgLevel.setEnabled(enabled);
		orgLevel.setCreateTime(createTime);
		orgLevel.setUpdateTime(updateTime);
		orgLevel.setDescription(description);
		orgLevel.setOrgAccountId(orgAccountId);
		orgLevel.setIsDeleted(isDeleted);
		orgLevel.setLevelId(levelId);
		orgLevel.setStatus(status);
		return orgLevel;
	}

	/**
	 * 
	 * @param id 主键
	 * @param name 名称
	 * @param enabled 是否有效 true：有效 false：无效
	 * @param sortId 排序号 
	 * @param state 状态 1. 在职 2. 离职
	 * @param isLoginable 是否可登录 true：可以 false：不可以
	 * @param isAdmin 是否管理员 true：是  false：不是
	 * @param createTime 
	 * @param updateTime
	 * @param orgDepartmentId 部门id
	 * @param orgLevelId  所属职务级别
	 * @param orgAccountId 单位id
	 * @param orgPostId 所属岗位
	 * @param isDeleted 是否已删除 true：删除  false：未删除
	 * @param status 人员状态 1. 正常  2.申请停用  3. 申请删除
	 * @return
	 */
	public V3xOrgMember createV3xOrgMember(Long id, String name,
			Boolean enabled, Integer sortId, Byte state,
			Boolean isLoginable, Boolean isAdmin, Date createTime,
			Date updateTime, Long orgDepartmentId, Long orgLevelId,
			Long orgAccountId, Long orgPostId, Boolean isDeleted,Integer status) {
		V3xOrgMember orgMember = new V3xOrgMember();
		orgMember.setId(id);
		orgMember.setName(name);
		orgMember.setEnabled(enabled);
		orgMember.setSortId(sortId);
		orgMember.setState(state);
		orgMember.setIsLoginable(isLoginable);
		orgMember.setIsAdmin(isAdmin);
		orgMember.setCreateTime(createTime);
		orgMember.setUpdateTime(updateTime);
		orgMember.setOrgDepartmentId(orgDepartmentId);
		orgMember.setOrgLevelId(orgLevelId);
		orgMember.setOrgAccountId(orgAccountId);
		orgMember.setOrgPostId(orgPostId);
		orgMember.setIsDeleted(isDeleted);
		orgMember.setStatus(status);
		return orgMember;
	}

	
	/**
	 * 
	 * @param principalId 主键ID
	 * @param className 类名称 默认值未：org.apache.jetspeed.security.InternalUserPrincipalImpl
	 * @param isEnabled 是否有效 0.否   1.是
	 * @param isMappingOnly 是否只许映 0.否 1.是 默认未0
	 * @param fullPath 用户路径 例如/user/csdw2_admin
	 * @param createTime 生成日期
	 * @param updateTime 修改日期
	 * @param entityId 实体ID(人员ID)
	 * @param orgAccountId 单位ID
	 * @return
	 */
	public JetspeedPrincipal createJetspeedPrincipal(long principalId,
			String className, int isEnabled, int isMappingOnly,
			String fullPath, Date createTime, Date updateTime, Long entityId,
			Long orgAccountId) {
		JetspeedPrincipal principal = new JetspeedPrincipal();
		principal.setPrincipalId(principalId);
		principal.setClassName(className);
		principal.setIsMappingOnly(isMappingOnly);
		principal.setIsEnabled(isEnabled);
		principal.setFullPath(fullPath);
		principal.setCreateTime(createTime);
		principal.setUpdateTime(updateTime);
		principal.setEntityId(entityId);
		principal.setOrgAccountId(orgAccountId);
		return principal;
	}

	
	/**
	 * 
	 * @param credentialId 密码ID
	 * @param principalId JetspeedPrincipal的主键
	 * @param columnValue 字段值(密码)
	 * @param type 状态 默认未0
	 * @param className 类名称  默认未org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImp
	 * @param updateRequired 默认未0
	 * @param isEncoded 是否可编码 0.否    1.是
	 * @param isEnabled 是否有效 0.否    1.是
	 * @param authFailures 权限失效 默认未0
	 * @param isExpired 是否过期 0.否    1.是
	 * @param createTime 生成日期
	 * @param updateTime 修改日期
	 * @param preAuthDate上次授权日期
	 * @param lastAuthDate 最后授权日期
	 * @return
	 */
	public JetspeedCredential createJetspeedCredential(long credentialId,
			long principalId, String columnValue, int type, String className,
			int updateRequired, int isEncoded, int isEnabled, int authFailures,
			int isExpired, Date createTime, Date updateTime, Date preAuthDate,
			Date lastAuthDate) {
		JetspeedCredential credential = new JetspeedCredential();
		credential.setCredentialId(credentialId);
		credential.setPrincipalId(principalId);
		credential.setColumnValue(columnValue);
		credential.setType(type);
		credential.setClassName(className);
		credential.setUpdateRequired(updateRequired);
		credential.setIsEncoded(isEncoded);
		credential.setIsEnabled(isEnabled);
		credential.setAuthFailures(authFailures);
		credential.setIsExpired(isExpired);
		credential.setCreateTime(createTime);
		credential.setUpdateTime(updateTime);
		credential.setPreAuthDate(preAuthDate);
		credential.setLastAuthDate(lastAuthDate);
		return credential;
	}
	
	private static Map jsonToObject(String s) {
		JSONDecoder de = new JSONDecoder(false);
		return de.decode(s, LinkedHashMap.class);
	}

	private static List<String> getList(String str1, String str2) {
		List<String> list = new ArrayList<String>();
		String[] strarr = str1.split(str2);
		for (int i = 0; i < strarr.length; i++) {
			if (strarr[i] != null)
				list.add(strarr[i]);
		}
		return list;

	}

}
