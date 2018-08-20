package com.zendesk.model

sealed trait Model

case class User(
  data_type: String,
  id: Int,
  account_id: Int) extends Model

case class Ticket(
  data_type: String,
  id: Int,
  account_id: Int,
  content: String) extends Model

case class Account(
  data_type: String,
  id: Int,
  `type`: String,
  created_from_ip: String
  ) extends Model


case class Data(
  ticket: Ticket,
  account: Account,
  users: Int=0
) extends Model
