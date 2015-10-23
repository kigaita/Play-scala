package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class NewsCommentSpec extends Specification {

  "NewsComment" should {

    val nc = NewsComment.syntax("nc")

    "find by primary keys" in new AutoRollback {
      val maybeFound = NewsComment.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = NewsComment.findBy(sqls.eq(nc.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = NewsComment.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = NewsComment.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = NewsComment.findAllBy(sqls.eq(nc.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = NewsComment.countBy(sqls.eq(nc.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = NewsComment.create(text = "MyString", newsId = 123, createdBy = 123, createdOn = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = NewsComment.findAll().head
      // TODO modify something
      val modified = entity
      val updated = NewsComment.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = NewsComment.findAll().head
      NewsComment.destroy(entity)
      val shouldBeNone = NewsComment.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = NewsComment.findAll()
      entities.foreach(e => NewsComment.destroy(e))
      val batchInserted = NewsComment.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
