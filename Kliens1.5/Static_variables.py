import socket

class Static_variables_class:
    main_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    port_integer = 10000
    ip_string = ""
    username_string = ""
    target_username_string = ""
    
    forward_bool = False
    backward_bool = False
    left_bool = False
    right_bool = False
    
    left_turned_percent_int = 0
    right_turned_percent_int = 0
    
    turning_pwm_int = 30
    moving_pwm_int = 100
    
    stop_turn = False
    stop_move = False
    turn_in_process = False
    
    forward_pin_int = 40
    backward_pin_int = 38
    left_pin_int = 36
    right_pin_int = 32
    
    """forward_pin_int = 38
    backward_pin_int = 40
    left_pin_int = 36
    right_pin_int = 32"""
    
    output_message_string = ""
    input_message_string = ""
    recieved_message_string = ""
    normal_exit_bool = False
    manual_wheel_setter = False
    qr_data_string = ""
    qr_data_prev_string = "prev"
    qr_data_found_bool = False
    camera_input_setter_int = 1
    show_camera_output = True
    connect_to_server = False
    
    
    exit_command_list = ["exit","kilep","kilepes","bezar","bezaras","Tschüss","tschüss","Auf wiedersehen","auf wiedersehen"]
    yes_command_list = ["Igen","igen","i","I","Yes","yes","Y","y","Si","si","Ja","ja","1"]
    no_command_list = ["Nem","nem","n","N","No","no","Nein","nein","0"]
    
    @staticmethod
    def output_message_builder(stat_var):
        stat_var.output_message_string = stat_var.target_username_string + "@" + stat_var.input_message_string + "\n"
        #stat_var.output_message_string = unidecode(stat_var.target_username_string + "@" + stat_var.input_message_string + "\n")
        #return stat_var.output_message_string
