<%@ taglib uri="/struts-tags" prefix="s" %>

<s:hidden name="status" />

<s:hidden name="name" />
<s:hidden name="type" />
<s:hidden name="uuid" />
<s:hidden name="hwAddr" />
<s:hidden name="zone" />
<s:hidden name="bootProto" />
<!-- <s:hidden name="onBoot" /> -->

<tr>
<td align="right">
<s:text name="interface.label.name" />
</td>
<td><font color="blue"><s:property value="name" /></font></td>
</tr>
<tr>
<td align="right">
<s:text name="interface.label.type" />
</td>
<td><font color="blue"><s:property value="type" /></font></td>
</tr>
<tr>
<td align="right">
<s:text name="interface.label.uuid" />
</td>
<td><font color="blue"><s:property value="uuid" /></font></td>
</tr>
<tr>
<td align="right">
<s:text name="interface.label.mac" />
</td>
<td><font color="blue"><s:property value="hwAddr" /></font></td>
</tr>
<tr>
<td align="right">
<s:text name="interface.label.firewallZone" />
</td>
<td><font color="blue"><s:property value="zone" /></font></td>
</tr>
<tr>
<td align="right">
<s:text name="interface.label.bootProto" />
</td>
<td><font color="blue"><s:property value="bootProto" /></font></td>
</tr>
<tr>
<td align="right">
<s:text name="interface.label.onBoot" />
</td>
<td>
  <!-- <font color="blue"><s:property value="onBoot" /></font> -->
  <s:checkbox name="onBoot" />
</td>
</tr>
