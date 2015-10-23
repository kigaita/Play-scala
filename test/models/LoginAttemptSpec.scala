package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class LoginAttemptSpec extends Specification {

  "LoginAttempt" should {

    val la = LoginAttempt.syntax("la")

    "find by primary keys" in new AutoRollback {
      val maybeFound = LoginAttempt.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = LoginAttempt.findBy(sqls.eq(la.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = LoginAttempt.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = LoginAttempt.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = LoginAttempt.findAllBy(sqls.eq(la.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = LoginAttempt.countBy(sqls.eq(la.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = LoginAttempt.create(ipAddress = "MyString", login = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = LoginAttempt.findAll().head
      // TODO modify something
      val modified = entity
      val updated = LoginAttempt.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = LoginAttempt.findAll().head
      LoginAttempt.destroy(entity)
      val shouldBeNone = LoginAttempt.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = LoginAttempt.findAll()
      entities.foreach(e => LoginAttempt.destroy(e))
      val batchInserted = LoginAttempt.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
