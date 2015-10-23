package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class StoryViewCountSpec extends Specification {

  "StoryViewCount" should {

    val svc = StoryViewCount.syntax("svc")

    "find by primary keys" in new AutoRollback {
      val maybeFound = StoryViewCount.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = StoryViewCount.findBy(sqls.eq(svc.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = StoryViewCount.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = StoryViewCount.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = StoryViewCount.findAllBy(sqls.eq(svc.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = StoryViewCount.countBy(sqls.eq(svc.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = StoryViewCount.create(storyId = 123, viewCount = 1L)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = StoryViewCount.findAll().head
      // TODO modify something
      val modified = entity
      val updated = StoryViewCount.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = StoryViewCount.findAll().head
      StoryViewCount.destroy(entity)
      val shouldBeNone = StoryViewCount.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = StoryViewCount.findAll()
      entities.foreach(e => StoryViewCount.destroy(e))
      val batchInserted = StoryViewCount.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
