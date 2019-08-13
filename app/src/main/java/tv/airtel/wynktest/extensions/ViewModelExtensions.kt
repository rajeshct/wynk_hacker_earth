package tv.airtel.wynktest.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : Fragment {
    return lazy { ViewModelProviders.of(this).get(VM::class.java) }
}

inline fun <T> LiveData<T>.observeK(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner, Observer { it?.let { observer(it) } })
}
