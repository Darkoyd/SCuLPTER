package sculpter

class Interpreter:

  private var stacks: Map[String, List[Option[Double]]] = Map().withDefaultValue(List())
  
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
    case BinaryStatement(operation, left, right) => executeBinary(operation, left, right)
  
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
                      if (v <= 0) {
                        currentStatement += 1
                        skippedInstruction = true
                      }
                  }
                } else {
                  throw new RuntimeException(s"Cannot QUESTION from empty stack $name")
                }
            case _ => 
                evaluateExpr(operand) match {
                  case None => 
                    currentStatement += 1
                    skippedInstruction = true
                  case Some(value) =>
                    if (value <= 0) {
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
          case _ => throw new RuntimeException("Cannot POP from non-stack expression")
          
      case TokenType.DUP =>
        operand match
          case StackExpr(name) => 
            ensureStackExists(name)
            if (stacks(name).nonEmpty)
              stacks = stacks.updated(name, stacks(name).head :: stacks(name))
            else
              stacks = stacks.updated(name, None :: stacks(name))
          case _ => throw new RuntimeException("Cannot DUP from non-stack expression")
          
      case TokenType.NEG =>
        operand match
          case StackExpr(name) => 
            ensureStackExists(name)
            if (stacks(name).nonEmpty) {
              stacks(name).head match {
                case Some(value) =>
                  stacks = stacks.updated(name, Some(-value) :: stacks(name).tail)
                case None =>
                  throw new RuntimeException("Cannot negate nil value")
              }
            } else {
              throw new RuntimeException(s"Cannot NEG from empty stack $name")
            }
          case _ => throw new RuntimeException("Cannot NEG from non-stack expression")
          
      case TokenType.JMP =>
        evaluateExpr(operand) match {
          case Some(value) =>
            val jump = value.toInt
            if (jump <= program.statements.length)
              currentStatement = currentStatement + jump - 1
            else
              throw new RuntimeException(s"Invalid jump target: $jump")
          case None =>
            throw new RuntimeException("Cannot jump to nil")
        }
          
      
      case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV | TokenType.MOD | TokenType.CMP =>
        operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).size >= 2) {
              (stacks(name)(0), stacks(name)(1)) match {
                case (Some(b), Some(a)) =>
                  val result = arithmeticOp(operation, b, a)
                  stacks = stacks.updated(name, Some(result) :: stacks(name).drop(2))
                case (None, None) =>
                    operation match
                        case TokenType.CMP =>
                            stacks = stacks.updated(name, Some(0.0) :: stacks(name).drop(2))
                        case _ =>
                            throw new RuntimeException("Cannot perform arithmetic on nil values")
                case _ =>
                  throw new RuntimeException("Cannot perform arithmetic on nil values")
              }
            } else {
              throw new RuntimeException(s"Not enough values on stack $name for operation")
            }
          case _ => throw new RuntimeException("Operand must be a stack")
          
      case _ => throw new RuntimeException(s"Unsupported unary operation: $operation")
  
  private def executeBinary(operation: TokenType, left: Expr, right: Expr): Unit =
    operation match
      case TokenType.PUSH =>
        left match
          case StackExpr(name) => 
            ensureStackExists(name)
            right match {
              case NilExpr() =>
                stacks = stacks.updated(name, None :: stacks(name))
              case _ =>
                evaluateExpr(right) match {
                  case Some(value) => 
                    stacks = stacks.updated(name, Some(value) :: stacks(name))
                  case None => 
                    stacks = stacks.updated(name, None :: stacks(name))
                }
            }
          case _ => throw new RuntimeException("First operand of PUSH must be a stack")
          
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
          case _ => throw new RuntimeException("Both operands of MOV must be stacks")
          
      case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV | TokenType.MOD | TokenType.CMP =>
        left match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).nonEmpty) {
              stacks(name).head match {
                case Some(stackValue) =>
                  right match {
                    case NumberExpr(rightValue) =>
                      val result = arithmeticOp(operation, stackValue, rightValue)
                      stacks = stacks.updated(name, Some(result) :: stacks(name).tail)
                    case NilExpr() =>
                      throw new RuntimeException("Cannot perform arithmetic with nil value")
                    case _ => 
                      evaluateExpr(right) match {
                        case Some(value) =>
                          val result = arithmeticOp(operation, stackValue, value)
                          stacks = stacks.updated(name, Some(result) :: stacks(name).tail)
                        case None =>
                          throw new RuntimeException("Cannot perform arithmetic with nil value")
                      }
                  }
                case None =>
                  operation match
                    case TokenType.CMP =>
                      right match
                        case NilExpr() =>
                          stacks = stacks.updated(name, Some(0.0) :: stacks(name).tail)
                        case _ =>
                            throw new RuntimeException("Cannot perform arithmetic on nil value")
                      
                    case _ => throw new RuntimeException("Cannot perform arithmetic on nil value")
              }
            } else {
              throw new RuntimeException(s"Stack $name is empty")
            }
          case _ => throw new RuntimeException("First operand must be a stack")
          
      case _ => throw new RuntimeException(s"Unsupported binary operation: $operation")
  
  private def evaluateExpr(expr: Expr): Option[Double] = expr match
    case NumberExpr(value) => Some(value)
    case StackExpr(name) => 
      ensureStackExists(name)
      if (stacks(name).nonEmpty) stacks(name).head 
      else None
    case NilExpr() => None
  
  private def arithmeticOp(op: TokenType, a: Double, b: Double): Double = op match
    case TokenType.ADD => a + b
    case TokenType.SUB => a - b
    case TokenType.MUL => a * b
    case TokenType.DIV => 
      if (b == 0) throw new RuntimeException("Division by zero")
      else a / b
    case TokenType.MOD => 
      if (b == 0) throw new RuntimeException("Modulo by zero")
      else a % b
    case TokenType.CMP =>
      if (a < b) -1.0
      else 1.0
    case _ => throw new RuntimeException(s"Unsupported arithmetic operation: $op")
  
  def getStacksState(): Map[String, List[Option[Double]]] = stacks
  
  def getCurrentStatement(): Int = currentStatement
  
  def getTotalStatements(): Int = if (program != null) program.statements.length else 0
  
  def didSkipInstruction(): Boolean = skippedInstruction
end Interpreter

object InterpreterInstance extends Interpreter