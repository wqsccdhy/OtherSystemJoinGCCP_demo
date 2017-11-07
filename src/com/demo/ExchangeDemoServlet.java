package com.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.gccp.Content;
import com.example.gccp.exchange.EdocDataOperation;
import com.example.gccp.exchange.SendDataMain;
import com.seeyon.v3x.common.utils.UUIDLong;
import com.seeyon.v3x.plugin.gccp.common.GCCPEnum;
import com.seeyon.v3x.plugin.gccp.exception.GCCPException;
import com.seeyon.v3x.plugin.gccp.exchange.domain.GCCPRequestDO;
import com.seeyon.v3x.plugin.gccp.standard.StandardData;

public class ExchangeDemoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3681979618970435231L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		SendDataMain dataMain = new SendDataMain();
		String resulr = "";
		try {
			resulr = dataMain.sendDataToTarger();
		} catch (Exception e) {
			resulr = "error";
			e.printStackTrace();
		} finally {
			response.setCharacterEncoding("utf-8");
			PrintWriter pw = response.getWriter();
			pw.print(resulr);
		}

	}

}
