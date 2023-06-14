pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        maven {
            url = uri("https://dl.google.com/dl/android/maven2/")
        }
        maven {
            url = uri("https://repo1.maven.org/maven2/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/jcenter")
        }
        maven {
            url = uri("https://s01.oss.sonatype.org/content/groups/public")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://repo1.maven.org/maven2/") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://s01.oss.sonatype.org/content/groups/public") }
        maven {
            url = uri("https://maven.pkg.github.com/trustwallet/wallet-core")
            credentials {
                username = "afterlifehy"
                password = "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"
            }
        }
    }
}
rootProject.name = "Detalk"
include("app")
include("base")
include("common")
