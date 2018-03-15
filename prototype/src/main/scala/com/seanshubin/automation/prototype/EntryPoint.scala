package com.seanshubin.automation.prototype

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.automation.contract.{FilesContract, FilesDelegate}
import com.seanshubin.automation.domain.{Downloader, Parser, ParserImpl}
import com.seanshubin.automation.ssh.{SshFactory, SshFactoryImpl}

object EntryPoint extends App {
  val host: String = args(0)
  val privateKeyPathName: String = args(1)
  val files: FilesContract = FilesDelegate
  val charset: Charset = StandardCharsets.UTF_8
  val sshFactory: SshFactory = new SshFactoryImpl(charset)
  val emit: String => Unit = println
  val parser: Parser = new ParserImpl
  val downloader: Runnable = new Downloader(
    host, privateKeyPathName, files, charset, sshFactory, emit, parser)
  downloader.run()
}
