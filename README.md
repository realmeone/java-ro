# Java-RO

It is named Java-RO, but actually it is kotlin based, so whatever, jvm family.

It is a blockchain product. follow my interest.OK, not just interest.
RO is short name of realme.one project, it is for identity. 

## Install

Coming soon

## Why not all in Kotlin?
Because some heavy compute in Java are faster than Kotlin, like Hashing, Signature.
so if there is a jar lib had such methods, we can do these in kotlin, else we use Java to implements.

I am very expect on native kotlin release, at that time, we can use Kotlin to implements.

## cmd

### address
```shell
$ ro address <command> [options...] [arguments...]
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