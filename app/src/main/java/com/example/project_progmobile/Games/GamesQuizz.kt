package com.example.project_progmobile.Games

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.MainActivity
import com.example.project_progmobile.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max


class GamesQuizz : ComponentActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answerButton1: Button
    private lateinit var answerButton2: Button
    private lateinit var answerButton3: Button
    private lateinit var answerButton4: Button
    private lateinit var btnReturnToModes: Button
    private lateinit var btnReturnToHome: Button
    private lateinit var timerTextView: TextView
    private var currentQuestionIndex = 0
    private var totalScore = 0
    private var questionScore = 1000 // Score initial par question
    private var answeredAllQuestions = false
    private lateinit var countDownTimer: CountDownTimer
    private val delayBeforeNextQuestionMillis = 500L // Délai de 0.5 seconde
    private lateinit var questions: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games_capitales)

        questionTextView = findViewById(R.id.questionTextView)
        answerButton1 = findViewById(R.id.answerButton1)
        answerButton2 = findViewById(R.id.answerButton2)
        answerButton3 = findViewById(R.id.answerButton3)
        answerButton4 = findViewById(R.id.answerButton4)
        btnReturnToModes = findViewById(R.id.btnReturnToModes)
        btnReturnToHome = findViewById(R.id.btnReturnToHome)
        timerTextView = findViewById(R.id.timerTextView)

        val difficultyMode = intent.getStringExtra("difficulty_mode")
        questions = when (difficultyMode) {
            "easy" -> easyQuestions.shuffled().take(15)
            "medium" -> mediumQuestions.shuffled().take(15)
            "hard" -> hardQuestions.shuffled().take(15)
            else -> easyQuestions.shuffled().take(15)
        }

        displayQuestion()

        answerButton1.setOnClickListener { checkAnswer(0) }
        answerButton2.setOnClickListener { checkAnswer(1) }
        answerButton3.setOnClickListener { checkAnswer(2) }
        answerButton4.setOnClickListener { checkAnswer(3) }
        btnReturnToModes.setOnClickListener { startActivity(Intent(this, DifficultiesMode::class.java)) }
        btnReturnToHome.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }

    private fun displayQuestion() {
        resetAnswerButtonBackground()
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            questionTextView.text = "Question ${currentQuestionIndex + 1}/${questions.size}: ${question.question}"
            answerButton1.text = question.options[0]
            answerButton2.text = question.options[1]
            answerButton3.text = question.options[2]
            answerButton4.text = question.options[3]
            startTimer()
        } else {
            finishGame()
        }
    }

    private fun resetAnswerButtonBackground() {
        answerButton1.setBackgroundResource(android.R.drawable.btn_default)
        answerButton2.setBackgroundResource(android.R.drawable.btn_default)
        answerButton3.setBackgroundResource(android.R.drawable.btn_default)
        answerButton4.setBackgroundResource(android.R.drawable.btn_default)
    }

    private fun startTimer() {
        questionScore = 1000
        countDownTimer = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = String.format("%.1f", millisUntilFinished / 1000.0)
                questionScore -= 50
            }

            override fun onFinish() {
                timerTextView.text = "0.0"
                totalScore += max(0, questionScore)
                currentQuestionIndex++
                displayQuestion()
            }
        }.start()
    }

    private fun checkAnswer(selectedAnswerIndex: Int) {
        countDownTimer.cancel()
        if (answeredAllQuestions) return
        val question = questions[currentQuestionIndex]

        if (selectedAnswerIndex == question.correctAnswerIndex) {
            totalScore += questionScore
            highlightCorrectAnswer(selectedAnswerIndex)
        } else {
            highlightCorrectAnswer(question.correctAnswerIndex)
            highlightWrongAnswer(selectedAnswerIndex)
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(delayBeforeNextQuestionMillis)
            currentQuestionIndex++
            displayQuestion()
        }
    }

    private fun finishGame() {
        //val message = "Score final: $totalScore"

        answerButton1.visibility = View.INVISIBLE
        answerButton2.visibility = View.INVISIBLE
        answerButton3.visibility = View.INVISIBLE
        answerButton4.visibility = View.INVISIBLE
        answeredAllQuestions = true

        // Afficher une fenêtre de dialogue avec le score final
        val resultIntent = Intent()
        resultIntent.putExtra("score", totalScore)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
    private fun highlightCorrectAnswer(correctAnswerIndex: Int) {
        when (correctAnswerIndex) {
            0 -> answerButton1.setBackgroundResource(R.drawable.correct_answer_background)
            1 -> answerButton2.setBackgroundResource(R.drawable.correct_answer_background)
            2 -> answerButton3.setBackgroundResource(R.drawable.correct_answer_background)
            3 -> answerButton4.setBackgroundResource(R.drawable.correct_answer_background)
        }
    }

    private fun highlightWrongAnswer(wrongAnswerIndex: Int) {
        when (wrongAnswerIndex) {
            0 -> answerButton1.setBackgroundResource(R.drawable.wrong_answer_background)
            1 -> answerButton2.setBackgroundResource(R.drawable.wrong_answer_background)
            2 -> answerButton3.setBackgroundResource(R.drawable.wrong_answer_background)
            3 -> answerButton4.setBackgroundResource(R.drawable.wrong_answer_background)
        }
    }


    data class Question(
        val question: String,
        val options: List<String>,
        val correctAnswerIndex: Int
    )


    // Define questions for each difficulty mode
    private val easyQuestions = listOf(
        Question(
            "Quelle est la capitale de la France?",
            listOf("Paris", "Londres", "Berlin", "Madrid"),
            0
        ),
        Question(
            "Quelle est la capitale de l'Allemagne?",
            listOf("Paris", "Amsterdam", "Berlin", "Madrid"),
            2
        ),
        Question(
            "Quelle est la capitale de l'Espagne?",
            listOf("Paris", "Londres", "Berlin", "Madrid"),
            3
        ),
        Question(
            "Quelle est la capitale du Royaume-Uni?",
            listOf("Paris", "Londres", "Berlin", "Madrid"),
            1
        ),
        Question(
            "Quelle est la capitale de l'Italie?",
            listOf("Paris", "Londres", "Rome", "Madrid"),
            2
        ),
        Question(
            "Quelle est la couleur du ciel par temps clair?",
            listOf("Bleu", "Rouge", "Vert", "Jaune"),
            0
        ),
        Question(
            "Combien y a-t-il de jours dans une semaine?",
            listOf("5", "6", "7", "8"),
            2
        ),
        Question(
            "Quel est le nom de l'océan le plus grand?",
            listOf("Atlantique", "Indien", "Pacifique", "Arctique"),
            2
        ),
        Question(
            "Quel est le symbole chimique de l'eau?",
            listOf("H2O", "CO2", "O2", "NaCl"),
            0
        ),
        Question(
            "Combien font 2+2?",
            listOf("3", "4", "5", "6"),
            1
        ),
        Question(
            "Qui a peint la Joconde?",
            listOf("Leonardo da Vinci", "Vincent van Gogh", "Pablo Picasso", "Claude Monet"),
            0
        ),
        Question(
            "Quel est l'animal national de la Chine?",
            listOf("Dragon", "Panda géant", "Tigre", "Lion"),
            1
        ),
        Question(
            "Qui a écrit 'Harry Potter'?",
            listOf("J.K. Rowling", "Stephen King", "George Orwell", "Tolkien"),
            0
        ),
        Question(
            "Combien de côtés a un carré?",
            listOf("3", "4", "5", "6"),
            1
        ),
        Question(
            "Quelle est la plus haute montagne du monde?",
            listOf("Mont Everest", "Mont Kilimandjaro", "Mont Fuji", "Mont Blanc"),
            0
        ),
        Question(
            "Quelle est la plus longue rivière du monde?",
            listOf("Nil", "Mississippi", "Amazone", "Yangzi Jiang"),
            2
        ),
        Question(
            "Combien de continents y a-t-il sur Terre?",
            listOf("4", "5", "6", "7"),
            3
        ),
        Question(
            "Qui a inventé l'ampoule électrique?",
            listOf("Thomas Edison", "Albert Einstein", "Alexander Graham Bell", "Isaac Newton"),
            0
        ),
        Question(
            "Quelle est la monnaie officielle du Japon?",
            listOf("Dollar", "Euro", "Yen", "Pound"),
            2
        ),
        Question(
            "Quelle est la langue la plus parlée dans le monde?",
            listOf("Anglais", "Espagnol", "Chinois", "Hindi"),
            2
        ),
        Question(
            "Quelle est la capitale du Canada?",
            listOf("Toronto", "Ottawa", "Montréal", "Vancouver"),
            1
        ),
        Question(
            "Quel est le symbole chimique du fer?",
            listOf("Fe", "I", "Au", "Hg"),
            0
        ),
        Question(
            "Combien y a-t-il de lettres dans l'alphabet anglais?",
            listOf("24", "25", "26", "27"),
            2
        ),
        Question(
            "Qui a écrit 'Le Petit Chaperon Rouge'?",
            listOf("Grimm Brothers", "Hans Christian Andersen", "Charles Perrault", "George R. R. Martin"),
            2
        ),
        Question(
            "Quelle est la couleur du feu de circulation qui signifie 'arrêt'?",
            listOf("Vert", "Jaune", "Rouge", "Bleu"),
            2
        ),
        Question(
            "Combien de jours y a-t-il dans une année bissextile?",
            listOf("365", "366", "367", "368"),
            1
        ),
        Question(
            "Quel est le principal gaz constituant de l'atmosphère terrestre?",
            listOf("Oxygène", "Azote", "Argon", "Dioxyde de carbone"),
            1
        ),
        Question(
            "Quel est le plus grand océan du monde?",
            listOf("Atlantique", "Indien", "Pacifique", "Arctique"),
            2
        ),
        Question(
            "Quelle est la couleur du drapeau de la Chine?",
            listOf("Bleu", "Blanc", "Vert", "Rouge"),
            3
        ),
        Question(
            "Quel est le fruit le plus consommé au monde?",
            listOf("Banane", "Pomme", "Orange", "Raisin"),
            0
        )
    )





    private val mediumQuestions = listOf(
        Question(
            "Quelle est la capitale du Canada?",
            listOf("Toronto", "Ottawa", "Montréal", "Vancouver"),
            1
        ),
        Question(
            "Quelle est la capitale de l'Australie?",
            listOf("Sydney", "Canberra", "Melbourne", "Brisbane"),
            1
        ),
        Question(
            "Quelle est la capitale de l'Inde?",
            listOf("New Delhi", "Mumbai", "Bangalore", "Kolkata"),
            0
        ),
        Question(
            "Quelle est la capitale du Brésil?",
            listOf("Rio de Janeiro", "Brasilia", "Sao Paulo", "Belo Horizonte"),
            1
        ),
        Question(
            "Quelle est la capitale du Japon?",
            listOf("Tokyo", "Osaka", "Kyoto", "Hiroshima"),
            0
        ),
        Question(
            "Quel est le nom du président actuel des États-Unis?",
            listOf("Donald Trump", "Joe Biden", "Barack Obama", "George W. Bush"),
            1
        ),
        Question(
            "Quelle est la devise de la France?",
            listOf("Lira", "Euro", "Peso", "Franc"),
            1
        ),
        Question(
            "Quelle est la planète la plus proche du Soleil?",
            listOf("Vénus", "Terre", "Mercure", "Mars"),
            2
        ),
        Question(
            "Qui a écrit 'Roméo et Juliette'?",
            listOf("William Shakespeare", "Charles Dickens", "Jane Austen", "Leo Tolstoy"),
            0
        ),
        Question(
            "Quel est le plus grand mammifère terrestre?",
            listOf("Éléphant", "Baleine bleue", "Girafe", "Rhino"),
            1
        ),
        Question(
            "Quelle est la monnaie officielle du Royaume-Uni?",
            listOf("Dollar", "Euro", "Yen", "Livre sterling"),
            3
        ),
        Question(
            "Quel est l'élément chimique le plus abondant dans l'Univers?",
            listOf("Hydrogène", "Oxygène", "Carbone", "Azote"),
            0
        ),
        Question(
            "Qui a peint 'Les Tournesols'?",
            listOf("Vincent van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Claude Monet"),
            0
        ),
        Question(
            "Quel est le plus grand océan du monde?",
            listOf("Atlantique", "Indien", "Pacifique", "Arctique"),
            2
        ),
        Question(
            "Qui a écrit '1984'?",
            listOf("George Orwell", "J.R.R. Tolkien", "Stephen King", "J.K. Rowling"),
            0
        ),
        Question(
            "Quelle est la plus longue rivière d'Europe?",
            listOf("Danube", "Volga", "Rhin", "Loire"),
            1
        ),
        Question(
            "Quelle est la montagne la plus haute d'Europe?",
            listOf("Mont Blanc", "Mont Everest", "Mont Elbrouz", "Mont Kilimandjaro"),
            2
        ),
        Question(
            "Quel est le symbole chimique du carbone?",
            listOf("Ca", "C", "Co", "Cr"),
            1
        ),
        Question(
            "Combien de pieds y a-t-il dans un mètre?",
            listOf("3", "10", "100", "1000"),
            2
        ),

        Question(
            "Quelle est la capitale de l'Australie?",
            listOf("Sydney", "Canberra", "Melbourne", "Brisbane"),
            1
        ),
        Question(
            "Qui a peint 'La Nuit étoilée'?",
            listOf("Pablo Picasso", "Vincent van Gogh", "Leonardo da Vinci", "Claude Monet"),
            1
        ),
        Question(
            "Combien de côtés a un octogone?",
            listOf("6", "7", "8", "9"),
            2
        ),
        Question(
            "Quel est le principal ingrédient du guacamole?",
            listOf("Avocat", "Tomate", "Oignon", "Citron vert"),
            0
        ),
        Question(
            "Qui a écrit 'Orgueil et Préjugés'?",
            listOf("Charlotte Brontë", "Emily Brontë", "Jane Austen", "Virginia Woolf"),
            2
        ),
        Question(
            "Quel est le plus grand désert chaud du monde?",
            listOf("Sahara", "Gobi", "Kalahari", "Arabie"),
            0
        ),
        Question(
            "Quelle est la devise de l'Allemagne?",
            listOf("Euro", "Mark", "Franc", "Dollar"),
            0
        ),
        Question(
            "Quel est le nom du célèbre pharaon égyptien?",
            listOf("Toutânkhamon", "Hatchepsout", "Néfertiti", "Ramsès II"),
            3
        ),
        Question(
            "Quelle est la capitale de l'Argentine?",
            listOf("Buenos Aires", "Santiago", "Lima", "Brasilia"),
            0
        )
    )

    private val hardQuestions = listOf(

        Question(
            "Quelle est la capitale de la Mongolie?",
            listOf("Oulan-Bator", "Astana", "Kuala Lumpur", "Singapour"),
            0
        ),
        Question(
            "Quelle est la capitale de l'Islande?",
            listOf("Reykjavik", "Helsinki", "Oslo", "Copenhague"),
            0
        ),
        Question(
            "Quelle est la capitale de Djibouti?",
            listOf("Djibouti", "Kigali", "Nairobi", "Tripoli"),
            0
        ),
        Question(
            "Quelle est la capitale du Bhoutan?",
            listOf("Thimphou", "Katmandou", "New Delhi", "Dacca"),
            0
        ),
        Question(
            "Quelle est la capitale du Suriname?",
            listOf("Paramaribo", "Georgetown", "Kingston", "Port-au-Prince"),
            0
        ) ,
        Question(
            "Quel est le pays le plus peuplé du monde?",
            listOf("Chine", "Inde", "États-Unis", "Brésil"),
            1
        ),
        Question(
            "Qui a écrit 'Guerre et Paix'?",
            listOf("Leo Tolstoy", "Fyodor Dostoevsky", "Anton Chekhov", "Nikolai Gogol"),
            0
        ),
        Question(
            "Quelle est la plus longue chaîne de montagnes du monde?",
            listOf("Himalaya", "Andes", "Rocheuses", "Alpes"),
            1
        ),
        Question(
            "Quel est le pays le plus grand du monde en superficie?",
            listOf("Russie", "Canada", "Chine", "États-Unis"),
            0
        ),
        Question(
            "Qui a écrit 'Le Petit Prince'?",
            listOf("Antoine de Saint-Exupéry", "Victor Hugo", "Albert Camus", "Jean-Paul Sartre"),
            0
        ),
        Question(
            "Quel est le composant principal de l'air que nous respirons?",
            listOf("Oxygène", "Azote", "Argon", "Dioxyde de carbone"),
            1
        ),
        Question(
            "Combien de planètes composent notre système solaire?",
            listOf("7", "8", "9", "10"),
            1
        ),
        Question(
            "Quelle est la plus haute cascade du monde?",
            listOf("Angel Falls", "Niagara Falls", "Victoria Falls", "Yosemite Falls"),
            0
        ),
        Question(
            "Quelle est la plus longue rivière d'Afrique?",
            listOf("Niger", "Zambèze", "Congo", "Nil"),
            3
        ),
        Question(
            "Qui a écrit 'Le Rouge et le Noir'?",
            listOf("Stendhal", "Gustave Flaubert", "Honoré de Balzac", "Émile Zola"),
            0
        ),

        Question(
            "Quel est le plus grand désert du monde?",
            listOf("Sahara", "Gobi", "Kalahari", "Antarctique"),
            0
        ),
        Question(
            "Quelle est la distance approximative de la Terre à la Lune en kilomètres?",
            listOf("100,000", "250,000", "384,400", "500,000"),
            2
        ),
        Question(
            "Qui a découvert la gravité?",
            listOf("Isaac Newton", "Albert Einstein", "Galileo Galilei", "Nikola Tesla"),
            0
        ),
        Question(
            "Quelle est la capitale de la Mongolie?",
            listOf("Oulan-Bator", "Astana", "Kuala Lumpur", "Singapour"),
            0
        ),
        Question(
            "Qui a écrit 'Crime et Châtiment'?",
            listOf("Fiodor Dostoïevski", "Léon Tolstoï", "Alexandre Pouchkine", "Anton Tchekhov"),
            0
        ),
        Question(
            "Combien de fois la Tour Eiffel a-t-elle été construite?",
            listOf("1", "2", "3", "4"),
            0
        ),
        Question(
            "Quelle est la distance moyenne entre la Terre et le Soleil?",
            listOf("120 millions de km", "150 millions de km", "180 millions de km", "200 millions de km"),
            1
        ),
        Question(
            "Quelle est la montagne la plus haute d'Afrique?",
            listOf("Mont Kenya", "Mont Elbrouz", "Mont Kilimandjaro", "Mont Ruwenzori"),
            2
        ),
        Question(
            "Quel est le plus grand lac d'eau douce au monde?",
            listOf("Lac Supérieur", "Lac Michigan", "Lac Baïkal", "Lac Victoria"),
            2
        ),
        Question(
            "Qui a inventé la machine à vapeur?",
            listOf("James Watt", "Thomas Edison", "Nikola Tesla", "Albert Einstein"),
            0
        ),
        Question(
            "Quel est le plus long fleuve d'Europe?",
            listOf("Volga", "Danube", "Dniepr", "Rhône"),
            0
        ),
        Question(
            "Qui a découvert la circulation sanguine?",
            listOf("William Harvey", "Andreas Vesalius", "Hippocrate", "Claude Bernard"),
            0
        ),
        Question(
            "Quelle est la capitale du Bangladesh?",
            listOf("Dhaka", "Colombo", "Katmandou", "Islamabad"),
            0
        )
    )
}
