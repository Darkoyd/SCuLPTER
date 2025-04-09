package sculpter

class ParseError extends RuntimeException

object Parser:
    var tokens: List[Token] = List[Token]()
    var current: Int = 0

    def apply(pTokens: List[Token]): Unit =
        tokens = pTokens
        current = 0
    end apply



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
        return tokens(current).tokenType == TokenType.EOF
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
                    // Do nothing
            end match
            advance()
        }
    end synchronize


end Parser