# The Language

This is the reference chapter. Everything SCuLPT can do is on this page: the values, the stacks, how to write programs in SCuLPTER, and every one of the 13 operations with an example.
You don't need to read it top to bottom. Use it as a dictionary, come back whenever an operation surprises you. Every example on this page runs in [SCuLPTER](/SCuLPTER/sculpter), so copy, paste, Parse and step through them!

## Values

SCuLPT has one type of value: the **number**. Numbers can be whole (`3`), negative (`-3`) or decimal (`0.5`).

There is also one special value: **nil**, the empty value. Nil means "there is nothing here". You get it when you read from an empty stack, or when you divide by zero. In the papers you may see nil written as `#`. It is the same value.

That's it. No strings, no booleans, no lists. True and false are numbers too: as you will see in [Control Flow](/SCuLPTER/documentation/control-flow), zero and positive numbers count as "yes", negative numbers and nil count as "no".

## Stacks

All values live in **stacks**. A stack is a pile: you can only put things on top, and you can only take things from the top. The last value you put in is the first one you get out. Computer scientists call this FILO (First In, Last Out).

- You can use as many stacks as you want.
- A stack is created the first time you use it, and it starts empty.
- A stack can hold as many values as you want.
- Every stack has a name (a label). Two parameters with the same label are the same stack.

**Every operation consumes the values it reads.** No operation looks at a value and leaves it in place. If you need a value again later, Duplicate it first. This is the single most important habit in SCuLPT!

Throughout this documentation we write stacks with the top on the **right**: `[1, 2, 3]` means 3 is on top. An empty stack is `[]`.

## Writing SCuLPT in SCuLPTER

In SCuLPTER, every block becomes one line of text. The connectors disappear, only the order of the blocks matters. A program reads like a list of instructions:

```sculpt
PUSH circle 4       // block 1
PUSH circle 7       // block 2
ADD circle          // block 3
```

Here are the rules for writing a line:

- **One block per line.** The operation comes first, then the first parameter, then the second one (if any), separated by spaces.
- **Operations** are written in all lowercase or all uppercase: `push` or `PUSH`. Mixed case like `Push` will not work.
- **Question** is written as a question mark: `? circle`.
- **Stack names** are words made of letters, digits and underscores, starting with a letter: `circle`, `triangle`, `big_star`, `stack2`. Names are case sensitive, so `circle` and `Circle` are two different stacks.
- **Stack names cannot be operation names.** A stack called `cmp`, `add` or `nil` will confuse the parser. Stick to shapes and you will be fine!
- **Numbers** are written as usual: `4`, `-4`, `0.25`.
- **nil** is written as `nil` (or `NIL`).
- **Comments** start with `//` and go until the end of the line. Use them to explain your program to others (and to your future self).

**Pro tip**: Blank lines and lines that only have a comment are **not** blocks. They are skipped, and they don't count when you Jump. This will matter a lot in [Control Flow](/SCuLPTER/documentation/control-flow), so keep it in mind.

## Stack operations

### Push

Puts a number on top of a stack. The second parameter is always a value (a number or nil), never a stack. To move a value from one stack to another, use Move.

```sculpt
PUSH circle 5       // circle: [5]
PUSH circle -2      // circle: [5, -2]
PUSH circle nil     // circle: [5, -2, nil]
```

### Pop

Removes the top value of a stack and throws it away. To keep the value, use Move instead. Popping an empty stack does nothing.

```sculpt
PUSH circle 1
PUSH circle 2
POP circle          // circle: [1]
```

### Move

Takes the top value of the **second** stack and puts it on top of the **first** one. Read it as "move into circle, from triangle". If the second stack is empty, Move puts nil on the first stack.

```sculpt
PUSH triangle 1
PUSH triangle 2
MOV circle triangle // circle: [2], triangle: [1]
MOV circle square   // square is empty, so circle: [2, nil]
```

### Duplicate

Copies the top value of a stack and puts the copy on top. If the stack is empty, Duplicate puts nil on it.

```sculpt
PUSH circle 3
DUP circle          // circle: [3, 3]
```

Since every operation consumes its values, you will use Duplicate all the time to keep a copy around.

## Arithmetic operations

Addition, Subtraction, Multiplication, Division and Modulo all come in two forms.

**With one parameter** (a stack), the operation takes the top value, then the value below it, and puts the result back on the stack:

```sculpt
PUSH circle 4
PUSH circle 7
ADD circle          // 7 + 4, circle: [11]
```

**With two parameters** (a stack and a value), the operation takes only the top value of the stack and uses the second parameter as the other number:

