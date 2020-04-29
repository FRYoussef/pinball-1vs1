#  Raspberry Pi Master for Arduino Slave
#  i2c_master_pi.py
#  Connects to Arduino via I2C
  
#  DroneBot Workshop 2019
#  https://dronebotworkshop.com

from smbus import SMBus
import time
import traceback
#import RPi.GPIO as GPIO

START = 0
ERROR_I2C = 1
END = 2
SCORE_UP = 3

#GPIO.setmode(GPIO.BCM)
#GPIO.setup(18,GPIO.OUT)

addr = 0x4 # bus address
bus = SMBus(1) # indicates /dev/ic2-1

numb = 1

print ("Enter 1 for ON or 0 for OFF")
while numb == 1:
	
	# try:
	# 	msg = bus.read_byte(addr)
	# 	print(msg)
	# 	time.sleep(0.5)
		
		

	# 	if(msg == SCORE_UP):
	# 		print("Score up!!")
	# except:
	# 	traceback.print_exc()
	
	ledstate = input(">>>>   ")

	if ledstate == "1":
		print("Sending start")
		#GPIO.output(18, True)
		bus.write_byte(addr, START)
	elif ledstate == "0":
		bus.write_byte(addr, END)
	else:
		numb = 0