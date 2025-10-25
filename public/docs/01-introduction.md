# Introduction

Welcome to **SCuLPT**, the Simple Cubic Language for Programming Tasks!

I'm [Darkoyd](https://github.com/Darkoyd), the creator of SCuLPT, and I'm thrilled you're here. I wrote this guide to help you, the dear reader, understand and master SCuLPT in all its forms.

SCuLPT is more than just another programming language. It's a unique fusion of **computational thinking** and **hands-on creativity**. Whether you prefer typing code on a screen or physically arranging blocks on a table, SCuLPT meets you where you are. The language is designed around three core principles: **simplicity** (so anyone can learn it), **accessibility** (so everyone can use it), and **expressiveness** (so you can actually do something meaningful with it).

This documentation will guide you through every aspect of the language, from the philosophy behind its design to practical tutorials that get you coding right away.

## What is SCuLPT?

At its core, SCuLPT is a **programming language**, but it's not like the ones you might be used to. Instead of being confined to a text editor and terminal, SCuLPT exists as both a **physical system** and a **digital environment**. Think of it as an "artistic sandbox" where computation meets creativity.

SCuLPT offers several distinct interfaces:

- **A physical interface** made up of 3D-printable components that you can hold, manipulate, and arrange. You literally sculpt your program.
- **A digital interface** called SCuLPTER, a browser-based development environment where you write code, visualize how it's parsed, and step through execution.
- **This documentation** serves as both a formal specification of the language and a learning resource to help you understand how to use it.

Whether you engage with SCuLPT through physical components, digital code, or both, you're working with the same language and the same underlying principles. It's one language, two completely different ways of experiencing it.

### Key Features

When I set out to create SCuLPT, I imposed strict constraints on myself. I wanted to design a language from scratch with minimal complexity, yet maximum expressiveness. This wasn't just for fun, it was a deliberate design challenge to prove that a truly simple language could still be powerful, not necessarily computationally powerful. The result is SCuLPT, which achieves this through these core features:

**One type to rule them all.** SCuLPT uses only one fundamental data type and one core data structure. No need to worry about integers vs. floats vs. strings. No complex type systems to learn. Everything in SCuLPT is a **number**, and all values live in **stacks**. This radical simplification removes a huge source of confusion for beginners while still allowing you to express complex ideas.

**Physical first, digital by choice.** The language was designed with physical components as the primary interface. The digital environment (SCuLPTER) came later as a more accessible alternative. This "physical-first" mindset shapes how the language works, it's designed to be tangible and concrete, which is why it translates so well to both interfaces.

**Just enough instructions.** SCuLPT has exactly **13 core instructions**. That's it. No bloated standard library, no endless functions to memorize. Just 13 operations that are composable and expressive. You can learn the entire language in an hour.

**Accessible to everyone, customizable for anyone.** This isn't just a language for experienced programmers. Whether you're a complete beginner, a student learning computational thinking, an artist exploring creative coding, or a maker building physical systems, SCuLPT is designed to be accessible to you. At the same time, the modular design means you can extend and customize SCuLPT to fit your specific needs.

### Philosophy

Beyond the technical design, SCuLPT is built on a set of broader ideals that I hold dear as both an artist, an engineer and an educator. These principles guide every decision made in the language's development:

**1. Simplicity: Easy to learn, easy to read, easy to teach, easy to play.**
Programming should not be a barrier to entry. SCuLPT is designed so that someone with zero programming experience can pick it up and start creating within minutes. Once you understand the basics, your code should be readable, not just to you, but to others. And when you teach SCuLPT to students, you're not fighting against complexity; you're building on a solid foundation of clarity. Most importantly, programming should be fun. It should feel like play.

**2. Low-cost: 3D printable components and open-source software.**
Cost should never be a barrier to learning or creating. All of SCuLPT's components are 3D-printable, which means anyone with access to a 3D printer (or a library, school, or maker space that has one) can build it. All of the software is open-source, meaning it's free to use, modify, and share. I believe that technology and education should be accessible to everyone, regardless of their situation.

**3. Extensibility: Modular design for easy expansion and customization.**
SCuLPT is designed to grow with you. The modular architecture means you can extend the language, create custom components, or modify the existing ones to fit your specific needs. Whether you want to add new operations, create specialized hardware, or build variations for different applications, the language's structure supports that growth without requiring you to rebuild everything from scratch.

