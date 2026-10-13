Compile:
  javac *.java

to Run interactively:
  java UserInterface

to Run the Stage 1 test sequence from a file:
  java UserInterface < testInput.txt

testInput.txt creates clients C1-C5 and products P1-P5, then adds to and prints
the wishlists in the order given in the assignment. At the end it also tries some
invalid input (unknown client C9, unknown product P9, quantity 0), and each of
these should be rejected.
