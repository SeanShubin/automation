package com.seanshubin.automation.domain

import com.seanshubin.automation.string.StringUtil

class ParserImpl extends Parser {

  import ParserImpl._

  override def parseKeyEqualsValue(text: String): Map[String, String] = {
    val lines = StringUtil.toLines(text)
    lines.map(parseKeyEqualsValueLine).toMap
  }

  private def parseKeyEqualsValueLine(line: String): (String, String) = {
    val KeyEqualsLinePattern(key, value) = line
    (key, value)
  }
}

object ParserImpl {
  private def capture(s: String): String = s"($s)"

  private def word(): String = """\w+"""

  private def doubleQuoted(s: String): String = "\"" + s + "\""

  private def keyEqualsLine(): String = capture(word()) + "=" + doubleQuoted(capture(word()))

  private val KeyEqualsLinePattern = keyEqualsLine().r
}
