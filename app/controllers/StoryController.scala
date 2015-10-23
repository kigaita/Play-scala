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
class StoryController extends Controller with AuthElement with AuthConfigImpl with LoginLogout{ 
   //actions
  /**
   * Handle default path requests, redirect to story list
   */  
  def index =  Home 
   /**
   * This result directly redirect to the application home.
   */
  val Home = Redirect(routes.StoryController.list(0, 2, ""))
     


   lazy val storyForm :Form[StoryFormData]  = Form(
    mapping(
        "id" -> optional(number),
      "title" -> nonEmptyText,
      "text" -> nonEmptyText,
      "keywords" -> optional(text),
      "status" -> nonEmptyText,
      "storyCategoryId" -> number,
      "ratingId" -> number,
      "createdBy" -> optional(number))(StoryFormData.apply)(StoryFormData.unapply)
  )

  
  /**
   * Display the 'new story form'.
   */
  def create =  StackAction(AuthorityKey -> NormalUser) {implicit request =>
    Ok(html.auth.story.createForm(storyForm,Some(loggedIn)))
  }
  
  /**
   * Handle the 'new story form' submission.
   */
  def save =  StackAction(AuthorityKey -> NormalUser) {implicit request =>
    storyForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.auth.story.createForm(formWithErrors,Some(loggedIn))),
      storyFormData => {
       Story.create(storyFormData.title, storyFormData.text, storyFormData.keywords, storyFormData.status,
           storyFormData.storyCategoryId, storyFormData.ratingId,1, new DateTime, null)
        Home.flashing("success" -> "User %s has been created".format(storyFormData.title))
      }
    )
  }
  
  /**
   * Display the 'edit form' of a existing Story.
   *
   * @param id Id of the Story to edit
   */
  def edit(id: Long) = StackAction(AuthorityKey -> Administrator){implicit request =>{
      val story = Story.findBy(scalikejdbc.sqls.eq(Story.s.id, id))
      story match {
       case Some(existing) =>Ok(html.auth.story.editForm(id, 
           storyForm.fill(
               StoryFormData(Some(existing.id),existing.title,existing.text, existing.keywords, existing.status,
           existing.storyCategoryId, existing.ratingId,Some(existing.createdBy))
               ),Some(loggedIn)))
       case None => Redirect(routes.StoryController.list()) flashing "message" -> "Story could not be found"
      }
  }
  }
  
  /**
   * Handle the 'edit form' submission 
   *
   * @param id Id of the story to edit
   */
  def update(id: Long) = StackAction(AuthorityKey -> Administrator){implicit request =>
    storyForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.auth.story.editForm(id, formWithErrors,Some(loggedIn))),
      storyFormData => {
        val story = models.Story.find(id.toInt).get
        
        Story.save(Story(id.toInt,
          storyFormData.title, storyFormData.text, storyFormData.keywords, storyFormData.status,
           storyFormData.storyCategoryId, storyFormData.ratingId,1, new DateTime, Some(new DateTime)
        ))
        Home.flashing("success" -> "Story %s has been updated".format(storyFormData.title))
      }
    )
  }
  
  /**
   * Handle story deletion.
   */
  def delete(id: Long) = Action {
    Story.destroyStory(id)
    Home.flashing("success" -> "Story has been deleted")
  }

  def list(page: Int, orderBy: Int, filter: String) =  StackAction(AuthorityKey -> NormalUser) {implicit request =>
    Ok(html.story.listStories(Some(loggedIn),
      Story.list(page = page, orderBy = orderBy, filter = ("%"+filter+"%")),
      orderBy, filter
    ))
  }

  
}