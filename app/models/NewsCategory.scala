package models

import scalikejdbc._

case class NewsCategory(
  id: Int,
  title: String,
  description: String) {

  def save()(implicit session: DBSession = NewsCategory.autoSession): NewsCategory = NewsCategory.save(this)(session)

  def destroy()(implicit session: DBSession = NewsCategory.autoSession): Unit = NewsCategory.destroy(this)(session)

}


object NewsCategory extends SQLSyntaxSupport[NewsCategory] {

  override val tableName = "news_category"

  override val columns = Seq("id", "title", "description")

  def apply(nc: SyntaxProvider[NewsCategory])(rs: WrappedResultSet): NewsCategory = apply(nc.resultName)(rs)
  def apply(nc: ResultName[NewsCategory])(rs: WrappedResultSet): NewsCategory = new NewsCategory(
    id = rs.get(nc.id),
    title = rs.get(nc.title),
    description = rs.get(nc.description)
  )

  val nc = NewsCategory.syntax("nc")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[NewsCategory] = {
    withSQL {
      select.from(NewsCategory as nc).where.eq(nc.id, id)
    }.map(NewsCategory(nc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[NewsCategory] = {
    withSQL(select.from(NewsCategory as nc)).map(NewsCategory(nc.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(NewsCategory as nc)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[NewsCategory] = {
    withSQL {
      select.from(NewsCategory as nc).where.append(where)
    }.map(NewsCategory(nc.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[NewsCategory] = {
    withSQL {
      select.from(NewsCategory as nc).where.append(where)
    }.map(NewsCategory(nc.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(NewsCategory as nc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: String,
    description: String)(implicit session: DBSession = autoSession): NewsCategory = {
    val generatedKey = withSQL {
      insert.into(NewsCategory).columns(
        column.title,
        column.description
      ).values(
        title,
        description
      )
    }.updateAndReturnGeneratedKey.apply()

    NewsCategory(
      id = generatedKey.toInt,
      title = title,
      description = description)
  }

  def batchInsert(entities: Seq[NewsCategory])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'title -> entity.title,
        'description -> entity.description))
        SQL("""insert into news_category(
        title,
        description
      ) values (
        {title},
        {description}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: NewsCategory)(implicit session: DBSession = autoSession): NewsCategory = {
    withSQL {
      update(NewsCategory).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.description -> entity.description
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: NewsCategory)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(NewsCategory).where.eq(column.id, entity.id) }.update.apply()
  }

}
