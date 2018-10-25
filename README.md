# Krot


~~It is named Java-RO, but actually it is kotlin based, so whatever, jvm family.~~

It is named Krot, first word of kotlin & realme.one tech

## Install

Coming soon

## Build & Assemble

run ./gradlew assemble

## Tech Stack
* Java 1.8+
* Kotlin 1.2.71+
* Guava 26.0-jre
* rocksdb 5.15.10
* protobuf-java 3.6.1
* nettty 4.1.30
* junit 5.3.1

## Why not all in Kotlin?
Because some heavy compute in Java are faster than Kotlin, like Hashing, Bytes Ops.
so if there is a jar lib had such methods, we can do these in kotlin, else we use Java to implements.

I am very expect on native kotlin, when it release, we can use pure Kotlin to implements all.

## cmd

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
* [] add conf feature
* [] add protocol message structure
* [] add store blocks feature
* [] add p2p discover feature
* [] add consensus alg DPOS feature 
* [] ...