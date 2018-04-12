package com.becitizen.app.becitizen.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPreferences {

    private static MySharedPreferences instance;
    private Context context;

    /**
     * Inicializa MySharedPreferences para usar las shared preferences
     * a partir del contexto recibido como parametro.
     * El metodo debe ser llamado antes de qualquier uso de MySharedPreferences
     *
     * @param context Contexto usado para obtener SharedPreferences
     */
    public static void init(Context context) {
        if(instance == null) {
            instance = new MySharedPreferences();
            instance.context = context;
        }
    }

    /**
     * Este metodo retorna la instancia del singleton.
     *
     * @return La instancia de MySharedPreferences
     * @throws SharedPreferencesException Si no se ha llamado previamente al metodo init()
     */
    public static MySharedPreferences getInstance() throws SharedPreferencesException {
        if(instance == null) {
            throw new SharedPreferencesException("MySharedPreferences has not been initialized");
        }
        return instance;
    }

    /**
     * Este metodo permite guardar un valor en SharedPreferences
     *
     * @param name Nombre que recibiran los SharedPreferences usados
     * @param key Clave del valor guardado
     * @param value Valor guardado
     */
    public void saveValue(String name, String key, String value) {

        SharedPreferences settings = context.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Este metodo permite obtener un valor guardado en SharedPreferences
     *
     * @param name Nombre de los SharedPreferences
     * @param key Clave del valor que se quiere obtener
     * @return Valor guardado en SharedPreferences name con clave key, si no existe devuelve un String vacio
     */
    public String getValue(String name, String key) {

        SharedPreferences settings = context.getSharedPreferences(name, MODE_PRIVATE);
        return settings.getString(key, "");
    }

}
