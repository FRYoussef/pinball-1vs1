#define EI_ARDUINO_INTERRUPTED_PIN
#include <EnableInterrupt.h> //Allow interruptions in all pins
#include <TimerOne.h> // simplify the way to interact with timer 1


const int leftButtonPin = 4;
const int RightButtonPin = 5;

// ultrasonic sensor pins
const int triggerPin = 3;
const int echoPin = 2;

// here is why 58 -> https://www.instructables.com/id/Non-blocking-Ultrasonic-Sensor-for-Arduino/
const int DISTANCE_CONVERSOR = 58;
// measurement in cm
const int DISTANCE_RANGE = 25;
// always must be above 20ms
const int PULSE_FREQUENCY = 50000;

volatile long echoTimeStart = 0;
volatile long distance = 0;


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


void echoWaveISR(){
    // echoTimeStart == 0 means, no pulse has sent
    if(echoTimeStart != 0){
      int duration = pulseIn(echoPin, HIGH);
        distance = (micros() - echoTimeStart) / DISTANCE_CONVERSOR;
        echoTimeStart = 0;

        // an object is between the sensor and the wall
        if(distance < DISTANCE_RANGE){
            Serial.print("Distance: ");
            Serial.println(distance);
        }        
    }
}


void pulseWaveISR(){
    // send pulses during 10 micro seconds 
    digitalWrite(triggerPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(triggerPin, LOW);
    echoTimeStart = micros();
}


void setup() {
    // configure pin modes 
    pinMode(leftButtonPin, INPUT);
    pinMode(RightButtonPin, INPUT);
    pinMode(echoPin, INPUT_PULLUP);
    pinMode(triggerPin, OUTPUT);

    Serial.begin(9600);
    while(!Serial) continue;
    //Timer1.initialize(PULSE_FREQUENCY);

    // link ISR functions
    enableInterrupt(leftButtonPin, buttonISR, CHANGE);
    enableInterrupt(RightButtonPin, buttonISR, CHANGE);
    enableInterrupt(echoPin, echoWaveISR, RISING);
    //Timer1.attachInterrupt(pulseWaveISR);
}


void loop() {
    digitalWrite(triggerPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(triggerPin, LOW);
    echoTimeStart = micros();

    /*int duration = pulseIn(echoPin, HIGH);
    distance = duration / DISTANCE_CONVERSOR;
    Serial.print("Distance: ");
    Serial.println(distance);*/
    
  delay(300);
}
