[![Build Status](https://travis-ci.org/demarkok/The-ants-are-my-friends.svg?branch=master)](https://travis-ci.org/demarkok/The-ants-are-my-friends)
# The ants are my friends
Game for android (educational project)

# Demo video

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/DGIbSgFiTdk/0.jpg)](https://www.youtube.com/watch?v=DGIbSgFiTdk)

## What I use
* [libgdx](https://github.com/libgdx/libgdx)
* [gdxAI](https://github.com/libgdx/gdx-ai)

## Build

* Download [gradle](https://gradle.org/gradle-download/)

* Setup gradle. Firstly add the environment variable `GRADLE_HOME`. This should point to the unpacked files from the Gradle website. Next add `GRADLE_HOME/bin` to your `PATH` environment variable.

* Don't forget to define your SDK location with `ANDROID_HOME` environment variable or in the `local.properties` file.

* `gradle build`

* The `.apk` file will be in `./android/build/outputs/apk/`
