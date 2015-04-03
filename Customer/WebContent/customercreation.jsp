<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<f:view>
<h:form id="helloForm">
<Center>
<H2>Customer Creation Page</H2>
</Br></Br>
<table>
<tr>
<td>
<h:outputLabel for="input1">
<h:outputText id="nameLabel" value="Customer Name"/>
</h:outputLabel>
</td>
<td>
<h:inputText id="input1" value="#{logonBean.name}" size="20"/>
</td>
</tr>
<tr>
<td>
<h:outputLabel for="input2">
<h:outputText id="dobLabel" value="Date of Birth"/>
</h:outputLabel>
</td>
<td>
<h:inputSecret id="input2" value="#{logonBean1.password}" size="20"/>
</td>
</tr>
<tr>
<td></td>
<td>
<h:commandButton id="logon" action="#{logonBean1.validate}" value="Logon">
</h:commandButton>
</td>
</tr>
</table>
</Center>
</h:form>
</f:view>
</body>
</html>