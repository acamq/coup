# Coup (Command-Line Java Edition)

This repository contains a self-contained implementation of the bluffing card game **Coup** for the command line. The game follows the standard Coup ruleset (a copy of the official rule book is available in `coup_rulebook.pdf`) and supports multiple human players sharing a terminal.

## Features

- Complete turn loop covering income, foreign aid, coup, tax, assassination, steal, and exchange actions.
- Built-in handling for challenges, blocks, and forced coups when players accumulate 10 or more coins.
- Secret influence management, including helper utilities to reveal cards only to the active player.
- Randomized deck creation that mirrors the physical game components.

## Prerequisites

- Java Development Kit (JDK) 8 or newer.

## Building

Compile all sources from the project root:

```bash
javac *.java
```

This produces the class files needed to run the game.

## Running

Running the packaged release is the preferred way to play the game and guarantees you are using the tested build that accompanies each version:

1. Visit the repository's **Releases** page and download the latest `coup-<version>.jar` asset.
2. From the download directory, launch the game with:

   ```bash
   java -jar coup-<version>.jar
   ```

   Replace `<version>` with the version number of the downloaded release.

If you prefer to build the game yourself, you can also run it directly from the compiled sources:

```bash
java Game
```

Follow the on-screen prompts to enter player information and to select actions. Unless the prompt specifies otherwise, you can decline an optional action by submitting an empty input (press Enter).

## Project Structure

- `Game.java` – Orchestrates player setup, turn order, action resolution, and overall game flow.
- `Player.java` – Represents each participant, tracking coins and influences.
- `Deck.java` and `Influence.java` – Model the Coup deck and cards with helper utilities for random draws.
- `SecretInfluenceViewer.java` – Ensures only the acting player sees their cards or exchange options.
- `ActionsPrinter.java` – Centralizes instructional text and action menus printed to the terminal.
- `StringToInt.java` – Contains safe input parsing utilities used throughout the interactive prompts.
- `coup_rulebook.pdf` – Reference copy of the official Coup rules.

## Contributing

Issues and pull requests are welcome. If you plan to submit changes, please ensure the project still compiles (`javac *.java`) before opening a pull request.

## License

This project is dedicated to the public domain under the [CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/) public-domain dedication. You can review the complete legal code on the Creative Commons website linked above.
