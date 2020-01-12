package drewhamilton.androiddatetimeformatters.javatime

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import androidx.test.runner.AndroidJUnitRunner

@Suppress("unused")
class MultiDexAndroidJUnitRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application =
        super.newApplication(cl, MultiDexApplication::class.java.name, context)
}
