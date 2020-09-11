// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// -------- Pseudo Code --------

// LOOP:
// i = 0
// size = 8192

// RENDER:
// if (i >= size) goto LOOP
// if (RAM[KBD] == 0) goto BLACK

// WHITE:
// RAM[SCREEN + i] = 0
// goto NEXT

// BLACK
// RAM[SCREEN + i] = -1
// goto NEXT

// NEXT:
// i++
// goto RENDER

// --------- Real Code ---------

   
(LOOP)
   @i
   M=0 		// i = 0
   @8192
   D=A 		// D = 8192
   @size
   M=D 		// size = 8192

(RENDER)
   @i
   D=M 		// D = i
   @size
   D=D-M 	// D = i - size
   @LOOP
   D;JGE 	// if (i >= size) goto LOOP

   @KBD
   D=M 		// D = RAM[KBD]
   @BLACK
   D;JNE 	// if (RAM[KBD] != 0) goto BLACK

(WHITE)
   @i
   D=M 		// D = i
   @SCREEN
   A=A+D 	// A = SCREEN + i
   M = 0 	// RAM[SCREEN + i] = 0
   @NEXT
   0;JMP

(BLACK)
   @i
   D=M 		// D = i
   @SCREEN
   A=A+D 	// A = SCREEN + i
   M = -1 	// RAM[SCREEN + i] = -1
   @NEXT
   0;JMP

(NEXT)
   @i
   M=M+1
   @RENDER
   0;JMP
