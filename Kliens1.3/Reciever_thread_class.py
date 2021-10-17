import socket
import threading
import _thread

class Reciever_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    def run(self):
        print("Szerveroldali fogadószál elindul!")
        while True:
                if self.stat_var.normal_exit_bool:
                    print("Szerveroldali fogadószál leáll!")
                    break
                message = self.stat_var.main_socket.recv(1024).decode('utf-8').strip()
                self.stat_var.recieved_message_string = message