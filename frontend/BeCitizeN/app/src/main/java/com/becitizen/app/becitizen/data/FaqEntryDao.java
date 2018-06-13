package com.becitizen.app.becitizen.data;
import android.arch.persistence.room.*;

import com.becitizen.app.becitizen.domain.entities.FaqEntry;

import java.util.List;

@Dao
public interface FaqEntryDao {
    @Query("SELECT * FROM FAQENTRY")
    List<FaqEntry> getAll();

    @Query("SELECT * FROM FAQENTRY WHERE category =:category and question LIKE :word ")
    List<FaqEntry> getByWordAndCategroy(String category, String word);

    @Query("Select * From FaqEntry Where category = :category")
    List<FaqEntry> getByCategory(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FaqEntry faqEntry);

    @Query("Select distinct category From FaqEntry")
    List<String> getCategories();

    @Delete
    void delete(FaqEntry faqEntry);
}
