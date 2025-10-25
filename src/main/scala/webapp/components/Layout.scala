package webapp.components

import com.raquo.laminar.api.L.{*, given}
import webapp.Router

object Layout:
    // Helper function to determine active class based on route
    private def isRouteActive(currentRoute: webapp.Route, targetRoute: webapp.Route): Boolean =
        (currentRoute, targetRoute) match {
            case (webapp.HomeRoute, webapp.HomeRoute) => true
            case (webapp.SculpterRoute, webapp.SculpterRoute) => true
            case (webapp.DocumentationRoute(_, _), webapp.DocumentationRoute(_, _)) => true
            case (webapp.ExamplesRoute, webapp.ExamplesRoute) => true
            case (webapp.GitHubRoute, webapp.GitHubRoute) => true
            case _ => false
        }

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
                    className <-- Router.currentRoute.map(route =>
                        if (isRouteActive(route, webapp.HomeRoute)) "nav-link active" else "nav-link"
                    ),
                    onClick.preventDefault --> (_ => Router.navigateTo("home"))
                ),
                a(
                    "SCuLPTER",
                    href := "#",
                    className <-- Router.currentRoute.map(route =>
                        if (isRouteActive(route, webapp.SculpterRoute)) "nav-link active" else "nav-link"
                    ),
                    onClick.preventDefault --> (_ => Router.navigateTo("sculpter"))
                ),
                a(
                    "Documentation",
                    href := "#",
                    className <-- Router.currentRoute.map(route =>
                        if (isRouteActive(route, webapp.DocumentationRoute())) "nav-link active" else "nav-link"
                    ),
                    onClick.preventDefault --> (_ => Router.navigateTo("documentation"))
                ),
                a(
                    "Examples",
                    href := "#",
                    className <-- Router.currentRoute.map(route =>
                        if (isRouteActive(route, webapp.ExamplesRoute)) "nav-link active" else "nav-link"
                    ),
                    onClick.preventDefault --> (_ => Router.navigateTo("examples"))
                ),
                a(
                    "More SCuLPT",
                    href := "#",
                    className <-- Router.currentRoute.map(route =>
                        if (isRouteActive(route, webapp.GitHubRoute)) "nav-link active" else "nav-link"
                    ),
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