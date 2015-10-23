package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class StoryComment(
  id: Int,
  text: String,
  parentId: Option[Int] = None,
  visible: Option[Boolean] = None,
  storyId: Int,
  createdBy: Int,
  createdOn: DateTime,
  modifiedOn: Option[DateTime] = None) {

  def save()(implicit session: DBSession = StoryComment.autoSession): StoryComment = StoryComment.save(this)(session)

  def destroy()(implicit session: DBSession = StoryComment.autoSession): Unit = StoryComment.destroy(this)(session)

}


object StoryComment extends SQLSyntaxSupport[StoryComment] {

  override val tableName = "story_comment"

  override val columns = Seq("id", "text", "parent_id", "visible", "story_id", "created_by", "created_on", "modified_on")

  def apply(sc: SyntaxProvider[StoryComment])(rs: WrappedResultSet): StoryComment = apply(sc.resultName)(rs)
  def apply(sc: ResultName[StoryComment])(rs: WrappedResultSet): StoryComment = new StoryComment(
    id = rs.get(sc.id),
    text = rs.get(sc.text),
    parentId = rs.get(sc.parentId),
    visible = rs.get(sc.visible),
    storyId = rs.get(sc.storyId),
    createdBy = rs.get(sc.createdBy),
    createdOn = rs.get(sc.createdOn),
    modifiedOn = rs.get(sc.modifiedOn)
  )

  val sc = StoryComment.syntax("sc")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[StoryComment] = {
    withSQL {
      select.from(StoryComment as sc).where.eq(sc.id, id)
    }.map(StoryComment(sc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[StoryComment] = {
    withSQL(select.from(StoryComment as sc)).map(StoryComment(sc.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(StoryComment as sc)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[StoryComment] = {
    withSQL {
      select.from(StoryComment as sc).where.append(where)
    }.map(StoryComment(sc.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[StoryComment] = {
    withSQL {
      select.from(StoryComment as sc).where.append(where)
    }.map(StoryComment(sc.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(StoryComment as sc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    text: String,
    parentId: Option[Int] = None,
    visible: Option[Boolean] = None,
    storyId: Int,
    createdBy: Int,
    createdOn: DateTime,
    modifiedOn: Option[DateTime] = None)(implicit session: DBSession = autoSession): StoryComment = {
    val generatedKey = withSQL {
      insert.into(StoryComment).columns(
        column.text,
        column.parentId,
        column.visible,
        column.storyId,
        column.createdBy,
        column.createdOn,
        column.modifiedOn
      ).values(
        text,
        parentId,
        visible,
        storyId,
        createdBy,
        createdOn,
        modifiedOn
      )
    }.updateAndReturnGeneratedKey.apply()

    StoryComment(
      id = generatedKey.toInt,
      text = text,
      parentId = parentId,
      visible = visible,
      storyId = storyId,
      createdBy = createdBy,
      createdOn = createdOn,
      modifiedOn = modifiedOn)
  }

  def batchInsert(entities: Seq[StoryComment])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'text -> entity.text,
        'parentId -> entity.parentId,
        'visible -> entity.visible,
        'storyId -> entity.storyId,
        'createdBy -> entity.createdBy,
        'createdOn -> entity.createdOn,
        'modifiedOn -> entity.modifiedOn))
        SQL("""insert into story_comment(
        text,
        parent_id,
        visible,
        story_id,
        created_by,
        created_on,
        modified_on
      ) values (
        {text},
        {parentId},
        {visible},
        {storyId},
        {createdBy},
        {createdOn},
        {modifiedOn}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: StoryComment)(implicit session: DBSession = autoSession): StoryComment = {
    withSQL {
      update(StoryComment).set(
        column.id -> entity.id,
        column.text -> entity.text,
        column.parentId -> entity.parentId,
        column.visible -> entity.visible,
        column.storyId -> entity.storyId,
        column.createdBy -> entity.createdBy,
        column.createdOn -> entity.createdOn,
        column.modifiedOn -> entity.modifiedOn
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: StoryComment)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(StoryComment).where.eq(column.id, entity.id) }.update.apply()
  }

}
