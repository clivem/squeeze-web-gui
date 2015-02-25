<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!-- html5 -->
<!DOCTYPE html>

<html>

<head>
	<title><s:text name="brand.name" />: <s:text name="configuration.title" /></title>
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
				<img src="<s:text name="system.logo" />" 
					width="<s:text name="system.logo.width" />" 
					height="<s:text name="system.logo.height" />" 
					alt="<s:text name="system.logo.alt" />" />
			</td>
			<td>
				<jsp:include page="logo_small.jsp" />
			</td>
			<td>
				<h2><s:text name="configuration.header" /></h2>
			</td>
		</tr>
	</table>
	
	<!-- errors -->
	<div style="clear: both;">
		<hr />
		<s:actionerror />
	</div>

	<!-- Version -->
	<div style="clear: both;">
		<div class="config">
			<fieldset>
				<legend>
					<s:text name="configuration.label.version" />
				</legend>
				<div class="entry-nomargin smaller bold">
					<div class="label">
						<s:text name="configuration.label.osVersion" />
					</div>
					<div class="input">
						<s:property value="csosVersion" />
					</div>
				</div>
				<div class="entry-nomargin smaller bold">
					<div class="label">
						<s:text name="configuration.label.fedoraVersion" />
					</div>
					<div class="input">
						<s:property value="fedoraVersion" />
					</div>
				</div>
			</fieldset>
		</div>

		<!-- Hostname -->
		<s:form action="ConfigurationHostname_save" theme="simple">
			<fieldset>
				<legend>
					<s:text name="configuration.label.hostName" />
				</legend>
				<table>
					<!-- hostName -->
					<tr><td>
						<span class="features simptip-position-right simptip-smooth"
							data-tooltip="<s:property value="getText('configuration.tooltip.hostName')" />">
							<label for="hostName" class="label">
								<s:text name="configuration.label.hostName" />
							</label>
							<s:textfield id="hostName" name="hostName" cssClass="input" />
						</span>
					</td></tr>
					<!-- reset/submit -->
					<tr><td align="right">
						<s:reset key="button.reset" />
						<s:submit action="ConfigurationHostname_save" key="button.save" />
					</td></tr>
				</table>
			</fieldset>
		</s:form>

		<!-- Samba
		<s:form action="ConfigurationSamba_save" theme="simple">
			<fieldset>
				<legend>
					<s:text name="configuration.label.samba" />
				</legend>
				<table>
					# sambaNetbiosName
					<tr><td>
						<span class="features simptip-position-right simptip-smooth"
							data-tooltip="<s:property value="getText('configuration.tooltip.sambaNetbiosName')" />">
							<label for="sambaNetbiosName" class="label">
								<s:text name="configuration.label.sambaNetbiosName" />
							</label>
							<s:textfield id="sambaNetbiosName" name="sambaNetbiosName"
								cssClass="input" />
						</span>
					</td></tr>
					# sambaWorkgroup
					<tr><td>
						<span class="features simptip-position-right simptip-smooth"
							data-tooltip="<s:property value="getText('configuration.tooltip.sambaWorkgroup')" />">
							<label for="sambaWorkgroup" class="label">
								<s:text name="configuration.label.sambaWorkgroup" />
							</label>
							<s:textfield id="sambaWorkgroup" name="sambaWorkgroup"
								cssClass="input" />
						</span>
					</td></tr>
					# submit
					<tr><td align="right">
						<s:reset key="button.reset" />
						<s:submit action="ConfigurationSamba_save" key="button.save" />
					</td></tr>
				</table>
			</fieldset>
		</s:form>
		-->
		
		<!-- Location -->
		<s:form action="ConfigurationLocation_save" theme="simple">		
			<fieldset>
				<legend>
					<s:text name="configuration.label.location" />
				</legend>
				<table>
					<!-- timeZone -->
					<tr><td>
						<span class="features simptip-position-right simptip-smooth"
							data-tooltip="<s:property value="getText('configuration.tooltip.timeZone')" />">
							<label for="timeZone" class="label">
								<s:text name="configuration.label.timeZone" />
							</label>
							<s:select id="timeZone" name="timeZone" list="timeZoneList"
								cssClass="input" />
						</span>
					</td></tr>
					<!-- locale -->
					<tr><td>
						<span class="features simptip-position-right simptip-smooth"
							data-tooltip="<s:property value="getText('configuration.tooltip.locale')" />">
							<label for="locale" class="label">
								<s:text name="configuration.label.locale" />
							</label>
							<s:select id="locale" name="systemLocale" cssClass="input"
								list="systemLocaleList" listKey="locale" listValue="description"
								emptyOption="false" headerKey="" headerValue="None" />
						</span>
					</td></tr>
					<!-- reset/submit -->
					<tr><td align="right">
						<s:reset key="button.reset" />
						<s:submit action="ConfigurationLocation_save" key="button.save" />
					</td></tr>
				</table>
			</fieldset>
		</s:form>
	</div>

	<!-- notes -->
	<div style="clear: both;">
		<hr />
		<div style="margin-right: 20px;">
			<fieldset style="width: 100%;">
				<legend>
					<s:text name="header.notes" />
				</legend>
				<p><s:text name="configuration.notes.desc" /></p>
			</fieldset>
		</div>
	</div>
	
	<jsp:include page="footer.jsp" />

</body>
</html>
