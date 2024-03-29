# compose-legos
Autogenerated runnable catalogue of compose samples

<img src="https://github.com/saied89/compose-legos/blob/main/app/src/main/ic_launcher-playstore.png" width="256" height="256"/>
<a href='https://play.google.com/store/apps/details?id=home.saied.composesamples'>
    <img alt='Get it on Google Play' 
         src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'
         height="116" width="300"/>
</a>

### How it works
- Android frameworks/support repositpry is added as a git submodule and various Samples directories are added as source directories to a project module.
- Samples are processed using [ksp](https://github.com/google/ksp) and runnable sample code is generated using [kotlinpoet](https://github.com/square/kotlinpoet).
- Generated samples are presented in the application.

### Experimental Idea Plugin
1. Make sure you have a working ADB and only a single connected Device
2. Install Compose Legos from either Play Store or Github Releases on your Device or Emulator
3. Install the [Plugin](https://plugins.jetbrains.com/plugin/20027-compose-legos)
4. There is now a Launch Sample context action when right clicking on `@Sample` annotated lines.
[![Compose Plugin Plugin Demo](https://img.youtube.com/vi/VxfxbK-XESw/maxresdefault.jpg)](https://www.youtube.com/watch?v=VxfxbK-XESw&feature=emb_title "Compose Plugin Plugin Demo")

### Todo
- [x] Add a way to inspect running sample state
- [ ] Add runtime theme modification support
- [ ] Add Analytics
- [ ] Add a way to change samples
- [ ] Add UI tests
