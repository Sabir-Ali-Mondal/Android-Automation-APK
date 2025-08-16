# Android Automation App

A complete Android Studio project that allows users to run automation tasks by reading JSON instructions entered manually.

## Features

- **Simple UI**: Multi-line EditText for JSON input and automation control buttons
- **JSON-based Automation**: Execute automation steps defined in JSON format
- **Supported Actions**: tap, swipe, type, and wait operations
- **Accessibility Service**: Uses Android's AccessibilityService for gesture execution
- **Real-time Feedback**: Toast notifications for automation status

## Supported Actions

### 1. Tap
```json
{ "action": "tap", "x": 500, "y": 1600, "delay": 1000 }
```
- `x`, `y`: Screen coordinates to tap
- `delay`: Milliseconds to wait after tap (optional)

### 2. Type
```json
{ "action": "type", "text": "Hello World", "delay": 1500 }
```
- `text`: Text to type into focused input field
- `delay`: Milliseconds to wait after typing (optional)

### 3. Swipe
```json
{ "action": "swipe", "startX": 500, "startY": 1500, "endX": 500, "endY": 500, "delay": 2000 }
```
- `startX`, `startY`: Starting coordinates
- `endX`, `endY`: Ending coordinates
- `delay`: Milliseconds to wait after swipe (optional)

### 4. Wait
```json
{ "action": "wait", "delay": 1000 }
```
- `delay`: Milliseconds to pause execution

## Example JSON

```json
{
  "steps": [
    { "action": "tap", "x": 500, "y": 1600, "delay": 1000 },
    { "action": "type", "text": "Hello World", "delay": 1500 },
    { "action": "swipe", "startX": 500, "startY": 1500, "endX": 500, "endY": 500, "delay": 2000 },
    { "action": "wait", "delay": 1000 }
  ]
}
```

## Setup Instructions

### 1. Open in Android Studio
- Open Android Studio
- Select "Open an existing Android Studio project"
- Navigate to this project folder and select it

### 2. Build and Run
- Wait for Gradle sync to complete
- Connect an Android device or start an emulator
- Click the "Run" button (green play icon)

### 3. Enable Accessibility Service
- On first run, the app will prompt you to enable accessibility
- Click "Enable Accessibility Service"
- In Android Settings, find "Automation App" under Accessibility
- Toggle the service ON
- Return to the app

### 4. Use the App
- Paste your JSON automation steps into the EditText
- Click "Run Automation"
- Watch as the app executes each step sequentially
- A "Automation Complete" toast will appear when finished

## Technical Details

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Language**: Java
- **Architecture**: AccessibilityService-based automation
- **Dependencies**: AndroidX, Material Design components

## Permissions

- `android.permission.BIND_ACCESSIBILITY_SERVICE`: Required for automation functionality

## File Structure

```
app/src/main/
├── java/com/example/automationapp/
│   ├── MainActivity.java              # Main UI and control logic
│   └── AutomationAccessibilityService.java  # Automation execution service
├── res/
│   ├── layout/activity_main.xml       # Main UI layout
│   ├── values/
│   │   ├── strings.xml                # String resources
│   │   └── colors.xml                 # Color resources
│   ├── drawable/
│   │   ├── button_background.xml      # Button styling
│   │   └── edittext_background.xml    # EditText styling
│   └── xml/accessibility_service_config.xml  # Service configuration
└── AndroidManifest.xml                # App manifest and permissions
```

## Troubleshooting

### Accessibility Service Not Working
- Ensure the service is enabled in Android Settings > Accessibility
- Check that the app has the necessary permissions
- Restart the app after enabling the service

### Automation Not Executing
- Verify JSON format is correct
- Check that coordinates are within screen bounds
- Ensure the target app is in the foreground
- Look for error messages in Android Studio Logcat

### Build Errors
- Sync project with Gradle files
- Clean and rebuild project
- Ensure Android SDK is properly configured

## Security Notes

- This app requires accessibility permissions to function
- The accessibility service can read screen content and perform gestures
- Only use with trusted automation scripts
- Be cautious when sharing automation scripts

## License

This project is provided as-is for educational and development purposes.
