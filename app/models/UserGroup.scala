package models

import scalikejdbc._

case class UserGroup(
  id: Int,
  userId: Int,
  groupId: Int) {

  def save()(implicit session: DBSession = UserGroup.autoSession): UserGroup = UserGroup.save(this)(session)

  def destroy()(implicit session: DBSession = UserGroup.autoSession): Unit = UserGroup.destroy(this)(session)

}


object UserGroup extends SQLSyntaxSupport[UserGroup] {

  override val tableName = "user_group"

  override val columns = Seq("id", "user_id", "group_id")

  def apply(ug: SyntaxProvider[UserGroup])(rs: WrappedResultSet): UserGroup = apply(ug.resultName)(rs)
  def apply(ug: ResultName[UserGroup])(rs: WrappedResultSet): UserGroup = new UserGroup(
    id = rs.get(ug.id),
    userId = rs.get(ug.userId),
    groupId = rs.get(ug.groupId)
  )

  val ug = UserGroup.syntax("ug")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[UserGroup] = {
    withSQL {
      select.from(UserGroup as ug).where.eq(ug.id, id)
    }.map(UserGroup(ug.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[UserGroup] = {
    withSQL(select.from(UserGroup as ug)).map(UserGroup(ug.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(UserGroup as ug)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[UserGroup] = {
    withSQL {
      select.from(UserGroup as ug).where.append(where)
    }.map(UserGroup(ug.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[UserGroup] = {
    withSQL {
      select.from(UserGroup as ug).where.append(where)
    }.map(UserGroup(ug.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(UserGroup as ug).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userId: Int,
    groupId: Int)(implicit session: DBSession = autoSession): UserGroup = {
    val generatedKey = withSQL {
      insert.into(UserGroup).columns(
        column.userId,
        column.groupId
      ).values(
        userId,
        groupId
      )
    }.updateAndReturnGeneratedKey.apply()

    UserGroup(
      id = generatedKey.toInt,
      userId = userId,
      groupId = groupId)
  }

  def batchInsert(entities: Seq[UserGroup])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'userId -> entity.userId,
        'groupId -> entity.groupId))
        SQL("""insert into user_group(
        user_id,
        group_id
      ) values (
        {userId},
        {groupId}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: UserGroup)(implicit session: DBSession = autoSession): UserGroup = {
    withSQL {
      update(UserGroup).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.groupId -> entity.groupId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: UserGroup)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(UserGroup).where.eq(column.id, entity.id) }.update.apply()
  }

}
