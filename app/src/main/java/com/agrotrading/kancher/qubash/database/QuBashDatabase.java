package com.agrotrading.kancher.qubash.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = QuBashDatabase.NAME, version = QuBashDatabase.VERSION)
public class QuBashDatabase {

    public static final String NAME = "Quotes";
    public static final int VERSION = 1;
}
