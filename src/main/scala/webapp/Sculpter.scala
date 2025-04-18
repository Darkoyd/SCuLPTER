package webapp

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

import sculpter.{Lexer, Parser, Program, UnaryStatement, BinaryStatement, StackExpr, NumberExpr, NilExpr, InterpreterInstance}

@main
def Sculpter(): Unit =
    renderOnDomContentLoaded(dom.document.getElementById("app"), TextArea.appElement())

object TextArea:
    def appElement(): Element =
        val textVar = Var("""// SCuLPT example program
// Try stepping through this program to see how it works

// Push values onto stack 'a'
PUSH a 5
PUSH a 10

// Duplicate the top value of stack 'a'
DUP a

// Create stack 'b' with value 3
PUSH b 3

// Move top value from stack 'a' to stack 'b'
MOV b a

// Add top values of stacks 'b' and 'a' (binary form)
ADD b a

// Conditional execution with QUESTION operator
PUSH c 1   // c > 0, so next instruction will be executed
? c
PUSH d 100 // This will be executed because c > 0

PUSH c -2  // c < 0, so next instruction will be skipped
? c
PUSH d 200 // This will be skipped because c <= 0

// Arithmetic examples
// Binary form (stack and number literal)
PUSH e 7
SUB e 2   // e now contains 5 (7-2)

// Unary form (operating on top two stack values)
PUSH f 3
PUSH f 5
SUB f     // f now contains -2 (3-5)

// Another unary example
PUSH g 3
PUSH g 4
MUL g     // g now contains 12
""")
        val lexerOutputVar = Var[List[String]](List())
        val parserOutputVar = Var[String]("")
        val hasParseError = Var(false)
        val isParsed = Var(false)
        
        val currentStacksVar = Var[Map[String, List[Option[Double]]]](Map())
        val currentStmtVar = Var(0)
        val totalStmtsVar = Var(0)
        val canStepForwardVar = Var(false)
        val canStepBackwardVar = Var(false)

        def resetInterpreter(): Unit = {
            currentStacksVar.set(Map())
            currentStmtVar.set(0)
            totalStmtsVar.set(0)
            canStepForwardVar.set(false)
            canStepBackwardVar.set(false)
        }

        val clearText = Observer[Any](_ => textVar.set(""))
        val clearOutput = Observer[Any](_ => {
            lexerOutputVar.set(List())
            parserOutputVar.set("")
            hasParseError.set(false)
            isParsed.set(false)
            resetInterpreter()
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
            h1("SCuLPTER"),
            h2("Simple Cubic Language for Programming Tasks Execution and Runtime"),
            
            div(
                className := "main-container",
                // Interpreter (now on the left)
                div(
                    className := "interpreter-container",
                    flex := "0 0 300px",
                    display <-- isParsed.signal.map(parsed => if (parsed) "block" else "none"),
                    
                    h3("Interpreter"),
                    
                    div(
                        className := "interpreter-controls",
                        
                        div(
                            className := "execution-info",
                            span("Statement: "),
                            span(
                                child.text <-- currentStmtVar.signal.map(_.toString)
                            ),
                            span(" / "),
                            span(
                                child.text <-- totalStmtsVar.signal.map(_.toString)
                            )
                        ),
                        
                        div(
                            className := "button-row",
                            button(
                                "Step Back",
                                disabled <-- canStepBackwardVar.signal.map(!_),
                                onClick --> Observer[Any](_ => {
                                    if (InterpreterInstance.stepBackward()) {
                                        currentStacksVar.set(InterpreterInstance.getStacksState())
                                        currentStmtVar.set(InterpreterInstance.getCurrentStatement())
                                        canStepForwardVar.set(true)
                                        canStepBackwardVar.set(InterpreterInstance.getCurrentStatement() > 0)
                                    }
                                })
                            ),
                            
                            button(
                                "Step Forward",
                                disabled <-- canStepForwardVar.signal.map(!_),
                                onClick --> Observer[Any](_ => {
                                    try {
                                        if (InterpreterInstance.stepForward()) {
                                            currentStacksVar.set(InterpreterInstance.getStacksState())
                                            currentStmtVar.set(InterpreterInstance.getCurrentStatement())
                                            canStepBackwardVar.set(true)
                                            canStepForwardVar.set(InterpreterInstance.getCurrentStatement() < InterpreterInstance.getTotalStatements())
                                            
                                            if (InterpreterInstance.didSkipInstruction()) {
                                                dom.window.alert("QUESTION operation evaluated to <= 0, skipping next instruction")
                                            }
                                        } else {
                                            canStepForwardVar.set(false)
                                        }
                                    } catch {
                                        case e: Exception =>
                                            dom.window.alert(s"Runtime Error: ${e.getMessage()}")
                                    }
                                })
                            ),
                            
                            button(
                                "Reset",
                                onClick --> Observer[Any](_ => {
                                    InterpreterInstance.reset(Parser(Lexer.tokens))
                                    currentStacksVar.set(InterpreterInstance.getStacksState())
                                    currentStmtVar.set(InterpreterInstance.getCurrentStatement())
                                    canStepForwardVar.set(InterpreterInstance.getTotalStatements() > 0)
                                    canStepBackwardVar.set(false)
                                })
                            )
                        )
                    ),
                    
                    div(
                        className := "stacks-container",
                        h4("Stacks"),
                        
                        div(
                            className := "stacks-view",
                            
                            children <-- currentStacksVar.signal.map(stacks => 
                                if (stacks.isEmpty) 
                                    List(p("No stacks defined yet. Execute code to see stacks."))
                                else 
                                    stacks.toList.map { case (name, values) => 
                                        div(
                                            className := "stack",
                                            h5(s"Stack: $name"),
                                            ul(
                                                className := "stack-items",
                                                values.map(optValue => li(
                                                    optValue match {
                                                        case Some(value) => value.toString
                                                        case None => "nil"
                                                    }
                                                ))
                                            )
                                        )
                                    }
                            )
                        )
                    )
                ),
                
                // Editor section (now on the right)
                div(
                    className := "editor-section",
                    flex := "1",
                    h3("Input"),
                    textArea(
                        typ := "text",
                        placeholder := "Type your SCuLPT code here...",
                        rows := 15,
                        width := "100%",
                        value <-- textVar.signal, 
                        onInput.mapToValue --> textVar
                    ),
                    
                    div(
                        className := "button-row",
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
                                Lexer.hadError = false
                                Lexer.tokens = List()
                                Lexer.start = 0
                                Lexer.current = 0
                                Lexer.line = 1
                                Lexer.source = textVar.now()
                                
                                val tokens = Lexer.scanTokens()
                                
                                val tokenStrings = tokens.map(token => 
                                    s"Line ${token.line}: ${token.tokenType} '${token.lexeme}' ${if (token.literal != null) token.literal else ""}"
                                )
                                
                                lexerOutputVar.set(tokenStrings)
                                hasParseError.set(false)
                                parserOutputVar.set("")
                                isParsed.set(false)
                                resetInterpreter()
                            })
                        ),
                        
                        button(
                            "Parse",
                            onClick --> Observer[Any](_ => {
                                Lexer.hadError = false
                                Lexer.tokens = List()
                                Lexer.start = 0
                                Lexer.current = 0
                                Lexer.line = 1
                                Lexer.source = textVar.now()
                                
                                val tokens = Lexer.scanTokens()
                                
                                try {
                                    val ast = Parser(tokens)
                                    parserOutputVar.set(formatASTNode(ast))
                                    hasParseError.set(false)
                                    
                                    InterpreterInstance.reset(ast)
                                    currentStacksVar.set(InterpreterInstance.getStacksState())
                                    currentStmtVar.set(InterpreterInstance.getCurrentStatement())
                                    totalStmtsVar.set(InterpreterInstance.getTotalStatements())
                                    canStepForwardVar.set(totalStmtsVar.now() > 0)
                                    canStepBackwardVar.set(false)
                                    isParsed.set(true)
                                } catch {
                                    case e: Exception =>
                                        parserOutputVar.set(s"Parse error: ${e.getMessage()}")
                                        hasParseError.set(true)
                                        isParsed.set(false)
                                        resetInterpreter()
                                }
                            })
                        )
                    )
                )
            ),
            
            // Lexer and Parser outputs (now at the bottom)
            div(
                className := "output-container",
                marginTop := "20px",
                
                div(
                    className := "output-row",
                    div(
                        className := "output-section",
                        width := "50%",
                        h3("Lexer Output"),
                        div(
                            className := "lexer-output",
                            height := "200px",
                            overflowY := "auto",
                            padding := "10px",
                            border := "1px solid #ccc",
                            backgroundColor := "#f5f5f5",
                            fontFamily := "monospace",
                            
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
                        width := "50%",
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
            
            styleTag("""
                .main-container {
                    display: flex;
                    gap: 20px;
                    margin-top: 20px;
                }
                .editor-section {
                    display: flex;
                    flex-direction: column;
                }
                .output-container {
                    width: 100%;
                }
                .output-row {
                    display: flex;
                    gap: 20px;
                }
                .button-row {
                    margin-top: 10px;
                    display: flex;
                    gap: 10px;
                }
                textarea {
                    font-family: monospace;
                    resize: vertical;
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
                button:disabled {
                    background-color: #cccccc;
                    cursor: not-allowed;
                }
                .interpreter-container {
                    padding: 20px;
                    border: 1px solid #ddd;
                    border-radius: 4px;
                    background-color: #f9f9f9;
                }
                .execution-info {
                    margin-bottom: 10px;
                }
                .stacks-container {
                    margin-top: 20px;
                }
                .stacks-view {
                    display: flex;
                    flex-direction: column;
                    gap: 10px;
                }
                .stack {
                    border: 1px solid #ccc;
                    border-radius: 4px;
                    padding: 10px;
                    background-color: #fff;
                }
                .stack h5 {
                    margin-top: 0;
                    margin-bottom: 10px;
                    color: #333;
                }
                .stack-items {
                    margin: 0;
                    padding-left: 20px;
                }
            """)
        )
    end appElement
end TextArea