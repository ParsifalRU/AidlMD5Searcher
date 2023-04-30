package com.example.aidlmd5searcher

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class SearchService : Service() {

    val searchList = arrayListOf<String>()
    val readyList = arrayListOf<String>()

    override fun onCreate() {
        super.onCreate()
        Log.d("LOGTAG", "Service Created")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("LOGTAG", "onStartCommand")
        onBind(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d("LOGTAG", "Service - Start")
        val data = intent.getStringExtra("data")
        val searchHash = intent.getStringExtra("searchHash")
        val threadPoolExecutor = Executors.newFixedThreadPool(2) as ThreadPoolExecutor
        threadPoolExecutor.execute {
            comparator(data!!, searchHash!!)
        }
        return null
    }

    private fun comparator(data:String, hash: String){
        val listAll = data.split("\n")
        Log.d("LOGTAG", "список - \n$listAll")
        Log.d("LOGTAG", "слово - \n$hash")
        val numb = listAll.size
        for (i in 1 .. numb){
            if(listAll[i-1].contains(hash)){
                Log.d("LOGTAG", "найдено TRUE")
                sendBroadcast(Intent("TRUE"))
                readyList.add(hash)
                stopSelf()
                break
            }else{
                Log.d("LOGTAG", "не найдено False")
                sendBroadcast(Intent("FALSE"))
                searchList.add(hash)
            }
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }
}
