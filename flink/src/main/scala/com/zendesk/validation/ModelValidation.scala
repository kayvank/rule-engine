package com.zendesk.validation

import cats.data._
import cats.data.Validated._
import cats.implicits._
import com.zendesk._
import model._
import utils.Utils._

object RuleValidators {
  val _validateWords: (String ⇒ Option[String]) ⇒ Data ⇒ Validation[Account] =
    f ⇒ data ⇒ f(data.ticket.content) match {
      case None ⇒  data.account.validNel
      case Some(w) ⇒ Err(
        code=ErrorCode.forbidenWords,
        msg=w,
        accountId=Some(data.account.id)).invalidNel
    }

  val _validateLinks: (String ⇒ Option[String]) ⇒ Data ⇒ Validation[Account] =
    f ⇒ data ⇒ f(data.ticket.content) match {
      case None  ⇒ data.account.validNel
      case Some(l) ⇒ Err(
        code=ErrorCode.suspiciousLinks,
        msg=l,
        accountId=Some(data.account.id)).invalidNel
    }

  val _validateIp:(String ⇒ Option[String]) ⇒ Account ⇒ Validation[Account] =
    f ⇒ account ⇒ f(account.created_from_ip) match {
      case None ⇒ account.validNel
      case Some(ip) ⇒ Err(
        code=ErrorCode.suspiciousIP,
        msg=ip,
        accountId= Some(account.id)).invalidNel
    }

  val _validateMaxUser: (Int ⇒ Boolean) ⇒ Data ⇒ Validation[Account] =
    f ⇒  data ⇒ if(f(data.users) )
    Err(
      code=ErrorCode.tooManyUsers,
      msg=data.users.toString, 
      accountId= Some(data.account.id)).invalidNel
       else data.account.validNel
}

object ModelRuleValidation  {
  import RuleValidators._
  import cats.Apply

  def validateWords(data: Data) =
    _validateWords(hasBlackListedWords)(data)
  def validateLinks(data: Data) =
    _validateLinks(hasBlackListedLink)(data)
  def validateIp(account: Account) =
    _validateIp(isFromBlackListedIP)(account)
  def validateMaxUser(data: Data) =
    _validateMaxUser(hasToomanyUsers)(data)
  implicit val toAcc: (Account,Account,Account,Account) ⇒ Account = (a,_,_,_)⇒ a

  implicit class TicketRuleEnforcer(data: Data) {
    def enforce: Validation[Account] =  (
      validateWords(data),
      validateLinks(data),
      validateIp(data.account),
      validateMaxUser(data)
    ).mapN(toAcc)
  }
}
object Store {
  import scala.collection.mutable._
  val accountUsers: Map[Int, Int] = new HashMap[Int, Int]() with SynchronizedMap[Int, Int]()
  val accounts: Map[Int, Account] = new HashMap[Int, Account]() with SynchronizedMap[Int, Account]()
}
