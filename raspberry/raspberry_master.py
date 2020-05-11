from bluetooth import *
from time import sleep
from smbus2 import SMBusWrapper
import _thread

address_s1 = 0x08
address_s2 = 0x04
bus = SMBusWrapper(1)

started = False

START = bytes([0])
END = bytes([64])
SCORE_UP =  bytes([128])

GOAL_P1 = bytes([128])
GOAL_P2 = bytes([144])

def end_msg():
    data = client_sock.recv(1024) # wait till end msg
    if data == END:
        started = False



if __name__ == '__main__':
    sleep(2) # Give the I2C device time to settle
    server_sock = BluetoothSocket( RFCOMM )
    server_sock.bind(("", PORT_ANY))
    server_sock.listen(1)

    port = server_sock.getsockname()[1]

    # Always use this uuid
    uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

    advertise_service(
        server_sock,
        "PinballServer",
        service_id = uuid,
        service_classes = [ uuid, SERIAL_PORT_CLASS ],
        profiles = [ SERIAL_PORT_PROFILE ])

    print(f"Waiting for connection on RFCOMM channel {port}")
    client_sock, client_info = server_sock.accept()
    print(f"Accepted connection from {client_info}")

  
    while 1:
        with SMBusWrapper(1) as bus:
            data = client_sock.recv(1024)
            _thread.start_new_thread( end_msg, () )
            if data == START:
                started = True
                print(f"received [{data}] from app")
                print("Sending start msg to arduinos")
                bus.write_byte(address_s1, int.from_bytes(START, byteorder='big'))
                bus.write_byte(address_s2, int.from_bytes(START, byteorder='big'))
            while started:
                try:
                    data1 = bus.read_i2c_block_data(address_s1, 0, 1)
                    data2 = bus.read_i2c_block_data(address_s2, 0, 1)
                    if data1[0] == int.from_bytes(SCORE_UP, byteorder='big'):
                        print(f"sending score up (p1) to app")
                        client_sock.send(GOAL_P2)
                        sleep(3)
                    if data2[0] == int.from_bytes(SCORE_UP, byteorder='big'):
                        print(f"sending score up (p2) to app")
                        client_sock.send(GOAL_P1)
                        sleep(3)
                except Exception as e:
                    bus.write_byte(address_s1, int.from_bytes(END, byteorder='big'))
                    bus.write_byte(address_s2, int.from_bytes(END, byteorder='big'))
                    client_sock.close()
                    server_sock.close()
                    print(e)