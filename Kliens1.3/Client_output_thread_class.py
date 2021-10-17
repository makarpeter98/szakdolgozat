import RPi.GPIO as GPIO
import threading
import _thread
import time

class Client_output_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    
    def gpio_set(self):
        
        print("GPIO kimenet beállíása!")
        
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
        
        print("GPIO kimenet beállítva!")
    
    def run(self):
        
        print("Kliens kimenet kezelőszál elindult!")
        
        Client_output_thread_class.gpio_set(self)

        while True:
                if self.stat_var.qr_data_found_bool:
                    
                    print(self.stat_var.qr_data_string)
                    
                    """Előre"""
                    if self.stat_var.qr_data_string == "FORWARD:TRUE":
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
                    
                    if self.stat_var.qr_data_string == "FORWARD:FALSE":
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
                    
                    """Hátra"""    
                    if self.stat_var.qr_data_string == "BACKWARD:TRUE":
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
                    
                    if self.stat_var.qr_data_string == "BACKWARD:FALSE":
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
                    
                    """Jobbra"""
                    if self.stat_var.qr_data_string == "RIGHT:TRUE":
                        GPIO.output(self.stat_var.right_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                    
                    if self.stat_var.qr_data_string == "RIGHT:FALSE":
                        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                    
                    """Balra"""
                    if self.stat_var.qr_data_string == "LEFT:TRUE":
                        GPIO.output(self.stat_var.left_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                    
                    if self.stat_var.qr_data_string == "LEFT:FALSE":
                        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                    
                    self.stat_var.qr_data_string = ""
                    self.stat_var.qr_data_found_bool = False
                        
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
                    print("Kliens kimenet kezelőszál leáll!")
                    break
