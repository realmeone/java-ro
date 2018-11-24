# Krot

It is named Krot, first word of kotlin & realme.one tech

## We use

| Component | Version | Description |
|:------------|:-------------|:-------------|
| Java | 1.8+| Oracle Java |
| Kotlin | 1.3.10+ | |
| Gradle | 4.10.2+ | |
| Guava | 27.0-jre | |
| Rocksdb | 5.15.10 | |
| Netty | 4.1.30 | |
| JUnit | 5.3.1 | |

## Building from source

check out the master branch

```git
git clone https://github.com/realmeone/krot.git
```

Get into the project root folder
 
```shell
./gradlew clean build
```

## Cmd

if run with no args, the default is running node cmd

### node
```shell
$ krot node <command> [options...] [arguments...]
```
```shell
COMMANDS:
     full    Running full node
     light   Running light node
```

### address
```shell
$ krot address <command> [options...] [arguments...]
```
```shell
COMMANDS:
     list    Print summary of existing addresses
     create  Create a new address
```

## Todo
* [ ] add conf feature
* [ ] add store blocks feature
* [ ] add p2p discover feature
* [ ] add consensus alg DPOS feature 
* [ ] ...

## Docs

See the wiki

## Contribution

We welcome everyone to contribute to this project, not just the code, but also the documentation, ideas, and my English (lol)

No matter what you help us improve is a sentence, a few lines of code, or an article, you will receive our reward (ROT) once adopted.