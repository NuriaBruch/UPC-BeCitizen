package com.becitizen.app.becitizen.presentation;

import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.ControllerThreadDomain;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Comment;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ControllerThreadPresentation {

    static ControllerThreadPresentation uniqueInstance;

    private ControllerThreadDomain controllerThreadDomain;

    /**
     * Constructora privada para que no se instancie
     * y inicializar valores
     */
    private ControllerThreadPresentation() {
        controllerThreadDomain = ControllerThreadDomain.getUniqueInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     * @return Instancia
     */
    public static ControllerThreadPresentation getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ControllerThreadPresentation();
        }

        return uniqueInstance;
    }

    /**
     * Metodo que retorna todos los threads de una categoria
     * @param category name of the category
     * @return empty arraylist si ha ocurrido algun error
     */
    public ArrayList<CategoryThread> getThreadsCategory(String category, int block, boolean sortedByVotes) throws NetworkErrorException {
        return controllerThreadDomain.getThreadsCategory(category, block, sortedByVotes);
    }

    /**
     * Metodo que retorna todas las categorias
     * @return empty arraylist si ha ocurrido algun error
     */
    public ArrayList<String> getCategories() throws NetworkErrorException {
        return controllerThreadDomain.getCategories();
    }

    /**
     * Metodo que crea un nuevo thread
     * @return empty arraylist si ha ocurrido algun error
     */
    public boolean newThread(Thread t) throws ServerException, NetworkErrorException {
        return controllerThreadDomain.newThread(t);
    }


    public Thread getThreadContent(int id) throws JSONException, ServerException, NetworkErrorException {
        return controllerThreadDomain.getThreadContent(id);
    }

    public List<Comment> getThreadComments(int id, boolean sortedByVotes) throws JSONException, ServerException, NetworkErrorException {
        return controllerThreadDomain.getThreadComments(id, sortedByVotes);
    }

    public void newComment(String commentText, int threadId) throws ServerException, JSONException, NetworkErrorException {
        controllerThreadDomain.newComment(commentText, threadId);
    }

    /**
     * Metodo que otorga un voto un thread por el usuario identificado.
     *
     * @param threadId  Identificador del thread que se quiere votar
     * @throws ServerException Si el servidor devuelve algún error
     * @throws JSONException Si se produce algún error al crear o leer JSONs
     */
    public void voteThread(int threadId) throws ServerException, JSONException, NetworkErrorException {
        controllerThreadDomain.voteThread(threadId);
    }

    /**
     * Metodo que reporta un thread por el usuario identificado.
     *
     * @param threadId  Identificador del thread que se quiere reportar
     * @throws ServerException Si el servidor devuelve algún error
     * @throws JSONException Si se produce algún error al crear o leer JSONs
     */
    public void reportThread(int threadId) throws ServerException, JSONException, NetworkErrorException {
        controllerThreadDomain.reportThread(threadId);
    }

    /**
     * Metodo que otorga un voto a un comentario por el usuario identificado.
     *
     * @param commentId  Identificador del comentario que se quiere votar
     * @throws ServerException Si el servidor devuelve algún error
     * @throws JSONException Si se produce algún error al crear o leer JSONs
     */
    public void voteComment(int commentId) throws ServerException, JSONException, NetworkErrorException {
        controllerThreadDomain.voteComment(commentId);
    }

    /**
     * Metodo que reporta un comentario por el usuario identificado.
     *
     * @param commentId  Identificador del comentario que se quiere reportar
     * @throws ServerException Si el servidor devuelve algún error
     * @throws JSONException Si se produce algún error al crear o leer JSONs
     */
    public void reportComment(int commentId) throws ServerException, JSONException, NetworkErrorException {
        controllerThreadDomain.reportComment(commentId);
    }

    public ArrayList<CategoryThread> getThreadsCategorySearch(String category, int block, boolean sortedByVotes, String searchWords) {
        return controllerThreadDomain.getThreadsCategorySearch(category, block, sortedByVotes, searchWords);
    }
}
