<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="storage.title" /></title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link href="html/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link href="favicon.ico" rel="icon" type="image/x-icon" />
  <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>
<body>

<!-- Header -->
<jsp:include page="Header.jsp"/>

<!-- Title -->
<table>
  <tr>
	<td>
	    <img src="<s:text name="storage.logo" />" 
			 width="<s:text name="storage.logo.width" />" 
			 height="<s:text name="storage.logo.height" />" 
			 alt="<s:text name="storage.logo.alt" />" />
	</td>
    <td>
      <jsp:include page="logo_small.jsp" />
    </td>
    <td>
      <h2><s:text name="storage.header.main" /></h2>
    </td>
  </tr>
</table>

<!-- Mounted File Systems -->
<hr />
<h4><s:text name="storage.header.mountedFileSystems" /></h4>
<s:form action="Storage_populate" theme="simple">
<table>
<tr>
<td>
	<s:textarea name="fsStatus" cols="100" rows="6" readonly="true" />
</td>
</tr>
<tr>
<td align="right">
	<s:submit action="Storage_populate" value="Refresh" />
</td>
</tr>
</table>
</s:form>

<hr />

<h4><s:text name="storage.header.userControlledMounts" /></h4>
<s:form action="Storage_populate" theme="simple">
<table>
<thead>
<tr>
	<td>
		<s:text name="storage.label.spec" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.spec')" />"
			 alt="<s:property value="getText('storage.tooltip.spec')" />" />
	</td>
	<td>
		<s:text name="storage.label.file" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.file')" />"
			 alt="<s:property value="getText('storage.tooltip.file')" />" />
	</td>
	<td>
		<s:text name="storage.label.vfsType" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.vfsType')" />"
			 alt="<s:property value="getText('storage.tooltip.vfsType')" />" />
	</td>
	<td>
		<s:text name="storage.label.mountOptions" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.mountOptions')" />"
			 alt="<s:property value="getText('storage.tooltip.mountOptions')" />" />
	</td>
	<td>
		<s:text name="storage.label.action" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.action')" />"
			 alt="<s:property value="getText('storage.tooltip.action')" />" />
	</td>
</tr>
</thead>
<s:iterator value="mountList" status="ind">
<tr>
	<td><s:textfield name="mountList[%{#ind.index}].spec" value="%{spec}" readonly="true" /></td>
	<td><s:textfield name="mountList[%{#ind.index}].mountPoint" value="%{mountPoint}" readonly="true" /></td>
	<td><s:textfield name="mountList[%{#ind.index}].fsType" value="%{fsType}" readonly="true" cssClass="size-75px" /></td>
	<td><s:textfield name="mountList[%{#ind.index}].options" value="%{options}" readonly="true" /></td>
	<td><s:select name="mountList[%{#ind.index}].action" value="%{action}" list="mountActionList" /></td>
</tr>
</s:iterator>

<tr>
<td colspan="5" align="right">
	<s:reset />
	<s:submit action="Storage_unmount" key="button.submit" />
</td>
</tr>

</table>
</s:form>

<!-- Mount Local File System -->
<hr />
<h4><s:text name="storage.header.mountLocalFs" /></h4>
<s:form action="Storage_mountLocalFs" theme="simple">
<table>
<thead>
<tr>
	<td>
		<s:text name="storage.label.spec" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.spec')" />"
			 alt="<s:property value="getText('storage.tooltip.spec')" />" />
	</td>
	<td>
		<s:text name="storage.label.file" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.file')" />"
			 alt="<s:property value="getText('storage.tooltip.file')" />" />
	</td>
	<td>
		<s:text name="storage.label.vfsType" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.vfsType')" />"
			 alt="<s:property value="getText('storage.tooltip.vfsType')" />" />
	</td>
	<td>
		<s:text name="storage.label.mountOptions" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.mountOptions')" />"
			 alt="<s:property value="getText('storage.tooltip.mountOptions')" />" />
	</td>
</tr>
</thead>
<tr>
	<td><s:select name="localFsPartition" list="availablePartitions" cssClass="size-150px" /></td>
	<td><s:select name="localFsMountPoint" list="mountPoints" cssClass="size-150px" /></td>
	<td><s:select name="localfsType" list="localFsTypes" cssClass="size-75px" /></td>
	<td><s:textfield name="localFsMountOptions" cssClass="size-300px" /></td>
</tr>

<tr>
<td colspan="4" align="right">
	<s:reset />
	<s:submit action="Storage_mountLocalFs" key="button.mount" />
</td>
</tr>

</table>
</s:form>

<!-- Mount Remote File System -->
<hr />
<h4><s:text name="storage.header.mountRemoteFs" /></h4>
<s:form action="Storage_mountRemoteFs" theme="simple">
<table>
<thead>
<tr>
	<td>
		<s:text name="storage.label.spec" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.spec')" />"
			 alt="<s:property value="getText('storage.tooltip.spec')" />" />
	</td>
	<td>
		<s:text name="storage.label.file" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.file')" />"
			 alt="<s:property value="getText('storage.tooltip.file')" />" />
	</td>
	<td>
		<s:text name="storage.label.vfsType" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.vfsType')" />"
			 alt="<s:property value="getText('storage.tooltip.vfsType')" />" />
	</td>
	<td>
		<s:text name="storage.label.mountOptions" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.mountOptions')" />"
			 alt="<s:property value="getText('storage.tooltip.mountOptions')" />" />
	</td>
</tr>
</thead>
<tr>
	<td><s:textfield name="remoteFsPartition" cssClass="size-150px" /></td>
	<td><s:select name="remoteFsMountPoint" list="mountPoints" cssClass="size-150px" /></td>
	<td><s:select name="remotefsType" list="remoteFsTypes" cssClass="size-75px" /></td>
	<td><s:textfield name="remoteFsMountOptions" cssClass="size-300px" /></td>
</tr>

<tr>
<td colspan="4" align="right">
	<s:reset />
	<s:submit action="Storage_mountRemoteFs" key="button.mount" />
</td>
</tr>

</table>
</s:form>

<!-- Notes -->
<hr />
<h4><s:text name="header.notes" /></h4>
<p><s:text name="storage.notes.desc" /></p>

<!-- Footer -->
<hr />
<jsp:include page="Footer.jsp"/>

</body>
</html>
