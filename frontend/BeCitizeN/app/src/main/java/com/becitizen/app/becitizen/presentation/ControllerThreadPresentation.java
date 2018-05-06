package com.becitizen.app.becitizen.presentation;

import com.becitizen.app.becitizen.domain.ControllerThreadDomain;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Comment;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

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
     *
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
     *
     * @param category name of the category
     * @return empty arraylist si ha ocurrido algun error
     */
    public ArrayList<CategoryThread> getThreadsCategory(String category) {
        return controllerThreadDomain.getThreadsCategory(category);
    }

    /**
     * Metodo que retorna todas las categorias
     *
     * @return empty arraylist si ha ocurrido algun error
     */
    public ArrayList<String> getCategories() {
<<<<<<< HEAD
        return controllerThreadDomain.getCategories();
=======
        JSONObject data = controllerThreadDomain.getCategories();
        ArrayList<String> threads = new ArrayList<>();
        //try {

            return threads;
        //} catch (JSONException e) {
        //    return threads;
        //}
>>>>>>> 087b527a2646fd1dc2661d4dae172a57edb3deed
    }

    /**
     * Metodo que crea un nuevo thread
     *
     * @return empty arraylist si ha ocurrido algun error
     */
    public boolean newThread(Thread t) throws ServerException {
        //TODO: delete line below
        //t = new Thread("hi","test", "justice");
        return controllerThreadDomain.newThread(t);
    }


    public Thread getThreadContent(int id) throws JSONException {
        return controllerThreadDomain.getThreadContent(id);
    }

    public List<Comment> getThreadComments(int id) throws JSONException {
        return controllerThreadDomain.getThreadComments(id);
    }

    public void newComment(String commentText, int threadId) {
        controllerThreadDomain.newComment(commentText, threadId);
    }

    public void voteThread(int threadId) {
        controllerThreadDomain.voteThread(threadId);
    }

    public void reportThread(int threadId) {
        controllerThreadDomain.reportThread(threadId);
    }

    public void voteComment(int commentId) {
        controllerThreadDomain.voteComment(commentId);
    }

    public void reportComment(int commentId) {
        controllerThreadDomain.reportComment(commentId);
    }

}
