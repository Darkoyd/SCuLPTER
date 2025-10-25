# Getting Started

Welcome! This chapter will guide you through getting up and running with your first SCuLPT program. Whether you're here for the tactile experience of pushing physical blocks around, the convenience of coding in your browser, or the best of both worlds, you're in the right place.

There are two completely different ways to use SCuLPT: the **physical way** with 3D printed components that you can hold and manipulate, and the **digital way** using SCuLPTER—our online editor and runtime environment. You can choose either or both, depending on your preference and what you have access to. Each approach offers unique benefits, and we'll explore both in this guide.

Let's dive in! 

## Physical Approach

The physical approach to SCuLPT is for those who want to experience programming in a tangible, hands-on way. You'll be building a system of interconnected blocks, stacks, and parameters that you can physically manipulate and rearrange. It's like computational thinking made concrete.

### Requirements

To get started with the physical version of SCuLPT, you'll need to manufacture or 3D print the components. The full 3D models and detailed assembly instructions are available in the [Hardware](/SCuLPTER/documentation/hardware) section of this documentation. Here's what you'll need in addition to the printed parts:

- **A 3D printer or access to one** - FDM 3D printers work great, though resin printing is also possible (with some additional finishing work).
- **Basic tools** - Small files, cutters, or other tools for detailing and assembly of the printed parts.
- **Dry erase markers** - These are used to write values on your components during programming.
- **A flat, stable work surface** - A table or board where you can arrange and organize your program.
- **Melamine board or whiteboard pieces** (approximately 2x2x2 cm) - These pieces will be needed to input information to the program.

**A note on experience**: It's helpful to have some prior experience with 3D printing and assembly, as the components may need some finishing touches after printing. However, don't let this discourage you—the finishing work is minimal and straightforward. If you're new to 3D printing, the [Hardware](/SCuLPTER/documentation/hardware) section includes guidance on printing best practices.

**Alternative materials**: If you're feeling creative, you can build the components using other materials like wood, metal, or resin. If you have the tools and skills, customizing SCuLPT to your preferred material is part of the fun!


#### Recommended starting parts

Ready to start building? Here's a curated list of parts recommended for creating your first physical SCuLPT programs. These quantities are designed to give you enough components to build a variety of simple to moderately complex programs without overwhelming you with excess parts.

**Printing specifications**: When 3D printing these parts, **PLA or PETG materials work best**. On average, you can expect to use approximately **one 1kg spool of filament** to print a full set of components. Print quality matters here, so take your time with the settings!

| Part Name                 | Quantity | Notes                                      |
|---------------------------|----------|--------------------------------------------|
| Base 1 Parameter Block    |     10   |  Print with no support on the screw section  |
| Base 2 Parameter Block    |     10   |  Print with no support on the screw section  |
| Connector                 |     30   |    |
| Parameter Block           |     30   |    |
| Nuts                      |     50   |  Print with no supports  |
| Wedges                    |     20   |  Print with no support on the screw section  |
| Dry Erase Marker          |     2    |    |
| Magnets                   |     60   | 10mm diameter, 2mm thick, neodymium recommended |
| Operations                |     65 (5 of each)   |    |

**A few helpful tips**: The quantities listed above are approximate and will vary depending on the complexity and scale of the programs you want to create. We recommend **printing a few extra parts** to account for printing errors or future projects. It's better to have extras than to run out mid-project!

Once you've got these parts, you can print additional pieces as needed for more ambitious programs. And remember—you're not limited to these exact specifications. Feel free to create custom parts if you have a specific idea in mind!


#### Printing the Components

The STL files for all the components are available in the [Hardware](/SCuLPTER/documentation/hardware) section of this documentation. Download the files and prepare them for your preferred printing method.

**For FDM printing** (most common): Standard FDM 3D printers will work beautifully. Just remember to pay attention to the support notes in the parts table above—some parts like the nuts should be printed without supports for the best results.

**For resin printing**: This method can produce excellent quality parts, though you may need to do some additional finishing work on the screw threads to ensure they're functional. The extra effort can be worth it if you have access to a resin printer!

**Regardless of which method you choose**, make sure to verify that:
- All parts fit together properly once printed
- The screw threads are functional and not damaged
- Any support marks or print artifacts are cleaned up

The [Hardware](/SCuLPTER/documentation/hardware) section includes detailed printing guides and assembly instructions specific to each part. Don't skip this—it can save you a lot of frustration later!

### Starting Your First Physical Program

Once you've printed and assembled your components, you're ready to build your first program! The physical components work by connecting blocks, stacks, and parameters together in specific arrangements. As you arrange the pieces, you're essentially "writing" code with your hands.

To learn how to actually write and execute programs using your physical components, head over to the [Start Sculpting](/SCuLPTER/documentation/start-sculpting) section of this documentation. There you'll find step-by-step guides on creating your first programs.


## Digital Approach

If you'd rather skip the 3D printing and jump straight into coding, you're in luck! SCuLPTER is available right here on this site. It's the perfect way to learn SCuLPT and experiment with ideas without needing any physical materials.

