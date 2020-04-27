# pinball-1vs1
A pinball player vs player. 2 arduinos and 1 raspberry pi as scoreboard. 

## Arduino

### Installation
This project has used an arduino UNO, which just has 2 external interrupts (pins 2,3), so, in order to avoid this disadvantage we have used this library: [https://github.com/GreyGnome/EnableInterrupt](https://github.com/GreyGnome/EnableInterrupt).

Follow this guide to install it: [https://github.com/GreyGnome/EnableInterrupt/wiki/Installation](https://github.com/GreyGnome/EnableInterrupt/wiki/Installation)

**Note: because of that library, an error has shown on the console when you compile the code. Don't care about it, just carry on.**

### Required components
This project uses two Arduino UNO, thus, for each microcontroller you need the following componenets:

* 2 x Push button
* 2 x 10k Ohm resistor
* 2 x 220 Ohm resistor
* 1 x HC-SR04 sensor
* 2 x 5V solenoid
* 2 x diode
* 2 x NPN transistor


## Raspberry pi

### Bluetooth installation
First, install bluez for bluetooth support, and bluetooth lib:

`sudo apt-get install bluez python3-bluez`

`sudo apt-get install python-bluetooth`

If you run the script the following error will come up:

`bluetooth.btcommon.BluetoothError: (2, 'No such file or directory')`

To solve it, you need to run Bluetooth daemon in 'compatibility' mode, to do that edit `/etc/systemd/system/dbus-org.bluez.service` file, and replace the following line:

`ExecStart=/usr/lib/bluetooth/bluetoothd`

into

`ExecStart=/usr/lib/bluetooth/bluetoothd -C`

Now, you have to add the Serial Port Profile:

`sudo sdptool add SP`

That's all, reboot your pi and everything will be fine.