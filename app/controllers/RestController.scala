package controllers

import javax.inject.Inject
import javax.inject.Singleton
import play.api.mvc._
import de.scala_quest.controller.defaultImpl.Controller

// inject controller
@Singleton
class RestController @Inject() (
      cc: ControllerComponents,
      serverCtrl: Controller,
   ) extends AbstractController(cc) {

  def onStartGame: Action[AnyContent] = Action {
    serverCtrl.startGame()
    Ok(views.html.gamePlay("ScalaQuest"))
  }

  def onQuitGame: Action[AnyContent] = Action {
    serverCtrl.onQuit()
    Ok(views.html.test("Thanks for playing ScalaQuest..."))
  }

  def onNewGame: Action[AnyContent] = Action {
    serverCtrl.newGame()
    Ok(views.html.addPlayers("New Game...", style = "scala"))
  }

  def onAddNewPlayerToGame(name: String): Action[AnyContent] = Action {
    serverCtrl.addNewPlayerToGame(name)
    println("here")
    Ok("I received post data")
    //Ok(views.html.test(name))
  }

  def onGetRoundNr(): Action[AnyContent] = Action {
    Ok(views.html.test("onGetRoundNr"))
  }

  def onGetPlayerInfo(): Action[AnyContent] = Action {
    Ok(views.html.test("onGetPlayerInfot"))
  }

  def onGetPlayersCurrentQuestion(): Action[AnyContent] = Action {
    Ok(views.html.test("onGetPlayersCurrentQuestion"))
  }

  def testPost(): Action[AnyContent] = Action {
    Ok("I received post data")
    Ok("I received post data")
  }
}
