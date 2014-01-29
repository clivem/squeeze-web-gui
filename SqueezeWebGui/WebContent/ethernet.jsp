<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="ethernet.title" /></title>
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
      <h2><s:text name="ethernet.header" /></h2>
    </td>
  </tr>
</table>

<hr />
<h4><s:text name="header.status" /></h4>
<s:form action="Ethernet_populate" theme="simple" >
<table>
<tr>
<td>
  <s:textarea name="status" cols="100" rows="7" readonly="true" />
</td>
</tr>
<tr>
<td align="right">
  <s:submit action="Ethernet_populate" key="button.refresh" />
</td>
</tr>
</table>
</s:form>

<hr />
<h4><s:text name="header.configuration" /></h4>
<s:actionerror />
<s:form action="EthernetSave_save" theme="simple">
					
<table>

<jsp:include page="interface-non-editable.jsp"/>
<jsp:include page="interface-editable.jsp"/>

<tr>
<td colspan="2" align="right">
  <s:reset key="button.reset" />
  <s:submit action="EthernetSave_save" key="button.save" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
  <s:submit action="EthernetSave_saveAndReboot" key="button.saveAndReboot" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
  <s:checkbox name="cbForceReboot" />
  <s:text name="cb.forceReboot" />
</td>
</tr>
<tr>
<td>&nbsp;</td>
<td>&nbsp;</td>
</tr>
<tr>
<td colspan="2" align="right">
  <s:submit key="button.interfaceUp" action="Ethernet_up" />
  <s:submit key="button.interfaceDown" action="Ethernet_down" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
  <s:submit key="button.interfaceDownUp" action="Ethernet_downUp" />
</td>
</tr>
</table>
</s:form>

<hr />
<h4><s:text name="header.notes" /></h4>
<p><s:text name="ethernet.notes.desc" /></p>
<ul><s:text name="ethernet.notes" /></ul>

<hr />
<jsp:include page="Footer.jsp"/>

</body>
</html>
