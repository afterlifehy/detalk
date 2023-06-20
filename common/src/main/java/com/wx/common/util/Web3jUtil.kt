package com.wx.common.util

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.ThreadUtils
import com.google.protobuf.ByteString
import com.wx.base.BaseApplication
import com.wx.base.BuildConfig
import com.wx.base.ds.PreferencesDataStore
import com.wx.base.ds.PreferencesKeys
import com.wx.base.util.Constant
import com.wx.base.util.ToastUtil
import com.wx.common.bean.NFTUint256Bean
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import wallet.core.jni.proto.Ethereum
import java.math.BigInteger
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


/**
 * Created by huy  on 2022/8/19.
 */
class Web3jUtil private constructor() {
    var wallet: HDWallet? = null
    var address = ""
    var web3j: Web3j? = null

    val BSC_CHAIN_ID_DEV = 97L
    val ETH_CHAIN_ID_DEV = 5L

    val BSC_CHAIN_ID_RELEASE = 56L
    val ETH_CHAIN_ID_RELEASE = 1L

    var CHAIN_ID = 0L

    var myCoinType = CoinType.SMARTCHAIN

    private val ERC20_TOKEN_BALANCE_FUNCTION_NAME = "balanceOf"
    private val ERC20_TOKEN_TRANSFER_FUNCTION_NAME = "transfer"
    private val NFT_LIST_FUNCTION_NAME = "userNftList"
    private val TOKEN_URI_FUNCTION_NAME = "tokenURI"
    private val IS_OPEN_FUNCTION_NAME = "isOpen"
    private val TRANSFER_FROM_FUNCTION_NAME = "transferFrom"

    companion object {
        private var mWeb3jUtil: Web3jUtil? = null
        val instance: Web3jUtil?
            get() {
                if (mWeb3jUtil == null) {
                    synchronized(Web3jUtil::class.java) {
                        if (mWeb3jUtil == null) {
                            mWeb3jUtil = Web3jUtil()
                        }
                    }
                }
                return mWeb3jUtil
            }
    }

    fun createWallet(passphrase: String): HDWallet {
        wallet = HDWallet(128, passphrase)
        return wallet as HDWallet
    }

    fun importWallet(mnemonic: String, passphrase: String): HDWallet {
        wallet = HDWallet(mnemonic, passphrase)
        return wallet as HDWallet
    }

    fun getHDWallet(): HDWallet {
        return wallet as HDWallet
    }

    fun getWalletAddress(): String {
        address = wallet?.getAddressForCoin(myCoinType).toString()
        return address
    }

    fun getMnemonic(): String {
        if (wallet != null) {
            return wallet?.mnemonic().toString()
        }
        return ""
    }

    fun buildWeb3j(httpServiceUrl: String): Web3j {
        web3j = Web3j.build(
            HttpService(
                httpServiceUrl,
                OkHttpClient().newBuilder().hostnameVerifier(object : HostnameVerifier {
                    override fun verify(hostname: String?, session: SSLSession?): Boolean {
                        return true
                    }
                }).connectTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build()
            )
        )
        return web3j as Web3j
    }

    fun getGasPrice(): BigInteger? {
        try {
            val gasPrice = web3j?.ethGasPrice()?.send()?.gasPrice
            return gasPrice
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread {
                ToastUtil.showToast(e.message.toString())
            }
        }
        return BigInteger("0")
    }

    /**
     * ERC20-token的查询余额的函数名
     */
    fun ethGetBalance(): Double {
        try {
            if (web3j != null) {
                val balance = web3j?.ethGetBalance(address, DefaultBlockParameterName.LATEST)?.send()?.balance
                return Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toDouble()
            }
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread {
                ToastUtil.showToast(e.message.toString())
            }
        }
        return 0.0
    }

    /**
     * 获取vst vmt余额
     */
    fun getBepBalance(address: String, contractAddress: String): Double {
        val function = Function(
            ERC20_TOKEN_BALANCE_FUNCTION_NAME,
            listOf(Address(address)) as List<Type<Any>>, listOf()
        )
        try {
            val ethCall = web3j!!.ethCall(
                Transaction.createEthCallTransaction(
                    address,
                    contractAddress,
                    FunctionEncoder.encode(function)
                ), DefaultBlockParameterName.LATEST
            ).send()
            return Convert.fromWei(Numeric.toBigInt(ethCall.value).toString(), Convert.Unit.ETHER).toDouble()
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread {
                ToastUtil.showToast(e.message.toString())
            }
        }
        return 0.0
    }

