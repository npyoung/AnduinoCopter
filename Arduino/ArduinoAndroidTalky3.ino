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
  byte msg[1];
  if(usb.isConnected()) {
    int len = usb.read(msg, sizeof(msg), 1);
    if(len>0) {
      Serial.println(len, DEC);
      if(msg[0] == 1) {
        Serial.println("got 1");
        digitalWrite(13, HIGH);
      } else {
        Serial.println("not 1");
        digitalWrite(13, LOW);
      }
    }
  } else {
    digitalWrite(13, LOW);
  }
}
