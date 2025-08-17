
## 📄 Cursor Prompt (APK Generator)

```
You are to generate a complete Android Studio project in Java.  
The app will run automation tasks by reading JSON entered manually by the user.  

---

### FUNCTIONALITY
1. Show a simple UI:
   - Multi-line EditText for JSON input
   - Button: "Run Automation"
2. User pastes JSON in this format:
{
  "steps": [
    { "action": "tap", "x": 500, "y": 1600, "delay": 1000 },
    { "action": "type", "text": "Hello World", "delay": 1500 },
    { "action": "swipe", "startX": 500, "startY": 1500, "endX": 500, "endY": 500, "delay": 2000 },
    { "action": "wait", "delay": 1000 }
  ]
}
3. On clicking "Run Automation":
   - Parse the JSON
   - Execute each step:
     - tap → dispatchGesture() at (x,y)
     - swipe → dispatchGesture() from start→end
     - type → input text into focused field
     - wait → pause for delay
4. When all steps are done, show a Toast: "Automation Complete".

---

### FILES TO GENERATE
**MainActivity.java**
- Contains EditText + Button
- Passes JSON string to AccessibilityService

**AutomationAccessibilityService.java**
- Extends AccessibilityService
- Reads JSON string
- Runs automation steps using dispatchGesture()
- Supports "tap", "swipe", "type", "wait"

**AndroidManifest.xml**
- Declare AccessibilityService with proper config
- Add required permissions:
  - android.permission.BIND_ACCESSIBILITY_SERVICE

**res/layout/activity_main.xml**
- Contains:
  - EditText (multi-line)
  - Button ("Run Automation")

---

### TECHNICAL REQUIREMENTS
- Language: Java
- Minimum SDK: 24
- JSON parsing with org.json
- Use AccessibilityService.dispatchGesture() for tap/swipe
- Use AccessibilityNodeInfo for typing
- Material Design for layout (basic)

---

### DELIVERY
Generate:
- MainActivity.java
- AutomationAccessibilityService.java
- AndroidManifest.xml
- res/layout/activity_main.xml

Ensure it compiles directly in Android Studio.
```

---

👉 If you paste this into Cursor, it will create the **multi-file Android project**.


---

## **Prompt — JSON Structure Creation (Enhanced Version)**

This prompt is for **generating the automation JSON** manually or with AI.

```
You are an automation task designer for an Android automation app.

GOAL:
Given a description of what to do, output a JSON in the following strict format to control taps, swipes, and typing on a phone screen.

DEVICE INFORMATION:
- Device Name: {DEVICE_NAME}
- Screen Resolution: {SCREEN_WIDTH}x{SCREEN_HEIGHT}
- Android Version: {ANDROID_VERSION}
- UI Skin: {UI_SKIN} (e.g., MIUI, OneUI, OxygenOS)

TASK:
"{TASK_DESCRIPTION}"

RULES:
1. Output ONLY valid JSON, no extra text.
2. Actions allowed: "tap", "swipe", "type", "wait".
3. Fields:
   - tap: { "action": "tap", "x": <int>, "y": <int>, "delay": <int> }
   - swipe: { "action": "swipe", "startX": <int>, "startY": <int>, "endX": <int>, "endY": <int>, "delay": <int> }
   - type: { "action": "type", "text": "<string>", "delay": <int> }
   - wait: { "action": "wait", "delay": <int> }
4. Coordinates must match the given resolution.
5. Delay is in milliseconds and applies before the next step.
6. Use the fewest steps possible to complete the task.
7. This device uses **gesture navigation**:
   - To go to home screen → swipe up from bottom-center ({x: SCREEN_WIDTH/2, y: SCREEN_HEIGHT-10} to {x: SCREEN_WIDTH/2, y: SCREEN_HEIGHT/2}).
   - To open the app drawer → swipe up again from mid-screen.
8. Always use the **app drawer search bar** (at {x: SCREEN_WIDTH/2, y: SCREEN_HEIGHT-60}) to launch apps instead of tapping app icons directly.
9. After opening the target app, continue the task steps as described.

EXAMPLE:
TASK: "Open YouTube and search for 'lofi music'"
{
  "steps": [
    { "action": "swipe", "startX": 360, "startY": 1520, "endX": 360, "endY": 760, "delay": 4000 },
    { "action": "swipe", "startX": 360, "startY": 700, "endX": 360, "endY": 200, "delay": 4000 },
    { "action": "tap", "x": 360, "y": 1480, "delay": 4000 },
    { "action": "type", "text": "YouTube", "delay": 5000 },
    { "action": "tap", "x": 200, "y": 280, "delay": 6000 },
    { "action": "tap", "x": 360, "y": 100, "delay": 4000 },
    { "action": "type", "text": "lofi music", "delay": 5000 }
  ]
}

```


## 🔹 Dataset Builder (from screenshots)

```
You are an Android UI dataset builder.

INPUT:
Screenshots of the device home screen and app drawer.

GOAL:
Analyze the screenshots and output a JSON dataset that captures key UI element positions.

OUTPUT FORMAT:
{
  "device": {
    "name": "{DEVICE_NAME}",
    "resolution": "{SCREEN_WIDTH}x{SCREEN_HEIGHT}",
    "android_version": "{ANDROID_VERSION}",
    "ui_skin": "{UI_SKIN}"
  },
  "ui_elements": {
    "home_screen": {
      "dock_y": <int>,
      "home_indicator_y": <int>
    },
    "app_drawer": {
      "search_bar": { "x": <int>, "y": <int>, "width": <int>, "height": <int> },
      "first_app_icon": { "x": <int>, "y": <int> },
      "grid_start_y": <int>,
      "grid_spacing": { "x": <int>, "y": <int> }
    }
  }
}

RULES:
1. Output ONLY valid JSON.
2. Coordinates must match the resolution of the screenshot.
3. Capture the app drawer search bar, dock position, and icon grid details.
```


* **Prompt** → you feed screenshots → get dataset JSON.


