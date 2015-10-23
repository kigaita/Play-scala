package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class LoginAttempt(
  id: Int,
  ipAddress: String,
  login: String,
  time: Option[DateTime] = None) {

  def save()(implicit session: DBSession = LoginAttempt.autoSession): LoginAttempt = LoginAttempt.save(this)(session)

  def destroy()(implicit session: DBSession = LoginAttempt.autoSession): Unit = LoginAttempt.destroy(this)(session)

}


object LoginAttempt extends SQLSyntaxSupport[LoginAttempt] {

  override val tableName = "login_attempt"

  override val columns = Seq("id", "ip_address", "login", "time")

  def apply(la: SyntaxProvider[LoginAttempt])(rs: WrappedResultSet): LoginAttempt = apply(la.resultName)(rs)
  def apply(la: ResultName[LoginAttempt])(rs: WrappedResultSet): LoginAttempt = new LoginAttempt(
    id = rs.get(la.id),
    ipAddress = rs.get(la.ipAddress),
    login = rs.get(la.login),
    time = rs.get(la.time)
  )

  val la = LoginAttempt.syntax("la")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[LoginAttempt] = {
    withSQL {
      select.from(LoginAttempt as la).where.eq(la.id, id)
    }.map(LoginAttempt(la.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[LoginAttempt] = {
    withSQL(select.from(LoginAttempt as la)).map(LoginAttempt(la.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(LoginAttempt as la)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[LoginAttempt] = {
    withSQL {
      select.from(LoginAttempt as la).where.append(where)
    }.map(LoginAttempt(la.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[LoginAttempt] = {
    withSQL {
      select.from(LoginAttempt as la).where.append(where)
    }.map(LoginAttempt(la.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(LoginAttempt as la).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    ipAddress: String,
    login: String,
    time: Option[DateTime] = None)(implicit session: DBSession = autoSession): LoginAttempt = {
    val generatedKey = withSQL {
      insert.into(LoginAttempt).columns(
        column.ipAddress,
        column.login,
        column.time
      ).values(
        ipAddress,
        login,
        time
      )
    }.updateAndReturnGeneratedKey.apply()

    LoginAttempt(
      id = generatedKey.toInt,
      ipAddress = ipAddress,
      login = login,
      time = time)
  }

  def batchInsert(entities: Seq[LoginAttempt])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'ipAddress -> entity.ipAddress,
        'login -> entity.login,
        'time -> entity.time))
        SQL("""insert into login_attempt(
        ip_address,
        login,
        time
      ) values (
        {ipAddress},
        {login},
        {time}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: LoginAttempt)(implicit session: DBSession = autoSession): LoginAttempt = {
    withSQL {
      update(LoginAttempt).set(
        column.id -> entity.id,
        column.ipAddress -> entity.ipAddress,
        column.login -> entity.login,
        column.time -> entity.time
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: LoginAttempt)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(LoginAttempt).where.eq(column.id, entity.id) }.update.apply()
  }

}
