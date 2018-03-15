package com.seanshubin.automation.prototype

import java.nio.charset.Charset
import java.nio.file.Paths

import com.seanshubin.automation.contract.FilesContract
import com.seanshubin.automation.io.IoUtil
import com.seanshubin.automation.ssh.SshFactory

class Downloader(host: String,
                 privateKeyPathName: String,
                 files: FilesContract,
                 charset: Charset,
                 sshFactory: SshFactory,
                 emit: String => Unit) extends Runnable {
  override def run(): Unit = {
    val privateKeyPath = Paths.get(privateKeyPathName)
    val privateKeyBytes = files.readAllBytes(privateKeyPath)
    val privateKey = IoUtil.bytesToString(privateKeyBytes, charset)
    val ssh = sshFactory.connect(host, privateKey)
    val text = ssh.execString("cat .digitalocean_password")
    emit(text)
  }
}
