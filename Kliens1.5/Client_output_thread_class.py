import RPi.GPIO as GPIO
import threading
import _thread
import time
from GPIO_output_handler_thread_class import GPIO_output_handler_thread_class
from Turn_thread_class import Turn_thread_class
from Move_thread_class import Move_thread_class

class Client_output_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var  
    
    def run(self):
        
        print("Kliens kimenet kezelőszál elindult!")
        
        GPIO_output_handler_thread_class.gpio_set(self)

        while True:
                if self.stat_var.qr_data_found_bool or self.stat_var.recieved_message_string != "":
                    
                    if self.stat_var.recieved_message_string != "":
                        print("bejovo uzenet: " + self.stat_var.recieved_message_string)
                    
                    """Előre"""
                    if self.stat_var.qr_data_string == "FORWARD:TRUE" or self.stat_var.recieved_message_string == "FORWARD:TRUE":
                        self.stat_var.forward_bool = True
                        self.stat_var.backward_bool = False
                        move_thread = Move_thread_class(6,"Move_thread_forward",self.stat_var)
                        move_thread.start()
                    
                    if self.stat_var.qr_data_string == "FORWARD:FALSE" or self.stat_var.recieved_message_string == "FORWARD:FALSE":
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
                        self.stat_var.forward_bool = False
                    
                    """Hátra"""    
                    if self.stat_var.qr_data_string == "BACKWARD:TRUE" or self.stat_var.recieved_message_string == "BACKWARD:TRUE":
                        self.stat_var.forward_bool = False
                        self.stat_var.backward_bool = True
                        move_thread = Move_thread_class(7,"Move_thread_back",self.stat_var)
                        move_thread.start()
                    
                    if self.stat_var.qr_data_string == "BACKWARD:FALSE" or self.stat_var.recieved_message_string == "BACKWARD:FALSE":
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
                        self.stat_var.backward_bool = False
                    
                    """Jobbra"""
                    if self.stat_var.qr_data_string == "RIGHT:TRUE" or self.stat_var.recieved_message_string == "RIGHT:TRUE":
                        self.stat_var.right_bool = True
                        self.stat_var.left_bool = False
                        turn_thread = Turn_thread_class(5,"Turn_thread",self.stat_var)
                        turn_thread.start()
                        
                    if self.stat_var.qr_data_string == "RIGHT:FALSE" or self.stat_var.recieved_message_string == "RIGHT:FALSE":
                        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                        self.stat_var.right_bool = False
                        
                    """Balra"""
                    if self.stat_var.qr_data_string == "LEFT:TRUE" or self.stat_var.recieved_message_string == "LEFT:TRUE":
                        self.stat_var.right_bool = False
                        self.stat_var.left_bool = True
                        turn_thread = Turn_thread_class(5,"Turn_thread",self.stat_var)
                        turn_thread.start()
                    
                    if self.stat_var.qr_data_string == "LEFT:FALSE" or self.stat_var.recieved_message_string == "LEFT:FALSE":
                        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                        self.stat_var.left_bool = False
                    
                    self.stat_var.qr_data_string = ""
                    self.stat_var.recieved_message_string = ""
                    self.stat_var.qr_data_found_bool = False
                    
                    
                        
                """if self.stat_var.recieved_message_string != "":
                    print("bejovo uzenet: " + self.stat_var.recieved_message_string)
                
                    Előre
                    if self.stat_var.recieved_message_string == "FORWARD:TRUE":
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
                    
                    if self.stat_var.recieved_message_string == "FORWARD:FALSE":
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
                    
                    Hátra    
                    if self.stat_var.recieved_message_string == "BACKWARD:TRUE":
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
                    
                    if self.stat_var.recieved_message_string == "BACKWARD:FALSE":
                        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
                    
                    Jobbra
                    if self.stat_var.recieved_message_string == "RIGHT:TRUE":
                        GPIO.output(self.stat_var.right_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                    
                    if self.stat_var.recieved_message_string == "RIGHT:FALSE":
                        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                    
                    Balra
                    if self.stat_var.recieved_message_string == "LEFT:TRUE":
                        GPIO.output(self.stat_var.left_pin_int, GPIO.HIGH)
                        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                    
                    if self.stat_var.recieved_message_string == "LEFT:FALSE":
                        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                    
                    self.stat_var.recieved_message_string = """""
                
                if self.stat_var.manual_wheel_setter:
                    GPIO_output_handler_thread_class.wheel_manual_setter(self)
                    self.stat_var.manual_wheel_setter = False
                if self.stat_var.normal_exit_bool:
                    print("Kliens kimenet kezelőszál leáll!")
                    GPIO_output_handler_thread_class.gpio_turn_off(self)
                    break
