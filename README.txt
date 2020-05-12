-------------------------
      Installation
-------------------------
In the build.gradle for your workspace add these lines under buildscript,
```plugins {
  id "com.wynprice.cursemaven" version "2.1.1"
}```
This will allow the gradle to get files from the curse maven.
In the dependencies add this line
```compile fg.deobf("curse.maven:abnormals-core:fileid")```
fileid - The file id of the project, for example for 1.15.2 Version 1.0.0 of AC the id is 2954634, so you'd do it like so
```compile fg.deobf("curse.maven:abnormals-core:2954634")```

And finally just run the gradle steps as normal and AC will be added as a dependency.
