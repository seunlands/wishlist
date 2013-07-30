<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Password reset</h2>

<form:form method="POST" action="passwordsubmit.do">
    <table>
        <tr>
            <td><form:label path="username">Username&nbsp;:&nbsp;</form:label></td>
            <td><form:input path="username" /></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Submit" />
            </td>
        </tr>
    </table>
</form:form>
