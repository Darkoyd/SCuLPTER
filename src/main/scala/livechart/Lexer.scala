package livechart

enum TokenType:
    // Single characters
    case QUESTION
    case NUM_NEG
    // Flow Control
    case JMP
    case CMP
    // Stack Manipulation
    case PUSH
    case POP
    case DUP
    case MOV
    // Arithmetic
    case ADD
    case SUB
    case MUL
    case DIV
    case MOD
    case NEG
    // Literals
    case NUMBER
    case STACK
    // Keywords
    case NIL
    // End of File
    case EOF

val keywords: Map[String, TokenType] = Map(
    "nil" -> TokenType.NIL,
    "jmp" -> TokenType.JMP,
    "cmp" -> TokenType.CMP,
    "push" -> TokenType.PUSH,
    "pop" -> TokenType.POP,
    "dup" -> TokenType.DUP,
    "mov" -> TokenType.MOV,
    "add" -> TokenType.ADD,
    "sub" -> TokenType.SUB,
    "mul" -> TokenType.MUL,
    "div" -> TokenType.DIV,
    "mod" -> TokenType.MOD,
    "neg" -> TokenType.NEG,
    "NIL" -> TokenType.NIL,
    "JMP" -> TokenType.JMP,
    "CMP" -> TokenType.CMP,
    "PUSH" -> TokenType.PUSH,
    "POP" -> TokenType.POP,
    "DUP" -> TokenType.DUP,
    "MOV" -> TokenType.MOV,
    "ADD" -> TokenType.ADD,
    "SUB" -> TokenType.SUB,
    "MUL" -> TokenType.MUL,
    "DIV" -> TokenType.DIV,
    "MOD" -> TokenType.MOD,
    "NEG" -> TokenType.NEG
)


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
            // Single characters
            case '?' => addToken(TokenType.QUESTION)
            // Comments
            case '/' => if (matchChar('/')) {
                while (peek() != '\n' && !isAtEnd()) advance()
            } else error(line, "Unexpected character.")
            // Empty characters
            case '\n' => line += 1
            case ' ' => ()
            case '\r' => ()
            case '\t' => ()

            case _:Char => if (isDigit(c)) {
                    number()
                } else if (isAlpha(c)) {
                identifier()
                } else error(line, "Unexpected character.")


        end match
    end scanToken

    def isAlpha(c: Char): Boolean =
        return (c >= 'a' && c >= 'z') || (c >= 'A' && c >= 'Z') || c == '_'
    end isAlpha

    def isAlphanumeric(c: Char): Boolean =
        return isAlpha(c) || isDigit(c)
    end isAlphanumeric

    def identifier() =
        while (isAlphanumeric(peek())) advance()

        val text = source.substring(start, current)
        val tokenType = keywords.getOrElse(text, TokenType.STACK)
        addToken(tokenType)
    end identifier

    def number() =
        while (isDigit(peek())) advance()
        if (peek() == '.' && isDigit(peekNext())) {
            advance()
            while (isDigit(peek())) advance()
        }
        addToken(TokenType.NUMBER, source.substring(start, current).toDouble)
    end number

    def peekNext(): Char =
        if (current + 1 >= source.length) '\u0000'
        else source.charAt(current + 1)
    end peekNext

    def isDigit(c: Char): Boolean =
        c >= '0' && c <= '9'
    end isDigit

    def matchChar (expected: Char): Boolean =
        if (isAtEnd()) return false
        if (source.charAt(current) != expected) return false
        current += 1
        true
    end matchChar

    def peek(): Char =
        if (isAtEnd()) '\u0000'
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

    def printResult() =
        for (token <- tokens) {
            println(token)
        }
    end printResult
end Lexer

