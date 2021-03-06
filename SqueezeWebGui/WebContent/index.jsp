<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!-- html5 -->
<!DOCTYPE html>

<html>

<head>
	<title><s:text name="brand.name" /> <s:text name="index.title" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="html/css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
	<link href="html/css/simptip-mini.css" rel="stylesheet" type="text/css" />
	<link href="favicon.ico" rel="icon" type="image/x-icon" />
	<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>

<body>
	<!-- Header -->
	<jsp:include page="header_css.jsp" />
	
	<!-- Language -->
	<jsp:include page="language.jsp" />
	
	<!-- Logo -->
	<div align="center">
		<h2><s:text name="brand.name" /> <s:text name="index.header" /></h2>
		<jsp:include page="logo_large.jsp" />
	</div>
	
	<!-- Footer -->
	<jsp:include page="footer.jsp" />
</body>

</html>
