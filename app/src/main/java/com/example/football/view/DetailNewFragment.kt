package com.example.football.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.football.R
import com.example.football.viewmodel.DetailsViewModel

class DetailNewFragment : Fragment() {

    private lateinit var viewFragment :View
    private lateinit var detailViewModel : DetailsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewFragment = inflater.inflate(R.layout.fragment_detail,container,false)

        val bundle = arguments
        val message = bundle!!.getInt("idContent")

        val title : TextView = viewFragment.findViewById(R.id.tv_detailTitle)

        detailViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        detailViewModel.getDetailNewObservable().observe(viewLifecycleOwner, Observer {
            if (it == null){
                Toast.makeText(this.context,"No result found", Toast.LENGTH_SHORT).show()
            }else{
                var data = it.data.content

            }
        })

        detailViewModel.getDetailNew(message)

        title.setText(message.toString())

        return viewFragment
    }
}