<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="reboot.title" /></title>
  <meta http-equiv="refresh" content="60;url=index.jsp" />
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link href="html/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link href="favicon.ico" rel="icon" type="image/x-icon" />
  <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>
 
<body>
  <center>
  <h1><s:text name="reboot.header" /></h1>
  <jsp:include page="logo_large.jsp" />
  <h2><s:text name="reboot.message" /></h2>
  </center>
  </body>
</html>
