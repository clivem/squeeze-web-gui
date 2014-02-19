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

<s:actionerror />

<!-- Mounted File Systems -->
<hr />
<h4><s:text name="storage.header.mountedFileSystems" /></h4>
<s:form action="Storage_populate" theme="simple">
<table>

<thead>
<tr>
	<td>
		<s:text name="storage.label.file" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.file')" />"
			 alt="<s:property value="getText('storage.tooltip.file')" />" />
	</td>
	<td>
		<s:text name="storage.label.spec" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.spec')" />"
			 alt="<s:property value="getText('storage.tooltip.spec')" />" />
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

<s:iterator value="systemMountList">
<tr>
	<td><s:textfield name="mountPoint" readonly="true" cssClass="size-200px" /></td>
	<td><s:textfield name="spec" readonly="true" cssClass="size-300px" /></td>
	<td><s:textfield name="fsType" readonly="true" cssClass="size-75px" /></td>
	<td><s:textfield name="options" readOnly="true" cssClass="size-300px" /></td>
</tr>
</s:iterator>

<tr>
<td colspan="4" align="right">
	<s:submit action="Storage_populate" value="Refresh" />
</td>
</tr>
</table>
</s:form>

<hr />

<h4><s:text name="storage.header.userControlledMounts" /></h4>
<s:form action="StorageMount_action" theme="simple">
<table>
<thead>
<tr>
	<td width="200">
		<s:text name="storage.label.file" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.file')" />"
			 alt="<s:property value="getText('storage.tooltip.file')" />" />
	</td>
	<td width="300">
		<s:text name="storage.label.spec" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.spec')" />"
			 alt="<s:property value="getText('storage.tooltip.spec')" />" />
	</td>
	<td width="75">
		<s:text name="storage.label.vfsType" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.vfsType')" />"
			 alt="<s:property value="getText('storage.tooltip.vfsType')" />" />
	</td>
	<td width="300">
		<s:text name="storage.label.mountOptions" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.mountOptions')" />"
			 alt="<s:property value="getText('storage.tooltip.mountOptions')" />" />
	</td>
	<td width="100">
		<s:text name="storage.label.action" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.action')" />"
			 alt="<s:property value="getText('storage.tooltip.action')" />" />
	</td>
	<!-- 
	<td width="75">
		<s:text name="storage.label.persist" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.persist')" />"
			 alt="<s:property value="getText('storage.tooltip.persist')" />" />
	</td>
	-->
</tr>
</thead>

<s:iterator value="userMountList" status="ind">
<tr>
	<td><s:textfield name="userMountList[%{#ind.index}].mountPoint" value="%{mountPoint}" readonly="true" cssClass="size-200px" /></td>
	<td><s:textfield name="userMountList[%{#ind.index}].spec" value="%{spec}" readonly="true" cssClass="size-300px" /></td>
	<td><s:textfield name="userMountList[%{#ind.index}].fsType" value="%{fsType}" readonly="true" cssClass="size-75px" /></td>
	<td><s:textfield name="userMountList[%{#ind.index}].options" value="%{options}" cssClass="size-300px" /></td>
	<td><s:select name="userMountList[%{#ind.index}].action" value="%{action}" list="userMountList[#ind.index].actionList" /></td>
	<!-- 
	<td><s:checkbox name="userMountList[%{#ind.index}].persist" cssClass="size-75px" /></td>
	-->
</tr>
<tr>
	<td>In /etc/fstab: <s:property value="userMountList[#ind.index].fstabEntry" /></td>
	<td>Mounted: <s:property value="userMountList[#ind.index].mounted" /></td>
</tr>
</s:iterator>

<s:iterator value="fstabUserMountList" status="ind">
<tr>
	<td><s:textfield name="fstabUserMountList[%{#ind.index}].mountPoint" value="%{mountPoint}" readonly="true" cssClass="size-200px" /></td>
	<td><s:textfield name="fstabUserMountList[%{#ind.index}].spec" value="%{spec}" readonly="true" cssClass="size-300px" /></td>
	<td><s:textfield name="fstabUserMountList[%{#ind.index}].fsType" value="%{fsType}" readonly="true" cssClass="size-75px" /></td>
	<td><s:textfield name="fstabUserMountList[%{#ind.index}].options" value="%{options}" cssClass="size-300px" /></td>
	<td><s:select name="fstabUserMountList[%{#ind.index}].action" value="%{action}" list="fstabUserMountList[#ind.index].actionList" /></td>
	<!-- 
	<td><s:checkbox name="fstabUserMountList[%{#ind.index}].persist" cssClass="size-75px" /></td>
	-->
</tr>
<tr>
	<td>In /etc/fstab: <s:property value="fstabUserMountList[#ind.index].fstabEntry" /></td>
	<td>Mounted: <s:property value="fstabUserMountList[#ind.index].mounted" /></td>
</tr>
</s:iterator>

<tr>
<td colspan="4" align="right">
	<s:submit action="StorageMount_action" key="button.submit" />
	<s:reset />
	<s:submit action="Storage_populate" key="button.refresh" />
</td>
</tr>

</table>
</s:form>

