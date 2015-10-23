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

class NewsController extends Controller with AuthElement with AuthConfigImpl with LoginLogout{ 
   //actions
  /**
   * Handle default path requests, redirect to story list
   */  
  def index =  Home 
   /**
   * This result directly redirect to the application home.
   */
  val Home = Redirect(routes.NewsController.list(0, 2, ""))
     


   lazy val newsForm :Form[NewsFormData]  = Form(
    mapping(
        "id" -> optional(number),
      "title" -> nonEmptyText,
      "text" -> nonEmptyText,
      "keywords" -> optional(text),
      "status" -> nonEmptyText,
      "newsCategoryId" -> number,
      "ratingId" -> number,
      "createdBy" -> optional(number))(NewsFormData.apply)(NewsFormData.unapply)
  )

  
  /**
   * Display the 'new news form'.
   */
  def create =  StackAction(AuthorityKey -> NormalUser) {implicit request =>
    Ok(html.auth.news.createForm(newsForm,Some(loggedIn)))
  }
  
  /**
   * Handle the 'new news form' submission.
   */
  def save =  StackAction(AuthorityKey -> NormalUser) {implicit request =>
    newsForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.auth.news.createForm(formWithErrors,Some(loggedIn))),
      newsFormData => {
       News.create(newsFormData.title, newsFormData.text, newsFormData.keywords.get, newsFormData.status,
           newsFormData.newsCategoryId, newsFormData.ratingId,1, new DateTime, null)
        Home.flashing("success" -> "News item %s has been created".format(newsFormData.title))
      }
    )
  }
  
  /**
   * Display the 'edit form' of a existing News Item.
   *
   * @param id Id of the News Item to edit
   */
  def edit(id: Long) = StackAction(AuthorityKey -> Administrator){implicit request =>{
      val newsItem = News.findBy(scalikejdbc.sqls.eq(News.n.id, id))
      newsItem match {
       case Some(existing) =>Ok(html.auth.news.editForm(id, 
           newsForm.fill(
               NewsFormData(Some(existing.id),existing.title,existing.text, Some(existing.keywords), existing.status,
           existing.newsCategoryId, existing.ratingId,Some(existing.createdBy))
               ),Some(loggedIn)))
       case None => Redirect(routes.NewsController.list()) flashing "message" -> "News Item could not be found"
      }
  }
  }
  
  /**
   * Handle the 'edit form' submission 
   *
   * @param id Id of the news to edit
   */
  def update(id: Long) = StackAction(AuthorityKey -> Administrator){implicit request =>
    newsForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.auth.news.editForm(id, formWithErrors,Some(loggedIn))),
      newsFormData => {
        val newsItem = models.News.find(id.toInt).get
        
        News.save(News(id.toInt,
          newsFormData.title, newsFormData.text, newsFormData.keywords.get, newsFormData.status,
           newsFormData.newsCategoryId, newsFormData.ratingId,1, new DateTime, Some(new DateTime)
        ))
        Home.flashing("success" -> "News Item %s has been updated".format(newsFormData.title))
      }
    )
  }
  
  /**
   * Handle story deletion.
   */
  def delete(id: Long) = Action {
    News.destroyNewsItem(id)
    Home.flashing("success" -> "News Item has been deleted")
  }

  def list(page: Int, orderBy: Int, filter: String) =  StackAction(AuthorityKey -> NormalUser) {implicit request =>
    Ok(html.news.listNewsItems(Some(loggedIn),
      News.list(page = page, orderBy = orderBy, filter = ("%"+filter+"%")),
      orderBy, filter
    ))
  }

  
}