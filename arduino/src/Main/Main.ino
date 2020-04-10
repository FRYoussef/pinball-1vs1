#define EI_ARDUINO_INTERRUPTED_PIN
#include <EnableInterrupt.h> //Thats for allow Int in all pins

const int leftButtonPin = 4;
const int RightButtonPin = 5;


void buttonISR() {
    switch (arduinoInterruptedPin) {
        case leftButtonPin:
            Serial.println("Button 1 is pressed");
            break;
        case RightButtonPin:
            Serial.println("Button 2 is pressed");
            break;
   }

   // for button bounce
   delay(100);
}


void setup() {
    Serial.begin(9600);
    pinMode(leftButtonPin, INPUT);
    pinMode(RightButtonPin, INPUT);

    enableInterrupt(leftButtonPin, buttonISR, CHANGE);
    enableInterrupt(RightButtonPin, buttonISR, CHANGE);
}


void loop() {
    delay(10000);
}
