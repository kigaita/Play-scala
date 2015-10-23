package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class UserGroupSpec extends Specification {

  "UserGroup" should {

    val ug = UserGroup.syntax("ug")

    "find by primary keys" in new AutoRollback {
      val maybeFound = UserGroup.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = UserGroup.findBy(sqls.eq(ug.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = UserGroup.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = UserGroup.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = UserGroup.findAllBy(sqls.eq(ug.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = UserGroup.countBy(sqls.eq(ug.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = UserGroup.create(userId = 123, groupId = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = UserGroup.findAll().head
      // TODO modify something
      val modified = entity
      val updated = UserGroup.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = UserGroup.findAll().head
      UserGroup.destroy(entity)
      val shouldBeNone = UserGroup.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = UserGroup.findAll()
      entities.foreach(e => UserGroup.destroy(e))
      val batchInserted = UserGroup.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
