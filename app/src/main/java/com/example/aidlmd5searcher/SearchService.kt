package com.example.aidlmd5searcher

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class SearchService : Service() {

    val searchList = arrayListOf<String>()
    val readyList = arrayListOf<String>()

    override fun onBind(intent: Intent): IBinder? {
        Log.d("LOGTAG", "Service - Start")
        val data = intent.getStringExtra("data")
        val searchHash = intent.getStringExtra("searchHash")
        val thread = Thread {
            //End or have hash
            comparator(data!!, searchHash!!)
        }
        if (!thread.isAlive){
                thread.start()
        }
        return null
    }

    private fun comparator(data:String, hash: String){
        val listAll = data.split("\n")
        Log.d("LOGTAG", "список - \n$listAll")
        Log.d("LOGTAG", "слово - \n$hash")
        val numb = listAll.size
        for (i in 1 .. numb){
            if(listAll[i].contains(hash)){
                Log.d("LOGTAG", "найдено TRUE")
                sendBroadcast(Intent("TRUE"))
                readyList.add(hash)
            }else{
                Log.d("LOGTAG", "не найдено False")
                sendBroadcast(Intent("FALSE"))
                searchList.add(hash)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LOGTAG", "onDestroy")
    }
}
