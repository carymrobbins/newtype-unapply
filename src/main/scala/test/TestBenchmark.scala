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

  // Handwritten newtype to avoid ClassTag unapply slowing things down
  type Foo2 = Foo2.Type
  object Foo2 {
    type Type <: Base with Tag
    type Base <: Any
    def apply(x: String): Foo2 = x.asInstanceOf[Foo2]
    def unapply(x: Foo2): Unapply = new Unapply(x.asInstanceOf[String])
  }

  case class Bar(x: String)
}

// Ideally these would live in the `object Foo2`; however, jmh seems to throw
// an InternalError for Malformed class name, so putting here instead.
trait Tag
final class Unapply(val get: String) extends AnyVal {
  def isEmpty: Boolean = false
}

class TestBenchmark {
  @Benchmark
  def testCaseClass = Bar("1") match { case Bar(x) => x }

  @Benchmark
  def testNewType = Foo("1") match { case Foo(x) => x }

  @Benchmark
  def testNewType2 = Foo2("1") match { case Foo2(x) => x }
}
