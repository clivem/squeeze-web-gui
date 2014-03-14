<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<title><s:text name="brand.name" />: <s:text name="squeezelite.title" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="html/css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
	<link href="html/css/simptip-mini.css" rel="stylesheet" type="text/css" />
	<link href="favicon.ico" rel="icon" type="image/x-icon" />
	<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>

<body>

	<script type="text/javascript">
		function displayResampleOptions() {
			if (document.getElementById('resampleEnabled').checked) {
				document.getElementById('resampleRecipeQuality_tr').style.display = '';
				document.getElementById('resampleRecipeFilter_tr').style.display = '';
				document.getElementById('resampleRecipeSteep_tr').style.display = '';
				document.getElementById('resampleRecipeException_tr').style.display = '';
				document.getElementById('resampleRecipeAsync_tr').style.display = '';
				document.getElementById('resampleRecipeFlags_tr').style.display = '';
				document.getElementById('resampleRecipeAttenuation_tr').style.display = '';
				document.getElementById('resampleRecipePrecision_tr').style.display = '';
				document.getElementById('resampleRecipePassbandEnd_tr').style.display = '';
				document.getElementById('resampleRecipeStopbandStart_tr').style.display = '';
				document.getElementById('resampleRecipePhaseResponse_tr').style.display = '';
			} else {
				document.getElementById('resampleRecipeQuality_tr').style.display = 'none';
				document.getElementById('resampleRecipeFilter_tr').style.display = 'none';
				document.getElementById('resampleRecipeSteep_tr').style.display = 'none';
				document.getElementById('resampleRecipeException_tr').style.display = 'none';
				document.getElementById('resampleRecipeAsync_tr').style.display = 'none';
				document.getElementById('resampleRecipeFlags_tr').style.display = 'none';
				document.getElementById('resampleRecipeAttenuation_tr').style.display = 'none';
				document.getElementById('resampleRecipePrecision_tr').style.display = 'none';
				document.getElementById('resampleRecipePassbandEnd_tr').style.display = 'none';
				document.getElementById('resampleRecipeStopbandStart_tr').style.display = 'none';
				document.getElementById('resampleRecipePhaseResponse_tr').style.display = 'none';
			}
		}

		function displayDopOptions() {
			if (document.getElementById('dopEnabled').checked) {
				document.getElementById('dopOptions').style.display = '';
			} else {
				document.getElementById('dopOptions').style.display = 'none';
			}
		}
	</script>  

	<!-- navigation header -->
	<s:include value="header_css.jsp" />

	<!-- title -->
		<table>
		<tr>
			<td>
				<img src="<s:text name="squeezeplayer.logo" />" 
					width="<s:text name="squeezeplayer.logo.width" />" 
					height="<s:text name="squeezeplayer.logo.height" />" 
					alt="<s:text name="squeezeplayer.logo.alt" />" />
			</td>
			<td>
				<jsp:include page="logo_small.jsp" />
			</td>
			<td>
				<h2><s:text name="squeezelite.header" /></h2>
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
							<s:a action="Squeezelite_populate"><s:property value="getText('button.refresh')" /></s:a>
						</td>
						<td align="right">
							<s:a action="Squeezelite_enableService"><s:property value="getText('button.enable')" /></s:a> |
							<s:a action="Squeezelite_disableService"><s:property value="getText('button.disable')" /></s:a> |
							<s:a action="Squeezelite_startService"><s:property value="getText('button.start')" /></s:a> |
							<s:a action="Squeezelite_stopService"><s:property value="getText('button.stop')" /></s:a> |
							<s:a action="Squeezelite_restartService"><s:property value="getText('button.restart')" /></s:a>
						</td>
					</tr>
					<tr>
						<td align="right" colspan="2">
							<s:a action="Squeezelite_enableAndStartService"><s:property value="getText('button.enableAndStart')" /></s:a> |
							<s:a action="Squeezelite_disableAndStopService"><s:property value="getText('button.disableAndStop')" /></s:a>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
	</div>
	
	<!-- log -->
	<div style="clear: both;">
		<hr />
		<div style="margin-right: 20px;">
			<fieldset style="width: 100%;">
				<legend>
					<s:text name="header.log" />
				</legend>
				<s:form action="Squeezelite_populate" theme="simple">
					<table class="full">
						<tr>
							<td colspan="2">
								<textarea readonly="readonly" class="log"><s:property value="%{log}" /></textarea>
							</td> 
						</tr>
						<tr>
							<td colspan="2">
								<label for="logLines">
									<s:text name="label.logLines" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('tooltip.logLines')" />"
										alt="<s:property value="getText('tooltip.logLines')" />" />
								</label>
								<s:textfield id="loglines" name="logLines" cssClass="size-75px" />
								<s:submit key="button.refresh" action="Squeezelite_populate" />
							</td>
						</tr>
					</table>
				</s:form>
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
				
				<s:actionerror />
				
				<s:form action="SqueezeliteSave_save" theme="simple" onreset="javascript:doReset();">
					<script>
						function doReset() {
							setTimeout(function(){displayResampleOptions();displayDopOptions();}, 0);
						}
					</script>
					
					<!-- hidden store the audioDevList -->
					<s:iterator value="audioDevList" status="stat">
						<s:hidden name="audioDevList[%{#stat.index}]" value="%{audioDevList[#stat.index]}" />
					</s:iterator>

					<table>
						<!-- name -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.name" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.name')" />"
									alt="<s:property value="getText('squeezelite.tooltip.name')" />" />
							</td>
							<td>
								<s:textfield name="name" cssClass="size-300px" />
							</td>      
						</tr>
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.logFile" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.logFile')" />"
									alt="<s:property value="getText('squeezelite.tooltip.logFile')" />" />
							</td>
							<td>
								<s:textfield name="logFile" cssClass="size-300px" />
							</td>
						</tr>
						<!-- log level all -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.logLevel" />
								<label for="logLevelAll" style="font-style: italic;">
									<s:text name="squeezelite.label.logLevel.all" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.logLevel')" />"
										alt="<s:property value="getText('squeezelite.tooltip.logLevel')" />" />
								</label>
							</td>
							<td>
								<s:select id="logLevelAll" name="logLevelAll" list="logLevelList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set"/>
							</td>
						</tr>
						<!-- log level slimproto -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.logLevel" />
								<label for="logLevelSlimproto" style="font-style: italic;">
									<s:text name="squeezelite.label.logLevel.slimproto" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.logLevel')" />"
										alt="<s:property value="getText('squeezelite.tooltip.logLevel')" />" />
								</label>
							</td>
							<td>
								<s:select id="logLevelSlimproto" name="logLevelSlimproto" list="logLevelList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set"/>
							</td>
						</tr>
						<!-- log level stream -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.logLevel" />
								<label for="logLevelStream" style="font-style: italic;">
									<s:text name="squeezelite.label.logLevel.stream" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.logLevel')" />"
										alt="<s:property value="getText('squeezelite.tooltip.logLevel')" />" />
								</label>
							</td>
							<td>
								<s:select id="logLevelStream" name="logLevelStream" list="logLevelList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set"/>
							</td>
						</tr>
						<!-- log level decode -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.logLevel" />
								<label for="logLevelDecode" style="font-style: italic;">
									<s:text name="squeezelite.label.logLevel.decode" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.logLevel')" />"
										alt="<s:property value="getText('squeezelite.tooltip.logLevel')" />" />
								</label>
							</td>
							<td>
								<s:select id="logLevelDecode" name="logLevelDecode" list="logLevelList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set"/>
							</td>
						</tr>
						<!-- log level output -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.logLevel" />
								<label for="logLevelOutput" style="font-style: italic;">
									<s:text name="squeezelite.label.logLevel.output" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.logLevel')" />"
										alt="<s:property value="getText('squeezelite.tooltip.logLevel')" />" />
								</label>
							</td>
							<td>
								<s:select id="logLevelOutput" name="logLevelOutput" list="logLevelList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set"/>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.buffer.stream" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.buffer.stream')" />"
									alt="<s:property value="getText('squeezelite.tooltip.buffer.stream')" />" />
							</td>
							<td>
								<s:textfield name="bufferStream" cssClass="size-300px" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.buffer.output" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.buffer.output')" />"
									alt="<s:property value="getText('squeezelite.tooltip.buffer.output')" />" />
							</td>
							<td>
								<s:textfield name="bufferOutput" cssClass="size-300px" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.priority" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.priority')" />"
									alt="<s:property value="getText('squeezelite.tooltip.priority')" />" />
							</td>
							<td>
								<s:select name="priority" list="priorityList" />
							</td>
						</tr>
						<!-- audio device -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.audioDev" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.audioDev')" />"
									alt="<s:property value="getText('squeezelite.tooltip.audioDev')" />" />
							</td>
							<td>
								<s:select name="audioDev" list="audioDevList" />
							</td>
						</tr>
						<!-- alsa params buffer size -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.alsaParams.buffer" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.alsaParams.buffer')" />"
									alt="<s:property value="getText('squeezelite.tooltip.alsaParams.buffer')" />" />
							</td>
							<td>
								<s:textfield name="alsaParamsBuffer" cssClass="size-100px" />
							</td>
						</tr>
						<!-- alsa params period count -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.alsaParams.period" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.alsaParams.period')" />"
									alt="<s:property value="getText('squeezelite.tooltip.alsaParams.period')" />" />
							</td>
							<td>
								<s:textfield name="alsaParamsPeriod" cssClass="size-100px" />
							</td>
						</tr>
						<!-- alsa params format -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.alsaParams.format" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.alsaParams.format')" />"
									alt="<s:property value="getText('squeezelite.tooltip.alsaParams.format')" />" />
							</td>
							<td>
								<s:select name="alsaParamsFormat" list="alsaParamsFormatList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set" />
							</td>
						</tr>
						<!-- alsa params mmap -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.alsaParams.mmap" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.alsaParams.mmap')" />"
									alt="<s:property value="getText('squeezelite.tooltip.alsaParams.mmap')" />" />
							</td>
							<td>
								<s:select name="alsaParamsMmap" list="alsaParamsMmapList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set" />
							</td>
						</tr>
						<!-- max rate -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.maxRate" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.maxRate')" />"
									alt="<s:property value="getText('squeezelite.tooltip.maxRate')" />" />
							</td>
							<td>
								<s:textfield name="maxRate" cssClass="size-300px" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.codec" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.codec')" />"
									alt="<s:property value="getText('squeezelite.tooltip.codec')" />" />
							</td>
							<td>
								<s:textfield name="codec" cssClass="size-300px" />
							</td>
						</tr>
						<!-- resample -->
						<tr>
							<td align="right">
								<label for="resampleEnabled">
									<s:text name="squeezelite.label.resample" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample')" />" />
								</label>
							</td>
							<td>
								<s:checkbox id="resampleEnabled" name="resample" 
									onchange="javascript:displayResampleOptions();" />
								<s:a href="squeezelite_upsample.jsp" target="_blank">Info</s:a>
							</td>
						</tr>
						<!-- resample recipe quality -->
						<tr id="resampleRecipeQuality_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resampleRecipeQuality" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.quality" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.quality')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.quality')" />" />
								</label>
							</td>
							<td>
								<s:select id="resampleRecipeQuality" name="resampleRecipeQuality" list="resampleQualityList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set"/>
							</td>
						</tr>
						<!-- resample recipe filter -->
						<tr id="resampleRecipeFilter_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resampleRecipeFilter" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.filter" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.filter')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.filter')" />" />
								</label>
							</td>
							<td>
								<s:select id="resampleRecipeFilter" name="resampleRecipeFilter" list="resampleFilterList" 
									listKey="flag" listValue="name" emptyOption="false" 
									headerKey="" headerValue="Not set"/>
							</td>
						</tr>
						<!-- resample recipe steep -->
						<tr id="resampleRecipeSteep_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resampleRecipeSteep" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.steep" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.steep')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.steep')" />" />
								</label>
							</td>
							<td>
								<s:checkbox id="resampleRecipeSteep" name="resampleRecipeSteep" />
							</td>
						</tr>												
						<!-- resample recipe exception -->
						<tr id="resampleRecipeException_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resampleRecipeException" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.exception" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.exception')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.exception')" />" />
								</label>
							</td>
							<td>
								<s:checkbox id="resampleRecipeException" name="resampleRecipeException" />
							</td>
						</tr>
						<!-- resample recipe async -->
						<tr id="resampleRecipeAsync_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resampleRecipeAsync" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.async" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.async')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.async')" />" />
								</label>
							</td>
							<td>
								<s:checkbox id="resampleRecipeAsync" name="resampleRecipeAsync" />
							</td>
						</tr>
						<!-- resample advanced flags -->
						<tr id="resampleRecipeFlags_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resampleFlags" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.flags" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.flags')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.flags')" />" />
								</label>
							</td>
							<td>
								<s:textfield id="resampleFlags" name="resampleFlags" cssClass="size-100px" />
							</td>
						</tr>
						<!-- resample attenuation -->
						<tr id="resampleRecipeAttenuation_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resampleAttenuation" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.attenuation" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.attenuation')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.attenuation')" />" />
								</label>
							</td>
							<td>
								<s:textfield id="resampleAttenuation" name="resampleAttenuation" cssClass="size-100px" />
							</td>
						</tr>
						<!-- resample precision -->
						<tr id="resampleRecipePrecision_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resamplePrecision" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.precision" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.precision')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.precision')" />" />
								</label>
							</td>
							<td>
								<s:textfield id="resamplePrecision" name="resamplePrecision" cssClass="size-100px" />
							</td>
						</tr>
						<!-- resample passband end -->
						<tr id="resampleRecipePassbandEnd_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resamplePassbandEnd" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.passbandEnd" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.passbandEnd')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.passbandEnd')" />" />
								</label>
							</td>
							<td>
								<s:textfield id="resamplePassbandEnd" name="resamplePassbandEnd" cssClass="size-100px" />
							</td>
						</tr>
						<!-- resample stopband start -->
						<tr id="resampleRecipeStopbandStart_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resampleStopbandStart" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.stopbandStart" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.stopbandStart')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.stopbandStart')" />" />
								</label>
							</td>
							<td>
								<s:textfield id="resampleStopbandStart" name="resampleStopbandStart" cssClass="size-100px" />
							</td>
						</tr>
						<!-- resample phase response -->
						<tr id="resampleRecipePhaseResponse_tr" 
							<s:if test="%{resample}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="resamplePhaseResponse" style="font-style: italic;">
									<s:text name="squeezelite.label.resample.phaseResponse" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.resample.phaseResponse')" />"
										alt="<s:property value="getText('squeezelite.tooltip.resample.phaseResponse')" />" />
								</label>
							</td>
							<td>
								<s:textfield id="resamplePhaseResponse" name="resamplePhaseResponse" cssClass="size-100px" />
							</td>
						</tr>																								
						<!-- DoP -->
						<tr>
							<td align="right">
								<label for="dopEnabled">
									<s:text name="squeezelite.label.dop" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.dop')" />"
										alt="<s:property value="getText('squeezelite.tooltip.dop')" />" />
								</label>
							</td>
							<td>
								<s:checkbox id="dopEnabled" name="dop"
									onchange="javascript:displayDopOptions();" />
							</td>
						</tr>
						<!-- DoP options -->
						<tr id="dopOptions" 
							<s:if test="%{dop}">style="display:"</s:if>
							<s:else>style="display:none"</s:else>>
							<td align="right">
								<label for="dopOptions" style="font-style: italic;">
									<s:text name="squeezelite.label.dop.delay" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tootip.dop.delay')" />"
										alt="<s:property value="getText('squeezelite.tootip.dop.delay')" />" />
								</label>
							</td>
							<td>
								<s:textfield id="dopOptions" name="dopOptions" cssClass="size-100px" />
							</td>
						</tr>
						<!-- visulizer -->
						<tr>
							<td align="right">
								<label for="visulizer">
									<s:text name="squeezelite.label.visulizer" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.visulizer')" />"
										alt="<s:property value="getText('squeezelite.tooltip.visulizer')" />" />
								</label>
							</td>
							<td>
								<s:checkbox id="visulizer" name="visulizer" />
							</td>
						</tr>
						<!-- Start: Advanced Options -->
						<tr>
							<td align="right">
								<label for="mac">
									<s:text name="squeezelite.label.mac" />
									<img src='struts/tooltip.gif'
										title="<s:property value="getText('squeezelite.tooltip.mac')" />"
										alt="<s:property value="getText('squeezelite.tooltip.mac')" />" />
								</label>
							</td>
							<td>
								<s:textfield id="mac" name="mac" cssClass="size-300px" />
							</td>
						</tr>
						<tr>
							<td />
							<td>
								<s:checkbox id="defaultMac" name="defaultMac" />
								<label for="defaultMac">
									<s:text name="squeezelite.label.mac.default" />
								</label>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.serverIp" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.serverIp')" />"
									alt="<s:property value="getText('squeezelite.tooltip.serverIp')" />" />
							</td>
							<td>
								<s:textfield name="serverIp" cssClass="size-300px" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.options" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.options')" />"
									alt="<s:property value="getText('squeezelite.tooltip.options')" />" />
							</td>
							<td>
								<s:textfield name="options" cssClass="size-300px" />
							</td>
						</tr>

						<!-- dummy row -->
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
							
						<!-- buttons -->
						<tr>
							<td colspan="2" align="right">
								<s:reset key="button.reset"/>
								<s:submit action="SqueezeliteSave_save" 
									key="button.save" />
							</td>
							</tr>
							
							<tr>
							<td colspan="2" align="right">
								<s:submit action="SqueezeliteSave_saveAndRestart" 
									key="button.saveAndConditionallyRestart" />
							</td>
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
				<p><s:text name="squeezelite.notes.desc" />
				</p>
				<ul><s:text name="squeezelite.notes" />
				</ul>
			</fieldset>
		</div>
	</div>
	
	<jsp:include page="footer.jsp"/>

</body>
</html>
