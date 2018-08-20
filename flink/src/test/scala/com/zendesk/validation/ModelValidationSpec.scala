package com.zendesk.validation

import org.specs2._
import ModelRuleValidation._
import com.zendesk.model._

class ModelValidationSpec extends Specification { def is = s2"""
ModelValidation Rules Specification
  break all validation specs $e1
  valid account specs $e2
"""
  def e1 = {
    val mocAccount = Account(
      data_type="account",
      id=1,
      `type`="test-account",
      created_from_ip="202.62.86.10" )
    val mocTicket = Ticket(
      data_type="ticket",
      id=1,
      account_id=1,
      content="Hello from Apple Inc http://bit.ly"
    )
    val mocData = Data(ticket=mocTicket, account=mocAccount, users=21)
      println(s"invalid moc-data = ${mocData}")
    val computed = mocData.enforce
    println(s"validate invalid moc-data = ${computed}")
    computed.isInvalid
  }
  def e2 = {
    val mocAccount = Account(
      data_type="account",
      id=1,
      `type`="test-account",
      created_from_ip="101.62.86.10" )
    val mocTicket = Ticket(
      data_type="ticket",
      id=1,
      account_id=1,
      content="Hello from Acme http://google.com"
    )
    val mocData = Data(ticket=mocTicket, account=mocAccount, users=19)
    println(s"valid moc-data = ${mocData}")
    val computed = mocData.enforce
    println(s"validate valid moc-data = ${computed}")
    computed.isValid 
  }
}
