package webapp.pages

import com.raquo.laminar.api.L.{*, given}
import scala.concurrent.ExecutionContext.Implicits.global

object ExamplesPage:
    // Fetched once, then reused on every visit.
    private val content = Var("# Examples\n\nLoading examples...")

    org.scalajs.dom.ext.Ajax.get("/SCuLPTER/examples/examples.md").onComplete {
        case scala.util.Success(xhr) => content.set(xhr.responseText)
        case scala.util.Failure(error) => content.set(s"# Examples\n\nCould not load the examples: ${error.getMessage}")
    }

    def render(): Element =
        div(
            className := "docs-content examples-page",
            child <-- content.signal.map(DocumentationPage.parseMarkdown)
        )
end ExamplesPage
