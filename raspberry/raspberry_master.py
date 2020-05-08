from time import sleep
from smbus2 import SMBusWrapper
address_s1 = 0x08
address_s2 = 0x04
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
            bus.write_byte(address_s1, START)
            bus.write_byte(address_s2, START)

while 1:
    with SMBusWrapper(1) as bus:
        """ ledstate = input("End?   ")
        if ledstate == "0":
            print("Sending end")
            bus.write_byte(address, END)
            break
        else: """
        try:
            data1 = bus.read_i2c_block_data(address_s1, 0, 1)
            data2 = bus.read_i2c_block_data(address_s2, 0, 1)
            if(data1[0] == SCORE_UP):
                print(data1[0])
                sleep(7)
        except:
            print('Error')
