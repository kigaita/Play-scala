package models

import scalikejdbc._

case class Rating(
  id: Int,
  classification: String,
  description: String) {

  def save()(implicit session: DBSession = Rating.autoSession): Rating = Rating.save(this)(session)

  def destroy()(implicit session: DBSession = Rating.autoSession): Unit = Rating.destroy(this)(session)

}


object Rating extends SQLSyntaxSupport[Rating] {

  override val tableName = "rating"

  override val columns = Seq("id", "classification", "description")

  def apply(r: SyntaxProvider[Rating])(rs: WrappedResultSet): Rating = apply(r.resultName)(rs)
  def apply(r: ResultName[Rating])(rs: WrappedResultSet): Rating = new Rating(
    id = rs.get(r.id),
    classification = rs.get(r.classification),
    description = rs.get(r.description)
  )

  val r = Rating.syntax("r")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Rating] = {
    withSQL {
      select.from(Rating as r).where.eq(r.id, id)
    }.map(Rating(r.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Rating] = {
    withSQL(select.from(Rating as r)).map(Rating(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Rating as r)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Rating] = {
    withSQL {
      select.from(Rating as r).where.append(where)
    }.map(Rating(r.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Rating] = {
    withSQL {
      select.from(Rating as r).where.append(where)
    }.map(Rating(r.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Rating as r).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    classification: String,
    description: String)(implicit session: DBSession = autoSession): Rating = {
    val generatedKey = withSQL {
      insert.into(Rating).columns(
        column.classification,
        column.description
      ).values(
        classification,
        description
      )
    }.updateAndReturnGeneratedKey.apply()

    Rating(
      id = generatedKey.toInt,
      classification = classification,
      description = description)
  }

  def batchInsert(entities: Seq[Rating])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'classification -> entity.classification,
        'description -> entity.description))
        SQL("""insert into rating(
        classification,
        description
      ) values (
        {classification},
        {description}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: Rating)(implicit session: DBSession = autoSession): Rating = {
    withSQL {
      update(Rating).set(
        column.id -> entity.id,
        column.classification -> entity.classification,
        column.description -> entity.description
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Rating)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Rating).where.eq(column.id, entity.id) }.update.apply()
  }

}
