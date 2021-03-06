package com.markusjais.scala.futures.examples.functional.advanced

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Success
import scala.util.Failure
import com.markusjais.scala.futures.examples.business.orders

object TraverseExample extends App {

  val listOfOrderFutures = List.fill(100)(Future { orders.getOrderInEurosFromDb(42) }) // 100 Futures with a Random value

  val vatFutureSequenced = Future.sequence(listOfOrderFutures) map { orderAmounts =>
    orderAmounts map (_ * 1.2)
  } map (_.sum)

  // with traverse the intermediate step of sequence/map is not necessary
  val vatFutureTraversed = Future.traverse(listOfOrderFutures) { futureAmount =>
    futureAmount map (_ * 1.2)
  } map (_.sum)

  print("total value with sequence: ")
  vatFutureSequenced foreach println

  Thread.sleep(10000)

  print("\n\ntotal value with traverse: ")
  vatFutureTraversed foreach println

  // necessary in this dummy app to let future complete
  System.in.read()

}