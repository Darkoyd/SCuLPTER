# Start Sculpting!

Learning is best done by practice. We will build a simple program while I tell you about the blocks, operations, how they work and how to SCuLPT!
If you are here on a digital only quest, feel free to skip ahead to [The Language](/SCuLPTER/documentation/the-language). Here we will be focusing more on the **shape** of SCuLPT, rather than its function.
I highly encourage you to try SCuLPT as more than a programming language. You can even use pen and paper. Creativity and physical interaction with the language are the other half of SCuLPT.

## Blocks of all shapes and colours

Once you have a set of pieces at your disposal, you are ready to code!
But where do you even start?
Using blocks, of course. Take a look at them:

![Blocks | center](/SCuLPTER/imgs/Renders/Blocks%20Render.png)

These are the stars of SCuLPT.
These are the two parameter block and one parameter block.
Let's take a closer look at one. Grab one for yourself and let's identify its parts.

![Blocks | center](/SCuLPTER/imgs/Renders/BlockAnatomy.png)

A block is composed of 5 distinctive features.

- **Screw**: The screw is the first feature of a block. It serves two purposes. It connects blocks and other components to each other (we will review the other shapes that make this connection in a bit). It also shows the input of a block. Blocks are read from the screw onwards. Always. No matter the orientation.
- **Operation Slot**: The slot next to the screw holds operations. We will check them in a bit. Operations tell the block what to **do**.
- **Parameter Holder**: A block may have one or two of these slots. They have a magnet that holds a parameter block. Parameters tell the block what to **use**.
- **Wedge Slot**: On the side of the block there is a slot to connect more pieces. The side of the block does **not** serve a computational purpose, but rather a structural one. Blocks connected to or through this opening will not connect on a logic level to the current block.
- **Output Hole**: On the opposite end of the screw there is a small hole, it fits more pieces to be connected. Other blocks connected using this hole will be connected logically, the blocks will be executed in order.

To sum up, blocks hold operations and parameters, they are the core of the language.

Which block should you grab? It depends on the operation. Some operations need only one parameter (like Pop, which only needs to know **which** stack to pop), and some need two (like Push, which needs a stack **and** a number). A one parameter block holds one parameter, a two parameter block holds two. That's the whole rule.

## Operations

An operation is a small tile that slides into the operation slot of a block. Each tile has a symbol carved on it, and that symbol is the whole instruction: it tells the block what to **do** with its parameters.

![Operations | center](/SCuLPTER/imgs/Renders/Ops%20Front.png)

SCuLPT has exactly 13 operations. Here they are, grouped by what they do. Don't worry about memorizing them now, [The Language](/SCuLPTER/documentation/the-language) section explains every single one with examples.

**Stack operations** move values around.

| Operation | Symbol | Block | What it does |
|-----------|--------|-------|--------------|
| Push | ↓ | 2 parameters | Puts a number on top of a stack |
| Pop | ↑ | 1 parameter | Throws away the top value of a stack |
| Move | ↓ into a tray | 2 parameters | Takes the top value of one stack and puts it on another |
| Duplicate | two overlapping squares | 1 parameter | Copies the top value of a stack |

**Arithmetic operations** do math.

| Operation | Symbol | Block | What it does |
|-----------|--------|-------|--------------|
| Addition | + | 1 or 2 parameters | Adds |
| Subtraction | − | 1 or 2 parameters | Subtracts |
| Multiplication | ✱ | 1 or 2 parameters | Multiplies |
| Division | / | 1 or 2 parameters | Divides |
| Modulo | % | 1 or 2 parameters | Gives the remainder of a division |
| Negation | ¬ | 1 parameter | Flips the sign of a number |

**Flow operations** decide what runs next.

| Operation | Symbol | Block | What it does |
|-----------|--------|-------|--------------|
| Compare | <=> | 1 or 2 parameters | Compares two values and leaves 1, 0 or -1 |
| Jump | ↺ | 1 parameter | Moves the execution forward or backward some blocks |
| Question | ? | 1 parameter | Skips the next block if a value is negative |

**A note on Pop**: Pop and Push are the same idea in opposite directions, so the Pop tile is the Push arrow pointing **up**. If your set has no separate Pop tile, simply slide a Push tile in upside down.

## Parameters

Parameters are the small cubes with a melamine (whiteboard) face that snap onto the parameter holders of a block through a magnet. They tell the block what to **use**. A parameter is blank until you write on it with a dry erase marker, and there are only two kinds of things you can write:

- **A stack label**: any symbol you like. A circle, a triangle, a star, a tiny drawing of your cat. The label is the **name** of a stack, and every parameter with the same label refers to the same stack.
- **A value**: a number, like `3`, `-1` or `0.5`. Values are used as they are, they never change.

![Stacks | center](/SCuLPTER/imgs/Renders/Stacks.png)

Here you can see two stacks, the star stack and the triangle stack, each one holding four values. You don't need to build stacks physically, they live in your head (or on a piece of paper next to your sculpture). The labels on your parameters are how you point at them.

