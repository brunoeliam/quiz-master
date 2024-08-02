package com.brunoeliam.proyecto

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.brunoeliam.proyecto.R.color.*
import org.json.JSONArray
import org.json.JSONObject


class Activity_preguntas : AppCompatActivity() {
    lateinit var pregunta: TextView
    lateinit var puntaje: TextView
    lateinit var rta1: Button
    lateinit var rta2: Button
    lateinit var rta3: Button
    lateinit var rta4: Button
    lateinit var comodin: Button
    lateinit var BotonCorrecto: Button
    lateinit var catego: TextView
    lateinit var tiempo: TextView


    lateinit var vJSON: JSONArray
    lateinit var lista: ArrayList<Question>
    lateinit var nombreJugador: String
    var contador = 0
    var cantPreg = 0
    var Comodin = true
    var cortarTiempo = true
    val handler = Handler()
    var time = 30
    var entroEnRanking = false
    val Timer = object : Runnable {
        override fun run() {
            if (cortarTiempo && time >= 0) {
                tiempo.text = time.toString()
                time--
                handler.postDelayed(this, 1000)
            } else if (time == -1) {
                btnCorrecto(lista[cantPreg].respuestaCorrecta)
                BotonCorrecto.setBackgroundColor(getColor(gris))
                cantPreg++
                handler.postDelayed({
                    BotonCorrecto.setBackgroundColor(getColor(botonColor))
                    time = 30
                    if (cantPreg <= lista.size - 1) {
                        iniciarPregunta()
                    } else {
                        puntajeFinal()
                    }
                }, 2000)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preguntas)
        lista = intent.getSerializableExtra("dato") as ArrayList<Question>
        asignarIds()
        iniciarPregunta()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("ElCantPreg", cantPreg)
        outState.putInt("ElContador", contador)
        outState.putBoolean("ElComodin", Comodin)
        outState.putInt("Eltiempo", time)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        cantPreg = savedInstanceState.getInt("ElCantPreg")
        contador = savedInstanceState.getInt("ElContador")
        Comodin = savedInstanceState.getBoolean("ElComodin")
        time = savedInstanceState.getInt("Eltiempo")
        if (cantPreg <= lista.size - 1) {
            iniciarPregunta()
        } else {
            puntajeFinal()
        }
    }
    override fun onResume() {
        super.onResume()
        if (cantPreg > lista.size - 1) {
            dialogo()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mi: MenuInflater = menuInflater;
        mi.inflate(R.menu.action_bar_juego, menu);
        return true;
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i: Intent
        if (item.itemId == R.id.Ayuda) {
            i = Intent(this, Activity_ayuda::class.java)
            startActivity(i)
        } else if (item.itemId == R.id.Atras) {
            this.finish()
        }
        return true;
    }
    fun asignarIds() {
        val categoria = intent.getStringExtra("categoria")
        nombreJugador = intent.getStringExtra("Usuario").toString()
        val usuario = findViewById<TextView>(R.id.Usuario)
        usuario.text = nombreJugador
        tiempo = findViewById(R.id.tiempo)
        catego = findViewById(R.id.categoria)
        catego.setText(categoria.toString())
        pregunta = findViewById(R.id.texto)
        comodin = findViewById(R.id.comodin)
        rta1 = findViewById(R.id.rta1)
        rta2 = findViewById(R.id.rta2)
        rta3 = findViewById(R.id.rta3)
        rta4 = findViewById(R.id.rta4)
        puntaje = findViewById(R.id.puntaje)
    }
    fun iniciarPregunta() {
        if(cortarTiempo){
            handler.post(Timer)
        } else {
            seguir()
            handler.post(Timer)
        }
        rta1.text = lista[cantPreg].opcion[0]
        rta2.text = lista[cantPreg].opcion[1]
        rta3.text = lista[cantPreg].opcion[2]
        rta4.text = lista[cantPreg].opcion[3]
        pregunta.text = lista[cantPreg].pregunta
        if (Comodin) {
            puntaje.setText(contador.toString() + " / " + cantPreg.toString())
        } else {
            val preg: Int = cantPreg - 1
            puntaje.setText(contador.toString() + " / " + preg.toString())
            comodin.setBackgroundColor(getColor(rojo))
        }
    }
    fun seguir(){
        if(cortarTiempo){
            cortarTiempo = false
        }else{
            cortarTiempo = true
            time = 30
        }
    }
    fun jugar(v: View) {
        var cumplio: Boolean
        habilitarODeshabilitar()
        seguir()
        if (cantPreg < lista.size - 1) {
            cumplio = comparar(v, lista[cantPreg].respuestaCorrecta)
            if (cumplio) {
                contador++
            }
            cantPreg++
            correctoEIncorrecto(v, cumplio)
        } else {
            cumplio = comparar(v, lista[cantPreg].respuestaCorrecta)
            if (cumplio) {
                contador++
            }
            cantPreg++
            correctoEIncorrecto(v, cumplio)
        }
    }
    private fun habilitarODeshabilitar() {
        var chequear=rta1.isEnabled
        if(chequear){
            comodin.setEnabled(false)
            rta1.setEnabled(false)
            rta2.setEnabled(false)
            rta3.setEnabled(false)
            rta4.setEnabled(false)
        }else{
            comodin.setEnabled(true)
            rta1.setEnabled(true)
            rta2.setEnabled(true)
            rta3.setEnabled(true)
            rta4.setEnabled(true)
        }
    }
    fun correctoEIncorrecto(v: View, correcto: Boolean) {
        if (correcto) {
            v.setBackgroundColor(getColor(verde))
        } else {
            v.setBackgroundColor(getColor(rojo))
            BotonCorrecto.setBackgroundColor(getColor(gris))
        }
        handler.postDelayed({
            v.setBackgroundColor(getColor(botonColor))
            BotonCorrecto.setBackgroundColor(getColor(botonColor))
            habilitarODeshabilitar()
            if (cantPreg <= lista.size - 1) {
                iniciarPregunta()
            } else {
                puntajeFinal()
            }
        }, 2000)
    }
    fun comparar(view: View, a: Int): Boolean {
        var aux: Int = -1
        // para saber que boton selecciono el usuario
        when (view.id) {
            R.id.rta1 -> aux = 0
            R.id.rta2 -> aux = 1
            R.id.rta3 -> aux = 2
            R.id.rta4 -> aux = 3
        }
        // guarda en BotonCorrecto el id de la respuesta correcta
        btnCorrecto(a)
        return (a == aux)
    }
    private fun btnCorrecto(a:Int){
        when (a) {
            0 -> BotonCorrecto = rta1
            1 -> BotonCorrecto = rta2
            2 -> BotonCorrecto = rta3
            3 -> BotonCorrecto = rta4
        }
    }
    fun comodin(v: View) {
        if ((Comodin) && (cantPreg < lista.size - 1)) {
            cantPreg++
            Comodin = false
            seguir()
            handler.postDelayed({iniciarPregunta()},1000)
        } else {
            if ((cantPreg == lista.size - 1) && (Comodin)) {
                cantPreg++
                Comodin = false
                puntajeFinal()
            }
        }
    }
    fun compartir(){
        if (entroEnRanking) {
            val puntajeComp: String = resources.getString(R.string.miPuntaje)
            var auxCantPreg = cantPreg
            if (!Comodin) {
                auxCantPreg = cantPreg - 1
            }
            val aux: String = puntajeComp + " " + "$contador / $auxCantPreg"
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, aux)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent,null)
            startActivity(shareIntent)
        } else {
            Toast.makeText(this, R.string.NoTop5, Toast.LENGTH_LONG).show()
        }
    }
    fun nuevoJugador () {
        val usuarioJSON = JSONObject()
        var cantPregAux: Int = cantPreg
        usuarioJSON.put("nombre", nombreJugador)
        usuarioJSON.put("puntaje", contador)
        if (!Comodin) {
            cantPregAux -= 1
        }
        usuarioJSON.put("total", cantPregAux)
        usuarioJSON.put("porcentaje", (contador.toDouble() / cantPregAux.toDouble()) * 100)
        vJSON.put(usuarioJSON)
        entroEnRanking = true
    }
    private fun convertirAArrayList (): ArrayList<JSONObject> {
        var listaAux = ArrayList<JSONObject>()

        if (vJSON.length() == 0) {
            val usuarioJson = vJSON.getJSONObject(0)
            listaAux.add(0,usuarioJson)
        } else {
            for (i in 0 until vJSON.length()) {
                val usuarioJson = vJSON.getJSONObject(i)
                listaAux.add(i,usuarioJson)
            }
        }
        return listaAux
    }
    fun agregarJugador(){
        val listaAux: ArrayList<JSONObject>
        val preferencias: SharedPreferences = getPreferences(MODE_PRIVATE)
        val editor = preferencias.edit()
        val prefString = preferencias.getString("ranking", "[]")
        vJSON = JSONArray(prefString)
        if (vJSON.length() < 5) {
            nuevoJugador()
            listaAux = convertirAArrayList()
            listaAux.sortByDescending{it.getDouble("porcentaje")}
            listaAux.sortByDescending{it.getInt("puntaje")}
            vJSON = JSONArray(listaAux)
        } else {
            listaAux = convertirAArrayList()
            entroEnRanking = actualizarRanking(listaAux)
        }
        editor.putString("ranking", vJSON.toString())
        editor.commit()
    }
    fun actualizarRanking (listaAux: ArrayList<JSONObject>): Boolean {
        var aux = false

        // evaluo si el jugador debe entrar al top
        var cantPregAux: Int = cantPreg
        if (!Comodin) {
            cantPregAux -= 1
        }
        var salir = false
        val porcentajeActual = (contador.toDouble() / cantPregAux.toDouble()) * 100
        for (usuario in listaAux) {
            if (!salir && porcentajeActual >= usuario.getDouble("porcentaje")) {
                salir = true
                aux = true // para despues marcar que entro al top
                val ultimoUsuario = listaAux.last()
                ultimoUsuario.put("nombre", nombreJugador)
                ultimoUsuario.put("puntaje", contador)
                ultimoUsuario.put("total", cantPregAux)
                ultimoUsuario.put("porcentaje", porcentajeActual)
                // tambien puedo usar break
            }
        }
        // ordeno el array
        listaAux.sortByDescending{it.getDouble("porcentaje")}
        listaAux.sortByDescending{it.getInt("puntaje")}
        vJSON = JSONArray(listaAux) // convierto de nuevo a JSONArray
        return aux
    }
    fun puntajeFinal() {
        catego.visibility = View.GONE
        puntaje.visibility = View.GONE
        comodin.visibility = View.GONE
        pregunta.visibility = View.GONE
        tiempo.visibility = View.GONE
        rta1.visibility = View.GONE
        rta2.visibility = View.GONE
        rta3.visibility = View.GONE
        rta4.visibility = View.GONE
        agregarJugador()
        dialogo()
    }
private fun volverAjugar() {
    cantPreg = 0
    contador = 0
    Comodin = true
    comodin.setBackgroundColor(getColor(botonColor))
    puntaje.visibility = View.VISIBLE
    comodin.visibility = View.VISIBLE
    pregunta.visibility = View.VISIBLE
    tiempo.visibility = View.VISIBLE
    rta1.visibility = View.VISIBLE
    rta2.visibility = View.VISIBLE
    rta3.visibility = View.VISIBLE
    rta4.visibility = View.VISIBLE
    catego.visibility = View.VISIBLE
    iniciarPregunta()
}
private fun dialogo() {
    val aux: String;
    val cantPregAux: Int
    var i: Intent
    if (Comodin) {
        cantPregAux = cantPreg
        aux = resources.getString(R.string.ComodinNoUsado)
    } else {
        cantPregAux = cantPreg - 1
        aux = resources.getString(R.string.ComodinUsado)
    }
    val builder = AlertDialog.Builder(this@Activity_preguntas)
        .setTitle(resources.getString(R.string.resultado) + "  " + contador.toString() + " / " + cantPregAux.toString())
        .setMessage(aux)
        .setNegativeButton(R.string.Aceptar) { dialog, which ->
            this.finish()
            i = Intent(this, Activity_ranking::class.java)
            startActivity(i)
        }
        .setNeutralButton(R.string.VolverAJugar) { dialog, which ->
            volverAjugar()
        }
        .setPositiveButton(R.string.Compartir) { dialog, which ->
            compartir()
        }
    builder.show()
}
}