```sculpt
PUSH circle 11
MUL circle 3        // 11 * 3, circle: [33]
```

**Watch the order!** In both forms, the value from the top of the stack comes **first**. This matters for Subtraction, Division and Modulo:

```sculpt
PUSH square 3
PUSH square 10
SUB square          // 10 - 3, square: [7]
```

If you pushed 10 first and then 3, you would get 3 - 10 = -7. When in doubt, push the second number first and the first number last.

| Operation | One parameter | Two parameters |
|-----------|---------------|----------------|
| Addition | `ADD s` | `ADD s n` |
| Subtraction | `SUB s` | `SUB s n` |
| Multiplication | `MUL s` | `MUL s n` |
| Division | `DIV s` | `DIV s n` |
| Modulo | `MOD s` | `MOD s n` |

**Division and modulo by zero** don't break your program. They put nil on the stack instead, so you can test for it later. Any **other** arithmetic on a nil value stops the program with an error. So does the one parameter form on a stack with fewer than two values.

### Negation

Negation is the odd one out: it only takes one stack. It takes the top value and puts back its negative.

```sculpt
PUSH circle 3
NEG circle          // circle: [-3]
```

## Flow operations

These three operations are the heart of decisions and loops. Here is what each one does, and [Control Flow](/SCuLPTER/documentation/control-flow) shows how to combine them.

### Compare

**With one parameter**, Compare takes the top value, then the value below it, and puts back:

- `1` if the top value is greater,
- `0` if they are equal,
- `-1` if the top value is lesser.

```sculpt
PUSH circle 2
PUSH circle 5
CMP circle          // 5 is greater than 2, circle: [1]
```

**With two parameters**, Compare takes only the top value and compares it against the second parameter. The value from the stack comes first.

```sculpt
PUSH circle 2
CMP circle 5        // 2 is lesser than 5, circle: [-1]
```

Compare is the only operation that is happy to work with nil. If both values are nil, the result is `1`. If only one of them is nil, the result is nil. A missing value (an empty stack) counts as nil. This gives you a simple test for "is this nil?":

```sculpt
CMP circle nil      // 1 if the top of circle was nil, nil otherwise
```

### Jump

Moves the execution forward or backward by a number of blocks. `JMP 1` goes to the next block (so it does nothing), `JMP 2` skips one block, `JMP -3` goes back three blocks.

```sculpt
PUSH circle 1
JMP 2               // skip the next block
PUSH circle 99      // never runs
PUSH circle 2       // circle: [1, 2]
```

The number can also come from a stack. In that case Jump takes the top value of the stack and uses it. Decimals are cut down to whole numbers.

```sculpt
PUSH steps 2
JMP steps           // same as JMP 2
```

### Question

Takes the top value of a stack and decides whether to run the next block:

- If the value is **zero or positive**, the next block runs.
- If the value is **negative or nil**, the next block is skipped.

```sculpt
PUSH circle -1
? circle            // -1 is negative...
PUSH circle 99      // ...so this block is skipped
PUSH circle 1       // circle: [1]
```

Question on an **empty** stack stops the program with an error. Question on a stack whose top value is nil simply skips.

## Errors

Most mistakes in SCuLPT are gentle: empty stacks give nil, division by zero gives nil. But a few things stop the program:

| What happened | Example |
|---------------|---------|
| Arithmetic on a nil value | `PUSH c nil` then `ADD c 1` |
| One parameter arithmetic with fewer than two values | `PUSH c 1` then `ADD c` |
| Negation on an empty stack or on nil | `NEG c` on `[]` |
| Question on an empty stack | `? c` on `[]` |
| Jump with a nil offset | `JMP c` on `[nil]` |

Syntax mistakes, like a missing parameter or a stack named after an operation, are caught when you click Parse, before the program runs. Check the Parser Output to see what went wrong.

## Cheat sheet

| Block | Does |
|-------|------|
| `PUSH s n` | put n on s |
| `POP s` | throw away the top of s |
| `MOV a b` | move the top of b onto a |
| `DUP s` | copy the top of s |
| `ADD s` / `ADD s n` | top + next, or top + n |
| `SUB s` / `SUB s n` | top - next, or top - n |
| `MUL s` / `MUL s n` | top * next, or top * n |
| `DIV s` / `DIV s n` | top ÷ next, or top ÷ n |
| `MOD s` / `MOD s n` | top % next, or top % n |
| `NEG s` | -top |
| `CMP s` / `CMP s n` | compare top with next, or with n: 1, 0 or -1 |
| `JMP n` / `JMP s` | move n blocks, or (top of s) blocks |
| `? s` | run next block if top of s is 0 or more |
