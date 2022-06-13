package com.example.football.view


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
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.football.R
import com.example.football.adapters.RecyclerNewsAdapter
import com.example.football.model.HomeBaoMoiData
import com.example.football.model.detail.Content
import com.example.football.model.detail.Data
import com.example.football.model.detail.DetailBaoMoiData
import com.example.football.model.detail.Related
import com.example.football.utils.Helpers
import com.example.football.utils.Helpers.Companion.margin
import com.example.football.viewmodel.DetailsViewModel
import com.example.football.viewmodel.NewsViewModel


class DetailNewFragment : Fragment(), HomeNewsFragment.GetIDContent{

    private lateinit var viewFragment :View
    private lateinit var detailViewModel : DetailsViewModel
    private lateinit var data : Data
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewFragment = inflater.inflate(R.layout.fragment_detail,container,false)

        val bundle = arguments
        val message = bundle!!.getInt("idContent")

        detailViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        detailViewModel.getDetailNewObservable().observe(viewLifecycleOwner, Observer {
            if (it == null){
                Toast.makeText(this.context,"No result found", Toast.LENGTH_SHORT).show()
            }else{
                //get content
                data = it.data

                //set content
                setContent(data.content,data.related)
            }
        })

        detailViewModel.getDetailNew(message)

        return viewFragment
    }

    fun setContent(data: Content,related: Related){
        val title : TextView = viewFragment.findViewById(R.id.tv_detailTitle)
        val layout : LinearLayout = viewFragment.findViewById(R.id.lo_detail)

        //title
        title.setText(data.title)

        //time
        var time : TextView = TextView(context)
        time.textSize=18f
        time.text=Helpers.CalculateDistanceTime(data.date)
        layout.addView(time)
        time.margin(top = 8f)

        //description
        var description : TextView = TextView(context)
        description.textSize= 23f
        description.setTypeface(null, Typeface.BOLD)
        description.text= data.description
        layout.addView(description)
        description.margin(top = 8f, bottom = 8f)

        //body
        for(body in data.body){
            when{
                body.type.equals("text") -> {
                    var text : TextView = TextView(context)

                    //set style base on subtype
                    if(!body.subtype.isNullOrBlank()){
                        if(body.subtype.equals("media-caption")){
                            text.textSize=16f
                            text.textAlignment=View.TEXT_ALIGNMENT_CENTER
                        }
                        else
                            text.textSize=22f
                    }
                    else
                        text.textSize=23f

                    //convert html tag
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        text.setText(Html.fromHtml(
                            body.content, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        text.setText(Html.fromHtml(body.content));
                    }

                    layout.addView(text)
                    text.margin(top = 8f, bottom = 8f)

                }

                body.type.equals("image") ->{
                    var img : ImageView = ImageView(context)
                    Glide.with(this).load(body.originUrl).into(img)
                    layout.addView(img)
                }

                body.type.equals("video") ->{
                    val videoView: VideoView = VideoView(context)

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


        //relate news
        var relateNews : TextView = TextView(context)
        relateNews.textSize= 22f
        relateNews.setTypeface(null, Typeface.ITALIC)
        relateNews.text= related.title
        layout.addView(relateNews)
        relateNews.margin(top = 20f, bottom = 8f)


        var layoutManager : RecyclerView.LayoutManager? = null
        var adapterNewlist : RecyclerNewsAdapter
        var newsList : RecyclerView? = context?.let { RecyclerView(it) }
        var newsViewModel : NewsViewModel? = null
        layoutManager = LinearLayoutManager(this.context)

        newsList?.layoutManager = layoutManager

        adapterNewlist = RecyclerNewsAdapter()
        adapterNewlist.setOnItemClickListener(object : RecyclerNewsAdapter.onItemClickListener{
            override fun onItemClick(idContent: Int) {
                showDetail(idContent)
            }
        })
        newsList?.adapter = adapterNewlist

        newsViewModel?.getListNewsObservable()?.observe(viewLifecycleOwner, Observer<HomeBaoMoiData>{
            if (it == null){
                Toast.makeText(this.context,"No result found", Toast.LENGTH_SHORT).show()
            }else{
                adapterNewlist.ListNews = related.contents.toMutableList()
                adapterNewlist.notifyDataSetChanged()
            }
        })

        adapterNewlist.ListNews=related.contents.toMutableList()
        layout.addView(newsList)

    }

    //
    override fun showDetail(idContent: Int) {
        val fragment = DetailNewFragment()

        val bundle = Bundle()
        bundle.putInt("idContent", idContent)
        fragment.arguments = bundle

        val fram = parentFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)

        fram.addToBackStack("${fragment.toString()}").commit()
    }




}