package com.zendesk.utils

import collection.JavaConversions._
import Globals._

object Utils {
  val blackListedWords = zenCfg.getStringList("rules.tickets.bad.words").toList
  val accountMaxUsers = zenCfg.getInt("rules.account.user.max")

  val blackListedLinks =
    zenCfg.getStringList("rules.tickets.bad.links").toList

  val blackListedIPs =  zenCfg.getStringList("rules.tickets.bad.ips").toList

  val _wordFinder: List[String] ⇒ String ⇒ Option[String] =
    blackList ⇒ word ⇒ blackList.find(x ⇒ x == word)

  val wordFinder: String ⇒ Option[String] = word ⇒ _wordFinder(blackListedWords)(word)

  val _linkFinder: List[String] ⇒ String ⇒ Option[String] = links ⇒ word ⇒
  links.map( link ⇒
    s"""http?.*${link}""".r.findFirstIn(word))
    .filter(_.isDefined).headOption.flatten

  val linkFinder: String ⇒ Option[String] = word ⇒ _linkFinder(blackListedLinks)(word)
  
  val hasBlackListedLink: String ⇒ Option[String] = content ⇒ 
    (content.split("\\s").toList).map(x ⇒
      linkFinder(x)).filter(_.isDefined) match { case Nil ⇒ None
      case l: List[Option[String]] ⇒ l.head
    }
  
  val hasBlackListedWords: String ⇒ Option[String] = content ⇒ 
    (content.split("\\s").toList).map(x ⇒
      wordFinder(x)).filter(_.isDefined) match {
      case Nil ⇒ None
      case l: List[Option[String]] ⇒ l.head
    }
  
  val isFromBlackListedIP: String ⇒ Option[String] = ip ⇒ blackListedIPs.find( _== ip)
  val hasToomanyUsers: Int ⇒ Boolean =
    numOfUsers ⇒ numOfUsers > accountMaxUsers
}
   
