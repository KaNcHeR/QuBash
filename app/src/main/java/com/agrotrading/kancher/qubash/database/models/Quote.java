package com.agrotrading.kancher.qubash.database.models;

import com.agrotrading.kancher.qubash.database.QuBashDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.List;

@Table(database = QuBashDatabase.class)
public class Quote extends BaseModel {

    @PrimaryKey
    long id;

    @Column
    String html;

    @Column
    boolean favorites;

    @Column
    long timeStamp;

    public static List<Quote> getAllQuotes(int offset, int count) {

        return SQLite.select()
                .from(Quote.class)
                .orderBy(Quote_Table.id, false)
                .offset(offset)
                .limit(count)
                .queryList();
    }

    public static List<Quote> getAllFavoriteQuotes(int offset, int count) {

        return SQLite.select()
                .from(Quote.class)
                .where(Quote_Table.favorites.is(true))
                .orderBy(Quote_Table.timeStamp, false)
                .offset(offset)
                .limit(count)
                .queryList();
    }

    public static void removeFromFavorites(long id) {

        SQLite.update(Quote.class)
                .set(Quote_Table.favorites.eq(false))
                .where(Quote_Table.id.is(id))
                .query();

    }

    public static void addToFavorites(long id) {

        SQLite.update(Quote.class)
                .set(
                        Quote_Table.favorites.eq(true),
                        Quote_Table.timeStamp.eq(new Date().getTime())
                )
                .where(Quote_Table.id.is(id)).query();
    }

    public long getId() {
        return id;
    }

    public String getStringId() {
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

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }
}