<!-- Mount Local File System -->
<hr />
<h4><s:text name="storage.header.mountLocalFs" /></h4>
<s:form action="StorageMountLocal_mountLocalFs" theme="simple">
<table>
<thead>
<tr>
	<td width="200">
		<s:text name="storage.label.file" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.file')" />"
			 alt="<s:property value="getText('storage.tooltip.file')" />" />
	</td>
	<td width="300">
		<s:text name="storage.label.partition" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.partition')" />"
			 alt="<s:property value="getText('storage.tooltip.partition')" />" />
	</td>
	<td width="75">
		<s:text name="storage.label.vfsType" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.vfsType')" />"
			 alt="<s:property value="getText('storage.tooltip.vfsType')" />" />
	</td>
	<td width="300">
		<s:text name="storage.label.mountOptions" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.mountOptions')" />"
			 alt="<s:property value="getText('storage.tooltip.mountOptions')" />" />
	</td>
	<td width="75">
		<s:text name="storage.label.persist" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.persist')" />"
			 alt="<s:property value="getText('storage.tooltip.persist')" />" />
	</td>
</tr>
</thead>
<tr>
	<td><s:select name="localFsMountPoint" list="mountPoints" cssClass="size-200px" /></td>
	<td><s:select name="localFsPartition" list="localFsPartitionList" cssClass="size-300px" /></td>
	<td><s:select name="localFsType" list="localFsTypes" cssClass="size-75px" /></td>
	<td><s:textfield name="localFsMountOptions" cssClass="size-300px" /></td>
	<td><s:checkbox name="localFsPersist" cssClass="size-75px" /></td>
</tr>

<tr>
<td colspan="4" align="right">
	<s:reset />
	<s:submit action="StorageMountLocal_mountLocalFs" key="button.mount" />
</td>
</tr>

</table>
</s:form>

<!-- Mount Remote File System -->
<hr />
<h4><s:text name="storage.header.mountRemoteFs" /></h4>
<s:form action="StorageMountRemote_mountRemoteFs" theme="simple">
<table>
<thead>
<tr>
	<td width="200">
		<s:text name="storage.label.file" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.file')" />"
			 alt="<s:property value="getText('storage.tooltip.file')" />" />
	</td>
	<td width="300">
		<s:text name="storage.label.remoteSpec" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.remoteSpec')" />"
			 alt="<s:property value="getText('storage.tooltip.remoteSpec')" />" />
	</td>
	<td width="75">
		<s:text name="storage.label.vfsType" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.vfsType')" />"
			 alt="<s:property value="getText('storage.tooltip.vfsType')" />" />
	</td>
	<td width="300">
		<s:text name="storage.label.mountOptions" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.mountOptions')" />"
			 alt="<s:property value="getText('storage.tooltip.mountOptions')" />" />
	</td>
	<td width="75">
		<s:text name="storage.label.persist" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.persist')" />"
			 alt="<s:property value="getText('storage.tooltip.persist')" />" />
	</td>
</tr>
</thead>
<tr>
	<td><s:select name="remoteFsMountPoint" list="mountPoints" cssClass="size-200px" /></td>
	<td><s:textfield name="remoteFsPartition" cssClass="size-300px" /></td>
	<td><s:select name="remoteFsType" list="remoteFsTypes" cssClass="size-75px" /></td>
	<td><s:textfield name="remoteFsMountOptions" cssClass="size-300px" /></td>
	<td><s:checkbox name="remoteFsPersist" cssClass="size-75px" /></td>
</tr>

<tr>
<td colspan="4">
<table>
<tr>
<td>
	<s:text name="storage.label.remoteFsUser" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('storage.tooltip.remoteFsUser')" />"
		 alt="<s:property value="getText('storage.tooltip.remoteFsUser')" />" />
</td>
<td><s:textfield name="remoteFsUser" cssClass="size-150px" /></td>
<td>
	<s:text name="storage.label.remoteFsPassword" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('storage.tooltip.remoteFsPassword')" />"
		 alt="<s:property value="getText('storage.tooltip.remoteFsPassword')" />" />
</td>
<td><s:textfield name="remoteFsPassword" cssClass="size-150px" /></td>
<td>
	<s:text name="storage.label.remoteFsDomain" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('storage.tooltip.remoteFsDomain')" />"
		 alt="<s:property value="getText('storage.tooltip.remoteFsDomain')" />" />
</td>
<td><s:textfield name="remoteFsDomain" cssClass="size-150px" /></td>
</tr>
</table>
</td>
</tr>

<tr>
<td colspan="4" align="right">
	<s:reset />
	<s:submit action="StorageMountRemote_mountRemoteFs" key="button.mount" />
</td>
</tr>

</table>
</s:form>

<!-- Create Storage Layout -->
<hr />
<h4><s:text name="storage.header.createStorageLayout" /></h4>
<p>Create the default file system directory structure under the chosen storage directory, DIR. 
(owner nobody, group nobody and mode 777.)</p>
<p>
<s:iterator value="storageLayoutList" status="ind">
	DIR<s:property value="storageLayoutList[#ind.index]" /><br />
</s:iterator>
</p>
<s:form action="Storage_createStorageLayout" theme="simple">
<table>
<tr>
	<td>
		<s:text name="storage.label.storageDirectory" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('storage.tooltip.storageDirectory')" />"
			 alt="<s:property value="getText('storage.tooltip.storageDirectory')" />" />
	</td>
	<td><s:select name="storageDirectory" list="mountPoints" cssClass="size-200px" /></td>
	<td><s:submit action="Storage_createStorageLayout" key="button.submit" /></td>
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
