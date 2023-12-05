import socket

def start_server(host, port):
    # 创建 socket 对象
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # 绑定地址和端口
    server_socket.bind((host, port))

    # 监听，等待客户端连接
    server_socket.listen(5)
    print(f"Server listening on {host}:{port}")

    while True:
        # 等待客户端连接
        client_socket, client_address = server_socket.accept()   # 阻塞
        print(f"Accepted connection from {client_address}")
        while True:
            # 接收客户端发送的数据
            data = client_socket.recv(1024)
            if not data:
                print(f"{client_address} exit...")
                break  # 如果没有数据，表示客户端已断开连接
            # 打印接收到的数据
            print(f"Received data: {data.decode('utf-8')}")

            # 发送一条回复给客户端
            response = "Message received!"
            client_socket.send(response.encode('utf-8'))

        # 关闭与客户端的连接    
        client_socket.close()
        # 关闭服务器的socket
        # server_socket.close()
        info = input("print 'q' to close server:")
        if info.lower() == 'q':
            print("server quit")
            server_socket.close()
            break

if __name__ == "__main__":
    # 指定服务器的主机和端口
    server_host = "172.25.46.29"  # 本地回环地址
    server_port = 20001

    start_server(server_host, server_port)
