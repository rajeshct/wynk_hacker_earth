package tv.airtel.wynktest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.model.ArticleDto
import kotlinx.android.synthetic.main.item_content.view.*
import kotlinx.android.synthetic.main.item_content_with_title.view.*
import tv.airtel.wynktest.R
import tv.airtel.wynktest.Utils
import tv.airtel.wynktest.data.FavouriteClick
import tv.airtel.wynktest.data.persistance.FavouriteModel
import tv.airtel.wynktest.extensions.loadImage

/**
 * @author Vipul Kumar; dated 24/01/19.
 *
 * Adapter to host content on home screen.
 * [setData] sets the data in an adapter.
 * */
class ContentListAdapter : RecyclerView.Adapter<ContentListAdapter.ContentViewHolder>() {
    private var articles = mutableListOf<ArticleDto>()
    private val listOfFavouriteNews = mutableListOf<FavouriteModel>()
    private var favChange: MutableLiveData<FavouriteClick>? = null

    init {
        setHasStableIds(true)
    }

    fun setData(list: MutableList<ArticleDto>) {
        articles = list
        notifyItemRangeInserted(0, articles.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.apply {
            title?.text = articles[holder.adapterPosition].title
            author?.text = articles[holder.adapterPosition].source.name
            image?.loadImage {
                load(articles[holder.adapterPosition].urlToImage)
            }
            val primaryKey = Utils.getFavouritePrimaryKey(articles[holder.adapterPosition])
            favClick?.isSelected = Utils.containFav(listOfFavouriteNews, primaryKey)
            categoryTitle?.text = articles[holder.adapterPosition].source.name
            favClick?.setOnClickListener {
                favChange?.let {
                    val articleTemp = articles[holder.adapterPosition]
                    val tempPrimaryKey = Utils.getFavouritePrimaryKey(articleTemp)
                    val isRemove = Utils.containFav(listOfFavouriteNews, tempPrimaryKey)
                    val favouriteElement = FavouriteClick(holder.adapterPosition, tempPrimaryKey, isRemove)
                    it.value = favouriteElement
                    if (isRemove) {
                        listOfFavouriteNews.remove(FavouriteModel(tempPrimaryKey))
                    } else {
                        listOfFavouriteNews.add(FavouriteModel(tempPrimaryKey))
                    }
                    this@ContentListAdapter.notifyItemChanged(holder.adapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            R.layout.item_content_with_title
        } else {
            val oldValue = articles[position - 1]
            val current = articles[position]
            if (oldValue.source.name.equals(current.source.name, true)) {
                R.layout.item_content
            } else {
                R.layout.item_content_with_title
            }
        }
    }

    fun setFavValues(favList: List<FavouriteModel>) {
        this.listOfFavouriteNews.addAll(favList)
    }

    fun setFavChange(favChange: MutableLiveData<FavouriteClick>) {
        this.favChange = favChange
    }


    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var image: ImageView? = null
        internal var title: TextView? = null
        internal var author: TextView? = null
        internal var favClick: ImageView? = null

        internal var categoryTitle: TextView? = null

        init {
            title = itemView.tvTitle
            image = itemView.ivImage
            author = itemView.tvAuthor
            favClick = itemView.ivFavourite
            categoryTitle = itemView.tvCategory
        }

        companion object {
            fun create(parent: ViewGroup, viewType: Int): ContentViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(viewType, parent, false)
                return ContentViewHolder(view)
            }
        }
    }
}
