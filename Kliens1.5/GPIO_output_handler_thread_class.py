import RPi.GPIO as GPIO
import threading
import _thread
import time

class GPIO_output_handler_thread_class (threading.Thread):
    
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    
    def wheel_manual_setter(self):
        GPIO_output_handler_thread_class.gpio_turn_off(self)
        
        print("Manuális kormánybeállítás\nJobbra: j\nBalra:b\nKilepes:q")
        while True:
            target = input()
            if target == "j":
                for i in range(0,2):
                    GPIO.output(self.stat_var.right_pin_int, GPIO.HIGH)
                    time.sleep(0.01)
                    GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                    time.sleep(0.01)
                
            elif target == "b":
                for i in range(0,2):
                    
                    GPIO.output(self.stat_var.left_pin_int, GPIO.HIGH)
                    time.sleep(0.01)
                    GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                    time.sleep(0.01)
            elif target == "q":
                print("Kiléptél a manuális kormánybeállításból!")
                break
            self.stat_var.input_message_string = ""
        self.stat_var.right_bool = False
        self.stat_var.left_bool = False
        self.stat_var.forward_bool = False
        self.stat_var.backward_bool = False
        
        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
        
        self.stat_var.right_turned_percent_int = 0
        self.stat_var.left_turned_percent_int = 0
        self.stat_var.manual_wheel_setter = False
        return
    
    def gpio_set(self):
        
        print("GPIO kimenet beállíása!")
        
        GPIO.setmode(GPIO.BOARD)
        GPIO.setwarnings(False)
        GPIO.setup(self.stat_var.forward_pin_int, GPIO.OUT)
        GPIO.setup(self.stat_var.backward_pin_int, GPIO.OUT)
        GPIO.setup(self.stat_var.left_pin_int, GPIO.OUT)
        GPIO.setup(self.stat_var.right_pin_int, GPIO.OUT)
        
        print("Előre teszt!")
        GPIO.output(self.stat_var.forward_pin_int, GPIO.HIGH)
        time.sleep(0.1)
        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
        time.sleep(0.2)
        
        print("Hátra teszt!")
        GPIO.output(self.stat_var.backward_pin_int, GPIO.HIGH)
        time.sleep(0.1)
        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
        
        print("Balra teszt!")
        GPIO.output(self.stat_var.left_pin_int, GPIO.HIGH)
        time.sleep(0.1)
        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
        time.sleep(0.2)
        
        print("Jobbra teszt!")
        GPIO.output(self.stat_var.right_pin_int, GPIO.HIGH)
        time.sleep(0.1)
        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
        time.sleep(0.2)
        
        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
        
        print("GPIO kimenet beállítva!")
     
    def gpio_turn_off(self):
        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
        GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
        
        print("GPIO kikapcsolva")