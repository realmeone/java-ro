# Krot


~~It is named Java-RO, but actually it is kotlin based, so whatever, jvm family.~~

It is named Krot, first word of kotlin & realme.one tech

## Install

todo

## Build & Assemble

Run 
```shell
./gradlew assemble
```

## Tech Stack
* Java 1.8+
* Kotlin 1.3.0+
* Guava 26.0-jre
* Rocksdb 5.15.10
* Protobuf-java 3.6.1
* Netty 4.1.30
* Junit 5.3.1
* Gradle 4.10.2+

## Why not all in Kotlin?
Because some heavy compute in Java are faster than Kotlin, like Hashing, Bytes Ops.
so if there is a jar lib had such methods, we can do these in kotlin, else we use Java to implements.

I am very expect on native kotlin, when it release, we can use pure Kotlin to implements all.

## Cmd

### address
```shell
$ krot address <command> [options...] [arguments...]
```
```shell
COMMANDS:
     list    Print summary of existing addresses
     create  Create a new address
     update  Update an existing address
     import  Import a private key
```
## Todo
* [ ] add conf feature
* [ ] add store blocks feature
* [ ] add p2p discover feature
* [ ] add consensus alg DPOS feature 
* [ ] ...

## Docs

See the wiki