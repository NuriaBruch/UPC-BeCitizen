package com.becitizen.app.becitizen.data;

import android.accounts.NetworkErrorException;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.becitizen.app.becitizen.domain.entities.FaqEntry;

import java.util.List;

public class ControllerFaqData {

    //URIs
    //private static final String URI_FAQS_CATEGORY = "http://becitizen.cf/getAllFaqCategory?category=";
    private static final String URI_FAQS_CATEGORY = "http://10.0.2.2:1337/getAllFaqCategory?category=";
    private static final String URI_FAQ_REPORT = "http://10.0.2.2:1337/reportFaq?faqId=";

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
     * Metodo que solicita los nombres de todas las categorias.
     *
     * @return La respuesta de nuestro servidor
     */
    public String getCategories() throws NetworkErrorException{
        return ServerAdapter.getInstance().doGetRequest(ControllerForumData.URI_CATEGORIES);
    }

    /**
     * Metodo que solicita las faqs de una categoria.
     *
     * @param category nombre de la categoria
     *
     * @return La respuesta de nuestro servidor
     */
    public String getFaqCategory(String category) throws NetworkErrorException {
        return ServerAdapter.getInstance().doGetRequest(URI_FAQS_CATEGORY + category);
    }

    public List<FaqEntry> getFaqByCategoryFromDB(String category){
        return myFaqEntryDao.getByCategory(category);
    }

    public List<String> getFaqCategoriesFromDB(){
        return myFaqEntryDao.getCategories();
    }

    public void insertMultiplesFaqsOnDB(List<FaqEntry> faqEntries){
        for(FaqEntry faq : faqEntries){
            myFaqEntryDao.insert(faq);
        }
    }

    public List<FaqEntry> getFaqByKeyWordAndCategory(String category, String keyword){
        return myFaqEntryDao.getByWordAndCategroy(category, keyword);
    }

    public String reportFaq(int id){
        String[] dataRequest = {URI_FAQ_REPORT + id, ""};
        return ServerAdapter.getInstance().doPostRequest(dataRequest);
    }

    public String rateFaq(int id, float rating){
        String[] dataRequest = {URI_FAQ_REPORT + id, ""};
        return ServerAdapter.getInstance().doPostRequest(dataRequest);
    }

}
