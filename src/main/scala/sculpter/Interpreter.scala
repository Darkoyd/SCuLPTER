package sculpter

/**
 * Interpreter for the SCuLPT language
 * 
 * SCuLPT is a stack-based language with operations for manipulating stacks
 * and performing arithmetic. The language supports the following operations:
 * 
 * Stack Operations:
 * - PUSH stack value: Push a value onto a stack
 * - POP stack: Remove the top value from a stack
 * - DUP stack: Duplicate the top value on a stack
 * - MOV target source: Move top value from source stack to target stack
 * 
 * Arithmetic Operations (two forms):
 * - Form 1 (unary): ADD stack - Add top two values on the stack, push result back
 * - Form 2 (binary): ADD stack value - Add value to the top value on the stack, push result back
 * 
 * Similar behavior for SUB, MUL, DIV, MOD
 * - NEG stack: Negate the top value on a stack
 * 
 * Control Flow:
 * - JMP value: Jump to statement at index value
 * - CMP stack: Compare top two values on stack, push 1 if equal or 0 if different
 * - QUESTION expr: Conditional execution - if expr > 0, execute next instruction, otherwise skip it
 */
class Interpreter:
  // Stack state storage - maps stack names to their values
  // We'll use Option[Double] to represent values, where None represents nil
  private var stacks: Map[String, List[Option[Double]]] = Map().withDefaultValue(List())
  
  // Execution history for step back functionality
  private var history: List[Map[String, List[Option[Double]]]] = List()
  
  // Current position in the program
  private var currentStatement: Int = 0
  
  // The program being executed
  private var program: Program = _
  
  // Flag to track if an instruction was skipped due to QUESTION operation
  private var skippedInstruction: Boolean = false
  
  // Resets the interpreter state
  def reset(program: Program): Unit =
    this.program = program
    stacks = Map().withDefaultValue(List())
    history = List()
    currentStatement = 0
    skippedInstruction = false
  
  // Step forward in execution
  def stepForward(): Boolean =
    if (currentStatement >= program.statements.length) 
      return false
      
    // Reset the skipped instruction flag
    skippedInstruction = false
      
    // Save current state for step back
    history = history :+ stacks

    // Execute the next statement
    execute(program.statements(currentStatement))
    currentStatement += 1
    
    true
  
  // Step backward in execution
  def stepBackward(): Boolean =
    if (history.isEmpty) 
      return false
      
    // Restore previous state
    stacks = history.last
    history = history.dropRight(1)
    currentStatement -= 1
    
    true
  
  // Execute a statement
  private def execute(statement: Statement): Unit = statement match
    case UnaryStatement(operation, operand) => executeUnary(operation, operand)
    case BinaryStatement(operation, left, right) => executeBinary(operation, left, right)
  
  // Helper to ensure a stack exists in the map
  private def ensureStackExists(name: String): Unit =
    if (!stacks.contains(name)) {
      stacks = stacks.updated(name, List())
    }
  
  // Execute a unary statement
  private def executeUnary(operation: TokenType, operand: Expr): Unit = 
    operation match
      case TokenType.QUESTION =>
        // Conditional execution - if value > 0, execute next instruction, otherwise skip it
        evaluateExpr(operand) match {
          case None => 
            // Nil values cause skip
            currentStatement += 1
            skippedInstruction = true
          case Some(value) =>
            if (value <= 0) {
              // Skip the next instruction by incrementing currentStatement
              currentStatement += 1
              skippedInstruction = true
            }
        }
        
      case TokenType.POP =>
        // Remove top value from a stack
        operand match
          case StackExpr(name) => 
            ensureStackExists(name)
            if (stacks(name).nonEmpty)
              stacks = stacks.updated(name, stacks(name).tail)
          case _ => throw new RuntimeException("Cannot POP from non-stack expression")
          
      case TokenType.DUP =>
        // Duplicate top value on a stack
        operand match
          case StackExpr(name) => 
            ensureStackExists(name)
            if (stacks(name).nonEmpty)
              stacks = stacks.updated(name, stacks(name).head :: stacks(name))
            else
              // Empty stack returns nil
              stacks = stacks.updated(name, None :: stacks(name))
          case _ => throw new RuntimeException("Cannot DUP from non-stack expression")
          
      case TokenType.NEG =>
        // Negate top value on a stack
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
        // Jump to a specific statement (line)
        evaluateExpr(operand) match {
          case Some(value) =>
            val jumpTo = value.toInt
            if (jumpTo >= 0 && jumpTo <= program.statements.length)
              // Adjust because we'll increment currentStatement after execution
              currentStatement = jumpTo - 1
            else
              throw new RuntimeException(s"Invalid jump target: $jumpTo")
          case None =>
            throw new RuntimeException("Cannot jump to nil")
        }
          
      case TokenType.CMP =>
        // Compare top value on stack with nil or compare top two values
        operand match
          case StackExpr(name) => 
            ensureStackExists(name)
            
            // Special handling for nil comparison
            if (stacks(name).isEmpty) {
              // Empty stack is treated as nil, so comparing with nil gives true (1)
              stacks = stacks.updated(name, Some(1.0) :: stacks(name))
            } else if (stacks(name).head == None) {
              // Top value is nil, so it equals nil
              stacks = stacks.updated(name, Some(1.0) :: stacks(name).tail)
            } else if (stacks(name).size >= 2) {
              // Compare top two values
              (stacks(name)(0), stacks(name)(1)) match {
                case (Some(a), Some(b)) =>
                  val result = if (a == b) 1.0 else 0.0
                  stacks = stacks.updated(name, Some(result) :: stacks(name).drop(2))
                case (None, None) =>
                  // Two nils are equal
                  stacks = stacks.updated(name, Some(1.0) :: stacks(name).drop(2))
                case _ =>
                  // One nil and one non-nil are not equal
                  stacks = stacks.updated(name, Some(0.0) :: stacks(name).drop(2))
              }
            } else {
              throw new RuntimeException("Not enough values on stack for CMP")
            }
          case _ => throw new RuntimeException("Cannot CMP on non-stack expression")
      
      // Handle arithmetic operations in unary form (operating on top two values of a stack)
      case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV | TokenType.MOD =>
        operand match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).size >= 2) {
              (stacks(name)(0), stacks(name)(1)) match {
                case (Some(b), Some(a)) =>
                  val result = arithmeticOp(operation, a, b)
                  stacks = stacks.updated(name, Some(result) :: stacks(name).drop(2))
                case _ =>
                  throw new RuntimeException("Cannot perform arithmetic on nil values")
              }
            } else {
              throw new RuntimeException(s"Not enough values on stack $name for operation")
            }
          case _ => throw new RuntimeException("Operand must be a stack")
          
      case _ => throw new RuntimeException(s"Unsupported unary operation: $operation")
  
  // Execute a binary statement
  private def executeBinary(operation: TokenType, left: Expr, right: Expr): Unit =
    operation match
      case TokenType.PUSH =>
        // Push a value onto a stack
        left match
          case StackExpr(name) => 
            ensureStackExists(name)
            right match {
              case NilExpr() =>
                // Push nil (None) onto the stack
                stacks = stacks.updated(name, None :: stacks(name))
              case _ =>
                // Push regular value
                evaluateExpr(right) match {
                  case Some(value) => 
                    stacks = stacks.updated(name, Some(value) :: stacks(name))
                  case None => 
                    // This happens when evaluating a nil value expression
                    stacks = stacks.updated(name, None :: stacks(name))
                }
            }
          case _ => throw new RuntimeException("First operand of PUSH must be a stack")
          
      case TokenType.MOV =>
        // Move a value from one stack to another
        (left, right) match
          case (StackExpr(to), StackExpr(from)) =>
            ensureStackExists(to)
            ensureStackExists(from)
            if (stacks(from).nonEmpty) {
              // Move the value
              stacks = stacks
                .updated(to, stacks(from).head :: stacks(to))
                .updated(from, stacks(from).tail)
            } else {
              // If source stack is empty, move nil
              stacks = stacks.updated(to, None :: stacks(to))
            }
          case _ => throw new RuntimeException("Both operands of MOV must be stacks")
          
      // Arithmetic operations in binary form (operating on stack top value and a literal)
      case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV | TokenType.MOD =>
        left match
          case StackExpr(name) =>
            ensureStackExists(name)
            if (stacks(name).nonEmpty) {
              stacks(name).head match {
                case Some(stackValue) =>
                  // Ensure right expression is a number
                  right match {
                    case NumberExpr(rightValue) =>
                      val result = arithmeticOp(operation, stackValue, rightValue)
                      // Replace the top value with the result
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
                  throw new RuntimeException("Cannot perform arithmetic on nil value")
              }
            } else {
              throw new RuntimeException(s"Stack $name is empty")
            }
          case _ => throw new RuntimeException("First operand must be a stack")
          
      case _ => throw new RuntimeException(s"Unsupported binary operation: $operation")
  
  // Evaluate an expression to get its value
  private def evaluateExpr(expr: Expr): Option[Double] = expr match
    case NumberExpr(value) => Some(value)
    case StackExpr(name) => 
      ensureStackExists(name)
      if (stacks(name).nonEmpty) stacks(name).head 
      else None  // Empty stack treated as nil
    case NilExpr() => None
  
  // Perform arithmetic operations
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
    case _ => throw new RuntimeException(s"Unsupported arithmetic operation: $op")
  
  // Get the current state of all stacks
  def getStacksState(): Map[String, List[Option[Double]]] = stacks
  
  // Get the current position in the program
  def getCurrentStatement(): Int = currentStatement
  
  // Get the total number of statements
  def getTotalStatements(): Int = if (program != null) program.statements.length else 0
  
  // Check if an instruction was skipped due to QUESTION operation
  def didSkipInstruction(): Boolean = skippedInstruction
end Interpreter

// Singleton object to be used in the UI
object InterpreterInstance extends Interpreter