**4. Community-driven: Open to contributions and feedback from users.**
The best ideas often come from the community using the tool, not just from the creator. SCuLPT welcomes contributions, feedback, and ideas from anyone. Your questions, suggestions, and creative uses of the language help shape its evolution. This is a collaborative project, and everyone's voice matters.

**5. Artistic expression: Encourages creativity and exploration through programming.**
Programming shouldn't be just about solving problems or building applications. It can be a form of creative expression, similar to music, visual art, or writing. SCuLPT is designed to inspire creativity and exploration. Whether you're creating beautiful visualizations of algorithms, building interactive art installations, or exploring mathematical patterns, SCuLPT gives you the tools to express your ideas in code.

## Who is SCuLPT for?

SCuLPT is designed with a wide range of people in mind. Here's who might find it particularly valuable:

**Beginners learning programming concepts**
If you've never written code before, SCuLPT is an ideal first window into code. With only 13 instructions and a single data type, you can grasp the fundamentals without getting overwhelmed. The step-by-step execution model means you can see exactly what's happening at each point in your program.

**Educators teaching coding and computational thinking**
Teachers at any level, from primary school to university, can use SCuLPT to teach the core concepts of programming. The simplicity of the language lets you focus on teaching algorithmic thinking, logic, and problem-solving rather than syntax and language quirks. Both the physical and digital interfaces give you flexible options for classroom activities and projects.

**Artists and designers exploring computational art**
SCuLPT treats code as a creative medium. If you're interested in computational art, algorithmic design, or exploring the intersection of code and creativity, SCuLPT provides an expressive canvas. The language's minimalism actually enhances creativity, with fewer rules and constraints, there's more room for experimentation and artistic expression.

**Hobbyists and makers interested in physical computing**
If you like building things and want to add a computational layer, SCuLPT's 3D-printable components are perfect. Create physical installations, interactive art pieces, or just explore how computation can manifest in the physical world. The hands-on nature of the physical interface makes it satisfying in a way that pure software often isn't.

**Anyone curious about programming and creativity**
Perhaps you don't fit neatly into any of the above categories. You might just be curious about how programming works, or you want to explore the creative possibilities of code. That's exactly who SCuLPT is for too. There's no gatekeeping here, if you're interested, you're welcome.

## Getting Help

Learning a new language can sometimes feel isolating, but you're not alone. Here are several ways to get help and connect with others:

**Explore the Documentation**
This documentation is your primary resource. It's organized sequentially, so you can follow along from introduction to advanced topics. Each section includes examples and explanations. If something isn't clear, don't hesitate to move on and come back to it, sometimes context from later sections helps things click into place.

**Check Out the Examples**
Sometimes the best way to learn is by example. Head over to the [Examples](/SCuLPTER/examples) section for working code samples that demonstrate different programming concepts. You can load these directly into SCuLPTER and experiment with them. Try modifying examples to see what happens. This kind of experimentation is how programming mastery grows.

**Connect with the Community**
Questions? Ideas? Found a bug? Want to share your creations? The SCuLPT community is here for you:

- **Discord**: Message me on [Discord](https://discordapp.com/users/152176968454897665). It's a great place for real-time conversation and to connect with other SCuLPT enthusiasts.
- **GitHub**: Open an issue on the [GitHub](https://github.com/Darkoyd/SCuLPTER) repository if you've found a bug, have a feature request, or want to contribute to the project.
- **Email**: Reach out directly by [E-Mail](mailto:n.londonoc@uniandes.edu.co) if you prefer asynchronous communication.

Remember, there are no stupid questions. Every question you have has probably been asked before, and every suggestion you have might lead to making SCuLPT better for everyone.

---

## Ready to Start?

If you're excited to dive in, head over to the [Getting Started](/SCuLPTER/documentation/getting-started) section. There you'll find everything you need to set up SCuLPT—whether you're going physical, digital, or both.

If you'd like to learn more about how to actually write SCuLPT code and create your first program, the [Start Sculpting](/SCuLPTER/documentation/start-sculpting) section will walk you through it step by step.

Welcome to SCuLPT community. I'm glad you're here!
