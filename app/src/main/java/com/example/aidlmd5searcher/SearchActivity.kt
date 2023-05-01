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
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class SearchActivity : AppCompatActivity(), OnClickListener {

    private lateinit var editText: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnImpExpActivity: Button
    private lateinit var waitingList: ArrayList<String>
    private lateinit var resultList: ArrayList<String>
    lateinit var status: TextView
    private var data = ""
    private val searchList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        
        initViewElements()

        registerBroadcastReceiver()

        setText(savedInstanceState)
    }
    //Восстанавливаем текст при перевороте
    private fun setText(savedInstanceState: Bundle?){
        if (savedInstanceState != null) {
            data = savedInstanceState.getString("EditText").toString()
            editText.setText(data)
        }
    }
    //Регистрируем BroadcastReceiver
    private fun registerBroadcastReceiver(){
        val intentFilter = IntentFilter()
        intentFilter.addAction("TRUE")
        intentFilter.addAction("FALSE")
        registerReceiver(broadcastReceiver(), intentFilter)
    }
    //Получаем текст из EditText
    private fun getText():String = editText.text.toString()
    //Проверяю на формат MD5 + заполненность списка сравнения хешей
    private fun checkFormat(hash: String):Boolean{
        val breakList =  listOf("a","b","c","d","e","f","0","1","2","3","4","5","6","7","8","9")
        var n = 0
        return if (hash.length != 32){
            false
        }else {
            for (i in 1 .. 16){
                n += hash.count { it.toString() == breakList[i - 1] }
                Log.d("LOGTAG", "$n")
            }
            if (n == 32){
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                true
            }else false
        }
    }
    //Переопределяю onSaveInstanceState
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val text = editText.text.toString()
        outState.putString("EditText", text)
        Toast.makeText(this, "saveData ${editText.text}", Toast.LENGTH_SHORT).show()
    }
    //Явно вывзываю активити с импортом и экспортом списка хешей
    private fun impExpIntent(){
        val intent = Intent(this, ImpExpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }
    //Определяю все View элементы
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
    //Обрабаотка кнопок + SharPref для получения списка хешей
    override fun onClick(v: View) {
            when(v){
                btnSearch -> {
                    val getSharPref = getSharedPreferences("sharedFile", MODE_PRIVATE)
                    val data:String = getSharPref.getString("file", "null").toString()
                    Log.d("LOGTAG", "value1: $data")
                    if (checkFormat(getText())){
                        if(data!="null"){
                            status.text = "Ищем совпадения"
                            val intent = Intent(this@SearchActivity, SearchService::class.java)
                            intent.putExtra("data", data)
                            intent.putExtra("searchHash", editText.text.toString())
                            try {
                                applicationContext.startService(intent)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
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
    //Сам BroadcastReceiver(+ добавляем хеши не найденные в поиске в список хешей на поиск)
    // + получение BR на выходе
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
                        Toast.makeText(context, "Ничего не найдено", Toast.LENGTH_SHORT).show()
                        status.text = "Хеш не найден"
                        val newSearchItem = intent.extras?.getString("itemAddSearchList", "null").toString()
                        searchList.add(newSearchItem)
                    }
                }
            }
        }
        return myBroadcastReceiver
    }
    //Отвязка BroadcastReceiver
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver())
    }
}

