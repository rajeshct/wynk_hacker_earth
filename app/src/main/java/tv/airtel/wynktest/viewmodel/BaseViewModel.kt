package tv.airtel.wynktest.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * @author Vipul Kumar; dated 22/10/18.
 *
 * Base implementation of android's [ViewModel].
 *
 * ViewModel should be the "logic house" of your application.
 * It should expose some result to the fragment;
 * ideally using [androidx.lifecycle.LiveData].
 *
 */
open class BaseViewModel : ViewModel() {
    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    /**
     * Adds the disposable to [disposables] which is cleared when viewModel clears.
     */
    protected fun Disposable.disposeOnClear(): Disposable {
        disposables += this
        return this
    }
}
