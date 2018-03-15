package com.seanshubin.automation.domain

import org.scalatest.FunSuite

class ParserTest extends FunSuite {
  test("parse key = value") {
    val sampleText =
      """root_mysql_pass="foo"
        |wordpress_mysql_pass="bar"""".stripMargin
    val parser = new ParserImpl
    val parsed = parser.parseKeyEqualsValue(sampleText)
    assert(parsed === Map("root_mysql_pass" -> "foo", "wordpress_mysql_pass" -> "bar"))
  }
}
