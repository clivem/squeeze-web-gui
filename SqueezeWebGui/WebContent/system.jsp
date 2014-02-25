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
	<div class="title-block">
		<div class="title-image">
			<img src="<s:text name="system.logo" />"
				width="<s:text name="system.logo.width" />"
				height="<s:text name="system.logo.height" />"
				alt="<s:text name="system.logo.alt" />" />
		</div>
		<div class="title-image">
			<s:a action="Index">
				<img src="<s:text name="logo.small" />"
					width="<s:text name="logo.small.width" />"
					height="<s:text name="logo.small.height" />"
					alt="<s:text name="brand.name" /> <s:text name="logo.small.alt" />" />
			</s:a>
		</div>
		<div class="title">
			<s:text name="configuration.header" />
		</div>
	</div>

	<!-- main -->
	<div style="clear: both;">
		<hr />
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

		<s:form action="Configuration_save" theme="simple">
			<!-- Hostname -->
			<fieldset>
				<legend>
					<s:text name="configuration.label.hostName" />
				</legend>
				<!-- hostName -->
				<div class="entry">
					<div class="features simptip-position-right simptip-smooth"
						data-tooltip="<s:property value="getText('configuration.tooltip.hostName')" />">
						<label for="hostName" class="label">
							<s:text name="configuration.label.hostName" />
						</label>
						<s:textfield id="hostName" name="hostName" cssClass="input" />
					</div>
				</div>
			</fieldset>
			<!-- Samba -->
			<fieldset>
				<legend>
					<s:text name="configuration.label.samba" />
				</legend>
				<!-- sambaNetbiosName -->
				<div class="entry">
					<div class="features simptip-position-right simptip-smooth"
						data-tooltip="<s:property value="getText('configuration.tooltip.sambaNetbiosName')" />">
						<label for="sambaNetbiosName" class="label">
							<s:text name="configuration.label.sambaNetbiosName" />
						</label>
						<s:textfield id="sambaNetbiosName" name="sambaNetbiosName"
							cssClass="input" />
					</div>
				</div>
				<!-- sambaWorkgroup -->
				<div class="entry">
					<div class="features simptip-position-right simptip-smooth"
						data-tooltip="<s:property value="getText('configuration.tooltip.sambaWorkgroup')" />">
						<label for="sambaWorkgroup" class="label">
							<s:text name="configuration.label.sambaWorkgroup" />
						</label>
						<s:textfield id="sambaWorkgroup" name="sambaWorkgroup"
							cssClass="input" />
					</div>
				</div>
			</fieldset>
			<!-- Location -->
			<fieldset>
				<legend>
					<s:text name="configuration.label.location" />
				</legend>
				<!-- timeZone -->
				<div class="entry">
					<div class="features simptip-position-right simptip-smooth"
						data-tooltip="<s:property value="getText('configuration.tooltip.timeZone')" />">
						<label for="timeZone" class="label">
							<s:text name="configuration.label.timeZone" />
						</label>
						<s:select id="timeZone" name="timeZone" list="timeZoneList"
							cssClass="input" />
					</div>
				</div>
				<!-- locale -->
				<div class="entry">
					<div class="features simptip-position-right simptip-smooth"
						data-tooltip="<s:property value="getText('configuration.tooltip.locale')" />">
						<label for="locale" class="label">
							<s:text name="configuration.label.locale" />
						</label>
						<s:select id="locale" name="systemLocale" cssClass="input"
							list="systemLocaleList" listKey="locale" listValue="description"
							emptyOption="false" headerKey="" headerValue="None" />
					</div>
				</div>
			</fieldset>
			<!-- Submit form -->
			<fieldset>
				<!-- submit -->
				<div class="entry submit">
					<s:reset key="button.reset" />
					<s:submit action="Configuration_save" key="button.save" />
				</div>
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
