package test

import io.estatico.newtype.macros.newtype
import org.openjdk.jmh.annotations.Benchmark

/**
  * Based on [[https://github.com/ktoso/sbt-jmh/blob/master/sbt-jmh-tester/src/main/scala/test/TestBenchmark.scala]]
  */


object `package` {
  @newtype case class Foo(private[this] val x: String)
  object Foo {
    def unapply(foo: Foo): Option[String] = Some(foo.asInstanceOf[String])
  }

  case class Bar(x: String)
}

class TestBenchmark {
  @Benchmark
  def testCaseClass = Bar("1") match { case Bar(x) => x }

  @Benchmark
  def testNewType = Foo("1") match { case Foo(x) => x }
}