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

void usbPrintLn(String toSend, AndroidAccessory usb) {
  for(int i=0; i < toSend.length(); i++) {
    usb.write(toSend[i]);
  }
  usb.write('\n');
}


void loop(){
  if(millis()-timer>100) {
    if(usb.isConnected()) {
      usb.beginTransmission();
      usbPrintLn("Kevin FTW", usb);
      usb.endTransmission();
    }
    timer=millis();
  }
}
