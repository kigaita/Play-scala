package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class StoryCategorySpec extends Specification {

  "StoryCategory" should {

    val sc = StoryCategory.syntax("sc")

    "find by primary keys" in new AutoRollback {
      val maybeFound = StoryCategory.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = StoryCategory.findBy(sqls.eq(sc.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = StoryCategory.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = StoryCategory.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = StoryCategory.findAllBy(sqls.eq(sc.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = StoryCategory.countBy(sqls.eq(sc.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = StoryCategory.create(title = "MyString", description = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = StoryCategory.findAll().head
      // TODO modify something
      val modified = entity
      val updated = StoryCategory.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = StoryCategory.findAll().head
      StoryCategory.destroy(entity)
      val shouldBeNone = StoryCategory.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = StoryCategory.findAll()
      entities.foreach(e => StoryCategory.destroy(e))
      val batchInserted = StoryCategory.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
