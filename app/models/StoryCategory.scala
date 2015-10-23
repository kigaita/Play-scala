package models

import scalikejdbc._

case class StoryCategory(
  id: Int,
  title: String,
  description: String) {

  def save()(implicit session: DBSession = StoryCategory.autoSession): StoryCategory = StoryCategory.save(this)(session)

  def destroy()(implicit session: DBSession = StoryCategory.autoSession): Unit = StoryCategory.destroy(this)(session)

}


object StoryCategory extends SQLSyntaxSupport[StoryCategory] {

  override val tableName = "story_category"

  override val columns = Seq("id", "title", "description")

  def apply(sc: SyntaxProvider[StoryCategory])(rs: WrappedResultSet): StoryCategory = apply(sc.resultName)(rs)
  def apply(sc: ResultName[StoryCategory])(rs: WrappedResultSet): StoryCategory = new StoryCategory(
    id = rs.get(sc.id),
    title = rs.get(sc.title),
    description = rs.get(sc.description)
  )

  val sc = StoryCategory.syntax("sc")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[StoryCategory] = {
    withSQL {
      select.from(StoryCategory as sc).where.eq(sc.id, id)
    }.map(StoryCategory(sc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[StoryCategory] = {
    withSQL(select.from(StoryCategory as sc)).map(StoryCategory(sc.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(StoryCategory as sc)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[StoryCategory] = {
    withSQL {
      select.from(StoryCategory as sc).where.append(where)
    }.map(StoryCategory(sc.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[StoryCategory] = {
    withSQL {
      select.from(StoryCategory as sc).where.append(where)
    }.map(StoryCategory(sc.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(StoryCategory as sc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: String,
    description: String)(implicit session: DBSession = autoSession): StoryCategory = {
    val generatedKey = withSQL {
      insert.into(StoryCategory).columns(
        column.title,
        column.description
      ).values(
        title,
        description
      )
    }.updateAndReturnGeneratedKey.apply()

    StoryCategory(
      id = generatedKey.toInt,
      title = title,
      description = description)
  }

  def batchInsert(entities: Seq[StoryCategory])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'title -> entity.title,
        'description -> entity.description))
        SQL("""insert into story_category(
        title,
        description
      ) values (
        {title},
        {description}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: StoryCategory)(implicit session: DBSession = autoSession): StoryCategory = {
    withSQL {
      update(StoryCategory).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.description -> entity.description
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: StoryCategory)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(StoryCategory).where.eq(column.id, entity.id) }.update.apply()
  }

}
