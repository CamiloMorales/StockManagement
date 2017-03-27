package models

import models.dao.CustomerDAO
import play.api.libs.concurrent.Akka
import play.api.libs.functional.syntax._
import play.api.libs.json._

object Customer {
  implicit val CustomerWrites: Writes[Customer] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "userName").write[String] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String]
    )(unlift(Customer.unapply))

  def add(userName: String, firstName: String, lastName: String): Option[Long] = {
    val new_customer = Customer(-1, userName, firstName, lastName)
    CustomerDAO.create(new_customer)
  }

  def getAll(): List[Customer] =
    CustomerDAO.getAll()
}

case class Customer(id:Int, userName:String, firstName:String, lastName:String)
