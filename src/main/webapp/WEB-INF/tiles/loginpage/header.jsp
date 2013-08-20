    <script language="javascript">
        function forceHttpsOnSubmit(objForm) {
            objForm.action = objForm.action.replace("http:", "https:").replace("localhost:8080","localhost:8443");
        }
    </script>

