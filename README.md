# Cardify Projects

This repository contains multiple independent Android projects.

## Cardify-FeatureNER
Provides AI-powered card creation and OCR/NLP features. Build with:
```bash
./gradlew assembleDebug
```

## Cardify-LoginBase
Base implementation with login and basic card management.
Build with:
```bash
./gradlew assembleDebug
```

## new
Experimental module used for early development.

Choose the project directory that matches the functionality you want to test and run the Gradle build from there.

### Offline login
Both projects include a fallback so you can sign in without the server.
If the network request fails, use one of these test accounts:

```
test@example.com / password123
admin / admin
```

### Highlights
- Newly created cards appear at the top of the card book and are highlighted.
- Camera permissions are configured so you can capture business cards without crashes.
