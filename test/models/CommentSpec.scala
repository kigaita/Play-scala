package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class CommentSpec extends Specification {

  "Comment" should {

    val c = Comment.syntax("c")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Comment.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Comment.findBy(sqls.eq(c.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Comment.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Comment.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Comment.findAllBy(sqls.eq(c.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Comment.countBy(sqls.eq(c.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Comment.create(text = "MyString", storyId = 123, createdBy = 123, createdOn = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Comment.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Comment.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Comment.findAll().head
      Comment.destroy(entity)
      val shouldBeNone = Comment.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Comment.findAll()
      entities.foreach(e => Comment.destroy(e))
      val batchInserted = Comment.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
