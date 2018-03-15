package com.seanshubin.automation.contract.test

import java.util.UUID

import com.seanshubin.automation.contract.UuidContract

trait UuidNotImplemented extends UuidContract {
  override def randomUUID: UUID = ???

  override def nameUUIDFromBytes(name: Array[Byte]): UUID = ???

  override def fromString(name: String): UUID = ???
}
