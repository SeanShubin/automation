package com.seanshubin.automation.domain

import java.nio.charset.Charset

import com.seanshubin.automation.io.IoUtil

class ExecutorImpl(charset: Charset) extends Executor {
  def exec(command: String*): Unit = {
    val processBuilder: ProcessBuilder = new ProcessBuilder().command(command: _*)
    val process = processBuilder.start()
    val exitCode = process.waitFor()
    if (exitCode != 0) {
      val message: String = IoUtil.inputStreamToString(process.getErrorStream, charset)
      throw new RuntimeException(message)
    }
  }
}
