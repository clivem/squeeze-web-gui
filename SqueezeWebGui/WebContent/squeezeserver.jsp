<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!-- html5 -->
<!DOCTYPE html>

<html>

<head>
	<title><s:text name="brand.name" />: <s:text name="squeezeserver.title" /></title>
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
	
	<!-- service -->
	<div style="clear: both;">
		<hr />
		<div style="margin-right: 20px;">
			<fieldset style="width: 100%;">
				<legend>
					<s:text name="header.service" />
				</legend>
				<table class="full">
					<tr>
						<td colspan="2">
							<s:textarea name="status" readonly="true" theme="simple" cssClass="log" />
						</td>
					</tr>
					<tr>
						<td>
							<s:a action="SqueezeServer_populate"><s:property value="getText('button.refresh')" /></s:a>
						</td>
						<td align="right">
							<s:a action="SqueezeServer_enableService"><s:property value="getText('button.enable')" /></s:a> |
							<s:a action="SqueezeServer_disableService"><s:property value="getText('button.disable')" /></s:a> |
							<s:a action="SqueezeServer_startService"><s:property value="getText('button.start')" /></s:a> |
							<s:a action="SqueezeServer_stopService"><s:property value="getText('button.stop')" /></s:a> |
							<s:a action="SqueezeServer_restartService"><s:property value="getText('button.restart')" /></s:a>
						</td>
					</tr>
					<tr>
						<td align="right" colspan="2">
							<s:a action="SqueezeServer_enableAndStartService"><s:property value="getText('button.enableAndStart')" /></s:a> |
							<s:a action="SqueezeServer_disableAndStopService"><s:property value="getText('button.disableAndStop')" /></s:a>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
	</div>

	<!-- configuration -->
	<div style="clear: both;">
		<hr />
		<div style="margin-right: 20px;">
			<fieldset style="width: 100%;">
				<legend>
					<s:text name="header.configuration" />
				</legend>
				<p><a href="http://${pageContext.request.serverName}:9000/" target="blank_">
					SqueezeServer Web Interface</a>
				</p>
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
				<p><s:text name="squeezeserver.notes.desc" />
				</p>
			</fieldset>
		</div>
	</div>
	
	<jsp:include page="footer.jsp"/>

</body>
</html>
