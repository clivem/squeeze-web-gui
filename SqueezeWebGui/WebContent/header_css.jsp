<%@ taglib uri="/struts-tags" prefix="s" %>

	<div id="navigation" class="navigation curved container">
		<span id="nav-home">
			<s:a action="Index"><s:text name="nav.home" /></s:a>
		</span> 
		<span id="nav-system">
			<s:a action="Configuration_populate"><s:text name="nav.system" /></s:a>
		</span>
		<span id="nav-wired">
			<s:a action="Ethernet_populate"><s:text name="nav.wired" /></s:a>
		</span>
		<span id="nav-wireless">
			<s:a action="Wireless_populate"><s:text name="nav.wireless" /></s:a>
		</span>
		<span id="nav-squeezelite">
			<s:a action="Squeezelite_populate"><s:text name="nav.squeezelite" /></s:a>
		</span> 
		<span id="nav-squeezeserver">
			<s:a action="SqueezeServer_populate"><s:text name="nav.squeezeserver" /></s:a>
		</span>
		<span id="nav-storage">
			<s:a action="Storage_populate"><s:text name="nav.storage" /></s:a>
		</span> 
		<span id="nav-shutdown">
			<s:a action="Shutdown"><s:text name="nav.shutdown" /></s:a>
		</span>
		<!-- 
		<span id="nav-faq">
			<s:a href="faq.jsp"><s:text name="nav.faq" /></s:a>
		</span>
		<span id="nav-help">
			<s:a href="help.jsp"><s:text name="nav.help" /></s:a>
		</span>
		 --> 
		<br class="separator" />
	</div>
	