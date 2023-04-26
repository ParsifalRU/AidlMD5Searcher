package com.example.aidlmd5searcher

import android.content.Intent
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
                status.text = "ОК"
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
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
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
                    val extras = intent.extras
                    if (extras != null) {
                        val value1 = extras.getString ("key1");
                        Log.d("LOGTAG", "value1: " + value1);
                    }

                    if (checkFormat(getText()) == true){
                        val intent2 = getIntent()
                        if(intent2.getStringExtra("hash")!= null){
                            status.text = "Ищем совпадения"
                            //Запуск службы по поиску

                        }
                        status.text = "База сравнений пуста"
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
}