## Heart-Rate-Sensor App:
customized design pattern to read heart-rate sensor specially made for IS.SW1.


##  Analyze & Inspection

>🛠️ Install ADB Wi-Fi to ease debugging of the IS.SW1 sensors without the need for a dock station that often disconnects upon movement. [Check it out here!](https://plugins.jetbrains.com/plugin/14969-adb-wi-fi)

Debug steps are taked to observe sensor behaviour and the Apps reponses. the App always respond to any sensor data comes from sensor-device [check step 2.]


## step 1. Enable debuggable-mode:

1. Press the **Power-Button**, scroll down, and choose **Settings**
2. Scroll down to **About Device**
3. Locate **SN No.** and press it 10 times continuously
4. Press the **Back-Button**
5. Press and hold the **Settings** label until "TestTools" opens, then activate the toggle for **Debuggable**



## step 2. Monitor sensor data of Heart Rate

1. ```adb shell```
2. ``` getevent /dev/input/event3 ```
3. now you open the HR sensor on the Terminal inspecting HR data from system.

 ----

## 🖼️ Screenshots

<img src="/demo/gif_demo_1.gif" width="250"/> 
