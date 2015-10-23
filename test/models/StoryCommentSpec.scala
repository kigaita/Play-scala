package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class StoryCommentSpec extends Specification {

  "StoryComment" should {

    val sc = StoryComment.syntax("sc")

    "find by primary keys" in new AutoRollback {
      val maybeFound = StoryComment.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = StoryComment.findBy(sqls.eq(sc.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = StoryComment.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = StoryComment.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = StoryComment.findAllBy(sqls.eq(sc.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = StoryComment.countBy(sqls.eq(sc.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = StoryComment.create(text = "MyString", storyId = 123, createdBy = 123, createdOn = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = StoryComment.findAll().head
      // TODO modify something
      val modified = entity
      val updated = StoryComment.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = StoryComment.findAll().head
      StoryComment.destroy(entity)
      val shouldBeNone = StoryComment.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = StoryComment.findAll()
      entities.foreach(e => StoryComment.destroy(e))
      val batchInserted = StoryComment.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
