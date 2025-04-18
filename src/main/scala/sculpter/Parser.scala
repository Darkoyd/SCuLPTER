package sculpter

class ParseError extends RuntimeException

object Parser:
  var tokens: List[Token] = List[Token]()
  var current: Int = 0
  
  def apply(pTokens: List[Token]): Program =
    tokens = pTokens
    current = 0
    program()
  end apply
  
  def program(): Program =
    var statements = List[Statement]()
    
    while (!isAtEnd()) {
      try {
        val stmt = statement()
        statements = statements :+ stmt
      } catch {
        case e: ParseError => 
          synchronize()
      }
    }
    
    Program(statements)
  end program
  
  def statement(): Statement =
    val token = peek()
    token.tokenType match

      
      case TokenType.ADD | TokenType.SUB | TokenType.MUL | 
           TokenType.DIV | TokenType.MOD | TokenType.CMP =>
        
        val operator = advance()
        val first = expression()
        
        if (!check(TokenType.ENTER) && !check(TokenType.EOF)) {
          val second = if (peek().tokenType == TokenType.NUMBER || 
                           peek().tokenType == TokenType.NUM_NEG || 
                           peek().tokenType == TokenType.NIL) {
            expression()
          } else {
            throw error(peek(), "Second argument of binary arithmetic must be a number or nil")
          }
          
          if (check(TokenType.ENTER)) advance()
          AST.createBinaryStmt(operator.tokenType, first, second)
        } else {
          if (check(TokenType.ENTER)) advance()
          AST.createUnaryStmt(operator.tokenType, first)
        }
      
      case TokenType.PUSH | TokenType.MOV =>
        binary()
        
      case TokenType.QUESTION | TokenType.JMP |
           TokenType.POP | TokenType.DUP | TokenType.NEG =>
        unary()
        
      case _ =>
        throw error(token, s"Expected statement, got ${token.tokenType}")
  end statement
  
  def unary(): UnaryStatement =
    val operator = advance()
    val operand = expression()
    
    if (check(TokenType.ENTER)) advance()
    
    AST.createUnaryStmt(operator.tokenType, operand)
  end unary
  
  def binary(): BinaryStatement =
    val operator = advance()
    val left = expression()
    val right = expression()
    
    if (check(TokenType.ENTER)) advance()
    
    AST.createBinaryStmt(operator.tokenType, left, right)
  end binary
  
  def expression(): Expr =
    if (matchToken(TokenType.NUM_NEG)) {
      val numToken = consume(TokenType.NUMBER, "Expected number after '-'")
      val value = -numToken.literal.asInstanceOf[Double]
      val token = Token(TokenType.NUMBER, "-" + numToken.lexeme, value, numToken.line)
      AST.createNumberExpr(token)
    } else if (matchToken(TokenType.NIL)) {
      AST.createNilExpr()
    } else if (matchToken(TokenType.NUMBER)) {
      AST.createNumberExpr(previous())
    } else if (matchToken(TokenType.STACK)) {
      AST.createStackExpr(previous())
    } else {
      throw error(peek(), "Expected expression")
    }
  end expression

  def matchToken(tokenType: TokenType): Boolean =
    if (check(tokenType)) {
      advance()
      true
    } else {
      false
    }
  end matchToken

  def check(tokenType: TokenType): Boolean =
    if (isAtEnd()) {
      false
    } else {
      peek().tokenType == tokenType
    }
  end check

  def advance(): Token =
    if (!isAtEnd()) {
      current += 1
    }
    return previous()
  end advance

  def isAtEnd(): Boolean =
    return peek().tokenType == TokenType.EOF
  end isAtEnd

  def peek(): Token =
    tokens(current)
  end peek

  def previous(): Token =
    tokens(current - 1)
  end previous

  def consume(tokenType: TokenType, message: String): Token =
    if (check(tokenType)) {
      return advance()
    }
    throw error(peek(), message)
  end consume

  def error(token: Token, message: String): ParseError =
    Lexer.error(token, message)
    return ParseError()
  end error

  def synchronize(): Unit =
    advance()
    while (!isAtEnd()) {
      if (previous().tokenType == TokenType.ENTER) {
        return
      }
      val t = peek().tokenType

      t match 
        case TokenType.ADD | TokenType.SUB | TokenType.MUL | TokenType.DIV | TokenType.MOD | TokenType.NEG
        | TokenType.DUP | TokenType.POP | TokenType.MOV | TokenType.PUSH 
        | TokenType.QUESTION | TokenType.JMP | TokenType.CMP =>
          return
        case _ =>
      end match
      advance()
    }
  end synchronize
end Parser