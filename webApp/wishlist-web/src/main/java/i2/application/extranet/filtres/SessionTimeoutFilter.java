/**
 * 
 */
package i2.application.extranet.filtres;

import i2.application.cerbere.commun.Cerbere;
import i2.application.cerbere.commun.CerbereConnexionException;
import i2.application.extranet.utils.Constants;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SessionTimeoutFilter
 * 
 * @author Bull
 * 
 */
public class SessionTimeoutFilter implements Filter {

    private final Log logger = LogFactory.getLog(SessionTimeoutFilter.class);

    private String timeoutPage = "deconnection.do";

    private String welcomePage = "accueil/init.do";

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
	// Rien à faire
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
	    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

	    // is session expired control required for this request?
	    if (isSessionControlRequiredForThisResource(httpServletRequest)) {
		boolean isSessionInValid = (httpServletRequest.getSession() == null || httpServletRequest.getSession().getAttribute(Constants.SES_UTILISATEUR) == null);

		if (isSessionInValid) {
		    String timeoutUrl = httpServletRequest.getContextPath() + "/" + getTimeoutPage();
		    if (httpServletRequest.getSession() != null)
			httpServletRequest.getSession().invalidate();
		    logger.debug("La session est invalide redirection vers la page de timeout de session : " + timeoutUrl);

		    // On utilise Cerbere pour déconnecter proprement
		    Cerbere c;
		    try {
			c = Cerbere.creation(httpServletRequest);
			c.logoff(httpServletRequest, httpServletResponse);
		    } catch (CerbereConnexionException e) {
			// Si Cerbere plante on force une déconnexion (sale)
			httpServletResponse.sendRedirect(timeoutUrl);
		    }
		    return;
		}
	    }
	}

	chain.doFilter(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
	// Rien à faire
    }

    /**
     * 
     * session shouldn't be checked for some pages. For example: for timeout page.. Since we're redirecting to timeout page from this filter, if we don't disable session control for it, filter will
     * again redirect to it and this will be result with an infinite loop... Ici pas besoin de mettre la page de timeout car c'est Cerbere qui déconnecte
     */
    private boolean isSessionControlRequiredForThisResource(HttpServletRequest httpServletRequest) {
	String requestPath = httpServletRequest.getRequestURI();

	boolean controlRequired = !StringUtils.contains(requestPath, welcomePage);
	return controlRequired;
    }

    public String getTimeoutPage() {
	return timeoutPage;
    }

}
