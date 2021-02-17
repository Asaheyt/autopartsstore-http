package kz.asaheyt.inc.autopartsstore.http.database

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import kz.asaheyt.inc.autopartsstore.http.model.AutoPart
import shapeless.syntax.std.tuple.productTupleOps

import scala.List
import scala.collection.mutable.ArrayBuffer

class Database {

  private val hicaryConf = new HikariConfig()

  private val ds = {
    hicaryConf.setDataSourceClassName(null)
    hicaryConf.setDriverClassName("org.postgresql.Driver")

    hicaryConf.setJdbcUrl("jdbc:postgresql://localhost:5432/dar")
    hicaryConf.setUsername("developer")
    hicaryConf.setPassword("developer@postgres")

    new HikariDataSource(hicaryConf)
  }


  def getConnection() = {
    ds.getConnection()
  }


  def addAutoPart(autoPart: AutoPart): AutoPart = {
    val sql = "INSERT INTO auto_parts_store.auto_part(name, price, quantity, customer_id, total_price) VALUES(?, ?, ?, ?, ?)"

    val conn = getConnection()

    val pst = conn.prepareStatement(sql)

    pst.setString(1, autoPart.name)
    pst.setDouble(2, autoPart.price)
    pst.setInt(3, autoPart.quantity)
    pst.setLong(4, autoPart.customerId)
    pst.setDouble(5, autoPart.totalPrice)

    pst.execute()

    conn.close
    autoPart
  }

  def editAutoPart(autoPart: AutoPart): AutoPart = {
    val sql = "UPDATE auto_parts_store.auto_part SET name=?, price=?, quantity=?, customer_id=?, total_price=? WHERE auto_part_id=?"

    val conn = getConnection()

    val pst = conn.prepareStatement(sql)

    pst.setString(1, autoPart.name)
    pst.setDouble(2, autoPart.price)
    pst.setInt(3, autoPart.quantity)
    pst.setLong(4, autoPart.customerId)
    pst.setDouble(5, autoPart.totalPrice)
    pst.setDouble(6, autoPart.autoPartId)

    pst.execute()

    conn.close
    autoPart
  }

  def deleteAutoPart(autoPartID: Long): Boolean = {
    val sql = "DELETE FROM auto_parts_store.auto_part WHERE auto_part_id = ?"

    val conn = getConnection()

    val pst = conn.prepareStatement(sql)

    pst.setLong(1, autoPartID)

    pst.execute()

    conn.close

    true
  }

  def getAutoPart(autoPartID: Long): AutoPart = {
    val sql = "SELECT auto_part_id, name, price, quantity, customer_id, total_price FROM auto_parts_store.auto_part WHERE auto_part_id = ?"

    var autoPartId: Long = 0
    var name: String = ""
    var price: Double = 0D
    var quantity: Int = 0
    var customerId: Long = 0
    var totalPrice: Double = 0D

    val conn = getConnection()

    val pst = conn.prepareStatement(sql)

    pst.setLong(1, autoPartID)

    val rs = pst.executeQuery()

    while (rs.next()) {
      autoPartId = rs.getLong("auto_part_id")
      name = rs.getString("name")
      price = rs.getDouble("price")
      quantity = rs.getInt("quantity")
      customerId = rs.getLong("customer_id")
      totalPrice = rs.getDouble("total_price")
    }

    AutoPart(autoPartId, name, price, quantity, customerId, totalPrice)
  }

  def getAutoParts(): ArrayBuffer[AutoPart] = {
    val sql = "SELECT auto_part_id, name, price, quantity, customer_id, total_price FROM auto_parts_store.auto_part"

    var autoPartId: Long = 0
    var name: String = ""
    var price: Double = 0D
    var quantity: Int = 0
    var customerId: Long = 0
    var totalPrice: Double = 0D

    var autoParts : ArrayBuffer[AutoPart] = ArrayBuffer()

    val conn = getConnection()

    val pst = conn.prepareStatement(sql)

    val rs = pst.executeQuery()

    while (rs.next()) {
      autoPartId = rs.getLong("auto_part_id")
      name = rs.getString("name")
      price = rs.getDouble("price")
      quantity = rs.getInt("quantity")
      customerId = rs.getLong("customer_id")
      totalPrice = rs.getDouble("total_price")

      autoParts += AutoPart(autoPartId, name, price, quantity, customerId, totalPrice)
    }

    autoParts
  }


  /*
  def getCuSlimeTableID(co: CuSlime) = {
    logger.info("getCuSlimeTableID -> Get table ID for update")
    var listDouble:ListBuffer[Double] = ListBuffer.empty
    var id = 0
    val sql = s"SELECT ID, ValueVMT, ValueSMT, ValueHumid FROM $cuSlime where ReportId = ? and DateValue = ? and Batch = ? "

    try {
      val conn = ds.getConnection()
      try {
        val pst = conn.prepareStatement(sql)
        pst.setString(1, co.reportId)
        co.dateValue match {
          case Some(v) =>  {
            pst.setTimestamp(2, new Timestamp(co.dateValue.get.getMillis))
          }
          case None => pst.setString(2, null)
        }
        pst.setInt(3, co.batch.get)
        val rs = pst.executeQuery()
        while (rs.next()) {
          if(rs.getInt("id") > 0)
            id = rs.getInt("id")

          logger.info("id is {}", rs.getInt("id"))
          listDouble += rs.getDouble("ValueVMT")
          listDouble += rs.getDouble("ValueSMT")
          listDouble += rs.getDouble("ValueHumid")
        }
      } catch {
        case e: Exception => e.printStackTrace()
      } finally {
        conn.close()
      }

    } catch {
      case e: Exception => e.printStackTrace()
    }
    (id, listDouble.toList)
  }

def setCuOperationsUpdate(id: Int, co: CuOperations): Int = {
    val sql = s"UPDATE $cuOperations " +
      "SET TS = ?, OperationID = ?, Quantity = ?, Weight = ?, BatchString = ?, WShift = ?, Deleted = ?, ReportDate = ? " +
      "WHERE ID = " + id

    val sqlDeleted = s"UPDATE $cuOperations " +
      "SET Deleted = ? WHERE ID = " + id
    val conn = ds.getConnection
    val changedLines = {
      try {
        if (co.deleted == 1) {
          val pst = conn.prepareStatement(sqlDeleted)
          pst.setByte(1, co.deleted)
          pst.executeUpdate()
        } else {
          val pst = conn.prepareStatement(sql)
          pst.setTimestamp(1, new Timestamp(co.ts.getMillis))
          pst.setInt(2, co.operationId.get)
          pst.setDouble(3, co.quantity.orElse(Some(0.toDouble)).get)
          pst.setDouble(4, co.weight.orElse(Some(0.toDouble)).get)
          co.batchString match {
            case Some(value) => pst.setString(5, value)
            case None => pst.setString(5, null)
          }
          pst.setInt(6, co.wShift.get)
          pst.setByte(7, co.deleted)
          pst.setTimestamp(8, new Timestamp(co.reportDate.getMillis))
          pst.executeUpdate()
        }
      } catch {
        case e: Exception =>{
          logger.error("setCuOperationsUpdate -> " + e.getMessage)
          0
        }
      } finally {
        conn.close()
      }
    }
    changedLines
  }




  * */
}
