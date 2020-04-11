#define EI_ARDUINO_INTERRUPTED_PIN
#include <EnableInterrupt.h> //Allow interruptions in all pins


const int leftButtonPin = 4;
const int RightButtonPin = 5;
const int triggerPin = 6;
const int echoPin = 2;

const char ERROR = -9;
// here is why 58 -> https://www.instructables.com/id/Non-blocking-Ultrasonic-Sensor-for-Arduino/
const char DISTANCE_CONVERSOR = 58;

volatile int time = 0;
volatile boolean leftButton = false;
volatile boolean rightButton = false;

long startPulseTime = 0;


void buttonISR() {
    switch (arduinoInterruptedPin) {
        case leftButtonPin:
            leftButton = !leftButton;
            break;
        case RightButtonPin:
            rightButton = !rightButton;
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


void setup() {
    // configure pin modes 
    pinMode(leftButtonPin, INPUT);
    pinMode(RightButtonPin, INPUT);
    pinMode(echoPin, INPUT_PULLUP);
    pinMode(triggerPin, OUTPUT);

    Serial.begin(9600);

    // link ISR functions
    enableInterrupt(leftButtonPin, buttonISR, CHANGE);
    enableInterrupt(RightButtonPin, buttonISR, CHANGE);
    enableInterrupt(echoPin, echoISR, CHANGE);
}


void loop() {
    sendPulse();
    int distance = (time / DISTANCE_CONVERSOR) + ERROR;

    Serial.print("Distance: ");
    Serial.print(distance);
    Serial.println("cm");
    
    delay(1000);
}
