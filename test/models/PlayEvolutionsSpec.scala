package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class PlayEvolutionsSpec extends Specification {

  "PlayEvolutions" should {

    val pe = PlayEvolutions.syntax("pe")

    "find by primary keys" in new AutoRollback {
      val maybeFound = PlayEvolutions.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = PlayEvolutions.findBy(sqls.eq(pe.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = PlayEvolutions.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = PlayEvolutions.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = PlayEvolutions.findAllBy(sqls.eq(pe.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = PlayEvolutions.countBy(sqls.eq(pe.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = PlayEvolutions.create(id = 123, hash = "MyString", appliedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = PlayEvolutions.findAll().head
      // TODO modify something
      val modified = entity
      val updated = PlayEvolutions.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = PlayEvolutions.findAll().head
      PlayEvolutions.destroy(entity)
      val shouldBeNone = PlayEvolutions.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = PlayEvolutions.findAll()
      entities.foreach(e => PlayEvolutions.destroy(e))
      val batchInserted = PlayEvolutions.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
