package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class NewsViewCountSpec extends Specification {

  "NewsViewCount" should {

    val nvc = NewsViewCount.syntax("nvc")

    "find by primary keys" in new AutoRollback {
      val maybeFound = NewsViewCount.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = NewsViewCount.findBy(sqls.eq(nvc.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = NewsViewCount.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = NewsViewCount.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = NewsViewCount.findAllBy(sqls.eq(nvc.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = NewsViewCount.countBy(sqls.eq(nvc.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = NewsViewCount.create(newsId = 123, viewCount = 1L)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = NewsViewCount.findAll().head
      // TODO modify something
      val modified = entity
      val updated = NewsViewCount.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = NewsViewCount.findAll().head
      NewsViewCount.destroy(entity)
      val shouldBeNone = NewsViewCount.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = NewsViewCount.findAll()
      entities.foreach(e => NewsViewCount.destroy(e))
      val batchInserted = NewsViewCount.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
