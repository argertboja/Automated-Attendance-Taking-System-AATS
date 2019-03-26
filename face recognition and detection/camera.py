from picamera import PiCamera
from time import sleep
import cv2
from picamera.array import PiRGBArray


def capture():
	camera = PiCamera()
	rawCapture = PiRGBArray(camera)
	camera.capture(rawCapture, format="bgr")
	image = rawCapture.array
	camera.close()
	return image

