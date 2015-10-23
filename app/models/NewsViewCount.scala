package models

import scalikejdbc._

case class NewsViewCount(
  id: Int,
  newsId: Int,
  viewCount: Long) {

  def save()(implicit session: DBSession = NewsViewCount.autoSession): NewsViewCount = NewsViewCount.save(this)(session)

  def destroy()(implicit session: DBSession = NewsViewCount.autoSession): Unit = NewsViewCount.destroy(this)(session)

}


object NewsViewCount extends SQLSyntaxSupport[NewsViewCount] {

  override val tableName = "news_view_count"

  override val columns = Seq("id", "news_id", "view_count")

  def apply(nvc: SyntaxProvider[NewsViewCount])(rs: WrappedResultSet): NewsViewCount = apply(nvc.resultName)(rs)
  def apply(nvc: ResultName[NewsViewCount])(rs: WrappedResultSet): NewsViewCount = new NewsViewCount(
    id = rs.get(nvc.id),
    newsId = rs.get(nvc.newsId),
    viewCount = rs.get(nvc.viewCount)
  )

  val nvc = NewsViewCount.syntax("nvc")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[NewsViewCount] = {
    withSQL {
      select.from(NewsViewCount as nvc).where.eq(nvc.id, id)
    }.map(NewsViewCount(nvc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[NewsViewCount] = {
    withSQL(select.from(NewsViewCount as nvc)).map(NewsViewCount(nvc.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(NewsViewCount as nvc)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[NewsViewCount] = {
    withSQL {
      select.from(NewsViewCount as nvc).where.append(where)
    }.map(NewsViewCount(nvc.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[NewsViewCount] = {
    withSQL {
      select.from(NewsViewCount as nvc).where.append(where)
    }.map(NewsViewCount(nvc.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(NewsViewCount as nvc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    newsId: Int,
    viewCount: Long)(implicit session: DBSession = autoSession): NewsViewCount = {
    val generatedKey = withSQL {
      insert.into(NewsViewCount).columns(
        column.newsId,
        column.viewCount
      ).values(
        newsId,
        viewCount
      )
    }.updateAndReturnGeneratedKey.apply()

    NewsViewCount(
      id = generatedKey.toInt,
      newsId = newsId,
      viewCount = viewCount)
  }

  def batchInsert(entities: Seq[NewsViewCount])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'newsId -> entity.newsId,
        'viewCount -> entity.viewCount))
        SQL("""insert into news_view_count(
        news_id,
        view_count
      ) values (
        {newsId},
        {viewCount}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: NewsViewCount)(implicit session: DBSession = autoSession): NewsViewCount = {
    withSQL {
      update(NewsViewCount).set(
        column.id -> entity.id,
        column.newsId -> entity.newsId,
        column.viewCount -> entity.viewCount
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: NewsViewCount)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(NewsViewCount).where.eq(column.id, entity.id) }.update.apply()
  }

}
