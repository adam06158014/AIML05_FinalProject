import cv2
import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf

def read_img(img_path, resize=(299,299)):
    img_string = tf.io.read_file(img_path)  # 讀取檔案
    img_decode = tf.image.decode_image(img_string)  # 將檔案以影像格式來解碼
    img_decode = tf.image.resize(img_decode, resize)  # 將影像resize到網路輸入大小
    img_decode = img_decode / 255.0  # 對影像做正規畫，將數值縮放到0~1之間
    # 將影像格式增加到4維(batch, height, width, channels)，模型預測要求格式
    img_decode = tf.expand_dims(img_decode, axis=0)  # 
    return img_decode

labels_path = tf.keras.utils.get_file('ImageNetLabels.txt', 'https://storage.googleapis.com/download.tensorflow.org/data/ImageNetLabels.txt')
# 讀取標籤檔中的數據
with open(labels_path) as file:
    lines = file.read().splitlines()
print(lines)  # 顯示讀取的標籤

imagenet_labels = np.array(lines)


model = tf.keras.applications.InceptionV3(include_top=True, weights='imagenet')
# Replace with your RTSP stream URL
rtsp_url = 'rtsp://your_rtsp_server/stream'

# Step 1: Connect to the RTSP server
cap = cv2.VideoCapture(0)

if not cap.isOpened():
    print("Error: Could not open RTSP stream.")
else:
    while True:
        # Step 2: Read frames from the stream
        ret, frame = cap.read()

        if not ret:
            print("Error: Could not read frame.")
            break

        resized_frame = cv2.resize(frame, (299, 299))

        # Convert the frame to RGB format (OpenCV uses BGR by default)
        frame_rgb = cv2.cvtColor(resized_frame, cv2.COLOR_BGR2RGB)

        # Step 3: Convert the frame to a TensorFlow tensor
        frame_tensor = tf.convert_to_tensor(frame_rgb, dtype=tf.float32)

        # Optional: Normalize the tensor (if needed)
        frame_tensor = frame_tensor / 255.0

        frame_tensor= tf.expand_dims(frame_tensor, 0)

        # Print the tensor type and shape for verification
        print(type(frame_tensor))
        print(frame_tensor.shape)


        preds = model.predict(frame_tensor)  # 預測圖片
        index = np.argmax(preds)  # 取得預測結果最大的Index
        print("Predicted:", imagenet_labels[index+1]) 


        # Display the frame (for testing purposes)
        cv2.imshow('RTSP Stream', resized_frame)

        # Break the loop on 'q' key press
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Release the capture object and close any OpenCV windows
    cap.release()
    cv2.destroyAllWindows()
