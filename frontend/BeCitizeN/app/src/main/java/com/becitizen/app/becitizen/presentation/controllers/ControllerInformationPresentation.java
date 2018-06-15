package com.becitizen.app.becitizen.presentation.controllers;

import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.controllers.ControllerInformationDomain;
import com.becitizen.app.becitizen.domain.entities.Information;

import java.util.ArrayList;

public class ControllerInformationPresentation {

    static ControllerInformationPresentation uniqueInstance;

    ControllerInformationDomain controllerInformationDomain;

    /**
     * Constructora privada para que no se instancie
     * y inicializar valores
     */
    private ControllerInformationPresentation() {
        controllerInformationDomain = ControllerInformationDomain.getUniqueInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return Instancia
     */
    public static ControllerInformationPresentation getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ControllerInformationPresentation();
        }

        return uniqueInstance;
    }

    /**
     * Metodo que retorna todas las informaciones de una categoria
     * @param category nombre de la categoria
     * @return null si ha ocurrido algun error
     */
    public ArrayList<Information> getInformationsCategory(String category) throws NetworkErrorException {
        return controllerInformationDomain.getInformationsCategory(category);
    }

    /**
     * Metodo que retorna una informacion
     * @param id de la informacion
     * @return information vacia si hay algun error
     */
    public Information getInformation(int id) throws NetworkErrorException {
        return controllerInformationDomain.getInformation(id);
    }

    public ArrayList<Information> getInformationsCategorySearch(String category, String searchWords) throws NetworkErrorException {
        return controllerInformationDomain.getInformationsCategorySearch(category, searchWords);
    }
}
