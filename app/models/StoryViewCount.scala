package models

import scalikejdbc._

case class StoryViewCount(
  id: Int,
  storyId: Int,
  viewCount: Long) {

  def save()(implicit session: DBSession = StoryViewCount.autoSession): StoryViewCount = StoryViewCount.save(this)(session)

  def destroy()(implicit session: DBSession = StoryViewCount.autoSession): Unit = StoryViewCount.destroy(this)(session)

}


object StoryViewCount extends SQLSyntaxSupport[StoryViewCount] {

  override val tableName = "story_view_count"

  override val columns = Seq("id", "story_id", "view_count")

  def apply(svc: SyntaxProvider[StoryViewCount])(rs: WrappedResultSet): StoryViewCount = apply(svc.resultName)(rs)
  def apply(svc: ResultName[StoryViewCount])(rs: WrappedResultSet): StoryViewCount = new StoryViewCount(
    id = rs.get(svc.id),
    storyId = rs.get(svc.storyId),
    viewCount = rs.get(svc.viewCount)
  )

  val svc = StoryViewCount.syntax("svc")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[StoryViewCount] = {
    withSQL {
      select.from(StoryViewCount as svc).where.eq(svc.id, id)
    }.map(StoryViewCount(svc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[StoryViewCount] = {
    withSQL(select.from(StoryViewCount as svc)).map(StoryViewCount(svc.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(StoryViewCount as svc)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[StoryViewCount] = {
    withSQL {
      select.from(StoryViewCount as svc).where.append(where)
    }.map(StoryViewCount(svc.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[StoryViewCount] = {
    withSQL {
      select.from(StoryViewCount as svc).where.append(where)
    }.map(StoryViewCount(svc.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(StoryViewCount as svc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    storyId: Int,
    viewCount: Long)(implicit session: DBSession = autoSession): StoryViewCount = {
    val generatedKey = withSQL {
      insert.into(StoryViewCount).columns(
        column.storyId,
        column.viewCount
      ).values(
        storyId,
        viewCount
      )
    }.updateAndReturnGeneratedKey.apply()

    StoryViewCount(
      id = generatedKey.toInt,
      storyId = storyId,
      viewCount = viewCount)
  }

  def batchInsert(entities: Seq[StoryViewCount])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'storyId -> entity.storyId,
        'viewCount -> entity.viewCount))
        SQL("""insert into story_view_count(
        story_id,
        view_count
      ) values (
        {storyId},
        {viewCount}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: StoryViewCount)(implicit session: DBSession = autoSession): StoryViewCount = {
    withSQL {
      update(StoryViewCount).set(
        column.id -> entity.id,
        column.storyId -> entity.storyId,
        column.viewCount -> entity.viewCount
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: StoryViewCount)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(StoryViewCount).where.eq(column.id, entity.id) }.update.apply()
  }

}
