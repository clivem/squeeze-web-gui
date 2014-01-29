<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="fstabAdd.title" /></title>
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
      <h2><s:text name="fstabAdd.header" /></h2>
    </td>
  </tr>
</table>

<hr />

<h4><s:text name="header.configuration" /></h4>
<s:actionerror />
<s:form action="FstabAddSave" theme="simple" >
<table>
<tr>
<td align="right">
	<s:text name="fstab.label.spec" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('fstab.tooltip.spec')" />"
		 alt="<s:property value="getText('fstab.tooltip.spec')" />" />
</td>
<td>
	<s:textfield name="spec" cssClass="size-300px" />
</td>      
</tr>
<tr>
<td align="right">
    <s:text name="fstab.label.file" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('fstab.tooltip.file')" />"
		 alt="<s:property value="getText('fstab.tooltip.file')" />" />
</td>
<td>
	<s:textfield name="file" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="fstab.label.vfsType" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('fstab.tooltip.vfsType')" />"
		 alt="<s:property value="getText('fstab.tooltip.vfsType')" />" />
</td>
<td>
	<s:textfield name="vfsType" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="fstab.label.mntOps" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('fstab.tooltip.mntOps')" />"
		 alt="<s:property value="getText('fstab.tooltip.mntOps')" />" />
</td>
<td>
	<s:textfield name="mntOps" cssClass="size-300px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="fstabAdd.label.freq" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('fstab.tooltip.freq')" />"
		 alt="<s:property value="getText('fstab.tooltip.freq')" />" />
</td>
<td>
	<s:textfield name="freq" cssClass="size-50px" />
</td>
</tr>
<tr>
<td align="right">
	<s:text name="fstabAdd.label.passNo" />
	<img src='struts/tooltip.gif'
		 title="<s:property value="getText('fstab.tooltip.passNo')" />"
		 alt="<s:property value="getText('fstab.tooltip.passNo')" />" />
</td>
<td>
	<s:textfield name="passNo" cssClass="size-50px" />
</td>
</tr>
<tr>
<td colspan="2" align="right">
	<s:reset key="button.reset"/>
	<s:submit action="FstabAddSave" key="button.save" />
</td>
</tr>
</table>
			
</s:form>

<hr />
<h4><s:text name="header.notes" /></h4>
<p><s:text name="fstabAdd.notes.desc" /></p>

<hr />
<jsp:include page="Footer.jsp"/>

</body>
</html>
