# NutriSport - Kotlin Multiplatform Project (KMP)

Repository ini merupakan proyek **Kotlin Multiplatform (KMP)** yang mengintegrasikan Android & iOS dengan **Firebase** dan **Google Sign-In**.

---

## Tech Stack

- Kotlin Multiplatform (KMP)
- Kotlin (Android & Shared)
- Swift (iOS)
- Navigation (Jetpack Navigation / Navigation Compose)
- Koin (Dependency Injection)
- Coil 3 (Image Loading)
- Ktor (Networking)
- Kotlin Coroutines (Asynchronous Programming)
- Firebase:
  - Authentication (Google Sign-In)
  - Firestore
  - Storage

---

## Development Environment

- Android Studio Panda 2025.3.2
- Menggunakan Module Marker di Android Studio untuk membuat module KMP

---

## Clone Project

```bash
git clone https://github.com/fahmi54321/nutrisport.git
cd nutrisport
````

---

## Setup Firebase

### 1. Buat Project di Firebase Console

* Buka Firebase Console
* Klik **Add Project**
* Ikuti langkah sampai selesai

---

### 2. Firestore Setup

Aktifkan Firestore Database, lalu gunakan rule berikut (untuk development):

```js
allow read, write: if true;
```

---

### 3. Authentication Setup

* Aktifkan **Google Sign-In**
* Untuk development, bisa gunakan rule:

```js
allow read, write: if true;
```

---

### 4. Firebase Storage

* Aktifkan Firebase Storage
* Gunakan rule sederhana saat development:

```js
allow read, write: if true;
```

---

## Setup iOS (jika build gagal)

Jika iOS error seperti:

* `No such module FirebaseCore`
* `No such module GoogleSignIn`

Ikuti langkah berikut:

---

### 1. Setup di Shared Module

📁 `shared/build.gradle.kts`

Tambahkan plugin:

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    kotlin("native.cocoapods")
}
```

---

### 2. Tambahkan Cocoapods Dependency

```kotlin
cocoapods {
    summary = "Shared module"
    homepage = "https://example.com"
    version = "1.0.0"

    ios.deploymentTarget = "14.0"

    pod("FirebaseCore")
    pod("FirebaseAuth")
    pod("FirebaseFirestore")
    pod("FirebaseStorage")
    pod("GoogleSignIn")
}
```

---

### 3. Setup CocoaPods di iOS App

📁 Folder: `iosApp/`

Buat file **Podfile**:

```ruby
platform :ios, '14.0'

target 'iosApp' do
  use_frameworks!

  pod 'shared', :path => '../shared'
end
```

---

### 4. Install Pods

```bash
cd iosApp
pod install
```

---

### 5. Buka Project iOS

Gunakan workspace (bukan `.xcodeproj`):

```bash
iosApp.xcworkspace
```

---

### 6. Setup Firebase di iOS

Ikuti dokumentasi:
[https://github.com/mirzemehdi/KMPAuth](https://github.com/mirzemehdi/KMPAuth)

---

## Notes

* Pastikan **GoogleService-Info.plist** sudah ditambahkan ke iOS project
* Pastikan **google-services.json** sudah ada di Android
* Gunakan workspace (`.xcworkspace`) agar CocoaPods terbaca
* Rule Firebase di atas hanya untuk development (jangan dipakai di production)

---