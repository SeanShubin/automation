package com.seanshubin.automation.prototype

import java.nio.charset.{Charset, StandardCharsets}
import java.time.Clock

import com.seanshubin.automation.contract.{FilesContract, FilesDelegate}
import com.seanshubin.automation.domain._
import com.seanshubin.automation.ssh.{SshFactory, SshFactoryImpl}

object EntryPoint extends App {
  val command: String = args(0)
  val host: String = args(1)
  val privateKeyPathName: String = args(2)
  val files: FilesContract = FilesDelegate
  val charset: Charset = StandardCharsets.UTF_8
  val sshFactory: SshFactory = new SshFactoryImpl(charset)
  val emit: String => Unit = println
  val parser: Parser = new ParserImpl
  val clock: Clock = Clock.systemUTC()
  val executor: Executor = new ExecutorImpl(charset)
  val downloader: Runnable = new Downloader(
    host,
    privateKeyPathName,
    files,
    charset,
    sshFactory,
    emit,
    parser,
    clock,
    executor)
  val uploader: Runnable = new Uploader(
    host,
    privateKeyPathName,
    files,
    charset,
    sshFactory,
    emit,
    parser,
    clock,
    executor)
  val dispatcher: Runnable = new Dispatcher(
    command, downloader, uploader)
  dispatcher.run()
}
