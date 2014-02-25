<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="fstab.title" /></title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link href="html/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link href="favicon.ico" rel="icon" type="image/x-icon" />
  <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>
<body>

<!-- Header -->
<jsp:include page="header.jsp"/>

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
      <h2><s:text name="fstab.header" /></h2>
    </td>
  </tr>
</table>

<!-- Mounted File Systems -->
<hr />
<h4><s:text name="header.mountedFileSystems" /></h4>
<s:form action="Fstab" theme="simple">
<table>
<tr>
<td>
	<s:checkbox name="cbAll" />
	<s:text name="cb.allFileSystemTypes" />
	<s:checkbox name="cbCifs" />
	<s:text name="cb.cifs" />
	<s:checkbox name="cbExt4" />
	<s:text name="cb.ext4" />
	<s:checkbox name="cbExt3" />
	<s:text name="cb.ext3" />
	<s:checkbox name="cbExt2" />
	<s:text name="cb.ext2" />
	<s:checkbox name="cbNfs" />
	<s:text name="cb.nfs" />
</td>
</tr>
<tr>
<td>
	<s:textarea name="status" cols="100" rows="6" readonly="true" />
</td>
</tr>
<tr>
<td align="right">
	<s:submit action="Fstab_populate" value="Refresh" />
</td>
</tr>
</table>
</s:form>

<!-- Add Fstab entry -->
<hr />
<h4><s:text name="header.fstabAdd" /></h4>
<a href="<s:url  action="FstabAdd.action" />">
  <s:text name="fstab.addEntry.message" />
</a>

<!-- Modify Fstab Entry -->
<hr />
<h4><s:text name="header.fstabModify" /></h4>
<s:actionerror />
<s:form action="FstabSave_save" theme="simple">

<s:hidden name="status" />

<table>
<thead>
<tr>
	<td>
		<s:text name="fstab.label.spec" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('fstab.tooltip.spec')" />"
			 alt="<s:property value="getText('fstab.tooltip.spec')" />" />
	</td>
	<td>
		<s:text name="fstab.label.file" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('fstab.tooltip.file')" />"
			 alt="<s:property value="getText('fstab.tooltip.file')" />" />
	</td>
	<td>
		<s:text name="fstab.label.vfsType" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('fstab.tooltip.vfsType')" />"
			 alt="<s:property value="getText('fstab.tooltip.vfsType')" />" />
	</td>
	<td>
		<s:text name="fstab.label.mntOps" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('fstab.tooltip.mntOps')" />"
			 alt="<s:property value="getText('fstab.tooltip.mntOps')" />" />
	</td>
	<td>
		<s:text name="fstab.label.freq" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('fstab.tooltip.freq')" />"
			 alt="<s:property value="getText('fstab.tooltip.freq')" />" />
	</td>
	<td>
		<s:text name="fstab.label.passNo" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('fstab.tooltip.passNo')" />"
			 alt="<s:property value="getText('fstab.tooltip.passNo')" />" />
	</td>
	<td>
		<s:text name="fstab.label.action" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('fstab.tooltip.action')" />"
			 alt="<s:property value="getText('fstab.tooltip.action')" />" />
	</td>
	<td>
		<s:text name="fstab.label.delete" />
		<img src='struts/tooltip.gif'
			 title="<s:property value="getText('fstab.tooltip.delete')" />"
			 alt="<s:property value="getText('fstab.tooltip.delete')" />" />
	</td>
</tr>
</thead>
<s:iterator value="fstabList" id="fstabList">
<tr>
	<td><s:textfield name="spec" readonly="true" /></td>
	<td><s:textfield name="file" /></td>
	<td><s:textfield name="vfsType" cssClass="size-50px" /></td>
	<td><s:textfield name="mntOps" /></td>
	<td><s:textfield name="freq" cssClass="size-50px" /></td>
	<td><s:textfield name="passNo" cssClass="size-50px" /></td>
	<td><s:select name="action" list="fstabEntryActionList" /></td>
	<td align="center">
		<s:checkbox name="delete" />
	</td>
</tr>
</s:iterator>
<tr>
<td colspan="8" align="left">
	<s:submit action="Fstab_populate" key="button.refresh" />
	<s:submit action="FstabSave_save" key="button.submit" />
	<s:reset />
</td>
</tr>
</table>
</s:form>

<!-- Notes -->
<hr />
<h4><s:text name="header.notes" /></h4>
<p><s:text name="fstab.notes.desc" /></p>

<!-- Footer -->
<hr />
<jsp:include page="footer.jsp"/>

</body>
</html>
