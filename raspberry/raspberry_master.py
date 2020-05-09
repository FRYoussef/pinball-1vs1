from time import sleep
from smbus2 import SMBusWrapper
address_s1 = 0x08
address_s2 = 0x04
bus = SMBusWrapper(1)
START = 0
END = 64
SCORE_UP = 128

player1 = 0
player2 = 0
winner = 0

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

while start:
    with SMBusWrapper(1) as bus:
        try:
            data1 = bus.read_i2c_block_data(address_s1, 0, 1)
            data2 = bus.read_i2c_block_data(address_s2, 0, 1)
            if data1[0] == SCORE_UP:
                player1 += 1
            if data2[0] == SCORE_UP:
                player2 += 1
            print(str(player1) + " - " + str(player2))
            sleep(2)
            if player1 == 5:
                winner = 1
            if player2 == 5:
                winner = 2
            if winner != 0:
                bus.write_byte(address_s1, END)
                bus.write_byte(address_s2, END)
                print("Winner: " + str(winner))
                start = False
        except:
            print('Error')
