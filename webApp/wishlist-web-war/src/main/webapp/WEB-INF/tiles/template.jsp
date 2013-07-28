<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <tiles:insertAttribute name="meta" />
    </head>
    <body>
        <div id="header">
            <tiles:insertAttribute name="header" />
        </div>
        <div id="body">
            <tiles:insertAttribute name="body" />
        </div>
        <div id="footer">
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>