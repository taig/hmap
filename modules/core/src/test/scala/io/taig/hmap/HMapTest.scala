package io.taig.hmap

import munit.FunSuite

final class HMapTest extends FunSuite:
  val format = Key[Int]("format")
  val name = Key[String]("name")
  val description = Key[String]("description")
  val address = Key[Option[String]]("address")

  val hmap: HMap[format.type | name.type] = HMap.Empty.put(format, 3).put(name, "foo")

  test("apply") {
    assertEquals(obtained = hmap.apply(format), expected = 3)
    assertEquals(obtained = hmap.apply(name), expected = "foo")

    assertNoDiff(
      obtained = compileErrors("""hmap.apply(description)"""),
      expected = """error:
                   |Found:    (HMapTest.this.description : io.taig.hmap.Key[String])
                   |Required: io.taig.hmap.Key[A] & Singleton & (
                   |  (HMapTest.this.format : io.taig.hmap.Key[Int]) |
                   |  (HMapTest.this.name : io.taig.hmap.Key[String]))
                   |
                   |where:    A is a type variable with constraint 
                   |
                   |
                   |The following import might make progress towards fixing the problem:
                   |
                   |  import munit.Clue.generate
                   |
                   |hmap.apply(description)
                   |          ^""".stripMargin
    )
  }

  test("apply: empty") {
    assertNoDiff(
      obtained = compileErrors("""HMap.Empty.apply(format)"""),
      expected = """error:
                   |Found:    (HMapTest.this.format : io.taig.hmap.Key[Int])
                   |Required: io.taig.hmap.Key[A] & Singleton & Nothing
                   |
                   |where:    A is a type variable with constraint 
                   |
                   |
                   |The following import might make progress towards fixing the problem:
                   |
                   |  import munit.Clue.generate
                   |
                   |HMap.Empty.apply(format)
                   |                ^""".stripMargin
    )
  }

  test("apply: one") {
    assertNoDiff(
      obtained = compileErrors("""HMap.Empty.put(description, "foobar").apply(name)"""),
      expected = """error:
                   |Found:    (HMapTest.this.name : io.taig.hmap.Key[String])
                   |Required: io.taig.hmap.Key[A] & Singleton &
                   |  (HMapTest.this.description : io.taig.hmap.Key[String])
                   |
                   |where:    A is a type variable with constraint 
                   |
                   |
                   |The following import might make progress towards fixing the problem:
                   |
                   |  import munit.Clue.generate
                   |
                   |HMap.Empty.put(description, "foobar").apply(name)
                   |                                           ^""".stripMargin
    )
  }

  test("get") {
    assertEquals(obtained = hmap.get(format), expected = Some(3))
    assertEquals(obtained = hmap.get(description), expected = None)
    assertEquals(obtained = hmap.get(address), expected = None)
    val hidden: HMap[format.type] = hmap.put(description, "foobar")
    assertEquals(obtained = hidden.get(description), expected = Some("foobar"))
  }

  test("update") {
    assertEquals(
      obtained = hmap.update(name)(_.reverse).apply(name),
      expected = "oof"
    )

    assertNoDiff(
      obtained = compileErrors("""hmap.update(description)(_.reverse)"""),
      expected = """error:
                   |Found:    (HMapTest.this.description : io.taig.hmap.Key[String])
                   |Required: io.taig.hmap.Key[A] & Singleton & (
                   |  (HMapTest.this.format : io.taig.hmap.Key[Int]) |
                   |  (HMapTest.this.name : io.taig.hmap.Key[String]))
                   |
                   |where:    A is a type variable with constraint 
                   |
                   |
                   |The following import might make progress towards fixing the problem:
                   |
                   |  import munit.Clue.generate
                   |
                   |hmap.update(description)(_.reverse)
                   |           ^""".stripMargin
    )
  }

  test("contravariance") {
    val a: HMap[format.type | name.type | description.type] =
      HMap.Empty.put(format, 3).put(name, "foo").put(description, "bar")
    val _: HMap[name.type | description.type] = a
  }
