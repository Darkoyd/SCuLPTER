# Start Sculpting!

Learning is best done by practice. We will build a simple program while I tell you about the blocks, operations, how they work and how to SCuLPT!
If you are here on a digital only quest, feel free to skip this section, here we will be focusing more on the _shape_ of SCuLPT, rather than its function.
I highly encourage you to try SCuLPT as more than a programming language. You can even use pen and paper. Creativity and physical interaction with the language are the the other half of SCuLPT.

## Blocks of all shapes and colours

Once you have a set of pieces at your disposal, you are ready to code!
But where do you event start?
Using blocks, of course. Take a look at them:

![Blocks | center](/SCuLPTER/imgs/Renders/Blocks%20Render.png)

These are the stars of SCuLPT.
These are the two parameter block and one parameter block.
Let's take a closer look at one. Grab one for yourself and let identify their parts.

![Blocks | center](/SCuLPTER/imgs/Renders/BlockAnatomy.png)

A block is composed of 5 distinctive features.

- *Screw*: The screw is the first feature of a block. It serves two main purposes.
  - Connect blocks and other components to each other. We will review other shapes that allow us the connection.
  - Show the input of a block. Blocks are read from the screw onwards. Always. No matter the orientation.
- *Operation Slot*: The slot next to the screw holds operations. We will check them in a bit. Operations tell the block what to _do_.
- *Parameter Holder*: A block may have one or two of these slots. They have a magnet that holds a parameter block. Parameters tell the block what to _use_.
- *Wedge Slot*: On the side of the block there is a slot to connect more pieces. The side of the block does _not_ serve a computational purpose, but rather a structural one. Blocks connected to or through this opening will not connect on a logic level to the current block.
- *Output Hole*: On the opposite end of the screw there is a small hole, it fits more pieces to be connected. Other blocks connected using this hole will be connected logically, the blocks will be executed in order.