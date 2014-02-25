<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="wireless.title" /></title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link href="html/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link href="favicon.ico" rel="icon" type="image/x-icon" />
  <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>

<body>

<jsp:include page="header.jsp"/>

<table>
  <tr>
  	<td>
	    <img src="<s:text name="network.logo" />" 
			 width="<s:text name="network.logo.width" />" 
			 height="<s:text name="network.logo.height" />" 
			 alt="<s:text name="network.logo.alt" />" />
	</td>
    <td>
	  <jsp:include page="logo_small.jsp" />
    </td>
    <td>
      <h2><s:text name="wireless.header" /></h2>
    </td>
  </tr>
</table>

<!-- Status -->
<hr />
<h4><s:text name="header.status" /></h4>

<table>

<tr>
	<td>
		<s:textarea name="status" cols="100" rows="7" readonly="true" theme="simple" />
	</td>
</tr>

<tr>
	<td>
		<s:a action="Wireless_populate"><s:property value="getText('button.refresh')" /></s:a>
	</td>
</tr>

</table>

<!-- Configuration -->
<hr />
<h4><s:text name="header.configuration" /></h4>
<s:actionerror />
<s:form action="WirelessSave_save" theme="simple">

	<s:hidden name="wirelessMode" />
	<s:hidden name="wirelessKeyMgmt" />

	<!-- hidden store the networkList -->
	<s:iterator value="networkList" status="stat">
	  <s:hidden name="networkList[%{#stat.index}]" 
	  			value="%{networkList[#stat.index]}" />
	</s:iterator>

<table>

<jsp:include page="interface-non-editable.jsp"/>

<tr>
<td align="right">
  <s:text name="interface.label.wirelessMode" />
</td>
<td>
  <font color="blue"><s:property value="wirelessMode" /></font>
</td>
</tr>
<tr>
<td align="right">
  <s:text name="interface.label.wirelessKeyMgmt" />
</td>
<td>
  <font color="blue"><s:property value="wirelessKeyMgmt" /></font>
</td>
</tr>
<tr>
<td align="right">
  <s:text name="interface.label.wirelessEssid" />
  <img src='struts/tooltip.gif'
	   title="<s:property value="getText('interface.tooltip.wirelessEssid')" />"
       alt="<s:property value="getText('interface.tooltip.wirelessEssid')" />" />
</td>
<td align="left">
  <s:select name="wirelessEssid" list="networkList" />
</td>
</tr>
<tr>
<td />
<td align="left">
  <s:textfield name="wirelessEssidOther" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
  <s:text name="interface.label.wirelessWpaPsk" />
  <img src='struts/tooltip.gif'
	   title="<s:property value="getText('interface.tooltip.wirelessWpaPsk')" />"
       alt="<s:property value="getText('interface.tooltip.wirelessWpaPsk')" />" />
</td>
<td>
  <s:textfield name="wirelessWpaPsk" cssClass="size-300px" />
</td>
</tr>
<tr>

<jsp:include page="interface-editable.jsp"/>

<td colspan="2" align="right">
  <s:reset key="button.reset" />
  <s:submit key="button.save" action="WirelessSave_save" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
  <s:submit key="button.saveAndReboot" action="WirelessSave_saveAndReboot" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
  <s:submit key="button.refreshNetworkList" action="Wireless_populate" />
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
  <s:submit key="button.interfaceUp" action="Wireless_up" />
  <s:submit key="button.interfaceDown" action="Wireless_down" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
  <s:submit key="button.interfaceDownUp" action="Wireless_downUp" />
</td>
</tr>
</table>
</s:form>

<hr />
<h4><s:text name="header.notes" /></h4>
<p><s:text name="wireless.notes.desc" /></p>
<ul>
<s:text name="wireless.notes" />
</ul>

<hr />
<jsp:include page="footer.jsp"/>

</body>
</html>
