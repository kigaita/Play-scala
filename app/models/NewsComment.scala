package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class NewsComment(
  id: Int,
  text: String,
  parentId: Option[Int] = None,
  visible: Option[Boolean] = None,
  newsId: Int,
  createdBy: Int,
  createdOn: DateTime,
  modifiedOn: Option[DateTime] = None) {

  def save()(implicit session: DBSession = NewsComment.autoSession): NewsComment = NewsComment.save(this)(session)

  def destroy()(implicit session: DBSession = NewsComment.autoSession): Unit = NewsComment.destroy(this)(session)

}


object NewsComment extends SQLSyntaxSupport[NewsComment] {

  override val tableName = "news_comment"

  override val columns = Seq("id", "text", "parent_id", "visible", "news_id", "created_by", "created_on", "modified_on")

  def apply(nc: SyntaxProvider[NewsComment])(rs: WrappedResultSet): NewsComment = apply(nc.resultName)(rs)
  def apply(nc: ResultName[NewsComment])(rs: WrappedResultSet): NewsComment = new NewsComment(
    id = rs.get(nc.id),
    text = rs.get(nc.text),
    parentId = rs.get(nc.parentId),
    visible = rs.get(nc.visible),
    newsId = rs.get(nc.newsId),
    createdBy = rs.get(nc.createdBy),
    createdOn = rs.get(nc.createdOn),
    modifiedOn = rs.get(nc.modifiedOn)
  )

  val nc = NewsComment.syntax("nc")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[NewsComment] = {
    withSQL {
      select.from(NewsComment as nc).where.eq(nc.id, id)
    }.map(NewsComment(nc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[NewsComment] = {
    withSQL(select.from(NewsComment as nc)).map(NewsComment(nc.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(NewsComment as nc)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[NewsComment] = {
    withSQL {
      select.from(NewsComment as nc).where.append(where)
    }.map(NewsComment(nc.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[NewsComment] = {
    withSQL {
      select.from(NewsComment as nc).where.append(where)
    }.map(NewsComment(nc.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(NewsComment as nc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    text: String,
    parentId: Option[Int] = None,
    visible: Option[Boolean] = None,
    newsId: Int,
    createdBy: Int,
    createdOn: DateTime,
    modifiedOn: Option[DateTime] = None)(implicit session: DBSession = autoSession): NewsComment = {
    val generatedKey = withSQL {
      insert.into(NewsComment).columns(
        column.text,
        column.parentId,
        column.visible,
        column.newsId,
        column.createdBy,
        column.createdOn,
        column.modifiedOn
      ).values(
        text,
        parentId,
        visible,
        newsId,
        createdBy,
        createdOn,
        modifiedOn
      )
    }.updateAndReturnGeneratedKey.apply()

    NewsComment(
      id = generatedKey.toInt,
      text = text,
      parentId = parentId,
      visible = visible,
      newsId = newsId,
      createdBy = createdBy,
      createdOn = createdOn,
      modifiedOn = modifiedOn)
  }

  def batchInsert(entities: Seq[NewsComment])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'text -> entity.text,
        'parentId -> entity.parentId,
        'visible -> entity.visible,
        'newsId -> entity.newsId,
        'createdBy -> entity.createdBy,
        'createdOn -> entity.createdOn,
        'modifiedOn -> entity.modifiedOn))
        SQL("""insert into news_comment(
        text,
        parent_id,
        visible,
        news_id,
        created_by,
        created_on,
        modified_on
      ) values (
        {text},
        {parentId},
        {visible},
        {newsId},
        {createdBy},
        {createdOn},
        {modifiedOn}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: NewsComment)(implicit session: DBSession = autoSession): NewsComment = {
    withSQL {
      update(NewsComment).set(
        column.id -> entity.id,
        column.text -> entity.text,
        column.parentId -> entity.parentId,
        column.visible -> entity.visible,
        column.newsId -> entity.newsId,
        column.createdBy -> entity.createdBy,
        column.createdOn -> entity.createdOn,
        column.modifiedOn -> entity.modifiedOn
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: NewsComment)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(NewsComment).where.eq(column.id, entity.id) }.update.apply()
  }

}
