package webapp.pages

import com.raquo.laminar.api.L.{*, given}
import scala.scalajs.js
import org.scalajs.dom
import com.raquo.airstream.flatten.FlattenStrategy.allowFlatMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.DynamicImplicits.truthValue


case class NavItem(id: String, title: String, level: Int, children: List[NavItem])
case class DocSection(id: String, title: String, filename: String, order: Int, headers: List[Map[String, Any]], navigation: List[NavItem])
case class DocsConfig(generated: String, sections: List[DocSection])

object DocumentationPage:
    private val currentChapter = Var("introduction")
    private val docsConfig = Var(Option.empty[DocsConfig])
    private val expandedSections = Var(Set[String]("introduction", "getting-started")) // Start with sections expanded
    
    // Load documentation config on initialization
    private def loadDocsConfig(): Unit =
        import org.scalajs.dom.ext.Ajax
        Ajax.get("/SCuLPTER/docs-config.json").onComplete {
            case scala.util.Success(xhr) =>
                try {
                    // Simple JSON parsing - in production you'd use a proper JSON library
                    val configText = xhr.responseText
                    // For now, we'll use a simplified approach and extract what we need
                    parseDocsConfig(configText)
                } catch {
                    case e: Exception =>
                        print("Failed to parse docs config:", e.getMessage)
                        // Fall back to default structure
                        createDefaultConfig()
                }
            case scala.util.Failure(error) =>
                print("Failed to load docs config:", error.getMessage)
                // Fall back to default structure  
                createDefaultConfig()
        }
    
    private def parseDocsConfig(configText: String): Unit =
        try {
            print("Parsing config, length:", configText.length)
            // Simplified parsing - just extract the basic section info for now
            val sections = extractBasicSections(configText)
            print("Found sections:", sections.length)
            docsConfig.set(Some(DocsConfig("", sections)))
        } catch {
            case e: Exception =>
                print("Error parsing config:", e.getMessage)
                createDefaultConfig()
        }
    
    private def extractBasicSections(configText: String): List[DocSection] =
        // Extract sections with their navigation data
        val sections = extractSectionsWithNavigation(configText)
        print("Extracted sections:", sections.map(s => s"${s.id}:${s.title}").mkString(", "))
        sections
    
    private def extractSectionsWithNavigation(configText: String): List[DocSection] =
        val sections = scala.collection.mutable.ListBuffer[DocSection]()
        
        // Find each complete section block
        val sectionPattern = """\{\s*"id":\s*"([^"]+)"[^}]*?"navigation":\s*\[([^\]]*?)\][^}]*?\}""".r
        
        // Also extract basic info for fallback
        val basicSectionPattern = """"id":\s*"([^"]+)"[^}]*"title":\s*"([^"]+)"[^}]*"filename":\s*"([^"]+)"[^}]*"order":\s*(\d+)""".r
        
        val basicSections = basicSectionPattern.findAllMatchIn(configText).map { m =>
            (m.group(1), m.group(2), m.group(3), m.group(4).toInt)
        }.toList
        
        // For each basic section, try to extract its navigation
        basicSections.foreach { case (id, title, filename, order) =>
            val navItems = extractNavigationForSection(configText, id)
            sections += DocSection(id, title, filename, order, List(), navItems)
        }
        
        sections.toList.sortBy(_.order)
    
    private def extractNavigationForSection(configText: String, sectionId: String): List[NavItem] =
        print(s"Extracting navigation for section: $sectionId")
        
        // Much simpler approach - find all headers for this section
        val headersPattern = s""""id":\\s*"$sectionId"[\\s\\S]*?"headers":\\s*\\[([^\\]]*?)\\]""".r
        
        headersPattern.findFirstMatchIn(configText) match {
            case Some(headersMatch) =>
                val headersText = headersMatch.group(1)
                print(s"Found headers text for $sectionId: ${headersText.take(100)}...")
                extractNavItemsFromHeaders(headersText)
            case None =>
                print(s"No headers found for section: $sectionId")
                List()
        }
    
    private def extractNavItemsFromHeaders(headersText: String): List[NavItem] =
        val navItems = scala.collection.mutable.ListBuffer[NavItem]()
        
        // Extract headers - much simpler pattern
        val headerPattern = """\{\s*"level":\s*(\d+)[^}]*"title":\s*"([^"]+)"[^}]*"id":\s*"([^"]+)"[^}]*\}""".r
        
        headerPattern.findAllMatchIn(headersText).foreach { headerMatch =>
            val level = headerMatch.group(1).toInt
            val title = headerMatch.group(2)
            val id = headerMatch.group(3)
            
            print(s"Found header: level=$level, title=$title, id=$id")
            
            // Only show level 2+ headers as subsections (skip the main title)
            if (level >= 2) {
                navItems += NavItem(id, title, level, List())
            }
        }
        
        print(s"Extracted ${navItems.length} nav items")
        navItems.toList
    
    
    
    private def createDefaultConfig(): Unit =
        val defaultSections = List(
            DocSection("introduction", "Introduction", "01-introduction.md", 1, List(), List()),
            DocSection("getting-started", "Getting Started", "02-getting-started.md", 2, List(), List())
        )
        docsConfig.set(Some(DocsConfig("", defaultSections)))
    
    // Initialize config loading
    loadDocsConfig()
    
    private val markdownCache = scala.collection.mutable.Map[String, String]()
    
    private val contentSignals = scala.collection.mutable.Map[String, Var[String]]()
    
    private def loadMarkdownContent(sectionId: String): Signal[String] =
        contentSignals.getOrElseUpdate(sectionId, {
            val contentVar = Var(s"# Loading...\n\nLoading content for $sectionId...")
            
            // Determine which file to load (section or parent section)
            val (fileName, isSubsection) = docsConfig.now() match {
                case Some(config) =>
                    // First check if it's a main section
                    config.sections.find(_.id == sectionId) match {
                        case Some(section) => (section.filename, false)
                        case None =>
                            // Check if it's a subsection (header within a document)
                            config.sections.find(_.navigation.exists(_.id == sectionId)) match {
                                case Some(parentSection) => (parentSection.filename, true)
                                case None => (s"$sectionId.md", false)
                            }
                    }
                case None =>
                    sectionId match {
                        case "introduction" => ("01-introduction.md", false)
                        case "getting-started" => ("02-getting-started.md", false) 
                        case _ => (s"$sectionId.md", false)
                    }
            }
            
            // Fetch the markdown file
            import org.scalajs.dom.ext.Ajax
            Ajax.get(s"/SCuLPTER/docs/$fileName").onComplete {
                case scala.util.Success(xhr) =>
                    contentVar.set(xhr.responseText)
                case scala.util.Failure(error) =>
                    contentVar.set(s"# Error Loading Content\n\nCould not load $fileName: ${error.getMessage}")
            }
            
            contentVar
        }).signal
    
    private def parseMarkdown(markdown: String): Element =
        // Simple markdown parser - in production you'd use a proper markdown library
        val lines = markdown.split("\n").toList
        val elements = scala.collection.mutable.ListBuffer[Element]()
        
        var i = 0
        while (i < lines.length) {
            val line = lines(i).trim
            
            if (line.startsWith("# ")) {
                val title = line.drop(2)
                val id = generateHeaderId(title)
                elements += h1(idAttr := id, title)
            } else if (line.startsWith("## ")) {
                val title = line.drop(3)
                val id = generateHeaderId(title)
                elements += h2(idAttr := id, title)
            } else if (line.startsWith("### ")) {
                val title = line.drop(4)
                val id = generateHeaderId(title)
                elements += h3(idAttr := id, title)
            } else if (line.startsWith("#### ")) {
                val title = line.drop(5)
                val id = generateHeaderId(title)
                elements += h4(idAttr := id, title)
            } else if (line.startsWith("```")) {
                // Code block
                val codeLines = scala.collection.mutable.ListBuffer[String]()
                i += 1
                while (i < lines.length && !lines(i).trim.startsWith("```")) {
                    codeLines += lines(i)
                    i += 1
                }
                elements += pre(code(codeLines.mkString("\n")))
            } else if (line.startsWith("- ") || line.startsWith("* ")) {
                // Unordered list
                val listItems = scala.collection.mutable.ListBuffer[String]()
                while (i < lines.length && (lines(i).trim.startsWith("- ") || lines(i).trim.startsWith("* "))) {
                    listItems += lines(i).trim.drop(2)
                    i += 1
                }
                i -= 1 // Adjust for the extra increment
                elements += ul(listItems.map(item => li(parseInlineFormatting(item))).toList)
            } else if (line.contains("|") && i < lines.length - 1 && lines(i + 1).trim.startsWith("|") && lines(i + 1).contains("-")) {
                // Table detection
                val tableData = parseTable(lines, i)
                elements += tableData._1
                i = tableData._2 - 1 // Adjust for the increment at end of loop
            } else if (line.nonEmpty) {
                // Regular paragraph with inline formatting
                elements += p(parseInlineFormatting(line))
            }
            
            i += 1
        }
        
        div(elements.toList)
    
    private def generateHeaderId(title: String): String =
        title.toLowerCase
            .replaceAll("[^\\w\\s-]", "")
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "")
    
    private def scrollToElement(elementId: String): Unit =
        import org.scalajs.dom
        dom.window.setTimeout(() => {
            dom.document.getElementById(elementId) match {
                case null => 
                    print(s"Element with ID '$elementId' not found")
                case element =>
                    element.scrollIntoView(js.Dynamic.literal(
                        behavior = "smooth",
                        block = "start"
                    ))
            }
        }, 100) // Small delay to ensure content is rendered
    
    private def parseInlineFormatting(text: String): List[Node] =
        val result = scala.collection.mutable.ListBuffer[Node]()
        var i = 0
        var currentText = ""
        
        while (i < text.length) {
            if (i < text.length - 1 && text.substring(i, i + 2) == "**") {
                // Add any accumulated text
                if (currentText.nonEmpty) {
                    result.addOne(currentText: Node)
                    currentText = ""
                }
                
                // Find the closing **
                val closingIndex = text.indexOf("**", i + 2)
                if (closingIndex != -1) {
                    val boldText = text.substring(i + 2, closingIndex)
                    result.addOne(strong(boldText))
                    i = closingIndex + 2
                } else {
                    // No closing **, treat as regular text
                    currentText = currentText + "**"
                    i += 2
                }
            } else if (text.charAt(i) == '`') {
                // Add any accumulated text
                if (currentText.nonEmpty) {
                    result.addOne(currentText: Node)
                    currentText = ""
                }
                
                // Find the closing `
                val closingIndex = text.indexOf('`', i + 1)
                if (closingIndex != -1) {
                    val codeText = text.substring(i + 1, closingIndex)
                    result.addOne(code(codeText))
                    i = closingIndex + 1
                } else {
                    // No closing `, treat as regular text
                    currentText = currentText + text.charAt(i).toString
                    i += 1
                }
            } else {
                currentText = currentText + text.charAt(i).toString
                i += 1
            }
        }
        
        // Add any remaining text
        if (currentText.nonEmpty) {
            result.addOne(currentText: Node)
        }
        
        result.toList
    
    private def parseTable(lines: List[String], startIndex: Int): (Element, Int) =
        var i = startIndex
        val tableRows = scala.collection.mutable.ListBuffer[List[String]]()
        
        // Parse header row
        val headerLine = lines(i).trim
        val headerCells = if (headerLine.startsWith("|") && headerLine.endsWith("|")) {
            headerLine.substring(1, headerLine.length - 1).split("\\|").map(_.trim).toList
        } else {
            headerLine.split("\\|").map(_.trim).filter(_.nonEmpty).toList
        }
        tableRows.addOne(headerCells)
        i += 1
        
        // Skip separator row (|---|---|---|)
        i += 1
        
        // Parse data rows
        while (i < lines.length && lines(i).trim.contains("|")) {
            val rowLine = lines(i).trim
            val rowCells = if (rowLine.startsWith("|") && rowLine.endsWith("|")) {
                rowLine.substring(1, rowLine.length - 1).split("\\|").map(_.trim).toList
            } else {
                rowLine.split("\\|").map(_.trim).filter(_.nonEmpty).toList
            }
            
            // Ensure all rows have the same number of columns as header
            val paddedCells = if (rowCells.length < headerCells.length) {
                rowCells ++ List.fill(headerCells.length - rowCells.length)("")
            } else {
                rowCells.take(headerCells.length)
            }
            
            tableRows.addOne(paddedCells)
            i += 1
        }
        
        // Create table element
        val tableElement = table(
            className := "docs-table",
            thead(
                tr(
                    tableRows.head.map(cell => th(if (cell.isEmpty) "\u00A0" else parseInlineFormatting(cell)))
                )
            ),
            tbody(
                tableRows.tail.map { row =>
                    tr(
                        row.map(cell => td(if (cell.isEmpty) "\u00A0" else parseInlineFormatting(cell)))
                    )
                }.toList
            )
        )
        
        (tableElement, i)
    
    private def getAllSections(sections: List[DocSection]): List[DocSection] =
        sections
    
    private def renderSidebar(): Element =
        div(
            className := "docs-sidebar",
            div(
                className := "docs-nav",
                child <-- docsConfig.signal.map {
                    case Some(config) =>
                        print("Rendering sidebar with sections:", config.sections.length)
                        div(config.sections.map(renderNavSection))
                    case None =>
                        print("No config loaded, showing loading message")
                        div("Loading navigation...")
                }
            )
        )
    
    private def renderNavSection(section: DocSection): Element =
        print(s"Rendering section ${section.id} with ${section.navigation.length} nav items")
        
        div(
            className := "docs-nav-section",
            // Section header with toggle
            div(
                className := "docs-nav-section-header",
                div(
                    className <-- expandedSections.signal.map(expanded => 
                        if expanded.contains(section.id) then "docs-nav-toggle expanded" else "docs-nav-toggle"
                    ),
                    onClick --> (_ => toggleSection(section.id)),
                    "▶"
                ),
                a(
                    href := "#",
                    className <-- currentChapter.signal.map(current => 
                        if current == section.id then "docs-nav-item main active" else "docs-nav-item main"
                    ),
                    section.title,
                    onClick.preventDefault --> (_ => {
                        currentChapter.set(section.id)
                        expandSection(section.id)
                    })
                )
            ),
            // Collapsible subsections
            div(
                className <-- expandedSections.signal.map(expanded =>
                    if expanded.contains(section.id) then "docs-nav-subsections expanded" else "docs-nav-subsections collapsed"
                ),
                renderNavItems(section.navigation)
            )
        )
    
    private def toggleSection(sectionId: String): Unit =
        val current = expandedSections.now()
        if (current.contains(sectionId)) {
            expandedSections.set(current - sectionId)
        } else {
            expandedSections.set(current + sectionId)
        }
    
    private def expandSection(sectionId: String): Unit =
        expandedSections.set(expandedSections.now() + sectionId)
    
    private def renderNavItems(navItems: List[NavItem]): List[Element] =
        navItems.map { navItem =>
            print(s"Rendering nav item: ${navItem.title} (level ${navItem.level})")
            
            val indentClass = navItem.level match {
                case 2 => "docs-nav-item level-2"
                case 3 => "docs-nav-item level-3" 
                case 4 => "docs-nav-item level-4"
                case _ => "docs-nav-item subsection"
            }
            
            a(
                href := "#",
                className <-- currentChapter.signal.map(current => 
                    if current == navItem.id then s"$indentClass active" else indentClass
                ),
                navItem.title,
                onClick.preventDefault --> (_ => navigateToSection(navItem.id))
            )
        }
    
    private def navigateToSection(sectionId: String): Unit =
        // Check if it's a main section or subsection
        docsConfig.now() match {
            case Some(config) =>
                // Check if it's a main section
                config.sections.find(_.id == sectionId) match {
                    case Some(section) =>
                        // It's a main section, just load it
                        currentChapter.set(sectionId)
                    case None =>
                        // It's a subsection, find parent and load document, then scroll
                        config.sections.find(_.navigation.exists(_.id == sectionId)) match {
                            case Some(parentSection) =>
                                currentChapter.set(parentSection.id)
                                // Wait for content to load, then scroll to the specific header
                                org.scalajs.dom.window.setTimeout(() => {
                                    scrollToElement(sectionId)
                                }, 500) // Give more time for markdown to render
                            case None =>
                                // Fallback - just set as current chapter
                                currentChapter.set(sectionId)
                        }
                }
            case None =>
                currentChapter.set(sectionId)
        }
    
    private def renderContent(): Element =
        div(
            className := "docs-content",
            child <-- currentChapter.signal.flatMap { chapterId =>
                loadMarkdownContent(chapterId).map(parseMarkdown)
            }
        )

    def render(): Element =
        div(
            className := "docs-layout",
            renderSidebar(),
            renderContent()
        )
end DocumentationPage