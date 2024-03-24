package io.taig.hmap

import scala.collection.immutable.HashMap

opaque type HMap[-T] = HashMap[String, Any]

object HMap:
  extension [T](self: HMap[T])
    def unsafeToHashMap: HashMap[String, Any] = self
    def apply[A](key: Key[A] & Singleton & T): A = self.apply(key.name).asInstanceOf[A]
    def get[A](key: Key[A] & Singleton): Option[A] = self.get(key.name).map(_.asInstanceOf[A])
    def put[A](key: Key[A] & Singleton, value: A): HMap[T | key.type] = self.updated(key.name, value)
    def update[A](key: Key[A] & Singleton & T)(f: A => A): HMap[T] = self.updated(key.name, f(apply(key)))

  val Empty: HMap[Nothing] = HashMap.empty
