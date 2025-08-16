package com.example.automationapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    
    private EditText jsonInputEditText;
    private Button runAutomationButton;
    private Button enableAccessibilityButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        jsonInputEditText = findViewById(R.id.json_input_edittext);
        runAutomationButton = findViewById(R.id.run_automation_button);
        enableAccessibilityButton = findViewById(R.id.enable_accessibility_button);
        
        // Set click listeners
        runAutomationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAutomation();
            }
        });
        
        enableAccessibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilitySettings();
            }
        });
        
        // Check if accessibility service is enabled
        updateButtonStates();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updateButtonStates();
    }
    
    private void updateButtonStates() {
        boolean isAccessibilityEnabled = isAccessibilityServiceEnabled();
        runAutomationButton.setEnabled(isAccessibilityEnabled);
        enableAccessibilityButton.setEnabled(!isAccessibilityEnabled);
        
        if (isAccessibilityEnabled) {
            runAutomationButton.setText("Run Automation");
            enableAccessibilityButton.setText("Accessibility Enabled ✓");
        } else {
            runAutomationButton.setText("Enable Accessibility First");
            enableAccessibilityButton.setText("Enable Accessibility Service");
        }
    }
    
    private boolean isAccessibilityServiceEnabled() {
        String accessibilityService = getPackageName() + "/" + AutomationAccessibilityService.class.getCanonicalName();
        String enabledServices = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return enabledServices != null && enabledServices.contains(accessibilityService);
    }
    
    private void openAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }
    
    private void runAutomation() {
        String jsonInput = jsonInputEditText.getText().toString().trim();
        
        if (jsonInput.isEmpty()) {
            Toast.makeText(this, "Please enter JSON automation steps", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!isAccessibilityServiceEnabled()) {
            Toast.makeText(this, "Please enable accessibility service first", Toast.LENGTH_LONG).show();
            openAccessibilitySettings();
            return;
        }
        
        // Send JSON to accessibility service
        Intent intent = new Intent(this, AutomationAccessibilityService.class);
        intent.setAction("RUN_AUTOMATION");
        intent.putExtra("json_data", jsonInput);
        startService(intent);
        
        Toast.makeText(this, "Starting automation...", Toast.LENGTH_SHORT).show();
    }
}
