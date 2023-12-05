import socket

def start_udp_server(host, port):
    # 创建 socket 对象
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # 绑定地址和端口
    server_socket.bind((host, port))
    print(f"UDP Server listening on {host}:{port}")

    while True:
        # 接收数据和客户端地址
        data, client_address = server_socket.recvfrom(1024)
        print(f"Received data from {client_address}: {data.decode('utf-8')}")

        # 发送一条回复给客户端
        response = "Message received!"
        server_socket.sendto(response.encode('utf-8'), client_address)

if __name__ == "__main__":
    # 指定服务器的主机和端口
    server_host = "172.25.46.29"  # 本地回环地址
    server_port = 20002 # UDP

    start_udp_server(server_host, server_port)
