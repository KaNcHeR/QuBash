package com.agrotrading.kancher.qubash.database.migrations;

import com.agrotrading.kancher.qubash.database.QuBashDatabase;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.database.models.Quote_Table;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Migration(version = 2, database = QuBashDatabase.class)
public class Migration1 extends AlterTableMigration<Quote> {

    public Migration1(Class<Quote> table) {
        super(table);
    }

    public Migration1() {
        super(Quote.class);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.get(boolean.class.getName()), Quote_Table.favorites.toString());
        addColumn(SQLiteType.get(long.class.getName()), Quote_Table.timeStamp.toString());
    }
}
