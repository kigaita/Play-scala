package models

import scalikejdbc._
import org.joda.time.{DateTime}
import utils.Page

case class StoryFormData(id: Option[Int],title: String, text: String,keywords: Option[String] = None,
    status: String, storyCategoryId: Int,ratingId: Int,createdBy: Option[Int])
case class Story(
  id: Int,
  title: String,
  text: String,
  keywords: Option[String] = None,
  status: String,
  storyCategoryId: Int,
  ratingId: Int,
  createdBy: Int,
  createdOn: DateTime,
  modifiedOn: Option[DateTime] = None) {

  def save()(implicit session: DBSession = Story.autoSession): Story = Story.save(this)(session)

  def destroy()(implicit session: DBSession = Story.autoSession): Unit = Story.destroy(this)(session)

}


object Story extends SQLSyntaxSupport[Story] {

  override val tableName = "story"

  override val columns = Seq("id", "title", "text", "keywords", "status", "story_category_id", "rating_id", "created_by", "created_on", "modified_on")

  def apply(s: SyntaxProvider[Story])(rs: WrappedResultSet): Story = apply(s.resultName)(rs)
  def apply(s: ResultName[Story])(rs: WrappedResultSet): Story = new Story(
    id = rs.get(s.id),
    title = rs.get(s.title),
    text = rs.get(s.text),
    keywords = rs.get(s.keywords),
    status = rs.get(s.status),
    storyCategoryId = rs.get(s.storyCategoryId),
    ratingId = rs.get(s.ratingId),
    createdBy = rs.get(s.createdBy),
    createdOn = rs.get(s.createdOn),
    modifiedOn = rs.get(s.modifiedOn)
  )

  val s = Story.syntax("s")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Story] = {
    withSQL {
      select.from(Story as s).where.eq(s.id, id)
    }.map(Story(s.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Story] = {
    withSQL(select.from(Story as s)).map(Story(s.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Story as s)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Story] = {
    withSQL {
      select.from(Story as s).where.append(where)
    }.map(Story(s.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Story] = {
    withSQL {
      select.from(Story as s).where.append(where)
    }.map(Story(s.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Story as s).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: String,
    text: String,
    keywords: Option[String] = None,
    status: String,
    storyCategoryId: Int,
    ratingId: Int,
    createdBy: Int,
    createdOn: DateTime,
    modifiedOn: Option[DateTime] = None)(implicit session: DBSession = autoSession): Story = {
    val generatedKey = withSQL {
      insert.into(Story).columns(
        column.title,
        column.text,
        column.keywords,
        column.status,
        column.storyCategoryId,
        column.ratingId,
        column.createdBy,
        column.createdOn,
        column.modifiedOn
      ).values(
        title,
        text,
        keywords,
        status,
        storyCategoryId,
        ratingId,
        createdBy,
        createdOn,
        modifiedOn
      )
    }.updateAndReturnGeneratedKey.apply()

    Story(
      id = generatedKey.toInt,
      title = title,
      text = text,
      keywords = keywords,
      status = status,
      storyCategoryId = storyCategoryId,
      ratingId = ratingId,
      createdBy = createdBy,
      createdOn = createdOn,
      modifiedOn = modifiedOn)
  }

  def batchInsert(entities: Seq[Story])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'title -> entity.title,
        'text -> entity.text,
        'keywords -> entity.keywords,
        'status -> entity.status,
        'storyCategoryId -> entity.storyCategoryId,
        'ratingId -> entity.ratingId,
        'createdBy -> entity.createdBy,
        'createdOn -> entity.createdOn,
        'modifiedOn -> entity.modifiedOn))
        SQL("""insert into story(
        title,
        text,
        keywords,
        status,
        story_category_id,
        rating_id,
        created_by,
        created_on,
        modified_on
      ) values (
        {title},
        {text},
        {keywords},
        {status},
        {storyCategoryId},
        {ratingId},
        {createdBy},
        {createdOn},
        {modifiedOn}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: Story)(implicit session: DBSession = autoSession): Story = {
    withSQL {
      update(Story).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.text -> entity.text,
        column.keywords -> entity.keywords,
        column.status -> entity.status,
        column.storyCategoryId -> entity.storyCategoryId,
        column.ratingId -> entity.ratingId,
        column.createdBy -> entity.createdBy,
        column.createdOn -> entity.createdOn,
        column.modifiedOn -> entity.modifiedOn
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Story)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Story).where.eq(column.id, entity.id) }.update.apply()
  }
  
  
   def destroyStory(id: Long)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Story).where.eq(column.id, id) }.update.apply()
  }
   
     
  /**
   * Return a page of (Story).
   *
   * @param page Page to display
   * @param pageSize Number of stories per page
   * @param orderBy story property used for sorting
   * @param filter Filter applied on the name column
   */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%")(implicit session: DBSession = autoSession) : Page[(Story)] = {
    
    val offest = pageSize * page
    
   val stories = withSQL {
      select.from(Story as s).where.like(s.storyCategoryId,filter)
      .orderBy(s.createdOn)
      .limit(pageSize)
      .offset(offest)
    }.map(Story(s.resultName)).list.apply()
    
   
      val totalRows =  withSQL {
      select(sqls.count).from(Story as s).where.like(s.storyCategoryId,filter)
    }.map(_.long(1)).single.apply().get
      

      Page(stories, page, offest, totalRows)
      
    }
    
}
