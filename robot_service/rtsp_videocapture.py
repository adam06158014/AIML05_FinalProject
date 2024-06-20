import os
import cv2
import time
import threading
import queue
import numpy as np
import tensorflow as tf
import cv2, queue, threading, time

# bufferless VideoCapture
class VideoCapture:
    def __init__(self, name):
        self.cap = cv2.VideoCapture(name)
        self.q = queue.Queue()
        self.lock = threading.Lock()
        self.running = True  # Flag to indicate if the thread should keep running
        self.t = threading.Thread(target=self._reader)
        self.t.daemon = True
        self.t.start()

    def _reader(self):
        while self.running:
            ret, frame = self.cap.read()
            if not ret:
                break
            if not self.q.empty():
                try:
                    self.q.get_nowait()   # Discard previous frame
                except queue.Empty:
                    pass

            self.q.put(frame)
            self.state=ret

    def read(self):
        return self.q.get(),self.state

    def stop(self):
        self.running = False
        self.t.join()  # Wait for the thread to exit


def read_label_list():
    current_script_path = os.path.abspath(__file__)
    current_script_directory = os.path.dirname(current_script_path)

    print(current_script_directory)
    labels_path = '/ImageNetLabels.txt'

    with open(labels_path) as file:
        lines = file.read().splitlines()
    print(lines)

    return np.array(lines)



def main():
    vs = VideoCapture("rtsp://192.168.93.132:8554/unicast")
    start_time2 = time.time()
    frame_rate = 35
    fps = 0
    # cv2.namedWindow("Live Stream", cv2.WINDOW_NORMAL)
    model = tf.keras.applications.InceptionV3(include_top=True, weights='imagenet')
    imagenet_labels = read_label_list()

    
    while True:
        frame,success = vs.read()
        start_time = time.time()
        if not success:
            break

        resized_frame = frame.copy()
        resized_frame = cv2.resize(resized_frame, (299, 299))
        frame_rgb = cv2.cvtColor(resized_frame, cv2.COLOR_BGR2RGB)
        frame_tensor = tf.convert_to_tensor(frame_rgb, dtype=tf.float32)
        frame_tensor = frame_tensor / 255.0
        frame_tensor= tf.expand_dims(frame_tensor, 0)
        
        
        preds = model.predict(frame_tensor)
        index = np.argmax(preds)

        predict_result = imagenet_labels[index+1]
        print("Predicted:", predict_result)



        loop_time = time.time() - start_time
        delay = max(1, int((1 / frame_rate - loop_time) * 1000))
        key = cv2.waitKey(delay) & 0xFF

        if key == ord('q'):
            break

        loop_time2 = time.time() - start_time
        if loop_time2 > 0:
            fps = 0.9 * fps + 0.1 / loop_time2
            print(fps)

        cv2.putText(frame, f"FPS: {fps:.2f} / {predict_result}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2, cv2.LINE_AA)
        cv2.imshow("Live Stream", frame)
    
    total_time = time.time() - start_time2
    print("Total time taken:", total_time, "seconds")

    cv2.destroyAllWindows()
    vs.cap.release()

# if __name__ == "__main__":
#     main()