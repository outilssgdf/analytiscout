<%@ page language="java" contentType="application/js;charset=utf-8" pageEncoding="utf-8" %>

<%@ page import="java.net.URL" %>
<%@ page import="org.leplan73.outilssgdf.servlet.common.ServerProperties" %>

<%
URL url = ServerProperties.getUrl();

response.setHeader("Cache-Control", "no-store");
response.setHeader("Pragma", "no-cache");
%>

param = {
	url: "<%= url %>/swagger.json"
};
