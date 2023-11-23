import socket

# 创建一个套接字，
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
# 绑定
host = "127.0.0.1"
port = 20001
address = (host, port)
while True:
    message = input("请输入： ")
    sock.sendto(message.encode('UTF-8'), address)
    data, address = sock.recvfrom(1024)
    print(data.decode('UTF-8'))
    print(address)


# 关闭链接
sock.close()
