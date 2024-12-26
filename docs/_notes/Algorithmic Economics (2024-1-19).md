---
title: Algorithmic Economics (2024-1-19)
feed: hide
date: 2024-1-19
---

### Announcements:

1. Fedevico's office hours: Fridays 10-12:30 (509 Evans)
2. PS1 out on Monday
3. Scribe signup available

### Preliminary Notions

Let X be a set, and define a *binary relation* on X as a subset B of $$X \times X$$

*Ex*: $$X = \{\text{Mary, John, Carlos}\}$$. Suppose Mary is older than Carlos, who is older than John. The binary relation "is older than" is {(Mary, Carlos), (Carlos, John), (Mary, John)}

**Notation**: Given a binary relation $$B$$, we shall write $$x B y$$ instead of $$(x, y) \in B$$  

*Ex*: $$\supset$$ is a binary relation of a set $$X$$. We write $$X \supset y$$ instead of $$(x, y) \in \supseteq$$

Let $$\geq$$ be a binary relation of a set $$X$$.

**Def**: $$\geq$$ is complete s.t. $$\forall x, y \in X, \quad x \geq y\text{or} y \geq x$$  (or both)

**Def** $$\geq$$ is **transitive** if $$\forall x, y, z \in X, if x \geq y,$$ and $$y \geq z \implies x \geq z$$. <br>Antisymmetric if $$\forall x, y \in X,x \geq y, y \geq x \implies x = y$$

**Def**: The indifference part of the binary relation is defined by <br> $$\sim = \{(x, y) : (x, y) \in \geq\ \text{and} (y, x) \in \geq\}$$

A binary order is called a <u> weak order </u> if it is a complete order and a transitive order.

In this class, we call weak orders preference relations.

Interpretations: If $$\geq$$ is the preference relation of an agent  then $$X \geq y$$ means that the agent is happy choosing $$x$$ out of the set $$ \{x, y\}$$. And $$x > y$$ means that the agent would exclusively choose $$x$$ for $$\{x, y\}$$. 

**Def**: A preference relation that is also antisymmetric is called a *strict preference*.

Actually antisymmetry and completeness implies exclusivity (no two elements have the same place in the order)

Given a function $$u: X \to R$$, we may define a binary relation $$\geq$$ by $$x \geq y$$ if and only if $$u(x) \geq u(y)$$. 

**Exercise:** Prove that $$\geq$$ defined in this way is a preference relation.

In this case, we say that u *represents* $$\geq$$, and that u is a *utility representation* of $$\geq$$.

And, $$x \sim y$$ iff $$u(x) = u(y)$$.

#### **First Economic Problem**

**Object Allocation**

Model primitives: 

A finite (nonempty) set $$A$$ of *agents*.

A finite (nonempty) set $$O$$ of *objects*.

A symbol $$\empty$$ representning the *outside option*.

Each student $$i \in A$$ is endowed with a strict prefefrence $$\succeq$$ over $$O \cup\{\empty\}$$. 

Ineffieient if slack in system: Stable matching

