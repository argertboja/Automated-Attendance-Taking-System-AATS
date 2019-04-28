#!/usr/bin/python2.7

import sys
import pyximport
pyximport.install(language_level=2)
import openface
import itertools
import os
import dlib
import numpy as np
np.set_printoptions(precision=2)

fileDir = os.path.dirname('/home/pi/Desktop/openface/demos/')
modelDir = os.path.join(fileDir, '..', 'models')
dlibModelDir = os.path.join(modelDir, 'dlib')
openfaceModelDir = os.path.join(modelDir, 'openface')

dlibFacePredictor = os.path.join(dlibModelDir, "shape_predictor_68_face_landmarks.dat")
networkModel = os.path.join(openfaceModelDir, 'nn4.small2.v1.ascii.t7')

imgDim = 96

# This calculates the faces and returns only the representations
def getRepOfRects(listOfRect, img):
    align = openface.AlignDlib(dlibFacePredictor)
    res = list()
    with openface.TorchNeuralNet(networkModel,imgDim) as net:
        for rect in listOfRect:
            alignedFace = align.align(imgDim, img, rect,landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
            rep = net.forward(alignedFace)
            res.append(rep)
    
    return res
# This calculates the faces and returns the faces with their actual location - used for demos and testing only
def getRepOfRects_rect(listOfRect, img):
    align = openface.AlignDlib(dlibFacePredictor)
    res = list()
    with openface.TorchNeuralNet(networkModel,imgDim) as net:
        for rect in listOfRect:
            alignedFace = align.align(imgDim, img, rect,landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
            rep = net.forward(alignedFace)
            res.append([rep,rect])
    
    return res
    
# Returns the representation of only one face
def getRepOfRect_rect(rect, img):
    align = openface.AlignDlib(dlibFacePredictor)
        
    with openface.TorchNeuralNet(networkModel,imgDim) as net:       
        alignedFace = align.align(imgDim, img, rect,landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
        rep = net.forward(alignedFace)
        
        return rep


