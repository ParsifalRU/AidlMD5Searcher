package com.example.aidlmd5searcher

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ImpExpActivity : AppCompatActivity(), OnClickListener{
    
    private lateinit var importRadioButton:RadioButton
    private lateinit var exportRadioButton:RadioButton
    private lateinit var dirBtn: Button
    private lateinit var backButton: Button
    private lateinit var openLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var saveLauncher:ActivityResultLauncher<String>
    var openData:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imp_exp_acitivity)


        if (savedInstanceState != null) {
            openData = savedInstanceState.getString("file")!!
        } else {
            openData = ""
        }

        initElements()

        openLauncher()

        saveLauncher()

        Toast.makeText(this, "$openData", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("file", openData)
        Toast.makeText(this, "saveData $openData", Toast.LENGTH_SHORT).show()
    }

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
                if (importRadioButton.isChecked == true){
                    openLauncher.launch(arrayOf("text/plain"))
                }
                else{
                    saveLauncher.launch("my-file.txt")
                }
            }
            backButton -> {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("key1", "value1");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent)
            }
        }
    }

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
                Toast.makeText(this, "Не получилось сохранить файл $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openFile(uri: Uri){
        openData = contentResolver.openInputStream(uri)?.use {
            String(it.readBytes())
        }
        val sharedPref = getSharedPreferences(
            "sharedFile", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("file", openData.toString()).apply()
    }

    private fun saveFile(uri: Uri){
        contentResolver.openOutputStream(uri)?.use {
            it.write(openData?.toByteArray())
        }
    }

    override fun onBackPressed() {
    }
}