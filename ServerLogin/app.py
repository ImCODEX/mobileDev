import numpy as np
from flask import Flask, request
from keras.models import load_model
from keras.utils import load_img, img_to_array

app = Flask(__name__)

dic = {0: 'Razvan', 1: 'Cristi', 2: 'Ioana'}

model = load_model('resnet_model_3.h5')

model.make_predict_function()


def predict_label(img_path):
    i = load_img(img_path, target_size=(32, 32))
    i = img_to_array(i) / 255.0
    i = i.reshape(1, 32, 32, 3)
    predict_x = model.predict(i)
    classes_x = np.argmax(predict_x, axis=1)
    return classes_x[0]


@app.route('/login', methods=['POST'])
def login():
    if 'image' not in request.files:
        return 'No file part', 400
    image = request.files['image']
    img_path = "static/" + image.filename
    image.save(img_path)
    label = predict_label(img_path)
    return str(label), 200


if __name__ == '__main__':
    app.run()
