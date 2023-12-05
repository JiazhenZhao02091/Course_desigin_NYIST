import socket

def start_client(host, port):
    # 创建 socket 对象
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # 连接服务器
    client_socket.connect((host, port))
    print(f"Connected to server on {host}:{port}")

    while True:
        # 输入要发送的消息
        message = input("Enter a message (or 'exit' to quit): ")

        # 如果输入 'exit'，则退出循环
        if message.lower() == 'exit':
            # 发送至服务器
            client_socket.send(message.encode('utf-8'))
            # 退出Client  
            break

        # 发送消息给服务器
        client_socket.send(message.encode('utf-8'))
        print(f"Sent message to server: {message}")

        # 接收服务器的回复
        response = client_socket.recv(1024)
        print(f"Received response from server: {response.decode('utf-8')}")

    # 关闭客户端的socket
    client_socket.close()

if __name__ == "__main__":
    # 指定服务器的主机和端口
    server_host = "47.93.127.150"  # 本地回环地址
    server_port = 20001

    start_client(server_host, server_port)


# (ip.dst ==47.93.127.150 and tcp and not ssh) or (ip.dst ==10.1.164.172 and tcp and not ssh and ip.src==47.93.127.150)