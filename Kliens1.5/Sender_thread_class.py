import socket
import threading
import _thread 
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
        
    def run(self):
        print("Küldő és bemenet kezelő szál elindul!")
        while True:
                self.stat_var.input_message_string = input()
                self.stat_var.output_message_builder(stat_var)
                if self.stat_var.input_message_string == "info":
                    print(INFO)
                elif self.stat_var.connect_to_server:
                    self.stat_var.main_socket.send(self.stat_var.output_message_string.encode())
                
                print("|"+self.stat_var.input_message_string+"|")
                
                if self.stat_var.input_message_string == "wheel_set":
                    self.stat_var.manual_wheel_setter = True
                
                if self.stat_var.input_message_string in self.stat_var.exit_command_list:
                    print("Küldő és bemenet kezelő szál leáll!")
                    self.stat_var.normal_exit_bool = True
                    break
                