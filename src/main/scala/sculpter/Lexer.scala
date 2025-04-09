package sculpter

object Lexer:
    var hadError: Boolean = false
    var tokens: List[Token] = List[Token]()
    var start: Int = 0
    var current: Int = 0
    var line: Int = 1
    var source: String = ""


    def apply(pSource: String) =
        hadError = false
        tokens = List[Token]()
        start = 0
        current = 0
        line = 1
        source = pSource
        tokens = scanTokens()
    end apply


    def scanTokens(): List[Token] =
        var tokenList = List[Token]()
        while (!isAtEnd()) {
            start = current
            scanToken()
        }
        // Add EOF token
        val eofToken = new Token(TokenType.EOF, "", null, line)
        tokens = tokens :+ eofToken
        tokens
    end scanTokens

    def scanToken() =
        val c = advance()
        c match
            // Single characters
            case '?' => addToken(TokenType.QUESTION)
            case '-' => addToken(TokenType.NUM_NEG)
            // Comments
            case '/' => if (matchChar('/')) {
                while (peek() != '\n' && !isAtEnd()) advance()
            } else error(line, "Unexpected character.")
            case '\n' => { line += 1
                addToken(TokenType.ENTER) }
            // Empty characters
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
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'
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
        val token = new Token(tokenType, text, literal, line)
        tokens = tokens :+ token
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
        hadError = true
    end report

    def error (token: Token, message: String) =
        if (token.tokenType == TokenType.EOF) {
            report(token.line, message, "at end")
        } else {
            report(token.line, message, "at '" + token.lexeme + "'")
        }
    end error

    def printResult() =
        for (token <- tokens) {
            println(token)
        }
    end printResult
end Lexer