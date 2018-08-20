package com.zendesk.validation

import cats.data._
import cats.data.Validated._

object ErrorCode extends Enumeration {
  type ErrorCode = Value
  val suspiciousIP,
      forbidenWords,
      tooManyUsers,
      suspiciousLinks,
      jsonDecodingError,
      jsonParsingError= Value
}

import ErrorCode._

case class Err(code: ErrorCode,
               msg: String,
               accountId: Option[Int]=None)
