#define EI_ARDUINO_INTERRUPTED_PIN
#include <EnableInterrupt.h> //Allow interruptions in all pins
#include <Wire.h>
#include <Thread.h>

const int leftButtonPin = 4;
const int rightButtonPin = 5;
const int triggerPin = 6;
const int echoPin = 2;
const int leftSolenoidPin = 9;
const int rightSolenoidPin = 10;

//Handle events
const int START = 0;
const int ACK_GOAL = 1;
const int END = 2;
const int ACK_START = 3;
const int SCORE_UP = 4;
const int AAK_END = 5;

const char ERROR = -9;
// here is why 58 -> https://www.instructables.com/id/Non-blocking-Ultrasonic-Sensor-for-Arduino/
const char DISTANCE_CONVERSOR = 58;

volatile int time = 0;
volatile boolean leftButton = false;
volatile boolean rightButton = false;


Thread sensorThread = Thread();
int started = true; //set to false when raspberry works

long startPulseTime = 0;

void setup() {
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

    // configure threads
    sensorThread.onRun(sensorFunction);
    sensorThread.setInterval(500);

    //configure I2C
    Wire.begin(0x8);
    Wire.onReceive(receiveEvent);
    //Wire.onRequest(sendData);
}


void sensorFunction(){

    sendPulse();
    int distance = (time / DISTANCE_CONVERSOR) + ERROR;
    Serial.print("Distance: ");
    Serial.print(distance);
    Serial.println("cm");
    
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
      case ACK_GOAL:
        break;
      case END:
        break;
    }
  }
}

/*void sendData(){
  Wire.write(number);
}*/

void loop() {

    while(!started) {} //Wait till START message
    
    Wire.write(ACK_START);

    if(sensorThread.shouldRun())
      sensorThread.run();
   

    
    
    delay(1000);
}
