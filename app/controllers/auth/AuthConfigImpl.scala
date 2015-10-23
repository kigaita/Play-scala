package controllers.auth

/**
 * @author kigaita
 */
import play.api.mvc.RequestHeader
import play.api.mvc.Results._
import play.api._
import play.api.mvc._
import scala.concurrent.{Future, ExecutionContext}
import jp.t2v.lab.play2.auth.AuthConfig
import models.{Permission, User}
import models.Permission._
import scala.reflect.{ClassTag, classTag}
import play.Routes

trait AuthConfigImpl extends AuthConfig {

  type Id = Int
  type User = models.User
  type Authority = Permission

  val idTag: ClassTag[Id] = classTag[Id]
  val sessionTimeoutInSeconds = 3600

  def resolveUser(id: Id)(implicit ctx: ExecutionContext) = Future.successful(User.find(id))
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext) = {
    println(" Permision "+user.permission+" authority "+authority)
    Future.successful((user.permission, authority) match {
    case (models.Administrator, authority) => true
    case (models.NormalUser, authority) => true
    case _ => false
  })
  }
  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) ={
     println(" >>> login successful")
    Future.successful(Redirect(controllers.routes.UserController.profile()))
  }
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = {
    println(" >>> logout successful")
    Future.successful(Redirect(controllers.routes.UserController.login))
  }
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = {
    println(" >>> login failed")
    Future.successful(Redirect(controllers.routes.UserController.login))
  }
  def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])(implicit ctx: ExecutionContext) ={
     println(" >>> authorizationFailed failed")
    Future.successful(Forbidden("no permission"))
  }

  
}