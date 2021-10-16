#!/usr/bin/python3
INFO = """
MuThCliPy_1.2
2021.10.15 21:05 Makár Péter Ákos L3HSVK (1.1)
2021.10.15 22:58 Makár Péter Ákos L3HSVK (1.2)
Ismert bugok:
-nincs (még) (1.1)
Csatlakozás a szerverhez (0.0)
Több szálas futás (1.0)
Párhuzamos írás és olvasás (1.0)
Nincs ékezet eltávolítás (majd lesz) (1.0)
GPIO lábak elérése, parancsok szerinti vezérlése (1.1)
Kamera integrálása, képen található QR kód beolvasása String formátumban (1.2)
"""
version = "1.2"

import RPi.GPIO as GPIO
import socket
import threading
import _thread
import time
import cv2
#import unidecode

class Static_Variables:
    main_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    port_integer = 10000
    ip_string = ""
    username_string = ""
    target_username_string = ""
    forward_bool = False
    backward_bool = False
    left_bool = False
    right_bool = False
    forward_pin_int = 32
    backward_pin_int = 36
    left_pin_int = 38
    right_pin_int = 40
    output_message_string = ""
    input_message_string = ""
    recieved_message_string = ""
    normal_exit_bool = False
    qr_data_string = ""
    qr_data_found_bool = False
    exit_command_list = ["exit","kilep","kilepes","bezar","bezaras"]
    
    @staticmethod
    def output_message_builder(stat_var):
        stat_var.output_message_string = stat_var.target_username_string + "@" + stat_var.input_message_string + "\n"
        #stat_var.output_message_string = unidecode(stat_var.target_username_string + "@" + stat_var.input_message_string + "\n")
        #return stat_var.output_message_string

class Test_class:
        def test_void():
            print("Test")
            
class Camera_handler_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    def run(self):
        cap = cv2.VideoCapture(1)
        detector = cv2.QRCodeDetector()
        while True:
            # get the image
            _, img = cap.read()
            # get bounding box coords and data
            data, bbox, _ = detector.detectAndDecode(img)
            
            # if there is a bounding box, draw one, along with the data
            if(bbox is not None):
                for i in range(len(bbox)):
                    cv2.line(img, tuple(bbox[i][0]), tuple(bbox[(i+1) % len(bbox)][0]), color=(255,
                             0, 255), thickness=2)
                cv2.putText(img, data, (int(bbox[0][0][0]), int(bbox[0][0][1]) - 10), cv2.FONT_HERSHEY_SIMPLEX,
                            0.5, (0, 255, 0), 2)
                if data:
                    print("data found: ", data)
            # display the image preview
            cv2.imshow("code detector", img)
            if(cv2.waitKey(1) == ord("q")):
                break
        # free camera object and exit
        cap.release()
        cv2.destroyAllWindows()

        return

class Client_output_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    
    def gpio_set(self):
        GPIO.setmode(GPIO.BOARD)
        GPIO.setwarnings(False)
        GPIO.setup(self.stat_var.forward_pin_int, GPIO.OUT)
        GPIO.setup(self.stat_var.backward_pin_int, GPIO.OUT)
        GPIO.setup(self.stat_var.left_pin_int, GPIO.OUT)
        GPIO.setup(self.stat_var.right_pin_int, GPIO.OUT)
        
        GPIO.output(self.stat_var.forward_pin_int, GPIO.HIGH)
        GPIO.output(self.stat_var.backward_pin_int, GPIO.HIGH)
        GPIO.output(self.stat_var.left_pin_int, GPIO.HIGH)
        GPIO.output(self.stat_var.right_pin_int, GPIO.HIGH)
        
        time.sleep(1)
        
        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
    
    def run(self):
        Client_output_thread_class.gpio_set(self)

        while True:
                
                if self.stat_var.recieved_message_string != "":
                    print("bejovo uzenet: " + self.stat_var.recieved_message_string)
                
                    """Előre"""
                    if self.stat_var.recieved_message_string == "FORWARD:TRUE":
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
                    
                    if self.stat_var.recieved_message_string == "FORWARD:FALSE":
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
                    
                    """Hátra"""    
                    if self.stat_var.recieved_message_string == "BACKWARD:TRUE":
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
                    
                    if self.stat_var.recieved_message_string == "BACKWARD:FALSE":
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
                    
                    """Jobbra"""
                    if self.stat_var.recieved_message_string == "RIGHT:TRUE":
                        GPIO.output(self.stat_var.right_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                    
                    if self.stat_var.recieved_message_string == "RIGHT:FALSE":
                        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                    
                    """Balra"""
                    if self.stat_var.recieved_message_string == "LEFT:TRUE":
                        GPIO.output(self.stat_var.left_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                    
                    if self.stat_var.recieved_message_string == "LEFT:FALSE":
                        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                    
                    self.stat_var.recieved_message_string = ""
                    
                if self.stat_var.normal_exit_bool:
                    break
                
class Reciever_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    def run(self):
        while True:
                if self.stat_var.normal_exit_bool:
                    break
                message = self.stat_var.main_socket.recv(1024).decode('utf-8').strip()
                self.stat_var.recieved_message_string = message

class Sender_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    def run(self):
        while True:
                self.stat_var.input_message_string = input()
                self.stat_var.output_message_builder(stat_var)
                if self.stat_var.input_message_string == "info":
                    print(INFO)
                else:  
                    self.stat_var.main_socket.send(self.stat_var.output_message_string.encode())
                
                if self.stat_var.input_message_string in self.stat_var.exit_command_list:
                    self.stat_var.normal_exit_bool = True
                    break
                
class Main_thread_class:

    def info_printer(input):
        print(input)
    
    def main(stat_var):
        try:
            stat_var.main_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
            stat_var.main_socket.connect((stat_var.ip_string,stat_var.port_integer))
            stat_var.main_socket.send(stat_var.username_string.encode())
            Main_thread_class.info_printer("Sikeres csatlakozás a szerverhez! :)")
            sender_thread = Sender_thread_class(1, "Sender_thread",stat_var)
            sender_thread.start()
                
            reciever_thread = Reciever_thread_class(2,"Reciever_thread",stat_var)
            reciever_thread.start()
                
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
    stat_var = Static_Variables()
    """stat_var.ip_string = input("Add meg a szerver IP-címét: ")
    stat_var.username_string = input("Add meg a felhasználóneved: ")+"\n"
    stat_var.target_username_string = input("Add meg a céleszköz felhasználónevét: ")"""
    stat_var.ip_string = "192.168.0.107"
    stat_var.username_string = "Sanyi\n"
    stat_var.target_username_string = "Sanyi"
    Main_thread_class.main(stat_var)