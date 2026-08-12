import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Hilt を使うモジュール向け。KSP と Hilt の Gradle プラグインを適用する。
 * hilt-android / hilt-compiler の依存宣言は各モジュールの build.gradle.kts に残す。
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("com.google.dagger.hilt.android")
        }
    }
}
