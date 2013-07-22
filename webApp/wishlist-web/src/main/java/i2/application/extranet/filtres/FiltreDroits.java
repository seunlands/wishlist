/**
 * 
 */
package i2.application.extranet.filtres;

import i2.application.extranet.bean.ExTypeDroit;
import i2.application.extranet.enums.EnumDroits;
import i2.application.extranet.enums.EnumTypeAcces;
import i2.application.extranet.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.UrlPathHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * FiltreDroits
 * 
 * @author Erik Lenoir
 * 
 */

public class FiltreDroits implements Filter {

	private final Log logger = LogFactory.getLog(FiltreDroits.class);

	/* Parametres de configuration */
	protected FilterConfig config;

	/* Page d'accueil et page d'erreur */
	private String ENTREE_APPLICATION;
	private String ERREUR_APPLICATION;
	private String ERREUR_AE;
	private static final String DECONNEXION="/deconnexion.do";

	/* Clés des différents paramètres */
	public static final String CONFIG_FILE_KEY = "config";
	public static final String CONFIG_ENTREE_APPLICATON = "applicationEntree";
	public static final String CONFIG_ERREUR = "applicationErreur";
	public static final String CONFIG_ERREUR_AE = "applicationErreurAE";

	/* Valeurs par défaut */
	public static final String DEFAULT_CONFIG_FILE = "/WEB-INF/classes/filtredroits.xml";
	public static final String DEFAULT_ENTREE_APPLICATION = "/accueil/init.do";
	public static final String DEFAULT_ERREUR_PAGE = "/erreurs/afficher.do";
	public static final String DEFAULT_ERREUR_AE_PAGE = "/erreurs/autoecole.do";

	/* Valeur de l'en-tete HTTP pour la provenance de l'utilisateur */
	public static final String HTTP_ORIGINE = "X-origine.UtilisateurProvenance";
	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	/*
	 * Inner-bean pour représenter les droits une url Un typeAccess
	 * (internet,intranet) => peut être NULL Une liste d'entier correspondant
	 * aux droits => peut être vide
	 */
	private class DroitsURL {
		private String typeAcces;

		private List<Integer> droits;

		public String getTypeAcces() {
			return typeAcces;
		}

		public void setTypeAcces(String typeAcces) {
			this.typeAcces = typeAcces;
		}

		public List<Integer> getDroits() {
			return droits;
		}

		public void setDroits(List<Integer> droits) {
			this.droits = droits;
		}
	}

	/* Map contenant les urls et les droits associés */
	private Map<String, DroitsURL> mapUrlDroits;

