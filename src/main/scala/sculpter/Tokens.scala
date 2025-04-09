package sculpter

enum TokenType:
    // Single characters
    case NUM_NEG
    case ENTER
    // Flow Control
    case QUESTION
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
        s"$tokenType $lexeme $literal"
    end toString
}

enum Arit:
    case ADD
    case SUB
    case MUL
    case DIV
    case MOD
    case NEG
end Arit

enum UnaryTokens:
    case NEG
    case JMP
    case CMP
    case POP
    case DUP
    case QUESTION
end UnaryTokens

enum BinaryTokens:
    case CMP
    case PUSH
    case MOV
end BinaryTokens
