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
		function displayAdvancedOptions() {
			if (document.getElementById('showAdvancedOptions').checked) {
				document.getElementById('advancedOptions').style.display = '';
			} else {
				document.getElementById('advancedOptions').style.display = 'none';
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
	
	<!-- configuration -->
	<div style="clear: both;">
		<hr />
		<div style="margin-right: 20px;">
			<fieldset style="width: 100%;">
				<legend>
					<s:text name="header.configuration" />
				</legend>
				
				<s:actionerror />
				
				<s:form action="SqueezeliteSave_save" theme="simple" onreset="javascript:doReset();" >
					<!-- kludge to make sure the reset is done before we decide whether to display advanced options -->
					<script>
						function doReset() {
							setTimeout(function(){displayAdvancedOptions();}, 0);
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
						<!-- alsa params -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.alsaParams" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.alsaParams')" />"
									alt="<s:property value="getText('squeezelite.tooltip.alsaParams')" />" />
							</td>
							<td>
								<s:textfield name="alsaParams" cssClass="size-300px" />
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
						<!-- upsample -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.upsample" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.upsample')" />"
									alt="<s:property value="getText('squeezelite.tooltip.upsample')" />" />
							</td>
							<td>
								<s:checkbox name="upsample" />
								<s:textfield name="upsampleOptions" cssClass="size-100px" />
								<s:a href="squeezelite_upsample.jsp">Info</s:a>
							</td>
						</tr>
						<!-- dop -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.dop" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.dop')" />"
									alt="<s:property value="getText('squeezelite.tooltip.dop')" />" />
							</td>
							<td>
								<s:checkbox name="dop" />
								<s:textfield name="dopOptions" cssClass="size-100px" />
							</td>
						</tr>
						<!-- visulizer -->
						<tr>
							<td align="right">
								<s:text name="squeezelite.label.visulizer" />
								<img src='struts/tooltip.gif'
									title="<s:property value="getText('squeezelite.tooltip.visulizer')" />"
									alt="<s:property value="getText('squeezelite.tooltip.visulizer')" />" />
							</td>
							<td>
								<s:checkbox name="visulizer" />
							</td>
						</tr>
						<!-- show advanced options -->
						<tr>
							<td align="right">
							</td>
							<td>
								<s:checkbox name="showAdvancedOptions" id="showAdvancedOptions" 
									onchange="javascript:displayAdvancedOptions();" />
								<label for="showAdvancedOptions">
									<s:text name="squeezelite.label.advancedOptions" />
								</label>
							</td>
						</tr>
						<!-- Start: Advanced Options -->
						<tr>
							<td colspan="2">
								<table id="advancedOptions" 
									<s:if test="%{showAdvancedOptions}">style="display:"</s:if>
									<s:else>style="display:none"</s:else>>
									<!-- MAC -->
									<tr>
										<td align="right">
											<s:text name="squeezelite.label.mac" />
											<img src='struts/tooltip.gif'
												title="<s:property value="getText('squeezelite.tooltip.mac')" />"
												alt="<s:property value="getText('squeezelite.tooltip.mac')" />" />
										</td>
										<td>
											<s:textfield name="mac" cssClass="size-300px" />
										</td>
									</tr>
									<!-- default MAC -->
									<tr>
										<td />
										<td>
											<s:checkbox name="defaultMac" />
											<s:text name="squeezelite.label.mac.default" />
										</td>
									</tr>
									<!-- log file -->
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
									<!-- log level -->
									<tr>
										<td align="right">
											<s:text name="squeezelite.label.logLevel" />
											<img src='struts/tooltip.gif'
												title="<s:property value="getText('squeezelite.tooltip.logLevel')" />"
												alt="<s:property value="getText('squeezelite.tooltip.logLevel')" />" />
										</td>
										<td>
											<s:textfield name="logLevel" cssClass="size-300px" />
										</td>
									</tr>
									<!-- priority -->
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
									<!-- buffer -->
									<tr>
										<td align="right">
											<s:text name="squeezelite.label.buffer" />
											<img src='struts/tooltip.gif'
												title="<s:property value="getText('squeezelite.tooltip.buffer')" />"
												alt="<s:property value="getText('squeezelite.tooltip.buffer')" />" />
										</td>
										<td>
											<s:textfield name="buffer" cssClass="size-300px" />
										</td>
									</tr>
									<!-- codec -->
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
									<!-- options -->
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
									<!-- server ip address -->
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
								</table>
							</td>
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
