package com.example.automationapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import android.os.Bundle;

public class AutomationAccessibilityService extends AccessibilityService {
    
    private static final String TAG = "AutomationService";
    private Handler mainHandler;
    private boolean isRunning = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mainHandler = new Handler(Looper.getMainLooper());
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "RUN_AUTOMATION".equals(intent.getAction())) {
            String jsonData = intent.getStringExtra("json_data");
            if (jsonData != null && !isRunning) {
                startAutomation(jsonData);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Handle accessibility events if needed
    }
    
    @Override
    public void onInterrupt() {
        // Handle interruption
    }
    
    private void startAutomation(String jsonData) {
        if (isRunning) {
            Log.w(TAG, "Automation already running");
            return;
        }
        
        isRunning = true;
        Log.d(TAG, "Starting automation with JSON: " + jsonData);
        
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray steps = jsonObject.getJSONArray("steps");
            
            executeSteps(steps, 0);
            
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
            showToast("Error parsing JSON: " + e.getMessage());
            isRunning = false;
        }
    }
    
    private void executeSteps(JSONArray steps, int currentIndex) {
        if (currentIndex >= steps.length()) {
            // All steps completed
            showToast("Automation Complete");
            isRunning = false;
            return;
        }
        
        try {
            JSONObject step = steps.getJSONObject(currentIndex);
            String action = step.getString("action");
            int delay = step.optInt("delay", 0);
            
            Log.d(TAG, "Executing step " + (currentIndex + 1) + ": " + action);
            
            switch (action.toLowerCase()) {
                case "tap":
                    executeTap(step, steps, currentIndex, delay);
                    break;
                case "swipe":
                    executeSwipe(step, steps, currentIndex, delay);
                    break;
                case "type":
                    executeType(step, steps, currentIndex, delay);
                    break;
                case "wait":
                    executeWait(delay, steps, currentIndex);
                    break;
                default:
                    Log.w(TAG, "Unknown action: " + action);
                    executeNextStep(steps, currentIndex);
                    break;
            }
            
        } catch (JSONException e) {
            Log.e(TAG, "Error executing step " + currentIndex, e);
            showToast("Error executing step: " + e.getMessage());
            isRunning = false;
        }
    }
    
    private void executeTap(JSONObject step, JSONArray steps, int currentIndex, int delay) throws JSONException {
        int x = step.getInt("x");
        int y = step.getInt("y");
        
        Path path = new Path();
        path.moveTo(x, y);
        
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 100));
        
        dispatchGesture(builder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                Log.d(TAG, "Tap completed at (" + x + ", " + y + ")");
                if (delay > 0) {
                    mainHandler.postDelayed(() -> executeNextStep(steps, currentIndex), delay);
                } else {
                    executeNextStep(steps, currentIndex);
                }
            }
            
            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                Log.w(TAG, "Tap cancelled");
                executeNextStep(steps, currentIndex);
            }
        }, null);
    }
    
    private void executeSwipe(JSONObject step, JSONArray steps, int currentIndex, int delay) throws JSONException {
        int startX = step.getInt("startX");
        int startY = step.getInt("startY");
        int endX = step.getInt("endX");
        int endY = step.getInt("endY");
        
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
        
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 500));
        
        dispatchGesture(builder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                Log.d(TAG, "Swipe completed from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");
                if (delay > 0) {
                    mainHandler.postDelayed(() -> executeNextStep(steps, currentIndex), delay);
                } else {
                    executeNextStep(steps, currentIndex);
                }
            }
            
            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                Log.w(TAG, "Swipe cancelled");
                executeNextStep(steps, currentIndex);
            }
        }, null);
    }
    
    private void executeType(JSONObject step, JSONArray steps, int currentIndex, int delay) throws JSONException {
        String text = step.getString("text");
        
        // Find focused node and type text
        AccessibilityNodeInfo focusedNode = getRootInActiveWindow().findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (focusedNode != null) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            boolean success = focusedNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            
            if (success) {
                Log.d(TAG, "Text typed: " + text);
            } else {
                Log.w(TAG, "Failed to type text: " + text);
            }
            
            focusedNode.recycle();
        } else {
            Log.w(TAG, "No focused input field found for typing");
        }
        
        if (delay > 0) {
            mainHandler.postDelayed(() -> executeNextStep(steps, currentIndex), delay);
        } else {
            executeNextStep(steps, currentIndex);
        }
    }
    
    private void executeWait(int delay, JSONArray steps, int currentIndex) {
        Log.d(TAG, "Waiting for " + delay + "ms");
        mainHandler.postDelayed(() -> executeNextStep(steps, currentIndex), delay);
    }
    
    private void executeNextStep(JSONArray steps, int currentIndex) {
        mainHandler.post(() -> executeSteps(steps, currentIndex + 1));
    }
    
    private void showToast(String message) {
        mainHandler.post(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }
}
