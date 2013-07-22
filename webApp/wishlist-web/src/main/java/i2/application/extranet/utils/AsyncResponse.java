package i2.application.extranet.utils;

import java.io.IOException;

/**
 * AsyncResponse permet de repondre à une requête ajax. L'envoie des reponses passe par une ecriture de chaine directement au client. Les implementations de cette interfaces fourni un format
 * specifique pour l'encodage des données envoyées
 * 
 */

public interface AsyncResponse {

    public void addResponse(String key, Object value);

    public void addMessage(String msg);

    public void sendError() throws IOException;

    public void sendWarn() throws IOException;

    public void sendSuccess() throws IOException;

    public void sendFail() throws IOException;

    public void send() throws IOException;

}
