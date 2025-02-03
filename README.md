# Mastermind Codebreaker AI

## Overview

This is a terminal-based **Mastermind** game where you play as the **Codemaker**, and an AI, using **Knuth's algorithm**, tries to guess your secret code in **5 moves or less**. You provide feedback after each guess, and the AI refines its approach.

## How to Play

1. **Compile the program:**
   ```sh
   javac Main.java
   ```
2. **Run the program:**
   ```sh
   java Main
   ```
4. The AI will make a **guess**.
5. Enter feedback:
    - **Black pegs** (correct position & color).
    - **White pegs** (correct color, wrong position).
6. The AI keeps guessing until it finds your code or reaches the limit.

