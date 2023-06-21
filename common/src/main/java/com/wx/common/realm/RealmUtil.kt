package com.wx.common.realm

import com.wx.common.bean.LocalWalletBean
import com.wx.common.util.Web3jUtil
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
     * 根据地址获取钱包对象
     */
    fun findWalletByAddress(address: String): List<LocalWalletBean> {
        return realm.where(LocalWalletBean::class.java).equalTo("address", address).findAll()
    }

    /**
     * 查询所有钱包，并根据timeStamp排序
     */
    fun findWalletList(): List<LocalWalletBean> {
        return realm.where(LocalWalletBean::class.java).findAll().sort("timeStamp", Sort.DESCENDING)
    }

    /**
     * 查询ETH链钱包，并根据timeStamp排序
     */
    fun findETHWalletList(): List<LocalWalletBean> {
        return realm.where(LocalWalletBean::class.java).equalTo("chainId", Web3jUtil.instance?.ETH_CHAIN_ID_DEV).or()
            .equalTo("chainId", Web3jUtil.instance?.ETH_CHAIN_ID_RELEASE).findAll().sort("timeStamp", Sort.DESCENDING)
    }

    /**
     * 查询BSC链钱包，并根据timeStamp排序
     */
    fun findBSCWalletList(): List<LocalWalletBean> {
        return realm.where(LocalWalletBean::class.java).equalTo("chainId", Web3jUtil.instance?.BSC_CHAIN_ID_DEV).or()
            .equalTo("chainId", Web3jUtil.instance?.BSC_CHAIN_ID_RELEASE).findAll().sort("timeStamp", Sort.DESCENDING)
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