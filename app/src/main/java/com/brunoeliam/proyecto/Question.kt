package com.brunoeliam.proyecto

import java.io.Serializable

class Question (question: String, options: ArrayList<String>, correctAnswerIndex: Int): Serializable{
    var pregunta: String = question
    var opcion: ArrayList<String> = options
    var respuestaCorrecta: Int = correctAnswerIndex;

}