    fun getPrivateKey(coinType: CoinType): ByteString? {
        val secretPrivateKey = wallet?.getKeyForCoin(coinType)
        return ByteString.copyFrom(secretPrivateKey?.data())
    }

    /**
     * bnb划转
     */
    fun transfer(toAddressString: String, amountString: String): Boolean {
        val ethGetTransactionCount =
            web3j?.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING)?.sendAsync()?.get()
        var nonceCount = ethGetTransactionCount?.transactionCount
        val signerInput = Ethereum.SigningInput.newBuilder().apply {
            nonce = ByteString.copyFrom(nonceCount?.toByteArray())
            chainId = ByteString.copyFrom(BigInteger(CHAIN_ID.toString()).toByteArray())
            gasPrice = ByteString.copyFrom(instance?.getGasPrice()?.toByteArray())
//            (BigInteger("37e11d600", 16)).toByteArray()
//            instance?.getGasPrice()?.toByteArray()
            gasLimit = ByteString.copyFrom((BigInteger("19a28", 16)).toByteArray())
            toAddress = toAddressString
            transaction = Ethereum.Transaction.newBuilder().apply {
                transfer = Ethereum.Transaction.Transfer.newBuilder().apply {
                    val sixteen = java.lang.Long.toHexString(
                        Uint256(
                            Convert.toWei(amountString, Convert.Unit.ETHER).toBigInteger()
                        ).value.toLong()
                    )
                    amount = ByteString.copyFrom((BigInteger(sixteen.toString(), 16)).toByteArray())
                }.build()
            }.build()
            privateKey = getPrivateKey(myCoinType)
        }.build()
        val signerOutput =
            AnySigner.sign(signerInput, CoinType.ETHEREUM, Ethereum.SigningOutput.parser())
        val signHexString = Numeric.toHexString(signerOutput.encoded.toByteArray())
        val ethSendTransaction = web3j?.ethSendRawTransaction(signHexString)?.sendAsync()?.get()
        if (ethSendTransaction!!.hasError()) {
            ToastUtil.showToast(ethSendTransaction.error.message)
        }
        return ethSendTransaction.hasError()
    }

    /**
     * vst vmt划转
     */
    fun transferContract(
        amount: String,
        gasLimit: BigInteger,
        toAddressString: String,
        contractAddress: String
    ): Boolean {
        val ethGetTransactionCount =
            web3j?.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING)?.sendAsync()?.get()
        val nonceCount = ethGetTransactionCount?.transactionCount

        val function = Function(
            ERC20_TOKEN_TRANSFER_FUNCTION_NAME,
            listOf(
                Address(toAddressString),
                Uint256(Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger())
            ),
            listOf()
        )
        val rawTransaction = RawTransaction.createTransaction(
            nonceCount,
            getGasPrice(),
            gasLimit,
            contractAddress,
            FunctionEncoder.encode(function)
        )
        val credentials: Credentials =
            Credentials.create(BigInteger(1, wallet?.getKeyForCoin(myCoinType)?.data()).toString(16))
        val signedMessage: ByteArray

        if (CHAIN_ID == null) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, CHAIN_ID, credentials)
        }
        val hexValue = Numeric.toHexString(signedMessage)

        try {
            val ethsend = web3j?.ethSendRawTransaction(hexValue)!!.send()
            if (ethsend.hasError()) {
                ToastUtil.showToast(ethsend.error.message)
            }
            return ethsend.hasError()
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread {
                ToastUtil.showToast(e.message.toString())
            }
        }
        return false
    }

    fun setChainId(chainId: Long) {
        CHAIN_ID = chainId
        getCoinType()
    }

    fun getCoinType(): CoinType {
        runBlocking {
            when (CHAIN_ID) {
                ETH_CHAIN_ID_DEV, ETH_CHAIN_ID_RELEASE -> {
                    myCoinType = CoinType.SMARTCHAIN
                }

                BSC_CHAIN_ID_DEV, BSC_CHAIN_ID_RELEASE -> {
                    myCoinType = CoinType.ETHEREUM
                }
            }
        }
        return myCoinType
    }

    /**
     * 获取nft列表
     */
    fun getNFTList(): MutableList<NFTUint256Bean>? {
        val function: Function = Function(
            NFT_LIST_FUNCTION_NAME,
            listOf(
                Address(160, Constant.getContractAddress()),
                Address(160, address)
            ),
            listOf(object : TypeReference<DynamicArray<Uint256?>?>() {})
        )
        try {
            val ethCall = web3j!!.ethCall(
                Transaction.createEthCallTransaction(
                    address,
                    Constant.getContractAddress(),
                    FunctionEncoder.encode(function)
                ), DefaultBlockParameterName.LATEST
            ).send()
            val result = FunctionReturnDecoder.decode(ethCall.value, function.outputParameters)
            return JSONArray.parseArray(JSONObject.toJSONString(result[0].value), NFTUint256Bean::class.java)
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread {
                ToastUtil.showToast(e.message.toString())
            }
        }
        return null
    }

    fun tokenURI(value: Long): String {
        val function: Function = Function(
            TOKEN_URI_FUNCTION_NAME,
            listOf(Uint256(value)),
            listOf(object : TypeReference<Utf8String?>() {})
        )
        try {
            val ethCall = web3j!!.ethCall(
                Transaction.createEthCallTransaction(
                    address,
                    Constant.getContractAddress(),
                    FunctionEncoder.encode(function)
                ), DefaultBlockParameterName.LATEST
            ).send()
            val result = FunctionReturnDecoder.decode(ethCall.value, function.outputParameters)
            if (result != null && result.size > 0) {
                return result[0].toString()
            } else {
                return ""
            }
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread {
                ToastUtil.showToast(e.message.toString())
            }
        }
        return ""
    }

    /**
     * 鞋盒是否打开
     */
    fun isOpen(tokenId: Long): Boolean {
        val function: Function = Function(
            IS_OPEN_FUNCTION_NAME,
            listOf(Uint256(tokenId)),
            listOf(object : TypeReference<Bool?>() {})
        )
        try {
            val ethCall = web3j!!.ethCall(
                Transaction.createEthCallTransaction(
                    Constant.PROJECT_ADDRESS,
                    Constant.getContractAddress(),
                    FunctionEncoder.encode(function)
                ), DefaultBlockParameterName.LATEST
            ).send()
            val result = FunctionReturnDecoder.decode(ethCall.value, function.outputParameters)
            return (JSONArray.parseArray(JSONArray.toJSONString(result))[0] as JSONObject).getBoolean("value")
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread {
                ToastUtil.showToast(e.message.toString())
            }
        }
        return false
    }

    /**
     * nft划转
     */
    fun transferNFT(gasLimit: BigInteger, contractAddress: String, toAddress: String, tokenId: String): Boolean {
        val ethGetTransactionCount =
            web3j?.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING)?.sendAsync()?.get()
        val nonceCount = ethGetTransactionCount?.transactionCount

        val function = Function(
            TRANSFER_FROM_FUNCTION_NAME,
            listOf(
                Address(160, address),
                Address(160, toAddress),
                Uint256(BigInteger(tokenId))
            ),
            listOf()
        )
        val rawTransaction = RawTransaction.createTransaction(
            nonceCount,
            getGasPrice(),
            gasLimit,
            contractAddress,
            FunctionEncoder.encode(function)
        )
        val credentials: Credentials =
            Credentials.create(BigInteger(1, wallet?.getKeyForCoin(myCoinType)?.data()).toString(16))
        val signedMessage: ByteArray

        if (CHAIN_ID == null) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, CHAIN_ID, credentials)
        }
        val hexValue = Numeric.toHexString(signedMessage)
        try {
            val ethsend = web3j?.ethSendRawTransaction(hexValue)!!.send()
            if (ethsend.hasError()) {
                ToastUtil.showToast(ethsend.error.message)
            }
            return ethsend.hasError()
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread {
                ToastUtil.showToast(e.message.toString())
            }
        }
        return false
    }

    fun clearWallet() {
        wallet = null
        address = ""
    }
}