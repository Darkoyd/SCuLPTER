package webapp.pages

import com.raquo.laminar.api.L.{*, given}

object HomePage:
    def render(): Element =
        div(
            className := "page-container",
            div(
                className := "hero-section",
                h1("Welcome to SCuLPTER", className := "hero-title"),
                p("Simple Cubic Language for Programming Tasks Execution and Runtime", className := "hero-description"),
                div(
                    className := "hero-buttons",
                    button("Try the IDE", className := "btn-primary", onClick --> (_ => webapp.Router.navigateTo("sculpter"))),
                    button("View Examples", className := "btn-secondary", onClick --> (_ => webapp.Router.navigateTo("examples")))
                )
            ),
            div(
                className := "features-section",
                h2("Features"),
                div(
                    className := "features-grid",
                    div(
                        className := "feature-card",
                        h3("Stack-Based Language"),
                        p("SCuLPT uses a unique stack-based approach to programming, making it easy to understand data flow and execution.")
                    ),
                    div(
                        className := "feature-card",
                        h3("Interactive Interpreter"),
                        p("Step through your programs line by line to see exactly how your code executes and how stacks change.")
                    ),
                    div(
                        className := "feature-card",
                        h3("Real-time Feedback"),
                        p("Get immediate feedback with our lexer and parser that show you the structure of your programs.")
                    )
                )
            )
        )
end HomePage