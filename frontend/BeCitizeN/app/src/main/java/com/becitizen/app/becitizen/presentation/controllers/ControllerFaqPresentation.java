package com.becitizen.app.becitizen.presentation.controllers;

import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.controllers.ControllerFaqDomain;
import com.becitizen.app.becitizen.domain.entities.Information;

import java.util.ArrayList;

public class ControllerFaqPresentation {

    static ControllerFaqPresentation uniqueInstance;

    ControllerFaqDomain controllerFaqDomain;

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

    /**
     * Metodo que retorna todas las informaciones de una categoria
     * @param category nombre de la categoria
     * @return null si ha ocurrido algun error
     */
    public ArrayList<Information> getFaqsCategory(String category) throws NetworkErrorException {
        return controllerFaqDomain.getFaqsCategory(category);
    }

    /**
     * Metodo que retorna una informacion
     * @param id de la informacion
     * @return information vacia si hay algun error
     */
    public boolean RateFaq(int id) throws NetworkErrorException {
        return controllerFaqDomain.rateFaq(id, 0.0f);
    }
}
