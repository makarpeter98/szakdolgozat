import threading
import _thread
import cv2
import time

class Camera_handler_thread_class (threading.Thread):
    def __init__(self,threadID,name,stat_var):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.stat_var = stat_var
    def run(self):
        
        print("Kamera kezelő szál elidult!")
        
        cap = cv2.VideoCapture(self.stat_var.camera_input_setter_int)
        detector = cv2.QRCodeDetector()
        if not self.stat_var.show_camera_output:
            print("Kamerakép: rejtett")
            while True:
                _, img = cap.read()
                data, bbox, _ = detector.detectAndDecode(img)
                
                if data:
                    self.stat_var.qr_data_found_bool = True
                    if self.stat_var.qr_data_prev_string != self.stat_var.qr_data_string:
                        self.stat_var.qr_data_string = data
                        self.stat_var.qr_data_prev_string = self.stat_var.qr_data_string
                        print("data found: ", self.stat_var.qr_data_string)
                else:
                    self.stat_var.qr_data_found_bool = False
                    self.stat_var.qr_data_string = ""
                if self.stat_var.normal_exit_bool:
                    break
                
            cap.release()
        else:
            print("Kamerakép: mutat")
            while True:
                _, img = cap.read()
                data, bbox, _ = detector.detectAndDecode(img)
                if(bbox is not None):
                    for i in range(len(bbox)):
                        cv2.line(img, tuple(bbox[i][0]), tuple(bbox[(i+1) % len(bbox)][0]), color=(255,
                                 0, 255), thickness=2)
                    cv2.putText(img, data, (int(bbox[0][0][0]), int(bbox[0][0][1]) - 10), cv2.FONT_HERSHEY_SIMPLEX,
                                0.5, (0, 255, 0), 2)
                    if data:
                        self.stat_var.qr_data_found_bool = True
                        print("QR tartalma: " + self.stat_var.qr_data_string + " " + self.stat_var.qr_data_prev_string)
                        if self.stat_var.qr_data_prev_string != self.stat_var.qr_data_string:
                            self.stat_var.qr_data_string = data
                            #print("QR tartalma: " + self.stat_var.qr_data_string + " " + self.stat_var.qr_data_prev_string)
                            self.stat_var.qr_data_prev_string = self.stat_var.qr_data_string
                    else:
                        self.stat_var.qr_data_found_bool = False
                        self.stat_var.qr_data_string = ""
                cv2.imshow("code detector", img)
                if self.stat_var.normal_exit_bool or cv2.waitKey(1) == ord("q"):
                    break
            cap.release()
            cv2.destroyAllWindows()

        print("Kamera kezelő szál leáll!")
        return