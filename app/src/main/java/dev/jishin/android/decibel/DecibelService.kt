package dev.jishin.android.decibel

import android.accessibilityservice.AccessibilityButtonController
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import java.util.concurrent.Executor

class DecibelService : AccessibilityService() {

    private var mIsAccessibilityButtonAvailable: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onServiceConnected() {
        Log.d(TAG, "onServiceConnected() called")
        val mAccessibilityButtonController = accessibilityButtonController
        mIsAccessibilityButtonAvailable =
            mAccessibilityButtonController.isAccessibilityButtonAvailable

        //if (!mIsAccessibilityButtonAvailable) return

        /*serviceInfo = serviceInfo.apply {
            flags = flags or AccessibilityServiceInfo.FLAG_REQUEST_ACCESSIBILITY_BUTTON
        }*/

        val accessibilityButtonCallback =
            @RequiresApi(Build.VERSION_CODES.O)
            object : AccessibilityButtonController.AccessibilityButtonCallback() {
                override fun onClicked(controller: AccessibilityButtonController) {
                    Log.d(TAG, "onClicked() called with: controller = $controller")

                    // Add custom logic for a service to react to the
                    // accessibility button being pressed.

                    showVolumeSliders()
                }

                override fun onAvailabilityChanged(
                    controller: AccessibilityButtonController,
                    available: Boolean
                ) {
                    Log.d(
                        TAG,
                        "onAvailabilityChanged() called with: controller = $controller, available = $available"
                    )
                    if (controller == mAccessibilityButtonController) {
                        mIsAccessibilityButtonAvailable = available
                    }
                }
            }

        mAccessibilityButtonController.registerAccessibilityButtonCallback(
            accessibilityButtonCallback
        )
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d(TAG, "onAccessibilityEvent() called with: event = $event")

        // get the source node of the event
        event?.source?.apply {

            // Use the event and node information to determine
            // what action to take


            /*takeScreenshot(0, Executor { },
                @RequiresApi(Build.VERSION_CODES.R)
                object : TakeScreenshotCallback {
                    override fun onSuccess(screenshotResult: ScreenshotResult) {

                    }

                    override fun onFailure(p0: Int) {

                    }
                })*/

            // take action on behalf of the user
            performGlobalAction(GLOBAL_ACTION_TAKE_SCREENSHOT)

            // recycle the nodeInfo object
            recycle()
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt() called")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind() called with: intent = $intent")
        return super.onUnbind(intent)
    }

    companion object {
        private const val TAG = "DecibelService"
    }
}