package webapp.pages

import com.raquo.laminar.api.L.{*, given}

object DocumentationPage:
    def render(): Element =
        div(
            className := "page-container",
            div(
                className := "under-construction",
                div(
                    className := "construction-icon",
                    "🚧"
                ),
                h1("Documentation"),
                p("This page is under construction."),
                p("Check back later for comprehensive documentation on SCuLPT language features, syntax, and examples."),
                button("Back to Home", className := "btn-primary", onClick --> (_ => webapp.Router.navigateTo("home")))
            )
        )
end DocumentationPage