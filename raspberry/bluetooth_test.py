from bluetooth import *
from time import sleep

if __name__ == '__main__':
    msgs = [bytes([128]), bytes([144]), bytes([144]), bytes([128]), bytes([144])]
    i_msg = 0
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

    first = True
    while True:
        print(f"Waiting for connection on RFCOMM channel {port}")

        client_sock, client_info = server_sock.accept()
        print(f"Accepted connection from {client_info}")

        try:
            print("Waiting for data...")
            data = client_sock.recv(1024)

            if len(data) == 0:
                break

            print(f"received [{data}]")

            while i_msg < len(msgs):
                print(f"sending [{msgs[i_msg]}]")
                client_sock.send(msgs[i_msg])
                i_msg += 1
                sleep(3)

        except IOError:
            pass

        except KeyboardInterrupt:
            print("disconnected")
            client_sock.close()
            server_sock.close()
            print("all done")
            break