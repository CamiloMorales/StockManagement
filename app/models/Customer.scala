package models

import models.dao.CustomerDAO
import play.api.libs.concurrent.Akka
import play.api.libs.functional.syntax._
import play.api.libs.json._

object Customer {
  implicit val CustomerWrites: Writes[Customer] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "userName").write[String]
    )(unlift(Customer.unapply))

  def add(userName: String): Option[Long] = {
    val new_customer = Customer(-1, userName)
    CustomerDAO.create(new_customer)
  }

  def getAll(): List[Customer] =
    CustomerDAO.getAll()

  def exists(username: String): Boolean = {
    CustomerDAO.getByUsername(username) match {
      case Some(customer) => true
      case _ => false
    }
  }

  def getByUsername(username: String): Option[Customer] = {
    CustomerDAO.getByUsername(username)
  }
}

case class Customer(id:Int, userName:String)
