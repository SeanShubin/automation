package com.seanshubin.automation.ssh

trait SshFactory {
  def connect(host: String, privateKey: String): SshWrapper
}
