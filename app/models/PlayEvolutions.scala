package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class PlayEvolutions(
  id: Int,
  hash: String,
  appliedAt: DateTime,
  applyScript: Option[String] = None,
  revertScript: Option[String] = None,
  state: Option[String] = None,
  lastProblem: Option[String] = None) {

  def save()(implicit session: DBSession = PlayEvolutions.autoSession): PlayEvolutions = PlayEvolutions.save(this)(session)

  def destroy()(implicit session: DBSession = PlayEvolutions.autoSession): Unit = PlayEvolutions.destroy(this)(session)

}


object PlayEvolutions extends SQLSyntaxSupport[PlayEvolutions] {

  override val tableName = "play_evolutions"

  override val columns = Seq("id", "hash", "applied_at", "apply_script", "revert_script", "state", "last_problem")

  def apply(pe: SyntaxProvider[PlayEvolutions])(rs: WrappedResultSet): PlayEvolutions = apply(pe.resultName)(rs)
  def apply(pe: ResultName[PlayEvolutions])(rs: WrappedResultSet): PlayEvolutions = new PlayEvolutions(
    id = rs.get(pe.id),
    hash = rs.get(pe.hash),
    appliedAt = rs.get(pe.appliedAt),
    applyScript = rs.get(pe.applyScript),
    revertScript = rs.get(pe.revertScript),
    state = rs.get(pe.state),
    lastProblem = rs.get(pe.lastProblem)
  )

  val pe = PlayEvolutions.syntax("pe")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[PlayEvolutions] = {
    withSQL {
      select.from(PlayEvolutions as pe).where.eq(pe.id, id)
    }.map(PlayEvolutions(pe.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[PlayEvolutions] = {
    withSQL(select.from(PlayEvolutions as pe)).map(PlayEvolutions(pe.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(PlayEvolutions as pe)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[PlayEvolutions] = {
    withSQL {
      select.from(PlayEvolutions as pe).where.append(where)
    }.map(PlayEvolutions(pe.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[PlayEvolutions] = {
    withSQL {
      select.from(PlayEvolutions as pe).where.append(where)
    }.map(PlayEvolutions(pe.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(PlayEvolutions as pe).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    id: Int,
    hash: String,
    appliedAt: DateTime,
    applyScript: Option[String] = None,
    revertScript: Option[String] = None,
    state: Option[String] = None,
    lastProblem: Option[String] = None)(implicit session: DBSession = autoSession): PlayEvolutions = {
    withSQL {
      insert.into(PlayEvolutions).columns(
        column.id,
        column.hash,
        column.appliedAt,
        column.applyScript,
        column.revertScript,
        column.state,
        column.lastProblem
      ).values(
        id,
        hash,
        appliedAt,
        applyScript,
        revertScript,
        state,
        lastProblem
      )
    }.update.apply()

    PlayEvolutions(
      id = id,
      hash = hash,
      appliedAt = appliedAt,
      applyScript = applyScript,
      revertScript = revertScript,
      state = state,
      lastProblem = lastProblem)
  }

  def batchInsert(entities: Seq[PlayEvolutions])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'id -> entity.id,
        'hash -> entity.hash,
        'appliedAt -> entity.appliedAt,
        'applyScript -> entity.applyScript,
        'revertScript -> entity.revertScript,
        'state -> entity.state,
        'lastProblem -> entity.lastProblem))
        SQL("""insert into play_evolutions(
        id,
        hash,
        applied_at,
        apply_script,
        revert_script,
        state,
        last_problem
      ) values (
        {id},
        {hash},
        {appliedAt},
        {applyScript},
        {revertScript},
        {state},
        {lastProblem}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: PlayEvolutions)(implicit session: DBSession = autoSession): PlayEvolutions = {
    withSQL {
      update(PlayEvolutions).set(
        column.id -> entity.id,
        column.hash -> entity.hash,
        column.appliedAt -> entity.appliedAt,
        column.applyScript -> entity.applyScript,
        column.revertScript -> entity.revertScript,
        column.state -> entity.state,
        column.lastProblem -> entity.lastProblem
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: PlayEvolutions)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(PlayEvolutions).where.eq(column.id, entity.id) }.update.apply()
  }

}
