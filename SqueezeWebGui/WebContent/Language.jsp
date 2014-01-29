<%@ taglib uri="/struts-tags" prefix="s" %>

<s:form name="localeForm" theme="simple">
  <div align="right">
  
	<p class="language">
	<s:text name="language.language" />:
	<select name="myLocales" class="language"
		OnChange="location.href=localeForm.myLocales.options[selectedIndex].value">

		<option selected>
		  <s:text name="language.select" />
		</option>

		<!-- English -->
		<s:url id="en">
		  <s:param name="request_locale">en</s:param>
		</s:url>
		<option value="<s:property value="%{en}" />">
			<s:text name="language.en" />
		</option>

		<!-- French -->
		<s:url id="fr">
		  <s:param name="request_locale">fr</s:param>
		</s:url>
		<option value="<s:property value="%{fr}" />">
			<s:text name="language.fr" />
		</option>

		<!-- Danish -->
		<s:url id="da">
		  <s:param name="request_locale">da</s:param>
		</s:url>
		<option value="<s:property value="%{da}" />">
			<s:text name="language.da" />
		</option>

		<!-- Dutch -->
		<s:url id="nl">
		  <s:param name="request_locale">nl</s:param>
		</s:url>
		<option value="<s:property value="%{nl}" />">
			<s:text name="language.nl" />
		</option>

		<!-- German -->
		<s:url id="de">
		  <s:param name="request_locale">de</s:param>
		</s:url>
		<option value="<s:property value="%{de}" />">
			<s:text name="language.de" />
		</option>

		<!-- Italian -->
		<s:url id="it">
		  <s:param name="request_locale">it</s:param>
		</s:url>
		<option value="<s:property value="%{it}" />">
			<s:text name="language.it" />
		</option>

		<!-- Swedish -->
		<s:url id="sv">
		  <s:param name="request_locale">sv</s:param>
		</s:url>
		<option value="<s:property value="%{sv}" />">
			<s:text name="language.sv" />
		</option>

	</select>
	</p>
  </div>
</s:form>

