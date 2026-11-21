# Tutorials

Time to sculpt for real! These six tutorials are the same challenges we gave to people of all ages and backgrounds during the SCuLPT workshops, from kids who had never programmed to software engineers with years of experience. They go from easy to tricky, and each one adds a new idea.
Every program here runs in [SCuLPTER](/SCuLPTER/sculpter). My advice: before reading the solution, try to build it yourself, with blocks or with text. Then come back and compare. There is never only one right answer in SCuLPT!

We write stacks with the top on the **right**: `[1, 2, 3]` means 3 is on top.

## 1. Arithmetic

**The challenge**: add two numbers, then multiply the result by a third one.

```
PUSH circle 4
PUSH circle 7
ADD circle          // circle: [11]
MUL circle 3        // circle: [33]
```

The one parameter Addition takes the two values on top of circle. The two parameter Multiplication takes only the top one and multiplies it by 3.

Now let's subtract. We want 10 - 3:

```
PUSH square 3
PUSH square 10
SUB square          // square: [7]
```

Notice that we pushed the 3 **first**. The top of the stack is always the first number of the operation, so 10 has to be on top.

**Try it yourself**: compute (2 + 3) * (4 + 5). Hint: use two stacks, then Move one result next to the other.

## 2. Summing to infinity

**The challenge**: add 1 + 2 + 3 + 4 + ... forever.

We need two stacks: `counter` holds the number we are adding, and `total` holds the sum so far.

```
PUSH counter 0
PUSH total 0
ADD counter 1       // loop starts here
DUP counter
MOV total counter
ADD total
JMP -4              // back to ADD counter 1
```

