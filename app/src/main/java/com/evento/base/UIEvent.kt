package com.evento.base

/**
 * Represents a one-time UI action (event) that should be triggered only once.
 *
 * Unlike UI state, which is persistent and survives configuration changes,
 * a UI event is meant for single-shot effects such as:
 *  - Showing a Snackbar / Toast
 *  - Navigation
 *  - Dialog open/close
 *  - Haptic feedback / vibration
 *
 * ViewModel emits UIEvent and the UI layer (Compose/Activity/Fragment)
 * consumes it exactly once and does not render it again on recomposition.
 */
interface UIEvent