package com.example.football.view


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.football.R
import com.example.football.data.model.detail.Content
import com.example.football.data.model.detail.Data
import com.example.football.data.model.detail.Related
import com.example.football.utils.Helpers
import com.example.football.utils.Helpers.Companion.margin
import com.example.football.view.adapters.RecyclerNewsAdapter
import com.example.football.viewmodel.DetailsViewModel
import com.example.football.viewmodel.NewsViewModel
import java.io.File


class DetailNewFragment : Fragment(), HomeNewsFragment.GetIDContent{

    private lateinit var viewFragment :View
    private lateinit var detailViewModel : DetailsViewModel
    private lateinit var data : Data

    private lateinit var title : TextView
    private lateinit var layout : LinearLayout

    private var message : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        message = bundle!!.getInt("idContent")

        //initial ViewModel and data
        detailViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        detailViewModel.getDetailNew(message,context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewFragment = inflater.inflate(R.layout.fragment_detail,container,false)

        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = viewFragment.findViewById(R.id.tv_detailTitle)
        layout = viewFragment.findViewById(R.id.lo_detail)
        bindViewModel()

    }

    private fun bindViewModel(){
        detailViewModel.getDetailNewObservable().observe(viewLifecycleOwner) {
            if (it == null) {
                title.text="Không có dữ liệu Offline về bài báo này.Xin hãy xem ở chế độ Online"
            } else {
                //get content
                data = it.data
                //set content
                setContent(data.content, data.related)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setContent(data: Content, related: Related){


        //title
        title.text = data.title

        //time
        val time = TextView(context)
        time.textSize=18f
        time.text=Helpers.CalculateDistanceTime(data.date)
        layout.addView(time)
        time.margin(top = 8f)

        //description
        val description = TextView(context)
        description.textSize= 20f
        description.setTypeface(null, Typeface.BOLD)
        description.text= data.description
        layout.addView(description)
        description.margin(top = 8f, bottom = 8f)

        //body
        for(body in data.body){
            when (body.type) {
                "text" -> {
                    val text = TextView(context)

                    //set style base on subtype
                    if(!body.subtype.isNullOrBlank()){
                        if(body.subtype == "media-caption"){
                            text.textSize=16f
                            text.textAlignment=View.TEXT_ALIGNMENT_CENTER
                        } else
                            text.textSize=20f
                    } else
                        text.textSize=20f

                    //convert html tag
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        text.text = Html.fromHtml(
                            body.content, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        text.text = Html.fromHtml(body.content)
                    }

                    layout.addView(text)
                    text.margin(top = 8f, bottom = 8f)

                }
                "image" -> {
                    val img = ImageView(context)
                    if(Helpers.internet)
                        Glide.with(this).load(body.originUrl).into(img)
                    else
                        Glide.with(this).load(File(body.originUrl)).into(img)
                    layout.addView(img)
                }
                "video" -> {
                    val videoView = VideoView(context)

                    // Uri object to refer the
                    // resource from the videoUrl
                    val uri: Uri = Uri.parse(body.content)

                    // sets the resource from the
                    // videoUrl to the videoView
                    videoView.setVideoURI(uri)

                    // creating object of
                    // media controller class
                    val mediaController = MediaController(context)

                    // sets the anchor view
                    // anchor view for the videoView
                    mediaController.setAnchorView(videoView)

                    // sets the media player to the videoView
                    mediaController.setMediaPlayer(videoView)

                    // sets the media controller to the videoView
                    videoView.setMediaController(mediaController)

                    layout.addView(videoView)
                    Log.e("body",body.content)
                }
                else -> {}
            }
        }

        //spacing
        val space = View(context)
        space.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10)
        space.setBackgroundColor(Color.LTGRAY)
        layout.addView(space)

        //relate news
        val relateNews = TextView(context)
        relateNews.textSize= 22f
        relateNews.setTypeface(null, Typeface.ITALIC)
        relateNews.text= related.title
        layout.addView(relateNews)
        relateNews.margin(top = 20f, bottom = 8f)


        val layoutManager: RecyclerView.LayoutManager?
        val newsList : RecyclerView? = context?.let { RecyclerView(it) }
        val newsViewModel : NewsViewModel? = null
        layoutManager = LinearLayoutManager(this.context)

        newsList?.layoutManager = layoutManager

        val adapterNewlist = RecyclerNewsAdapter()
        adapterNewlist.setOnItemClickListener(object : RecyclerNewsAdapter.onItemClickListener{
            override fun onItemClick(idContent: Int) {
                showDetail(idContent)
            }
        })
        newsList?.adapter = adapterNewlist

        newsViewModel?.getListNewsObservable()?.observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(this.context, "No result found", Toast.LENGTH_SHORT).show()
            } else {
                adapterNewlist.ListNews = related.contents.toMutableList()
                adapterNewlist.notifyDataSetChanged()
            }
        }

        adapterNewlist.ListNews=related.contents.toMutableList()
        layout.addView(newsList)

    }

    //Replace Detail news Fragment with news Details new when user click on Related News
    override fun showDetail(idContent: Int) {
        val fragment = DetailNewFragment()

        val bundle = Bundle()
        bundle.putInt("idContent", idContent)
        fragment.arguments = bundle

        val fram = parentFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)

        fram.addToBackStack(fragment.toString()).commit()
    }




}