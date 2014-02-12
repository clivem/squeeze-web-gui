<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="configuration.title" /></title>
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
      <h2><s:text name="configuration.header" /></h2>
    </td>
  </tr>
</table>

<table class="copyright">
<tr>
<td align="right" >OS Version:</td>
<td><s:property value="csosVersion" /></td>
</tr>
<tr>
<td align="right" >Fedora Version:</td>
<td><s:property value="fedoraVersion" /></td>
</tr>
</table>

<hr />

<h4><s:text name="header.configuration" /></h4>
<s:form action="Configuration_save" theme="simple" >
<table>

<tr>
<td align="right" >
    <s:text name="configuration.label.hostName" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('configuration.tooltip.hostName')" />"
      	 alt="<s:property value="getText('configuration.tooltip.hostName')" />" />
</td>
<td><s:textfield name="hostName" cssClass="size-300px" /></td>
</tr>

<tr>
<td align="right" >
    <s:text name="configuration.label.sambaNetbiosName" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('configuration.tooltip.sambaNetbiosName')" />"
      	 alt="<s:property value="getText('configuration.tooltip.sambaNetBiosName')" />" />
</td>
<td><s:textfield name="sambaNetbiosName" cssClass="size-300px" /></td>
</tr>

<tr>
<td align="right" >
    <s:text name="configuration.label.sambaWorkgroup" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('configuration.tooltip.sambaWorkgroup')" />"
      	 alt="<s:property value="getText('configuration.tooltip.sambaWorkgroup')" />" />
</td>
<td><s:textfield name="sambaWorkgroup" cssClass="size-300px" /></td>
</tr>

<tr>
<td align="right">
	<s:text name="configuration.label.timeZone" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('configuration.tooltip.timeZone')" />"
      	 alt="<s:property value="getText('configuration.tooltip.timeZone')" />" />
</td>
<td>
	<s:select name="timeZone" list="timeZoneList" />
</td>
</tr>

<tr>
<td align="right">
	<s:text name="configuration.label.locale" />
	<img src='struts/tooltip.gif'
	  	 title="<s:property value="getText('configuration.tooltip.locale')" />"
      	 alt="<s:property value="getText('configuration.tooltip.locale')" />" />
</td>
<td>
	<s:select name="systemLocale"
        list="systemLocaleList"
        listKey="locale"
        listValue="description"
        emptyOption="false"
        headerKey=""
        headerValue="None" />
</td>
</tr>

<tr>
<td colspan="2" align="right">
	<s:reset key="button.reset"/>
	<s:submit action="Configuration_save" key="button.submit" />
</td>
</tr>

</table>
</s:form>

<hr />
<h4><s:text name="header.notes" /></h4>
<p><s:text name="configuration.notes.desc" /></p>

<hr />
<jsp:include page="Footer.jsp"/>

</body>
</html>
