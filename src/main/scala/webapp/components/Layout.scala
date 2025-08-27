package webapp.components

import com.raquo.laminar.api.L.{*, given}
import webapp.Router

object Layout:
    def navbar(): Element =
        div(
            className := "navbar",
            div(
                className := "navbar-brand",
                h1("SCuLPTER", className := "navbar-title"),
                span("Simple Cubic Language for Programming Tasks Execution and Runtime", className := "navbar-subtitle")
            ),
            div(
                className := "navbar-nav",
                a(
                    "Home", 
                    href := "#", 
                    className <-- Router.currentRoute.map(route => if (route.toString == "HomeRoute") "nav-link active" else "nav-link"), 
                    onClick.preventDefault --> (_ => Router.navigateTo("home"))
                ),
                a(
                    "SCuLPTER", 
                    href := "#", 
                    className <-- Router.currentRoute.map(route => if (route.toString == "SculpterRoute") "nav-link active" else "nav-link"), 
                    onClick.preventDefault --> (_ => Router.navigateTo("sculpter"))
                ),
                a(
                    "Documentation", 
                    href := "#", 
                    className <-- Router.currentRoute.map(route => if (route.toString == "DocumentationRoute") "nav-link active" else "nav-link"), 
                    onClick.preventDefault --> (_ => Router.navigateTo("documentation"))
                ),
                a(
                    "Examples", 
                    href := "#", 
                    className <-- Router.currentRoute.map(route => if (route.toString == "ExamplesRoute") "nav-link active" else "nav-link"), 
                    onClick.preventDefault --> (_ => Router.navigateTo("examples"))
                ),
                a(
                    "More SCuLPT", 
                    href := "#", 
                    className <-- Router.currentRoute.map(route => if (route.toString == "GitHubRoute") "nav-link active" else "nav-link"), 
                    onClick.preventDefault --> (_ => Router.navigateTo("github"))
                )
            )
        )
    
    def mainLayout(content: Element): Element =
        div(
            className := "app-layout",
            navbar(),
            div(
                className := "main-content",
                content
            )
        )
end Layout