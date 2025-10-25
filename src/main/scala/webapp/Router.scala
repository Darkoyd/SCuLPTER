package webapp

import com.raquo.laminar.api.L.{*, given}
import webapp.pages.{HomePage, DocumentationPage, ExamplesPage, GitHubPage}
import webapp.components.IDE
import org.scalajs.dom
import scala.scalajs.js

sealed trait Route
case object HomeRoute extends Route
case object SculpterRoute extends Route
case class DocumentationRoute(section: String = "introduction", subsection: Option[String] = None) extends Route
case object ExamplesRoute extends Route
case object GitHubRoute extends Route

object Router:
    private val currentRouteVar = Var[Route](HomeRoute)
    private val baseUrl = "/SCuLPTER"

    // Parse route from URL path
    private def parseRoute(path: String): Route =
        val cleanPath = path.stripPrefix(baseUrl).stripSuffix("/")
        val segments = cleanPath.split("/").filter(_.nonEmpty).map(_.toLowerCase)

        segments.headOption.getOrElse("") match {
            case "" | "home" => HomeRoute
            case "sculpter" => SculpterRoute
            case "documentation" =>
                val section = segments.lift(1).getOrElse("introduction")
                val subsection = segments.lift(2)
                DocumentationRoute(section, subsection)
            case "examples" => ExamplesRoute
            case "github" => GitHubRoute
            case _ => HomeRoute
        }

    // Convert route to URL path
    private def routeToPath(route: Route): String =
        val path = route match {
            case HomeRoute => ""
            case SculpterRoute => "/sculpter"
            case DocumentationRoute(section, subsection) =>
                subsection match {
                    case Some(sub) => s"/documentation/$section/$sub"
                    case None => s"/documentation/$section"
                }
            case ExamplesRoute => "/examples"
            case GitHubRoute => "/github"
        }
        baseUrl + path

    // Initialize router by parsing current URL
    private def initializeRouter(): Unit =
        val currentPath = dom.window.location.pathname
        val initialRoute = parseRoute(currentPath)
        currentRouteVar.set(initialRoute)

        // Listen for back/forward button navigation
        dom.window.addEventListener("popstate", (_: dom.PopStateEvent) => {
            val newPath = dom.window.location.pathname
            val newRoute = parseRoute(newPath)
            currentRouteVar.set(newRoute)
        })

    // Initialize on first load
    initializeRouter()

    // Navigate to a route by page name
    def navigateTo(pageName: String): Unit =
        val route = pageName.toLowerCase match {
            case "home" => HomeRoute
            case "sculpter" => SculpterRoute
            case "documentation" => DocumentationRoute()
            case "examples" => ExamplesRoute
            case "github" => GitHubRoute
            case _ => HomeRoute
        }
        updateRoute(route)

    // Navigate to a specific documentation section
    def navigateToDocumentation(section: String, subsection: Option[String] = None): Unit =
        updateRoute(DocumentationRoute(section, subsection))

    // Navigate to a documentation section by name
    def navigateToDocumentationByName(sectionName: String): Unit =
        updateRoute(DocumentationRoute(sectionName))

    // Internal function to update route and URL
    private def updateRoute(route: Route): Unit =
        currentRouteVar.set(route)
        val newPath = routeToPath(route)
        dom.window.history.pushState(null, "", newPath)

    // Get the current route as a signal (reactive)
    def currentRoute: Signal[Route] = currentRouteVar.signal

    // Get current route value synchronously
    def currentRouteNow: Route = currentRouteVar.now()

    // Main render function that shows the appropriate page
    def render(): Element =
        div(
            child <-- currentRoute.map {
                case HomeRoute => HomePage.render()
                case SculpterRoute => IDE.appElement()
                case DocumentationRoute(section, subsection) => DocumentationPage.render(section, subsection)
                case ExamplesRoute => ExamplesPage.render()
                case GitHubRoute => GitHubPage.render()
            }
        )
end Router