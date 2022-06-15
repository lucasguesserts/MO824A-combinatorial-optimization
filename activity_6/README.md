# Activity 6

## Credits

This project was developed by:

- Ieremies Vieira Da Fonseca Romero
- Adolfo Aires Schneider
- Lucas Guesser Targino da Silva (lucasguesserts@gmail.com)

The Genetic Algorithm framework was initially developed by:

- Fábio Luiz Usberti (fusberti@ic.unicamp.br)
- Celso Cavellucci (celsocv@ic.unicamp.br)

## Requirements

- Java 17
- Gradle 7

If you install [skdman](https://sdkman.io/), you can easily install and set the requirements.

See [this sdk config file](./.sdkmanrc) for more informantion.

## run

```sh
./script/run
```

## Debug in vscode

Put a breakpoint in the file [`GA_QBF.java`](./genetic/src/main/java/problems/qbf/solvers/GA_QBF.java) and then press `F5` to see all the magic happening.

Obs: the JDK configured is specified in the [vscode settings file](./.vscode/settings.json). If it doesn't work for you, try either to set your own java runtime or delete the configurations completely and let vscode find it automatically (but do not commit such changes).
