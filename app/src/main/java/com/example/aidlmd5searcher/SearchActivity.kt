package com.example.aidlmd5searcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity(), OnClickListener {

    private lateinit var editText: EditText
    lateinit var btnSearch: Button
    lateinit var btnImpExpActivity: Button
    lateinit var waitingList: ArrayList<String>
    lateinit var resultList: ArrayList<String>
    lateinit var status: TextView
    var data = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        
        initViewElements()

        val intentFilter = IntentFilter()
        intentFilter.addAction("TRUE")
        intentFilter.addAction("FALSE")

        registerReceiver(broadcastReceiver(), intentFilter)

        if (savedInstanceState != null) {
            data = savedInstanceState.getString("EditText").toString()
            editText.setText(data)
        }
    }

    private fun getText():String = editText.text.toString()

    private fun checkFormat(hash: String):Boolean{
        if (hash.length != 32){
            return false
        }
        else{
            if (hash.indexOf("A")>0){
                return false
            }
            else{
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val text = editText.text.toString()
        outState.putString("EditText", text)
        Toast.makeText(this, "saveData ${editText.text}", Toast.LENGTH_SHORT).show()
    }

    private fun impExpIntent(){
        val intent = Intent(this, ImpExpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent)
    }

    private fun initViewElements(){
        editText = findViewById(R.id.HashMd5EditText)
        btnSearch = findViewById(R.id.btnSearch)
        btnSearch.setOnClickListener(this)
        btnImpExpActivity = findViewById(R.id.btnImpExpActivity)
        btnImpExpActivity.setOnClickListener(this)
        waitingList = ArrayList()
        resultList = ArrayList()
        status = findViewById(R.id.status)
    }

    override fun onClick(v: View) {
            when(v){
                btnSearch -> {

                    val getSharPref = getSharedPreferences("sharedFile", MODE_PRIVATE)
                    val data:String = getSharPref.getString("file", "null").toString()
                    Log.d("LOGTAG", "value1: " + data);

                    /*val service = SearchService(data, editText.text.toString())*/

                    if (checkFormat(getText())){
                        if(data!="null"){
                            status.text = "Ищем совпадения"
                            val intent = Intent(this@SearchActivity, SearchService::class.java)
                            intent.putExtra("data", data)
                            intent.putExtra("searchHash", editText.text.toString())
                            applicationContext.startService(intent)

                        }else {
                            status.text = "База сравнений пуста"
                        }

                    }
                    else{
                        status.text = "Неверный формат"
                    }
                }
                btnImpExpActivity -> {
                    impExpIntent()
                }
            }
    }

    private fun broadcastReceiver(): BroadcastReceiver{
        val myBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Toast.makeText(context, "Зареган Броэдкаст", Toast.LENGTH_SHORT).show()
                when(intent.action){
                    "TRUE" -> {
                        Toast.makeText(context, "Найдено", Toast.LENGTH_SHORT).show()
                        status.text = "ОК"
                    }
                    "FALSE" -> {
                        Toast.makeText(context, "Нихрена не найдено", Toast.LENGTH_SHORT).show()
                        status.text = "Хеш не найден"
                    }
                }
            }
        }
        return myBroadcastReceiver
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver())
    }
}
