package com.becitizen.app.becitizen.data;

import android.accounts.NetworkErrorException;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.becitizen.app.becitizen.domain.entities.FaqEntry;

import java.util.List;

public class ControllerFaqData {

    //URIs
    private static final String URI_INFORMATIONS_CATEGORY = "http://becitizen.cf/getAllInfoCategory?category=";

    private static AppDatabase myDB;
    private static ControllerFaqData instance = null;
    private Context context = null;
    private FaqEntryDao myFaqEntryDao = null;

    /**
     * Constructora por defecto para evitar que sea instanciado
     */
    protected ControllerFaqData() {
        // Exists only to defeat instantiation.
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerForumData
     */
    public static ControllerFaqData getInstance() {
        if(instance == null) instance = new ControllerFaqData();
        return instance;

    }

    /**
     * Necesario para utilizar la db
     * @param context Application context
     */
    public void setContext(Context context){
        this.context = context;
        myDB =  Room.databaseBuilder(context,
                AppDatabase.class, "FaqEntry").build();
        myFaqEntryDao = myDB.getFaqEntryDao();
    }

    /**
     * Metodo que solicita las faqs de una categoria.
     *
     * @param category nombre de la categoria
     *
     * @return La respuesta de nuestro servidor
     */
    public String getFaqCategory(String category) throws NetworkErrorException {
        return ServerAdapter.getInstance().doGetRequest(URI_INFORMATIONS_CATEGORY + category);
    }

    public List<FaqEntry> getFaqByCategoryFromDB(String category){
        return myFaqEntryDao.getByCategory(category);
    }

    public void insertMultiplesFaqsOnDB(List<FaqEntry> faqEntries){
        for(FaqEntry faq : faqEntries){
            myFaqEntryDao.insert(faq);
        }
    }

}
