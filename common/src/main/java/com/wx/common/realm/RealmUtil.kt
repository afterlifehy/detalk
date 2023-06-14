package com.wx.common.realm

import io.realm.*

class RealmUtil {
    private val versionCode = 1
    private var transaction: RealmAsyncTask? = null
    private val config: RealmConfiguration = RealmConfiguration.Builder() // 文件名
        .name("detalk.realm") // 版本号
        .schemaVersion(versionCode.toLong())
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .migration(MyMigration())
        .build()
    val realm: Realm
        get() = Realm.getDefaultInstance()
    private val realmTask: RealmAsyncTask?
        private get() = transaction

    companion object {
        private var realmUtil: RealmUtil? = null

        @get:Synchronized
        val instance: RealmUtil?
            get() {
                if (realmUtil == null) realmUtil = RealmUtil()
                return realmUtil
            }
        val instanceBlock: RealmUtil?
            get() {
                if (realmUtil == null) synchronized(RealmUtil::class.java) {
                    if (realmUtil == null) realmUtil = RealmUtil()
                }
                return realmUtil
            }
    }

    init {
        Realm.setDefaultConfiguration(config)
    }

    fun addRealmAsync(realmObject: RealmObject) {
        transaction =
            realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(realmObject) }
    }

    /**
     * 新增category
     */
    fun addRealm(realmObject: RealmObject) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(realmObject) }
    }

    /**
     * 添加默认category
     */
    fun addRealmAsyncList(realmObjectList: List<RealmObject>) {
        transaction =
            realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(realmObjectList) }
    }

    fun addRealmList(realmObjectList: List<RealmObject>) {
        transaction =
            realm.executeTransactionAsync { realm -> realm.insert(realmObjectList) }
    }

    /**
     * 删除
     */
    fun deleteRealm(realmObject: RealmObject) {
        realm.executeTransaction { transactionRealm ->
            realmObject.deleteFromRealm()
        }
    }

    fun deleteRealmAsync(realmList: RealmResults<RealmObject>) {
        realm.executeTransactionAsync {
            realmList.deleteAllFromRealm()
        }
    }


}