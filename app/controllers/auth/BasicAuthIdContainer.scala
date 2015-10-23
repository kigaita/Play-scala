package controllers.auth

/**
 * @author kigaita
 */

import jp.t2v.lab.play2.auth.{AuthenticityToken, AsyncIdContainer,CacheIdContainer}
import play.api.mvc.RequestHeader
import scala.concurrent.{Future, ExecutionContext}
import models.User

class BasicAuthIdContainer extends AsyncIdContainer[User] {
  override def prolongTimeout(token: AuthenticityToken, timeoutInSeconds: Int)(implicit request: RequestHeader, context: ExecutionContext): Future[Unit] = {
    Future.successful(())
  }

  override def get(token: AuthenticityToken)(implicit context: ExecutionContext): Future[Option[User]] = Future {
    val Pattern = "(.*?):(.*)".r
    PartialFunction.condOpt(token) {
      case Pattern(user, pass) => User.authenticate(user, pass)
    }.flatten
  }

  override def remove(token: AuthenticityToken)(implicit context: ExecutionContext): Future[Unit] = {
    Future.successful(())
  }

 override def startNewSession(userId: User, timeoutInSeconds: Int)
 (implicit request: RequestHeader, context: ExecutionContext):  Future[AuthenticityToken] 
 = Future{new CacheIdContainer[User].startNewSession(userId, timeoutInSeconds)}
}