package com.zendesk.utils

import org.specs2._


class UtilsSpec extends Specification { def is =  s2"""
Utils specifications
  hasBlackListed link specs $e1
  hasBlackListedLink specs $e2
  hasBlackListed words specs $e3
"""
  import Utils._

  def e1 =  {
    val badLink = "http://bit.ly/18z84398"
    val goodLink2 = "bit.ly/18z84398"
    val goodLink = "http://google.com/18z84398"
     linkFinder(badLink).isDefined
    
  }
  def e2 =  {
    val badLink = "Earn your money here: http://bit.ly/18z84398"
    val goodLink2 = "Earn your money here: bit.ly/18z84398"
    val goodLink = "Earn your money here: http://google.com/18z84398"

    hasBlackListedLink(badLink).isDefined && 
    hasBlackListedLink(goodLink).isEmpty && 
    hasBlackListedLink(goodLink2).isEmpty
  }
  def e3 = {
    val bad = "Hello from Apple Inc"
    val good = "Hello from  Inc"
    hasBlackListedWords(bad).isDefined && 
    hasBlackListedWords(good).isEmpty
  }
}