	/* Regex pour détecter les medias (images,js,css...) */
	private static String MEDIA_RE = ".*\\.(?:(?:gif)|(?:jpe?g)|(?:png)|(?:css)|(?:js)|(?:ico))";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// Rien a faire
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if ((request instanceof HttpServletRequest)
				&& (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;

			// URL de la page demandée (décodée)
			String pageDemandee = urlPathHelper.getLookupPathForRequest(httpServletRequest);
			// Contexte
			String context = httpServletRequest.getContextPath();
			// URL sans contexte
			String pageDemandeeSansContext=pageDemandee.replace(context, "");
			
			// A l'entrée la Map de droits n'est pas initialisée, seule la page
			// d'entrée de l'application peut se trouver dans ce cas
			if (httpServletRequest.getSession().getAttribute(
					Constants.SES_DROITS) == null
					&& !pageDemandeeSansContext.equals(ENTREE_APPLICATION)) {
				// Redirection sale (car ce cas n'est pas normal du tout)
				httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
					
			// Evite de récupérer une Map nulle à l'entrée
			if (httpServletRequest.getSession().getAttribute(
					Constants.SES_DROITS) != null) {
				// Si on a pas déjà vu la requete
				if ((request.getAttribute("ALREADY_PROCESSED") == null)
						|| !(request.getAttribute("ALREADY_PROCESSED")
								.equals("TRUE"))) 
				{
					request.setAttribute("ALREADY_PROCESSED", "TRUE");
					// Si ce n'est pas un Media (png, css, js...)
					if (!estMedia(pageDemandeeSansContext)) {
						// Si il y a une erreur interdisant la navigation totale 
						// => Sauf sur la page d'erreur et la page de déconnexion
						if (httpServletRequest.getSession().getAttribute(Constants.SES_ERREUR) != null
								&& !pageDemandeeSansContext.equals(ERREUR_AE) 
								&& !pageDemandeeSansContext.equals(DECONNEXION)) {
							httpServletResponse.sendRedirect(context+ERREUR_AE);
							return;
						}
						// Récupération des droits
						List<ExTypeDroit> droits = (List<ExTypeDroit>) httpServletRequest
								.getSession().getAttribute(Constants.SES_DROITS);
						if (logger.isDebugEnabled()) {
							logger.debug("URL (page demandée): " + pageDemandeeSansContext);
						}
						// Transformation pour faciliter les tests
						List<Integer> droitsUser = new ArrayList<Integer>();
						if (droits!=null) {
							for(ExTypeDroit d : droits) {
								droitsUser.add(d.getIdTypeDroit());
							}
						}
						// Parcours de la map d'urls
						boolean valable=true;
						for(Entry<String,DroitsURL> entree : mapUrlDroits.entrySet()) {
							String cle = entree.getKey();
							// Si on a une correspondance (page<=>url)
							if (pageDemandeeSansContext.matches(cle)) {
								if (logger.isDebugEnabled())
									logger.debug("URL (page filtrée) : "+cle+" a partir de "+pageDemandeeSansContext);
								// Type d'accès
								String typeAcces = entree.getValue().getTypeAcces();
								// On va chercher des informations sur les headers
								String origineHTTP = httpServletRequest.getHeader(HTTP_ORIGINE);
								if (logger.isDebugEnabled())
									logger.debug("ORIGINE HTTP : "+origineHTTP);
								// Si on a récupéré le type d'acces dans l'entete HTTP
								if (typeAcces != null) {
									// Si l'origine est un réseu ader on le considère comme intranet
									if (origineHTTP != null && origineHTTP.equalsIgnoreCase("ader")) {
										origineHTTP = "intranet";
									}
									// On test la correspondance
									if (!typeAcces.equalsIgnoreCase(origineHTTP)) {
										if (logger.isDebugEnabled())
											logger.debug("ORIGINE FILTREE EN : "+typeAcces);
										valable=false;
										break;
									}
								}
								// Droits
								List<Integer> droitsVoulus = entree.getValue().getDroits();
								for(Integer droitVoulu : droitsVoulus) {
									if (!droitsUser.contains(droitVoulu)) {
										if (logger.isDebugEnabled())
											logger.debug("DROIT NON ACQUIS : "+droitVoulu);
										valable=false;
										break;
									}
								}
							}
						}
						// Sortie du for,  tester le boolean valable
						if (logger.isDebugEnabled())
							logger.debug("BOOL AUTORISATION : "+valable);
						if (!valable) {
							// Redirection propre sur la page d'erreur
							httpServletResponse.sendRedirect(context+ERREUR_APPLICATION);
							return;
						}
						// Cas du /
						else if (valable && pageDemandeeSansContext.equals("/")) {
							httpServletResponse.sendRedirect(context+ENTREE_APPLICATION);
							return;
						}
					}
				}
			}

		}

		chain.doFilter(request, response);
	}

	private boolean estMedia(String URL) {
		return (URL.matches(MEDIA_RE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
		String configFile = config.getInitParameter(CONFIG_FILE_KEY);
		String entree = config.getInitParameter(CONFIG_ENTREE_APPLICATON);
		String erreur = config.getInitParameter(CONFIG_ERREUR);
		String erreurAE = config.getInitParameter(CONFIG_ERREUR_AE);
		if (configFile == null) {
			configFile = DEFAULT_CONFIG_FILE;
		}
		if (entree == null) {
			this.ENTREE_APPLICATION = DEFAULT_ENTREE_APPLICATION;
		} else {
			this.ENTREE_APPLICATION = entree;
		}
		if (erreur == null) {
			this.ERREUR_APPLICATION = DEFAULT_ERREUR_PAGE;
		} else {
			this.ERREUR_APPLICATION = erreur;
		}
		if (erreurAE == null) {
			this.ERREUR_AE = DEFAULT_ERREUR_AE_PAGE;
		} else {
			this.ERREUR_APPLICATION = erreurAE;
		}
		try {
			// Parsage du fichier XML (inutile de faire des verifs la DTD
			// garantit la
			// correction de la sémantique du fichier
			URL configURL = config.getServletContext().getResource(configFile);
			InputStream stream = configURL.openStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList listeNoeuds = doc.getElementsByTagName("filtre");
			mapUrlDroits = new HashMap<String, DroitsURL>();
			for (int i = 0; i < listeNoeuds.getLength(); i++) {
				Node pere = listeNoeuds.item(i);
				DroitsURL droitsURL = new DroitsURL();
				if (pere.getNodeType() == Node.ELEMENT_NODE) {
					Element elementPere = (Element) pere;
					// Récupération de l'URL
					Element urlElmt = (Element) elementPere
							.getElementsByTagName("url").item(0);
					String url = urlElmt.getChildNodes().item(0).getNodeValue();
					// Remplacement correcte de l'étoile pour interprétation
					url = url.replace("*", ".*");
					// Récupération du type d'accès
					Element typeAccesElmt = (Element) elementPere
							.getElementsByTagName("typeAcces").item(0);
					if (typeAccesElmt != null) {
						String typeAcces = typeAccesElmt.getChildNodes()
								.item(0).getNodeValue();
						droitsURL.setTypeAcces(EnumTypeAcces.valueOf(typeAcces)
								.getType());
					}
					// Récupération des types de droits acceptes
					List<Integer> droits = new ArrayList<Integer>();
					NodeList droitsElmt = elementPere
							.getElementsByTagName("typeDroit");
					for (int j = 0; j < droitsElmt.getLength(); j++) {
						Element droitElmt = (Element) droitsElmt.item(j);
						String nomDroit = droitElmt.getChildNodes().item(0)
								.getNodeValue();
						droits.add(EnumDroits.valueOf(nomDroit).getId());
					}
					droitsURL.setDroits(droits);
					mapUrlDroits.put(url, droitsURL);
				}
			}
		} catch (MalformedURLException e) {
			throw new ServletException("Probleme fichier de conf : " + config);
		} catch (IOException e) {
			throw new ServletException(
					"IO : Probleme lecture fichier de conf : " + e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new ServletException("Probleme parse fichier XML : " + config);
		} catch (SAXException e) {
			throw new ServletException("Probleme SAX fichier XML : " + config);
		} catch (IllegalArgumentException e) {
			throw new ServletException("Droit inconnu : " + e.getMessage());
		}

	}

}
