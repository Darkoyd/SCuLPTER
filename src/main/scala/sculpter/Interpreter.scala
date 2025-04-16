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
  private var stacks: Map[String, List[Double]] = Map().withDefaultValue(List())
  
  // Execution history for step back functionality
  private var history: List[Map[String, List[Double]]] = List()
  
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
  
  // Execute a unary statement
  private def executeUnary(operation: TokenType, operand: Expr): Unit = 
    operation match
      case TokenType.QUESTION =>
        // Conditional execution - if value > 0, execute next instruction, otherwise skip it
        val value = evaluateExpr(operand)
        if (value <= 0) {
          // Skip the next instruction by incrementing currentStatement
          currentStatement += 1
          skippedInstruction = true
        }
        
      case TokenType.POP =>
        // Remove top value from a stack
        operand match
          case StackExpr(name) => 
            if (stacks(name).nonEmpty)
              stacks = stacks.updated(name, stacks(name).tail)
          case _ => throw new RuntimeException("Cannot POP from non-stack expression")
          
      case TokenType.DUP =>
        // Duplicate top value on a stack
        operand match
          case StackExpr(name) => 
            if (stacks(name).nonEmpty)
              stacks = stacks.updated(name, stacks(name).head :: stacks(name))
          case _ => throw new RuntimeException("Cannot DUP from non-stack expression")
          
      case TokenType.NEG =>
        // Negate top value on a stack
        operand match
          case StackExpr(name) => 
            if (stacks(name).nonEmpty)
              stacks = stacks.updated(name, -stacks(name).head :: stacks(name).tail)
          case _ => throw new RuntimeException("Cannot NEG from non-stack expression")
          
      case TokenType.JMP =>
        // Jump to a specific statement (line)
        val jumpTo = evaluateExpr(operand).toInt
        if (jumpTo >= 0 && jumpTo <= program.statements.length)
          // Adjust because we'll increment currentStatement after execution
          currentStatement = jumpTo - 1
        else
          throw new RuntimeException(s"Invalid jump target: $jumpTo")
          
      case TokenType.CMP =>
        // Compare top two values on a stack - pushes 1 if true, 0 if false
        operand match
          case StackExpr(name) => 
            if (stacks(name).size >= 2)
              val result = if (stacks(name)(0) == stacks(name)(1)) 1.0 else 0.0
              stacks = stacks.updated(name, result :: stacks(name).drop(2))
            else
              throw new RuntimeException("Not enough values on stack for CMP")
          case _ => throw new RuntimeException("Cannot CMP on non-stack expression")
      
      // Handle arithmetic operations in unary form (operating on top two values of a stack)
      case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV | TokenType.MOD =>
        operand match
          case StackExpr(name) =>
            if (stacks(name).size >= 2) {
              val b = stacks(name)(0) // First popped value (top of stack)
              val a = stacks(name)(1) // Second popped value
              val result = arithmeticOp(operation, a, b)
              // Replace the top two values with the result
              stacks = stacks.updated(name, result :: stacks(name).drop(2))
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
            val value = evaluateExpr(right)
            stacks = stacks.updated(name, value :: stacks(name))
          case _ => throw new RuntimeException("First operand of PUSH must be a stack")
          
      case TokenType.MOV =>
        // Move a value from one stack to another
        (left, right) match
          case (StackExpr(to), StackExpr(from)) =>
            if (stacks(from).nonEmpty)
              stacks = stacks
                .updated(to, stacks(from).head :: stacks(to))
                .updated(from, stacks(from).tail)
            else
              throw new RuntimeException(s"Stack $from is empty")
          case _ => throw new RuntimeException("Both operands of MOV must be stacks")
          
      // Arithmetic operations in binary form (operating on stack top value and a literal)
      case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV | TokenType.MOD =>
        left match
          case StackExpr(name) =>
            if (stacks(name).nonEmpty) {
              val stackValue = stacks(name).head
              
              // Ensure right expression is a number
              right match
                case NumberExpr(rightValue) =>
                  val result = arithmeticOp(operation, stackValue, rightValue)
                  // Replace the top value with the result
                  stacks = stacks.updated(name, result :: stacks(name).tail)
                case _ => 
                  throw new RuntimeException("Second operand of binary arithmetic must be a number")
            } else {
              throw new RuntimeException(s"Stack $name is empty")
            }
          case _ => throw new RuntimeException("First operand must be a stack")
          
      case _ => throw new RuntimeException(s"Unsupported binary operation: $operation")
  
  // Evaluate an expression to get its value
  private def evaluateExpr(expr: Expr): Double = expr match
    case NumberExpr(value) => value
    case StackExpr(name) => 
      if (stacks(name).nonEmpty) stacks(name).head 
      else throw new RuntimeException(s"Stack $name is empty")
    case NilExpr() => 0.0
  
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
  def getStacksState(): Map[String, List[Double]] = stacks
  
  // Get the current position in the program
  def getCurrentStatement(): Int = currentStatement
  
  // Get the total number of statements
  def getTotalStatements(): Int = if (program != null) program.statements.length else 0
  
  // Check if an instruction was skipped due to QUESTION operation
  def didSkipInstruction(): Boolean = skippedInstruction
end Interpreter

// Singleton object to be used in the UI
object InterpreterInstance extends Interpreter