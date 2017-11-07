package com.example.gccp.register;

import com.example.gccp.Content;
import com.seeyon.v3x.plugin.gccp.common.ResponseResult;
import com.seeyon.v3x.plugin.gccp.register.manager.ClientRegisterManager;

public class RegisterMain {

    public static void main(String[] args) throws Exception {
        Content content = Content.getInstance();
        ResponseResult<?> result = ClientRegisterManager.sendRegisterInfoToIsnc(content.getServerSystem(),
                content.getSelfSystem());
        boolean success = result.isSuccess();
        if (success) {
            System.out.println("注册成功！");
        } else {
            System.out.println("注册失败，原因：" + result.getMessage());
        }
    }
}
