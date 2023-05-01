package com.example.aidlmd5searcher

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.appcompat.app.AppCompatActivity


class ImpExpActivity : AppCompatActivity(), OnClickListener{
    
    private lateinit var importRadioButton:RadioButton
    private lateinit var exportRadioButton:RadioButton
    private lateinit var dirBtn: Button
    private lateinit var backButton: Button
    private lateinit var openLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var saveLauncher:ActivityResultLauncher<String>
    private var openData:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imp_exp_acitivity)

        takeData(savedInstanceState)

        initElements()

        openLauncher()

        saveLauncher()

        Toast.makeText(this, "$openData", Toast.LENGTH_SHORT).show()
    }
    //Сохранение загруженного списка хешей при переходе на другие активити
    private fun takeData(savedInstanceState: Bundle?){
        openData = if (savedInstanceState != null) {
            savedInstanceState.getString("file")!!
        } else {
            ""
        }
    }
    //Добавление логики в onSaveInstanceSave
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("file", openData)
        Toast.makeText(this, "saveData $openData", Toast.LENGTH_SHORT).show()
    }
    //Инициализация элементов экрана
    private fun initElements(){
        importRadioButton = findViewById(R.id.radioButton)
        importRadioButton.setOnClickListener(this)
        exportRadioButton = findViewById(R.id.radioButton2)
        exportRadioButton.setOnClickListener(this)
        dirBtn = findViewById(R.id.dirButton)
        dirBtn.setOnClickListener(this)
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener(this)
    }
    //Обработка кнопок
    override fun onClick(v: View) {
        when(v){
            importRadioButton -> {
                importRadioButton.isChecked = true
                exportRadioButton.isChecked = false
                Toast.makeText(this, "Import", Toast.LENGTH_SHORT).show()
            }
            exportRadioButton -> {
                importRadioButton.isChecked = false
                exportRadioButton.isChecked = true
                Toast.makeText(this, "Export", Toast.LENGTH_SHORT).show()
            }
            dirBtn -> {
                if (importRadioButton.isChecked){
                    openLauncher.launch(arrayOf("text/plain"))
                }
                else{
                    saveLauncher.launch("my-file.txt")
                }
            }
            backButton -> {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("key1", "value1")
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
        }
    }
    //Создание контракта на открытие и чтение документа
    private fun openLauncher(){
        openLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){
            try {
                if (it != null) {
                    openFile(it)
                }else{
                    Toast.makeText(this, "Пустой файл", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                Toast.makeText(this, "Can't open it $e", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Создание контракта на сохранение документа
    private fun saveLauncher(){
        saveLauncher = registerForActivityResult(CreateDocument("text/plain")){
            try {
                it.let {
                    if (it != null) {
                        saveFile(it)
                    }else{
                        Toast.makeText(this, "Пустой файл", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e:Exception){
                Toast.makeText(
                    this, "Не получилось сохранить файл $e", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    //Логика при открытии документа на чтение
    private fun openFile(uri: Uri){
        openData = contentResolver.openInputStream(uri)?.use {
            String(it.readBytes())
        }
        val sharedPref = getSharedPreferences(
            "sharedFile", Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putString("file", openData.toString()).apply()
    }
    //Логика при открытии документа на сохранение
    private fun saveFile(uri: Uri){
        contentResolver.openOutputStream(uri)?.use {
            it.write(openData?.toByteArray())
        }
    }
    //Заблокировать конпку назад
    override fun onBackPressed() {
    }
}