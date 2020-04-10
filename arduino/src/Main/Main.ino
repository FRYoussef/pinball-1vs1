#define EI_ARDUINO_INTERRUPTED_PIN
#include <EnableInterrupt.h> //Thats for allow Int in all pins

const int button1Pin = 4;
const int button2Pin = 5;


void buttonISR() {
    switch (arduinoInterruptedPin) {
        case button1Pin:
            Serial.println("Button 1 is pressed");
            break;
        case button2Pin:
            Serial.println("Button 2 is pressed");
            break;
   }

   // for button bounce
   delay(100);
}


void setup() {
    Serial.begin(9600);
    pinMode(button1Pin, INPUT);
    pinMode(button2Pin, INPUT);

    enableInterrupt(button1Pin, buttonISR, CHANGE);
    enableInterrupt(button2Pin, buttonISR, CHANGE);
}


void loop() {
    delay(10000);
}
