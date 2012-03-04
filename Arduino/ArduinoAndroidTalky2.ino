#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>

AndroidAccessory usb("Google", "AnduinoCopter", "ArduinoMegaADK", "1.0", 
"jhu.edu", "1");

void setup() {
  Serial.begin(9600);
  usb.powerOn();
  Serial.println("asdf");
}

void loop() {
  if(Serial.available()>0) {
    if(usb.isConnected()) {
      Serial.read();
//      usb.beginTransmission();
//      usb.write(Serial.read());
//      usb.endTransmission();
    }
  }
//  if(usb.isConnected()) {
//    Serial.print(usb.read());
//  }
}
