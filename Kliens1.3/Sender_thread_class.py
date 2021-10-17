import socket
import threading
import _thread

class Sender_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
        
    def run(self):
        print("Küldő és bemenet kezelő szál leáll!")
        while True:
                self.stat_var.input_message_string = input()
                self.stat_var.output_message_builder(stat_var)
                if self.stat_var.input_message_string == "info":
                    print(INFO)
                elif self.stat_var.connect_to_server:
                    self.stat_var.main_socket.send(self.stat_var.output_message_string.encode())
                if self.stat_var.input_message_string in self.stat_var.exit_command_list:
                    print("Küldő és bemenet kezelő szál leáll!")
                    self.stat_var.normal_exit_bool = True
                    break