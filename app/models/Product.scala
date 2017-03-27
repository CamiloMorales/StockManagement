package models

import models.dao.ProductDAO
import play.api.libs.concurrent.Akka
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.Play.current

object Product {
  implicit val ProductWrites: Writes[Product] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "productName").write[String] and
    (JsPath \ "currentQuantity").write[Int]
    )(unlift(Product.unapply))

  def add(productName: String, currentQuantity: Int): Option[Long] = {
    val new_product = Product(-1, productName, currentQuantity)
    ProductDAO.create(new_product)
  }

  def getAll(): List[Product] =
    ProductDAO.getAll()

  def refill(prod_id:Int, quantity:Int): Int =
    ProductDAO.updateQuantity(prod_id, quantity)
}

case class Product(id:Int, productName: String, currentQuantity: Int)
