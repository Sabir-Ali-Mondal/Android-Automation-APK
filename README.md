
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

EXAMPLE:
TASK: "Open YouTube and search for 'lofi music'"
{
  "steps": [
    { "action": "tap", "x": 540, "y": 1800, "delay": 1000 },
    { "action": "type", "text": "YouTube", "delay": 1500 },
    { "action": "tap", "x": 500, "y": 450, "delay": 3000 },
    { "action": "tap", "x": 300, "y": 150, "delay": 500 },
    { "action": "type", "text": "lofi music", "delay": 1000 }
  ]
}
```

Working proto ✅

1. **Prompt 1 → Dataset Builder (from screenshots like you shared)**
2. **Prompt 2 → Automation Task JSON Generator (uses dataset for better accuracy)**

---

## 🔹 Prompt 1: Dataset Builder (from screenshots)

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

---

## 🔹 Prompt 2: Automation Task JSON Generator (using dataset)

```
You are an automation task designer for an Android automation app.

INPUTS:
1. Dataset JSON of the device UI (generated separately).
2. Task description: "{TASK_DESCRIPTION}"

GOAL:
Output a JSON automation flow to complete the task with accurate taps/swipes.

RULES:
1. Output ONLY valid JSON.
2. Actions allowed: "tap", "swipe", "type", "wait".
3. Fields:
   - tap: { "action": "tap", "x": <int>, "y": <int>, "delay": <int> }
   - swipe: { "action": "swipe", "startX": <int>, "startY": <int>, "endX": <int>, "endY": <int>, "delay": <int> }
   - type: { "action": "type", "text": "<string>", "delay": <int> }
   - wait: { "action": "wait", "delay": <int> }
4. Always begin by returning to the home screen (swipe up if gesture navigation is enabled).
5. For app launching, use the app drawer search bar from the dataset.
6. Use the fewest steps possible.
7. Delay must be at least 3000ms between steps (slow and safe).
```

---

✅ With this separation:

* **Prompt 1** → you feed screenshots → get dataset JSON.
* **Prompt 2** → you feed dataset JSON + task → get automation steps.


