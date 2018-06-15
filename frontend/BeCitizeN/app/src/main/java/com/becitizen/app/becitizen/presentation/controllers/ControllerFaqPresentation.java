package com.becitizen.app.becitizen.presentation.controllers;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.becitizen.app.becitizen.domain.controllers.ControllerFaqDomain;
import com.becitizen.app.becitizen.domain.entities.FaqEntry;

import java.util.ArrayList;

public class ControllerFaqPresentation {

    static ControllerFaqPresentation uniqueInstance;

    ControllerFaqDomain controllerFaqDomain;
    private Context context;

    /**
     * Constructora privada para que no se instancie
     * y inicializar valores
     */
    private ControllerFaqPresentation() {
        controllerFaqDomain = ControllerFaqDomain.getUniqueInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return Instancia
     */
    public static ControllerFaqPresentation getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ControllerFaqPresentation();
        }

        return uniqueInstance;
    }

    public void setContext(Context context) {
        this.context = context;
        controllerFaqDomain.setContext(context);
    }

    /**
     * Metodo que retorna todas las categorias
     * @return empty arraylist si ha ocurrido algun error
     */
    public ArrayList<String> getCategories() throws NetworkErrorException {
        return controllerFaqDomain.getCategories(false);
    }

    /**
     * Metodo que retorna todas las categorias despues de actualizar la base de datos
     * @return empty arraylist si ha ocurrido algun error
     */
    public ArrayList<String> getCategoriesForceRefresh() throws NetworkErrorException {
        return controllerFaqDomain.getCategories(true);
    }

    /**
     * Metodo que retorna todas las faqs de una categoria
     * @param category nombre de la categoria
     * @return null si ha ocurrido algun error
     */
    public ArrayList<FaqEntry> getFaqsCategory(String category) throws NetworkErrorException {
        return controllerFaqDomain.getFaqsCategory(category);
    }

    /**
     * Metodo que retorna todas las faqs de una categoria coincidentes con la busqueda
     * @param category nombre de la categoria
     * @param word nombre de la categoria
     * @return null si ha ocurrido algun error
     */
    public ArrayList<FaqEntry> getFaqsCategoryWord(String category, String word) throws NetworkErrorException {
        return controllerFaqDomain.getFaqsCategoryWord(category, word);
    }

    /**
     * Metodo que retorna una informacion
     * @param id de la informacion
     * @param rating valoracion de la pregunta
     * @return information vacia si hay algun error
     */
    public boolean rateFaq(int id, int rating) throws NetworkErrorException {
        return controllerFaqDomain.rateFaq(id, rating);
    }


    /**
     * Metodo que retorna una informacion
     * @param id de la informacion
     * @return information vacia si hay algun error
     */
    public boolean reportFaq(int id) throws NetworkErrorException {
        return controllerFaqDomain.reportFaq(id);
    }
}
