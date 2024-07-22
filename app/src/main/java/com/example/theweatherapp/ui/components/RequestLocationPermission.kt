package com.example.theweatherapp.ui.components

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit,
) {
    val permissionState =
        rememberPermissionState(
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

    LaunchedEffect(key1 = permissionState) {
        val isPermissionRevoked =
            !permissionState.status.isGranted && !permissionState.status.shouldShowRationale

        val isPermissionGranted =
            permissionState.status.isGranted

        if (!isPermissionGranted) permissionState.launchPermissionRequest()

        if (isPermissionRevoked) {
            onPermissionsRevoked()
        } else if (isPermissionGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }
}
