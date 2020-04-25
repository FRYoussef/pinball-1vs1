# pinball-1vs1
A pinball player vs player. 2 arduinos and 1 raspberry pi as scoreboard. 

## Arduino

### Installation
This project has used an arduino UNO, which just has 2 external interrupts (pins 2,3), so, in order to avoid this disadvantage we have used this library: [https://github.com/GreyGnome/EnableInterrupt](https://github.com/GreyGnome/EnableInterrupt).

Follow this guide to install it: [https://github.com/GreyGnome/EnableInterrupt/wiki/Installation](https://github.com/GreyGnome/EnableInterrupt/wiki/Installation)

**Note: because of that library, an error has shown on the console when you compile the code. Don't care about it, just carry on.**

### Required components

* 2 x Arduino UNO
* 4 x Push button
* 4 x 10k Ohm resistor
* 4 x 220 Ohm resistor
* 2 x HC-SR04 sensor
* 4 x 5V solenoid
* 4 x diode
* 4 x NPN transistor