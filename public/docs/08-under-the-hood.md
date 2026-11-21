# Under the Hood

You made it to the end! This chapter is for the curious: why 13 operations are enough to compute anything, how SCuLPTER turns your text into running stacks, and how you can run it, change it, and help it grow.
None of this is needed to sculpt. But if you like to know how things work, grab a coffee.

## Is SCuLPT a real language?

Yes! SCuLPT is **Turing complete**. That's the computer science way of saying it can compute anything any other programming language can compute, given enough blocks and enough time. Python, Java and SCuLPT are all equally powerful in this sense. SCuLPT is just a lot more patient.

The proof works by showing that SCuLPT can imitate a **Turing machine**, the simplest model of a computer. A Turing machine has an infinite tape of cells, a head that reads and writes one cell at a time, and a table of rules that says what to write, where to move and which rule to use next.

Here is how SCuLPT plays each part:

- **The tape** is two stacks. `left` holds the cells to the left of the head, and `right` holds the current cell and everything to its right. Moving the head is just a Move from one stack to the other!
- **Blank cells** are nil. Since empty stacks give nil, the tape is infinite for free: walk off the end and you find blanks.
- **The rules** are groups of blocks, one group per state. Each group reads the current cell, checks which symbol it is with Compare and Question, writes the new symbol, moves the head, and Jumps to the group of the next state.
- **Halting** is a group with no next block.

Since any Turing machine can be translated this way, any computation can be written in SCuLPT. The full proof, with every block and every jump offset, is in the journal paper listed under Research, at the end of this chapter.

## A Turing machine in SCuLPT

Here is a real one: the 3-state **Busy Beaver**. It's a famous little machine that starts on a tape of 0s, writes six 1s, and stops after 14 moves. This version keeps the current cell in its own `head` stack, and uses `temp` to compare.

```
PUSH left 0
PUSH left 0
PUSH left 0
PUSH left 0
PUSH right 0
PUSH right 0
PUSH right 0
PUSH right 0
PUSH head 0
PUSH temp 1         // STATE A
MOV temp head
CMP temp
? temp
JMP 13              // head is 1: go to A_1
PUSH head 1
MOV right head
MOV head left
PUSH temp 1         // STATE B
MOV temp head
CMP temp
? temp
JMP 9               // head is 1: go to B_1
PUSH head 1
MOV left head
MOV head right
JMP -16             // go to A
PUSH head 1         // A_1
MOV left head
MOV head right
JMP 5               // go to C
PUSH head 1         // B_1
MOV right head
MOV head left
JMP -16             // go to B
PUSH temp 1         // STATE C
MOV temp head
CMP temp
? temp
JMP 5               // head is 1: go to HALT
PUSH head 1
MOV left head
MOV head right
JMP -25             // go to B
PUSH head 1         // HALT
```

Paste it in SCuLPTER and step through it. When it stops, count the 1s across `left`, `head` and `right`: there are six of them.

Notice that the comments sit at the end of a line, next to the block where each state starts. That way they don't get in the way of the Jumps.

## How SCuLPTER works

SCuLPTER is a small interpreter written in [Scala](https://www.scala-lang.org/) and compiled to JavaScript with [Scala.js](https://www.scala-js.org/), so it runs entirely in your browser. Nothing is sent to a server. The interface is built with [Laminar](https://laminar.dev/) and served with [Vite](https://vitejs.dev/).

When you click the buttons, your code goes through three stages:

- **Lexer** (the Lex button): reads your text character by character and cuts it into tokens: operations, stack names, numbers, nil and line ends. Comments are thrown away here.
- **Parser** (the Parse button): reads the tokens and builds the list of blocks, checking that each operation has the right parameters. A line it can't understand is reported and skipped.
- **Interpreter** (the Step buttons): runs one block at a time over a set of stacks. Before every step it saves a snapshot of all the stacks, which is how Step Back can undo.

The whole language fits in five files under `src/main/scala/sculpter/`:

| File | What it does |
|------|--------------|
| `Tokens.scala` | The list of token types and keywords |
| `Lexer.scala` | Text to tokens |
| `AST.scala` | The shapes of a parsed program |
| `Parser.scala` | Tokens to a program |
| `Interpreter.scala` | Runs a program step by step |

The web interface lives in `src/main/scala/webapp/`, and this documentation lives in `public/docs/` as plain Markdown files.

## Running SCuLPTER locally

You need [sbt](https://www.scala-sbt.org/) 1.7.3 or newer, [Node.js](https://nodejs.org/en) 16.13 or newer, and npm 8.1 or newer.

In a first terminal, start sbt and keep the Scala.js compiler watching your files:

```
sbt
~fastLinkJS
```

In a second terminal, install the dependencies and start the development server:

```
npm install
npm run dev
```

Open the address Vite prints, and you have your own SCuLPTER. Change a Scala file and the page reloads by itself.

## Contributing

SCuLPT is a community project, and there are many ways to help:

- **Found a bug?** Open an issue on [GitHub](https://github.com/Darkoyd/SCuLPTER) with the program that triggers it.
- **Have an idea?** Syntax highlighting, better error messages, saving and loading files, and a camera that reads real sculptures are all on the wish list.
- **Improve these docs.** Each chapter is a Markdown file in `public/docs/`, named with its order and its address, like `05-control-flow.md`. After adding or renaming a chapter, run `node scripts/generate-docs-config.cjs` to rebuild the sidebar.
- **Design pieces.** New operations, new connectors, new shapes. See [Hardware](/SCuLPTER/documentation/hardware).
- **Teach with it.** Run a workshop and tell me how it went!

## Research

SCuLPT was born as a research project at Universidad de los Andes, exploring programming as a tangible and artistic experience. We have tested it in workshops with children, teenagers, university students, programmers and artists. If you want to dig deeper into the design, the evaluation, or the full Turing completeness proof, these are the documents:

- **SCuLPT: Simple Cubic Language for Programming Tasks**, the thesis on the design and theory of the language. [GitHub](https://github.com/Darkoyd/SCuLPT-Document)
- **ArtLang**, the conference paper presented at CCC'24. [GitHub](https://github.com/Darkoyd/ArtLang)
- **SCuLPT**, the journal paper submitted to The Art, Science, and Engineering of Programming, with the full Turing completeness proof. [GitHub](https://github.com/FLAGlab/sculpt)

Thanks for reading all the way here. Now go make something!
