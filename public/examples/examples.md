# Examples

A collection of ready to run SCuLPT programs. Click **Open in SCuLPTER** under any of them to load it into the editor, then Parse and step through it.
These are short on explanation on purpose. If you want the full walkthroughs, head to the [Tutorials](/SCuLPTER/documentation/tutorials). If an operation puzzles you, [The Language](/SCuLPTER/documentation/the-language) has the answer.

We write stacks with the top on the **right**: `[1, 2, 3]` means 3 is on top.

## Warming up

### Add and multiply

Computes (4 + 7) * 3. Leaves `circle: [33]`.

```sculpt
PUSH circle 4
PUSH circle 7
ADD circle
MUL circle 3
```

### Absolute value

Turns a number positive. Leaves `x: [8]`. Try it with a positive number too!

```sculpt
PUSH x -8
DUP x
CMP x 0             // -1 if x is negative
? x
JMP 2               // x is 0 or positive: skip NEG
NEG x
```

### The biggest of two

Puts the bigger of `a` and `b` into `max`. Leaves `max: [12]`.

```sculpt
PUSH a 7
PUSH b 12
DUP a
MOV t a
DUP b
MOV t b             // t: [a, b]
CMP t               // 1 if b > a, 0 if equal, -1 if b < a
? t
JMP 3               // b wins: go to MOV max b
MOV max a
JMP 2               // skip MOV max b
MOV max b
```

### Countdown

Counts n down to 0. Leaves `n: [0]`. Notice the extra `SUB n 1`: zero counts as yes for Question!

```sculpt
PUSH n 3
SUB n 1
DUP n
SUB n 1
? n
JMP -4
```

### Factorial

Computes 5! = 5 * 4 * 3 * 2 * 1. Leaves `result: [120]`.

```sculpt
PUSH n 5
PUSH result 1
DUP n               // loop starts here
MOV result n
MUL result          // result = n * result
SUB n 1
DUP n
SUB n 1
? n                 // keep going while n is 1 or more
JMP -7              // back to DUP n
```

### Reverse a stack

Moves every value of `input` to `output`, which flips their order. Leaves `output: [3, 2, 1]`.

```sculpt
PUSH input 1
PUSH input 2
PUSH input 3
DUP input           // loop starts here
CMP input nil
? input
JMP 3               // input is empty: done
MOV output input
JMP -5              // back to DUP input
```

## The classics

### Sum from 1 to 10

Leaves `total: [55]`.

