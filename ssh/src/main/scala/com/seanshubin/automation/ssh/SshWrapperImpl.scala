package com.seanshubin.automation.ssh

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream}
import java.nio.charset.Charset

import com.jcabi.ssh.{Shell, Ssh}
import com.seanshubin.automation.io.IoUtil

class SshWrapperImpl(host: String,
                     userName: String,
                     privateKey: String,
                     charset: Charset) extends SshWrapper {
  private val unsafeShell = new Ssh(host, 22, userName, privateKey)
  private val safeShell = new Shell.Safe(unsafeShell)

  override def execString(command: String): String = {
    println(command)
    val outputStream = new ByteArrayOutputStream()
    execOutputStream(command, outputStream)
    val text = IoUtil.bytesToString(outputStream.toByteArray, charset)
    text
  }

  override def execOutputStream(command: String, outputStream: OutputStream): Unit = {
    val inputStream = new ByteArrayInputStream(Array[Byte]())
    val errorStream = new ByteArrayOutputStream()
    safeShell.exec(command, inputStream, outputStream, errorStream)
  }

  override def execInputStream(command: String, inputStream: InputStream): Unit = {
    val outputStream = new ByteArrayOutputStream()
    val errorStream = new ByteArrayOutputStream()
    safeShell.exec(command, inputStream, outputStream, errorStream)
  }
}
