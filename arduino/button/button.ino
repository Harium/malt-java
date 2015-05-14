/*
 * Blink example with serial
 */
 
// Connect the button to pin 2 in arduino
const int buttonPin = 2;
// Pin 13 has an LED connected on most Arduino boards.
const int ledPin = 13;
//The button state (0 = released, HIGH = pressed)
const int buttonState = 0;

// the setup routine runs once when you press reset:
void setup() {
  // initialize the digital pin as an output.
  pinMode(ledPin, OUTPUT);
  // initialize the button pin as an input.
  // connect the button in VDD and PIN 2
  pinMode(buttonPin, INPUT);
  Serial.begin(9600);
}

// the loop routine runs over and over again forever:
void loop() {
  digitalWrite(led, HIGH);   // turn the LED on (HIGH is the voltage level)
  Serial.println("LED ON");  // send a message via serial interface
  delay(1000);               // wait for a second
  digitalWrite(led, LOW);    // turn the LED off by making the voltage LOW
  Serial.println("LED OFF"); // send a message via serial interface
  delay(1000);               // wait for a second
}
