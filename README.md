# hmap

> An opinionated HMap micro library for Scala 3

## Installation

```sbt
libraryDependencies ++=
  "io.taig" %%% "hmap" % "x.y.z" ::
  Nil
```

## Usage

```scala
Welcome to Scala 3.3.3 (21.0.2, Java OpenJDK 64-Bit Server VM).
Type in expressions for evaluation. Or try :help.

scala> import io.taig.hmap.*

scala> val name = Key[String]("name")
val name: io.taig.hmap.Key[String] = name

scala> val age = Key[Int]("age")
val age: io.taig.hmap.Key[Int] = age

scala> val address = Key[Option[String]]("address")
val address: io.taig.hmap.Key[Option[String]] = address

scala> val data = HMap.Empty.put(name, "Grogu").put(age, 50)
val data: io.taig.hmap.HMap[name.type | age.type] = HashMap(name -> Grogu, age -> 50)

scala> data(name)
val res0: String = Grogu

scala> data(age)
val res1: Int = 50

scala> data(address)
-- [E007] Type Mismatch Error: -------------------------------------------------
1 |data(address)
  |     ^^^^^^^
  |Found:    (address : io.taig.hmap.Key[Option[String]])
  |Required: io.taig.hmap.Key[A] & Singleton & ((name : io.taig.hmap.Key[String]) |
  |  (age : io.taig.hmap.Key[Int]))
  |
  |where:    A is a type variable with constraint
  |
  | longer explanation available when compiling with `-explain`
1 error found

scala> data.get(address)
val res2: Option[Option[String]] = None

scala> val data2 = data.put(name, "Yoda").update(age)(_ + 850)
val data2: io.taig.hmap.HMap[name.type | age.type | name.type] = HashMap(name -> Yoda, age -> 900)

scala> data2(name)
val res3: String = Yoda

scala> data2(age)
val res4: Int = 900

scala> val data3 = data.put(address, None)
val data3: io.taig.hmap.HMap[name.type | age.type | address.type] = HashMap(name -> Grogu, age -> 50, address -> None)

scala> data3(address)
val res5: Option[String] = None
```
