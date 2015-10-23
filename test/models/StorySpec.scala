package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class StorySpec extends Specification {

  "Story" should {

    val s = Story.syntax("s")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Story.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Story.findBy(sqls.eq(s.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Story.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Story.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Story.findAllBy(sqls.eq(s.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Story.countBy(sqls.eq(s.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Story.create(title = "MyString", text = "MyString", keywords = "MyString", status = false, banned = false, storyCategoryId = 123, ratingId = 123, createdBy = 123, createdOn = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Story.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Story.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Story.findAll().head
      Story.destroy(entity)
      val shouldBeNone = Story.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Story.findAll()
      entities.foreach(e => Story.destroy(e))
      val batchInserted = Story.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
