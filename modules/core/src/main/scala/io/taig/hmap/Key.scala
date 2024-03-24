package io.taig.hmap

opaque type Key[A] = String

object Key:
  extension [A](self: Key[A]) def name: String = self

  def apply[A](value: String): Key[A] = value
