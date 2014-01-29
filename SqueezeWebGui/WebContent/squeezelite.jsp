<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="squeezelite.title" /></title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link href="html/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link href="favicon.ico" rel="icon" type="image/x-icon" />
  <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>
<body>

<jsp:include page="Header.jsp"/>

<table>
  <tr>
    <td>
	  <jsp:include page="logo_small.jsp" />
    </td>
    <td>
      <h2><s:text name="squeezelite.header" /></h2>
    </td>
  </tr>
</table>

<hr />
<h4><s:text name="header.service" /></h4>
<s:form action="Squeezelite_populate" theme="simple" >
<table>
<tr>
<td>
	<s:textarea name="status" cols="100" rows="4" readonly="true" />
</td>
</tr>
<tr>
<td align="right">
	<s:submit action="Squeezelite_populate" key="button.refresh" />
	<s:submit action="Squeezelite_enableService" key="button.enable" />
	<s:submit action="Squeezelite_disableService" key="button.disable" />
	<s:submit action="Squeezelite_startService" key="button.start" />
	<s:submit action="Squeezelite_stopService" key="button.stop" />
	<s:submit action="Squeezelite_restartService" key="button.restart" />
</td>
</tr>
<tr>
<td align="right">
	<s:submit action="Squeezelite_enableAndStartService" key="button.enableAndStart" />
	<s:submit action="Squeezelite_disableAndStopService" key="button.disableAndStop" />
</td>
</tr>
</table>
</s:form>

<hr />
<h4><s:text name="header.configuration" /></h4>
<s:actionerror />
<s:form action="SqueezeliteSave_save" theme="simple" >
<table>
<tr>
<td align="right">
	<s:text name="squeezelite.label.name" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.name')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.name')" />" />
</td>
<td>
	<s:textfield name="name" cssClass="size-300px" />
</td>      
</tr>
<tr>
<td align="right">
    <s:text name="squeezelite.label.mac" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.mac')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.mac')" />" />
</td>
<td>
	<s:textfield name="mac" cssClass="size-300px" />
</td>
</tr>
<tr>
<td />
<td>
	<s:checkbox name="defaultMac" />
    <s:text name="squeezelite.label.mac.default" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.audioDev" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.audioDev')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.audioDev')" />" />
</td>
<td>
	<s:select name="audioDev" list="audioDevList" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.maxRate" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.maxRate')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.maxRate')" />" />
</td>
<td>
	<s:textfield name="maxRate" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.logFile" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.logFile')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.logFile')" />" />
</td>
<td>
	<s:textfield name="logFile" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.logLevel" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.logLevel')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.logLevel')" />" />
</td>
<td>
	<s:textfield name="logLevel" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.priority" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.priority')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.priority')" />" />
</td>
<td>
	<s:select name="priority" list="priorityList" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.buffer" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.buffer')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.buffer')" />" />
</td>
<td>
	<s:textfield name="buffer" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.codec" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.codec')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.codec')" />" />
</td>
<td>
	<s:textfield name="codec" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.alsaParams" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.alsaParams')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.alsaParams')" />" />
</td>
<td>
	<s:textfield name="alsaParams" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
    <s:text name="squeezelite.label.upsample" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.upsample')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.upsample')" />" />
</td>
<td>
	<s:checkbox name="upsample" />
	<s:textfield name="upsampleOptions" cssClass="size-100px" />
    <s:a href="squeezelite_upsample.jsp">Info</s:a>
</td>
</tr>
<tr>
<td align="right">
    <s:text name="squeezelite.label.dop" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.dop')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.dop')" />" />
</td>
<td>
	<s:checkbox name="dop" />
	<s:textfield name="dopOptions" cssClass="size-100px" />
</td>
</tr>
<tr>
<td align="right">
    <s:text name="squeezelite.label.visulizer" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.visulizer')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.visulizer')" />" />
</td>
<td>
	<s:checkbox name="visulizer" />
</td>
</tr>
<tr>
<td align="right">
    <s:text name="squeezelite.label.options" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.options')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.options')" />" />
</td>
<td>
	<s:textfield name="options" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="squeezelite.label.serverIp" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('squeezelite.tooltip.serverIp')" />"
      	 alt="<s:property value="getText('squeezelite.tooltip.serverIp')" />" />
</td>
<td>
	<s:textfield name="serverIp" cssClass="size-300px" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
	<s:reset key="button.reset"/>
	<s:submit action="SqueezeliteSave_save" 
			  key="button.save" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
	<s:submit action="SqueezeliteSave_saveAndRestart" 
			  key="button.saveAndConditionallyRestart" />
</td>
</tr>
</table>
		
<!-- hidden store the audioDevList -->
<s:iterator value="audioDevList" status="stat">
  <s:hidden name="audioDevList[%{#stat.index}]" value="%{audioDevList[#stat.index]}" />
</s:iterator>

<!-- hidden store the status -->
<s:hidden name="status" />
	
</s:form>

<hr />
<h4><s:text name="header.notes" /></h4>
<p><s:text name="squeezelite.notes.desc" /></p>
<ul>
<s:text name="squeezelite.notes" />
</ul>

<hr />
<jsp:include page="Footer.jsp"/>

</body>
</html>
