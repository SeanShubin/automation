package com.seanshubin.automation.domain

import java.io.OutputStream
import java.nio.charset.Charset
import java.nio.file.{Path, Paths}
import java.time.Clock

import com.seanshubin.automation.contract.FilesContract
import com.seanshubin.automation.io.IoUtil
import com.seanshubin.automation.ssh.SshFactory

class Downloader(host: String,
                 privateKeyPathName: String,
                 files: FilesContract,
                 charset: Charset,
                 sshFactory: SshFactory,
                 emit: String => Unit,
                 parser: Parser,
                 clock: Clock) extends Runnable {
  override def run(): Unit = {
    val privateKeyPath = Paths.get(privateKeyPathName)
    val privateKeyBytes = files.readAllBytes(privateKeyPath)
    val privateKey = IoUtil.bytesToString(privateKeyBytes, charset)
    val ssh = sshFactory.connect(host, privateKey)
    val text = ssh.execString("cat .digitalocean_password")
    val mySqlPassword = parser.parseKeyEqualsValue(text)("root_mysql_pass")
    val mySqlDumpDir = Paths.get("target", PathUtil.makeFileNameSafeForOperatingSystem(clock.instant().toString))
    files.createDirectories(mySqlDumpDir)
    val mySqlDumpPath = mySqlDumpDir.resolve("my-sql.txt")
    withOutputStream(mySqlDumpPath) { outputStream =>
      ssh.execOutputStream(s"mysqldump -u root -p$mySqlPassword wordpress", outputStream)
    }
  }

  private def withOutputStream[T](path: Path)(f: OutputStream => T): T = {
    val outputStream = files.newOutputStream(path)
    try {
      f(outputStream)
    } finally {
      outputStream.close()
    }
  }
}

//backup: # mysqldump -u root -p[root_password] [database_name] > dumpfilename.sql

//restore:# mysql -u root -p[root_password] [database_name] < dumpfilename.sql