# Hardware

This chapter is for the makers. Here you will find every physical piece of SCuLPT, the files to print it, and tips to put it all together.
All the 3D models are open source and live in the [SCuLPT repository](https://github.com/Darkoyd/SCuLPT). Print them, remix them, make them bigger, smaller, prettier. If you improve something, please share it back!

## The pieces

SCuLPT pieces come in two families. **Computational pieces** are the language itself: blocks, operations and parameters. **Structural pieces** hold the sculpture together: connectors, nuts and wedges. In the renders below each family has its own colour, but you can print them in any colour you like.

### Blocks

![One parameter block | center](/SCuLPTER/imgs/Renders/1.png)

The **one parameter block** holds an operation and a single parameter. Use it for Pop, Duplicate, Negation, Jump, Question, and the one parameter forms of arithmetic and Compare.

![Two parameter block | center](/SCuLPTER/imgs/Renders/2.png)

The **two parameter block** holds an operation and two parameters. Use it for Push, Move, and the two parameter forms of arithmetic and Compare.

Both blocks share the same anatomy: a screw, an operation slot, parameter holders with magnets, a wedge slot on the side and an output hole. See [Start Sculpting](/SCuLPTER/documentation/start-sculpting) for a tour.

### Operations

![Operations | center](/SCuLPTER/imgs/Renders/3.png)

Operation tiles slide into the operation slot of a block. Each one has its symbol carved on top. Print them with the symbol facing up for the cleanest result, and consider a filament change (or a paint marker) to make the symbols pop.

### Parameters

The parameter has a magnet, so it snaps onto a parameter holder. It is a 2 x 2 cm block of melamine (whiteboard material), so you can write stack labels and values with a dry erase marker, and wipe them off later.

### Connectors

![Connector | center](/SCuLPTER/imgs/Renders/8.png)

The **connector** is a straight rod with a flat end and a spherical end. Execution flows from the flat end to the spherical end.

![T Connector | center](/SCuLPTER/imgs/Renders/7.png)

The **T connector** merges two paths into one. It is how you close a physical loop.

![Male connector | center](/SCuLPTER/imgs/Renders/6.png)

The **male connector** screws onto a block and receives the end of a connector.

### Fixtures

![Nut | center](/SCuLPTER/imgs/Renders/4.png)

The **nut** locks a threaded connection in place, so your sculpture doesn't fall apart when you pick it up.

![Wedge | center](/SCuLPTER/imgs/Renders/5.png)

The **wedge** fits into the wedge slot on the side of a block. Anything attached through a wedge is structure, not code: use it for bases, supports and decorations.

## Files

### Ready to print

These STL files are ready to slice. Click a name to download it.

| Part | File | Notes |
|------|------|-------|
| One parameter block | [1 Param Block v9.stl](https://github.com/Darkoyd/SCuLPT/raw/main/STLs/1%20Param%20Block%20v9.stl) | No supports on the screw section |
| Two parameter block | [2 Param Block v7.stl](https://github.com/Darkoyd/SCuLPT/raw/main/STLs/2%20Param%20Block%20v7.stl) | No supports on the screw section |
| Male connector | [Block Male Connector v2.stl](https://github.com/Darkoyd/SCuLPT/raw/main/STLs/Block%20Male%20Connector%20v2.stl) | |
| Nut | [Nut v7.stl](https://github.com/Darkoyd/SCuLPT/raw/main/STLs/Nut%20v7.stl) | No supports |
| Parameter | [Parameter v4.stl](https://github.com/Darkoyd/SCuLPT/raw/main/STLs/Parameter%20v4.stl) | |
| Wedge | [Wedge v3.stl](https://github.com/Darkoyd/SCuLPT/raw/main/STLs/Wedge%20v3.stl) | No supports on the screw section |

### Editable models

Want to change the size, the ergonomics, or design your own operation? The original Fusion 360 models are available here:

| Model | Link |
|-------|------|
| One parameter block | [a360.co/4dIMM4s](https://a360.co/4dIMM4s) |
| Two parameter block | [a360.co/3T9inE2](https://a360.co/3T9inE2) |
| Connectors 1 | [a360.co/4dG0lBF](https://a360.co/4dG0lBF) |
| Connectors 2 | [a360.co/4755mkN](https://a360.co/4755mkN) |
| Operations | [a360.co/3MthmTE](https://a360.co/3MthmTE) |

Some pieces have no STL of their own yet. The straight connector, the T connector and the operation tiles live inside these Fusion 360 files, several pieces per file. Open the file and export the piece you need as STL.

## Printing

**Material**: PLA or PETG work best. A full starter set takes about one 1 kg spool. The full parts list is in [Getting Started](/SCuLPTER/documentation/getting-started).

**Supports**: Never put supports inside a thread. Threads printed on supports come out rough and won't screw properly. Orient the blocks, nuts and wedges so the thread prints standing up.

**Tolerances**: Every printer is a little different. Print one block, one nut and one connector first and check that they fit before you print the whole set. If the threads are too tight, scale the nut up slightly (1 to 2 percent) in your slicer. If the magnets don't fit their holes, adjust the hole size in the editable model.

**Resin**: Resin printing gives beautiful parts, but the threads usually need some extra sanding to work smoothly.

**Print extras**: Small parts like nuts and connectors get lost. Workshops always lose a few. Print more than you think you need!

## Assembly

**Parameters**: Cut melamine or whiteboard pieces into 2 x 2 cm blocks. Each one gets a magnet, either pressure fit or glued with epoxy. Do the same with the parameter holders of every block. Don't worry about polarity: as long as the parameter sticks to the block, you are good to go.

**Operations**: Slide an operation tile into the slot next to the screw. The tile should fit snugly, but come out with a little push so you can reuse blocks.

**Connecting blocks**: Join the output hole of one block to the screw of the next with a connector, flat end first. Lock each joint with a male connector and a nut. The order is all that matters for the program: use longer chains, T connectors and wedges to give your sculpture any shape you want.

![Construction | center](/SCuLPTER/imgs/Figures/construction.png)

## Make it your own

SCuLPT is meant to be remixed. Some ideas we have seen or dreamed of:

- Print each family in its own colour to make programs easier to read.
- Print the blocks bigger for younger hands, or smaller for jewelry.
- Make pieces in wood, metal or clay.
- Design a base that holds a whole program upright as a sculpture.

If you make something cool, tell me on [GitHub](https://github.com/Darkoyd/SCuLPT) or on Discord. I'd love to see it!
