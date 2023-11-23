import socket
import time

# 创建一个套接字，
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
# 绑定
host = '127.0.0.1'
port = 20001
sock.bind((host, port))
print("Server start ...")

while True:
        data, address = sock.recvfrom(1024)

        # 打印输出信息以及客户端地址
        print(data.decode('UTF-8'), address)

        message = "服务器收到信息"
        if data:
            sent = sock.sendto(message.encode('UTF-8'), address)

# 关闭链接

sock.close()
