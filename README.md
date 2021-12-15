# Box2D Editor - DestSol Edition
### About
A rewrite of the old [Box2D Editor](https://github.com/MovingBlocks/box2d-editor), with this version optimised for use with [Destination Sol](https://destinationsol.org).

This version is written in Kotlin from the ground up using the original as a reference.

Note however that it is currently heavily work in progress and missing critical features, and thus completely useless for anything besides testing.

### Building and Testing
Use `gradlew run` to build and run the software, or use your IDEs facilities.

In the current version which does not include most of the planned UI, use the left mouse button to add and move points, and the right one to remove them.

The software crashing when removing the last point is known and will be fixed once separate mesh management is in place.