<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!-- html5 -->
<!DOCTYPE html>

<html>

<head>
	<title><s:text name="brand.name" />: <s:text name="help.title" /></title>
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
				<jsp:include page="logo_small.jsp" />
			</td>
			<td>
				<h2><s:text name="help.header" /></h2>
			</td>
		</tr>
	</table>
	

	<!-- main -->
	<div style="clear: both;">
		<hr />
		<div style="margin-right: 20px;">
			<fieldset style="width: 100%;">
				<legend>
					<s:text name="header.help" />
				</legend>
				<p>Coming soon...</p>
				<p>In the meantime, some configuration pages have a Notes section at the bottom 
					of the page.</p>
			</fieldset>
		</div>
	</div>

	<jsp:include page="footer.jsp"/>
	
</body>
</html>
