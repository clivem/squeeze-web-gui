<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!-- html5 -->
<!DOCTYPE html>

<html>

<head>
	<title><s:text name="brand.name" />: <s:text name="shutdown.title" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="html/css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
	<link href="html/css/simptip-mini.css" rel="stylesheet" type="text/css" />
	<link href="favicon.ico" rel="icon" type="image/x-icon" />
	<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>

<body>

	<!-- navigation header -->
	<s:include value="header_css.jsp" />
	
	<!-- title -->
	<table>
		<tr>
			<td>
				<img src="<s:text name="shutdown.logo" />" 
					width="<s:text name="shutdown.logo.width" />" 
					height="<s:text name="shutdown.logo.height" />" 
					alt="<s:text name="shutdown.logo.alt" />" />
			</td>
			<td>
				<jsp:include page="logo_small.jsp" />
			</td>
			<td>
				<h2><s:text name="shutdown.header" /></h2>
			</td>
		</tr>
	</table>

	<!-- main -->
	<div style="clear: both;">
		<hr />
		<div style="margin-right: 20px;">
			<fieldset style="width: 100%;">
				<legend>
					<s:text name="header.control" />
				</legend>
				<s:form action="Reboot" theme="simple" >
					<table>
						<tr>
							<td align="right"><s:submit key="button.halt" action="Halt" /></td>
							<td><s:text name="shutdown.halt.mesg" /></td>
						</tr>
						<tr>
							<td align="right"><s:submit key="button.reboot" action="Reboot" /></td>
							<td><s:text name="shutdown.reboot.mesg" /></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td align="right"><s:checkbox name="cbForceReboot" /></td>
							<td><s:text name="cb.forceRebootHalt" /></td>
						</tr>
					</table>
				</s:form>
			</fieldset>
		</div>
	</div>

	<!-- notes -->
	<div style="clear: both;">
		<hr />
		<div style="margin-right: 20px;">
			<fieldset style="width: 100%;">
				<legend>
					<s:text name="header.notes" />
				</legend>
				<p><s:text name="shutdown.notes.desc" /></p>
			</fieldset>
		</div>
	</div>
	
	<jsp:include page="footer.jsp"/>

</body>
</html>
