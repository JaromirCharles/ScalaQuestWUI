package controllers

import de.scala_quest.GameState
import javax.inject.Inject
import javax.inject.Singleton
import play.api.mvc._
import de.scala_quest.controller.defaultImpl.Controller
import de.scala_quest.model.Player
import de.scala_quest.view.Ui
import play.api.data.Form
import play.api.data.Forms._

@Singleton
class RestController @Inject() (
      cc: ControllerComponents,
      serverCtrl: Controller,
   ) extends AbstractController(cc) with Ui {

  /* Add WUI to observer list */
  serverCtrl.addObserver(this)

  def doLogin = Action { implicit request =>
    val loginRequest = loginForm.bindFromRequest.get
    Ok(s"username: '${loginRequest.player1}', password: '${loginRequest.player2}'")
  }

  case class LoginRequest(player1:String, player2:String)

  def loginForm = Form(mapping("player1" -> text, "player2" -> text)
  (LoginRequest.apply)(LoginRequest.unapply))

//  def onAddPlayers: Action[AnyContent] =  Action { implicit request =>
//    val loginRequest = loginForm.bindFromRequest.get
//    serverCtrl.addNewPlayerToGame(loginRequest.player1)
//    serverCtrl.addNewPlayerToGame(loginRequest.player2)
//    println("Players: " + serverCtrl.getPlayers())
//    Ok(s"first name: '${loginRequest.player1}', second name: '${loginRequest.player2}'")
//  }

  def onStartGame: Action[AnyContent] = Action {implicit request =>
    val loginRequest = loginForm.bindFromRequest.get

    serverCtrl.addNewPlayerToGame(loginRequest.player1)
    serverCtrl.addNewPlayerToGame(loginRequest.player2)
    println("Players: " + serverCtrl.getPlayers())
    serverCtrl.startGame()
    /* get roundNr, current player, points current question and current answers */
    val roundNr = serverCtrl.getRoundNr()
    val (playersName, playersPoints) = serverCtrl.getPlayerInfo()
    val currentQuestion = serverCtrl.getPlayersCurrentQuestion()
    val currentAnswers = serverCtrl.getPlayersCurrentAnswers()

    println("roundNr: " + roundNr)
    println("playersName: " + playersName)
    println("playersPoints: " + playersPoints)
    println("currentQuestion: " + currentQuestion)
    println("currentAnswers: " + currentAnswers)

    Ok(views.html.gamePlay(
      "ScalaQuest",
      roundNr,
      playersName,
      playersPoints,
      currentQuestion,
      currentAnswers,
      style ="scala"
      )
    )
  }

  def onQuitGame: Action[AnyContent] = Action {
    serverCtrl.onQuit()
    Ok(views.html.index("Thanks for playing ScalaQuest"))
  }

  def onNewGame: Action[AnyContent] = Action {
    serverCtrl.newGame()
    println("RestController.onNewGame")
    Ok(views.html.addPlayers("Enter player names", "",style = "scala"))
  }

//  def onAddNewPlayerToGame(name: String): Action[AnyContent] =  Action {
//    serverCtrl.addNewPlayerToGame(name)
//    val res = serverCtrl.getPlayerNames()
//    Ok(views.html.test(name))
//    Ok(views.html.addPlayers("New Game...", name, style ="scala"))
//  }

  case class PossibleAnswers(answer: String)

  def answerForm: Form[PossibleAnswers] = Form(mapping(
    "answer" -> text)
  (PossibleAnswers.apply)(PossibleAnswers.unapply))


  def onProcessAnswer: Action[AnyContent] = Action {implicit request =>
    val userInput = answerForm.bindFromRequest.get

    //Ok(s"ans1: '${userInput.answer}'")
    serverCtrl.processAnswer(userInput.answer.toInt)

    val roundNr = serverCtrl.getRoundNr()
    val (playersName, playersPoints) = serverCtrl.getPlayerInfo()
    val currentQuestion = serverCtrl.getPlayersCurrentQuestion()
    val currentAnswers = serverCtrl.getPlayersCurrentAnswers()

    println("roundNr: " + roundNr)
    println("playersName: " + playersName)
    println("playersPoints: " + playersPoints)
    println("currentQuestion: " + currentQuestion)
    println("currentAnswers: " + currentAnswers)

    if(roundNr < 4) {
      Ok(views.html.gamePlay(
        "ScalaQuest",
        roundNr,
        playersName,
        playersPoints,
        currentQuestion,
        currentAnswers,
        style = "scala"
      )
      )
    } else {
      val players: List[Player] = serverCtrl.getPlayers()
      val winner = getWinner(players)
      Ok(views.html.displayResults("Results", players, winner, style = "scala"))
    }
  }

   def getWinner(players: List[Player]): String = {
    val sortedAscList = players.sortBy(_.points)
    var winner: String = sortedAscList.last.name
    val highestScore = sortedAscList.last.points
    // Check if any other player has same high score
    sortedAscList.dropRight(1).foreach(p => {
      if (p.points == highestScore) {
        winner += s", ${p.name}"
      }
    })

    s"$winner"
  }

  override def update(updateData: GameState): Unit = {

  }
}