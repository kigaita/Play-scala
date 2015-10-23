package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class NewsCategorySpec extends Specification {

  "NewsCategory" should {

    val nc = NewsCategory.syntax("nc")

    "find by primary keys" in new AutoRollback {
      val maybeFound = NewsCategory.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = NewsCategory.findBy(sqls.eq(nc.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = NewsCategory.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = NewsCategory.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = NewsCategory.findAllBy(sqls.eq(nc.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = NewsCategory.countBy(sqls.eq(nc.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = NewsCategory.create(title = "MyString", description = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = NewsCategory.findAll().head
      // TODO modify something
      val modified = entity
      val updated = NewsCategory.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = NewsCategory.findAll().head
      NewsCategory.destroy(entity)
      val shouldBeNone = NewsCategory.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = NewsCategory.findAll()
      entities.foreach(e => NewsCategory.destroy(e))
      val batchInserted = NewsCategory.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
