#define EI_ARDUINO_INTERRUPTED_PIN
#include <EnableInterrupt.h> //Allow interruptions in all pins
#include <Wire.h>
 
#define SLAVE_ADDRESS 0x08
 
boolean goal = false;
 
const int leftButtonPin = 4;
const int rightButtonPin = 5;
const int triggerPin = 6;
const int echoPin = 2;
const int leftSolenoidPin = 9;
const int rightSolenoidPin = 10;
const int startLedPin = 13;

//Handle events
const int START = 0;
const int ERROR_I2C = 1;
const int END = 2;
const int SCORE_UP = 3;

const char ERROR = -9;
// here is why 58 -> https://www.instructables.com/id/Non-blocking-Ultrasonic-Sensor-for-Arduino/
const char DISTANCE_CONVERSOR = 58;

volatile int time = 0;
volatile boolean leftButton = false;
volatile boolean rightButton = false;
volatile int started = false;
long startPulseTime = 0;

void setup(){
  // configure pin modes 
  pinMode(leftButtonPin, INPUT);
  pinMode(rightButtonPin, INPUT);
  pinMode(echoPin, INPUT_PULLUP);
  pinMode(triggerPin, OUTPUT);
  pinMode(leftSolenoidPin, OUTPUT);
  pinMode(rightSolenoidPin, OUTPUT);
  
  
    
  Serial.begin(9600);
  
  // link ISR functions
  enableInterrupt(leftButtonPin, buttonISR, CHANGE);
  enableInterrupt(rightButtonPin, buttonISR, CHANGE);
  enableInterrupt(echoPin, echoISR, CHANGE);
    
  Wire.begin(SLAVE_ADDRESS);
  Wire.onReceive(receiveEvent);
  Wire.onRequest(requestEvent);
  Serial.println("I2C Ready!");
}
 

void buttonISR() {
    switch (arduinoInterruptedPin) {
        case leftButtonPin:
            leftButton = !leftButton;
            // activate solenoid
            digitalWrite(leftSolenoidPin, leftButton);
            break;
        case rightButtonPin:
            rightButton = !rightButton;
            // activate solenoid
            digitalWrite(rightSolenoidPin, rightButton);
            break;
   }
   //button bounce
   delayMicroseconds(50);
}

void echoISR(){
    time = micros() - startPulseTime;
}


void sendPulse(){
    startPulseTime = micros();
    digitalWrite(triggerPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(triggerPin, LOW);
}


void receiveEvent(int howMany) {
  while (Wire.available()) { // loop through all but the last
    int ev = Wire.read(); // receive byte as a character
    switch(ev) {
      case START:
        started = true;
        break;
      case END:
        started = false;
        break;
    }
  }
}
 
// Use the offset value to select a function
void requestEvent(){
    if(goal){
      Wire.write(SCORE_UP);
      Serial.println("GOAL SEND");
      goal = false;
    }
    
  
}

 
void loop(){
   if(started) {

    sendPulse();
    int distance = (time / DISTANCE_CONVERSOR) + ERROR;
    Serial.print("Distance: ");
    Serial.print(distance);
    Serial.println("cm");
  
    if (distance < 10 && distance > 0) { //depends on the goal size
        goal=true;
        Serial.println("Value:" + goal);
    }
  
    delay(1000);
  }
}