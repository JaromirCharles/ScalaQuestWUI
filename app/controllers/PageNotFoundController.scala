package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle invalid HTTP requests to the
 * application's home page.
 */
@Singleton
class PageNotFoundController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  /**
   * Create an Action to render an HTML page with a 404 message.
   */
  def index(currentPath: String): Action[AnyContent] = Action {
    implicit request: RequestHeader => println(s"User tried to access unavailable route '${currentPath}'")
    NotFound(views.html.pageNotFound(request.toString()))
  }
}