Each time around the loop, counter goes up by one. We Duplicate it (so we don't lose it), Move the copy to total, and add it to what was already there. Then Jump takes us back to the start of the loop.

| Block | counter | total |
|-------|---------|-------|
| PUSH counter 0 | [0] | [] |
| PUSH total 0 | [0] | [0] |
| ADD counter 1 | [1] | [0] |
| DUP counter | [1, 1] | [0] |
| MOV total counter | [1] | [0, 1] |
| ADD total | [1] | [1] |
| JMP -4 | [1] | [1] |
| ADD counter 1 | [2] | [1] |
| ... | ... | ... |

This program never ends. In SCuLPTER, just stop stepping whenever you like. With blocks, this is a perfect candidate for a physical loop: replace the Jump with a T connector!

**Try it yourself**: make it stop when counter reaches 10. The solution is in [Control Flow](/SCuLPTER/documentation/control-flow), under Leaving a loop.

## 3. Map: squaring numbers

**The challenge**: given a stack of numbers, build a new stack with the square of each one.

This is our first loop that ends because it runs out of things to do. We will use the nil test from [Control Flow](/SCuLPTER/documentation/control-flow) to check if `input` is empty.

```
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

The first three blocks prepare the input. The loop does this:

- Test if input is empty. If it is, jump past the last block and end.
- Otherwise, Duplicate the top number and Multiply it by itself.
- Move the result to output, and go back to the start.

At the end, `input` is `[]` and `output` is `[4, 9, 16]`. The 2 was on top of input, so it was squared first and ended at the bottom of output. Stacks flip things around!

**Try it yourself**: change the program so it doubles every number instead (only one block needs to change). Then make it add 10 to every number.

## 4. Filter: even and odd

**The challenge**: split a stack of numbers into an `even` stack and an `odd` stack.

Now the loop has a decision **inside** it. A number is even when the remainder of dividing it by 2 is 0.

```
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

The trick is in the middle. Modulo gives 0 for even numbers and 1 for odd ones. Negation turns that into 0 or -1, which is exactly what Question understands: 0 runs the next block, -1 skips it.

- **Even**: Question runs `JMP 3`, which lands on `MOV even input`, and then we loop.
- **Odd**: Question skips `JMP 3`, so `MOV odd input` runs, and then we loop.

At the end, `even` is `[2, 8]` and `odd` is `[7, 5]`.

Notice the two backward Jumps have different numbers (-10 and -12), even though they go to the same place. They start from different blocks!

**A note on negative numbers**: the remainder of -3 divided by 2 is -1, and negating that gives 1, which counts as yes. So with this program, odd negative numbers end up in the even stack. Can you fix it? Hint: multiply the remainder by itself before negating it, and remember to update the Jumps.

## 5. Fibonacci

**The challenge**: generate the Fibonacci numbers: 0, 1, 1, 2, 3, 5, 8, 13... Each number is the sum of the two before it.

This is the program of the Fibonacci sculpture you saw in [Start Sculpting](/SCuLPTER/documentation/start-sculpting). The circle stack collects all the numbers, triangle and hexagon hold the last two, and trapezoid is the place where we add them.

```
PUSH hexagon 0
PUSH circle 0
PUSH circle 1
PUSH triangle 1
DUP triangle        // loop starts here
DUP hexagon
MOV trapezoid hexagon
MOV trapezoid triangle
ADD trapezoid
POP hexagon
MOV hexagon triangle
DUP trapezoid
MOV circle trapezoid
MOV triangle trapezoid
JMP -10             // back to DUP triangle
```

Let's follow the first time around the loop:

| # | Block | hexagon | triangle | trapezoid | circle |
|---|-------|---------|----------|-----------|--------|
| 1 | PUSH hexagon 0 | [0] | [] | [] | [] |
| 2 | PUSH circle 0 | [0] | [] | [] | [0] |
| 3 | PUSH circle 1 | [0] | [] | [] | [0, 1] |
| 4 | PUSH triangle 1 | [0] | [1] | [] | [0, 1] |
| 5 | DUP triangle | [0] | [1, 1] | [] | [0, 1] |
| 6 | DUP hexagon | [0, 0] | [1, 1] | [] | [0, 1] |
| 7 | MOV trapezoid hexagon | [0] | [1, 1] | [0] | [0, 1] |
| 8 | MOV trapezoid triangle | [0] | [1] | [0, 1] | [0, 1] |
| 9 | ADD trapezoid | [0] | [1] | [1] | [0, 1] |
| 10 | POP hexagon | [] | [1] | [1] | [0, 1] |
| 11 | MOV hexagon triangle | [1] | [] | [1] | [0, 1] |
| 12 | DUP trapezoid | [1] | [] | [1, 1] | [0, 1] |
| 13 | MOV circle trapezoid | [1] | [] | [1] | [0, 1, 1] |
| 14 | MOV triangle trapezoid | [1] | [1] | [] | [0, 1, 1] |
| 15 | JMP -10 | [1] | [1] | [] | [0, 1, 1] |

After the second time around, circle is `[0, 1, 1, 2]`, then `[0, 1, 1, 2, 3]`, and so on forever. Step through it in SCuLPTER and watch the sequence grow!

In the sculpture there is no Jump: block 14 connects back into block 5 through a T connector. Same program, different shape.

**Try it yourself**: make the program stop once it has generated 10 numbers. You will need a counter stack and a Question.

## 6. Sorting

**The challenge**: sort a stack of numbers, smallest at the bottom, biggest on top.

This is the final boss. It uses everything: two nested loops, the nil test, Compare, Question and a lot of Moves. Take your time!

The idea is the same one you use to sort a hand of cards. Take numbers from `input` one at a time, and insert each one at its right place in `sorted`. To find the place, we move the bigger numbers from `sorted` back to `input` (they will come back later), until the top of `sorted` is not bigger than the number we are holding.

```
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
CMP pair            // hold vs top of sorted
? pair
JMP 3               // hold >= top: place hold
MOV input sorted    // top is bigger: give it back to input
JMP -12             // back to the inner loop
MOV sorted hold     // place hold
JMP -19             // back to the outer loop
```

Here is what each part does:

- **Outer loop**: if `input` is empty, we are done. Otherwise, Move its top number into `hold`.
- **Inner loop**: if `sorted` is empty, there is nothing to compare with, so we place `hold` right away.
- **Comparing**: we copy the top of `sorted` and the number in `hold` into the `pair` stack, and Compare them. Compare eats both copies, so `sorted` and `hold` are untouched.
- If `hold` is bigger or equal, we place it on top of `sorted` and go back to the outer loop.
- If `hold` is smaller, the top of `sorted` goes back to `input`, and we try again with the next one.

At the end, `sorted` is `[-3, 1, 2, 4, 4, 5]`. It works with negative numbers and with repeated numbers too.

**Try it yourself**: change the program so it sorts the other way around, with the biggest number at the bottom.

## Where to go from here

If you solved all six, congratulations: you have written loops, conditions, maps, filters and a sorting algorithm with only 13 operations. That's real programming!

Now it's your turn to create. Some ideas:

- Compute the factorial of a number (5! = 5 * 4 * 3 * 2 * 1).
- Reverse a stack.
- Find the biggest number in a stack.
- Build a sculpture that is beautiful **and** computes something. Share it with me, I'd love to see it!

And if you are curious about **why** 13 operations are enough to compute anything, read [Under the Hood](/SCuLPTER/documentation/under-the-hood).
