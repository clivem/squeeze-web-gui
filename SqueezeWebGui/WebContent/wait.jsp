<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="wait.title" /></title>
  <meta http-equiv="refresh" content="1;url=<s:url includeParams="all" />" />
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link href="html/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link href="favicon.ico" rel="icon" type="image/x-icon" />
  <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>
 
<body>
  <center>
  <h1><s:text name="wait.header" /></h1>
  <jsp:include page="logo_large.jsp" />
  <h2><s:text name="wait.message" /></h2>
  </center>
</body>
</html>
