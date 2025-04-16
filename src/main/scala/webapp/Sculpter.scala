package webapp

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

import sculpter.{Lexer, Parser, Program, UnaryStatement, BinaryStatement, StackExpr, NumberExpr, NilExpr}

@main
def Sculpter(): Unit =
    renderOnDomContentLoaded(dom.document.getElementById("app"), TextArea.appElement())

object TextArea:
    def appElement(): Element =
        val textVar = Var("")
        val lexerOutputVar = Var[List[String]](List())
        val parserOutputVar = Var[String]("")
        val hasParseError = Var(false)

        val clearText = Observer[Any](_ => textVar.set(""))
        val clearOutput = Observer[Any](_ => {
            lexerOutputVar.set(List())
            parserOutputVar.set("")
            hasParseError.set(false)
        })

        // Helper to format AST node for display
        def formatASTNode(node: Any, indent: Int = 0): String = {
            val indentStr = "  " * indent
            node match {
                case Program(statements) =>
                    s"${indentStr}Program\n" + statements.map(s => formatASTNode(s, indent + 1)).mkString("\n")
                    
                case UnaryStatement(op, expr) =>
                    s"${indentStr}UnaryStatement: $op\n" + formatASTNode(expr, indent + 1)
                    
                case BinaryStatement(op, left, right) =>
                    s"${indentStr}BinaryStatement: $op\n" + 
                    formatASTNode(left, indent + 1) + "\n" + 
                    formatASTNode(right, indent + 1)
                    
                case StackExpr(name) =>
                    s"${indentStr}Stack: $name"
                    
                case NumberExpr(value) =>
                    s"${indentStr}Number: $value"
                    
                case NilExpr() =>
                    s"${indentStr}Nil"
                    
                case other =>
                    s"${indentStr}Unknown node: $other"
            }
        }

        div(
            h1("SCuLPT IDE"),
            
            div(
                className := "editor-container",
                div(
                    className := "input-section",
                    h3("Input"),
                    textArea(
                        typ := "text",
                        placeholder := "Type your SCuLPT code here...",
                        rows := 15,
                        cols := 60,
                        value <-- textVar.signal, 
                        onInput.mapToValue --> textVar
                    ),
                    
                    div(className := "button-row",
                        button(
                            "Clear",
                            onClick --> Observer[Any](_ => {
                                clearText.onNext(())
                                clearOutput.onNext(())
                            })
                        ),
                        
                        button(
                            "Lex",
                            onClick --> Observer[Any](_ => {
                                // Reset the lexer state
                                Lexer.hadError = false
                                Lexer.tokens = List()
                                Lexer.start = 0
                                Lexer.current = 0
                                Lexer.line = 1
                                Lexer.source = textVar.now()
                                
                                // Run the lexer
                                val tokens = Lexer.scanTokens()
                                
                                // Convert tokens to string representation for display
                                val tokenStrings = tokens.map(token => 
                                    s"Line ${token.line}: ${token.tokenType} '${token.lexeme}' ${if (token.literal != null) token.literal else ""}"
                                )
                                
                                // Update the output variable
                                lexerOutputVar.set(tokenStrings)
                                hasParseError.set(false)
                                parserOutputVar.set("")
                            })
                        ),
                        
                        button(
                            "Parse",
                            onClick --> Observer[Any](_ => {
                                // First run the lexer
                                Lexer.hadError = false
                                Lexer.tokens = List()
                                Lexer.start = 0
                                Lexer.current = 0
                                Lexer.line = 1
                                Lexer.source = textVar.now()
                                
                                val tokens = Lexer.scanTokens()
                                
                                // Then run the parser
                                try {
                                    val ast = Parser(tokens)
                                    parserOutputVar.set(formatASTNode(ast))
                                    hasParseError.set(false)
                                } catch {
                                    case e: Exception =>
                                        parserOutputVar.set(s"Parse error: ${e.getMessage()}")
                                        hasParseError.set(true)
                                }
                            })
                        )
                    )
                ),
                
                div(
                    className := "output-container",
                    div(
                        className := "output-section",
                        h3("Lexer Output"),
                        div(
                            className := "lexer-output",
                            height := "200px",
                            overflowY := "auto",
                            padding := "10px",
                            border := "1px solid #ccc",
                            backgroundColor := "#f5f5f5",
                            fontFamily := "monospace",
                            
                            // Display each token on a new line
                            children <-- lexerOutputVar.signal.map(tokens => 
                                if (tokens.isEmpty) 
                                    List(p("No tokens to display. Enter some code and click 'Lex'."))
                                else 
                                    tokens.map(tokenStr => div(tokenStr))
                            )
                        )
                    ),
                    
                    div(
                        className := "output-section",
                        h3("Parser Output"),
                        div(
                            className := "parser-output",
                            height := "200px",
                            overflowY := "auto",
                            padding := "10px",
                            border := "1px solid #ccc",
                            backgroundColor := "#f5f5f5",
                            fontFamily := "monospace",
                            color <-- hasParseError.signal.map(hasError => if (hasError) "red" else "black"),
                            
                            // Display the AST
                            children <-- parserOutputVar.signal.map(output => 
                                if (output.isEmpty) 
                                    List(p("No AST to display. Enter some code and click 'Parse'."))
                                else 
                                    output.split("\n").map(line => div(line)).toList
                            )
                        )
                    )
                )
            ),
            
            // Add some basic styling
            styleTag("""
                .editor-container {
                    display: flex;
                    gap: 20px;
                    margin-top: 20px;
                }
                .input-section {
                    flex: 1;
                }
                .output-container {
                    flex: 1;
                    display: flex;
                    flex-direction: column;
                    gap: 20px;
                }
                .output-section {
                    flex: 1;
                }
                .button-row {
                    margin-top: 10px;
                    display: flex;
                    gap: 10px;
                }
                textarea {
                    width: 100%;
                    font-family: monospace;
                }
                h1 {
                    color: #333;
                }
                button {
                    padding: 8px 16px;
                    background-color: #4CAF50;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                }
                button:hover {
                    background-color: #45a049;
                }
            """)
        )
    end appElement
end TextArea