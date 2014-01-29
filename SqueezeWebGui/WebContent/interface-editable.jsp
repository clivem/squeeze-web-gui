<%@ taglib uri="/struts-tags" prefix="s" %>

<tr>
<td align="right">
	<s:text name="interface.label.ipAddress" />
	<img src='struts/tooltip.gif'
	     title="<s:property value="getText('interface.tooltip.ipAddress')" />"
         alt="<s:property value="getText('interface.tooltip.ipAddress')" />" />
</td>
<td>
	<s:textfield name="ipAddr0" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="interface.label.netmask" />
	<img src='struts/tooltip.gif'
	     title="<s:property value="getText('interface.tooltip.netmask')" />"
         alt="<s:property value="getText('interface.tooltip.netmask')" />" />
</td>
<td>
	<s:textfield name="netmask0" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="interface.label.gateway" />
	<img src='struts/tooltip.gif'
	     title="<s:property value="getText('interface.tooltip.gateway')" />"
         alt="<s:property value="getText('interface.tooltip.gateway')" />" />
</td>
<td>
	<s:textfield name="gateway0" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
  <s:text name="interface.label.dns1" />
  <img src='struts/tooltip.gif'
	  title="<s:property value="getText('interface.tooltip.dns1')" />"
      alt="<s:property value="getText('interface.tooltip.dns1')" />" />
</td>
<td>
	<s:textfield name="dns1" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
  <s:text name="interface.label.dns2" />
  <img src='struts/tooltip.gif'
	  title="<s:property value="getText('interface.tooltip.dns2')" />"
      alt="<s:property value="getText('interface.tooltip.dns2')" />" />
</td>
<td>
	<s:textfield name="dns2" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
  <s:text name="interface.label.dns3" />
  <img src='struts/tooltip.gif'
	  title="<s:property value="getText('interface.tooltip.dns3')" />"
      alt="<s:property value="getText('interface.tooltip.dns3')" />" />
</td>
<td>
	<s:textfield name="dns3" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
  <s:text name="interface.label.domain" />
  <img src='struts/tooltip.gif'
	  title="<s:property value="getText('interface.tooltip.domain')" />"
      alt="<s:property value="getText('interface.tooltip.domain')" />" />
</td>
<td>
	<s:textfield name="domain" cssClass="size-300px" />
</td>
</tr>			
