package livechart

enum TokenType:
    // Single characters
    case QUESTION
    // Flow Control
    case JMP, CMP
    // Stack Manipulation
    case PUSH, POP, DUP, MOV
    // Arithmetic
    case ADD, SUB, MUL, DIV, MOD, NEG
    // Literals
    case NUMBER, STACK
    // Keywords
    case NIL
    // End of File
    case EOF


class Token(pTokenType: TokenType, pLexeme: String, pLiteral: Any, pLine: Int) {
    var tokenType: TokenType = pTokenType
    var lexeme: String = pLexeme
    var literal: Any = pLiteral
    var line: Int = pLine

    override def toString(): String =
        s"$this.tokenType $this.lexeme $this.literal"
    end toString
}



object Lexer:
    var hadError: Boolean = false
    var tokens: List[Token] = List[Token]()
    var start: Int = 0
    var current: Int = 0
    var line: Int = 1
    var source: String = ""


    def apply(pSource: String) =
        hadError = false
        tokens = scanTokens()
        start = 0
        current = 0
        line = 1
        source = pSource
    end apply


    def scanTokens(): List[Token] =
        while (!isAtEnd()) {
            start = current
            scanToken()
        }
        tokens :+ Token(TokenType.EOF, "", null, line)
        tokens
    end scanTokens

    def scanToken() =
        val c = advance()
        c match
            case '?' => addToken(TokenType.QUESTION)
            case _: Char => error(line, "Unexpected character.")
        end match
    end scanToken

    def peek(): Char =
        if (isAtEnd()) '\0'
        else source.charAt(current)
    end peek

    def advance(): Char =
        current += 1
        source.charAt(current - 1)
    end advance

    def addToken(tokenType: TokenType, literal: Any): Unit =
        val text = source.substring(start, current)
        tokens :+ new Token(tokenType, text, literal, line)
    end addToken

    def addToken(tokenType: TokenType): Unit =
        addToken(tokenType, null)
    end addToken

    def isAtEnd(): Boolean =
        current >= source.length
    end isAtEnd


    def error (line: Int, message: String) =
        report(line, message, "")
    end error

    def report (line: Int, message: String, where: String) = 
        println(s"[line $line] Error $where: $message")
    end report
end Lexer

