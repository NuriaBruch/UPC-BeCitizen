package com.becitizen.app.becitizen.data;
import android.arch.persistence.room.*;
import android.content.Context;

import com.becitizen.app.becitizen.domain.entities.FaqEntry;


@Database(entities = {FaqEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FaqEntryDao getFaqEntryDao();

    private static AppDatabase INSTANCE;
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "FaqEntry")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
