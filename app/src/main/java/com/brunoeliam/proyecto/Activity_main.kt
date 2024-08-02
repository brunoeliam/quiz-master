package com.brunoeliam.proyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import org.json.JSONArray
import java.io.InputStream

class Activity_main : AppCompatActivity() {
    var questionList: ArrayList<Question> = ArrayList()
    var nombre: String = ""
    private var mostrarDialogo = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
           mostrarDialogo = savedInstanceState.getBoolean("dialogo", true)
        }
        if (mostrarDialogo) {
            alertaUsuario()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("dialogo", mostrarDialogo)
        outState.putString("nombre", nombre)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mostrarDialogo = savedInstanceState.getBoolean("dialogo")
        nombre = savedInstanceState.getString("nombre")!!
    }

    private fun alertaUsuario() {
        val builder = AlertDialog.Builder(this@Activity_main)
        val view = layoutInflater.inflate(R.layout.dialogoxml, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val usuario = view.findViewById<EditText>(R.id.usuario)
        val botonGuardar = view.findViewById<Button>(R.id.btnDialogo)
        botonGuardar.setOnClickListener {
            nombre = usuario.text.toString()
            dialog.dismiss()
            mostrarDialogo = false
        }
    }

    private fun loadQuestionsFromJSON(name: String) {
        val inputStream: InputStream = assets.open(name + ".json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val text = String(buffer, Charsets.UTF_8)
        val jsonArray = JSONArray(text)
        for (i in 0..jsonArray.length() - 1) {
            val jsonObject = jsonArray.getJSONObject(i)
            val question = jsonObject.getString("question")
            val optionsArray = jsonObject.getJSONArray("options")
            val options = ArrayList<String>()
            for (j in 0..optionsArray.length() - 1) {
                options.add(optionsArray.getString(j))
            }
            val correctAnswerIndex = jsonObject.getInt("correctAnswerIndex")
            val q = Question(question, options, correctAnswerIndex)
            questionList.add(q)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mi: MenuInflater = menuInflater;
        mi.inflate(R.menu.action_bar_menuppal, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i: Intent
        if (item.itemId == R.id.Ayuda){
            i = Intent(this, Activity_ayuda::class.java)
        } else {
            i = Intent(this, Activity_ranking::class.java)
        }
        startActivity(i)
        return true;
    }

    override fun onResume() {
        super.onResume()
        questionList.clear() // Lo utilizamos para que se limpie el arrayList y no conserve las preguntas anteriores
    }

    fun iniciar(v: View) {
        val boton: Button = findViewById(v.id)
        val categoria = boton.text.toString()
        loadQuestionsFromJSON(categoria)
        val i = Intent(this, Activity_preguntas::class.java)
        i.putExtra("Usuario", nombre)
        i.putExtra("categoria", categoria)
        i.putExtra("dato", questionList)
        startActivity(i)
    }
}