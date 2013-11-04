KeyPadApp
=========

A desktop app programmed in Java using Swing UI lib to mimic a access control keypad

PROBLEM :

Design and implement a program to simulate a keypad door security system for a restricted area within a factory.

Managers and authorised employees can open the door using a four-digit employee code.

Factory managers can change the employee code using a special four-digit manager code.


Details

The door is opened by entering the employee code and then pressing the Open button.  
The door remains opened until the Close button is pressed.  
If an invalid employee code is entered, a warning message is displayed.  
If the manager code is entered, the manager is prompted to key in the new employee code.



If the employee code is entered and the Open button pressed, the keys on the keypad, except for the Close button, are disabled.

If the manager code is entered, the manager is prompted to enter the new employee code. When the new code has been entered, the manager presses the Enter button to implement the change.

The Cancel/Close Button

If the Close button is pressed, the door will be closed and the keys on the keypad will be active again. 

The Cancel button can be used to cancel the keying in of a code.

Animation 

The graphic display can be your own choice (either drawn or imported).  The status messages should include the following:	

Door open
	Door closed
	Please enter a code
	Invalid code!
	Enter new code
	Code changed

Any other messages or graphics are at your own discretion.

Program Objectives

Display a JLabel icon to represent the door status
Store the employee and manager codes as String variables.
Associate ActionListeners with each of the keypad controls.

Note:  You can use “coded Swing” or the GUI environment which will generate most of the code for you.
