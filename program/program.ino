#include<BluetoothSerial.h>
#include<Adafruit_AHTX0.h>

BluetoothSerial serialBT;

// Voltage Sensor
const int voltagePin = 34;
// Sonic wave sensor
const int trigPin = 19;
const int echoPin = 18;
// Motion sensor
const int motionPin = 23;
// Temperature Sensor
// const int SDAPin = 21;
// const int SCLPin = 22;
Adafruit_AHTX0 aht;
Adafruit_Sensor *aht_humi, *aht_temp;
// Current Sensor
const int currentPin = 34;
// Flame Sensor
const int flamePin = 32;
// Gas Sensor
const int gasPin = 33;

void setup() {
  Serial.begin(115200); // Initialize serial communication
  analogReadResolution(12); // Set ADC resolution to 12 bits (0-4095)
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(currentPin, INPUT);
  pinMode(flamePin, INPUT);
  pinMode(gasPin, INPUT);
  pinMode(motionPin, INPUT);

  aht.begin();
  serialBT.begin("ESP32"); // Bluetooth device name
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
    String strNumber = String(voltage);
    serialBT.println(strNumber);
  }
  delay(500);
}

void ultra_sonic_sensor() {
// Sonic wave sensor
  long duration, distance;
  long averange = 0;
  
  // Trigger pulse for 10 microseconds to start the ranging
  for(int i(0); i < 6; ++ i) {
    digitalWrite(trigPin, LOW);
    delayMicroseconds(4);
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin, LOW);
    
    // Measure the duration of the pulse from the sensor
    duration = pulseIn(echoPin, HIGH);
    
    // Calculate the distance in centimeters (assumes speed of sound as 34300 cm/s)
    distance = duration / 29 / 2; // Divide by 2 for return trip and by 29.1 (microseconds to cm)
    
    averange += distance;

    delay(100);
  }

  averange /= 5;

  Serial.print("Distance: ");
  Serial.print(averange);
  Serial.println(" cm");
  String strNumber = String(averange);
  serialBT.println(strNumber);
  
}

void current_sensor() {
  // Current Sensor
  int currentValue = analogRead(currentPin);

  float sensitivity = 100; 
  
  float voltageA = currentValue * (3.3 / 4096.0);
  
  // Calculate current in Amperes
  float current = (voltageA - 1.515) / sensitivity; // Subtract 2.5V (midpoint) and divide by sensitivity

  Serial.print("Current: ");
  Serial.print(currentValue, 3); // Print current with 3 decimal places for precision
  Serial.println(" A");
  String strNumber = String(current);
  serialBT.println(strNumber);
  
  delay(500);
}

void flame_sensor() {
// Flame Sensor
  int threshold = 500;
  int flameValue = analogRead(flamePin);

  Serial.print("Sensor Value: ");
  Serial.println(flameValue);

  if (flameValue <= threshold) {
    Serial.println("Flame detected!");
    serialBT.println("Flame detected!");
  } else {
    Serial.println("No flame detected");
    serialBT.println("No Flame detected!");
  }

  delay(1000);
}

void gas_sensor() {
// Gas Sensor
  int gasValue = analogRead(gasPin);

  Serial.println(gasValue);
  
  String strNumber = String(gasValue);
  // serialBT.println(strNumber);
  
  // Smoke detection logic
  if (gasValue >= 550) {
    Serial.println("Alcohol detected!");
    serialBT.println("Alcohol detected!");
    // Add action for smoke detection
  }

  // Gas danger logic (adjust threshold based on your requirements)
  else if (gasValue >= 120) {
    Serial.println("Gas detected!");
    serialBT.println("Gas detected!");
    // Add action for dangerous gas detection
  }

  else {
    Serial.println("No Smoke detected!");
    serialBT.println("No Smoke detected!");
  }

  // Alcohol detection logic (adjust threshold based on your requirements)
  if (gasValue > 900) {
    // Add action for alcohol detection
  }
  delay(500);
}

void temperature_humid_sensor() {
  // put your main code here, to run repeatedly:
  aht_temp = aht.getTemperatureSensor();
  // aht_temp->printSensorDetails();

  aht_humi = aht.getHumiditySensor();
  // aht_humi->printSensorDetails();

  sensors_event_t humidity;
  sensors_event_t temp;
  aht_humi->getEvent(&humidity);
  aht_temp->getEvent(&temp);

  serialBT.println(String(temp.temperature) + " " + String(humidity.relative_humidity));

  delay(2000); // Delay for 2 seconds before reading again
}

void motion_sensor() {
  int motionDetected = digitalRead(motionPin);

  if (motionDetected == HIGH) {
    Serial.println("Motion detected!");
    serialBT.println("Motion detected!");
    // You can add your desired actions here when motion is detected
  } else {
    serialBT.println("No Motion detected!");
    Serial.println("No motion detected");
  }

  delay(1000);
}

int cmd = -1;

void loop() {

  if(serialBT.available()) {
    cmd = serialBT.read();
    Serial.println(cmd);
  }

  if(cmd == '1') {
    voltage_sensor();
  }
  else if(cmd == '2') {
    current_sensor();
  }
  else if(cmd == '3') {
    flame_sensor();
  }
  else if(cmd == '4') {
    temperature_humid_sensor();
  }
  else if(cmd == '5') {
    motion_sensor();
  }
  else if(cmd == '6') {
    ultra_sonic_sensor();
  } 
  else if(cmd == '7') {
    gas_sensor();
  }
}
