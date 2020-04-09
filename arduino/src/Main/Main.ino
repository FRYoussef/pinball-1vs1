const int button1Pin = 2;
const int button2Pin = 3;

void setup() {
    Serial.begin(9600);
    pinMode(button1Pin, INPUT);
    pinMode(button2Pin, INPUT);
}

void loop() {
    if(digitalRead(button1Pin))
      Serial.println("Button 1 is pressed");

    if(digitalRead(button2Pin))
      Serial.println("Button 2 is pressed");
      
    delay(150);
}
