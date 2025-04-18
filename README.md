# SCuLPTER
## Simple Cubic Language for Programming Tasks's Environment and Runtime

SCuLPTER is a complete environment and runtime for the SCuLPT programming language built with Scala.js, Vite and Laminar.
SCuLPTER allows for lexing, parsing, and executing SCuLPT programs step by step.

SCuLPTER is still in an early phase of development but it is completely functional.

Next steps include adding better tooling and more DX utilities such as error hinting, a functional linter, a language server and syntax highlighting.

### Setup

#### Dependencies
To run SCuLPTER locally you need:

- sbt
- node.js

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



