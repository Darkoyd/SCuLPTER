# SCuLPTER

## Simple Cubic Language for Programming Tasks's Environment and Runtime

SCuLPTER is a complete environment and runtime for the SCuLPT programming language built with Scala.js, Vite and Laminar.
SCuLPTER allows for lexing, parsing, and executing SCuLPT programs step by step.

SCuLPTER is still in an early phase of development but it is completely functional.

Next steps include adding better tooling and more DX utilities such as error hinting, a functional linter, a language server and syntax highlighting.

SCuLPTER in its current state can be accessed and used for free [here!](https://darkoyd.github.io/SCuLPTER/)

### Setup

#### Dependencies

To run SCuLPTER locally you need:

- [sbt](https://www.scala-sbt.org/) >= 1.7.3
- [node.js](https://nodejs.org/en) >= 16.13.0
- [npm](https://www.npmjs.com/) >= 8.1.0

#### Run locally

Having the dependencies, running is as simple as

Start the SBT server with

```
sbt 
```

Then, inside the sbt CLI, initialize the Scala.js linker with

```
fastLinkJS
// OR
~fastLinkJS //For live updates on file change
```

Lastly, start the local web server with Vite on a separate terminal using

```
npm run dev
```
