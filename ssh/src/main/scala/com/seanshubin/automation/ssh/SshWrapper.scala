package com.seanshubin.automation.ssh

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, OutputStream}
import java.nio.charset.Charset

import com.jcabi.ssh.{Shell, Ssh}
import com.seanshubin.automation.io.IoUtil

class SshWrapper(host: String, userName: String, privateKey: String, charset: Charset) {
  private val unsafeShell = new Ssh(host, 22, userName, privateKey)
  private val safeShell = new Shell.Safe(unsafeShell)

  def execString(command: String): String = {
    val outputStream = new ByteArrayOutputStream()
    execOutputStream(command, outputStream)
    val text = IoUtil.bytesToString(outputStream.toByteArray, charset)
    text
  }

  def execOutputStream(command: String, outputStream: OutputStream): Unit = {
    val inputStream = new ByteArrayInputStream(Array[Byte]())
    val errorStream = new ByteArrayOutputStream()
    safeShell.exec(command, inputStream, outputStream, errorStream)
  }
}
