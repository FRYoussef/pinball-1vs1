from time import sleep
from smbus2 import SMBusWrapper
address = 0x08
bus = SMBusWrapper(1)
START = 0
ERROR_I2C = 1
END = 2
SCORE_UP = 3

player1 = 0
player2 = 0

# Give the I2C device time to settle
sleep(2)

start = False
while not start:
    with SMBusWrapper(1) as bus:
        ledstate = input(">>>>   ")
        if ledstate == "1":
            start = True
            print("Sending start")
            bus.write_byte(address, START)

while 1:
    with SMBusWrapper(1) as bus:
        """ ledstate = input("End?   ")
        if ledstate == "0":
            print("Sending end")
            bus.write_byte(address, END)
            break
        else: """
        try:
            data = bus.read_i2c_block_data(address, 0, 1)
            if(data[0] == SCORE_UP):
                print("Score up!!")
                sleep(5)
        except:
            print('Error')
