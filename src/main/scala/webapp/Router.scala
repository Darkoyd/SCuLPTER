package webapp

import com.raquo.laminar.api.L.{*, given}
import webapp.pages.{HomePage, DocumentationPage, ExamplesPage, GitHubPage}
import webapp.components.IDE

sealed trait Route
case object HomeRoute extends Route
case object SculpterRoute extends Route
case object DocumentationRoute extends Route
case object ExamplesRoute extends Route
case object GitHubRoute extends Route

object Router:
    private val currentRouteVar = Var[Route](HomeRoute)
    
    def navigateTo(routeName: String): Unit = {
        val route = routeName.toLowerCase match {
            case "home" => HomeRoute
            case "sculpter" => SculpterRoute
            case "documentation" => DocumentationRoute
            case "examples" => ExamplesRoute
            case "github" => GitHubRoute
            case _ => HomeRoute
        }
        currentRouteVar.set(route)
    }
    
    def currentRoute: Signal[Route] = currentRouteVar.signal
    
    def render(): Element = {
        div(
            child <-- currentRoute.map {
                case HomeRoute => HomePage.render()
                case SculpterRoute => IDE.appElement()
                case DocumentationRoute => DocumentationPage.render()
                case ExamplesRoute => ExamplesPage.render()
                case GitHubRoute => GitHubPage.render()
            }
        )
    }
end Router