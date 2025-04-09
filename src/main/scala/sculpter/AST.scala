package sculpter

sealed trait ASTNode

case class Program(statements: List[Statement]) extends ASTNode

sealed trait Statement extends ASTNode

case class UnaryStatement(operation: TokenType, operand: Expr) extends Statement

case class BinaryStatement(operation: TokenType, left: Expr, right: Expr) extends Statement

sealed trait Expr extends ASTNode

case class StackExpr(name: String) extends Expr

case class NumberExpr(value: Double) extends Expr {
  def isNegative: Boolean = value < 0
}

case class NilExpr() extends Expr

object AST {
  def createStackExpr(token: Token): StackExpr = StackExpr(token.lexeme)
  
  def createNumberExpr(token: Token): NumberExpr = 
    if (token.literal != null) NumberExpr(token.literal.asInstanceOf[Double])
    else NumberExpr(token.lexeme.toDouble)
  
  def createNilExpr(): NilExpr = NilExpr()
  
  def createUnaryStmt(operation: TokenType, operand: Expr): UnaryStatement =
    UnaryStatement(operation, operand)
    
  def createBinaryStmt(operation: TokenType, left: Expr, right: Expr): BinaryStatement =
    BinaryStatement(operation, left, right)
}