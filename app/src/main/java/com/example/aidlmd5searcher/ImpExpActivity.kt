package com.example.aidlmd5searcher

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.view.View
import android.view.View.*
import android.widget.*
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.core.view.isVisible

class ImpExpActivity : AppCompatActivity(), OnClickListener{
    
    private lateinit var importRadioButton:RadioButton
    private lateinit var exportRadioButton:RadioButton
    private lateinit var dirBtn: Button
    private lateinit var openLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var saveLauncher:ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imp_exp_acitivity)

        initElements()

        openLauncher()

        saveLauncher()

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show()
    }
    
    private fun initElements(){
        importRadioButton = findViewById(R.id.radioButton)
        importRadioButton.setOnClickListener(this)
        exportRadioButton = findViewById(R.id.radioButton2)
        exportRadioButton.setOnClickListener(this)
        dirBtn = findViewById(R.id.dirButton)
        dirBtn.setOnClickListener(this)
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
                Toast.makeText(this, "Directory", Toast.LENGTH_SHORT).show()
                if (importRadioButton.isChecked == true){
                    openLauncher.launch(arrayOf("text/plain"))
                }
                else{
                    saveLauncher.launch("my-file.txt")
                }
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
                Toast.makeText(this, "Can't open it", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Can't save it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openFile(uri: Uri){
        val data = contentResolver.openInputStream(uri)?.use {
            //Запуск сервиса
            String(it.readBytes())
        }
        Toast.makeText(this, "Текст из файла - $data ", Toast.LENGTH_SHORT).show()
    }

    private fun saveFile(uri: Uri){
        val data = contentResolver.openOutputStream(uri)?.use {
            val text = "example"
            it.write(text.toByteArray())
        }
        Toast.makeText(this, "Текст из файла - $data  ", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show()
    }

}