package webapp.pages

import com.raquo.laminar.api.L.{*, given}

object ExamplesPage:
    def render(): Element =
        div(
            className := "page-container",
            div(
                className := "under-construction",
                div(
                    className := "construction-icon",
                    "📚"
                ),
                h1("Examples"),
                p("This page is under construction."),
                p("Soon you'll find a collection of SCuLPT code examples demonstrating various programming concepts and language features."),
                button("Back to Home", className := "btn-primary", onClick --> (_ => webapp.Router.navigateTo("home")))
            )
        )
end ExamplesPage