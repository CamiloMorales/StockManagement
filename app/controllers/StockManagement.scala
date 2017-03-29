package controllers

import models.{Transaction, _}
import play.api.libs.json.{JsPath, Json, Writes}
import play.api.mvc._
import play.api.Play.current
import play.api.libs.functional.syntax._

class StockManagement extends Controller {

  def addProduct (productName: String, currentQuantity: Int) = Action {
    Product.exists(productName) match {
      case true => Ok(Json.obj("result" -> false))
      case false => Product.add(productName, currentQuantity) match {
        case Some(id) => Ok(Json.obj("result" -> true))
        case None => Ok(Json.obj("result" -> false))
      }
    }
  }

  def getAllProducts() = Action {
    val allProducts = Product.getAll()
    Ok(Json.obj("result" -> allProducts))
  }

  def addCustomer (userName: String) = Action {
    val result_message = Customer.add(userName) match {
      case Some(id) => "Customer added correctly. Id="+id
      case None => "The Customer was not added to the system. Please try again later."
    }
    Ok(Json.obj("result" -> result_message))
  }

  def getAllCustomers() = Action {
    val allCustomers = Customer.getAll()
    Ok(Json.obj("result" -> allCustomers))
  }

  def addTransaction(customer_id: Int, product_id:Int, trans_type:String, quantity:Int, finished:Int) = Action {
    val result_message = Transaction.add(customer_id, product_id, trans_type, quantity, finished) match {
      case Some(id) => "Transaction added correctly. Id="+id
      case None => "The Transaction was not added to the system. Please try again later."
    }
    Ok(Json.obj("result" -> result_message))
  }

  def getAllTransactions() = Action {
    val allTransactions = Transaction.getAll()
    Ok(Json.obj("result" -> allTransactions))
  }

  def getAllTransactionsByCustomer(username: String) = Action {
    val allTransactions = Transaction.getAllByCustomer(username)
    Ok(Json.obj("result" -> allTransactions))
  }

  def refillProduct(prod_id:Int, quantity:Int) = Action {
    val numberOfRowsUpdated  = Product.refill(prod_id, quantity)
    Ok(Json.obj("result" -> numberOfRowsUpdated))
  }

  def signIn (userName: String) = Action {
   Customer.exists(userName) match {
      case true => Ok(Json.obj("result" -> false))
      case false => Customer.add(userName) match {
        case Some(id) => Ok(Json.obj("result" -> true))
        case None => Ok(Json.obj("result" -> false))
      }
    }
  }

  def buyProduct(username: String, prod_Id:Int, quantity:Int) = Action {
    val numberOfRowsUpdated  = Product.buyProduct(username, prod_Id, quantity)
    Ok(Json.obj("result" -> numberOfRowsUpdated))
  }

  def reserveProduct(username: String, prod_Id:Int, quantity:Int) = Action {
    val numberOfRowsUpdated  = Product.reserveProduct(username, prod_Id, quantity)
    Ok(Json.obj("result" -> numberOfRowsUpdated))
  }

  def customerExists(username: String) = Action {
    Customer.exists(username) match {
      case true => Ok(Json.obj("result" -> true))
      case false => Ok(Json.obj("result" -> false))
    }
  }

  def finishReserve(transaction_id: Int) = Action {
    val numberOfRowsUpdated  = Transaction.updateFinished(transaction_id)
    Ok(Json.obj("result" -> numberOfRowsUpdated))
  }
}
