package adt

import adt.FunctorModule.Functor
import adt.ListModule.{Cons, MyList, MyNil}

object TreeModule extends App {
  sealed trait Tree[+T]{
    def value: T
    def map[R](f: T => R): Tree[R]
  }

  case class Branch[T](left: Tree[T],value: T,right: Tree[T]) extends Tree[T] {
    override def map[R](f: (T) => R): Tree[R] = Branch(left.map(f),f(value),right.map(f))
  }
  case class Leaf[T](value: T) extends Tree[T] {
    override def map[R](f: (T) => R): Tree[R] = Leaf(f(value))
  }
//  case object Empty extends Tree[Nothing]

  private val tree: Branch[Int] = Branch(Leaf(2), 1, Leaf(3))
  println(tree)
  println(s"value is ${tree.right.value}")

  tree match {
    case Branch(_,_,Leaf(v)) => println(s"leaf is $v")
  }

  private val bigTree:Tree[Int] = Branch(Leaf(2), 1, Branch(Leaf(3),4,Leaf(5)))

  def rightMost[T]: Tree[T] => T = {
    case Leaf(v) => v
    case Branch(_,_,right) => rightMost(right)
  }

  println(s"right most value is ${rightMost(bigTree)}")

  def allMulti2(tree: Tree[Int]): Tree[Int] = map(tree)(_ * 2)

  def map[A,B](tree: Tree[A])(f: A => B): Tree[B] = tree.map(f)

  println(allMulti2(bigTree))


  def map[T[_], A, B](t: T[A])(f: A => B)(implicit functor: Functor[T]): T[B] = {
    functor.map(t)(f)
  }

  import FunctorModule._

  println(map(bigTree)(_ * 2))

  val list: MyList[Int] = Cons(1, Cons(2, Cons(3, MyNil)))
  println(map(list)(_ * 2))
}
