package com.seanshubin.automation.ssh

import java.io.{InputStream, OutputStream}

trait SshWrapper {
  def execString(command: String): String

  def execOutputStream(command: String, outputStream: OutputStream): Unit

  def execInputStream(command: String, inputStream: InputStream): Unit
}
