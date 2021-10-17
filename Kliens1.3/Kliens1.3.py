#!/usr/bin/python3
INFO = """
MuThCliPy_1.2
2021.10.15 21:05 Makár Péter Ákos L3HSVK (1.1)
2021.10.15 22:58 Makár Péter Ákos L3HSVK (1.2)
2021.10.15 15:33 Makár Péter Ákos L3HSVK (1.3)
Ismert bugok:
-nincs (még) (1.1)
-Sender_thread_class nem látja a stat_var-t (1.3)
Csatlakozás a szerverhez (0.0)
Több szálas futás (1.0)
Párhuzamos írás és olvasás (1.0)
Nincs ékezet eltávolítás (majd lesz) (1.0)
GPIO lábak elérése, parancsok szerinti vezérlése (1.1)
Kamera integrálása, képen található QR kód beolvasása String formátumban (1.2)
Forráskód kiszervezése több fájlba, GPIO QR-kód szerinti vezérlése (1.3)
"""
version = "1.3"

#import RPi.GPIO as GPIO
import socket
import threading
import _thread
#import time
#import cv2

from Camera_handler_thread_class import Camera_handler_thread_class
from Static_variables import Static_variables_class
from Client_output_thread_class import Client_output_thread_class
from Reciever_thread_class import Reciever_thread_class
#from Sender_thread_class import Sender_thread_class
#import unidecode

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
                elif self.stat_var.connect_to_server and self.stat_var.input_message_string != "exit":
                    self.stat_var.main_socket.send(self.stat_var.output_message_string.encode())
                if self.stat_var.input_message_string in self.stat_var.exit_command_list:
                    print("Küldő és bemenet kezelő szál leáll!")
                    self.stat_var.normal_exit_bool = True
                    break
                
class Main_class:

    def info_printer(input):
        print(input)
    
    def main(stat_var):
        try:
            if stat_var.connect_to_server: 
                stat_var.main_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
                stat_var.main_socket.connect((stat_var.ip_string,stat_var.port_integer))
                stat_var.main_socket.send(stat_var.username_string.encode())
                Main_class.info_printer("Sikeres csatlakozás a szerverhez! :)")
                
                reciever_thread = Reciever_thread_class(2,"Reciever_thread",stat_var)
                reciever_thread.start()
                
            sender_thread = Sender_thread_class(1, "Sender_thread",stat_var)
            sender_thread.start()
                
            client_output_thread = Client_output_thread_class(3,"Client_output_thread",stat_var)
            client_output_thread.start()
            
            camera_handler_thread = Camera_handler_thread_class(4,"Camera_handler_thread",stat_var)
            camera_handler_thread.start()
            
        except:
            if not stat_var.normal_exit_bool:
                Main_thread_class.info_printer("Nem sikerült csatlakozni a szerverhez!\nEllenőrizd az internetkapcsolatot!\nLehet, hogy a szerver offline.")
            return 0
        

if __name__ == "__main__":
    print("Többszálas kliens Python változat " + version)
    stat_var = Static_variables_class()
    """stat_var.ip_string = input("Add meg a szerver IP-címét: ")
    stat_var.username_string = input("Add meg a felhasználóneved: ")+"\n"
    stat_var.target_username_string = input("Add meg a céleszköz felhasználónevét: ")"""
    stat_var.ip_string = "192.168.0.107"
    stat_var.username_string = "Sanyi\n"
    stat_var.target_username_string = "Sanyi"
    
    command = input("Csatlakozzak a szerverre? (I/N) ")
    if command in stat_var.yes_command_list:
        stat_var.connect_to_server = True
    if command in stat_var.no_command_list:
        stat_var.connect_to_server = False
    
    command = input("Jelenítsem meg a kamera képét? (I/N) ")
    if command in stat_var.yes_command_list:
        stat_var.show_camera_output = True
    if command in stat_var.no_command_list:
        stat_var.show_camera_output = False
        
    command = input("USB kamerát használjak a QR kód olvasására? (I/N) ")
    if command in stat_var.yes_command_list:
        stat_var.camera_input_setter_int = 1
    if command in stat_var.no_command_list:
        stat_var.camera_input_setter_int = 0
    Main_class.main(stat_var)