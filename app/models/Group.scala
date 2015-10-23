package models

import scalikejdbc._

case class Group(
  id: Int,
  name: String,
  description: String) {

  def save()(implicit session: DBSession = Group.autoSession): Group = Group.save(this)(session)

  def destroy()(implicit session: DBSession = Group.autoSession): Unit = Group.destroy(this)(session)

}


object Group extends SQLSyntaxSupport[Group] {

  override val tableName = "group"

  override val columns = Seq("id", "name", "description")

  def apply(g: SyntaxProvider[Group])(rs: WrappedResultSet): Group = apply(g.resultName)(rs)
  def apply(g: ResultName[Group])(rs: WrappedResultSet): Group = new Group(
    id = rs.get(g.id),
    name = rs.get(g.name),
    description = rs.get(g.description)
  )

  val g = Group.syntax("g")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Group] = {
    withSQL {
      select.from(Group as g).where.eq(g.id, id)
    }.map(Group(g.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Group] = {
    withSQL(select.from(Group as g)).map(Group(g.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Group as g)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Group] = {
    withSQL {
      select.from(Group as g).where.append(where)
    }.map(Group(g.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Group] = {
    withSQL {
      select.from(Group as g).where.append(where)
    }.map(Group(g.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Group as g).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String,
    description: String)(implicit session: DBSession = autoSession): Group = {
    val generatedKey = withSQL {
      insert.into(Group).columns(
        column.name,
        column.description
      ).values(
        name,
        description
      )
    }.updateAndReturnGeneratedKey.apply()

    Group(
      id = generatedKey.toInt,
      name = name,
      description = description)
  }

  def batchInsert(entities: Seq[Group])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'name -> entity.name,
        'description -> entity.description))
        SQL("""insert into group(
        name,
        description
      ) values (
        {name},
        {description}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: Group)(implicit session: DBSession = autoSession): Group = {
    withSQL {
      update(Group).set(
        column.id -> entity.id,
        column.name -> entity.name,
        column.description -> entity.description
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Group)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Group).where.eq(column.id, entity.id) }.update.apply()
  }

}
