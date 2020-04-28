#  Raspberry Pi Master for Arduino Slave
#  i2c_master_pi.py
#  Connects to Arduino via I2C
  
#  DroneBot Workshop 2019
#  https://dronebotworkshop.com

from smbus import SMBus
#import RPi.GPIO as GPIO

START = 0
ACK_GOAL = 1
END = 2
ACK_START = 3
SCORE_UP = 4
ACK_END = 5

#GPIO.setmode(GPIO.BCM)
#GPIO.setup(18,GPIO.OUT)

addr = 0x8 # bus address
bus = SMBus(1) # indicates /dev/ic2-1

numb = 1

print ("Enter 1 for ON or 0 for OFF")
while numb == 1:

	ledstate = input(">>>>   ")

	if ledstate == "1":
		#GPIO.output(18, True)
		bus.write_byte(addr, START)
	elif ledstate == "0":
		bus.write_byte(addr, END)
	else:
		numb = 0