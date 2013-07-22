package i2.application.extranet.utils;

import i2.application.extranet.utils.convert.JsonConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Implment l'interface <code>AsyncResponse</code>. AsyncJsonResponseWrapper fourni un wrapper de reponse <code>HttpServletResponse</code>. passe par une avec JSON comme corps de rgrace à un
 * convertiseur prcesiser lors de l'instanciation de la classeet les adapter à une reponse servlet <code>HttpServletResponse</code>
 * 
 * @author BULL SAS
 * @version 1.0
 * 
 */
public class AsyncJsonResponseWrapper implements AsyncResponse {
    private JSONObject jsonResponse;
    private HttpServletResponse servletResponse;
    private List<String> listMessage;
    private JsonConverter converter;

    public AsyncJsonResponseWrapper(HttpServletResponse servletResponse, JsonConverter converter) {
	this.servletResponse = servletResponse;
	this.converter = converter;
	this.servletResponse.setContentType("text/javascript");
	this.servletResponse.setCharacterEncoding("UTF-8");
	this.servletResponse.setHeader("Cache-Control", "no-cache");
	this.jsonResponse = new JSONObject();
	this.listMessage = new ArrayList<String>();
    }

    @Override
    public void sendError() throws IOException {
	printResponse(true, "error");
    }

    @Override
    public void sendWarn() throws IOException {
	printResponse(true, "warn");
    }

    @Override
    public void sendSuccess() throws IOException {
	printResponse(true, "success");
    }

    @Override
    public void sendFail() throws IOException {
	if (listMessage.isEmpty()) {
	    listMessage.add("Une erreur est survenue pendant le traitement.");
	}
	printResponse(false, "server");
    }

    @Override
    public void send() throws IOException {
	printResponse(true, null);
    }

    @Override
    public void addResponse(String key, Object value) {
	jsonResponse.put(key, converter.getAsJson(value));
    }

    @Override
    public void addMessage(String msg) {
	listMessage.add(msg);
    }

    private void printResponse(boolean isSucess, String code) throws IOException {
	JSONArray messageArray = converter.getAsJsonArray(listMessage);
	JSONObject response = new JSONObject();
	jsonResponse.put("root", "data");
	response.put("success", isSucess);
	response.put("response", jsonResponse);
	response.put("code", code);
	response.put("msg", messageArray);
	servletResponse.getWriter().print(response.toString());
    }

}
