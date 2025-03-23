package livechart

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

@main
def Sculpter(): Unit =
    renderOnDomContentLoaded(dom.document.getElementById("app"), TextArea.appElement())



object TextArea:
    def appElement(): Element =

        val textVar = Var("")
        val lexerOutputVar = Var[List[String]](List())

        val clearText = Observer[Any](_ => textVar.set(""))
        val clearOutput = Observer[Any](_ => lexerOutputVar.set(List()))

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
                            })
                        )
                    )
                ),
                
                div(
                    className := "output-section",
                    h3("Lexer Output"),
                    div(
                        className := "lexer-output",
                        height := "400px",
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
                )
            ),
            
            // Add some basic styling
            styleTag("""
                .editor-container {
                    display: flex;
                    gap: 20px;
                    margin-top: 20px;
                }
                .input-section, .output-section {
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
            """)
        )
    end appElement
end TextArea