package com.zendesk.validation

import io.circe._
import io.circe.parser._
import io.circe.syntax._
import com.zendesk.model._

object ProtocolErrTransformer  {
  implicit class AsParsingError[A](value: Either[io.circe.ParsingFailure, Json]) {
    def toErr: Either[Err, Json] = value match {
      case Left(e)⇒  Left(Err(ErrorCode.jsonParsingError, e.getMessage))
      case Right(x) ⇒ Right(x)
    }
  }
  implicit class AsDecodingError[A](value: Either[io.circe.DecodingFailure, Model]) {
    def toErr: Either[Err, Model] = value match {
      case Left(e)⇒  Left(Err(ErrorCode.jsonDecodingError, e.getMessage))
      case Right(x) ⇒ Right(x)
    }
  }
}
