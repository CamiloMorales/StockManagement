package controllers

import models.{Transaction, _}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.Play.current

class StockManagement extends Controller {

  def addProduct (productName: String, currentQuantity: Int) = Action {
    val result_message = Product.add(productName, currentQuantity) match {
      case Some(id) => "Product added correctly. Id="+id
      case None => "The product was not added to the stock. Please try again later."
    }
    Ok(Json.obj("result" -> result_message))
  }

  def getAllProducts() = Action {
    val allProducts = Product.getAll()
    Ok(Json.obj("result" -> allProducts))
  }

  def addCustomer (userName: String, firstName: String, lastName: String) = Action {
    val result_message = Customer.add(userName,firstName,lastName) match {
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

  def getAllTransactionsByCustomer(customer_id: Int) = Action {
    val allTransactions = Transaction.getAllByCustomer(customer_id)
    Ok(Json.obj("result" -> allTransactions))
  }

  def refillProduct(prod_id:Int, quantity:Int) = Action {
    val numberOfRowsUpdated  = Product.refill(prod_id, quantity)
    Ok(Json.obj("result" -> numberOfRowsUpdated))
  }
}
