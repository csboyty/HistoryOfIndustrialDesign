package com.zhongyi.hid.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ServerStatusServlet extends HttpServlet{

	public void init(ServletConfig servletConfig)  throws ServletException{
		super.init(servletConfig);
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		try{
			writer.print("Server live!");
			writer.flush();
		}finally{
			writer.close();
		}
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws IOException{
		doGet(request,response);
	}
}
