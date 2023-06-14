package com.wx.common.realm;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by huy  on 2022/12/21.
 */
public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
//        if (oldVersion == 1) {
//            RealmObjectSchema budgetSchema = schema.create("BudgetBean")
//                    .addField("id", Integer.class, FieldAttribute.PRIMARY_KEY).setRequired("id",true)
//                    .addField("budgetName", String.class, FieldAttribute.REQUIRED)
//                    .addField("totalBudget", String.class, FieldAttribute.REQUIRED)
//                    .addField("typeIds", String.class, FieldAttribute.REQUIRED)
//                    .addField("month", String.class, FieldAttribute.REQUIRED)
//                    .addField("createTime", Long.class, FieldAttribute.REQUIRED);
//            oldVersion++;
//        }
    }
}
