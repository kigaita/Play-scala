package controllers
import play.api._
import utils.Page
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.auth._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.data.validation.Constraint
import play.api.data.validation.Valid
import play.api.data.validation.Invalid
import play.api.i18n.Messages
import scala.concurrent.{Future, ExecutionContext}
import scala.reflect.{ClassTag, classTag}
import jp.t2v.lab.play2.auth.LoginLogout
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import views._
import models._
import jp.t2v.lab.play2.auth.AuthElement
import org.joda.time.DateTime

/**
 * @author kigaita
 */
class UserController extends Controller with AuthElement with AuthConfigImpl with LoginLogout{ 
   
  
  //actions
  /**
   * Handle default path requests, redirect to users list
   */  
  def index =  login 
  
  val Home = Redirect(routes.UserController.list(0, 2, ""))
      /**
   * This result directly redirect to the application home.
   */
 // val Home = Redirect(routes.Application.lessons(0, 2, ""))
  
   private def verifyPasswords = Constraint[SignupData] {
    data: SignupData =>
      Logger.debug("verifying passwords")
      data.password match {
        case data.passwordRepeat => Valid
        case _ => {
          Logger.warn("Invalid second password")
          play.api.data.validation.Invalid(play.api.i18n.Messages("user.password.validation.no.match"))
        }
      }
  }

   lazy val loginForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText) verifying ("Invalid user or password", result => result match {
        case (email, password) => {
          println("user=" + email + " password=" + password);
          val userList = User.authenticate(email, password)
         // userList == 1
          userList isDefined
        }
        case _ => false
      }))

   /**
   * Describe the user form (used in both edit and create screens).
   */ 
  val userForm :Form[SignupData] = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "passwordRepeat"-> nonEmptyText,
      "firstname" -> nonEmptyText,
      "lastname" -> nonEmptyText
    )(SignupData.apply)(SignupData.unapply)verifying (verifyPasswords)
  )
 
  
  
  /**
   * Display the 'new user form'.
   */
  def create =  Action {implicit request =>
    Ok(html.user.createForm(userForm))
  }
  
  /**
   * Handle the 'new user form' submission.
   */
  def save =  Action {implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.user.createForm(formWithErrors)),
      signupData => {
        User.create(request.remoteAddress, signupData.email,  signupData.password, null, signupData.email, NormalUser,
            null, null, null, null, null, 
            new DateTime, null, Some(true), Some(signupData.firstName), Some(signupData.lastName), null, null)
       // User.createUser(signupData.email, signupData.password, signupData.fullname)
        Home.flashing("success" -> "User %s has been created".format(signupData.firstName))
      }
    )
  }
  
  /**
   * Display the 'edit form' of a existing User.
   *
   * @param id Id of the User to edit
   */
  def edit(id: Long) = StackAction(AuthorityKey -> Administrator){implicit request =>{
      val user = User.findBy(scalikejdbc.sqls.eq(User.u.id, id))
      user match {
       case Some(existing) =>Ok(html.auth.user.editForm(id, 
           userForm.fill(SignupData(existing.email,"","",existing.firstName.get,existing.lastName.get)
               ),Some(loggedIn)))
       case None => Redirect(routes.UserController.list()) flashing "message" -> "User could not be found"
      }
  }
  }
  
  /**
   * Handle the 'edit form' submission 
   *
   * @param id Id of the user to edit
   */
  def update(id: Long) = StackAction(AuthorityKey -> Administrator){implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.auth.user.editForm(id, formWithErrors,Some(loggedIn))),
      signupData => {
        val user = models.User.find(id.toInt).get
        
        User.save(User(id.toInt,
          request.remoteAddress, signupData.email,  signupData.password, null, signupData.email, NormalUser,
            null, null, null, null, null, 
            new DateTime, null, Some(true), Some(signupData.firstName), Some(signupData.lastName), null, null
        ))
        Home.flashing("success" -> "User %s has been updated".format(signupData.firstName))
      }
    )
  }
  
  def profile = StackAction(AuthorityKey -> NormalUser){implicit request =>
    Ok(html.auth.user.profile(Some(loggedIn)))
  }
  
  /**
   * Handle user deletion.
   */
  def delete(id: Long) = Action {
    User.destroyUser(id)
    Home.flashing("success" -> "User has been deleted")
  }

  def list(page: Int, orderBy: Int, filter: String) =  StackAction(AuthorityKey -> Administrator){implicit request =>
    Ok(html.admin.user.listUsers(Some(loggedIn),
      User.list(page = page, orderBy = orderBy, filter = ("%"+filter+"%")),
      orderBy, filter
    ))
  }

  
  def login = Action {implicit request =>
    
    Ok(html.user.login(loginForm))
  }
  
  /**
   * Handle login form submission.
   */
  def authenticate =  Action.async{implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.user.login(formWithErrors))),
//      user => Redirect(Some(loggedIn)
//          routes.AdminController.index).withSession("email" -> user._1)
      
      user =>{
       val userFound = User.findBy(scalikejdbc.sqls.eq(User.u.email, user._1))
        println("FOund user "+userFound.get.firstName+" role : "+userFound.get.permission)
      // flash("message", "FOund user "+userFound.get.fullname+" role : "+userFound.get.permission);
       //gotoLoginSucceeded(userFound)
     userFound match {
       case Some(userFound) => {
         gotoLoginSucceeded(userFound.id)
       }
       case None => Future.successful(Redirect(routes.UserController.profile()) flashing "message" -> "User could not be found")
        }
        }
      )
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action.async {
//    Redirect(routes.Application.login).withNewSession.flashing(
//      "success" -> "You've been logged out")
    implicit request => gotoLogoutSucceeded.map(_.flashing(
   "message" -> "You've been logged out"
    ))
  }  
}