### Using SCuLPTER

To start coding, simply navigate to the **SCuLPTER** tab at the top of this page. You'll be greeted with a clean, intuitive interface designed to make learning and coding enjoyable.

**Here's what you'll see**:
- **A code editor** on the left where you write your SCuLPT programs
- **Output consoles** below the editor showing the results of your code
- **Control buttons** to compile, execute, and inspect your code
- **An interpreter section** (appears after successful parsing) showing your program's state step-by-step

**How to write and run code**: Type your SCuLPT code directly into the editor. When you're ready, click the "Parse" button to check your code for syntax errors. If everything looks good, the interpreter will be ready, and you can begin stepping through your program. The output will be displayed in **two distinct formats**:
- The **textual output** showing token and parse tree details
- The **visual representation** of your code's structure

You can clear the editor at any time by clicking the "Clear" button and start fresh.

#### Lexing vs Parsing

Here's a cool feature of SCuLPTER: you get to see how your code is actually processed by the language! Most programming environments hide this behind the scenes, but understanding how your code is interpreted can be incredibly helpful for learning.

Every programming language goes through two main stages when processing your code:

**Lexing** is the first step. It's like reading a sentence and breaking it down into individual words. Your code is broken down into the smallest units of meaning, called **tokens**. Each token represents something concrete: an operation, a number, a stack name, etc. When you view the lexer output, you'll see exactly how SCuLPTER understands each part of your code at the word level.

**Parsing** is the second step. Now that you have all these tokens, the parser figures out how they relate to each other and follow the language's grammar rules. It builds a **parse tree** showing the structure and meaning of your program. This is like taking those individual words and understanding how they form sentences and convey meaning.

**Why does this matter?** Understanding the difference helps you debug your code:
- **Syntax errors** are mistakes in the structure of your code—typos, missing operations, wrong order of things. These show up during lexing or parsing.
- **Semantic errors** are mistakes in the meaning—your code is syntactically correct but doesn't do what you intended. These are usually caught later when you run your program.

**Using the tool**: The SCuLPTER interface lets you visualize both stages! Use the "Lex" button to see how your code tokenizes, and the "Parse" button to see the parsed tree structure. This is a great way to understand exactly how SCuLPT sees your program—and to catch errors before they cause problems.

#### Saving and Loading Code

The easiest way to save your work is to **copy the code from the editor and paste it into a text file** on your computer. You can use any text editor—Notepad, VS Code, your editor of choice—and save it as a `.scu` file or even just `.txt`.

To **load code back into SCuLPTER**, simply copy the code from your text file and paste it into the editor. Your previous work will load right back up, ready to continue or modify.

**Note**: Download buttons for direct exporting/importing are planned for future updates to make this process even smoother!


#### Running Code

Once you've written some code and successfully parsed it, it's time to run it! Here's how the execution process works:

**Step 1: Parse your code**
Click the "Parse" button to check your code for errors. If there are any syntax errors, the parser will let you know exactly what's wrong. Fix them and try again. Once parsing succeeds, you're ready to go!

**Step 2: Meet the Interpreter**
After successful parsing, the **Interpreter section** will appear below the output console. This is where the magic happens—it's your window into how your program executes.

**What you'll see in the Interpreter**:
- **Statement counter** - Shows you which statement you're currently on and the total number of statements in your program (e.g., "3 / 10" means you're on statement 3 out of 10)
- **Stacks display** - Shows the current state of all your stacks and their values as you step through the program
- **Step Forward button** - Executes the next statement and updates the display
- **Step Backward button** - Goes back to the previous statement, undoing the last execution
- **Reset button** - Clears everything and takes you back to the starting state

**The execution model**: One of the cool things about SCuLPT is that you execute your program **one statement at a time**. This gives you complete visibility into what's happening at each step. You can watch values change, see how stacks grow and shrink, and truly understand the flow of your program.

**Pro tip**: This step-by-step approach is perfect for learning and debugging. You can run through your program slowly, inspect the state after each operation, and catch any logic errors before they become problematic.

Ready to write some code? Head over to the [Start Sculpting](/SCuLPTER/documentation/start-sculpting) section to learn the language syntax and create your first programs!


## What's Next?

Congratulations! You now have everything you need to start exploring SCuLPT. Whether you've chosen the **physical approach** with 3D printed components, the **digital approach** using SCuLPTER right here in your browser, or my personal favorite, **both**! You're ready to take the next step.

Here's where to go from here:

**Want to dive into the Examples?** Check out the [Examples](/SCuLPTER/examples) section for working code samples that demonstrate different programming concepts. You can load these directly into SCuLPTER and experiment with them.

**Need more details on the language itself?** The [Introduction](/SCuLPTER/documentation/introduction) section dives deeper into SCuLPT's philosophy and design principles, which can help you understand the "why" behind the language.

---

No matter which path you choose, remember: **programming is about experimentation**. Don't be afraid to try things, make mistakes, and learn from them, have fun, create. That's exactly what SCuLPT is designed for.

Happy building!