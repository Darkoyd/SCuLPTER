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

        val clearText = Observer[Any](_ => textVar.set(""))

        div(
            textArea(
                typ := "text",
                placeholder := "Type your code here...",
                rows := 10,
                cols := 50,

                value <-- textVar.signal, 
                onInput.mapToValue --> textVar
            ),

        br(),

        button(
            "Clear",
            onClick --> clearText
        ),

        button(
            "Lex",
            onClick --> (_ => Lexer.lexer(textVar.now()))
        )
            
        )
    end appElement
end TextArea

