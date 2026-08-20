package sculpter

class Interpreter:

  private var stacks: Map[String, List[Option[Double]]] =
    Map().withDefaultValue(List())

  private var history: List[Map[String, List[Option[Double]]]] = List()

  private var currentStatement: Int = 0

  private var program: Program = _

  private var skippedInstruction: Boolean = false

  def reset(program: Program): Unit =
    this.program = program
    stacks = Map().withDefaultValue(List())
    history = List()
    currentStatement = 0
    skippedInstruction = false

  def stepForward(): Boolean =
    if (currentStatement >= program.statements.length)
      return false

    skippedInstruction = false

    history = history :+ stacks

    execute(program.statements(currentStatement))
    currentStatement += 1

    true

  def stepBackward(): Boolean =
    if (history.isEmpty)
      return false

    stacks = history.last
    history = history.dropRight(1)
    currentStatement -= 1

    true

  private def execute(statement: Statement): Unit = statement match
    case UnaryStatement(operation, operand) => executeUnary(operation, operand)
    case BinaryStatement(operation, left, right) =>
      executeBinary(operation, left, right)

  private def ensureStackExists(name: String): Unit =
    if (!stacks.contains(name)) {
      stacks = stacks.updated(name, List())
    }

  private def executeUnary(operation: TokenType, operand: Expr): Unit =
    operation match
      case TokenType.QUESTION =>
        operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).nonEmpty) {
              val value = stacks(name).head
              stacks = stacks.updated(name, stacks(name).tail)

              value match {
                case None =>
                  currentStatement += 1
                  skippedInstruction = true
                case Some(v) =>
                  if (v < 0) {
                    currentStatement += 1
                    skippedInstruction = true
                  }
              }
            } else {
              throw new RuntimeException(
                s"Cannot QUESTION from empty stack $name"
              )
            }
          case _ =>
            evaluateExpr(operand) match {
              case None =>
                currentStatement += 1
                skippedInstruction = true
              case Some(value) =>
                if (value < 0) {
                  currentStatement += 1
                  skippedInstruction = true
                }
            }
      case TokenType.POP =>
        operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).nonEmpty)
              stacks = stacks.updated(name, stacks(name).tail)
          case _ =>
            throw new RuntimeException("Cannot POP from non-stack expression")

      case TokenType.DUP =>
        operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).nonEmpty)
              stacks = stacks.updated(name, stacks(name).head :: stacks(name))
            else
              stacks = stacks.updated(name, None :: stacks(name))
          case _ =>
            throw new RuntimeException("Cannot DUP from non-stack expression")

      case TokenType.NEG =>
        operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).nonEmpty) {
              stacks(name).head match {
                case Some(value) =>
                  stacks =
                    stacks.updated(name, Some(-value) :: stacks(name).tail)
                case None =>
                  throw new RuntimeException("Cannot negate nil value")
              }
            } else {
              throw new RuntimeException(s"Cannot NEG from empty stack $name")
            }
          case _ =>
            throw new RuntimeException("Cannot NEG from non-stack expression")

      case TokenType.JMP =>
        val offset = operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            val contents = stacks(name)
            stacks = stacks.updated(name, contents.drop(1))
            contents.headOption.flatten
          case _ => evaluateExpr(operand)

        offset match {
          case Some(value) =>
            val jump = value.toInt
            if (jump <= program.statements.length)
              currentStatement = currentStatement + jump - 1
            else
              throw new RuntimeException(s"Invalid jump target: $jump")
          case None =>
            throw new RuntimeException("Cannot jump to nil")
        }

      case TokenType.CMP =>
        operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            val contents = stacks(name)
            // Missing elements read as nil, so both operands are always defined.
            val a = contents.headOption.flatten
            val b = contents.drop(1).headOption.flatten
            stacks = stacks.updated(name, compare(a, b) :: contents.drop(2))
          case _ => throw new RuntimeException("Operand must be a stack")

      case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV |
          TokenType.MOD =>
        operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).size >= 2) {
              (stacks(name)(0), stacks(name)(1)) match {
                case (Some(a), Some(b)) =>
                  // The top of the stack is the left operand.
                  stacks = stacks.updated(
                    name,
                    arithmeticOp(operation, a, b) :: stacks(name).drop(2)
                  )
                case _ =>
                  throw new RuntimeException(
                    "Cannot perform arithmetic on nil values"
                  )
              }
            } else {
              throw new RuntimeException(
                s"Not enough values on stack $name for operation"
              )
            }
          case _ => throw new RuntimeException("Operand must be a stack")

      case _ =>
        throw new RuntimeException(s"Unsupported unary operation: $operation")

  private def executeBinary(
      operation: TokenType,
      left: Expr,
      right: Expr
  ): Unit =
    operation match
      case TokenType.PUSH =>
        left match
          case StackExpr(name) =>
            ensureStackExists(name)
            right match {
              case NilExpr() =>
                stacks = stacks.updated(name, None :: stacks(name))
              case NumberExpr(value) =>
                stacks = stacks.updated(name, Some(value) :: stacks(name))
              case _ =>
                throw new RuntimeException(
                  "Second operand of PUSH must be a literal; use MOV to transfer a value between stacks"
                )
            }
          case _ =>
            throw new RuntimeException("First operand of PUSH must be a stack")

      case TokenType.MOV =>
        (left, right) match
          case (StackExpr(to), StackExpr(from)) =>
            ensureStackExists(to)
            ensureStackExists(from)
            if (stacks(from).nonEmpty) {
              stacks = stacks
                .updated(to, stacks(from).head :: stacks(to))
                .updated(from, stacks(from).tail)
            } else {
              stacks = stacks.updated(to, None :: stacks(to))
            }
          case _ =>
            throw new RuntimeException("Both operands of MOV must be stacks")

      case TokenType.CMP =>
        left match
          case StackExpr(name) =>
            ensureStackExists(name)
            val contents = stacks(name)
            // An empty stack reads as nil, so CMP always has both operands.
            val a = contents.headOption.flatten
            val n = right match {
              case NumberExpr(value) => Some(value)
              case NilExpr()         => None
              case _ =>
                throw new RuntimeException(
                  "Second operand of CMP must be a literal"
                )
            }
            stacks = stacks.updated(name, compare(a, n) :: contents.drop(1))
          case _ => throw new RuntimeException("First operand must be a stack")

      case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV |
          TokenType.MOD =>
        left match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).isEmpty)
              throw new RuntimeException(s"Stack $name is empty")

            val rightValue = right match {
              case NumberExpr(value) => value
              case NilExpr() =>
                throw new RuntimeException(
                  "Cannot perform arithmetic with nil value"
                )
              case _ =>
                throw new RuntimeException(
                  "Second operand of binary arithmetic must be a literal"
                )
            }

            stacks(name).head match {
              case Some(stackValue) =>
                stacks = stacks.updated(
                  name,
                  arithmeticOp(operation, stackValue, rightValue) ::
                    stacks(name).tail
                )
              case None =>
                throw new RuntimeException(
                  "Cannot perform arithmetic on nil value"
                )
            }
          case _ => throw new RuntimeException("First operand must be a stack")

      case _ =>
        throw new RuntimeException(s"Unsupported binary operation: $operation")

  private def evaluateExpr(expr: Expr): Option[Double] = expr match
    case NumberExpr(value) => Some(value)
    case StackExpr(name) =>
      ensureStackExists(name)
      if (stacks(name).nonEmpty) stacks(name).head
      else None
    case NilExpr() => None

  // An undefined result is nil rather than an error, so a program can test for
  // it with CMP and recover.
  private def arithmeticOp(
      op: TokenType,
      a: Double,
      b: Double
  ): Option[Double] =
    op match
      case TokenType.ADD => Some(a + b)
      case TokenType.SUB => Some(a - b)
      case TokenType.MUL => Some(a * b)
      case TokenType.DIV => if (b == 0) None else Some(a / b)
      case TokenType.MOD => if (b == 0) None else Some(a % b)
      case _ =>
        throw new RuntimeException(s"Unsupported arithmetic operation: $op")

  // Two nil operands compare as 1, which is how a program tests for nil.
  // Exactly one nil operand yields nil.
  private def compare(a: Option[Double], b: Option[Double]): Option[Double] =
    (a, b) match
      case (Some(x), Some(y)) =>
        if (x == y) Some(0.0)
        else if (x < y) Some(-1.0)
        else Some(1.0)
      case (None, None) => Some(1.0)
      case _            => None

  def getStacksState(): Map[String, List[Option[Double]]] = stacks

  def getCurrentStatement(): Int = currentStatement

  def getTotalStatements(): Int =
    if (program != null) program.statements.length else 0

  def didSkipInstruction(): Boolean = skippedInstruction
end Interpreter

object InterpreterInstance extends Interpreter
