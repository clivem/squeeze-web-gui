<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="squeezeserver.title" /></title>
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
	    <img src="<s:text name="squeezeserver.logo" />" 
			 width="<s:text name="squeezeserver.logo.width" />" 
			 height="<s:text name="squeezeserver.logo.height" />" 
			 alt="<s:text name="squeezeserver.logo.alt" />" />
	</td>
    <td>
	  <jsp:include page="logo_small.jsp" />
    </td>
    <td>
      <h2><s:text name="squeezeserver.header" /></h2>
    </td>
  </tr>
</table>

<hr />

<h4><s:text name="header.service" /></h4>
<s:form action="SqueezeServer_populate" theme="simple" >
<table>
<tr>
<td>
  <s:textarea name="status" cols="100" rows="8" readonly="true" />
</td>
</tr>
<tr>
<td align="right">
  <s:submit action="SqueezeServer_populate" key="button.refresh" />
  <s:submit action="SqueezeServer_enableService" key="button.enable" />
  <s:submit action="SqueezeServer_disableService" key="button.disable" />
  <s:submit action="SqueezeServer_startService" key="button.start" />
  <s:submit action="SqueezeServer_stopService" key="button.stop" />
  <s:submit action="SqueezeServer_restartService" key="button.restart" />
</td>
</tr>
<tr>
<td align="right">
  <s:submit action="SqueezeServer_enableAndStartService" key="button.enableAndStart" />
  <s:submit action="SqueezeServer_disableAndStopService" key="button.disableAndStop" />
</td>
</tr>
</table>
</s:form>
<hr />

<h4><s:text name="header.configuration" /></h4>
<p><a href="http://${pageContext.request.serverName}:9000/" target="blank_">
SqueezeServer Web Interface</a>
</p>
<hr />

<h4><s:text name="header.notes" /></h4>
<p><s:text name="squeezeserver.notes.desc" /></p>
<hr />

<jsp:include page="Footer.jsp"/>

</body>
</html>
