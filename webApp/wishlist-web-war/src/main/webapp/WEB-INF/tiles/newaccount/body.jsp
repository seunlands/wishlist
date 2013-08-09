<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Create account</h1>
<div id="login-error">${error}</div>

<form:form action="<c:url value='/auth/accountsubmit.do' />" method="post" >
    <p>
        <label for="username">Username</label>
        <for id="username" name="username" type="text" />
    </p>
    <p>
        <label for="j_password">Password</label>
        <input id="j_password" name="j_password" type="password" />
    </p>
    <p>
        <label for="_spring_security_remember_me">Remember me : <input type="checkbox" name="_spring_security_remember_me"/></label>
    </p>

    <input  type="submit" value="Login"/>
    <p>
    Forgot the password ? Click <a href="<c:url value='/auth/forgottenpassword.do' />" title="Forgotten password">here</a> <br />
    Wanna join ? <a href="<c:url value='/auth/createaccount.do' />" title="New Account">Create your account</a>
    </p>
</form>
