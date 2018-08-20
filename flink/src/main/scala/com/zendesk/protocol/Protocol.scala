package com.zendesk.protocol

import io.circe._
import com.zendesk.model._
import io.circe.syntax._
import io.circe.parser._
import cats.data._
import cats.data.Validated._
import cats.implicits._
import com.zendesk.validation._
import com.zendesk.validation.ProtocolErrTransformer._

object Protocol {

  implicit val decodeUser: Decoder[User] =
    Decoder.forProduct3("data_type", "id", "account_id")(User.apply)
  implicit val encodeUser: Encoder[User] =
    Encoder.forProduct3("data_type", "id", "account_id")(u ⇒
      (u.data_type, u.id, u.account_id))

  implicit val decodeTicket: Decoder[Ticket] =
    Decoder.forProduct4("data_type", "id", "account_id", "content")(Ticket.apply)
  implicit val encodeTicket: Encoder[Ticket] =
    Encoder.forProduct4("data_type", "id", "account_id", "content")(t ⇒
      (t.data_type, t.id, t.account_id, t.content))

  implicit val decodeAccount: Decoder[Account] =
    Decoder.forProduct4("data_type", "id", "type", "created_from_ip")(Account.apply)
  implicit val encoderAccount: Encoder[Account] =
    Encoder.forProduct4("data_type", "id", "type", "created_from_ip")(a ⇒
      (a.data_type, a.id, a.`type`, a.created_from_ip))

  val asAccount: PartialFunction[Json, Validation[Model]] = {
    case j if j.as[Account].isRight &&
        j.as[Account].right.get.data_type.toLowerCase == "account" ⇒
      j.as[Account].toErr.toValidatedNel //right.get.validNel
  }
  val asUser: PartialFunction[Json, Validation[Model]] = {
    case j if j.as[User].isRight &&
        j.as[User].right.get.data_type.toLowerCase == "user" ⇒
      j.as[User].toErr.toValidatedNel
  }
  val asTicket: PartialFunction[Json, Validation[Model]] = {
    case j if j.as[Ticket].isRight &&
        j.as[Ticket].right.get.data_type.toLowerCase == "ticket"⇒
      j.as[Ticket].toErr.toValidatedNel
  }
  
  val fallBack: PartialFunction[Json, Validation[Model]] ={
    case j: Json ⇒ Err(
      code=ErrorCode.jsonDecodingError,
      msg=s"invalid JSON: ${j.toString}",
      accountId=None).invalidNel
  }
 
  val zenDecoder: Json ⇒ Validation[Model]=
    json ⇒  (asAccount orElse asUser orElse asTicket orElse fallBack)(json)

  val asZenModel: String ⇒ Validation[Model]  = text ⇒ 
    parse(text) match {
      case Right(json) ⇒ zenDecoder(json)
      case Left(e) ⇒ Err(
        code=ErrorCode.jsonParsingError,
        msg=s"invalid JSON: ${e.message}",
        accountId=None).invalidNel
  }

}
