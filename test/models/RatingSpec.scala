package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class RatingSpec extends Specification {

  "Rating" should {

    val r = Rating.syntax("r")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Rating.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Rating.findBy(sqls.eq(r.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Rating.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Rating.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Rating.findAllBy(sqls.eq(r.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Rating.countBy(sqls.eq(r.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Rating.create(classification = "MyString", description = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Rating.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Rating.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Rating.findAll().head
      Rating.destroy(entity)
      val shouldBeNone = Rating.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Rating.findAll()
      entities.foreach(e => Rating.destroy(e))
      val batchInserted = Rating.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
