package com.example.aidlmd5searcher

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.TypedArrayUtils.getText

class SearchActivity : AppCompatActivity(), OnClickListener {

    private lateinit var editText: EditText
    lateinit var btnSearch: Button
    lateinit var btnImpExpActivity: Button
    lateinit var waitingList: ArrayList<String>
    lateinit var resultList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViewElements()

        

    }

    private fun getText():String = editText.text.toString()

    private fun checkFormat(hash: String):Boolean{
        val toast = Toast.makeText(this, "Введенный хеш не соответствует формату MD5", Toast.LENGTH_SHORT).show()
        if (hash.length != 32){
            toast
            return false
        }
        else{
            if (hash.indexOf("ds")>0){
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                return false
            }
            else{
                return true
            }
        }

    }

    fun search(){

    }

    fun impExpIntent(){
        val intent = Intent(this, ImpExpActivity::class.java)

        startActivity(intent)
    }

    fun saveProgress(){

    }


    private fun initViewElements(){
        editText = findViewById<EditText>(R.id.HashMd5EditText)
        btnSearch = findViewById<Button>(R.id.btnSearch)
        btnSearch.setOnClickListener(this)
        btnImpExpActivity = findViewById<Button>(R.id.btnImpExpActivity)
        btnImpExpActivity.setOnClickListener(this)
        waitingList = ArrayList<String>()
        resultList = ArrayList<String>()
    }

    override fun onClick(v: View) {

            when(v){
                btnSearch -> {

                    checkFormat(getText())

                    search()

                }

                btnImpExpActivity -> {
                    impExpIntent()
                }
            }

    }
}