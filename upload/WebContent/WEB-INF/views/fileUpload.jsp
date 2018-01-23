<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>File upload using servlet</title>
</head>
<body>
    <form action="uploadServlet" method="post" enctype="multipart/form-data">
        Select file to upload : <input type="file" name="fileName"/></br></br>
        <input type="submit" value="Upload" />
    </form>
    <h5>${message}</h5>
</body>
</html>