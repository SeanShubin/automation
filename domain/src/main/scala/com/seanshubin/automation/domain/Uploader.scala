package com.seanshubin.automation.domain

import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.{Path, Paths}
import java.time.Clock

import com.seanshubin.automation.contract.FilesContract
import com.seanshubin.automation.io.IoUtil
import com.seanshubin.automation.ssh.SshFactory

class Uploader(host: String,
               privateKeyPathName: String,
               files: FilesContract,
               charset: Charset,
               sshFactory: SshFactory,
               emit: String => Unit,
               parser: Parser,
               clock: Clock,
               executor: Executor) extends Runnable {
  override def run(): Unit = {
    val dir = Paths.get(
      "target",
      "local",
      PathUtil.makeFileNameSafeForOperatingSystem(
        clock.instant().toString))
    files.createDirectories(dir)
    val mySqlDumpPath: Path = dir.resolve("wordpress.sql")
    executor.execRedirectOutput(
      mySqlDumpPath,
      "/Applications/MAMP/Library/bin/mysqldump",
      "-u", "root", "-proot", "wordpress")
    executor.exec(
      "scp",
      mySqlDumpPath.toString,
      s"root@$host:"
    )
    val privateKeyPath = Paths.get(privateKeyPathName)
    val privateKeyBytes = files.readAllBytes(privateKeyPath)
    val privateKey = IoUtil.bytesToString(privateKeyBytes, charset)
    val ssh = sshFactory.connect(host, privateKey)
    val text = ssh.execString("cat .digitalocean_password")
    val mySqlPassword = parser.parseKeyEqualsValue(text)("root_mysql_pass")
    withInputStream(mySqlDumpPath) {
      inputStream =>
        ssh.execInputStream(
          s"mysql -u root -p$mySqlPassword wordpress", inputStream)
    }
    val updateSql = s"update wp_options set option_value = 'http://$host' where option_name in ('siteurl', 'home')"
    val quotedUpdateSql = "\"" + updateSql + "\""
    ssh.execString(
      s"mysql -u root -p$mySqlPassword -e $quotedUpdateSql wordpress")
    executor.exec(
      "scp",
      "-r",
      s"/Applications/MAMP/htdocs/wordpress/wp-content/themes",
      s"root@$host:/var/www/html/wp-content/"
    )
    executor.exec(
      "scp",
      "-r",
      s"/Applications/MAMP/htdocs/wordpress/wp-content/uploads",
      s"root@$host:/var/www/html/wp-content/"
    )
  }

  private def withInputStream[T](path: Path)(f: InputStream => T): T = {
    val inputStream = files.newInputStream(path)
    try {
      f(inputStream)
    } finally {
      inputStream.close()
    }
  }

}
