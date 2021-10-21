import RPi.GPIO as GPIO
import threading
import _thread
import time

class Move_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    
    def run(self):
        
        print("Előre:" + str(self.stat_var.forward_bool) + " Hátra: " + str(self.stat_var.backward_bool))
        
        move_time =  0.001 * (self.stat_var.moving_pwm_int)
        stop_time = 0.001 * (100 - self.stat_var.moving_pwm_int)
        
        while self.stat_var.forward_bool:
            
            GPIO.output(self.stat_var.forward_pin_int, GPIO.HIGH)
            time.sleep(move_time)
            GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
            time.sleep(stop_time)
            
        GPIO.output(self.stat_var.forward_pin_int, GPIO.LOW)
        self.stat_var.forward_bool = False
        
        while self.stat_var.backward_bool:
            GPIO.output(self.stat_var.backward_pin_int, GPIO.HIGH)
            time.sleep(move_time)
            GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
            time.sleep(stop_time)
        GPIO.output(self.stat_var.backward_pin_int, GPIO.LOW)
        self.stat_var.forward_bool = False
        return