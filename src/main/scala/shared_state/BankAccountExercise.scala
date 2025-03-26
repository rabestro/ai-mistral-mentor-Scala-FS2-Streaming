package shared_state

import cats.effect.{IO, IOApp, Ref}
import cats.implicits.catsSyntaxParallelTraverse_

object BankAccountExercise extends IOApp.Simple {
  private val InitialBalance = 1000
  private val DepositAmount = 200
  private val WithdrawAmount = 100

  private type BankAccount = Ref[IO, Int]

  private val account: IO[BankAccount] = Ref[IO].of(InitialBalance)

  private def deposit(account: BankAccount): IO[Unit] =
    (1 to 5).toList.parTraverse_(
      _ => account.update(_ + DepositAmount) <* IO.println("deposit"))

  private def withdraw(account: BankAccount): IO[Unit] =
    (1 to 5).toList.parTraverse_(
      _ => account.update(_ - WithdrawAmount) <* IO.println("withdraw"))

  override def run: IO[Unit] = for {
    ref <- account
    amount <- ref.get
    _ <- IO.println(s"Starting balance: $amount")
    _ <- deposit(ref).both(withdraw(ref))
    amount <- ref.get
    _ <- IO.println(s"Final balance: $amount")
  } yield ()
}
