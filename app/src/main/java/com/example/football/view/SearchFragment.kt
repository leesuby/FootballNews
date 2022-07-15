package com.example.football.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.football.R
import com.example.football.view.adapters.RecyclerRelatedNewsAdapter
import com.example.football.viewmodel.NewsViewModel


class SearchFragment : Fragment(), HomeNewsFragment.GetIDContent{
    private lateinit var views : View
    private lateinit var newsViewModel : NewsViewModel

    private lateinit var searchField : EditText
    private lateinit var recyclerListNews: RecyclerView
    private lateinit var adapterNewlist: RecyclerRelatedNewsAdapter
    private lateinit var textView_no_result : TextView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initial ViewModel and data
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_search,container,false)
        return views
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        textView_no_result = views.findViewById(R.id.tv_no_result)
        recyclerListNews = views.findViewById(R.id.RV_search)

        layoutManager = LinearLayoutManager(this.context)
        recyclerListNews.layoutManager = layoutManager

        adapterNewlist = RecyclerRelatedNewsAdapter()
        adapterNewlist.setOnItemClickListener(object : RecyclerRelatedNewsAdapter.onNewsClickListener{
            override fun onItemClick(idContent: Int) {
                showDetail(idContent)
            }
        })

        recyclerListNews.adapter = adapterNewlist

        newsViewModel.getListNewsObservable().observe(viewLifecycleOwner) {
            if (it == null) {
                textView_no_result.visibility= View.VISIBLE
            } else {
                if(it.data.contents.isEmpty()) {
                    textView_no_result.visibility = View.VISIBLE
                    adapterNewlist.setNewsList(mutableListOf())
                }
                else {
                    Log.e("data",it.data.contents.toString())
                    textView_no_result.visibility = View.GONE
                    adapterNewlist.setNewsList(it.data.contents.toMutableList())
                }
            }
        }

        searchField=views.findViewById(R.id.edtxt_searchfield)
        searchField.addTextChangedListener(object :TextWatcher{

            var timer: CountDownTimer? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newsViewModel.getListSearch(s.toString())
                Log.e("keyword",s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

                timer?.cancel()
                timer = object : CountDownTimer(2500, 1500) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        closeKeyBoard()
                    }
                }.start()


            }

        })

    }

    override fun showDetail(idContent: Int) {
        val fragment = DetailNewFragment()

        val bundle = Bundle()
        bundle.putInt("idContent", idContent)
        fragment.arguments = bundle

        val fram = parentFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)

        fram.addToBackStack(fragment.toString()).commit()
    }

    private fun closeKeyBoard() {
        val view = this.activity?.currentFocus
        if (view != null) {
            val imm = this.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}