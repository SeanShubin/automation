package com.seanshubin.automation.ssh

import java.nio.charset.Charset

class SshFactoryImpl(charset: Charset) extends SshFactory {
  def connect(host: String, privateKey: String): SshWrapper = {
    new SshWrapperImpl(host, "root", privateKey, charset)
  }
}
