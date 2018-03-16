package com.seanshubin.automation.domain

import java.nio.file.Path

trait Executor {
  def exec(command: String*): Unit

  def execRedirectOutput(path: Path, command: String*): Unit
}
