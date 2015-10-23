package models

import scalikejdbc._
import org.joda.time.{DateTime}
import utils.Page

case class NewsFormData(id: Option[Int],title: String, text: String,keywords: Option[String] = None,
    status: String, newsCategoryId: Int,ratingId: Int,createdBy: Option[Int])
case class News(
  id: Int,
  title: String,
  text: String,
  keywords: String,
  status: String,
  newsCategoryId: Int,
  ratingId: Int,
  createdBy: Int,
  createdOn: DateTime,
  modifiedOn: Option[DateTime] = None) {

  def save()(implicit session: DBSession = News.autoSession): News = News.save(this)(session)

  def destroy()(implicit session: DBSession = News.autoSession): Unit = News.destroy(this)(session)

}


object News extends SQLSyntaxSupport[News] {

  override val tableName = "news"

  override val columns = Seq("id", "title", "text", "keywords", "status", "news_category_id", "rating_id", "created_by", "created_on", "modified_on")

  def apply(n: SyntaxProvider[News])(rs: WrappedResultSet): News = apply(n.resultName)(rs)
  def apply(n: ResultName[News])(rs: WrappedResultSet): News = new News(
    id = rs.get(n.id),
    title = rs.get(n.title),
    text = rs.get(n.text),
    keywords = rs.get(n.keywords),
    status = rs.get(n.status),
    newsCategoryId = rs.get(n.newsCategoryId),
    ratingId = rs.get(n.ratingId),
    createdBy = rs.get(n.createdBy),
    createdOn = rs.get(n.createdOn),
    modifiedOn = rs.get(n.modifiedOn)
  )

  val n = News.syntax("n")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[News] = {
    withSQL {
      select.from(News as n).where.eq(n.id, id)
    }.map(News(n.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[News] = {
    withSQL(select.from(News as n)).map(News(n.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(News as n)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[News] = {
    withSQL {
      select.from(News as n).where.append(where)
    }.map(News(n.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[News] = {
    withSQL {
      select.from(News as n).where.append(where)
    }.map(News(n.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(News as n).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: String,
    text: String,
    keywords: String,
    status: String,
    newsCategoryId: Int,
    ratingId: Int,
    createdBy: Int,
    createdOn: DateTime,
    modifiedOn: Option[DateTime] = None)(implicit session: DBSession = autoSession): News = {
    val generatedKey = withSQL {
      insert.into(News).columns(
        column.title,
        column.text,
        column.keywords,
        column.status,
        column.newsCategoryId,
        column.ratingId,
        column.createdBy,
        column.createdOn,
        column.modifiedOn
      ).values(
        title,
        text,
        keywords,
        status,
        newsCategoryId,
        ratingId,
        createdBy,
        createdOn,
        modifiedOn
      )
    }.updateAndReturnGeneratedKey.apply()

    News(
      id = generatedKey.toInt,
      title = title,
      text = text,
      keywords = keywords,
      status = status,
      newsCategoryId = newsCategoryId,
      ratingId = ratingId,
      createdBy = createdBy,
      createdOn = createdOn,
      modifiedOn = modifiedOn)
  }

  def batchInsert(entities: Seq[News])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'title -> entity.title,
        'text -> entity.text,
        'keywords -> entity.keywords,
        'status -> entity.status,
        'newsCategoryId -> entity.newsCategoryId,
        'ratingId -> entity.ratingId,
        'createdBy -> entity.createdBy,
        'createdOn -> entity.createdOn,
        'modifiedOn -> entity.modifiedOn))
        SQL("""insert into news(
        title,
        text,
        keywords,
        status,
        news_category_id,
        rating_id,
        created_by,
        created_on,
        modified_on
      ) values (
        {title},
        {text},
        {keywords},
        {status},
        {newsCategoryId},
        {ratingId},
        {createdBy},
        {createdOn},
        {modifiedOn}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: News)(implicit session: DBSession = autoSession): News = {
    withSQL {
      update(News).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.text -> entity.text,
        column.keywords -> entity.keywords,
        column.status -> entity.status,
        column.newsCategoryId -> entity.newsCategoryId,
        column.ratingId -> entity.ratingId,
        column.createdBy -> entity.createdBy,
        column.createdOn -> entity.createdOn,
        column.modifiedOn -> entity.modifiedOn
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: News)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(News).where.eq(column.id, entity.id) }.update.apply()
  }

     def destroyNewsItem(id: Long)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(News).where.eq(column.id, id) }.update.apply()
  }
   
     
  /**
   * Return a page of (News Items).
   *
   * @param page Page to display
   * @param pageSize Number of news Items per page
   * @param orderBy news property used for sorting
   * @param filter Filter applied on the name column
   */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%")(implicit session: DBSession = autoSession) : Page[(News)] = {
    
    val offest = pageSize * page
    
   val newsItems = withSQL {
      select.from(News as n).where.like(n.newsCategoryId,filter)
      .orderBy(n.createdOn)
      .limit(pageSize)
      .offset(offest)
    }.map(News(n.resultName)).list.apply()
    
   
      val totalRows =  withSQL {
      select(sqls.count).from(News as n).where.like(n.newsCategoryId,filter)
    }.map(_.long(1)).single.apply().get
      

      Page(newsItems, page, offest, totalRows)
      
    }
}
