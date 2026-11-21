# Control Flow

So far our programs run from the first block to the last, one after the other. That's fine for adding two numbers, but real programs need to **decide** and to **repeat**. In SCuLPT you have three tools for that: the shape of your sculpture, the Jump operation, and the Question operation.
This chapter is where SCuLPT starts to feel like a puzzle. Take it slowly, step through every example in [SCuLPTER](/SCuLPTER/sculpter), and watch the statement counter move. It will click!

## How execution flows

Execution always starts at the first block and moves one block at a time.

- In a **sculpture**, the next block is the one connected to the output hole, following the connector from its flat end to its spherical end.
- In **SCuLPTER**, the next block is the next line. Blank lines and comment lines don't count.
- When there is no next block, the program ends.

That's the default. Jump and Question are the only two operations that change it.

## Jumping around

Jump moves the execution by a number of blocks, counted from the Jump block itself:

| Block | Goes to |
|-------|---------|
| `JMP 1` | the next block (does nothing special) |
| `JMP 2` | skips one block |
| `JMP -1` | the block right before the Jump |
| `JMP -4` | four blocks back |
| `JMP 0` | the Jump itself, forever. Don't do this! |

To find the right number, count the blocks: put your finger on the Jump, and count how many blocks you need to move to reach the target. Forward is positive, backward is negative.

Jumping past the last block ends the program. This is a handy way to say "we are done".

**Watch out**: In SCuLPTER, comment lines and blank lines are not blocks. If you count them, your jumps will land in the wrong place. A good habit is to put comments at the **end** of a line, next to the block they explain.

## Asking questions

Question takes the top value of a stack and uses it to decide if the next block runs:

- **zero or positive**: the next block runs, as usual.
- **negative or nil**: the next block is skipped.

Question only ever affects **one** block, the one right after it. The trick is to make that block a Jump! A Question followed by a Jump is SCuLPT's "if": if the value is zero or positive, jump somewhere, otherwise keep going.

Remember that **zero counts as yes**. This surprises everyone at least once (it surprised me too).

## Asking the right question

Question only knows "negative or not". To ask anything else, you prepare a number first, usually with Compare.

**Is a less than b?** Compare gives -1 when the top value is lesser. So `CMP a 10` followed by `? a` skips the next block while a is less than 10, and runs it once a reaches 10 or more.

**Is a equal to b?** Compare gives 0 only when they are equal, and ±1 otherwise. Multiply the result by itself and negate it: you get 0 when equal and -1 when not.

```
DUP a               // keep a copy of a
CMP a 3             // 0 if a = 3, otherwise 1 or -1
DUP a
MUL a               // 0 if a = 3, otherwise 1
NEG a               // 0 if a = 3, otherwise -1
? a                 // runs the next block only if a = 3
```

**Is the stack empty?** See the next section, it deserves its own.

**Pro tip**: Question and Compare consume the value they read. If you still need it afterwards, Duplicate it first. Most "my program lost a number" bugs come from here.

## Testing for nil

Many loops need to stop when a stack runs out of values. When you read from an empty stack, you get nil. And Compare has a special rule for nil: comparing nil with nil gives `1`, comparing nil with a number gives nil. Put those together and you get this little recipe, which you will use again and again:

```
DUP input           // copy the top (or nil, if input is empty)
CMP input nil       // 1 if it was nil, nil otherwise
? input             // runs the next block only if input was empty
```

After these three blocks, `input` is exactly as it was before. Nothing was lost! You can also use the same recipe after a Division or Modulo, to check if you divided by zero.

**Why not just Question the stack?** Question on an **empty** stack stops the program with an error. The recipe above is the safe way to ask.

## Loops

A loop is a group of blocks that runs again and again. There are two ways to build one.

**Physical loops.** In a sculpture, connect the last block of the group back to the first one with a T connector. The execution goes around forever. This is beautiful, but it needs space (at least five blocks) and it never stops on its own. The Fibonacci sculpture in [Start Sculpting](/SCuLPTER/documentation/start-sculpting) is a physical loop.

**Jump loops.** Put a negative Jump at the end of the group. This works both in sculptures and in SCuLPTER (where physical loops don't exist). Combine it with a Question to decide when to stop.

Here is a countdown. It starts with 3 in n and counts down until n reaches 0:

```
PUSH n 3
SUB n 1             // loop starts here
DUP n
SUB n 1             // the next value of n
? n                 // still 0 or more?
JMP -4              // then back to SUB n 1
```

Let's follow it:

| Block | n after | Comment |
|-------|---------|---------|
| PUSH n 3 | [3] | |
| SUB n 1 | [2] | |
| DUP n | [2, 2] | |
| SUB n 1 | [2, 1] | |
| ? n | [2] | 1 is positive, so the Jump runs |
| JMP -4 | [2] | back to SUB n 1 |
| ... | ... | the loop runs again with 2, then with 1 |
| ? n | [0] | -1 is negative, so the Jump is skipped |

The program ends with n at 0.

Why the extra `SUB n 1` before the Question? Because zero counts as yes! If we asked about n itself, the loop would run one more time and end at -1. Try it: delete the second `SUB n 1`, change the Jump to `JMP -3` and see what happens.

## Leaving a loop

Sometimes it's easier to jump **out** of a loop than to jump back in. Put the Question and a forward Jump in the middle of the loop, and a backward Jump at its end. Here's a program that adds the numbers from 1 to 10:

```
PUSH counter 0
PUSH total 0
ADD counter 1       // loop starts here
DUP counter
MOV total counter
ADD total
DUP counter
CMP counter 10      // -1 while counter < 10
? counter
JMP 2               // counter reached 10: leave the loop
JMP -8              // back to ADD counter 1
```

While counter is less than 10, Compare gives -1, Question skips the `JMP 2`, and `JMP -8` takes us back to the start of the loop. When counter reaches 10, Compare gives 0, Question lets `JMP 2` run, and we jump past the last block: the program ends with 55 in total.

## Common mistakes

- **Counting comment lines in a Jump.** Only blocks count.
- **Forgetting that zero is yes.** Your loop runs one time too many.
- **Forgetting that everything consumes.** Compare, Question and arithmetic all eat their values. Duplicate first.
- **Subtracting in the wrong order.** The top of the stack comes first: push the second number first.
- **Questioning an empty stack.** Use the nil test instead.

Ready to put it all together? The [Tutorials](/SCuLPTER/documentation/tutorials) are waiting!
