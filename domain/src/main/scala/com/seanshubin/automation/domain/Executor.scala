package com.seanshubin.automation.domain

trait Executor {
  def exec(command: String*): Unit
}