```sculpt
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

### Map: squares

Squares every number of `input`. Leaves `output: [4, 9, 16]`.

```sculpt
PUSH input 4
PUSH input 3
PUSH input 2
DUP input           // loop starts here
CMP input nil       // 1 if input is empty, nil if not
? input
JMP 5               // input is empty: leave the loop
DUP input
MUL input
MOV output input
JMP -7              // back to DUP input
```

### Filter: even and odd

Splits `input` in two. Leaves `even: [2, 8]` and `odd: [7, 5]`.

```sculpt
PUSH input 8
PUSH input 5
PUSH input 2
PUSH input 7
DUP input           // loop starts here
CMP input nil
? input
JMP 10              // input is empty: leave the loop
DUP input
MOD input 2         // 0 if even, 1 if odd
NEG input           // 0 if even, -1 if odd
? input
JMP 3               // even: go to MOV even input
MOV odd input
JMP -10             // back to the start of the loop
MOV even input
JMP -12             // back to the start of the loop
```

### Fibonacci

Fills `circle` with Fibonacci numbers, forever. Step as long as you like.

```sculpt
PUSH hexagon 0
PUSH circle 0
PUSH circle 1
PUSH triangle 1
DUP triangle
DUP hexagon
MOV trapezoid hexagon
MOV trapezoid triangle
ADD trapezoid
POP hexagon
MOV hexagon triangle
DUP trapezoid
MOV circle trapezoid
MOV triangle trapezoid
JMP -10
```

### Insertion sort

Sorts `input` into `sorted`, smallest at the bottom. Leaves `sorted: [-3, 1, 2, 4, 4, 5]`.

```sculpt
PUSH input 5
PUSH input 1
PUSH input 4
PUSH input 2
PUSH input 4
PUSH input -3
DUP input           // outer loop: take the next number
CMP input nil
? input
JMP 17              // input is empty: done
MOV hold input
DUP sorted          // inner loop: find the place for hold
CMP sorted nil
? sorted
JMP 10              // sorted is empty: place hold
DUP sorted
MOV pair sorted
DUP hold
MOV pair hold
CMP pair             // hold vs top of sorted
? pair
JMP 3               // hold >= top: place hold
MOV input sorted    // top is bigger: give it back to input
JMP -12             // back to the inner loop
MOV sorted hold     // place hold
JMP -19             // back to the outer loop
```

## Challenge: heapsort

Insertion sort is easy to sculpt, but it gets slow with long lists. Heapsort is one of the classic fast sorting algorithms. It's also a real challenge in SCuLPT, because heapsort needs an **array**: it keeps jumping between positions, from a value to its children and back. Stacks only let you see the top!

Here is how this program pulls it off:

- **An array made of two stacks.** The values live in `high`, with position 1 on top. To reach position k, we Move k - 1 values from `high` to `low`, and the value we want is now on top of `high`. To go back to the start, we Move everything from `low` back to `high`. Think of it as a cursor sliding along a row of cards.
- **Variables are stacks with one value.** `size` is the size of the heap, `i` is the position we are sinking, `c` is its child, `v` is its value.
- **A function!** Sinking a value down the heap (SIFT) is needed in two places. Instead of writing it twice, the program jumps into it and jumps back out. Before jumping in, the caller pushes into `ret` how far the final `JMP ret` must jump to come back to it. That's `JMP` with a stack as parameter, in action.

The program first counts the values, then turns the array into a heap (every parent is bigger than its children), and then, again and again, swaps the biggest value to the end of the array and sinks the new first value back into place. At the end, everything is moved into `result`.

Leaves `result: [-3, 1, 2, 4, 4, 5]`, smallest at the bottom. To sort your own numbers, change the `PUSH high` blocks at the top: the program counts them by itself. With 216 blocks and about 1,600 steps, this is the longest program on this page. Use it to impress your friends.

```sculpt
PUSH high -3
PUSH high 4
PUSH high 2
PUSH high 4
PUSH high 1
PUSH high 5
PUSH size 0
DUP high            // count the values into size
CMP high nil
? high
JMP 4
MOV low high
ADD size 1
JMP -6
DUP size            // start = size / 2, rounded down
MOV start size
DUP start
MOD start 2
SUB start
NEG start
DIV start 2
DUP start           // build the heap: sift every parent, last to first
MOV t start
SUB t 1
? t
JMP 2
JMP 7
DUP start
MOV i start
PUSH ret -184
JMP 59
SUB start 1
JMP -11
POP start
DUP size            // sort: while the heap has 2 or more values
MOV t size
SUB t 2
? t
JMP 2
JMP 36
DUP low
CMP low nil
? low
JMP 3
MOV high low
JMP -5
DUP high
MOV carry high
DUP size
MOV k size
SUB k 1
DUP k
SUB k 1
? k
JMP 2
JMP 4
MOV low high
SUB k 1
JMP -7
POP k
MOV x high
MOV high carry
DUP low
CMP low nil
? low
JMP 3
MOV high low
JMP -5
POP high
MOV high x
SUB size 1
PUSH i 1
PUSH ret -141
JMP 16
JMP -40
POP size            // rewind the array
DUP low
CMP low nil
? low
JMP 3
MOV high low
JMP -5
DUP high            // move the array into result
CMP high nil
? high
JMP 3
MOV result high
JMP -5
JMP 128             // end of the program
DUP low             // SIFT(i): sink a[i] down the heap
CMP low nil
? low
JMP 3
MOV high low
JMP -5
DUP i
MOV k i
SUB k 1
DUP k
SUB k 1
? k
JMP 2
JMP 4
MOV low high
SUB k 1
JMP -7
POP k
DUP high
MOV v high
DUP i               // c = 2i, the left child
MOV c i
MUL c 2
DUP c
MOV t c
DUP size
MOV t size
SUB t
? t
JMP 2
JMP 93
DUP c
MOV t c
DUP size
MOV t size
SUB t
SUB t 1
? t
JMP 2
JMP 31
DUP low
CMP low nil
? low
JMP 3
MOV high low
JMP -5
DUP c
MOV k c
SUB k 1
DUP k
SUB k 1
? k
JMP 2
JMP 4
MOV low high
SUB k 1
JMP -7
POP k
DUP high
MOV t high
MOV low high
DUP high
MOV t high
MOV high low
CMP t
SUB t 1
? t
JMP 2
JMP 2
ADD c 1
DUP low             // is a[c] bigger than v?
CMP low nil
? low
JMP 3
MOV high low
JMP -5
DUP c
MOV k c
SUB k 1
DUP k
SUB k 1
? k
JMP 2
JMP 4
MOV low high
SUB k 1
JMP -7
POP k
DUP v
MOV t v
DUP high
MOV t high
CMP t
SUB t 1
? t
JMP 2
JMP 27
MOV carry high
DUP v
MOV high v
DUP low
CMP low nil
? low
JMP 3
MOV high low
JMP -5
DUP i
MOV k i
SUB k 1
DUP k
SUB k 1
? k
JMP 2
JMP 4
MOV low high
SUB k 1
JMP -7
POP k
POP high
MOV high carry
POP i
MOV i c
JMP -102
POP c               // done sinking: clean up
POP v
POP i
JMP ret             // return to the caller
```

Did you sculpt something cool? Share it with me on [GitHub](https://github.com/Darkoyd/SCuLPTER) and it might end up on this page!
