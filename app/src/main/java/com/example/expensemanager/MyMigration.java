package com.example.expensemanager;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // Define your migration logic here
        if (oldVersion == 0) {
            // Perform migration for schema version 0 to 1
            // For example, you can add the Transaction class
            realm.getSchema().create("Transaction")
                    .addField("type", String.class)
                    .addField("category", String.class)
                    .addField("paymentMethod", String.class)
                    .addField("note", String.class)
                    .addField("date", Date.class)
                    .addField("amount", double.class)
                    .addField("id", long.class);
            oldVersion++;
        }
    }
}
