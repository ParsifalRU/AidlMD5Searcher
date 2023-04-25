package com.example.aidlmd5searcher

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SearchService : Service() {

    override fun onBind(intent: Intent): IBinder? {


        return null
    }

    fun comparator(hash: String, bigList: ArrayList<String>, readyList: ArrayList<String>){
        if (bigList.contains(hash)){
            readyList.add(hash)
            readyList.add("OK")
        }else{
            readyList.add(hash)
            readyList.add("No")
        }

    }


}