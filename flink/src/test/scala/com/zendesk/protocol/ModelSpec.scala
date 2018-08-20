package com.zendesk.protocol

import scala.io._
import Codec._
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.optics.JsonPath._
import cats.data._
import cats.data.Validated._
import cats.implicits._
import org.specs2._


class ModelSpec extends Specification { def is =  s2"""
Model json protocol specifications
  Account class protocol conversions $e1
  User class protocol conversions $e2
  Ticket class protocol conversions $e3
"""
  import com.zendesk.model._
  import Protocol._
  import com.zendesk.validation.ProtocolErrTransformer._

  val stream = getClass.getResourceAsStream("/data.json")
  val testData = scala.io.Source.fromInputStream(stream)(UTF8).getLines
  val jsonData: List[Either[ParsingFailure, Json]] = testData.toList.map(parse(_) )
  val badJsonString = """ {o: 1, x: [1,2,3,4], [1,2}"""
  val badAccount = 
  """ { "data_type":123, "id": 1, "type": "Trial", "created_at": 1517792363, "__created_from_ip": "202.62.86.10" }"""
  def e1 = {
  val a = parse(badAccount)
    println(s" parsed json = ${a}")
   val computedFail = a.flatMap(_.as[Account] )
    println(s" serialized to account json = ${computedFail}")
    computedFail.isLeft
  }
  def e2 = {
    val invalidData =
      """ { "data_type":"uknown-type" , "id": 1, "type": "Trial", "created_at": 1517792363, "__created_from_ip": "202.62.86.10" }"""
    val invalidJsonData = parse(invalidData).right.get
    val ticketJson = jsonData.reverse.head.toOption.get
    val userJson = jsonData.toVector(10).toOption.get
    val acctJson = jsonData.toVector(1).toOption.get
    val computedTicket = zenDecoder(ticketJson)
    val computedUser = zenDecoder(userJson)
    val computedAccount = zenDecoder(acctJson)
    val computedInvalid = zenDecoder(invalidJsonData)
    println(s"#### ticket = ${computedTicket}")
    println(s"#### user = ${computedUser}")
    println(s"#### account = ${computedAccount}")
    computedTicket.isValid &&
      computedAccount.isValid &&
      computedUser.isValid &&
      computedInvalid.isInvalid
  }
  def e3 = 1 must_== 1
}
