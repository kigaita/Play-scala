package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class NewsSpec extends Specification {

  "News" should {

    val n = News.syntax("n")

    "find by primary keys" in new AutoRollback {
      val maybeFound = News.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = News.findBy(sqls.eq(n.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = News.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = News.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = News.findAllBy(sqls.eq(n.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = News.countBy(sqls.eq(n.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = News.create(title = "MyString", text = "MyString", keywords = "MyString", status = false, banned = false, newsCategoryId = 123, ratingId = 123, createdBy = 123, createdOn = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = News.findAll().head
      // TODO modify something
      val modified = entity
      val updated = News.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = News.findAll().head
      News.destroy(entity)
      val shouldBeNone = News.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = News.findAll()
      entities.foreach(e => News.destroy(e))
      val batchInserted = News.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
