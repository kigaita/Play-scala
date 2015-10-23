package models

import scalikejdbc._
import org.joda.time.{DateTime}
import utils.Page

 case class SignupData(email: String,  password: String, passwordRepeat: String, firstName: String, lastName: String)

case class User(
  id: Int,
  ipAddress: String,
  username: String,
  password: String,
  salt: Option[String] = None,
  email: String,
   permission: Permission,
  activationCode: Option[String] = None,
  activationTime: Option[DateTime] = None,
  forgottenPasswordCode: Option[String] = None,
  forgottenPasswordTime: Option[DateTime] = None,
  rememberCode: Option[String] = None,
  createdOn: DateTime,
  lastLogin: Option[DateTime] = None,
  active: Option[Boolean] = None,
  firstName: Option[String] = None,
  lastName: Option[String] = None,
  company: Option[String] = None,
  phone: Option[String] = None) {

  def save()(implicit session: DBSession = User.autoSession): User = User.save(this)(session)

  def destroy()(implicit session: DBSession = User.autoSession): Unit = User.destroy(this)(session)

}


object User extends SQLSyntaxSupport[User] {

  override val tableName = "user"

  override val columns = Seq("id", "ip_address", "username", "password", "salt", "email", "permission", "activation_code", "activation_time", "forgotten_password_code", "forgotten_password_time", "remember_code", "created_on", "last_login", "active", "first_name", "last_name", "company", "phone")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = new User(
    id = rs.get(u.id),
    ipAddress = rs.get(u.ipAddress),
    username = rs.get(u.username),
    password = rs.get(u.password),
    salt = rs.get(u.salt),
    email = rs.get(u.email),
    permission = Permission.fromString(rs.get(u.permission)).get,
    activationCode = rs.get(u.activationCode),
    activationTime = rs.get(u.activationTime),
    forgottenPasswordCode = rs.get(u.forgottenPasswordCode),
    forgottenPasswordTime = rs.get(u.forgottenPasswordTime),
    rememberCode = rs.get(u.rememberCode),
    createdOn = rs.get(u.createdOn),
    lastLogin = rs.get(u.lastLogin),
    active = rs.get(u.active),
    firstName = rs.get(u.firstName),
    lastName = rs.get(u.lastName),
    company = rs.get(u.company),
    phone = rs.get(u.phone)
  )

  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.id, id)
    }.map(User(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[User] = {
    withSQL(select.from(User as u)).map(User(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(User as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).list.apply()
  }
  
  def authenticate(email : String, password : String) (implicit session: DBSession = autoSession): Option[User]  ={
     withSQL {
      select.from(User as u).where.eq(u.email, email).and.eq(u.password, password)
    }.map(User(u.resultName)).single.apply()
  }
  
  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(User as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    ipAddress: String,
    username: String,
    password: String,
    salt: Option[String] = None,
    email: String,
    permission : Permission,
    activationCode: Option[String] = None,
    activationTime: Option[DateTime] = None,
    forgottenPasswordCode: Option[String] = None,
    forgottenPasswordTime: Option[DateTime] = None,
    rememberCode: Option[String] = None,
    createdOn: DateTime,
    lastLogin: Option[DateTime] = None,
    active: Option[Boolean] = None,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    company: Option[String] = None,
    phone: Option[String] = None)(implicit session: DBSession = autoSession): User = {
    val generatedKey = withSQL {
      insert.into(User).columns(
        column.ipAddress,
        column.username,
        column.password,
        column.salt,
        column.email,
        column.permission,
        column.activationCode,
        column.activationTime,
        column.forgottenPasswordCode,
        column.forgottenPasswordTime,
        column.rememberCode,
        column.createdOn,
        column.lastLogin,
        column.active,
        column.firstName,
        column.lastName,
        column.company,
        column.phone
      ).values(
        ipAddress,
        username,
        password,
        salt,
        email,
        permission.toString,
        activationCode,
        activationTime,
        forgottenPasswordCode,
        forgottenPasswordTime,
        rememberCode,
        createdOn,
        lastLogin,
        active,
        firstName,
        lastName,
        company,
        phone
      )
    }.updateAndReturnGeneratedKey.apply()

    User(
      id = generatedKey.toInt,
      ipAddress = ipAddress,
      username = username,
      password = password,
      salt = salt,
      email = email,
      permission = permission,
      activationCode = activationCode,
      activationTime = activationTime,
      forgottenPasswordCode = forgottenPasswordCode,
      forgottenPasswordTime = forgottenPasswordTime,
      rememberCode = rememberCode,
      createdOn = createdOn,
      lastLogin = lastLogin,
      active = active,
      firstName = firstName,
      lastName = lastName,
      company = company,
      phone = phone)
  }

  def batchInsert(entities: Seq[User])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity => 
      Seq(
        'ipAddress -> entity.ipAddress,
        'username -> entity.username,
        'password -> entity.password,
        'salt -> entity.salt,
        'email -> entity.email,
        'permission -> entity.permission.toString,
        'activationCode -> entity.activationCode,
        'activationTime -> entity.activationTime,
        'forgottenPasswordCode -> entity.forgottenPasswordCode,
        'forgottenPasswordTime -> entity.forgottenPasswordTime,
        'rememberCode -> entity.rememberCode,
        'createdOn -> entity.createdOn,
        'lastLogin -> entity.lastLogin,
        'active -> entity.active,
        'firstName -> entity.firstName,
        'lastName -> entity.lastName,
        'company -> entity.company,
        'phone -> entity.phone))
        SQL("""insert into user(
        ip_address,
        username,
        password,
        salt,
        email,
        permission,
        activation_code,
        activation_time,
        forgotten_password_code,
        forgotten_password_time,
        remember_code,
        created_on,
        last_login,
        active,
        first_name,
        last_name,
        company,
        phone
      ) values (
        {ipAddress},
        {username},
        {password},
        {salt},
        {email},
        {permission},
        {activationCode},
        {activationTime},
        {forgottenPasswordCode},
        {forgottenPasswordTime},
        {rememberCode},
        {createdOn},
        {lastLogin},
        {active},
        {firstName},
        {lastName},
        {company},
        {phone}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: User)(implicit session: DBSession = autoSession): User = {
    withSQL {
      update(User).set(
        column.id -> entity.id,
        column.ipAddress -> entity.ipAddress,
        column.username -> entity.username,
        column.password -> entity.password,
        column.salt -> entity.salt,
        column.email -> entity.email,
        column.permission -> entity.permission.toString,
        column.activationCode -> entity.activationCode,
        column.activationTime -> entity.activationTime,
        column.forgottenPasswordCode -> entity.forgottenPasswordCode,
        column.forgottenPasswordTime -> entity.forgottenPasswordTime,
        column.rememberCode -> entity.rememberCode,
        column.createdOn -> entity.createdOn,
        column.lastLogin -> entity.lastLogin,
        column.active -> entity.active,
        column.firstName -> entity.firstName,
        column.lastName -> entity.lastName,
        column.company -> entity.company,
        column.phone -> entity.phone
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: User)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(User).where.eq(column.id, entity.id) }.update.apply()
  }
  
   def destroyUser(id: Long)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(User).where.eq(column.id, id) }.update.apply()
  }
  
  /**
   * Return a page of (User).
   *
   * @param page Page to display
   * @param pageSize Number of users per page
   * @param orderBy user property used for sorting
   * @param filter Filter applied on the name column
   */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%")(implicit session: DBSession = autoSession) : Page[(User)] = {
    
    val offest = pageSize * page
    
   val users = withSQL {
      select.from(User as u).where.like(u.email,filter)
      .orderBy(u.email)
      .limit(pageSize)
      .offset(offest)
    }.map(User(u.resultName)).list.apply()
    
   
      val totalRows =  withSQL {
      select(sqls.count).from(User as u).where.like(u.email,filter)
    }.map(_.long(1)).single.apply().get
      

      Page(users, page, offest, totalRows)
      
    }
    
  
  
//  implicit val rowToPermission: Column[Permission] = {
//    Column.nonNull[Permission] { (value, meta) =>
//      value match {
//        case 1 => Right(Administrator)
//        case 2 => Right(NormalUser)
//        case _ => Left(TypeDoesNotMatch(
//          "Cannot convert %s : %s to Permission for column %s".format(value, value.getClass, meta.column)))
//      }
//    }
//  }
//  
//  object Clob {
//    def unapply(clob: Clob): Option[String] = Some(clob.getSubString(1, clob.length.toInt))
//  }
}
