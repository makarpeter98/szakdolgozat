import RPi.GPIO as GPIO
import threading
import _thread
import time

class Turn_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    
    def run(self):
        print("Kormányszögállítás indul!")
        while self.stat_var.turn_in_process:
            time.sleep(0.01)
            Print("Várok...")
        
        if self.stat_var.right_bool and self.stat_var.left_bool:
            self.stat_var.right_bool = False
            self.stat_var.left_bool = False
            return
        
        if self.stat_var.turning_pwm_int < 15:
            print("Kisebb mint 15!")
            self.stat_var_turning_pwm_int = 15
        
        pwm_multiplier = 0.8
        
        turn_time_num = 0.001 * (self.stat_var.turning_pwm_int)
        turn_stop_time_num = 0.001 * (100 - self.stat_var.turning_pwm_int)
        
        #print("fordít: " + str(turn_time_num) + " megáll: " + str(turn_stop_time_num))
        
        if self.stat_var.right_bool and self.stat_var.right_turned_percent_int < 100:
            
            while self.stat_var.right_turned_percent_int < 100:
                if self.stat_var.left_turned_percent_int > 0:
                    self.stat_var.left_turned_percent_int -= self.stat_var.turning_pwm_int*pwm_multiplier
                    print("Balra: "+str(self.stat_var.left_turned_percent_int) + "%")
                else:
                    self.stat_var.right_turned_percent_int += self.stat_var.turning_pwm_int*pwm_multiplier
                    print("Jobbra: "+str(self.stat_var.right_turned_percent_int) + "%")
                GPIO.output(self.stat_var.right_pin_int, GPIO.HIGH)
                time.sleep(turn_time_num / 2)
                GPIO.output(self.stat_var.right_pin_int, GPIO.LOW)
                time.sleep(turn_stop_time_num / 2)
            self.stat_var.left_turned_percent_int = 0
            self.stat_var.right_turned_percent_int = 100
             
        if self.stat_var.left_bool and self.stat_var.left_turned_percent_int < 100:
            
            while self.stat_var.left_turned_percent_int < 100:
                #print("Balra: "+str((i*10) / 2) + "%")
                if self.stat_var.right_turned_percent_int > 0:
                    self.stat_var.right_turned_percent_int -= self.stat_var.turning_pwm_int*pwm_multiplier
                    print("Jobbra: "+str(self.stat_var.right_turned_percent_int) + "%")
                else:
                    self.stat_var.left_turned_percent_int += self.stat_var.turning_pwm_int*pwm_multiplier
                    print("Balra: "+str(self.stat_var.left_turned_percent_int) + "%")
                GPIO.output(self.stat_var.left_pin_int, GPIO.HIGH)
                time.sleep(turn_time_num / 2)
                GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
                time.sleep(turn_stop_time_num / 2)
            GPIO.output(self.stat_var.left_pin_int, GPIO.LOW)
            self.stat_var.left_turned_percent_int = 100
            self.stat_var.right_turned_percent_int = 0
        print("Kormányszögállítás leáll!")
        return