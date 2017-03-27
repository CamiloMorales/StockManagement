package models

import models.dao.TransactionDAO
import play.api.libs.concurrent.Akka
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.Play.current

object Transaction {
  implicit val TransactionWrites: Writes[Transaction] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "customer_id").write[Int] and
    (JsPath \ "product_id").write[Int] and
    (JsPath \ "trans_type").write[String]and
    (JsPath \ "quantity").write[Int] and
    (JsPath \ "finished").write[Int]
    )(unlift(Transaction.unapply))

  def add(customer_id:Int, product_id:Int, trans_type:String, quantity:Int, finished:Int): Option[Long] = {
    val new_transaction = Transaction(-1, customer_id, product_id, trans_type, quantity, finished)
    TransactionDAO.create(new_transaction)
  }

  def getAll(): List[Transaction] =
    TransactionDAO.getAll()

  def getAllByCustomer(customer_id:Int): List[Transaction] =
    TransactionDAO.getAllByCustomer(customer_id)

}

case class Transaction(id:Int, customer_id:Int, product_id:Int, trans_type:String, quantity:Int, finished:Int)