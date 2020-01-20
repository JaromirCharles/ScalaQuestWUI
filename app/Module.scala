import com.google.inject.AbstractModule

import de.scala_quest.controller.defaultImpl.Controller
import de.scala_quest.{GameState, UpdateAction}
import de.scala_quest.model.defaultImpl.Game

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure() = {
    val game = Game(List())
    val serverCtrl = Controller(GameState(UpdateAction.CLOSE_APPLICATION, game))

    bind(classOf[Controller]).toInstance(serverCtrl)
  }

}
