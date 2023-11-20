#include<DallasTemperature.h>
#include<OneWire.h>
#include<BluetoothSerial.h>

BluetoothSerial serialBT;

// Voltage Sensor
const int voltagePin = 34;
// Sonic wave sensor
const int trigPin = 19;
const int echoPin = 18;
// Vibration sensor
const int vibraPin = 17;
// Temperature Sensor
const int tempPin = 16;
OneWire oneWire(tempPin);
DallasTemperature tempSensor(&oneWire);
// Current Sensor
const int currentPin = 15;
// Flame Sensor
const int flamePin = 23;
// Gas Sensor
const int gasPin = 21;

void setup() {
  Serial.begin(115200); // Initialize serial communication
  analogReadResolution(12); // Set ADC resolution to 12 bits (0-4095)
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(currentPin, INPUT);
  pinMode(vibraPin, INPUT);
  pinMode(flamePin, INPUT);
  pinMode(gasPin, INPUT);

  serialBT.begin("ESP32"); // Bluetooth device name

  tempSensor.begin();
}

void voltage_sensor() {
// Voltage sensor
  int voltageValue = analogRead(voltagePin); // Read the analog value from the sensor
  float voltage = map(voltageValue, 0, 1023, 0, 2500) + 20; // Convert ADC value to voltage (assuming 3.3V reference voltage and 12-bit resolution)
  voltage /= 500;

  if(voltageValue > 0) {
    Serial.print("Sensor Value: ");
    Serial.print(voltageValue);
    Serial.print(", Voltage: "); 
    Serial.print(voltage, 2);
    Serial.println("V");
  }
}

void ultra_sonic_sensor() {
// Sonic wave sensor
  long duration, distance;
  
  // Trigger pulse for 10 microseconds to start the ranging
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  
  // Measure the duration of the pulse from the sensor
  duration = pulseIn(echoPin, HIGH);
  
  // Calculate the distance in centimeters (assumes speed of sound as 34300 cm/s)
  distance = (duration / 2) / 29.1; // Divide by 2 for return trip and by 29.1 (microseconds to cm)
  
  // Output the distance measured
  if(distance <= 400) {
    Serial.print("Distance: ");
    Serial.print(distance);
    Serial.println(" cm");
  }
}

void current_sensor() {
  // Current Sensor
  int currentValue = analogRead(currentPin);

  float sensitivity = 100; 
  
  float voltageA = currentValue * (3.3 / 4096.0);
  
  // Calculate current in Amperes
  float current = (voltageA - 1.515) / sensitivity; // Subtract 2.5V (midpoint) and divide by sensitivity

  if(current > 0.01) {
    Serial.print("Current: ");
    Serial.print(current, 3); // Print current with 3 decimal places for precision
    Serial.println(" A");
  }
}

void flame_sensor() {
// Flame Sensor
  int flameValue = digitalRead(flamePin);

  if (flameValue == HIGH) {
    Serial.println("FLAME DETECTED!");
  }
}

void gas_sensor() {
// Gas Sensor
  int gasValue = analogRead(gasPin);

  Serial.println(gasValue);
  // // Smoke detection logic
  // if (gasValue > 600) {
  //   Serial.println("Smoke detected!");
  //   // Add action for smoke detection
  // }

  // // Gas danger logic (adjust threshold based on your requirements)
  // if (gasValue > 800) {
  //   Serial.println("Dangerous gas level detected!");
  //   // Add action for dangerous gas detection
  // }

  // // Alcohol detection logic (adjust threshold based on your requirements)
  // if (gasValue > 900) {
  //   Serial.println("Alcohol detected!");
  //   // Add action for alcohol detection
  // }
}

void loop() {

  // while(serialBT.available()) {
  //   int get_data = serialBT.read();

  //   if(get_data ==  1) {
      
  //   }
  // }
  gas_sensor();

  delay(50);
}
