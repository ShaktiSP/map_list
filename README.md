✅ Goal:
User types in an EditText

Google Places autocomplete shows suggestions below the EditText

Selecting a suggestion gets full place info (like name, coordinates)

Optional: Open Google Map (can be added)

✅ Step-by-Step Full Code (Google Places Autocomplete)
🔹 1. Enable Required APIs
Make sure the following are enabled in Google Cloud Console:

Places API

Maps SDK for Android
🔹 2. Add Dependencies in build.gradle
groovy
Copy
Edit
// Google Maps and Places SDK
implementation 'com.google.android.libraries.places:places:3.3.0' // latest as of 2025

🧪 Example Output
User types "New"

Dropdown shows:

"New York, NY, USA"

"Newark, NJ, USA"

On tap: Fetches full details like lat/lng, name, and address

✅ You're Done!
🎉 This is a fully working Google Places autocomplete integrated in your Android app.
