# Getting Started

This chapter will help you get up and running quickly with your first SCuLPT program.
There are two completely different ways to use SCuLPT: the physical way with the 3D printed components, and the digital way with SCuLPTER. You can choose either or both!
We will cover both methods here. 

## Physical Approach

### Requirements

To use SCuLPT physically, you will need to print/manufacture the components. You can find the 3D models and assembly instructions in the {Hardware} section.
In addition, you will need:
- A 3D printer or access to one.
- Basic tools for part detailing and assembly.
- Dry erase markers.
- A flat surface to work on.
- Melamine board or whiteboard 2x2x2 cm pieces.

It is recommended to have some experience with 3D printing and assembly as the components may require some finishing.
Building the components in other materials (wood, metal, etc.) is also possible if you have the necessary tools and skills.


#### Recommended starting parts

The following list of parts is recommended for building simple physical SCuLPT programs. When 3D printed, the parts should be printed in PLA or PETG for best results. On average, expect to use around one 1kg spool of filament for a full set of components.

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

The quantities above are approximate and will depend on the complexity of the programs you wish to create. It is recommended to print a few extra parts to account for any printing errors or future projects.
Additional parts can be printed as needed. It is also possible to create custom parts for specific needs.

#### Printing the Components

You can download the STL files for 3D printing from the {Hardware} section.
The parts can be printed using standard FDM 3D printers. Resin printing is also possible but may require additional finishing for the screw threads.
Regardless of the printing method, ensure that the parts fit together properly and that the screw threads are functional.
Additional guides for printing and assembly can be found in the {Hardware} section of the documentation.


Once you have printed and assembled the components, you can start creating physical SCuLPT programs!
You can go on with this guide to learn how to use the physical components!


## Digital Approach

You can use SCuLPT through right here! You may have stumbled with SCuLPTER, the online editor and interpreter for SCuLPT code.


### Using SCuLPTER

To start coding in SCuLPT, simply navigate to the SCuLPTER tab in this web page. The interface includes a code editor, consoles for output, and buttons to interact with SCuLPT.
You can write your SCuLPT code in the editor and click the "Run" button to execute it. The output will be displayed in the console below in two distinct formats: the textual output and the visual representation of the code structure.
You can clear the code at any time by clicking the "Clear" button.

#### Lexing vs Parsing

Writing any program in any language can be hard. But understanding how the language is built can help you write better code.
Every programming language has two main steps: lexing and parsing.
- Lexing is the process of breaking down the code into tokens. Tokens are the smallest units of meaning in the code. Visualizing your code as tokens can help you understand how the language works. But more importantly, it can help you identify syntax errors.
- Parsing is the process of analysing the tokens to understand their structure and meaning. This is where the language's grammar comes into play. Visualizing your code as a parse tree can help you understand how the language works. But more importantly, it can help you identify semantic errors.

Syntax errors are mistakes in the structure of the code. They are usually easy to spot and fix. Semantic errors are mistakes in the meaning of the code. They are usually harder to spot and fix.
The SCuLPTER interface allows you to visualize both the lexing and parsing of your code. You can toggle between the two views using the "Lex" and "Parse" buttons.
This can help you understand how the language works and identify errors in your code.

#### Saving and Loading Code

**EVENTUALLY ADD DOWNLOAD BUTTONS**

You can save your SCuLPT code by copying it from the editor and pasting it into a text file on your computer. You can load code by copying it from a text file and pasting it into the editor.


#### Running Code

You can run your SCuLPT code by clicking the "Parse" button. If there are no syntax errors, the interpreter section will appear.
The interpreter section includes a statement counter, the stacks subsection and several buttons to interact with the interpreter.
- The statement counter shows the current statement being executed and the total number of statements in the program.
- The stacks subsection shows the current state of the the program.
- The "Step Forward" and "Step Back" buttons execute the next statement in the program or go back to the previous statement, respectively.

The interpreter will execute the code step by step, allowing you to see how the program state changes with each statement.
You can stop the execution at any time by clicking the "Reset" button. This will reset the program state to the initial state.

For now, you don't need to worry about the details of anything right now. Just know that you can run your code and see the output in the console.
You can go on with this guide to learn how to write SCuLPT code!


## What's Next?

Now that you have SCuLPT either physically, digitally or my personal favourite, both, you're ready to dive deeper into the language!

Happy coding!