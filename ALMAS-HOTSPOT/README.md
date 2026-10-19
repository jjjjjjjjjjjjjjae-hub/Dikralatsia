# ALMAS Hotspot

Android Kotlin project targeting Xiaomi Mi A3 with Magisk root.

## Goals
- Custom hotspot dashboard
- Root permission check
- Network diagnostics (cellular, tethering, DNS, MTU)
- VPN integration roadmap for WireGuard and tethered clients
- Smart screen-off continuity
- User-visible fail-safe and rollback options

## Important limitations
VPN encrypts traffic but cannot reliably conceal tethering from Activ, bypass data caps, or remove carrier throttling. Device-specific tethering and VPN routing require testing.

## Next milestone
Create Android Studio project with Kotlin UI, root check, status monitoring and an explicit-permission setup flow.
