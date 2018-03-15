package com.seanshubin.automation.domain

trait Parser {
  def parseKeyEqualsValue(text: String): Map[String, String]
}
