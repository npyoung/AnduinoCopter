#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>

AndroidAccessory usb("Google", "AnduinoCopter", "ArduinoMegaADK", "1.0", 
"jhu.edu", "1");

int timer=0;

void setup(){
  Serial.begin(9600);
  usb.powerOn();
  Serial.println("asdf");
}

void loop(){
  if(millis()-timer>500) {
    if(usb.isConnected()) {
      usb.beginTransmission();
      usb.write(23);
      usb.endTransmission();
    }
    timer=millis();
  }
}