**Why symbols and not names?** Because drawing a triangle is faster than writing "accumulator", and anyone can read it, even someone who doesn't share your language. When you move to SCuLPTER, you will write the symbol as a word instead: `triangle`, `star`, `circle`.

There is one special value you will meet sooner or later: **nil**, the empty value. Reading from an empty stack gives you nil. In the papers you may see it written as `#`. In SCuLPTER you write it as `nil`. We will see how to use it in [Control Flow](/SCuLPTER/documentation/control-flow).

**Pro tip**: Use the first parameter holder (the one closest to the screw) for the stack the operation works on. In SCuLPT the first parameter is always a stack. The second parameter can be a stack or a value, depending on the operation.

## Connectors and fixtures

Blocks, operations and parameters are the language. Connectors and fixtures are the **sculpture**. They hold the program together and give it its shape, but they do not compute anything.

| Piece | What it is for |
|-------|----------------|
| Connector | A straight rod. Flow goes from its flat end to its spherical end. |
| T Connector | Joins two paths into one. This is how you close a loop. |
| Male Connector | Screws onto the screw of a block so a connector can enter it. |
| Nut | Locks a screw connection in place. |
| Wedge | Fits in the wedge slot of a block to attach structure that is **not** part of the program, like a base or a decoration. |

![Construction | center](/SCuLPTER/imgs/Figures/construction.png)

The image shows the different ways pieces connect: an operation sliding into a block, two blocks joined by connectors and a T, the chain of nut, male connector and connector going into a block, and a wedge holding a block from its side.

Here is the most important idea of this section: **two sculptures with different shapes can be exactly the same program**. Only the order of the blocks matters. Long connectors, short connectors, a block hanging upside down from a wedge, a tower, a spiral, it's all the same to the computer. The shape is yours to play with. Artists who tried SCuLPT used this freedom more than any other feature!

## Reading a sculpture

Running a physical SCuLPT program is something you do with your eyes and your hands. Here are the rules:

- Start at the block that has nothing connected to its screw. That is the first block of the program.
- Read each block from the screw onwards: first the operation, then the first parameter, then the second.
- Do what the block says to your stacks (write the stacks on a paper, or use a whiteboard).
- Follow the connector that comes out of the output hole to the next block. Connectors always flow from their flat end to their spherical end.
- When a block has nothing connected to its output hole, the program ends.

**Loops** can be built physically: connect the end of a chain back into an earlier point with a T connector, and the execution goes around and around. Physical loops need some space, the smallest one you can build with the current pieces has five blocks. For tighter loops, use the Jump operation instead.

**There is no split.** You may wonder why there is a T to merge paths, but no Y to split them. A split would mean the program runs two ways at once, and you would need to keep two copies of every stack to follow it. Instead, decisions are made with the Question operation, which simply skips a block. You will learn all about it in [Control Flow](/SCuLPTER/documentation/control-flow).

## Your first sculpture

Time to build! Let's make a program that adds two numbers. You will need two two parameter blocks, one one parameter block, a few connectors, two Push tiles and one Addition tile.

- **Block 1**: a two parameter block with Push, a circle, and the value 4.
- **Block 2**: a two parameter block with Push, a circle, and the value 7.
- **Block 3**: a one parameter block with Addition and a circle.

Connect them in order, output hole to screw. Now run it with your finger and a paper:

| Block | Operation | Circle stack after |
|-------|-----------|--------------------|
| 1 | Push circle 4 | [4] |
| 2 | Push circle 7 | [4, 7] |
| 3 | Add circle | [11] |

We write stacks with the top on the **right**, so `[4, 7]` means 7 is on top. Addition took the two top values of circle, added them, and put back the result. Congratulations, you just sculpted your first program!

The same program in SCuLPTER looks like this:

```sculpt
PUSH circle 4
PUSH circle 7
ADD circle
```

Paste it into the [SCuLPTER](/SCuLPTER/sculpter) editor, click Parse and step forward. You should see the same stack as on your paper.

## A bigger sculpture: Fibonacci

Here is a sculpture that generates the Fibonacci numbers (0, 1, 1, 2, 3, 5, 8...) forever, filling the circle stack with them.

![Fibonacci sculpture | center](/SCuLPTER/imgs/Figures/fibonacci_render.png)

Blocks 1 to 4 prepare the stacks. Blocks 5 to 14 are the loop: after block 14, the T connector under block 4 sends the execution back into block 5, and it starts again. No Jump, no Question, just shape. Try to follow it with your finger! You will find the full step by step walkthrough in the [Tutorials](/SCuLPTER/documentation/tutorials).

## What's Next?

You know the pieces and how to read them. Now it's time to learn what each operation does exactly. Head over to [The Language](/SCuLPTER/documentation/the-language) for the full reference, or jump straight to the [Tutorials](/SCuLPTER/documentation/tutorials) if you learn best by doing.

Keep sculpting!
