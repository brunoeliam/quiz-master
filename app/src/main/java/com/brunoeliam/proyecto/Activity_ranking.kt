package com.brunoeliam.proyecto

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import org.json.JSONArray

class Activity_ranking : AppCompatActivity() {
    lateinit var  texto:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        texto=findViewById(R.id.JugadoresRanking)
        setear()
    }
    fun setear(){
        val preferencias: SharedPreferences = getSharedPreferences("Activity_preguntas",MODE_PRIVATE)
        val prefString = preferencias.getString("ranking", "[]")
        var vJSON = JSONArray(prefString)
        var aux: String = ""
        for (i in 0 until vJSON.length()) {
            aux += (i+1).toString() + "- " + vJSON.getJSONObject(i).getString("nombre") + " " +vJSON.getJSONObject(i).getInt("puntaje") + " / " + vJSON.getJSONObject(i).getInt("total") + "\n"
        }
        texto.text = aux
    }
    fun salirRanking(v: View){
        this.finish()
    }
}