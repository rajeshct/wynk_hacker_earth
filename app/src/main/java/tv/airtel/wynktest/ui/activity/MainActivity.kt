package tv.airtel.wynktest.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import tv.airtel.wynktest.R
import tv.airtel.wynktest.ui.fragment.ContentListFragment

/**
 * @author Vipul Kumar; dated 24/01/19.
 *
 * Activity to host fragments.
 */
 class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchContentListFragment()
    }

    private fun launchContentListFragment() {
        supportFragmentManager
            .transaction {
                replace(R.id.fragmentContainer, ContentListFragment())
            }
    }
}
