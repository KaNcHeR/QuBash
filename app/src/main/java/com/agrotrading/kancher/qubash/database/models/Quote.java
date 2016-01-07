package com.agrotrading.kancher.qubash.database.models;

import com.agrotrading.kancher.qubash.database.QuBashDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(database = QuBashDatabase.class)
public class Quote extends BaseModel {

    @PrimaryKey
    long id;

    @Column
    String html;


    public static List<Quote> getAllQuotes(int offset, int count) {

        return SQLite.select()
                .from(Quote.class)
                .orderBy(Quote_Table.id, false)
                .offset(offset)
                .limit(count)
                .queryList();
    }

    public static void deleteQuote(long id) {
         SQLite.delete(Quote.class)
                 .where(Quote_Table.id.eq(id))
                 .query();
    }

    public String getId() {
        return Long.toString(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

}
