package webapp.pages

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object GitHubPage:
    def render(): Element =
        div(
            className := "page-container",
            div(
                className := "redirect-page",
                div(
                    className := "redirect-icon",
                    "🔗"
                ),
                h1("More SCuLPT"),
                p("This page is still under construction"),
                p("When finished, it will provide more information about the SCuLPT project as a whole."),
                button("Back to Home", className := "btn-primary", onClick --> (_ => webapp.Router.navigateTo("home")))
            )
        )
end GitHubPage