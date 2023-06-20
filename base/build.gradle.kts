plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    namespace = "com.wx.base"
    compileSdk = BuildConfig.compileSdk
    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

        testInstrumentationRunner = BuildConfig.testInstrumentationRunner

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            res.srcDirs("src/main/res")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "is_debug", BuildConfig.debug_is_debug)
            buildConfigField("boolean", "is_dev", BuildConfig.debug_is_dev)
            buildConfigField("boolean", "is_proxy", BuildConfig.debug_is_proxy)
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "is_debug", BuildConfig.release_is_debug)
            buildConfigField("boolean", "is_dev", BuildConfig.release_is_dev)
            buildConfigField("boolean", "is_proxy", BuildConfig.release_is_proxy)
        }
    }
    compileOptions {
        sourceCompatibility = Jdk.sourceCompatibility
        targetCompatibility = Jdk.sourceCompatibility
    }
    kotlinOptions {
        jvmTarget = Jdk.jvmTarget
    }
}

dependencies {
    implementation(AndroidX.androidXCoreKtx)
    implementation(AndroidX.androidXAppcompat)
    implementation(Google.material)
    testImplementation(Testing.junit)
    androidTestImplementation(Testing.androidXJunit)
    androidTestImplementation(Testing.espresso)
    api(Kotlin.stdlib)

    //ARouter
    api(ThirdPart.arouter)
    kapt(ThirdPart.arouter_compiler)

    //radius
    api(ThirdPart.UIWidget_core)
    api(ThirdPart.UIWidget_alert)
    api(ThirdPart.UIWidget_collapsing)
    api(ThirdPart.UIWidget_tab_layout)

    //basequickadapter
    api(ThirdPart.BaseRecyclerViewAdapterHelper)
    api(AndroidX.androidXRecyclerview)

    //原角图
    api(ThirdPart.roundedimageview)

    //下拉刷新
    api(ThirdPart.refresh_layout_kernel)      //core
    api(ThirdPart.refresh_header_classics)    //ClassicsHeader
//    api("com.scwang.smart:refresh-header-radar:2.0.5")       //BezierRadarHeader
//    api("com.scwang.smart:refresh-header-falsify:2.0.5")     //FalsifyHeader
//    api("com.scwang.smart:refresh-header-material:2.0.5")    //MaterialHeader
//    api("com.scwang.smart:refresh-header-two-level:2.0.5")   //TwoLevelHeader
//    api("com.scwang.smart:refresh-footer-ball:2.0.5")        //BallPulseFooter
    api(ThirdPart.refresh_footer_classics)    //ClassicsFooter

    //工具库
    api(ThirdPart.utilcodex)

    //eventbus
    api(ThirdPart.eventbus)

    //协程
    api(Kotlin.reflect)
    api(Kotlin.Coroutines.android)

    //glide
    api(ThirdPart.glide)
    kapt(ThirdPart.glide_compiler)

    //fragment_swipe
    api(ThirdPart.fragmentationx)
    api(ThirdPart.fragmentationx_swipeback)

    //gson
    api(ThirdPart.fastjson)
    api(ThirdPart.gson)

    //rxpermission
    api(ThirdPart.rxpermissions)

    //rx
    api(ThirdPart.rxjava2_rxkotlin)
    api(ThirdPart.rxjava2_rxandroid)
    api(ThirdPart.rxjava3_rxjava)
    api(ThirdPart.rxjava)
    api(ThirdPart.rxandroid)

    //retrofit
    api(ThirdPart.retrofit)
    api(ThirdPart.retrofit_converter_scalars)
    api(ThirdPart.retrofit_converter_gson)
    api(ThirdPart.okhttp3_logging_interceptor)
    api(ThirdPart.retrofit_kotlin_coroutines_adapter)
    api(ThirdPart.retrofit_rxjava2_adapter)

    //okhttp3
    api(ThirdPart.okhttp3)

    //zxing
    api(ThirdPart.zxing_android_core)
    api(ThirdPart.zxing_core)

    //trustWallet
    api(ThirdPart.trustwallet)

    // Web3
    api(ThirdPart.web3j)

    //realm
    api(ThirdPart.realm)

    //pictureSeletor
    api(ThirdPart.PICTURE_SELECTOR)
    api(ThirdPart.PICTURE_UCROP)
    api(ThirdPart.PICTURE_COMPRESS)

    //datastore
    api(ThirdPart.datastore)

    //anchortask
    api(ThirdPart.anchortask)

    //lottie
    api(ThirdPart.lottie)

    //banner
    api(ThirdPart.banner)

}