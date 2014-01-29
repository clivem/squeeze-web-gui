<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="help.title" /></title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link href="html/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link href="favicon.ico" rel="icon" type="image/x-icon" />
  <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>
<body>

<jsp:include page="Header.jsp"/>

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

<hr />

<h4><s:text name="header.help" /></h4>
<p>Coming soon...</p>
<p>In the meantime, some configuration pages have a Notes section at the bottom 
   of the page.</p>
<hr />
<jsp:include page="Footer.jsp"/>

</body>
</html>
