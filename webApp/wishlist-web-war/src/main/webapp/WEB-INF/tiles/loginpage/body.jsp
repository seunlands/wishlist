<h1>Login</h1>7
<div id="login-error">${error}</div>

<form action="../j_spring_security_check" method="post" onsubmit="forceHttpsOnSubmit(this)" >
    <p>
        <label for="j_username">Username</label>
        <input id="j_username" name="j_username" type="text" />
    </p>
    <p>
        <label for="j_password">Password</label>
        <input id="j_password" name="j_password" type="password" />
    </p>
    <p>
        <label for="_spring_security_remember_me">Remember me : <input type="checkbox" name="_spring_security_remember_me"/></label>
    </p>

    <input  type="submit" value="Login"/>

</form>