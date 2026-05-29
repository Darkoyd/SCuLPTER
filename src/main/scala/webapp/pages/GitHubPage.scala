package webapp.pages

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object GitHubPage:

    private case class RepoCard(title: String, url: String, description: String)

    private val repos: List[RepoCard] = List(
        RepoCard(
            "SCuLPT",
            "https://github.com/Darkoyd/SCuLPT",
            "The language specification and 3D-printable physical blocks. Defines the stack-based syntax and ships STL and Fusion 360 sources for the tangible tiles."
        ),
        RepoCard(
            "SCuLPTER",
            "https://github.com/Darkoyd/SCuLPTER",
            "This web runtime — a Scala.js and Laminar IDE that lexes, parses, and step-executes SCuLPT programs in the browser."
        ),
        RepoCard(
            "SCuLPT-Document",
            "https://github.com/Darkoyd/SCuLPT-Document",
            "The thesis (LaTeX) covering the language design, theory, and evaluation behind SCuLPT."
        ),
        RepoCard(
            "ArtLang",
            "https://github.com/Darkoyd/ArtLang",
            "CCC'24 conference paper presenting SCuLPT as a tangible, art-oriented programming language."
        ),
        RepoCard(
            "ArtLangPoster",
            "https://github.com/Darkoyd/ArtLangPoster",
            "Poster companion to the ArtLang paper, used at CCC'24."
        ),
        RepoCard(
            "sculpt-programming",
            "https://github.com/FLAGlab/sculpt",
            "Journal-paper submission to Programming, expanding on the language design and pedagogical results."
        )
    )

    private def repoCard(card: RepoCard): Element =
        div(
            className := "feature-card",
            h3(card.title),
            p(card.description),
            a(
                href := card.url,
                target := "_blank",
                rel := "noopener noreferrer",
                className := "docs-link docs-external-link",
                "View on GitHub"
            )
        )

    def render(): Element =
        div(
            className := "page-container",
            div(
                className := "hero-section",
                h1("More SCuLPT", className := "hero-title"),
                p(
                    "SCuLPT is a multi-repo research project that spans a tangible language specification, a web runtime, academic papers, and a thesis. Explore the sibling repositories and the photo gallery below.",
                    className := "hero-description"
                )
            ),
            div(
                className := "features-section",
                h2("Repositories"),
                div(
                    className := "features-grid",
                    repos.map(repoCard)
                )
            ),
            div(
                className := "features-section",
                h2("Gallery"),
                div(
                    className := "features-grid",
                    div(
                        className := "feature-card",
                        h3("Flickr Album"),
                        p("Photos of printed SCuLPT blocks and assembled programs."),
                        a(
                            href := "https://www.flickr.com/photos/203211239@N04/albums",
                            target := "_blank",
                            rel := "noopener noreferrer",
                            className := "docs-link docs-external-link",
                            "View on Flickr"
                        )
                    )
                )
            ),
            div(
                className := "hero-buttons",
                button("Back to Home", className := "btn-primary", onClick --> (_ => webapp.Router.navigateTo("home")))
            )
        )
end GitHubPage
