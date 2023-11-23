import socket
def main():
    # 创建套接字 AF_INET:IPV4 地址 SOCK_STREAM流套接字
    tcp_server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 绑定服务器 ip 和 端口号
    tcp_server_socket.bind(("127.0.0.1", 20000))

    # 设置为listen
    tcp_server_socket.listen(128) # 最多链接数为 128
    print("TCP server start ...")

    # accept：服务端进入阻塞状态
    # new_client_socket：与客户端服务器的通信socket
    # client_addr：客户的 internet 地址
    new_client_socket, client_addr = tcp_server_socket.accept()
    print("Get client ...")

    # 打印客户端ip地址
    print(f"client address is {client_addr}")

    # 接收客户端发送过来的请求
    recv_data = new_client_socket.recv(1024) # 1024 字节
    print(recv_data)

    # 回送数据给客户端
    new_client_socket.send("TCP server has receive message ... status= OK".encode("utf-8"))

    # 关闭套接字
    new_client_socket.close()
    tcp_server_socket.close()


if __name__ == "__main__":
    main()

