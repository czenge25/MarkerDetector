import os
import shutil
from glob import glob
from pathlib import Path, PurePath

import pandas as pd
import numpy as np

import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import seaborn as sns

from PIL import Image
import cv2

import  xml.dom.minidom as minidom
import xmltodict

from collections import Counter

from tqdm import tqdm

from sklearn.model_selection import train_test_split

ANNOTATIONS = ""
IMAGES = ""

images = glob(IMAGES + "/*")
images = sorted(images)
len(images)

annotations = glob(f"{ANNOTATIONS}/*")
annotations = sorted(annotations)
len(annotations)

first_nine_images_shape = []
rows, cols = 3, 3
plt.figure(figsize=(12, 12))

for ind, img in enumerate(images):
    if ind==9:
        break
    im = Image.open(img)
    first_nine_images_shape.append(im.size)
    plt.subplot(rows, cols, ind+1)
    plt.imshow(im)
    im.close()

def plot_coords(coords, color, label, ax):
    x, y, w, h = coords.values()
    edgecolor = {"marker": "y"}
    mpatch = mpatches.Rectangle(
        (x, y),
        w-x, h-y,
        linewidth=1,
        edgecolor=color,
        facecolor="none")
    ax.add_patch(mpatch)
    rx, ry = mpatch.get_xy()
    ax.annotate(label, (rx, ry),
                color=color, weight='bold',
                fontsize=10, ha='left', va='baseline')

def annotate_image(img, xml_file):
    with open(xml_file) as f:
        doc = xmltodict.parse(f.read())

    image = plt.imread(img)

    fig, ax = plt.subplots(1)
    ax.axis("off")
    fig.set_size_inches(10, 5)

    temp = doc['annotation']['object']

    if isinstance(temp, list):
        for i in range(len(temp)):
            if temp[i]["name"] == "marker":
                plot_coords(temp[i]['bndbox'], 'green', temp[i]["name"],ax)

    else:
        x, y, w, h = list(map(int, temp["bndbox"].values()))
        edgecolor = {"marker": "y"}
        mpatch = mpatches.Rectangle(
            (x, y),
            w-x, h-y,
            linewidth=1,
            edgecolor=edgecolor[temp["name"]],
            facecolor="none")
        ax.add_patch(mpatch)
        rx, ry = mpatch.get_xy()
        ax.annotate(temp["name"], (rx, ry),
                    color=edgecolor[temp["name"]], weight='bold',
                    fontsize=10, ha='left', va='baseline')
    ax.imshow(image)

