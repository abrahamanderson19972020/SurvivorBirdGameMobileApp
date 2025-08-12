package com.abraham.survivorbirdgame.model

data class Question(
    val text:String,
    val correctAnswer:Int,
    val answers:List<Int>
)
{
    fun getShuffledAnswers(): List<Int> = (answers+ correctAnswer).shuffled()
}
