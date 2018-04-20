package test

import io.estatico.newtype.macros.newtype
import org.openjdk.jmh.annotations.Benchmark

/**
  * Based on [[https://github.com/ktoso/sbt-jmh/blob/master/sbt-jmh-tester/src/main/scala/test/TestBenchmark.scala]]
  */
object types {

  @newtype case class Foo(private[this] val x: String)
  object Foo {
    def unapply(foo: Foo): Option[String] = Some(foo.asInstanceOf[String])
  }

  // Handwritten newtype to avoid ClassTag unapply slowing things down
  type Manual = Manual.Type

  // Handwritten newtype to avoid ClassTag unapply slowing things down
  type ManualUnapplyValueClass = ManualUnapplyValueClass.Type

  case class Bar(x: String)
}

import types._

object Manual {
  type Type <: Base with Tag
  trait Tag
  type Base <: Any
  def apply(x: String): Manual = x.asInstanceOf[Manual]
  def unapply(x: Manual): Some[String] = Some(x.asInstanceOf[String])
}

object ManualUnapplyValueClass {
  type Type <: Base with Tag
  trait Tag
  type Base <: Any
  def apply(x: String): ManualUnapplyValueClass = x.asInstanceOf[ManualUnapplyValueClass]
  def unapply(x: ManualUnapplyValueClass): Unapply = new Unapply(x.asInstanceOf[String])
  final class Unapply(val get: String) extends AnyVal {
    def isEmpty: Boolean = false
  }
}

class TestBenchmark {

  @Benchmark
  def testCaseClass = Bar("1") match { case Bar(x) => x }

  @Benchmark
  def testManualSimpleUnapply = Manual("1") match { case Manual(x) => x }

  @Benchmark
  def testManualUnapplyValueClass = ManualUnapplyValueClass("1") match { case ManualUnapplyValueClass(x) => x }

  @Benchmark
  def testNewTypeSimpleUnapply = Foo("1") match { case Foo(x) => x }
}
