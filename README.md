# pinball-1vs1
A pinball player vs player. 2 Arduinos, a Raspberry Pi as server, and an Android app as scoreboard. 

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

Besides, for the I2C communication between the Arduinos and the Raspberry you need:
* 2 x 10k Ohm resistor

To know how to build the circuit, follow the diagrams.

### I2C communication
First of all,  open a terminal on the Raspberry Pi and execute the following command:
```shell
sudo raspi-config
```
On the screen, select "Interfacing options", then select "I2C" and "Yes", this will enable the ARM I2C interface.

Secondly, you must install the dependencies required by the SMBus2 library and the SMBus2 library itselfs (you must have Python 3 installed too).
```shell
sudo apt-get install i2c-tools
sudo pip3 install smbus2
```

Then, open Arduino IDE and run the script in both Arduinos. Remember to change SLAVE_ADDRESS (comment and uncomment the lines at the code) in one of the scripts you run. Now, if you execute the following command on your Pi terminal:
```shell
i2cdetect -y 1
```
You should get an output where you can see that 04 and 08 addresses are ready.

Lastly, on your Pi terminal, run the python script:
```shell
python3 raspberry_master.py
```

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

## App

After installing Bluetooth requirements in your Raspberry pi, you also need to install the app in an Android phone. Once done, before launching the app, first pair your Android device and your Pi. After that, you can launch it and select your Pi to connect it.
