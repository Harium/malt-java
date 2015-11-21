/*
  Based on Serial Event example
  by Tom Igoe
 
 This example code is in the public domain.
 http://www.arduino.cc/en/Tutorial/SerialEvent
 
*/

String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

void setup() {
  // initialize serial:
  Serial.begin(9600);
  // reserve 200 bytes for the inputString:
  inputString.reserve(256);
}

void loop() {
  readSerial();
  
  if (stringComplete) {
    //Expects 1 on;
    //2 off;
    
    String pinText = getValue(inputString, ' ', 0);
    String actionText = getValue(inputString, ' ', 1);
    Serial.println(pinText);
    Serial.println(actionText);
    
    int pin = pinText.toInt();
    
    if (actionText.equals("on;")) {
      digitalWrite(pin, HIGH);
    }
    if (actionText.equals("off;")) {
      digitalWrite(pin, LOW);
    }
    
    // clear the string:
    inputString = "";
    stringComplete = false;
  }

  delay(100);
}

/*
  SerialEvent occurs whenever a new data comes in the
 hardware serial RX.  This routine is run between each
 time loop() runs, so using delay inside loop can delay
 response.  Multiple bytes of data may be available.
 */
void readSerial() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == ';') {
      stringComplete = true;
    }
  }
}

String getValue(String data, char separator, int index) {
  int found = 0;
  int strIndex[] = {0, -1};
  int maxIndex = data.length() - 1;
  for (int i=0; i<=maxIndex && found<=index; i++) {
    if (data.charAt(i)==separator || i==maxIndex) {
      found++;
      strIndex[0] = strIndex[1]+1;
      strIndex[1] = (i == maxIndex) ? i+1 : i;
    }
  }
  return found>index ? data.substring(strIndex[0], strIndex[1]) : "";
}
