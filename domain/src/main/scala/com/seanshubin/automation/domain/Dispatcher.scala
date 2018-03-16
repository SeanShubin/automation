package com.seanshubin.automation.domain

class Dispatcher(command: String,
                 downloader: Runnable,
                 uploader: Runnable) extends Runnable {
  override def run(): Unit = {
    command match {
      case "download" => downloader.run()
      case "upload" => uploader.run()
      case x => throw new UnsupportedOperationException(s"Unsupported command $x")
    }
  }
}
