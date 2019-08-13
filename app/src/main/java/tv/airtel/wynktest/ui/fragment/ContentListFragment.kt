package tv.airtel.wynktest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.model.ArticlesDto
import kotlinx.android.synthetic.main.fragment_content_list.*
import tv.airtel.wynktest.R
import tv.airtel.wynktest.data.FavouriteClick
import tv.airtel.wynktest.data.persistance.FavouriteModel
import tv.airtel.wynktest.extensions.observeK
import tv.airtel.wynktest.extensions.viewModel
import tv.airtel.wynktest.ui.adapter.ContentListAdapter
import tv.airtel.wynktest.viewmodel.ContentListViewModel

/**
 * @author Vipul Kumar; dated 27/12/18.
 *
 * Fragment to host content list on homepage.
 *
 * It fetches data from [ContentListViewModel]
 * and shows it in a recyclerView using [ContentListAdapter].
 */
class ContentListFragment : Fragment() {
    private val viewModel: ContentListViewModel by viewModel() // lazy initialization
    private val adapter = ContentListAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvContent.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@ContentListFragment.adapter
            this@ContentListFragment.adapter.setFavChange(viewModel.favChange)
        }
        viewModel.favouriteElement.observeK(this, ::onFavouriteLoaded)
        viewModel.result.observeK(this, ::onNewsLoaded)
        viewModel.favChange.observe(this
                , Observer { onFavouriteClicked(favouriteClick = it) })
        progressBar.visibility = View.VISIBLE
        viewModel.fetchFavourite()
    }

    private fun onFavouriteClicked(favouriteClick: FavouriteClick) {
        viewModel.saveToDb(favouriteClick)
    }

    private fun onFavouriteLoaded(result: Result<List<FavouriteModel>>) {
        if (result.isSuccess) {
            adapter.setFavValues(result.getOrThrow().toList())
            viewModel.fetchNews()
        }

        if (result.isFailure) {
            viewModel.fetchNews()
        }
    }

    private fun onNewsLoaded(result: Result<ArticlesDto>) {
        progressBar.visibility = View.GONE
        if (result.isSuccess) {
            val articleElement=result.getOrThrow().articles
            adapter.setData(viewModel.sortBySource(articleElement)) // sets the data into adapter
        }

        if (result.isFailure) {
            throw result.exceptionOrNull() ?: RuntimeException("unknown error from api")
        }
    }
}
