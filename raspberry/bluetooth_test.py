from bluetooth import *
from time import sleep

if __name__ == '__main__':
    msgs = [bytes([12]), bytes([8]), bytes([9]), bytes([9]), bytes([8]), bytes([9]), bytes([12])]
    i_msg = 0
    server_sock = BluetoothSocket( RFCOMM )
    server_sock.bind(("", PORT_ANY))
    server_sock.listen(1)

    port = server_sock.getsockname()[1]

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

        while True:
            try:
                print("Witing for data...")
                data = client_sock.recv(1024)

                if len(data) == 0:
                    break

                print(f"received [{data}]")
                client_sock.send(msgs[i_msg])
                print(f"sending [{msgs[i_msg]}]")
                i_msg = (i_msg + 1) % len(msgs)
                sleep(3)

                if first:
                    first = False
                    client_sock.send(msgs[i_msg])
                    print(f"sending [{msgs[i_msg]}]")
                    i_msg = (i_msg + 1) % len(msgs)

            except IOError:
                pass

            except KeyboardInterrupt:
                print("disconnected")
                client_sock.close()
                server_sock.close()
                print("all done")
                break